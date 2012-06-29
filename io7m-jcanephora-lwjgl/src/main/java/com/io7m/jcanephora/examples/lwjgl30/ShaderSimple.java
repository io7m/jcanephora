package com.io7m.jcanephora.examples.lwjgl30;

import java.util.Properties;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.Display;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferCursorWritable3f;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableMap;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLInterfaceLWJGL30;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.GLUnsignedType;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableMap;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.Program;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorM3F;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathReal;
import com.io7m.jvvfs.PathVirtual;

public final class ShaderSimple implements Runnable
{
  public static void main(
    final String args[])
    throws ConstraintError,
      GLException,
      FilesystemError,
      GLCompileException
  {
    LWJGL30.createDisplay("ShaderSimple", 640, 480);

    final Log log = new Log(new Properties(), "com.io7m", "example");
    final GLInterface gl = new GLInterfaceLWJGL30(log);

    /**
     * Mount the "resources" directory containing shader source code into the
     * virtual filesystem.
     */

    final FilesystemAPI fs = new Filesystem(log, new PathReal("src/main"));
    fs.mount("resources", "/");

    try {
      final ShaderSimple v = new ShaderSimple(gl, log, fs);
      v.run();
    } finally {
      Display.destroy();
    }
  }

  private final @Nonnull ArrayBuffer           buffer;
  private final @Nonnull ArrayBufferDescriptor buffer_type;
  private final @Nonnull GLInterface           gl;
  private final @Nonnull VectorI4F             background;
  private final @Nonnull IndexBuffer           triangle;
  private final @Nonnull Program               program;
  private final @Nonnull MatrixM4x4F           matrix_modelview;
  private final @Nonnull MatrixM4x4F           matrix_projection;
  private final @Nonnull FilesystemAPI         fs;

  private ShaderSimple(
    final GLInterface gl,
    final Log log,
    final FilesystemAPI fs)
    throws ConstraintError,
      GLException,
      GLCompileException
  {
    this.gl = gl;
    this.fs = fs;
    this.background = new VectorI4F(0.2f, 0.2f, 0.2f, 1.0f);

    /**
     * Allocate an array buffer to hold per-vertex position and color data.
     */

    final ArrayBufferAttribute pba[] = new ArrayBufferAttribute[2];
    pba[0] = new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3);
    pba[1] = new ArrayBufferAttribute("color", GLScalarType.TYPE_FLOAT, 3);
    this.buffer_type = new ArrayBufferDescriptor(pba);

    this.buffer = gl.arrayBufferAllocate(3, this.buffer_type);
    {
      final ArrayBufferWritableMap map = gl.arrayBufferMapWrite(this.buffer);
      final ArrayBufferCursorWritable3f color = map.getCursor3f("color");
      final ArrayBufferCursorWritable3f position =
        map.getCursor3f("position");

      color.put3f(1.0f, 0.0f, 0.0f);
      color.put3f(1.0f, 1.0f, 0.0f);
      color.put3f(0.0f, 1.0f, 0.0f);

      position.put3f(-0.5f, 0.5f, 0.0f);
      position.put3f(-0.5f, -0.5f, 0.0f);
      position.put3f(0.5f, -0.5f, 0.0f);

      gl.arrayBufferUnmap(this.buffer);
    }

    /**
     * Allocate an index buffer to store the indices of the triangle to be
     * drawn.
     */

    this.triangle = gl.indexBufferAllocate(this.buffer, 3);
    assert this.triangle.getType() == GLUnsignedType.TYPE_UNSIGNED_BYTE;

    {
      final IndexBufferWritableMap map =
        gl.indexBufferMapWrite(this.triangle);

      map.put(0, 0);
      map.put(1, 1);
      map.put(2, 2);

      gl.indexBufferUnmap(this.triangle);
    }

    /**
     * Create a new GLSL program, add a vertex and fragment shader, and
     * compile it.
     */

    this.program = new Program("triangle-draw", log);
    this.program.addVertexShader(new PathVirtual("/shaders/trivial.v"));
    this.program.addFragmentShader(new PathVirtual("/shaders/trivial.f"));
    this.program.compile(fs, gl);

    /**
     * Allocate a projection and modelview matrix.
     */

    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();
  }

  private void render()
    throws GLException,
      ConstraintError
  {
    /**
     * Initialize the modelview matrix.
     */

    MatrixM4x4F.setIdentity(this.matrix_modelview);
    MatrixM4x4F.translateByVector3FInPlace(
      this.matrix_modelview,
      new VectorM3F(0.0f, 0.0f, -1.0f));

    /**
     * Initialize the projection matrix with an orthographic projection.
     */

    MatrixM4x4F.setIdentity(this.matrix_projection);
    ProjectionMatrix.makeOrthographic(
      this.matrix_projection,
      -1,
      1,
      -1,
      1,
      1,
      100);

    this.gl.colorBufferClearV4f(this.background);

    /**
     * Tell the GLSL program to recompile itself if necessary. If the shading
     * language programs have not been modified, this operation is a no-op.
     * 
     * The program image is only replaced upon a successful compilation. Note
     * that the code catches the compilation error and prints an error
     * message, and the program continues working with the previous compiled
     * executable.
     */

    try {
      this.program.compile(this.fs, this.gl);
    } catch (final GLCompileException e) {
      e.printStackTrace();
    }

    /**
     * Activate the GLSL shading program.
     */

    this.program.activate(this.gl);
    try {

      /**
       * Bind the vertex attributes - note the explicit binding of typed
       * vertex attributes to typed program attributes: an exception will be
       * raised if the types do not match.
       */

      this.gl.arrayBufferBind(this.buffer);
      this.gl.arrayBufferBindVertexAttribute(
        this.buffer,
        this.buffer_type.getAttribute("position"),
        this.program.getAttribute("position"));
      this.gl.arrayBufferBindVertexAttribute(
        this.buffer,
        this.buffer_type.getAttribute("color"),
        this.program.getAttribute("color"));

      /**
       * Upload the matrices.
       */

      this.gl.programPutUniformMatrix4x4f(
        this.program.getUniform("matrix_projection"),
        this.matrix_projection);
      this.gl.programPutUniformMatrix4x4f(
        this.program.getUniform("matrix_modelview"),
        this.matrix_modelview);

      /**
       * Draw the triangle!
       */

      this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.triangle);

      this.gl.arrayBufferUnbind();
    } finally {
      this.program.deactivate(this.gl);
    }
  }

  @Override public void run()
  {
    try {
      while (Display.isCloseRequested() == false) {
        this.render();
        Display.update();
        Display.sync(60);
      }
    } catch (final GLException e) {
      e.printStackTrace();
    } catch (final ConstraintError e) {
      e.printStackTrace();
    } finally {
      Display.destroy();
    }
  }
}
