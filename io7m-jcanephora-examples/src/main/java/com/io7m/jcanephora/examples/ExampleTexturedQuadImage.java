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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferTypeDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableData;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.CursorWritable2f;
import com.io7m.jcanephora.CursorWritable4f;
import com.io7m.jcanephora.CursorWritableIndex;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.ShaderUtilities;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureLoader;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureTypeMeta;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShader;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.FSCapabilityAll;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

/**
 * Example program that draws a textured quad to the screen, with an
 * orthographic projection. The program cycles between the same texture but
 * reloaded as all of the separate texture types.
 */

public final class ExampleTexturedQuadImage implements Example
{
  private final ArrayBuffer               array;
  private final ArrayBufferWritableData   array_data;
  private final ArrayBufferTypeDescriptor array_type;
  private final ExampleConfig             config;
  private int                             frame         = 0;
  private final JCGLInterfaceCommon       gl;
  private boolean                         has_shut_down;
  private final IndexBuffer               indices;
  private final IndexBufferWritableData   indices_data;
  private final MatrixM4x4F               matrix_modelview;
  private final MatrixM4x4F               matrix_projection;
  private final ProgramReference          program;
  private int                             texture_index = 0;
  private final List<TextureUnit>         texture_units;
  private final Texture2DStatic           textures[];

  public ExampleTexturedQuadImage(
    final @Nonnull ExampleConfig config)
    throws ConstraintError,
      JCGLException,
      JCGLCompileException,
      IOException,
      FilesystemError
  {
    this.config = config;
    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();
    this.gl = this.config.getGL().getGLCommon();

    /**
     * Initialize shaders.
     */

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
      this.program = this.gl.programCreateCommon("color", v, f);
    }

    /**
     * Obtain access to the available texture units.
     */

    this.texture_units = this.gl.textureGetUnits();

    /**
     * Load a texture from an image file.
     */

    final EnumSet<TextureType> ttypes =
      TextureTypeMeta.getTextures2DRequiredByCommonSubset();
    this.textures = new Texture2DStatic[ttypes.size()];

    final TextureLoader loader = config.getTextureLoader();
    final FSCapabilityAll filesystem = config.getFilesystem();

