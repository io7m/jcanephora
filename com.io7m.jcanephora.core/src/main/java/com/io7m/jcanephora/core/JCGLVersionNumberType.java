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
import com.io7m.junreachable.UnreachableCodeException;
import org.immutables.value.Value;

/**
 * <p>A structure representing the version number of a given OpenGL
 * implementation, broken into "major", "minor", and "micro" components.</p>
 */

@JCGLImmutableStyleType
@Value.Immutable
public interface JCGLVersionNumberType extends Comparable<JCGLVersionNumberType>
{
  /**
   * @return The major version of the implementation.
   */

  @Value.Parameter(order = 0)
  int major();

  /**
   * @return The minor version of the implementation.
   */

  @Value.Parameter(order = 1)
  int minor();

  /**
   * @return The micro version of the implementation.
   */

  @Value.Parameter(order = 2)
  int micro();

  @Override
  default int compareTo(
    final @Nullable JCGLVersionNumberType other)
  {
    final JCGLVersionNumberType o =
      NullCheck.notNull(other, "Other");

    if (this.equals(other)) {
      return 0;
    }

    if (this.major() < o.major()) {
      return -1;
    }
    if (this.major() > o.major()) {
      return 1;
    }
    if (this.minor() < o.minor()) {
      return -1;
    }
    if (this.minor() > o.minor()) {
      return 1;
    }
    if (this.micro() < o.micro()) {
      return -1;
    }
    if (this.micro() > o.micro()) {
      return 1;
    }

    throw new UnreachableCodeException();
  }
}
