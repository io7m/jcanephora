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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferTypeDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableData;
import com.io7m.jcanephora.CursorWritable2f;
import com.io7m.jcanephora.CursorWritable4f;
import com.io7m.jcanephora.CursorWritableIndex;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLImplementation;
import com.io7m.jcanephora.JCGLImplementationVisitor;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLInterfaceGL2;
import com.io7m.jcanephora.JCGLInterfaceGL3;
import com.io7m.jcanephora.JCGLInterfaceGLES2;
import com.io7m.jcanephora.JCGLInterfaceGLES3;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.ShaderUtilities;
import com.io7m.jcanephora.SpatialCursorWritable3f;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.Texture2DWritableData;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShader;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

/**
 * Example program that draws a textured quad to the screen, with an
 * orthographic projection.
 */

public final class ExampleTexturedQuad implements Example
{
  private final ArrayBuffer               array;
  private final ArrayBufferWritableData   array_data;
  private final ArrayBufferTypeDescriptor array_type;
  private final ExampleConfig             config;
  private final JCGLInterfaceCommon       glc;
  private boolean                         has_shut_down;
  private final IndexBuffer               indices;
  private final IndexBufferWritableData   indices_data;
  private final MatrixM4x4F               matrix_modelview;
  private final MatrixM4x4F               matrix_projection;
  private final ProgramReference          program;
  private final Texture2DStatic           texture;
  private final List<TextureUnit>         texture_units;
  private final Texture2DWritableData     texture_update;
  private final JCGLImplementation        gl;

