package com.io7m.jcanephora.examples;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableData;
import com.io7m.jcanephora.CursorWritable4f;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceEmbedded;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferCursorWritable;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.PathVirtual;

public final class ExampleArrayBuffer implements Example
{
  private final GLInterfaceEmbedded     gl;
  private final ArrayBufferDescriptor   array_type;
  private final ArrayBuffer             array;
  private final ArrayBufferWritableData array_data;
  private final Program                 program;
  private final MatrixM4x4F             matrix_projection;
  private final MatrixM4x4F             matrix_modelview;
  private final IndexBuffer             indices;
  private final IndexBufferWritableData indices_data;

  public ExampleArrayBuffer(
    final @Nonnull GLInterfaceEmbedded gl,
    final @Nonnull FilesystemAPI filesystem,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException,
      GLCompileException
  {
    this.gl = gl;

    {
      this.array_type =
        new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
          new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 4),
          new ArrayBufferAttribute("color", GLScalarType.TYPE_FLOAT, 4), });
      this.array = this.gl.arrayBufferAllocate(3, this.array_type);
      this.array_data = new ArrayBufferWritableData(this.array);

      final CursorWritable4f pos_cursor =
        this.array_data.getCursor4f("position");
      final CursorWritable4f col_cursor =
        this.array_data.getCursor4f("color");

      pos_cursor.put4f(0.0f, 100.0f, 0.0f, 1.0f);
      col_cursor.put4f(1.0f, 0.0f, 0.0f, 1.0f);

      pos_cursor.put4f(0.0f, 0.0f, 0.0f, 1.0f);
      col_cursor.put4f(0.0f, 1.0f, 0.0f, 1.0f);

      pos_cursor.put4f(100.0f, 0.0f, 0.0f, 1.0f);
      col_cursor.put4f(0.0f, 0.0f, 1.0f, 1.0f);

      this.gl.arrayBufferBind(this.array);
      this.gl.arrayBufferUpdate(this.array, this.array_data);
    }

    {
      this.indices = this.gl.indexBufferAllocate(this.array, 3);
      this.indices_data = new IndexBufferWritableData(this.indices);

      final IndexBufferCursorWritable ind_cursor =
        this.indices_data.getCursor();
      ind_cursor.putIndex(0);
      ind_cursor.putIndex(1);
      ind_cursor.putIndex(2);

      this.gl.indexBufferUpdate(this.indices, this.indices_data);
    }

    this.program = new Program("simple", log);
    this.program.addVertexShader(new PathVirtual(
      "/com/io7m/jcanephora/examples/simple.v"));
    this.program.addFragmentShader(new PathVirtual(
      "/com/io7m/jcanephora/examples/simple.f"));
    this.program.compile(filesystem, gl);

    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();
  }

  @Override public void display()
    throws GLException,
      GLCompileException,
      ConstraintError
  {
    this.gl.colorBufferClear3f(0.15f, 0.2f, 0.15f);

    MatrixM4x4F.setIdentity(this.matrix_projection);
    ProjectionMatrix.makeOrthographic(
      this.matrix_projection,
      0,
      640,
      0,
      480,
      1,
      100);

    MatrixM4x4F.setIdentity(this.matrix_modelview);

    this.program.activate(this.gl);
    {
      final ProgramUniform u_proj =
        this.program.getUniform("matrix_projection");
      final ProgramUniform u_model =
        this.program.getUniform("matrix_modelview");

      this.gl.programPutUniformMatrix4x4f(u_proj, this.matrix_projection);
      this.gl.programPutUniformMatrix4x4f(u_model, this.matrix_modelview);

      final ProgramAttribute p_pos =
        this.program.getAttribute("vertex_position");
      final ProgramAttribute p_col =
        this.program.getAttribute("vertex_color");

      final ArrayBufferAttribute b_pos =
        this.array_type.getAttribute("position");
      final ArrayBufferAttribute b_col =
        this.array_type.getAttribute("color");

      this.gl.arrayBufferBind(this.array);
      this.gl.arrayBufferBindVertexAttribute(this.array, b_pos, p_pos);
      this.gl.arrayBufferBindVertexAttribute(this.array, b_col, p_col);

      this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
    }
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
  }

  @Override public void shutdown()
    throws GLException,
      ConstraintError,
      GLCompileException
  {
    this.gl.arrayBufferDelete(this.array);
    this.gl.indexBufferDelete(this.indices);
    this.program.delete(this.gl);
  }
}
