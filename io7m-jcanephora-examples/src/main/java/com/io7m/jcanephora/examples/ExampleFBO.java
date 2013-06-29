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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Indeterminate;
import com.io7m.jaux.functional.Indeterminate.Failure;
import com.io7m.jaux.functional.Indeterminate.Success;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableData;
import com.io7m.jcanephora.AttachmentColor;
import com.io7m.jcanephora.AttachmentColor.AttachmentColorTexture2DStatic;
import com.io7m.jcanephora.CursorWritable2f;
import com.io7m.jcanephora.CursorWritable4f;
import com.io7m.jcanephora.CursorWritableIndex;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferColorAttachmentPoint;
import com.io7m.jcanephora.FramebufferConfigurationGLES2;
import com.io7m.jcanephora.FramebufferConfigurationGLES2Actual;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLImplementation;
import com.io7m.jcanephora.GLInterfaceCommon;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.Texture2DStaticUsable;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.MatrixM4x4F.Context;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorI3F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jvvfs.PathVirtual;

public final class ExampleFBO implements Example
{
  private static final VectorReadable3F           Z_AXIS;

  static {
    Z_AXIS = new VectorI3F(0.0f, 0.0f, 1.0f);
  }

  private final GLImplementation                  gli;
  private final GLInterfaceCommon                 gl;
  private final Texture2DStaticUsable             texture;
  private final Framebuffer                       framebuffer;
  private boolean                                 has_shut_down;
  private final ArrayBufferDescriptor             textured_quad_type;
  private final ArrayBuffer                       textured_quad;
  private final ArrayBufferWritableData           textured_quad_data;
  private final IndexBuffer                       indices;
  private final IndexBufferWritableData           indices_data;
  private final ExampleConfig                     config;
  private final MatrixM4x4F                       matrix_modelview;
  private final MatrixM4x4F                       matrix_projection;
  private final Program                           program_uv;
  private final Program                           program_color;
  private final TextureUnit[]                     texture_units;
  private final FramebufferColorAttachmentPoint[] framebuffer_color_points;
  private final Context                           context;
  private float                                   current_angle       = 0.0f;
  private ArrayBufferDescriptor                   color_quad_type;
  private ArrayBuffer                             color_quad;
  private final ArrayBufferWritableData           color_quad_data;

  private int                                     framebuffer_width;
  private int                                     framebuffer_height;
  private final int                               framebuffer_divisor = 8;
  private final FramebufferConfigurationGLES2     framebuffer_config;

  public ExampleFBO(
    final @Nonnull ExampleConfig config)
    throws ConstraintError,
      GLException,
      GLCompileException
  {
    this.config = config;
    this.gli = config.getGL();
    this.gl = this.gli.getGLCommon();
    this.context = new MatrixM4x4F.Context();
    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();

    /**
     * Initialize shaders.
     */

    this.program_uv = new Program("uv", config.getLog());
    this.program_uv.addVertexShader(PathVirtual
      .ofString("/com/io7m/jcanephora/examples/uv.v"));
    this.program_uv.addFragmentShader(PathVirtual
      .ofString("/com/io7m/jcanephora/examples/uv.f"));
    this.program_uv.compile(config.getFilesystem(), this.gl);

    this.program_color = new Program("color", config.getLog());
    this.program_color.addVertexShader(PathVirtual
      .ofString("/com/io7m/jcanephora/examples/color.v"));
    this.program_color.addFragmentShader(PathVirtual
      .ofString("/com/io7m/jcanephora/examples/color.f"));
    this.program_color.compile(config.getFilesystem(), this.gl);

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
     * Create a new ES2-compatible configuration.
     * 
     * When the term "ES2-compatible" is used, it's intended to mean that this
     * framebuffer will work equally well on ES2 and full OpenGL 3.0. It's not
     * possible to configure the framebuffer in such a way that it would not
     * work correctly when running on a real ES2 implementation.
     */

    this.framebuffer_config =
      new FramebufferConfigurationGLES2Actual(
        this.framebuffer_width,
        this.framebuffer_height);

    /**
     * Request a color buffer backed by a 2D texture, using the best quality
     * texture available on the current implementation. This will probably be
     * RGB8888 on OpenGL 3.0, and RGBA4444 on ES2.
     */

    this.framebuffer_config.requestBestRGBAColorTexture2D(
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
      this.framebuffer_config.make(this.gli);

    if (result.isFailure()) {
      final Failure<Framebuffer, FramebufferStatus> failure =
        (Failure<Framebuffer, FramebufferStatus>) result;
      throw new GLException(0, "Could not create framebuffer: "
        + failure.value);
    }

    final Success<Framebuffer, FramebufferStatus> success =
      (Success<Framebuffer, FramebufferStatus>) result;

    this.framebuffer = success.value;

    /**
     * Retrieve the texture at attachment point 0.
     * 
     * ES2-compatible framebuffers only have one color attachment point.
     */

    this.framebuffer_color_points =
      this.gl.framebufferGetColorAttachmentPoints();

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
      final ArrayBufferAttribute[] ab = new ArrayBufferAttribute[2];
      ab[0] =
        new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 4);
      ab[1] = new ArrayBufferAttribute("uv", GLScalarType.TYPE_FLOAT, 2);
      this.textured_quad_type = new ArrayBufferDescriptor(ab);
      this.textured_quad =
        this.gl.arrayBufferAllocate(
          4,
          this.textured_quad_type,
          UsageHint.USAGE_STATIC_DRAW);
    }

    {
      final ArrayBufferAttribute[] ab = new ArrayBufferAttribute[2];
      ab[0] =
        new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 4);
      ab[1] = new ArrayBufferAttribute("color", GLScalarType.TYPE_FLOAT, 4);
      this.color_quad_type = new ArrayBufferDescriptor(ab);
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
    this.gl.framebufferDrawUnbind();
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

    this.gl.arrayBufferDelete(this.color_quad);
    this.gl.arrayBufferDelete(this.textured_quad);
    this.framebuffer.delete(this.gl);
    this.program_color.delete(this.gl);
    this.program_uv.delete(this.gl);
  }
}
