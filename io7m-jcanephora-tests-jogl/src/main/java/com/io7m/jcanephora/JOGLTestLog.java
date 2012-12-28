package com.io7m.jcanephora;

import java.util.Properties;

import com.io7m.jlog.Log;

final class JOGLTestLog
{
  private static final Log log;

  static {
    final Properties properties = new Properties();
    log = new Log(properties, "com.io7m.jcanephora", "jogl30-test");
  }

  static Log getLog()
  {
    return JOGLTestLog.log;
  }
}
