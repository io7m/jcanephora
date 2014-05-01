/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora;

import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;

/**
 * <p>
 * A structure representing the version of the current OpenGL shading language
 * implementation.
 * </p>
 */

public final class JCGLSLVersion
{
  /**
   * GLSL 1.10, as published with OpenGL 2.0.
   */

  public static final JCGLSLVersion GLSL_110;

  /**
   * GLSL 1.20, as published with OpenGL 2.1.
   */

  public static final JCGLSLVersion GLSL_120;

  /**
   * GLSL 1.30, as published with OpenGL 3.0.
   */

  public static final JCGLSLVersion GLSL_130;

  /**
   * GLSL 1.40, as published with OpenGL 3.1.
   */

  public static final JCGLSLVersion GLSL_140;

  /**
   * GLSL 1.50, as published with OpenGL 3.2.
   */

  public static final JCGLSLVersion GLSL_150;

  /**
   * GLSL 3.30, as published with OpenGL 3.3.
   */

  public static final JCGLSLVersion GLSL_330;

  /**
   * GLSL 4.0, as published with OpenGL 4.0.
   */

  public static final JCGLSLVersion GLSL_40;

  /**
   * GLSL 4.10, as published with OpenGL 4.1.
   */

  public static final JCGLSLVersion GLSL_410;

  /**
   * GLSL 4.20, as published with OpenGL 4.2.
   */

  public static final JCGLSLVersion GLSL_420;

  /**
   * GLSL 4.30, as published with OpenGL 4.3.
   */

  public static final JCGLSLVersion GLSL_430;

  /**
   * GLSL 4.40, as published with OpenGL 4.4.
   */

  public static final JCGLSLVersion GLSL_440;

  /**
   * GLSL ES 1.00, as published with OpenGL ES 2.0.
   */

  public static final JCGLSLVersion GLSL_ES_100;

  /**
   * GLSL ES 3.0, as published with OpenGL ES 3.0.
   */

  public static final JCGLSLVersion GLSL_ES_30;

  static {
    GLSL_ES_100 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(1, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.0");

    GLSL_ES_30 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(3, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 3.0");

    GLSL_110 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(1, 10),
        JCGLApi.JCGL_FULL,
        "1.10");

    GLSL_120 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(1, 20),
        JCGLApi.JCGL_FULL,
        "1.20");

    GLSL_130 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(1, 30),
        JCGLApi.JCGL_FULL,
        "1.30");

    GLSL_140 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(1, 40),
        JCGLApi.JCGL_FULL,
        "1.40");

    GLSL_150 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(1, 50),
        JCGLApi.JCGL_FULL,
        "1.50");

    GLSL_330 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(3, 30),
        JCGLApi.JCGL_FULL,
        "3.30");

    GLSL_40 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(4, 0),
        JCGLApi.JCGL_FULL,
        "4.0");

    GLSL_410 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(4, 10),
        JCGLApi.JCGL_FULL,
        "4.10");

    GLSL_420 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(4, 20),
        JCGLApi.JCGL_FULL,
        "4.20");

    GLSL_430 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(4, 30),
        JCGLApi.JCGL_FULL,
        "4.30");

    GLSL_440 =
      new JCGLSLVersion(
        new JCGLSLVersionNumber(4, 40),
        JCGLApi.JCGL_FULL,
        "4.40");
  }

  static JCGLSLVersion make(
    final JCGLSLVersionNumber number,
    final JCGLApi api,
    final String text)
  {
    return new JCGLSLVersion(
      NullCheck.notNull(number, "Number"),
      NullCheck.notNull(api, "API"),
      NullCheck.notNull(text, "Text"));
  }

  private final JCGLApi             api;
  private final JCGLSLVersionNumber number;
  private final String              text;

  private JCGLSLVersion(
    final JCGLSLVersionNumber number1,
    final JCGLApi api1,
    final String text1)
  {
    this.number = number1;
    this.text = text1;
    this.api = api1;
  }

  @Override public boolean equals(
    final @Nullable Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final JCGLSLVersion other = (JCGLSLVersion) obj;
    if (this.api != other.api) {
      return false;
    }
    if (!this.number.equals(other.number)) {
      return false;
    }
    if (!this.text.equals(other.text)) {
      return false;
    }
    return true;
  }

  /**
   * @return The API of the current implementation.
   */

  public JCGLApi getAPI()
  {
    return this.api;
  }

  /**
   * @return The version number as a {@link JCGLSLVersionNumber} structure.
   */

  public JCGLSLVersionNumber getNumber()
  {
    return this.number;
  }

  /**
   * @return The original version string that was parsed to produce this
   *         version structure.
   */

  public String getText()
  {
    return this.text;
  }

  /**
   * @return The major version of the implementation.
   */

  public int getVersionMajor()
  {
    return this.number.getVersionMajor();
  }

  /**
   * @return The minor version of the implementation.
   */

  public int getVersionMinor()
  {
    return this.number.getVersionMinor();
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.api.hashCode();
    result = (prime * result) + this.number.hashCode();
    result = (prime * result) + this.text.hashCode();
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[JCGLSLVersion ");
    builder.append(this.getVersionMajor());
    builder.append(".");
    builder.append(this.getVersionMinor());
    builder.append(" ");
    builder.append(this.api.getName());
    builder.append(" [");
    builder.append(this.text);
    builder.append("]]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
