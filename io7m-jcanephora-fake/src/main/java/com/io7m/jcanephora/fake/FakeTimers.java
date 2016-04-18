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
import com.io7m.jcanephora.core.JCGLExceptionQueryAlreadyRunning;
import com.io7m.jcanephora.core.JCGLExceptionQueryNotRunning;
import com.io7m.jcanephora.core.JCGLQueryResultAvailability;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLTimerQueryType;
import com.io7m.jcanephora.core.JCGLTimerQueryUsableType;
import com.io7m.jcanephora.core.api.JCGLTimersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

final class FakeTimers implements JCGLTimersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(FakeTimers.class);
  }

  private final FakeContext context;
  private @Nullable FakeTimerQuery running;

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
  public void timerQueryBegin(final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final FakeTimerQuery tq =
      FakeCompatibilityChecks.checkTimerQuery(this.context, q);
    JCGLResources.checkNotDeleted(q);

    if (this.running != null) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Timer query is already running.");
      sb.append(System.lineSeparator());
      sb.append("Query: ");
      sb.append(q);
      sb.append(System.lineSeparator());
      throw new JCGLExceptionQueryAlreadyRunning(sb.toString());
    }

    tq.setTimeStart(System.nanoTime());
    tq.setStarted(true);
    this.running = tq;
  }

  @Override
  public void timerQueryFinish(final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final FakeTimerQuery tq =
      FakeCompatibilityChecks.checkTimerQuery(this.context, q);
    JCGLResources.checkNotDeleted(q);

    if (!Objects.equals(q, this.running)) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("This timer query is not running.");
      sb.append(System.lineSeparator());
      if (this.running != null) {
        sb.append("Currently running query: ");
        sb.append(this.running);
        sb.append(System.lineSeparator());
      }
      throw new JCGLExceptionQueryNotRunning(sb.toString());
    }

    tq.setTimeEnd(System.nanoTime());
    this.running = null;
  }

  @Override
  public JCGLQueryResultAvailability timerQueryResultAvailability(
    final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final FakeTimerQuery tq =
      FakeCompatibilityChecks.checkTimerQuery(this.context, q);
    JCGLResources.checkNotDeleted(q);

    if (!tq.isStarted()) {
      return JCGLQueryResultAvailability.QUERY_RESULT_NOT_YET_REQUESTED;
    }

    return JCGLQueryResultAvailability.QUERY_RESULT_AVAILABLE;
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

    return tq.getTimeEnd() - tq.getTimeStart();
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

    if (Objects.equals(this.running, q)) {
      this.running = null;
    }

    tq.setDeleted();
  }
}
