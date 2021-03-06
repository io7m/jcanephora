/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.jogl;

import com.io7m.jcanephora.core.JCGLArrayBufferType;
import com.io7m.jcanephora.core.JCGLArrayBufferUsableType;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jnull.NullCheck;
import com.jogamp.opengl.GLContext;

final class JOGLArrayBuffer extends JOGLBuffer implements JCGLArrayBufferType
{
  private final String image;

  JOGLArrayBuffer(
    final GLContext in_context,
    final int in_id,
    final long in_size,
    final JCGLUsageHint in_usage)
  {
    super(in_context, in_id, in_size, in_usage);
    this.image = String.format("[JOGLArrayBuffer %s]", super.toString());
  }

  static JOGLArrayBuffer checkArray(
    final GLContext current,
    final JCGLArrayBufferUsableType x)
    throws JCGLExceptionWrongContext
  {
    NullCheck.notNull(x, "Array buffers");
    return (JOGLArrayBuffer) JOGLCompatibilityChecks.checkAny(current, x);
  }

  @Override
  public String toString()
  {
    return this.image;
  }
}
