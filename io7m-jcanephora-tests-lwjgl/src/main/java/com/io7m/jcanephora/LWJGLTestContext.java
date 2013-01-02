package com.io7m.jcanephora;

import java.util.Properties;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Pbuffer;
import org.lwjgl.opengl.PixelFormat;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

public final class LWJGLTestContext
{
  private static final class Profile
  {
    int     version_major;

    int     version_minor;
    boolean version_es;
    int     width;
    int     height;

    Profile()
    {

    }
  }

  private static final Profile PROFILE_OPENGL_3_0;
  private static final Profile PROFILE_OPENGL_ES_2_0;
  private static final Profile PROFILE_OPENGL_3_1;

  static {
    PROFILE_OPENGL_3_0 = new Profile();
    LWJGLTestContext.PROFILE_OPENGL_3_0.version_major = 3;
    LWJGLTestContext.PROFILE_OPENGL_3_0.version_minor = 0;
    LWJGLTestContext.PROFILE_OPENGL_3_0.version_es = false;
    LWJGLTestContext.PROFILE_OPENGL_3_0.width = 640;
    LWJGLTestContext.PROFILE_OPENGL_3_0.height = 480;

    PROFILE_OPENGL_3_1 = new Profile();
    LWJGLTestContext.PROFILE_OPENGL_3_1.version_major = 3;
    LWJGLTestContext.PROFILE_OPENGL_3_1.version_minor = 1;
    LWJGLTestContext.PROFILE_OPENGL_3_1.version_es = false;
    LWJGLTestContext.PROFILE_OPENGL_3_1.width = 640;
    LWJGLTestContext.PROFILE_OPENGL_3_1.height = 480;

    PROFILE_OPENGL_ES_2_0 = new Profile();
    LWJGLTestContext.PROFILE_OPENGL_ES_2_0.version_major = 2;
    LWJGLTestContext.PROFILE_OPENGL_ES_2_0.version_minor = 0;
    LWJGLTestContext.PROFILE_OPENGL_ES_2_0.version_es = true;
    LWJGLTestContext.PROFILE_OPENGL_ES_2_0.width = 640;
    LWJGLTestContext.PROFILE_OPENGL_ES_2_0.height = 480;
  }

  static final String          LOG_DESTINATION_OPENGL_ES_2_0;
  static final String          LOG_DESTINATION_OPENGL_3_X;

  static final PathVirtual     GLSL_110_SHADER_PATH;
  static final PathVirtual     GLSL_ES_100_SHADER_PATH;

  static {
    LOG_DESTINATION_OPENGL_ES_2_0 = "lwjgl_es_2_0-test";
    LOG_DESTINATION_OPENGL_3_X = "lwjgl_3_x-test";

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

  private static Pbuffer       buffer = null;

  static Pbuffer createOffscreenDisplay(
    final Profile want)
  {
    try {
      final PixelFormat pixel_format = new PixelFormat(8, 24, 8);
      final ContextAttribs attribs =
        new ContextAttribs(want.version_major, want.version_minor);

      ContextAttribs attribs_w;
      if (want.version_es) {
        attribs_w = attribs.withProfileES(want.version_es);
      } else {
        attribs_w = attribs;
      }

      final Pbuffer pbuffer =
        new Pbuffer(
          want.width,
          want.height,
          pixel_format,
          null,
          null,
          attribs_w);
      pbuffer.makeCurrent();
      return pbuffer;
    } catch (final LWJGLException e) {
      e.printStackTrace();
      System.exit(1);
    }

    throw new UnreachableCodeException();
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
    // XXX: Surely not!
    return true;
  }

  public static boolean isOpenGLES2Supported()
  {
    /**
     * TODO: OpenGL ES2 support seems to be completely broken as of LWJGL
     * 2.8.4 - most functions erroneously raise a "function not supported"
     * error.
     */

    return false;
  }

  public static TestContext makeContextWithOpenGL_ES2()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final Log log =
      LWJGLTestContext.getLog(LWJGLTestContext.LOG_DESTINATION_OPENGL_ES_2_0);
    final FilesystemAPI fs = LWJGLTestContext.getFS(log);
    final GLImplementation gi =
      LWJGLTestContext.makeImplementationWithOpenGL_ES2(log);
    return new TestContext(
      fs,
      gi,
      log,
      LWJGLTestContext.GLSL_ES_100_SHADER_PATH);
  }

  public static TestContext makeContextWithOpenGL3_X()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final Log log =
      LWJGLTestContext.getLog(LWJGLTestContext.LOG_DESTINATION_OPENGL_3_X);
    final FilesystemAPI fs = LWJGLTestContext.getFS(log);
    final GLImplementation gi =
      LWJGLTestContext.makeImplementationWithOpenGL_3_X(log);
    return new TestContext(fs, gi, log, LWJGLTestContext.GLSL_110_SHADER_PATH);
  }

  public static GLImplementation makeImplementationWithOpenGL_3_X(
    final Log log)
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    LWJGLTestContext.openContext(LWJGLTestContext.PROFILE_OPENGL_3_0);
    return new GLImplementationLWJGL(log);
  }

  public static GLImplementation makeImplementationWithOpenGL_ES2(
    final Log log)
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    LWJGLTestContext.openContext(LWJGLTestContext.PROFILE_OPENGL_ES_2_0);
    return new GLImplementationLWJGL(log);
  }

  private static Pbuffer openContext(
    final Profile want)
  {
    if (LWJGLTestContext.buffer != null) {
      LWJGLTestContext.buffer.destroy();
    }

    LWJGLTestContext.buffer = LWJGLTestContext.createOffscreenDisplay(want);
    return LWJGLTestContext.buffer;
  }

  private LWJGLTestContext()
  {
    throw new UnreachableCodeException();
  }
}
