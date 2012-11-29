package com.io7m.jcanephora.examples;

import java.util.HashMap;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

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
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;

final class JOGL30ExampleRunner implements GLEventListener, KeyListener
{
  private static final int FRAMES_PER_SECOND = 60;

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

  private final Log                                                                 log;
  protected final GLWindow                                                          window;
  private final FPSAnimator                                                         animator;
  private GLInterfaceEmbedded_JOGL_ES2                                              gl;
  private final Filesystem                                                          filesystem;
  private final VectorM2I                                                           window_position;
  private final VectorM2I                                                           window_size;
  private ExampleConfig                                                             config;

  private final HashMap<String, PartialFunction<ExampleConfig, Example, Throwable>> examples;
  private final TreeSet<String>                                                     examples_names_sorted;
  private String                                                                    example_name_current;
  private Example                                                                   example_current;

  JOGL30ExampleRunner()
    throws Throwable
  {
    this.command_queue =
      new ConcurrentLinkedQueue<JOGL30ExampleRunner.Command>();

    this.examples_names_sorted = new TreeSet<String>();
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
    this.window.addKeyListener(this);

    this.animator = new FPSAnimator(JOGL30ExampleRunner.FRAMES_PER_SECOND);
    this.animator.setUpdateFPSFrames(
      JOGL30ExampleRunner.FRAMES_PER_SECOND,
      System.err);
    this.animator.add(this.window);
    this.animator.start();
  }

  @Override public void display(
    @SuppressWarnings("unused") final GLAutoDrawable drawable)
  {
    try {
      while (this.command_queue.peek() != null) {
        switch (this.command_queue.poll()) {
          case COMMAND_NEXT:
          {
            if (this.exampleHasNext()) {
              this.exampleNext();
            } else {
              this.exampleShutdown();
              this.exampleFirst();
            }
            break;
          }
          case COMMAND_PREVIOUS:
          {
            if (this.exampleHasPrevious()) {
              this.examplePrevious();
            }
            break;
          }
        }
      }

      if (this.example_current.hasShutDown() == false) {
        this.example_current.display();
      }
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

  private void exampleFirst()
  {
    try {
      this.example_name_current = this.examples_names_sorted.first();
      this.log.debug("First: " + this.example_name_current);
      this.example_current =
        this.examples.get(this.example_name_current).call(this.config);
    } catch (final Throwable x) {
      JOGL30ExampleRunner.fatal(x);
    }
  }

  private boolean exampleHasNext()
  {
    return this.examples_names_sorted.higher(this.example_name_current) != null;
  }

  private boolean exampleHasPrevious()
  {
    return this.examples_names_sorted.lower(this.example_name_current) != null;
  }

  private void exampleNext()
  {
    try {
      this.exampleShutdown();
      this.example_name_current =
        this.examples_names_sorted.higher(this.example_name_current);
      this.log.debug("Example: " + this.example_name_current);
      this.example_current =
        this.examples.get(this.example_name_current).call(this.config);
    } catch (final Throwable x) {
      JOGL30ExampleRunner.fatal(x);
    }
  }

  private void examplePrevious()
  {
    try {
      this.exampleShutdown();
      this.example_name_current =
        this.examples_names_sorted.lower(this.example_name_current);
      this.log.debug("Example: " + this.example_name_current);
      this.example_current =
        this.examples.get(this.example_name_current).call(this.config);
    } catch (final Throwable x) {
      JOGL30ExampleRunner.fatal(x);
    }
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
      "Textured quad",
      new PartialFunction<ExampleConfig, Example, Throwable>() {
        @Override public Example call(
          final ExampleConfig c)
          throws Throwable
        {
          JOGL30ExampleRunner.this.window.setTitle("Textured quad");
          return new ExampleTexturedQuad(c);
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

    for (final String name : this.examples.keySet()) {
      this.examples_names_sorted.add(name);
    }
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

      this.exampleFirst();

    } catch (final GLException e) {
      JOGL30ExampleRunner.fatal(e);
    } catch (final ConstraintError e) {
      JOGL30ExampleRunner.fatal(e);
    } catch (final Throwable e) {
      JOGL30ExampleRunner.fatal(e);
    }
  }

  @Override public void keyPressed(
    final KeyEvent e)
  {
    // Nothing
  }

  private enum Command
  {
    COMMAND_NEXT,
    COMMAND_PREVIOUS
  }

  private final ConcurrentLinkedQueue<Command> command_queue;

  @Override public void keyReleased(
    final KeyEvent e)
  {
    try {
      switch (e.getKeyChar()) {
        case 'n':
        {
          this.command_queue.add(Command.COMMAND_NEXT);
          break;
        }
        case 'p':
        {
          this.command_queue.add(Command.COMMAND_PREVIOUS);
          break;
        }
      }

    } catch (final Throwable x) {
      JOGL30ExampleRunner.fatal(x);
    }
  }

  private void exampleShutdown()
  {
    try {
      this.log.debug("Stopping: " + this.example_name_current);
      this.example_current.shutdown();
    } catch (final Throwable x) {
      JOGL30ExampleRunner.fatal(x);
    }
  }

  @Override public void keyTyped(
    final KeyEvent e)
  {
    // Nothing
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
      this.example_current.reshape(this.window_position, this.window_size);
    } catch (final GLException e) {
      JOGL30ExampleRunner.fatal(e);
    } catch (final GLCompileException e) {
      JOGL30ExampleRunner.fatal(e);
    } catch (final ConstraintError e) {
      JOGL30ExampleRunner.fatal(e);
    }
  }
}
