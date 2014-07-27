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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLSLVersion;
import com.io7m.jcanephora.JCGLVersion;
import com.io7m.jcanephora.api.JCGLMetaType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class FakeMeta implements JCGLMetaType
{
  private final FakeContext context;
  private final LogType     log;

  FakeMeta(
    final FakeContext in_context,
    final LogUsableType in_log)
  {
    this.context = NullCheck.notNull(in_context, "Context");
    this.log = NullCheck.notNull(in_log, "Log").with("meta");
  }

  @Override public int metaGetError()
    throws JCGLExceptionRuntime
  {
    return 0;
  }

  @Override public String metaGetRenderer()
    throws JCGLExceptionRuntime
  {
    return this.context.getRenderer();
  }

  @Override public JCGLSLVersion metaGetSLVersion()
    throws JCGLExceptionRuntime
  {
    return this.context.getSLVersion();
  }

  @Override public String metaGetVendor()
    throws JCGLExceptionRuntime
  {
    return this.context.getVendor();
  }

  @Override public JCGLVersion metaGetVersion()
    throws JCGLExceptionRuntime
  {
    return this.context.getVersion();
  }
}
