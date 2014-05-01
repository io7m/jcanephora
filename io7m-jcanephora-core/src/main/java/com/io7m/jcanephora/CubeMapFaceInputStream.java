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

import java.io.IOException;
import java.io.InputStream;

import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;

/**
 * An input stream representing a specific face of a cube map. The type
 * parameter <code>F</code> prevents the accidental mixing up of cube faces at
 * the type level.
 * 
 * @param <F>
 *          A type representing a cube map face.
 */

public final class CubeMapFaceInputStream<F extends CubeMapFaceKind> extends
  InputStream
{
  private final InputStream stream;

  /**
   * Construct a new stream based on the given stream.
   * 
   * @param in_stream
   *          The stream.
   */

  public CubeMapFaceInputStream(
    final InputStream in_stream)
  {
    this.stream = NullCheck.notNull(in_stream, "Input stream");
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
    final CubeMapFaceInputStream<?> other = (CubeMapFaceInputStream<?>) obj;
    return this.stream.equals(other.stream);
  }

  /**
   * @return The actual stream
   */

  public InputStream getStream()
  {
    return this.stream;
  }

  @Override public int hashCode()
  {
    return this.stream.hashCode();
  }

  @Override public int read()
    throws IOException
  {
    return this.stream.read();
  }
}
