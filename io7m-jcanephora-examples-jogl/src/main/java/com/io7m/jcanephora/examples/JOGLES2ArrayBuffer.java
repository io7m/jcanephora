package com.io7m.jcanephora.examples;

import java.util.Properties;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttribute;
import com.io7m.jcanephora.ArrayBufferDescriptor;
import com.io7m.jcanephora.ArrayBufferWritableData;
import com.io7m.jcanephora.CursorWritable4f;
import com.io7m.jcanephora.GLEmbeddedJOGLES2;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceEmbedded;
import com.io7m.jcanephora.GLScalarType;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferCursorWritable;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jlog.Log;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;

public final class JOGLES2ArrayBuffer implements GLEventListener
{
  private static void fatal(
    final Throwable e)
  {
    e.printStackTrace();
    System.exit(1);
  }

  public static void main(
    final String args[])
  {
    new JOGLES2ArrayBuffer();
  }

  private final GLWindow          window;
  private GLInterfaceEmbedded     gl;
  private final Log               log;
  private final FPSAnimator       animator;
  private ArrayBufferDescriptor   array_type;
  private ArrayBuffer             array;
  private ArrayBufferWritableData array_data;
  private IndexBuffer             indices;
  private IndexBufferWritableData indices_data;

  public JOGLES2ArrayBuffer()
  {
    final Properties properties = new Properties();
    this.log = new Log(properties, "com.io7m.jcanephora.examples", "main");

    final GLProfile pro = GLProfile.getDefault();
    final GLCapabilities caps = new GLCapabilities(pro);

    this.window = GLWindow.create(caps);
    this.window.setSize(640, 480);
    this.window.setVisible(true);
    this.window.setTitle(this.getClass().getName());
    this.window.addWindowListener(new WindowAdapter() {
      @Override public void windowDestroyNotify(
        @SuppressWarnings("unused") final WindowEvent e)
      {
        System.exit(0);
      }
    });

    this.window.addGLEventListener(this);

    this.animator = new FPSAnimator(60);
    this.animator.setUpdateFPSFrames(60, System.err);
    this.animator.add(this.window);
    this.animator.start();
  }

  @Override public void display(
    final GLAutoDrawable drawable)
  {
    try {
      this.gl.colorBufferClear3f(0.15f, 0.2f, 0.15f);
    } catch (final GLException e) {
      JOGLES2ArrayBuffer.fatal(e);
    } catch (final ConstraintError e) {
      JOGLES2ArrayBuffer.fatal(e);
    }
  }

  @Override public void dispose(
    final GLAutoDrawable drawable)
  {
    // TODO Auto-generated method stub
  }

  @Override public void init(
    final GLAutoDrawable drawable)
  {
    try {
      this.gl = new GLEmbeddedJOGLES2(drawable.getContext(), this.log);

      /**
       * Allocate and populate array buffer.
       */

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

      /**
       * Allocate and populate index buffer.
       */

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

      this.gl.indexBufferUpdate(this.indices, this.indices_data);

    } catch (final GLException e) {
      JOGLES2ArrayBuffer.fatal(e);
    } catch (final ConstraintError e) {
      JOGLES2ArrayBuffer.fatal(e);
    }
  }

  @Override public void reshape(
    final GLAutoDrawable drawable,
    final int x,
    final int y,
    final int w,
    final int h)
  {
    // TODO Auto-generated method stub
  }
}
