/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcanephora.examples;

import java.io.IOException;
import java.util.HashMap;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Indeterminate;
import com.io7m.jaux.functional.Indeterminate.Failure;
import com.io7m.jaux.functional.Indeterminate.Success;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferTypeDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableData;
import com.io7m.jcanephora.AttachmentColor;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTexture2DStatic;
import com.io7m.jcanephora.CursorWritable2f;
import com.io7m.jcanephora.CursorWritable4f;
import com.io7m.jcanephora.CursorWritableIndex;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferColorAttachmentPoint;
import com.io7m.jcanephora.FramebufferConfigurationGL3;
import com.io7m.jcanephora.FramebufferConfigurationGL3Actual;
import com.io7m.jcanephora.FramebufferDrawBuffer;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLImplementation;
import com.io7m.jcanephora.JCGLInterfaceGL3;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.ShaderUtilities;
import com.io7m.jcanephora.Texture2DStaticUsable;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShader;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.MatrixM4x4F.Context;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public final class ExampleFBO3 implements Example
{
  private static final VectorReadable3F           Z_AXIS;

  static {
    Z_AXIS = new VectorI3F(0.0f, 0.0f, 1.0f);
  }

  private final JCGLImplementation                gl_implementation;
  private final JCGLInterfaceGL3                  gl;
  private boolean                                 supported;

  private final Texture2DStaticUsable             texture;
  private final Framebuffer                       framebuffer;
  private boolean                                 has_shut_down;
  private final ArrayBufferTypeDescriptor         textured_quad_type;
  private final ArrayBuffer                       textured_quad;
  private final ArrayBufferWritableData           textured_quad_data;
  private final IndexBuffer                       indices;
  private final IndexBufferWritableData           indices_data;
  private final ExampleConfig                     config;
  private final MatrixM4x4F                       matrix_modelview;
  private final MatrixM4x4F                       matrix_projection;
  private final ProgramReference                  program_uv;
  private final ProgramReference                  program_color;
  private final TextureUnit[]                     texture_units;
  private final FramebufferColorAttachmentPoint[] framebuffer_color_points;
  private final Context                           context;
  private float                                   current_angle       = 0.0f;
  private ArrayBufferTypeDescriptor               color_quad_type;
  private ArrayBuffer                             color_quad;
  private final ArrayBufferWritableData           color_quad_data;

  private int                                     framebuffer_width;
  private int                                     framebuffer_height;
  private final int                               framebuffer_divisor = 8;
  private final FramebufferConfigurationGL3       framebuffer_config;
  private final FramebufferDrawBuffer[]           framebuffer_draw_buffers;
  private HashMap<String, ProgramUniform>         program_uv_uniforms;
  private HashMap<String, ProgramAttribute>       program_uv_attributes;
  private HashMap<String, ProgramUniform>         program_color_uniforms;
  private HashMap<String, ProgramAttribute>       program_color_attributes;

  public ExampleFBO3(
    final @Nonnull ExampleConfig config)
    throws ConstraintError,
      JCGLException,
      JCGLCompileException,
      IOException,
      FilesystemError
  {
    this.config = config;
    this.context = new MatrixM4x4F.Context();
    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();
    this.gl_implementation = this.config.getGL();

    /**
     * This example only works on OpenGL 3.*
     * 
     * If given an implementation that does not support OpenGL 3.*, the
     * example simply prints a message and does nothing.
     */

    switch (this.gl_implementation.getGL3().type) {
      case OPTION_SOME:
        this.gl =
          ((Some<JCGLInterfaceGL3>) this.gl_implementation.getGL3()).value;
        this.supported = true;
        break;
      case OPTION_NONE:
      default:
        config.getLog().error("Example not supported on this implementation");
        this.supported = false;
        this.color_quad = null;
        this.color_quad_data = null;
        this.framebuffer_color_points = null;
        this.framebuffer_draw_buffers = null;
        this.framebuffer_config = null;
        this.textured_quad = null;
        this.textured_quad_type = null;
        this.textured_quad_data = null;
        this.texture_units = null;
        this.texture = null;
        this.program_uv = null;
        this.program_color = null;
        this.indices = null;
        this.indices_data = null;
        this.gl = null;
        this.framebuffer = null;
        return;
    }

    /**
     * Initialize shaders.
     */

    {
      final VertexShader v =
        this.gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(config.getFilesystem().openFile(
            PathVirtual.ofString("/com/io7m/jcanephora/examples/color.v"))));
      final FragmentShader f =
        this.gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(config.getFilesystem().openFile(
            PathVirtual.ofString("/com/io7m/jcanephora/examples/color.f"))));
      this.program_color = this.gl.programCreateCommon("color", v, f);
      this.program_color_uniforms = new HashMap<String, ProgramUniform>();
      this.program_color_attributes = new HashMap<String, ProgramAttribute>();
      this.gl.programGetAttributes(
        this.program_color,
        this.program_color_attributes);
      this.gl.programGetUniforms(
        this.program_color,
        this.program_color_uniforms);
    }

    {
      final VertexShader v =
        this.gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(config.getFilesystem().openFile(
            PathVirtual.ofString("/com/io7m/jcanephora/examples/uv.v"))));
      final FragmentShader f =
        this.gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(config.getFilesystem().openFile(
            PathVirtual.ofString("/com/io7m/jcanephora/examples/uv.f"))));
      this.program_uv = this.gl.programCreateCommon("uv", v, f);
      this.program_uv_uniforms = new HashMap<String, ProgramUniform>();
      this.program_uv_attributes = new HashMap<String, ProgramAttribute>();
      this.gl.programGetAttributes(
        this.program_uv,
        this.program_uv_attributes);
      this.gl.programGetUniforms(this.program_uv, this.program_uv_uniforms);
    }

    /**
     * Allocate and initialize a framebuffer using the high level
     * {@link Framebuffer} API.
     */

    /**
     * The size of the requested framebuffer is deliberately different to that
     * of the screen. By using a much smaller framebuffer than the screen,
     * there is clear visual aliasing that shows how the contents of the
     * framebuffer texture are being used.
     */

    this.framebuffer_width =
      config.getWindowSize().getXI() / this.framebuffer_divisor;
    this.framebuffer_height =
      config.getWindowSize().getYI() / this.framebuffer_divisor;

    /**
     * Create a new GL3-compatible configuration.
     * 
     * When the term "GL3-compatible" is used, it's intended to mean that this
     * framebuffer will work on OpenGL 3.0 (or something that is 3.0
     * compatible via the use of extensions).
     * 
     * Note that color attachment points and draw buffers are represented by
     * concrete objects (as opposed to being integer indices in OpenGL). This
     * prevents a program from using more attachment points or draw buffers
     * than are actually available from the underlying implementation.
     */

    this.framebuffer_color_points =
      this.gl.framebufferGetColorAttachmentPoints();
    this.framebuffer_draw_buffers = this.gl.framebufferGetDrawBuffers();

    this.framebuffer_config =
      new FramebufferConfigurationGL3Actual(
        this.framebuffer_width,
        this.framebuffer_height);

    /**
     * Request a color buffer backed by a 2D texture, using the best quality
     * texture available on the current implementation. This will inevitably
     * be RGBA8888.
     * 
     * The color buffer is attached at attachment point 0, and draw buffer 0
     * is configured to send output there. That is, anything writing to
     * <code>gl_FragColor</code>, <code>gl_FragData[0]</code>, or an
     * <code>out</code> variable in a fragment shader set to layout
     * <code>0</code>, will be written to the created color buffer.
     */

    this.framebuffer_config.requestBestRGBAColorTexture2D(
      this.framebuffer_color_points[0],
      this.framebuffer_draw_buffers[0],
      TextureWrapS.TEXTURE_WRAP_REPEAT,
      TextureWrapT.TEXTURE_WRAP_REPEAT,
      TextureFilterMinification.TEXTURE_FILTER_NEAREST,
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    /**
     * Request a depth buffer. Most implementations will provide a packed
     * depth/stencil buffer and nothing else.
     */

    this.framebuffer_config.requestDepthRenderbuffer();

    /**
     * Construct the framebuffer.
     * 
     * Constructing the framebuffer will return either a validated
     * framebuffer, or a status value indicating why the framebuffer could not
     * be constructed.
     */

    final Indeterminate<Framebuffer, FramebufferStatus> result =
      this.framebuffer_config.make(this.gl);

    if (result.isFailure()) {
      final Failure<Framebuffer, FramebufferStatus> failure =
        (Failure<Framebuffer, FramebufferStatus>) result;
      throw new JCGLException(0, "Could not create framebuffer: "
        + failure.value);
    }

    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    this.framebuffer = success.value;

    /**
     * Retrieve the texture at attachment point 0.
     */

    final AttachmentColor a =
      this.framebuffer.getColorAttachment(this.framebuffer_color_points[0]);

    /**
     * Inspect the type of the color attachment. As an RGBA texture was
     * requested, it can only be one of the possible cases.
     */

    switch (a.type) {
      case ATTACHMENT_COLOR_RENDERBUFFER:
      case ATTACHMENT_COLOR_TEXTURE_CUBE:
      case ATTACHMENT_SHARED_COLOR_RENDERBUFFER:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_2D:
      case ATTACHMENT_SHARED_COLOR_TEXTURE_CUBE:
        throw new UnreachableCodeException();
      case ATTACHMENT_COLOR_TEXTURE_2D:
        break;
    }

    final AttachmentColorTexture2DStatic at =
      (AttachmentColorTexture2DStatic) a;
    this.texture = at.getTexture2D();

    /**
     * Retrieve a reference to the available texture units.
     */

    this.texture_units = this.gl.textureGetUnits();

    /**
     * Allocate a pair of array buffers, one for a textured quad and one for a
     * simple colored quad.
     */

    {
      final ArrayBufferAttributeDescriptor[] ab =
        new ArrayBufferAttributeDescriptor[2];
      ab[0] =
        new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          4);
      ab[1] =
        new ArrayBufferAttributeDescriptor("uv", JCGLScalarType.TYPE_FLOAT, 2);
      this.textured_quad_type = new ArrayBufferTypeDescriptor(ab);
      this.textured_quad =
        this.gl.arrayBufferAllocate(
          4,
          this.textured_quad_type,
          UsageHint.USAGE_STATIC_DRAW);
    }

    {
      final ArrayBufferAttributeDescriptor[] ab =
        new ArrayBufferAttributeDescriptor[2];
      ab[0] =
        new ArrayBufferAttributeDescriptor(
          "position",
          JCGLScalarType.TYPE_FLOAT,
          4);
      ab[1] =
        new ArrayBufferAttributeDescriptor(
          "color",
          JCGLScalarType.TYPE_FLOAT,
          4);
      this.color_quad_type = new ArrayBufferTypeDescriptor(ab);
      this.color_quad =
        this.gl.arrayBufferAllocate(
          4,
          this.color_quad_type,
          UsageHint.USAGE_STATIC_DRAW);
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
    this.gl.arrayBufferUpdate(this.textured_quad_data);
    this.gl.arrayBufferBind(this.color_quad);
    this.gl.arrayBufferUpdate(this.color_quad_data);

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

    this.gl.indexBufferUpdate(this.indices_data);
  }

  @Override public void display()
    throws JCGLException,
      JCGLCompileException,
      ConstraintError
  {
    if (this.supported) {
      this.drawFramebufferScene();
      this.drawActualScene();
    }
  }

  /**
   * With a scene rendered into a framebuffer, draw a quad textured with the
   * framebuffer texture.
   */

  private void drawActualScene()
    throws ConstraintError,
      JCGLException
  {
    this.gl.colorBufferClear3f(0.15f, 0.3f, 0.2f);

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
        ExampleFBO3.Z_AXIS);
    }

    /**
     * Activate shading program, and associate parts of the array buffer with
     * inputs to the shader.
     */

    this.gl.programActivate(this.program_uv);
    {
      /**
       * Get references to the program's uniform variable inputs.
       */

      final ProgramUniform u_proj =
        this.program_uv_uniforms.get("matrix_projection");
      final ProgramUniform u_model =
        this.program_uv_uniforms.get("matrix_modelview");
      final ProgramUniform u_texture =
        this.program_uv_uniforms.get("texture");

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
        this.program_uv_attributes.get("vertex_position");
      final ProgramAttribute p_uv =
        this.program_uv_attributes.get("vertex_uv");

      /**
       * Get references to the array buffer's vertex attributes.
       */

      final ArrayBufferAttribute b_pos =
        this.textured_quad.getAttribute("position");
      final ArrayBufferAttribute b_uv = this.textured_quad.getAttribute("uv");

      /**
       * Bind the array buffer, and associate program vertex attribute inputs
       * with array vertex attributes.
       */

      this.gl.arrayBufferBind(this.textured_quad);
      this.gl.arrayBufferBindVertexAttribute(b_pos, p_pos);
      this.gl.arrayBufferBindVertexAttribute(b_uv, p_uv);

      /**
       * Draw primitives, using the array buffer and the given index buffer.
       */

      this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
      this.gl.arrayBufferUnbind();
    }
    this.gl.programDeactivate();
  }

  /**
   * Render a scene into the allocated framebuffer. The scene is a simple
   * rotating quad.
   */

  private void drawFramebufferScene()
    throws ConstraintError,
      JCGLException
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

    this.gl.framebufferDrawBind(this.framebuffer.getFramebuffer());
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
        ExampleFBO3.Z_AXIS);

      /**
       * Switch to color shader, draw quad.
       */

      this.gl.programActivate(this.program_color);
      {
        /**
         * Get references to the program's uniform variable inputs.
         */

        final ProgramUniform u_proj =
          this.program_color_uniforms.get("matrix_projection");
        final ProgramUniform u_model =
          this.program_color_uniforms.get("matrix_modelview");

        /**
         * Upload the matrices to the uniform variable inputs.
         */

        this.gl.programPutUniformMatrix4x4f(u_proj, this.matrix_projection);
        this.gl.programPutUniformMatrix4x4f(u_model, this.matrix_modelview);

        /**
         * Get references to the program's vertex attribute inputs.
         */

        final ProgramAttribute p_pos =
          this.program_color_attributes.get("vertex_position");
        final ProgramAttribute p_col =
          this.program_color_attributes.get("vertex_color");

        /**
         * Get references to the array buffer's vertex attributes.
         */

        final ArrayBufferAttribute b_pos =
          this.color_quad.getAttribute("position");
        final ArrayBufferAttribute b_col =
          this.color_quad.getAttribute("color");

        /**
         * Bind the array buffer, and associate program vertex attribute
         * inputs with array vertex attributes.
         */

        this.gl.arrayBufferBind(this.color_quad);
        this.gl.arrayBufferBindVertexAttribute(b_pos, p_pos);
        this.gl.arrayBufferBindVertexAttribute(b_col, p_col);

        /**
         * Draw primitives, using the array buffer and the given index buffer.
         */

        this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
        this.gl.arrayBufferUnbind();
      }
      this.gl.programDeactivate();
    }
    this.gl.framebufferDrawUnbind();
  }

  @Override public boolean hasShutDown()
  {
    return this.has_shut_down;
  }

  @Override public void reshape(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I size)
    throws JCGLException,
      ConstraintError,
      JCGLCompileException
  {
    if (this.supported) {
      this.gl.viewportSet(position, size);

      this.framebuffer_width =
        this.config.getWindowSize().getXI() / this.framebuffer_divisor;
      this.framebuffer_height =
        this.config.getWindowSize().getYI() / this.framebuffer_divisor;
    }
  }

  @Override public void shutdown()
    throws JCGLException,
      ConstraintError,
      JCGLCompileException
  {
    if (this.supported) {
      this.has_shut_down = true;
      this.gl.arrayBufferDelete(this.color_quad);
      this.gl.arrayBufferDelete(this.textured_quad);
      this.framebuffer.delete(this.gl);
      this.gl.programDelete(this.program_color);
      this.gl.programDelete(this.program_uv);
    }
  }
}
