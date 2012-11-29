package com.io7m.jcanephora.examples;

import java.util.HashMap;
import java.util.Properties;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.PartialFunction;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceEmbedded_JOGL_ES2;
import com.io7m.jlog.Log;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.PathVirtual;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;

final class JOGL30ExampleRunner implements GLEventListener
{
  static void fatal(
    final Throwable e)
  {
    e.printStackTrace();
    System.exit(1);
  }

  @SuppressWarnings("unused") public static void main(
    final String args[])
  {
    try {
      new JOGL30ExampleRunner();
    } catch (final Throwable e) {
      JOGL30ExampleRunner.fatal(e);
    }
  }

  private final HashMap<String, PartialFunction<ExampleConfig, Example, Throwable>> examples;
  private final Log                                                                 log;
  protected final GLWindow                                                          window;
  private final FPSAnimator                                                         animator;
  private Example                                                                   current_example;
  private GLInterfaceEmbedded_JOGL_ES2                                              gl;
  private final Filesystem                                                          filesystem;
  private final VectorM2I                                                           window_position;
  private final VectorM2I                                                           window_size;
  private ExampleConfig                                                             config;

  JOGL30ExampleRunner()
    throws Throwable
  {
    this.examples =
      new HashMap<String, PartialFunction<ExampleConfig, Example, Throwable>>();
    this.examplesInitialize();

    this.window_position = new VectorM2I(0, 0);
    this.window_size = new VectorM2I(640, 480);

    final Properties p = new Properties();
    p.setProperty(
      "com.io7m.jcanephora.examples.logs.main.filesystem",
      "false");
    this.log = new Log(p, "com.io7m.jcanephora.examples", "main");

    this.filesystem = new Filesystem(this.log);
    this.filesystem.mountUnsafeClasspathItem(Example.class, new PathVirtual(
      "/"));

    final GLProfile pro = GLProfile.getDefault();
    final GLCapabilities caps = new GLCapabilities(pro);

    this.window = GLWindow.create(caps);
    this.window.setSize(this.window_size.x, this.window_size.y);
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
    @SuppressWarnings("unused") final GLAutoDrawable drawable)
  {
    try {
      this.current_example.display();
    } catch (final GLException e) {
      JOGL30ExampleRunner.fatal(e);
    } catch (final GLCompileException e) {
      JOGL30ExampleRunner.fatal(e);
    } catch (final ConstraintError e) {
      JOGL30ExampleRunner.fatal(e);
    }
  }

  @Override public void dispose(
    @SuppressWarnings("unused") final GLAutoDrawable drawable)
  {
    // Nothing.
  }

  private void examplesInitialize()
  {
    this.examples.put(
      "Triangle",
      new PartialFunction<ExampleConfig, Example, Throwable>() {
        @Override public Example call(
          final ExampleConfig c)
          throws Throwable
        {
          JOGL30ExampleRunner.this.window.setTitle("Triangle");
          return new ExampleTriangle(c);
        }
      });

    this.examples.put(
      "FBO Triangle",
      new PartialFunction<ExampleConfig, Example, Throwable>() {
        @Override public Example call(
          final ExampleConfig c)
          throws Throwable
        {
          JOGL30ExampleRunner.this.window.setTitle("FBO Triangle");
          return new ExampleFBO(c);
        }
      });
  }

  @Override public void init(
    final GLAutoDrawable drawable)
  {
    try {
      this.gl =
        new GLInterfaceEmbedded_JOGL_ES2(drawable.getContext(), this.log);

      this.config =
        new ExampleConfig(
          this.gl,
          this.log,
          this.filesystem,
          this.window_position,
          this.window_size);

      this.current_example = this.examples.get("Triangle").call(this.config);

    } catch (final GLException e) {
      JOGL30ExampleRunner.fatal(e);
    } catch (final ConstraintError e) {
      JOGL30ExampleRunner.fatal(e);
    } catch (final Throwable e) {
      JOGL30ExampleRunner.fatal(e);
    }
  }

  @Override public void reshape(
    @SuppressWarnings("unused") final GLAutoDrawable drawable,
    final int x,
    final int y,
    final int w,
    final int h)
  {
    this.window_position.x = x;
    this.window_position.y = y;
    this.window_size.x = w;
    this.window_size.y = h;

    try {
      this.current_example.reshape(this.window_position, this.window_size);
    } catch (final GLException e) {
      JOGL30ExampleRunner.fatal(e);
    } catch (final GLCompileException e) {
      JOGL30ExampleRunner.fatal(e);
    } catch (final ConstraintError e) {
      JOGL30ExampleRunner.fatal(e);
    }
  }
}
