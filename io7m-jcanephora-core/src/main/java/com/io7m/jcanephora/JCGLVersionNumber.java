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
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.UnreachableCodeException;

/**
 * <p>
 * A structure representing the version number of a given OpenGL
 * implementation, broken into "major", "minor", and "micro" components.
 * </p>
 */

@Immutable public final class JCGLVersionNumber implements
  Comparable<JCGLVersionNumber>
{
  private final int version_major;
  private final int version_minor;
  private final int version_micro;

  public JCGLVersionNumber(
    final int version_major,
    final int version_minor,
    final int version_micro)
  {
    this.version_major = version_major;
    this.version_minor = version_minor;
    this.version_micro = version_micro;
  }

  @Override public int compareTo(
    final @Nonnull JCGLVersionNumber other)
  {
    if (this.equals(other)) {
      return 0;
    }

    if (this.version_major < other.version_major) {
      return -1;
    }
    if (this.version_major > other.version_major) {
      return 1;
    }
    if (this.version_minor < other.version_minor) {
      return -1;
    }
    if (this.version_minor > other.version_minor) {
      return 1;
    }
    if (this.version_micro < other.version_micro) {
      return -1;
    }
    if (this.version_micro > other.version_micro) {
      return 1;
    }

    throw new UnreachableCodeException();
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
    final JCGLVersionNumber other = (JCGLVersionNumber) obj;
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
   * Retrieve the major version of the implementation.
   */

  public int getVersionMajor()
  {
    return this.version_major;
  }

  /**
   * Retrieve the micro version of the implementation (typically zero).
   */

  public int getVersionMicro()
  {
    return this.version_micro;
  }

  /**
   * Retrieve the minor version of the implementation.
   */

  public int getVersionMinor()
  {
    return this.version_minor;
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + this.version_major;
    result = (prime * result) + this.version_micro;
    result = (prime * result) + this.version_minor;
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[JCGLVersionNumber ");
    builder.append(this.version_major);
    builder.append(" ");
    builder.append(this.version_minor);
    builder.append(" ");
    builder.append(this.version_micro);
    builder.append("]");
    return builder.toString();
  }
}