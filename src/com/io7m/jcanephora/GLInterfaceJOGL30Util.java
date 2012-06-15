package com.io7m.jcanephora;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.PropertyUtils;
import com.io7m.jlog.Log;

class GLInterfaceJOGL30Util
{
  static GLInterface getGL(
    final @Nonnull GLContext context)
    throws IOException,
      ConstraintError,
      GLException
  {
    final Properties properties =
      PropertyUtils.loadFromFile("tests.properties");
    final Log log = new Log(properties, "com.io7m", "example");
    return new GLInterfaceJOGL30(context, log);
  }
}
