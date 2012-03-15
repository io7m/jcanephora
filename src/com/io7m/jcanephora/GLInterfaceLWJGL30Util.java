package com.io7m.jcanephora;

import java.io.IOException;
import java.util.Properties;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.PropertyUtils;
import com.io7m.jlog.Log;

class GLInterfaceLWJGL30Util
{
  static GLInterface getGL()
    throws IOException,
      ConstraintError
  {
    final Properties properties =
      PropertyUtils.loadFromFile("tests.properties");
    final Log log = new Log(properties, "com.io7m", "example");
    return new GLInterfaceLWJGL30(log);
  }
}
