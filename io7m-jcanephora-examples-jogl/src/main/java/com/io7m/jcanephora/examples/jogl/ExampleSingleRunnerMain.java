/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcanephora.examples.jogl;

import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLExceptionUnsupported;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.examples.core.ExampleType;
import com.io7m.jcanephora.jogl.JCGLImplementationJOGL;
import com.io7m.jcanephora.jogl.JCGLImplementationJOGLType;
import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;
import com.jogamp.nativewindow.WindowClosingProtocol;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A single example runner.
 */

public final class ExampleSingleRunnerMain
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(ExampleSingleRunnerMain.class);
  }

  private ExampleSingleRunnerMain()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Main program.
   *
   * @param args Command line arguments
   *
   * @throws Exception On errors
   */

  public static void main(final String[] args)
    throws Exception
  {
    if (args.length < 1) {
      ExampleSingleRunnerMain.LOG.info("usage: example-class");
      System.exit(1);
    }

    final ExampleType ex = ExampleSingleRunnerMain.lookupExample(args[0]);

    final GLProfile pro = GLProfile.get(GLProfile.GL3);
    final GLCapabilities caps = new GLCapabilities(pro);
    final GLWindow window = GLWindow.create(caps);
    window.setSize(640, 480);
    window.addGLEventListener(new ExampleEventListener(ex));
    window.setDefaultCloseOperation(
      WindowClosingProtocol.WindowClosingMode.DISPOSE_ON_CLOSE);
    window.setVisible(true);

    final Animator anim = new Animator(window);

    /**
     * Close the program when the window closes.
     */

    window.addWindowListener(new WindowAdapter()
    {
      @Override public void windowDestroyed(
        final WindowEvent e)
      {
        ExampleSingleRunnerMain.LOG.debug("Stopping animator");
        anim.stop();
        ExampleSingleRunnerMain.LOG.debug("Exiting");
        System.exit(0);
      }
    });

    anim.start();
  }

  private static ExampleType lookupExample(final String name)
    throws
    ClassNotFoundException,
    InstantiationException,
    IllegalAccessException
  {
    ExampleSingleRunnerMain.LOG.debug("looking up class {}", name);
    final Class<?> c = Class.forName(name);
    return (ExampleType) c.newInstance();
  }

  private static final class ExampleEventListener implements GLEventListener
  {
    private final ExampleType     example;
    private       JCGLContextType context;
    private       int             frame;

    ExampleEventListener(
      final ExampleType in_example)
    {
      this.example = NullCheck.notNull(in_example);
    }

    @Override public void init(
      final GLAutoDrawable drawable)
    {

    }

    @Override public void dispose(
      final GLAutoDrawable drawable)
    {
      this.example.onFinish(this.context.contextGetGL33());
    }

    @Override public void display(
      final GLAutoDrawable drawable)
    {
      try {
        if (this.frame == 0) {
          final GL gl = drawable.getGL();
          gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
          gl.glClear(
            GL.GL_COLOR_BUFFER_BIT
            | GL.GL_DEPTH_BUFFER_BIT
            | GL.GL_STENCIL_BUFFER_BIT);
          return;
        }

        if (this.frame == 1) {
          final JCGLImplementationJOGLType g =
            JCGLImplementationJOGL.getInstance();

          this.context = g.newContextFromWithSupplier(
            drawable.getContext(),
            (c) -> new DebugGL3(c.getGL().getGL3()),
            "main");
          this.example.onInitialize(this.context.contextGetGL33());
          this.example.onRender(this.context.contextGetGL33());
          return;
        }

        this.example.onRender(this.context.contextGetGL33());
      } catch (final JCGLExceptionUnsupported x) {
        ExampleSingleRunnerMain.LOG.error("unsupported: ", x);
      } catch (final JCGLExceptionNonCompliant x) {
        ExampleSingleRunnerMain.LOG.error("non compliant: ", x);
      } finally {
        ++this.frame;
      }
    }

    @Override public void reshape(
      final GLAutoDrawable drawable,
      final int x,
      final int y,
      final int width,
      final int height)
    {

    }
  }
}
