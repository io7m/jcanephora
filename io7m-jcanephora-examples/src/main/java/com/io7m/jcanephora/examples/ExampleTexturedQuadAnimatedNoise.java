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
import com.io7m.jcanephora.SpatialCursorWritable3i;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.Texture2DWritableData;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.PathVirtual;

/**
 * Example program that draws a textured quad to the screen, with an
 * orthographic projection.
 * 
 * The texture is replaced once-per frame with new texture data.
 */

public final class ExampleTexturedQuadAnimatedNoise implements Example
{
  private final GLInterfaceES2          gl;
  private final ArrayBufferDescriptor   array_type;
  private final ArrayBuffer             array;
  private final ArrayBufferWritableData array_data;
  private final Program                 program;
  private final MatrixM4x4F             matrix_projection;
  private final MatrixM4x4F             matrix_modelview;
  private final IndexBuffer             indices;
  private final IndexBufferWritableData indices_data;
  private final ExampleConfig           config;
  private boolean                       has_shut_down;
  private final Texture2DStatic         texture;
  private final Texture2DWritableData   texture_update;
  private final TextureUnit[]           texture_units;

  public ExampleTexturedQuadAnimatedNoise(
    final @Nonnull ExampleConfig config)
    throws ConstraintError,
      GLException,
      GLCompileException
  {
    this.config = config;
    this.gl = config.getGL();
    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();

    /**
     * Initialize shaders.
     */

    this.program = new Program("uv", config.getLog());
    this.program.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/examples/uv.v"));
    this.program.addFragmentShader(new PathVirtual(
      "/com/io7m/jcanephora/examples/uv.f"));
    this.program.compile(config.getFilesystem(), this.gl);

    /**
     * Obtain access to the available texture units.
     */

    this.texture_units = this.gl.textureGetUnits();

    /**
     * Allocate a texture.
     */

    this.texture =
      this.gl.texture2DStaticAllocate(
        "gradient",
        64,
        64,
        TextureType.TEXTURE_TYPE_RGB_888_3BPP,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    /**
     * Allocate texture data.
     */

    this.texture_update = new Texture2DWritableData(this.texture);

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

    final ArrayBufferAttribute[] ab = new ArrayBufferAttribute[2];
    ab[0] = new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 4);
    ab[1] = new ArrayBufferAttribute("uv", GLScalarType.TYPE_FLOAT, 2);
    this.array_type = new ArrayBufferDescriptor(ab);
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
    throws GLException,
      GLCompileException,
      ConstraintError
  {
    this.gl.colorBufferClear3f(0.15f, 0.15f, 0.15f);

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

    /**
     * Generate new texture data and update the texture.
     */

    {
      final SpatialCursorWritable3i tx_cursor =
        this.texture_update.getCursor3i();

      while (tx_cursor.canWrite()) {
        final double x = tx_cursor.getElementX();
        final double y = tx_cursor.getElementY();
        final double red = x / 64.0;
        final double green = Math.random() * 0.5;
        final double blue = y / 64.0;

        tx_cursor.put3i(
          (int) (255 * red),
          (int) (255 * green),
          (int) (255 * blue));
      }

      this.gl.texture2DStaticUpdate(this.texture_update);
    }

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

      this.gl.texture2DStaticBind(this.texture_units[0], this.texture);
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

      final ArrayBufferAttribute b_pos =
        this.array_type.getAttribute("position");
      final ArrayBufferAttribute b_uv = this.array_type.getAttribute("uv");

      /**
       * Bind the array buffer, and associate program vertex attribute inputs
       * with array vertex attributes.
       */

      this.gl.arrayBufferBind(this.array);
      this.gl.arrayBufferBindVertexAttribute(this.array, b_pos, p_pos);
      this.gl.arrayBufferBindVertexAttribute(this.array, b_uv, p_uv);

      /**
       * Draw primitives, using the array buffer and the given index buffer.
       */

      this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
      this.gl.arrayBufferUnbind();
    }
    this.program.deactivate(this.gl);
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
    throws GLException,
      ConstraintError,
      GLCompileException
  {
    this.has_shut_down = true;
    this.gl.arrayBufferDelete(this.array);
    this.gl.indexBufferDelete(this.indices);
    this.gl.texture2DStaticDelete(this.texture);
    this.program.delete(this.gl);
  }
}
