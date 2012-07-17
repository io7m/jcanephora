package com.io7m.jcanephora;

import java.util.Properties;

import com.io7m.jlog.Log;

final class LWJGL30TestLog
{
  private static final Log log;

  static {
    final Properties properties = new Properties();
    log = new Log(properties, "com.io7m.jcanephora", "lwjgl30-test");
  }

  static Log getLog()
  {
    return LWJGL30TestLog.log;
  }
}
