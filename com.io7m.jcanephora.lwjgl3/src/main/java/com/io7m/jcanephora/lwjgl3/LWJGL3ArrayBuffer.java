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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLArrayBufferType;
import com.io7m.jcanephora.core.JCGLArrayBufferUsableType;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jnull.NullCheck;

final class LWJGL3ArrayBuffer extends LWJGL3Buffer implements
  JCGLArrayBufferType
{
  private final String image;

  LWJGL3ArrayBuffer(
    final LWJGL3Context in_context,
    final int in_id,
    final long in_size,
    final JCGLUsageHint in_usage)
  {
    super(in_context, in_id, in_size, in_usage);
    this.image = String.format("[LWJGLArrayBuffer %s]", super.toString());
  }

  static LWJGL3ArrayBuffer checkArray(
    final LWJGL3Context current,
    final JCGLArrayBufferUsableType x)
    throws JCGLExceptionWrongContext
  {
    NullCheck.notNull(x, "Context");
    return (LWJGL3ArrayBuffer) LWJGL3CompatibilityChecks.checkAny(current, x);
  }

  @Override
  public String toString()
  {
    return this.image;
  }
}
