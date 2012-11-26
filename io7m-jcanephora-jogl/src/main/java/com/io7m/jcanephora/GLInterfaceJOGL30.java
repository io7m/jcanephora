package com.io7m.jcanephora;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jlog.Log;

/**
 * A class implementing GLInterface that uses only non-deprecated features of
 * OpenGL 3.0, using JOGL as the backend.
 * 
 * A {@link javax.media.opengl.GLContext} is used to construct the interface,
 * and therefore the <code>GLInterfaceJOGL30</code> interface has the same
 * thread safe/unsafe behaviour.
 * 
 * The <code>GLInterfaceJOGL30</code> implementation does not call
 * {@link javax.media.opengl.GLContext#makeCurrent()} or
 * {@link javax.media.opengl.GLContext#release()}, so these calls must be made
 * by the programmer when necessary (typically, programs call
 * {@link javax.media.opengl.GLContext#makeCurrent()}, perform all rendering,
 * and then call {@link javax.media.opengl.GLContext#release()} at the end of
 * the frame). The JOGL library can also optionally manage this via the
 * {@link javax.media.opengl.GLAutoDrawable} interface.
 * 
 * As OpenGL 3.0 is essentially a subset of 2.1, this class works on OpenGL
 * 2.1 implementations.
 */

@NotThreadSafe public final class GLInterfaceJOGL30 extends
  GLEmbeddedJOGLES2Actual implements GLInterface
{
  /**
   * The size of the integer cache, in bytes.
   */

  private static final int INTEGER_CACHE_SIZE = 8 * 4;

  private static void shaderReadSource(
    final @Nonnull InputStream stream,
    final @Nonnull ArrayList<String> lines,
    final @Nonnull ArrayList<Integer> lengths)
    throws IOException
  {
    final BufferedReader reader =
      new BufferedReader(new InputStreamReader(stream));

    for (;;) {
      final String line = reader.readLine();
      if (line == null) {
        break;
      }
      lines.add(line + "\n");
      lengths.add(Integer.valueOf(line.length() + 1));
    }

    assert (lines.size() == lengths.size());
  }

  public GLInterfaceJOGL30(
    final @Nonnull GLContext context,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    super(context, log);
  }

  @Override public ByteBuffer arrayBufferMapRead(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: map " + id);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getLocation());
    GLError.check(this);
    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ARRAY_BUFFER, GL2GL3.GL_READ_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLError.check(this);

    return b;
  }

  @Override public ArrayBufferWritableMap arrayBufferMapWrite(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: map " + id);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getLocation());
    GLError.check(this);
    gl.glBufferData(
      GL.GL_ARRAY_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    final ByteBuffer b = gl.glMapBuffer(GL.GL_ARRAY_BUFFER, GL.GL_WRITE_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLError.check(this);

    return new ArrayBufferWritableMap(id, b);
  }

  @Override public void arrayBufferUnmap(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    this.log.debug("vertex-buffer: unmap " + id);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getLocation());
    gl.glUnmapBuffer(GL.GL_ARRAY_BUFFER);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLError.check(this);
  }

  @Override public IndexBufferReadableMap indexBufferMapRead(
    final @Nonnull IndexBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    this.log.debug("index-buffer: map " + id);

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getLocation());
    GLError.check(this);
    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, GL2GL3.GL_READ_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLError.check(this);

    return new IndexBufferReadableMap(id, b);
  }

  @Override public IndexBufferWritableMap indexBufferMapWrite(
    final @Nonnull IndexBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    this.log.debug("index-buffer: map " + id);

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getLocation());
    GLError.check(this);
    gl.glBufferData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, GL.GL_WRITE_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLError.check(this);

    return new IndexBufferWritableMap(id, b);
  }

  @Override public void indexBufferUnmap(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    this.log.debug("index-buffer: unmap " + id);

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getLocation());
    gl.glUnmapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLError.check(this);
  }

  @Override public void logicOperationsDisable()
    throws GLException
  {
    final GL2GL3 gl = this.contextGetGL();
    gl.glDisable(GL2.GL_LOGIC_OP);
    GLError.check(this);
  }

  @Override public void logicOperationsEnable(
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(operation, "Logic operation");
    gl.glEnable(GL2.GL_LOGIC_OP);
    gl.glLogicOp(GLInterfaceJOGL30.logicOpToGL(operation));
    GLError.check(this);
  }

  @Override public boolean logicOperationsEnabled()
    throws GLException
  {
    final GL2GL3 g = this.contextGetGL();
    final boolean e = g.glIsEnabled(GL2.GL_LOGIC_OP);
    GLError.check(this);
    return e;
  }

  @Override public PixelUnpackBuffer pixelUnpackBufferAllocate(
    final long elements,
    final @Nonnull GLScalarType type,
    final long element_values,
    final @Nonnull UsageHint hint)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainRange(elements, 1, Long.MAX_VALUE, "Element count");
    Constraints.constrainRange(
      element_values,
      1,
      Long.MAX_VALUE,
      "Element values");
    Constraints.constrainNotNull(hint, "Usage hint");
    Constraints.constrainNotNull(type, "Element type");

    final long bytes =
      (element_values * GLScalarTypeMeta.getSizeBytes(type)) * elements;

    this.log.debug("pixel-unpack-buffer: allocate ("
      + elements
      + " elements of type ("
      + type
      + ", "
      + element_values
      + "), "
      + bytes
      + " bytes)");

    this.integer_cache.rewind();
    gl.glGenBuffers(1, this.integer_cache);
    GLError.check(this);

    final int id = this.integer_cache.get(0);
    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, id);
    GLError.check(this);
    gl.glBufferData(
      GL2GL3.GL_PIXEL_UNPACK_BUFFER,
      bytes,
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    this.log.debug("pixel-unpack-buffer: allocated " + id);
    return new PixelUnpackBuffer(id, elements, type, element_values);
  }

  @Override public void pixelUnpackBufferDelete(
    final @Nonnull PixelUnpackBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(id, "Pixel unpack buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Pixel unpack buffer not deleted");

    this.log.debug("pixel-unpack-buffer: delete " + id);

    this.integer_cache.rewind();
    this.integer_cache.put(0, id.getLocation());
    gl.glDeleteBuffers(1, this.integer_cache);
    id.setDeleted();
    GLError.check(this);
  }

  @Override public ByteBuffer pixelUnpackBufferMapRead(
    final @Nonnull PixelUnpackBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(id, "Pixel unpack buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Pixel unpack buffer not deleted");

    this.log.debug("pixel-unpack-buffer: map " + id);

    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, id.getLocation());
    GLError.check(this);
    final ByteBuffer b =
      gl.glMapBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, GL2GL3.GL_READ_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, 0);
    GLError.check(this);

    return b;
  }

  @Override public PixelUnpackBufferWritableMap pixelUnpackBufferMapWrite(
    final @Nonnull PixelUnpackBuffer id)
    throws GLException,
      ConstraintError
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(id, "Pixel unpack buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Pixel unpack buffer not deleted");

    this.log.debug("pixel-unpack-buffer: map " + id);

    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, id.getLocation());
    GLError.check(this);
    gl.glBufferData(
      GL2GL3.GL_PIXEL_UNPACK_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLError.check(this);

    final ByteBuffer b =
      gl.glMapBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, GL.GL_WRITE_ONLY);
    GLError.check(this);
    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, 0);
    GLError.check(this);

    return new PixelUnpackBufferWritableMap(id, b);
  }

  @Override public void pixelUnpackBufferUnmap(
    final @Nonnull PixelUnpackBuffer id)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(id, "Pixel unpack buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Pixel unpack buffer not deleted");

    this.log.debug("pixel-unpack-buffer: unmap " + id);

    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, id.getLocation());
    gl.glUnmapBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER);
    gl.glBindBuffer(GL2GL3.GL_PIXEL_UNPACK_BUFFER, 0);
    GLError.check(this);
  }

  @Override public @Nonnull PolygonMode polygonGetModeBack()
    throws ConstraintError,
      GLException
  {
    final GL2GL3 g = this.contextGetGL();

    this.integer_cache.rewind();
    g.glGetIntegerv(GL2.GL_POLYGON_MODE, this.integer_cache);
    GLError.check(this);
    return GLInterfaceJOGL30.polygonModeFromGL(this.integer_cache.get(1));
  }

  @Override public @Nonnull PolygonMode polygonGetModeFront()
    throws ConstraintError,
      GLException
  {
    final GL2GL3 g = this.contextGetGL();

    this.integer_cache.rewind();
    g.glGetIntegerv(GL2.GL_POLYGON_MODE, this.integer_cache);
    GLError.check(this);
    return GLInterfaceJOGL30.polygonModeFromGL(this.integer_cache.get(0));
  }

  @Override public void polygonSetMode(
    final @Nonnull FaceSelection faces,
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      GLException
  {
    final GL2GL3 gl = this.contextGetGL();

    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(mode, "Polygon mode");

    final int im = GLInterfaceJOGL30.polygonModeToGL(mode);
    final int fm = GLEmbeddedJOGLES2Actual.faceSelectionToGL(faces);
    gl.glPolygonMode(fm, im);
    GLError.check(this);
  }

  static final int polygonModeToGL(
    final PolygonMode g)
  {
    switch (g) {
      case POLYGON_FILL:
        return GL2GL3.GL_FILL;
      case POLYGON_LINES:
        return GL2GL3.GL_LINE;
      case POLYGON_POINTS:
        return GL2GL3.GL_POINT;
    }

    throw new UnreachableCodeException();
  }

  static final PolygonMode polygonModeFromGL(
    final int g)
  {
    switch (g) {
      case GL2GL3.GL_FILL:
        return PolygonMode.POLYGON_FILL;
      case GL2GL3.GL_LINE:
        return PolygonMode.POLYGON_LINES;
      case GL2GL3.GL_POINT:
        return PolygonMode.POLYGON_POINTS;
    }

    throw new UnreachableCodeException();
  }

  static final int logicOpToGL(
    final @Nonnull LogicOperation op)
  {
    switch (op) {
      case LOGIC_AND:
        return GL.GL_AND;
      case LOGIC_AND_INVERTED:
        return GL.GL_AND_INVERTED;
      case LOGIC_AND_REVERSE:
        return GL.GL_AND_REVERSE;
      case LOGIC_CLEAR:
        return GL.GL_CLEAR;
      case LOGIC_COPY:
        return GL.GL_COPY;
      case LOGIC_COPY_INVERTED:
        return GL.GL_COPY_INVERTED;
      case LOGIC_EQUIV:
        return GL.GL_EQUIV;
      case LOGIC_INVERT:
        return GL.GL_INVERT;
      case LOGIC_NAND:
        return GL.GL_NAND;
      case LOGIC_NOR:
        return GL.GL_NOR;
      case LOGIC_NO_OP:
        return GL.GL_NOOP;
      case LOGIC_OR:
        return GL.GL_OR;
      case LOGIC_OR_INVERTED:
        return GL.GL_OR_INVERTED;
      case LOGIC_OR_REVERSE:
        return GL.GL_OR_REVERSE;
      case LOGIC_SET:
        return GL.GL_SET;
      case LOGIC_XOR:
        return GL.GL_XOR;
    }

    throw new UnreachableCodeException();
  }

  static final LogicOperation logicOpFromGL(
    final int op)
  {
    switch (op) {
      case GL.GL_XOR:
        return LogicOperation.LOGIC_XOR;
      case GL.GL_SET:
        return LogicOperation.LOGIC_SET;
      case GL.GL_OR_REVERSE:
        return LogicOperation.LOGIC_OR_REVERSE;
      case GL.GL_OR_INVERTED:
        return LogicOperation.LOGIC_OR_INVERTED;
      case GL.GL_OR:
        return LogicOperation.LOGIC_OR;
      case GL.GL_NOOP:
        return LogicOperation.LOGIC_NO_OP;
      case GL.GL_NOR:
        return LogicOperation.LOGIC_NOR;
      case GL.GL_NAND:
        return LogicOperation.LOGIC_NAND;
      case GL.GL_INVERT:
        return LogicOperation.LOGIC_INVERT;
      case GL.GL_EQUIV:
        return LogicOperation.LOGIC_EQUIV;
      case GL.GL_COPY_INVERTED:
        return LogicOperation.LOGIC_COPY_INVERTED;
      case GL.GL_COPY:
        return LogicOperation.LOGIC_COPY;
      case GL.GL_CLEAR:
        return LogicOperation.LOGIC_CLEAR;
      case GL.GL_AND_REVERSE:
        return LogicOperation.LOGIC_AND_REVERSE;
      case GL.GL_AND_INVERTED:
        return LogicOperation.LOGIC_AND_INVERTED;
      case GL.GL_AND:
        return LogicOperation.LOGIC_AND;
    }

    throw new UnreachableCodeException();
  }
}
