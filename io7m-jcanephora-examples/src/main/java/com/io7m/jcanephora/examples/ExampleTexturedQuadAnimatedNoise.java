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
import java.util.List;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateUnmapped;
import com.io7m.jcanephora.ArrayBufferUpdateUnmappedType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.ArrayDescriptorBuilderType;
import com.io7m.jcanephora.CursorWritable2fType;
import com.io7m.jcanephora.CursorWritable4fType;
import com.io7m.jcanephora.CursorWritableIndexType;
import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.IndexBufferType;
import com.io7m.jcanephora.IndexBufferUpdateUnmapped;
import com.io7m.jcanephora.IndexBufferUpdateUnmappedType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.SpatialCursorWritable3fType;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.Texture2DStaticUpdate;
import com.io7m.jcanephora.Texture2DStaticUpdateType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLImplementationVisitorType;
import com.io7m.jcanephora.api.JCGLInterfaceCommonType;
import com.io7m.jcanephora.api.JCGLInterfaceGL2Type;
import com.io7m.jcanephora.api.JCGLInterfaceGL3Type;
import com.io7m.jcanephora.api.JCGLInterfaceGLES2Type;
import com.io7m.jcanephora.api.JCGLInterfaceGLES3Type;
import com.io7m.jcanephora.utilities.ShaderUtilities;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

/**
 * Example program that draws a textured quad to the screen, with an
 * orthographic projection.
 * 
 * The texture is replaced once-per frame with new texture data.
 */

@SuppressWarnings("null") public final class ExampleTexturedQuadAnimatedNoise implements
  Example
{
  private final ArrayBufferType               array;
  private final ArrayBufferUpdateUnmappedType array_data;
  private final ArrayDescriptor               array_type;
  private final ExampleConfig                 config;
  private final JCGLImplementationType        gl;
  private boolean                             has_shut_down;
  private final IndexBufferType               indices;
  private final IndexBufferUpdateUnmappedType indices_data;
  private final MatrixM4x4F                   matrix_modelview;
  private final MatrixM4x4F                   matrix_projection;
  private final ProgramType                   program;
  private final Texture2DStaticType           texture;
  private final List<TextureUnitType>         texture_units;
  private final Texture2DStaticUpdateType     texture_update;
  private final JCGLInterfaceCommonType       glc;

  public ExampleTexturedQuadAnimatedNoise(
    final ExampleConfig config1)
    throws IOException,
      FilesystemError,
      JCGLException
  {
    this.config = config1;
    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();
    this.gl = this.config.getGL();
    this.glc = this.gl.getGLCommon();

    /**
     * Initialize shaders.
     */

    {
      final VertexShaderType v =
        this.glc.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(config1.getFilesystem().openFile(
            PathVirtual.ofString("/com/io7m/jcanephora/examples/uv.v"))));
      final FragmentShaderType f =
        this.glc.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(config1.getFilesystem().openFile(
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
        .implementationAccept(new JCGLImplementationVisitorType<Texture2DStaticType, JCGLException>() {
          @Override public Texture2DStaticType implementationIsGLES2(
            final JCGLInterfaceGLES2Type gles2)
            throws JCGLException
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

          @Override public Texture2DStaticType implementationIsGLES3(
            final JCGLInterfaceGLES3Type gles3)
            throws JCGLException
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

          @Override public Texture2DStaticType implementationIsGL2(
            final JCGLInterfaceGL2Type gl2)
            throws JCGLException
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

          @Override public Texture2DStaticType implementationIsGL3(
            final JCGLInterfaceGL3Type gl3)
            throws JCGLException
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
     * Allocate texture data.
     */

    this.texture_update = Texture2DStaticUpdate.newReplacingAll(this.texture);

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

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      4));
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "uv",
      JCGLScalarType.TYPE_FLOAT,
      2));
    this.array_type = b.build();

    this.array =
      this.glc.arrayBufferAllocate(
        4,
        this.array_type,
        UsageHint.USAGE_STATIC_DRAW);

    /**
     * Then, allocate a buffer of data that will be populated and uploaded.
     */

    this.array_data =
      ArrayBufferUpdateUnmapped.newUpdateReplacingAll(this.array);

    {
      /**
       * Obtain typed cursors to the parts of the array to be populated. Note
       * that writes to the two cursors can be interleaved. Each cursor can
       * only point to the parts of the array relevant to their attribute.
       */

      final CursorWritable4fType pos_cursor =
        this.array_data.getCursor4f("position");
      final CursorWritable2fType uv_cursor =
        this.array_data.getCursor2f("uv");

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

    this.glc.arrayBufferUpdate(this.array_data);

    /**
     * Allocate and initialize an index buffer sufficient for two triangles.
     */

    this.indices =
      this.glc
        .indexBufferAllocate(this.array, 6, UsageHint.USAGE_STATIC_DRAW);
    this.indices_data = IndexBufferUpdateUnmapped.newReplacing(this.indices);

    {
      final CursorWritableIndexType ind_cursor =
        this.indices_data.getCursor();
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
    throws JCGLException
  {
    this.glc.colorBufferClear3f(0.15f, 0.15f, 0.15f);

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
     * Generate new texture data and update the texture.
     */

    {
      final SpatialCursorWritable3fType tx_cursor =
        this.texture_update.getCursor3f();

      final VectorM3F colour = new VectorM3F();
      while (tx_cursor.isValid()) {
        final float x = tx_cursor.getElementX();
        final float y = tx_cursor.getElementY();

        final float cx = x / 64.0f;
        final float cy = (float) (Math.random() * 0.5f);
        final float cz = y / 64.0f;

        colour.set3F(cx, cy, cz);
        tx_cursor.put3f(colour);
      }

      this.glc.texture2DStaticUpdate(this.texture_update);
    }

    /**
     * Activate shading program, and associate parts of the array buffer with
     * inputs to the shader.
     */

    this.glc.programActivate(this.program);
    {
      /**
       * Get references to the program's uniform variable inputs.
       */

      final ProgramUniformType u_proj =
        this.program.programGetUniforms().get("matrix_projection");
      final ProgramUniformType u_model =
        this.program.programGetUniforms().get("matrix_modelview");
      final ProgramUniformType u_texture =
        this.program.programGetUniforms().get("texture");

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

      final ProgramAttributeType p_pos =
        this.program.programGetAttributes().get("vertex_position");
      final ProgramAttributeType p_uv =
        this.program.programGetAttributes().get("vertex_uv");

      /**
       * Get references to the array buffer's vertex attributes.
       */

      final ArrayAttributeType b_pos =
        this.array.arrayGetAttribute("position");
      final ArrayAttributeType b_uv = this.array.arrayGetAttribute("uv");

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
    final VectorReadable2IType position,
    final VectorReadable2IType size)
    throws JCGLException
  {
    ProjectionMatrix.makeOrthographicProjection(
      this.matrix_projection,
      0,
      size.getXI(),
      0,
      size.getYI(),
      1,
      100);

    final RangeInclusiveL range_x =
      new RangeInclusiveL(position.getXI(), position.getXI()
        + (size.getXI() - 1));
    final RangeInclusiveL range_y =
      new RangeInclusiveL(position.getYI(), position.getYI()
        + (size.getYI() - 1));

    this.glc.viewportSet(new AreaInclusive(range_x, range_y));
  }

  @Override public void shutdown()
    throws JCGLException
  {
    this.has_shut_down = true;
    this.glc.arrayBufferDelete(this.array);
    this.glc.indexBufferDelete(this.indices);
    this.glc.texture2DStaticDelete(this.texture);
    this.glc.programDelete(this.program);
  }
}
