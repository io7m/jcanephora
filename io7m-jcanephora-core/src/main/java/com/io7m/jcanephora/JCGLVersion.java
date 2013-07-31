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

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A structure representing the version of the current OpenGL implementation.
 */

@Immutable public final class JCGLVersion
{
  private final @Nonnull JCGLVersionNumber number;
  private final @Nonnull JCGLApi           api;
  private final @Nonnull String            text;

  JCGLVersion(
    final @Nonnull JCGLVersionNumber number,
    final @Nonnull JCGLApi api,
    final @Nonnull String text)
    throws ConstraintError
  {
    this.number = Constraints.constrainNotNull(number, "Number");
    this.text = Constraints.constrainNotNull(text, "Text");
    this.api = Constraints.constrainNotNull(api, "API");
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
   * <p>
   * Retrieve the API of the current implementation.
   * </p>
   */

  public @Nonnull JCGLApi getAPI()
  {
    return this.api;
  }

  /**
   * <p>
   * Retrieve the version number as a {@link JCGLVersionNumber} structure.
   * </p>
   */

  public @Nonnull JCGLVersionNumber getNumber()
  {
    return this.number;
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
    return this.number.getVersionMajor();
  }

  /**
   * <p>
   * Retrieve the micro version of the implementation (typically zero).
   * </p>
   */

  public int getVersionMicro()
  {
    return this.number.getVersionMicro();
  }

  /**
   * <p>
   * Retrieve the minor version of the implementation.
   * </p>
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
    builder.append("[JCGLVersion ");
    builder.append(this.getVersionMajor());
    builder.append(".");
    builder.append(this.getVersionMinor());
    builder.append(".");
    builder.append(this.getVersionMicro());
    builder.append(" ");
    builder.append(this.api.getName());
    builder.append(" [");
    builder.append(this.text);
    builder.append("]]");
    return builder.toString();
  }
}
