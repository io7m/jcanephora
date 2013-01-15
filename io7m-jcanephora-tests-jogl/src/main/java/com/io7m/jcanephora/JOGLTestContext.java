package com.io7m.jcanephora;

import java.util.Properties;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLOffscreenAutoDrawable;
import javax.media.opengl.GLProfile;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public final class JOGLTestContext
{
  private static GLContext               context;
  private static GLOffscreenAutoDrawable buffer;
  static final String                    LOG_DESTINATION_OPENGL_ES_2_0;
  static final String                    LOG_DESTINATION_OPENGL_3_X;
  static final PathVirtual               GLSL_110_SHADER_PATH;
  static final PathVirtual               GLSL_ES_100_SHADER_PATH;

  static {
    LOG_DESTINATION_OPENGL_ES_2_0 = "jogl_es_2_0-test";
    LOG_DESTINATION_OPENGL_3_X = "jogl_3_x-test";

    try {
      GLSL_110_SHADER_PATH =
        new PathVirtual("/com/io7m/jcanephora/shaders/glsl110");
      GLSL_ES_100_SHADER_PATH =
        new PathVirtual("/com/io7m/jcanephora/shaders/glsles100");
    } catch (final ConstraintError e) {
      e.printStackTrace();
      throw new UnreachableCodeException();
    }
  }

  private static GLOffscreenAutoDrawable createOffscreenDrawable(
    final GLProfile profile,
    final int width,
    final int height)
  {
    final GLCapabilities cap = new GLCapabilities(profile);
    cap.setFBO(true);

    final GLDrawableFactory f = GLDrawableFactory.getFactory(profile);
    final GLOffscreenAutoDrawable k =
      f.createOffscreenAutoDrawable(null, cap, null, width, height, null);

    return k;
  }

  private static GLContext getContext(
    final GLProfile profile)
  {
    if (JOGLTestContext.buffer != null) {
      JOGLTestContext.context.release();
      JOGLTestContext.context.destroy();
      JOGLTestContext.buffer.destroy();
    }

    JOGLTestContext.buffer =
      JOGLTestContext.createOffscreenDrawable(profile, 640, 480);
    JOGLTestContext.context = JOGLTestContext.buffer.createContext(null);

    final int r = JOGLTestContext.context.makeCurrent();
    if (r == GLContext.CONTEXT_NOT_CURRENT) {
      throw new AssertionError("Could not make context current");
    }

    return JOGLTestContext.context;
  }

  private static FilesystemAPI getFS(
    final Log log)
  {
    try {
      final Filesystem fs = new Filesystem(log);
      fs.mountUnsafeClasspathItem(TestContext.class, new PathVirtual("/"));
      return fs;
    } catch (final FilesystemError e) {
      throw new java.lang.RuntimeException(e);
    } catch (final ConstraintError e) {
      throw new java.lang.RuntimeException(e);
    }
  }

  private static Log getLog(
    final String destination)
  {
    final Properties properties = new Properties();
    return new Log(properties, "com.io7m.jcanephora", destination);
  }

  public static boolean isOpenGL3Supported()
  {
    return GLProfile.isAvailable(GLProfile.GL3);
  }

  public static boolean isOpenGLES2Supported()
  {
    return GLProfile.isAvailable(GLProfile.GLES2);
  }

  public static TestContext makeContextWithOpenGL_ES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final Log log =
      JOGLTestContext.getLog(JOGLTestContext.LOG_DESTINATION_OPENGL_ES_2_0);
    final FilesystemAPI fs = JOGLTestContext.getFS(log);
    final GLImplementation gi =
      JOGLTestContext.makeGLImplementationWithOpenGL_ES2(log);
    return new TestContext(
      fs,
      gi,
      log,
      JOGLTestContext.GLSL_ES_100_SHADER_PATH);
  }

  public static TestContext makeContextWithOpenGL3_X()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final Log log =
      JOGLTestContext.getLog(JOGLTestContext.LOG_DESTINATION_OPENGL_3_X);
    final FilesystemAPI fs = JOGLTestContext.getFS(log);
    final GLImplementation gi =
      JOGLTestContext.makeGLImplementationWithOpenGL3_X(log);
    return new TestContext(fs, gi, log, JOGLTestContext.GLSL_110_SHADER_PATH);
  }

  private static GLImplementation makeGLImplementationWithOpenGL_ES2(
    final Log log)
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final GLContext ctx =
      JOGLTestContext.getContext(GLProfile.get(GLProfile.GLES2));
    return new GLImplementationJOGL(ctx, log);
  }

  private static GLImplementation makeGLImplementationWithOpenGL3_X(
    final Log log)
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final GLContext ctx =
      JOGLTestContext.getContext(GLProfile.get(GLProfile.GL3));
    return new GLImplementationJOGL(ctx, log);
  }

  private JOGLTestContext()
  {
    throw new UnreachableCodeException();
  }
}
