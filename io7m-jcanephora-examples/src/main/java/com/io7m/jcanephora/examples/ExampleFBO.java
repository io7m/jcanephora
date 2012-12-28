package com.io7m.jcanephora.examples;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableData;
import com.io7m.jcanephora.CursorWritable2f;
import com.io7m.jcanephora.CursorWritable4f;
import com.io7m.jcanephora.CursorWritableIndex;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferAttachment;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.RenderbufferD24S8;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.MatrixM4x4F.Context;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jvvfs.PathVirtual;

public final class ExampleFBO implements Example
{
  private static final VectorReadable3F Z_AXIS;

  static {
    Z_AXIS = new VectorI3F(0.0f, 0.0f, 1.0f);
  }

  private final GLInterfaceES2          gl;
  private final RenderbufferD24S8       depth_buffer;
  private final Texture2DStatic         texture;
  private final Framebuffer             framebuffer;
  private boolean                       has_shut_down;
  private final ArrayBufferDescriptor   textured_quad_type;
  private final ArrayBuffer             textured_quad;
  private final ArrayBufferWritableData textured_quad_data;
  private final IndexBuffer             indices;
  private final IndexBufferWritableData indices_data;
  private final ExampleConfig           config;
  private final MatrixM4x4F             matrix_modelview;
  private final MatrixM4x4F             matrix_projection;
  private final Program                 program_uv;
  private final Program                 program_color;
  private final TextureUnit[]           texture_units;
  private final Context                 context;
  private float                         current_angle       = 0.0f;
  private ArrayBufferDescriptor         color_quad_type;
  private ArrayBuffer                   color_quad;
  private final ArrayBufferWritableData color_quad_data;

  private int                           framebuffer_width;
  private int                           framebuffer_height;
  private final int                     framebuffer_divisor = 8;

  public ExampleFBO(
    final @Nonnull ExampleConfig config)
    throws ConstraintError,
      GLException,
      GLCompileException
  {
    this.config = config;
    this.gl = config.getGL();
    this.context = new MatrixM4x4F.Context();
    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();

    /**
     * Initialize shaders.
     */

    this.program_uv = new Program("uv", config.getLog());
    this.program_uv.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/examples/uv.v"));
    this.program_uv.addFragmentShader(new PathVirtual(
      "/com/io7m/jcanephora/examples/uv.f"));
    this.program_uv.compile(config.getFilesystem(), this.gl);

    this.program_color = new Program("color", config.getLog());
    this.program_color.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/examples/color.v"));
    this.program_color.addFragmentShader(new PathVirtual(
      "/com/io7m/jcanephora/examples/color.f"));
    this.program_color.compile(config.getFilesystem(), this.gl);

    /**
     * Allocate and initialize a framebuffer in three steps:
     * 
     * 1. Allocate a combined depth and stencil buffer.
     * 
     * 2. Allocate a texture to act as the color buffer for the framebuffer.
     * 
     * 3. Attach the allocated buffers to a framebuffer.
     * 
     * Note that the size of the framebuffer is deliberately different to that
     * of the screen. By using a much smaller framebuffer than the screen,
     * there is clear visual aliasing that shows how the contents of the
     * framebuffer texture are being used.
     */

    this.framebuffer_width =
      config.getWindowSize().getXI() / this.framebuffer_divisor;
    this.framebuffer_height =
      config.getWindowSize().getYI() / this.framebuffer_divisor;

    this.depth_buffer =
      this.gl.renderbufferD24S8Allocate(
        this.framebuffer_width,
        this.framebuffer_height);

    this.texture =
      this.gl.texture2DStaticAllocate(
        "color_buffer",
        this.framebuffer_width,
        this.framebuffer_height,
        TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_LINEAR,
        TextureFilter.TEXTURE_FILTER_LINEAR);

    this.framebuffer =
      this.gl.framebufferAllocate(new FramebufferAttachment[] {
        new ColorAttachment(this.texture, 0),
        new RenderbufferD24S8Attachment(this.depth_buffer) });

    /**
     * Retrieve a reference to the available texture units.
     */

    this.texture_units = this.gl.textureGetUnits();

    /**
     * Allocate a pair of array buffers, one for a textured quad and one for a
     * simple colored quad.
     */

    {
      final ArrayBufferAttribute[] ab = new ArrayBufferAttribute[2];
      ab[0] =
        new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 4);
      ab[1] = new ArrayBufferAttribute("uv", GLScalarType.TYPE_FLOAT, 2);
      this.textured_quad_type = new ArrayBufferDescriptor(ab);
      this.textured_quad =
        this.gl.arrayBufferAllocate(4, this.textured_quad_type);
    }

