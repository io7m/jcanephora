package com.io7m.jcanephora.examples.lwjgl30;

import java.util.Properties;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferCursorWritable3f;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableMap;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLInterfaceLWJGL30;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.GLUnsignedType;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableMap;
import com.io7m.jcanephora.LWJGL30;
import com.io7m.jlog.Log;
import com.io7m.jtensors.VectorI4F;

public final class VBOExample implements Runnable
{
  public static void main(
    final String args[])
    throws ConstraintError,
      GLException
  {
    LWJGL30.createDisplay("VBOExample", 640, 480);

    final Log log = new Log(new Properties(), "com.io7m", "example");
    final GLInterface gl = new GLInterfaceLWJGL30(log);

    try {
      final VBOExample v = new VBOExample(gl);
      v.run();
    } finally {
      Display.destroy();
    }
  }

  private final @Nonnull ArrayBuffer           buffer;
  private final @Nonnull ArrayBufferDescriptor buffer_type;
  private final @Nonnull IndexBuffer           triangles[];
  private final @Nonnull GLInterface           gl;
  private final @Nonnull VectorI4F             background;

  private VBOExample(
    final GLInterface gl)
    throws ConstraintError,
      GLException
  {
    this.gl = gl;
    this.background = new VectorI4F(0.2f, 0.2f, 0.2f, 1.0f);

    /*
     * An array buffer is an array of records. First, create the description
     * of the record type by assigning and name and type to each of the
     * fields. Here, the two fields are "position" and "color" - both
     * three-element vectors of type 'float'.
     */

    final ArrayBufferAttribute as[] = new ArrayBufferAttribute[2];
    as[0] = new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3);
    as[1] = new ArrayBufferAttribute("color", GLScalarType.TYPE_FLOAT, 3);
    this.buffer_type = new ArrayBufferDescriptor(as);

    /*
     * Allocate an array buffer containing four elements of the above record
     * type.
     */

    this.buffer = gl.arrayBufferAllocate(4, this.buffer_type);
    {
      /*
       * Map the allocated buffer into the application's address space. Obtain
       * cursors to the "position" and "cursor" fields. Cursors automatically
       * seek through the buffer when writing values. The type of the cursor
       * is checked against the type of the target attribute, raising an
       * exception if the types do not match.
       */

      final ArrayBufferWritableMap map = gl.arrayBufferMapWrite(this.buffer);
      final ArrayBufferCursorWritable3f position =
        map.getCursor3f("position");
      final ArrayBufferCursorWritable3f color = map.getCursor3f("color");

      /*
       * Note that the two cursors are independent: writes to the attributes
       * could be interleaved without changing the semantics of the program.
       */

      position.put3f(-0.5f, 0.5f, 0.0f);
      position.put3f(-0.5f, -0.5f, 0.0f);
      position.put3f(0.5f, -0.5f, 0.0f);
      position.put3f(0.5f, 0.5f, 0.0f);
      color.put3f(1.0f, 0.0f, 0.0f);
      color.put3f(1.0f, 1.0f, 0.0f);
      color.put3f(0.0f, 1.0f, 0.0f);
      color.put3f(0.0f, 1.0f, 1.0f);

      /*
       * Check that all elements of the buffer have been written.
       */

      assert (position.hasNext() == false);
      assert (color.hasNext() == false);

      /*
       * Unmap the array buffer.
       */

      gl.arrayBufferUnmap(this.buffer);
    }

    /*
     * Create two index buffers, representing two triangles. The index buffer
     * interface automatically picks the smallest type possible for indices
     * (based on the number of elements in the passed array buffer). For
     * example, if an array buffer has less than 256 elements, the indices
     * will be single bytes.
     */

    this.triangles = new IndexBuffer[2];
    this.triangles[0] = gl.indexBufferAllocate(this.buffer, 3);
    this.triangles[1] = gl.indexBufferAllocate(this.buffer, 3);

    assert this.triangles[0].getType() == GLUnsignedType.TYPE_UNSIGNED_BYTE;
    assert this.triangles[1].getType() == GLUnsignedType.TYPE_UNSIGNED_BYTE;

    {
      final IndexBufferWritableMap map =
        gl.indexBufferMapWrite(this.triangles[0]);

      /*
       * Insert vertex indices into the map.
       */

      map.put(0, 0);
      map.put(1, 1);
      map.put(2, 2);

      gl.indexBufferUnmap(this.triangles[0]);
    }

    {
      final IndexBufferWritableMap map =
        gl.indexBufferMapWrite(this.triangles[1]);

      map.put(0, 0);
      map.put(1, 2);
      map.put(2, 3);

      gl.indexBufferUnmap(this.triangles[1]);
    }
  }

  private void render()
    throws GLException,
      ConstraintError
  {
    this.gl.colorBufferClear(this.background);

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.buffer.getLocation());
    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    GL11.glVertexPointer(
      3,
      GL11.GL_FLOAT,
      (int) this.buffer.getElementSizeBytes(),
      this.buffer.getDescriptor().getAttributeOffset("position"));
    GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    GL11.glColorPointer(
      3,
      GL11.GL_FLOAT,
      (int) this.buffer.getElementSizeBytes(),
      this.buffer.getDescriptor().getAttributeOffset("color"));

    GL15.glBindBuffer(
      GL15.GL_ELEMENT_ARRAY_BUFFER,
      this.triangles[0].getLocation());
    GL11.glDrawElements(
      GL11.GL_TRIANGLES,
      (int) this.triangles[0].getElements(),
      GL11.GL_UNSIGNED_BYTE,
      0L);

    GL15.glBindBuffer(
      GL15.GL_ELEMENT_ARRAY_BUFFER,
      this.triangles[1].getLocation());
    GL11.glDrawElements(
      GL11.GL_TRIANGLES,
      (int) this.triangles[1].getElements(),
      GL11.GL_UNSIGNED_BYTE,
      0L);

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
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
