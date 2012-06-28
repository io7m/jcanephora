package com.io7m.jcanephora.examples;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.fixedfunc.GLMatrixFunc;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;

public final class JOGLInit
{
  public static void main(
    final String args[])
    throws InterruptedException
  {
    final GLWindow window0 = JOGLInit.makeWindow("Window 0");
    final GLWindow window1 = JOGLInit.makeWindow("Window 1");

    while (window0.isVisible() || window1.isVisible()) {
      Thread.sleep(1000 / 60);
      System.out.println("thread "
        + Thread.currentThread().getId()
        + " sleep");
    }

    System.exit(0);
  }

  public static GLWindow makeWindow(
    final String name)
  {
    final GLProfile pro = GLProfile.getDefault();
    final GLCapabilities caps = new GLCapabilities(pro);
    final GLWindow window = GLWindow.create(caps);

    window.setSize(640, 480);
    window.setVisible(true);
    window.setTitle(name);
    window.addWindowListener(new WindowAdapter() {
      @Override public void windowDestroyNotify(
        final WindowEvent e)
      {
        // System.exit(0);
      }
    });
    window.addGLEventListener(new GLEventListener() {
      int quad_x = (int) (Math.random() * 640);
      int quad_y = (int) (Math.random() * 480);

      @Override public void display(
        final GLAutoDrawable drawable)
      {
        System.out.println("thread "
          + Thread.currentThread().getId()
          + " display");

        this.quad_x = (this.quad_x + 1) % 640;
        this.quad_y = (this.quad_y + 1) % 480;

        final GL2 g2 = drawable.getGL().getGL2();
        g2.glClearColor(0.0f, 0.0f, 0.3f, 1.0f);
        g2.glClear(GL.GL_COLOR_BUFFER_BIT);

        g2.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        g2.glLoadIdentity();
        g2.glOrtho(0, 640, 0, 480, 1, 100);
        g2.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        g2.glLoadIdentity();
        g2.glTranslated(0, 0, -1);

        g2.glBegin(GL2.GL_QUADS);
        {
          g2.glVertex2d(this.quad_x, this.quad_y + 10);
          g2.glVertex2d(this.quad_x, this.quad_y);
          g2.glVertex2d(this.quad_x + 10, this.quad_y);
          g2.glVertex2d(this.quad_x + 10, this.quad_y + 10);
        }
        g2.glEnd();
      }

      @Override public void dispose(
        final GLAutoDrawable drawable)
      {
        // Unused.
      }

      @Override public void init(
        final GLAutoDrawable drawable)
      {
        // Unused.
      }

      @Override public void reshape(
        final GLAutoDrawable drawable,
        final int x,
        final int y,
        final int width,
        final int height)
      {
        // Unused.
      }
    });

    final FPSAnimator animator = new FPSAnimator(window, 60);
    animator.start();

    return window;
  }
}
