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
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureLoader;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.UsageHint;
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
  private final JCGLInterfaceCommon       gl;
  private final ArrayBufferTypeDescriptor array_type;
  private final ArrayBuffer               array;
  private final ArrayBufferWritableData   array_data;
  private final Program                   program;
  private final MatrixM4x4F               matrix_projection;
  private final MatrixM4x4F               matrix_modelview;
  private final IndexBuffer               indices;
  private final IndexBufferWritableData   indices_data;
  private final ExampleConfig             config;
  private boolean                         has_shut_down;
  private final Texture2DStatic           textures[];
  private final TextureUnit[]             texture_units;
  private int                             frame         = 0;
  private int                             texture_index = 0;

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

    this.program = new Program("uv", config.getLog());
    this.program.addVertexShader(PathVirtual
      .ofString(("/com/io7m/jcanephora/examples/uv.v")));
    this.program.addFragmentShader(PathVirtual
      .ofString(("/com/io7m/jcanephora/examples/uv.f")));
    this.program.compile(config.getFilesystem(), this.gl);

    /**
     * Obtain access to the available texture units.
     */

    this.texture_units = this.gl.textureGetUnits();

    /**
     * Load a texture from an image file.
     */

    this.textures =
      new Texture2DStatic[TextureType.get2DTypesCommon().size()];

    final TextureLoader loader = config.getTextureLoader();
    final FSCapabilityAll filesystem = config.getFilesystem();

    int index = 0;
    for (final TextureType type : TextureType.get2DTypesCommon()) {
      final InputStream stream =
        filesystem.openFile(PathVirtual
          .ofString("/com/io7m/jcanephora/examples/reference_8888_4.png"));

      switch (type) {
        case TEXTURE_TYPE_DEPTH_16_2BPP:
        case TEXTURE_TYPE_DEPTH_24_4BPP:
        case TEXTURE_TYPE_DEPTH_32F_4BPP:
        case TEXTURE_TYPE_RGBA_4444_2BPP:
        case TEXTURE_TYPE_RGBA_5551_2BPP:
        case TEXTURE_TYPE_RGB_565_2BPP:
        case TEXTURE_TYPE_RG_88_2BPP:
        case TEXTURE_TYPE_R_8_1BPP:
        {
          break;
        }
        case TEXTURE_TYPE_RGBA_8888_4BPP:
        {
          this.textures[index] =
            loader.load2DStaticRGBA8888(
              this.gl,
              TextureWrapS.TEXTURE_WRAP_REPEAT,
              TextureWrapT.TEXTURE_WRAP_REPEAT,
              TextureFilterMinification.TEXTURE_FILTER_NEAREST,
              TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
              stream,
              type.toString());
          break;
        }
        case TEXTURE_TYPE_RGB_888_3BPP:
        {
          this.textures[index] =
            loader.load2DStaticRGB888(
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

    final ArrayBufferAttributeDescriptor[] ab =
      new ArrayBufferAttributeDescriptor[2];
    ab[0] =
      new ArrayBufferAttributeDescriptor(
        "position",
        JCGLScalarType.TYPE_FLOAT,
        4);
    ab[1] =
      new ArrayBufferAttributeDescriptor("uv", JCGLScalarType.TYPE_FLOAT, 2);
    this.array_type = new ArrayBufferTypeDescriptor(ab);
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

      uv_cursor.put2f(0.0f, 0.0f);
      uv_cursor.put2f(0.0f, 1.0f);
      uv_cursor.put2f(1.0f, 1.0f);
      uv_cursor.put2f(1.0f, 0.0f);
    }

    /**
     * Upload the array data.
     */

    this.gl.arrayBufferBind(this.array);
    this.gl.arrayBufferUpdate(this.array, this.array_data);

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

    this.gl.indexBufferUpdate(this.indices, this.indices_data);
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
    ProjectionMatrix.makeOrthographic(
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

    this.program.activate(this.gl);
    {
      /**
       * Get references to the program's uniform variable inputs.
       */

      final ProgramUniform u_proj =
        this.program.getUniform("matrix_projection");
      final ProgramUniform u_model =
        this.program.getUniform("matrix_modelview");
      final ProgramUniform u_texture = this.program.getUniform("texture");

      /**
       * Upload the matrices to the uniform variable inputs.
       */

      this.gl.programPutUniformMatrix4x4f(u_proj, this.matrix_projection);
      this.gl.programPutUniformMatrix4x4f(u_model, this.matrix_modelview);

      /**
       * Bind the texture to the first available texture unit, then upload the
       * texture unit reference to the shader.
       */

      this.gl.texture2DStaticBind(
        this.texture_units[0],
        this.textures[this.texture_index]);
      this.gl.programPutUniformTextureUnit(u_texture, this.texture_units[0]);

      /**
       * Get references to the program's vertex attribute inputs.
       */

      final ProgramAttribute p_pos =
        this.program.getAttribute("vertex_position");
      final ProgramAttribute p_uv = this.program.getAttribute("vertex_uv");

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
      this.gl.arrayBufferBindVertexAttribute(b_pos, p_pos);
      this.gl.arrayBufferBindVertexAttribute(b_uv, p_uv);

      /**
       * Draw primitives, using the array buffer and the given index buffer.
       */

      this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
      this.gl.arrayBufferUnbind();
    }
    this.program.deactivate(this.gl);

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
    ProjectionMatrix.makeOrthographic(
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

    this.program.delete(this.gl);
  }
}
