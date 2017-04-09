/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.core.JCGLIndexBufferType;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jnull.NullCheck;

import java.nio.ByteBuffer;

final class FakeIndexBuffer extends FakeBuffer implements JCGLIndexBufferType
{
  private final long indices;
  private final JCGLUnsignedType type;
  private final String image;

  FakeIndexBuffer(
    final FakeContext in_context,
    final int in_id,
    final long in_indices,
    final JCGLUnsignedType in_type,
    final ByteBuffer in_data,
    final JCGLUsageHint in_usage)
  {
    super(in_context, in_id, in_data, in_usage);
    this.indices = in_indices;
    this.type = NullCheck.notNull(in_type, "Type");

    this.image = String.format(
      "[JOGLIndexBuffer %d]", Integer.valueOf(this.glName()));
  }

  @Override
  public long indices()
  {
    return this.indices;
  }

  @Override
  public String toString()
  {
    return this.image;
  }

  @Override
  public JCGLUnsignedType type()
  {
    return this.type;
  }
}
