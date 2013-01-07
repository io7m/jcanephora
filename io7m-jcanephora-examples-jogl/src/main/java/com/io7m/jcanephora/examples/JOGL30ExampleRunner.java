package com.io7m.jcanephora.examples;

import java.util.HashMap;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesImmutable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.PartialFunction;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLImplementationJOGL;
import com.io7m.jcanephora.GLInterface3;
import com.io7m.jcanephora.TextureLoader;
import com.io7m.jcanephora.TextureLoaderImageIO;
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
  private enum Command
  {
    COMMAND_NEXT,
    COMMAND_PREVIOUS
  }

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
  private GLInterface3                                                              gl;
  private GLImplementationJOGL                                                      gl_implementation;
  private final Filesystem                                                          filesystem;
  private final VectorM2I                                                           window_position;
  private final VectorM2I                                                           window_size;

  private ExampleConfig                                                             config;
  private final HashMap<String, PartialFunction<ExampleConfig, Example, Throwable>> examples;
  private final TreeSet<String>                                                     examples_names_sorted;
  private String                                                                    example_name_current;
  private Example                                                                   example_current;

  private final ConcurrentLinkedQueue<Command>                                      command_queue;
  private TextureLoader                                                             texture_loader;

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

    final GLProfile profile = GLProfile.get(GLProfile.GL3);
    final GLCapabilities requested_caps = new GLCapabilities(profile);
    requested_caps.setStencilBits(8);
    requested_caps.setDepthBits(24);
    requested_caps.setRedBits(8);
    requested_caps.setBlueBits(8);
    requested_caps.setGreenBits(8);

    this.window = GLWindow.create(requested_caps);
    this.window.setSize(this.window_size.x, this.window_size.y);
    this.window.setVisible(true);

    {
      final GLCapabilitiesImmutable actual_caps =
        this.window.getChosenGLCapabilities();
      this.log.info("Actual capabilities: " + actual_caps);

      if (actual_caps.getStencilBits() < 8) {
        this.log
          .critical("At least 8 bits of stencil buffer are required, got "
            + actual_caps.getStencilBits());
        System.exit(1);
      }

      if (actual_caps.getDepthBits() < 16) {
        this.log
          .critical("At least 16 bits of depth buffer are required, got "
            + actual_caps.getDepthBits());
        System.exit(1);
      }
    }

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

  private void exampleShutdown()
  {
    try {
      this.log.debug("Stopping: " + this.example_name_current);
      this.example_current.shutdown();
      System.gc();
    } catch (final Throwable x) {
      JOGL30ExampleRunner.fatal(x);
    }
  }

  private void examplesInitialize()
  {
    this.examples.put(
      "Stencil",
      new PartialFunction<ExampleConfig, Example, Throwable>() {
        @Override public Example call(
          final ExampleConfig c)
          throws Throwable
        {
          JOGL30ExampleRunner.this.window.setTitle("Stencil");
          return new ExampleStencil(c);
        }
      });

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
      "Shaders",
      new PartialFunction<ExampleConfig, Example, Throwable>() {
        @Override public Example call(
          final ExampleConfig c)
          throws Throwable
        {
          JOGL30ExampleRunner.this.window.setTitle("Shaders");
          return new ExampleShaders(c);
        }
      });

    this.examples.put(
      "Image textured quad",
      new PartialFunction<ExampleConfig, Example, Throwable>() {
        @Override public Example call(
          final ExampleConfig c)
          throws Throwable
        {
          JOGL30ExampleRunner.this.window.setTitle("Image textured quad");
          return new ExampleTexturedQuadImage(c);
        }
      });

    this.examples.put(
      "Animated textured quad",
      new PartialFunction<ExampleConfig, Example, Throwable>() {
        @Override public Example call(
          final ExampleConfig c)
          throws Throwable
        {
          JOGL30ExampleRunner.this.window.setTitle("Animated textured quad");
          return new ExampleTexturedQuadAnimatedNoise(c);
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

    this.examples.put(
      "FBO Triangle GL3",
      new PartialFunction<ExampleConfig, Example, Throwable>() {
        @Override public Example call(
          final ExampleConfig c)
          throws Throwable
        {
          JOGL30ExampleRunner.this.window.setTitle("FBO Triangle GL3");
          return new ExampleFBO3(c);
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
      this.texture_loader = new TextureLoaderImageIO();
      this.gl_implementation =
        new GLImplementationJOGL(drawable.getContext(), this.log);

      this.config =
        new ExampleConfig(
          this.gl_implementation,
          this.texture_loader,
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

  @SuppressWarnings("unused") @Override public void keyPressed(
    final KeyEvent e)
  {
    // Nothing
  }

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

  @SuppressWarnings("unused") @Override public void keyTyped(
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
