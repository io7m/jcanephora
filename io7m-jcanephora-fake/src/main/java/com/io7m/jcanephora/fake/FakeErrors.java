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
 * ACTION OF CONTRACT, NEFakeContextIGENCE OR OTHER TORTIOUS ACTION, ARISING
 * OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.JCGLErrorCodesType;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class FakeErrors implements JCGLErrorCodesType
{
  private static final int INVALID_OPERATION = 0x502;

  public static void check(
    final FakeContext gl)
    throws JCGLExceptionRuntime
  {
    final int e = gl.getError();
    if (e != 0) {
      throw new JCGLExceptionRuntime(e, "OpenGL error: code: " + e);
    }
  }

  private final FakeContext gl;
  private final LogType     log;

  FakeErrors(
    final FakeContext in_gl,
    final LogUsableType in_log)
  {
    this.gl = NullCheck.notNull(in_gl, "FakeContext");
    this.log = NullCheck.notNull(in_log, "Log").with("draw");
  }

  @Override public boolean errorCodeIsInvalidOperation(
    final int code)
  {
    return code == FakeErrors.INVALID_OPERATION;
  }

}