  public ExampleTexturedQuad(
    final @Nonnull ExampleConfig config)
    throws ConstraintError,
      JCGLException,
      IOException,
      FilesystemError
  {
    this.config = config;
    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();
    this.gl = this.config.getGL();
    this.glc = this.gl.getGLCommon();

    /**
     * Initialize shaders.
     */

    {
      final VertexShader v =
        this.glc.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(config.getFilesystem().openFile(
            PathVirtual.ofString("/com/io7m/jcanephora/examples/uv.v"))));
      final FragmentShader f =
        this.glc.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(config.getFilesystem().openFile(
            PathVirtual.ofString("/com/io7m/jcanephora/examples/uv.f"))));
      this.program = this.glc.programCreateCommon("color", v, f);
    }

    /**
     * Obtain access to the available texture units.
     */

    this.texture_units = this.glc.textureGetUnits();

    /**
     * Allocate a texture.
     */

    this.texture =
      this.gl
        .implementationAccept(new JCGLImplementationVisitor<Texture2DStatic, JCGLException>() {
          @Override public Texture2DStatic implementationIsGLES2(
            final JCGLInterfaceGLES2 gles2)
            throws JCGLException,
              ConstraintError,
              JCGLException
          {
            return gles2.texture2DStaticAllocateRGB565(
              "gradient",
              64,
              64,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          }

          @Override public Texture2DStatic implementationIsGLES3(
            final JCGLInterfaceGLES3 gles3)
            throws JCGLException,
              ConstraintError,
              JCGLException
          {
            return gles3.texture2DStaticAllocateRGB8(
              "gradient",
              64,
              64,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          }

          @Override public Texture2DStatic implementationIsGL2(
            final JCGLInterfaceGL2 gl2)
            throws JCGLException,
              ConstraintError,
              JCGLException
          {
            return gl2.texture2DStaticAllocateRGB8(
              "gradient",
              64,
              64,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          }

          @Override public Texture2DStatic implementationIsGL3(
            final JCGLInterfaceGL3 gl3)
            throws JCGLException,
              ConstraintError,
              JCGLException
          {
            return gl3.texture2DStaticAllocateRGB8(
              "gradient",
              64,
              64,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
          }
        });

    /**
     * Allocate texture data and populate it using the cursor interface.
     */

    this.texture_update = new Texture2DWritableData(this.texture);

    {
      final SpatialCursorWritable3f tx_cursor =
        this.texture_update.getCursor3f();

      final VectorM3F colour = new VectorM3F();
      while (tx_cursor.isValid()) {
        final float x = tx_cursor.getElementX();
        final float y = tx_cursor.getElementY();
        colour.x = x / 64.0f;
        colour.y = (float) (Math.random() * 0.5f);
        colour.z = y / 64.0f;
        tx_cursor.put3f(colour);
      }

      this.glc.texture2DStaticUpdate(this.texture_update);
    }

    /**
     * Allocate an array buffer.
     * 
     * Set up a type descriptor that describes the types of elements within
     * the array. In this case, each element of the array is a series of four
     * floats representing the position of a vertex, followed by a series of
     * two floats representing the texture coordinates of a vertex.
     * 
     * Then, use this descriptor to allocate an array.
     */

    final ArrayList<ArrayBufferAttributeDescriptor> abs =
      new ArrayList<ArrayBufferAttributeDescriptor>();
    abs.add(new ArrayBufferAttributeDescriptor(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      4));
    abs.add(new ArrayBufferAttributeDescriptor(
      "uv",
      JCGLScalarType.TYPE_FLOAT,
      2));

    this.array_type = new ArrayBufferTypeDescriptor(abs);
    this.array =
      this.glc.arrayBufferAllocate(
        4,
        this.array_type,
        UsageHint.USAGE_STATIC_DRAW);

    /**
     * Then, allocate a buffer of data that will be populated and uploaded.
     */

    this.array_data = new ArrayBufferWritableData(this.array);

    {
      /**
       * Obtain typed cursors to the parts of the array to be populated. Note
       * that writes to the two cursors can be interleaved. Each cursor can
       * only point to the parts of the array relevant to their attribute.
       */

      final CursorWritable4f pos_cursor =
        this.array_data.getCursor4f("position");
      final CursorWritable2f uv_cursor = this.array_data.getCursor2f("uv");

      pos_cursor.put4f(-100.0f, 100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(-100.0f, -100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(100.0f, -100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(100.0f, 100.0f, -1.0f, 1.0f);

      uv_cursor.put2f(0.0f, 1.0f);
      uv_cursor.put2f(0.0f, 0.0f);
      uv_cursor.put2f(1.0f, 0.0f);
      uv_cursor.put2f(1.0f, 1.0f);
    }

    /**
     * Upload the array data.
     */

    this.glc.arrayBufferBind(this.array);
    this.glc.arrayBufferUpdate(this.array_data);

    /**
     * Allocate and initialize an index buffer sufficient for two triangles.
     */

    this.indices = this.glc.indexBufferAllocate(this.array, 6);
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

    this.glc.indexBufferUpdate(this.indices_data);
  }

  @Override public void display()
    throws JCGLRuntimeException,
      JCGLCompileException,
      ConstraintError
  {
    this.glc.colorBufferClear3f(0.2f, 0.15f, 0.15f);

    /**
     * Initialize the projection matrix to an orthographic projection.
     */

    MatrixM4x4F.setIdentity(this.matrix_projection);
    ProjectionMatrix.makeOrthographicProjection(
      this.matrix_projection,
      0,
      640,
      0,
      480,
      1,
      100);

    /**
     * Initialize the modelview matrix, and translate.
     */

    MatrixM4x4F.setIdentity(this.matrix_modelview);
    MatrixM4x4F.translateByVector2FInPlace(
      this.matrix_modelview,
      new VectorI2F(this.config.getWindowSize().getXI() / 2, this.config
        .getWindowSize()
        .getYI() / 2));

    /**
     * Activate shading program, and associate parts of the array buffer with
     * inputs to the shader.
     */

    this.glc.programActivate(this.program);
    {
      /**
       * Get references to the program's uniform variable inputs.
       */

      final ProgramUniform u_proj =
        this.program.getUniforms().get("matrix_projection");
      final ProgramUniform u_model =
        this.program.getUniforms().get("matrix_modelview");
      final ProgramUniform u_texture =
        this.program.getUniforms().get("texture");

      /**
       * Upload the matrices to the uniform variable inputs.
       */

      this.glc.programUniformPutMatrix4x4f(u_proj, this.matrix_projection);
      this.glc.programUniformPutMatrix4x4f(u_model, this.matrix_modelview);

      /**
       * Bind the texture to the first available texture unit, then upload the
       * texture unit reference to the shader.
       */

      this.glc.texture2DStaticBind(this.texture_units.get(0), this.texture);
      this.glc.programUniformPutTextureUnit(
        u_texture,
        this.texture_units.get(0));

      /**
       * Get references to the program's vertex attribute inputs.
       */

      final ProgramAttribute p_pos =
        this.program.getAttributes().get("vertex_position");
      final ProgramAttribute p_uv =
        this.program.getAttributes().get("vertex_uv");

      /**
       * Get references to the array buffer's vertex attributes.
       */

      final ArrayBufferAttribute b_pos = this.array.getAttribute("position");
      final ArrayBufferAttribute b_uv = this.array.getAttribute("uv");

      /**
       * Bind the array buffer, and associate program vertex attribute inputs
       * with array vertex attributes.
       */

      this.glc.arrayBufferBind(this.array);
      this.glc.programAttributeArrayAssociate(p_uv, b_uv);
      this.glc.programAttributeArrayAssociate(p_pos, b_pos);

      /**
       * Draw primitives, using the array buffer and the given index buffer.
       */

      this.glc.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
      this.glc.programAttributeArrayDisassociate(p_pos);
      this.glc.programAttributeArrayDisassociate(p_uv);
      this.glc.arrayBufferUnbind();
    }
    this.glc.programDeactivate();
  }

  @Override public boolean hasShutDown()
  {
    return this.has_shut_down;
  }

  @Override public void reshape(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I size)
    throws JCGLRuntimeException,
      ConstraintError,
      JCGLCompileException
  {
    ProjectionMatrix.makeOrthographicProjection(
      this.matrix_projection,
      0,
      size.getXI(),
      0,
      size.getYI(),
      1,
      100);

    this.glc.viewportSet(position, size);
  }

  @Override public void shutdown()
    throws JCGLRuntimeException,
      ConstraintError,
      JCGLCompileException
  {
    this.has_shut_down = true;
    this.glc.arrayBufferDelete(this.array);
    this.glc.indexBufferDelete(this.indices);
    this.glc.programDelete(this.program);
  }
}
