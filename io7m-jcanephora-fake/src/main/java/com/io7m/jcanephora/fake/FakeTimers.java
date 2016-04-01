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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLTimerQueryType;
import com.io7m.jcanephora.core.JCGLTimerQueryUsableType;
import com.io7m.jcanephora.core.api.JCGLTimersType;
import com.io7m.jnull.NullCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class FakeTimers implements JCGLTimersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(FakeTimers.class);
  }

  private final FakeContext context;

  FakeTimers(final FakeContext c)
  {
    this.context = NullCheck.notNull(c);
  }

  @Override
  public JCGLTimerQueryType timerQueryAllocate()
    throws JCGLException
  {
    return new FakeTimerQuery(this.context, this.context.getFreshID());
  }

  @Override
  public void timerQueryUpdate(
    final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final FakeTimerQuery tq =
      FakeCompatibilityChecks.checkTimerQuery(this.context, q);
    JCGLResources.checkNotDeleted(q);

    tq.update(System.nanoTime());
  }

  @Override
  public boolean timerQueryResultIsReady(
    final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final FakeTimerQuery tq =
      FakeCompatibilityChecks.checkTimerQuery(this.context, q);
    JCGLResources.checkNotDeleted(q);
    return true;
  }

  @Override
  public long timerQueryResultGet(
    final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final FakeTimerQuery tq =
      FakeCompatibilityChecks.checkTimerQuery(this.context, q);
    JCGLResources.checkNotDeleted(q);

    return tq.getTime();
  }

  @Override
  public void timerQueryDelete(
    final JCGLTimerQueryType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final FakeTimerQuery tq =
      FakeCompatibilityChecks.checkTimerQuery(this.context, q);
    JCGLResources.checkNotDeleted(q);

    tq.setDeleted();
  }
}
