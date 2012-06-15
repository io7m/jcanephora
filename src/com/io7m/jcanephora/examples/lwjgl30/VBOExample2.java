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
import com.io7m.jlog.Log;
import com.io7m.jtensors.VectorI4F;

public final class VBOExample2 implements Runnable
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
      final VBOExample2 v = new VBOExample2(gl);
      v.run();
    } finally {
      Display.destroy();
    }
  }

  private final @Nonnull ArrayBuffer           buffer_rgb;
  private final @Nonnull ArrayBufferDescriptor buffer_rgb_type;
  private final @Nonnull ArrayBuffer           buffer_pos;
  private final @Nonnull ArrayBufferDescriptor buffer_pos_type;
  private final @Nonnull IndexBuffer           triangles[];
  private final @Nonnull GLInterface           gl;
  private final @Nonnull VectorI4F             background;

  private VBOExample2(
    final GLInterface gl)
    throws ConstraintError,
      GLException
  {
    this.gl = gl;
    this.background = new VectorI4F(0.2f, 0.2f, 0.2f, 1.0f);

    final ArrayBufferAttribute pba[] = new ArrayBufferAttribute[1];
    pba[0] = new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3);
    this.buffer_pos_type = new ArrayBufferDescriptor(pba);

    final ArrayBufferAttribute pca[] = new ArrayBufferAttribute[1];
    pca[0] = new ArrayBufferAttribute("color", GLScalarType.TYPE_FLOAT, 3);
    this.buffer_rgb_type = new ArrayBufferDescriptor(pca);

    this.buffer_rgb = gl.arrayBufferAllocate(4, this.buffer_rgb_type);
    {
      final ArrayBufferWritableMap map =
        gl.arrayBufferMapWrite(this.buffer_rgb);
      final ArrayBufferCursorWritable3f color = map.getCursor3f("color");

      color.put3f(1.0f, 0.0f, 0.0f);
      color.put3f(1.0f, 1.0f, 0.0f);
      color.put3f(0.0f, 1.0f, 0.0f);
      color.put3f(0.0f, 1.0f, 1.0f);

      gl.arrayBufferUnmap(this.buffer_rgb);
    }

    this.buffer_pos = gl.arrayBufferAllocate(4, this.buffer_pos_type);
    {
      final ArrayBufferWritableMap map =
        gl.arrayBufferMapWrite(this.buffer_pos);
      final ArrayBufferCursorWritable3f position =
        map.getCursor3f("position");

      position.put3f(-0.5f, 0.5f, 0.0f);
      position.put3f(-0.5f, -0.5f, 0.0f);
      position.put3f(0.5f, -0.5f, 0.0f);
      position.put3f(0.5f, 0.5f, 0.0f);

      gl.arrayBufferUnmap(this.buffer_pos);
    }

    this.triangles = new IndexBuffer[2];
    this.triangles[0] = gl.indexBufferAllocate(this.buffer_pos, 3);
    this.triangles[1] = gl.indexBufferAllocate(this.buffer_pos, 3);
    assert this.triangles[0].getType() == GLUnsignedType.TYPE_UNSIGNED_BYTE;
    assert this.triangles[1].getType() == GLUnsignedType.TYPE_UNSIGNED_BYTE;

    {
      final IndexBufferWritableMap map =
        gl.indexBufferMapWrite(this.triangles[0]);

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
    this.gl.colorBufferClearV4f(this.background);

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.buffer_pos.getLocation());
    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
    GL11.glVertexPointer(
      3,
      GL11.GL_FLOAT,
      (int) this.buffer_pos.getElementSizeBytes(),
      0L);

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.buffer_rgb.getLocation());
    GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    GL11.glColorPointer(
      3,
      GL11.GL_FLOAT,
      (int) this.buffer_rgb.getElementSizeBytes(),
      0L);

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