    int index = 0;
    for (final TextureType type : ttypes) {
      System.out.println("Loading texture as " + type);

      final InputStream stream =
        filesystem.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/examples/reference_8888_4.png"));

      switch (type) {
        case TEXTURE_TYPE_RGBA_1010102_4BPP:
        case TEXTURE_TYPE_RGBA_16F_8BPP:
        case TEXTURE_TYPE_RGBA_16I_8BPP:
        case TEXTURE_TYPE_RGBA_16U_8BPP:
        case TEXTURE_TYPE_RGBA_16_8BPP:
        case TEXTURE_TYPE_RGBA_32I_16BPP:
        case TEXTURE_TYPE_RGBA_32U_16BPP:
        case TEXTURE_TYPE_RGBA_8I_4BPP:
        case TEXTURE_TYPE_RGBA_8U_4BPP:
        case TEXTURE_TYPE_RGB_16F_6BPP:
        case TEXTURE_TYPE_RGB_16I_6BPP:
        case TEXTURE_TYPE_RGB_16U_6BPP:
        case TEXTURE_TYPE_RGB_16_6BPP:
        case TEXTURE_TYPE_RGB_32F_12BPP:
        case TEXTURE_TYPE_RGB_32I_12BPP:
        case TEXTURE_TYPE_RGB_32U_12BPP:
        case TEXTURE_TYPE_RGB_8I_3BPP:
        case TEXTURE_TYPE_RGB_8U_3BPP:
        case TEXTURE_TYPE_RG_16F_4BPP:
        case TEXTURE_TYPE_RG_16I_4BPP:
        case TEXTURE_TYPE_RG_16U_4BPP:
        case TEXTURE_TYPE_RG_16_4BPP:
        case TEXTURE_TYPE_RG_32F_8BPP:
        case TEXTURE_TYPE_RG_32I_8BPP:
        case TEXTURE_TYPE_RG_32U_8BPP:
        case TEXTURE_TYPE_RG_8I_2BPP:
        case TEXTURE_TYPE_RG_8U_2BPP:
        case TEXTURE_TYPE_R_16F_2BPP:
        case TEXTURE_TYPE_R_16I_2BPP:
        case TEXTURE_TYPE_R_16U_2BPP:
        case TEXTURE_TYPE_R_16_2BPP:
        case TEXTURE_TYPE_R_32F_4BPP:
        case TEXTURE_TYPE_R_32I_4BPP:
        case TEXTURE_TYPE_R_32U_4BPP:
        case TEXTURE_TYPE_R_8I_1BPP:
        case TEXTURE_TYPE_R_8U_1BPP:
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RGB_565_2BPP:
        case TEXTURE_TYPE_RG_8_2BPP:
        case TEXTURE_TYPE_R_8_1BPP:
        case TEXTURE_TYPE_RGBA_32F_16BPP:
        {
          break;
        }
        case TEXTURE_TYPE_RGBA_8_4BPP:
        {
          this.textures[index] =
            loader.load2DStaticRGBA8(
              this.gl,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
              stream,
              type.toString());
          break;
        }
        case TEXTURE_TYPE_RGB_8_3BPP:
        {
          this.textures[index] =
            loader.load2DStaticRGB8(
              this.gl,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
              stream,
              type.toString());
        }
      }

      stream.close();
      ++index;
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
      this.gl.arrayBufferAllocate(
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

    this.gl.arrayBufferBind(this.array);
    this.gl.arrayBufferUpdate(this.array_data);

    /**
     * Allocate and initialize an index buffer sufficient for two triangles.
     */

    this.indices = this.gl.indexBufferAllocate(this.array, 6);
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
    ++this.frame;

    if ((this.frame % 60) == 0) {
      this.texture_index = (this.texture_index + 1) % this.textures.length;
      this.config.getLog().debug(
        "Selected " + this.textures[this.texture_index]);
    }

    this.gl.colorBufferClear3f(0.0f, 0.2f, 0.3f);

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

    this.gl.blendingEnable(
      BlendFunction.BLEND_SOURCE_ALPHA,
      BlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA);

    /**
     * Activate shading program, and associate parts of the array buffer with
     * inputs to the shader.
     */

    this.gl.programActivate(this.program);
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

      this.gl.programUniformPutMatrix4x4f(u_proj, this.matrix_projection);
      this.gl.programUniformPutMatrix4x4f(u_model, this.matrix_modelview);

      /**
       * Bind the texture to the first available texture unit, then upload the
       * texture unit reference to the shader.
       */

      this.gl.texture2DStaticBind(
        this.texture_units.get(0),
        this.textures[this.texture_index]);
      this.gl.programUniformPutTextureUnit(
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

      this.gl.arrayBufferBind(this.array);
      this.gl.programAttributeArrayAssociate(p_uv, b_uv);
      this.gl.programAttributeArrayAssociate(p_pos, b_pos);

      /**
       * Draw primitives, using the array buffer and the given index buffer.
       */

      this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
      this.gl.programAttributeArrayDisassociate(p_pos);
      this.gl.programAttributeArrayDisassociate(p_uv);
      this.gl.arrayBufferUnbind();
    }
    this.gl.programDeactivate();

    this.gl.blendingDisable();
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
    ProjectionMatrix.makeOrthographicProjection(
      this.matrix_projection,
      0,
      size.getXI(),
      0,
      size.getYI(),
      1,
      100);

    this.gl.viewportSet(position, size);
  }

  @Override public void shutdown()
    throws JCGLException,
      ConstraintError,
      JCGLCompileException
  {
    this.has_shut_down = true;
    this.gl.arrayBufferDelete(this.array);
    this.gl.indexBufferDelete(this.indices);

    for (final Texture2DStatic t : this.textures) {
      this.gl.texture2DStaticDelete(t);
    }

    this.gl.programDelete(this.program);
  }
}