    {
      final ArrayBufferAttribute[] ab = new ArrayBufferAttribute[2];
      ab[0] =
        new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 4);
      ab[1] = new ArrayBufferAttribute("color", GLScalarType.TYPE_FLOAT, 4);
      this.color_quad_type = new ArrayBufferDescriptor(ab);
      this.color_quad = this.gl.arrayBufferAllocate(4, this.color_quad_type);
    }

    /**
     * Populate the buffers in the usual manner.
     */

    this.textured_quad_data = new ArrayBufferWritableData(this.textured_quad);
    this.color_quad_data = new ArrayBufferWritableData(this.color_quad);

    {
      final CursorWritable4f pos_cursor =
        this.textured_quad_data.getCursor4f("position");
      final CursorWritable2f uv_cursor =
        this.textured_quad_data.getCursor2f("uv");

      pos_cursor.put4f(-100.0f, 100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(-100.0f, -100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(100.0f, -100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(100.0f, 100.0f, -1.0f, 1.0f);

      uv_cursor.put2f(0.0f, 1.0f);
      uv_cursor.put2f(0.0f, 0.0f);
      uv_cursor.put2f(1.0f, 0.0f);
      uv_cursor.put2f(1.0f, 1.0f);
    }

    {
      final CursorWritable4f pos_cursor =
        this.color_quad_data.getCursor4f("position");
      final CursorWritable4f col_cursor =
        this.color_quad_data.getCursor4f("color");

      pos_cursor.put4f(-100.0f, 100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(-100.0f, -100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(100.0f, -100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(100.0f, 100.0f, -1.0f, 1.0f);

      col_cursor.put4f(1.0f, 0.0f, 0.0f, 1.0f);
      col_cursor.put4f(0.0f, 1.0f, 0.0f, 1.0f);
      col_cursor.put4f(0.0f, 0.0f, 1.0f, 1.0f);
      col_cursor.put4f(0.0f, 0.0f, 0.0f, 1.0f);
    }

    /**
     * Upload the array data.
     */

    this.gl.arrayBufferBind(this.textured_quad);
    this.gl.arrayBufferUpdate(this.textured_quad, this.textured_quad_data);
    this.gl.arrayBufferBind(this.color_quad);
    this.gl.arrayBufferUpdate(this.color_quad, this.color_quad_data);

    /**
     * Allocate and initialize an index buffer sufficient for two triangles.
     */

    this.indices = this.gl.indexBufferAllocate(this.textured_quad, 6);
    this.indices_data = new IndexBufferWritableData(this.indices);

    {
      final CursorWritableIndex ind_cursor = this.indices_data.getCursor();
      ind_cursor.putIndex(0);
      ind_cursor.putIndex(1);
      ind_cursor.putIndex(2);

      ind_cursor.putIndex(0);
      ind_cursor.putIndex(2);
      ind_cursor.putIndex(3);
    }

    this.gl.indexBufferUpdate(this.indices, this.indices_data);
  }

  @Override public void display()
    throws GLException,
      GLCompileException,
      ConstraintError
  {
    this.drawFramebufferScene();
    this.drawActualScene();
  }

  /**
   * With a scene rendered into a framebuffer, draw a quad textured with the
   * framebuffer texture.
   */

  private void drawActualScene()
    throws ConstraintError,
      GLException
  {
    this.gl.colorBufferClear3f(0.15f, 0.15f, 0.2f);

    {
      final int width = this.config.getWindowSize().getXI();
      final int height = this.config.getWindowSize().getYI();

      /**
       * Initialize the projection matrix to an orthographic projection.
       */

      MatrixM4x4F.setIdentity(this.matrix_projection);
      ProjectionMatrix.makeOrthographic(
        this.matrix_projection,
        0,
        width,
        0,
        height,
        1,
        100);

      /**
       * Reinitialize the modelview matrix, and translate/rotate.
       */

      MatrixM4x4F.setIdentity(this.matrix_modelview);
      MatrixM4x4F.translateByVector2FInPlace(
        this.matrix_modelview,
        new VectorI2F(width / 2, height / 2));
      MatrixM4x4F.rotateInPlaceWithContext(
        this.context,
        (float) Math.toRadians(-this.current_angle),
        this.matrix_modelview,
        ExampleFBO.Z_AXIS);
    }

    /**
     * Activate shading program, and associate parts of the array buffer with
     * inputs to the shader.
     */

    this.program_uv.activate(this.gl);
    {
      /**
       * Get references to the program's uniform variable inputs.
       */

      final ProgramUniform u_proj =
        this.program_uv.getUniform("matrix_projection");
      final ProgramUniform u_model =
        this.program_uv.getUniform("matrix_modelview");
      final ProgramUniform u_texture = this.program_uv.getUniform("texture");

      /**
       * Upload the matrices to the uniform variable inputs.
       */

      this.gl.programPutUniformMatrix4x4f(u_proj, this.matrix_projection);
      this.gl.programPutUniformMatrix4x4f(u_model, this.matrix_modelview);

      /**
       * Bind the framebuffer texture to the first available texture unit,
       * then upload the texture unit reference to the shader.
       */

      this.gl.texture2DStaticBind(this.texture_units[0], this.texture);
      this.gl.programPutUniformTextureUnit(u_texture, this.texture_units[0]);

      /**
       * Get references to the program's vertex attribute inputs.
       */

      final ProgramAttribute p_pos =
        this.program_uv.getAttribute("vertex_position");
      final ProgramAttribute p_uv = this.program_uv.getAttribute("vertex_uv");

      /**
       * Get references to the array buffer's vertex attributes.
       */

      final ArrayBufferAttribute b_pos =
        this.textured_quad_type.getAttribute("position");
      final ArrayBufferAttribute b_uv =
        this.textured_quad_type.getAttribute("uv");

      /**
       * Bind the array buffer, and associate program vertex attribute inputs
       * with array vertex attributes.
       */

      this.gl.arrayBufferBind(this.textured_quad);
      this.gl
        .arrayBufferBindVertexAttribute(this.textured_quad, b_pos, p_pos);
      this.gl.arrayBufferBindVertexAttribute(this.textured_quad, b_uv, p_uv);

      /**
       * Draw primitives, using the array buffer and the given index buffer.
       */

      this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
      this.gl.arrayBufferUnbind();
    }
    this.program_uv.deactivate(this.gl);
  }

  /**
   * Render a scene into the allocated framebuffer. The scene is a simple
   * rotating quad.
   */

  private void drawFramebufferScene()
    throws ConstraintError,
      GLException
  {
    final int width = this.config.getWindowSize().getXI();
    final int height = this.config.getWindowSize().getYI();

    /**
     * Initialize the projection matrix to an orthographic projection.
     */

    MatrixM4x4F.setIdentity(this.matrix_projection);
    ProjectionMatrix.makeOrthographic(
      this.matrix_projection,
      0,
      width * this.framebuffer_divisor,
      0,
      height * this.framebuffer_divisor,
      1,
      100);

    /**
     * Switch to using the allocated framebuffer, and render a scene.
     */

    this.gl.framebufferBind(this.framebuffer);
    {
      this.gl.colorBufferClear3f(1.0f, 1.0f, 1.0f);
      this.current_angle = (float) ((this.current_angle + 1.0) % 360.0);

      MatrixM4x4F.setIdentity(this.matrix_modelview);
      MatrixM4x4F.translateByVector2FInPlace(
        this.matrix_modelview,
        new VectorI2F(width / 2, height / 2));
      MatrixM4x4F.rotateInPlaceWithContext(
        this.context,
        (float) Math.toRadians(this.current_angle),
        this.matrix_modelview,
        ExampleFBO.Z_AXIS);

      /**
       * Switch to color shader, draw quad.
       */

      this.program_color.activate(this.gl);
      {
        /**
         * Get references to the program's uniform variable inputs.
         */

        final ProgramUniform u_proj =
          this.program_color.getUniform("matrix_projection");
        final ProgramUniform u_model =
          this.program_color.getUniform("matrix_modelview");

        /**
         * Upload the matrices to the uniform variable inputs.
         */

        this.gl.programPutUniformMatrix4x4f(u_proj, this.matrix_projection);
        this.gl.programPutUniformMatrix4x4f(u_model, this.matrix_modelview);

        /**
         * Get references to the program's vertex attribute inputs.
         */

        final ProgramAttribute p_pos =
          this.program_color.getAttribute("vertex_position");
        final ProgramAttribute p_col =
          this.program_color.getAttribute("vertex_color");

        /**
         * Get references to the array buffer's vertex attributes.
         */

        final ArrayBufferAttribute b_pos =
          this.color_quad_type.getAttribute("position");
        final ArrayBufferAttribute b_col =
          this.color_quad_type.getAttribute("color");

        /**
         * Bind the array buffer, and associate program vertex attribute
         * inputs with array vertex attributes.
         */

        this.gl.arrayBufferBind(this.color_quad);
        this.gl.arrayBufferBindVertexAttribute(this.color_quad, b_pos, p_pos);
        this.gl.arrayBufferBindVertexAttribute(this.color_quad, b_col, p_col);

        /**
         * Draw primitives, using the array buffer and the given index buffer.
         */

        this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
        this.gl.arrayBufferUnbind();
      }
      this.program_color.deactivate(this.gl);
    }
    this.gl.framebufferUnbind();
  }

  @Override public boolean hasShutDown()
  {
    return this.has_shut_down;
  }

  @Override public void reshape(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I size)
    throws GLException,
      ConstraintError,
      GLCompileException
  {
    this.gl.viewportSet(position, size);

    this.framebuffer_width =
      this.config.getWindowSize().getXI() / this.framebuffer_divisor;
    this.framebuffer_height =
      this.config.getWindowSize().getYI() / this.framebuffer_divisor;
  }

  @Override public void shutdown()
    throws GLException,
      ConstraintError,
      GLCompileException
  {
    this.has_shut_down = true;
    this.color_quad.resourceDelete(this.gl);
    this.textured_quad.resourceDelete(this.gl);
    this.framebuffer.resourceDelete(this.gl);
    this.depth_buffer.resourceDelete(this.gl);
    this.texture.resourceDelete(this.gl);
    this.program_color.delete(this.gl);
    this.program_uv.delete(this.gl);
  }
}
