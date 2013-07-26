/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

import javax.annotation.Nonnull;

/**
 * A structure representing the version of the current OpenGL implementation.
 */

public final class JCGLVersion
{
  private final int             version_major;
  private final int             version_minor;
  private final int             version_micro;
  private final boolean         es;
  private final @Nonnull String text;

  JCGLVersion(
    final int version_major,
    final int version_minor,
    final int version_micro,
    final boolean es,
    final @Nonnull String text)
  {
    this.version_major = version_major;
    this.version_minor = version_minor;
    this.version_micro = version_micro;
    this.es = es;
    this.text = text;
  }

  @Override public boolean equals(
    final Object obj)
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
    if (this.es != other.es) {
      return false;
    }
    if (!this.text.equals(other.text)) {
      return false;
    }
    if (this.version_major != other.version_major) {
      return false;
    }
    if (this.version_micro != other.version_micro) {
      return false;
    }
    if (this.version_minor != other.version_minor) {
      return false;
    }
    return true;
  }

  /**
   * <p>
   * Retrieve the original version string that was parsed to produce this
   * version structure.
   * </p>
   */

  public @Nonnull String getText()
  {
    return this.text;
  }

  /**
   * <p>
   * Retrieve the major version of the implementation.
   * </p>
   */

  public int getVersionMajor()
  {
    return this.version_major;
  }

  /**
   * <p>
   * Retrieve the micro version of the implementation (typically zero).
   * </p>
   */

  public int getVersionMicro()
  {
    return this.version_micro;
  }

  /**
   * <p>
   * Retrieve the minor version of the implementation.
   * </p>
   */

  public int getVersionMinor()
  {
    return this.version_minor;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + (this.es ? 1231 : 1237);
    result = (prime * result) + this.text.hashCode();
    result = (prime * result) + this.version_major;
    result = (prime * result) + this.version_micro;
    result = (prime * result) + this.version_minor;
    return result;
  }

  /**
   * <p>
   * Return <code>true</code> if the implementation is OpenGL ES.
   * </p>
   */

  public boolean isES()
  {
    return this.es;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[JCGLVersion ");
    builder.append(this.version_major);
    builder.append(".");
    builder.append(this.version_minor);
    builder.append(".");
    builder.append(this.version_micro);
    builder.append(" ");
    builder.append(this.es ? "ES" : "");
    builder.append(" [");
    builder.append(this.text);
    builder.append("]]");
    return builder.toString();
  }

}
