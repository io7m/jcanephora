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
import com.io7m.jcanephora.GLInterfaceEmbedded_JOGL_ES2;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceEmbedded;
import com.io7m.jlog.Log;
import com.io7m.jtensors.VectorM2I;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.PathVirtual;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;

final class JOGL30ExampleRunner implements GLEventListener
{
  private final HashMap<String, PartialFunction<Parameters, Example, Throwable>> examples;
  private final Log                                                              log;
  private final GLWindow                                                         window;
  private final FPSAnimator                                                      animator;
  private final Example                                                          current_example;
  private final GLInterfaceEmbedded_JOGL_ES2                                                gl;
  private final Filesystem                                                       filesystem;
  private final Parameters                                                       parameters;
  private final VectorM2I                                                        window_position;
  private final VectorM2I                                                        window_size;

  JOGL30ExampleRunner()
    throws Throwable
  {
    this.examples =
      new HashMap<String, PartialFunction<Parameters, Example, Throwable>>();
    this.examplesInitialize();

    final Properties properties = new Properties();
    this.log = new Log(properties, "com.io7m.jcanephora.examples", "main");

    this.filesystem = new Filesystem(this.log);
    this.filesystem.mountUnsafeClasspathItem(Example.class, new PathVirtual(
      "/"));

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

    this.gl = new GLInterfaceEmbedded_JOGL_ES2(this.window.getContext(), this.log);
    this.parameters = new Parameters();
    this.parameters.log = this.log;
    this.parameters.gl = this.gl;
    this.parameters.filesystem = this.filesystem;

    this.animator = new FPSAnimator(60);
    this.animator.setUpdateFPSFrames(60, System.err);
    this.animator.add(this.window);
    this.animator.start();

    this.current_example =
      this.examples.get("ArrayBuffer").call(this.parameters);
    this.window.addGLEventListener(this);

    this.window_position = new VectorM2I();
    this.window_size = new VectorM2I();
  }

  private static final class Parameters
  {
    Parameters()
    {

    }

    protected GLInterfaceEmbedded gl;
    protected Log                 log;
    protected FilesystemAPI       filesystem;
  }

  private void examplesInitialize()
  {
    this.examples.put(
      "ArrayBuffer",
      new PartialFunction<Parameters, Example, Throwable>() {
        @Override public Example call(
          final Parameters p)
          throws Throwable
        {
          return new ExampleArrayBuffer(p.gl, p.filesystem, p.log);
        }
      });
  }

  static void fatal(
    final Throwable e)
  {
    e.printStackTrace();
    System.exit(1);
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

  @Override public void init(
    @SuppressWarnings("unused") final GLAutoDrawable drawable)
  {
    // Nothing.
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

  @SuppressWarnings("unused") public static void main(
    final String args[])
  {
    try {
      new JOGL30ExampleRunner();
    } catch (final Throwable e) {
      JOGL30ExampleRunner.fatal(e);
    }
  }
}
