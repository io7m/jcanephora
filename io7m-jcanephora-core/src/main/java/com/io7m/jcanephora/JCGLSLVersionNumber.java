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

import com.io7m.jnull.Nullable;
import com.io7m.junreachable.UnreachableCodeException;

/**
 * <p>
 * A structure representing the version number of a given OpenGL shading
 * language implementation, broken into "major" and "minor" components.
 * </p>
 */

public final class JCGLSLVersionNumber implements
  Comparable<JCGLSLVersionNumber>
{
  private final int version_major;
  private final int version_minor;

  /**
   * Construct a version number.
   * 
   * @param major
   *          The major number.
   * @param minor
   *          The minor number.
   */

  public JCGLSLVersionNumber(
    final int major,
    final int minor)
  {
    this.version_major = major;
    this.version_minor = minor;
  }

  @Override public int compareTo(
    final JCGLSLVersionNumber other)
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

    throw new UnreachableCodeException();
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
    final JCGLSLVersionNumber other = (JCGLSLVersionNumber) obj;
    if (this.version_major != other.version_major) {
      return false;
    }
    if (this.version_minor != other.version_minor) {
      return false;
    }
    return true;
  }

  /**
   * @return The major version of the implementation.
   */

  public int getVersionMajor()
  {
    return this.version_major;
  }

  /**
   * @return The minor version of the implementation.
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
    result = (prime * result) + this.version_minor;
    return result;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[JCGLSLVersionNumber ");
    builder.append(this.version_major);
    builder.append(" ");
    builder.append(this.version_minor);
    builder.append("]");
    final String r = builder.toString();
    assert r != null;
    return r;
  }
}
