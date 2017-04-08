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

package com.io7m.jcanephora.core;

import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;

import java.util.Objects;

/**
 * A structure representing the version of the current OpenGL implementation.
 */

public final class JCGLVersion
{
  private final JCGLVersionNumber number;
  private final String text;

  private JCGLVersion(
    final JCGLVersionNumber in_number,
    final String in_text)
  {
    this.number = in_number;
    this.text = in_text;
  }

  /**
   * Construct a new version.
   *
   * @param number The number.
   * @param text   The version text from which the original number was
   *               produced.
   *
   * @return A new version.
   */

  public static JCGLVersion make(
    final JCGLVersionNumber number,
    final String text)
  {
    NullCheck.notNull(number, "Number");
    NullCheck.notNull(text, "Text");
    return new JCGLVersion(number, text);
  }

  @Override
  public boolean equals(
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
    final JCGLVersion other = (JCGLVersion) obj;
    return Objects.equals(this.number, other.number) && Objects.equals(
      this.text,
      other.text);
  }

  /**
   * @return The version number as a {@link JCGLVersionNumber} structure.
   */

  public JCGLVersionNumber getNumber()
  {
    return this.number;
  }

  /**
   * @return The original version string that was parsed to produce this version
   * structure.
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
   * @return The micro version of the implementation (typically zero).
   */

  public int getVersionMicro()
  {
    return this.number.getVersionMicro();
  }

  /**
   * @return The minor version of the implementation.
   */

  public int getVersionMinor()
  {
    return this.number.getVersionMinor();
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.number.hashCode();
    result = (prime * result) + this.text.hashCode();
    return result;
  }

  @Override
  public String toString()
  {
    final StringBuilder builder = new StringBuilder(16 + this.text.length());
    builder.append(this.getNumber());
    builder.append(" [");
    builder.append(this.text);
    builder.append("]");
    return builder.toString();
  }
}
