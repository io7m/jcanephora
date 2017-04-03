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
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL33;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

final class LWJGL3Timers implements JCGLTimersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(LWJGL3Timers.class);
  }

  private final LWJGL3Context context;
  private @Nullable LWJGL3TimerQuery running;

  LWJGL3Timers(final LWJGL3Context c)
  {
    this.context = NullCheck.notNull(c);
  }

  @Override
  public JCGLTimerQueryType timerQueryAllocate()
    throws JCGLException
  {
    final int id = GL15.glGenQueries();
    final LWJGL3TimerQuery t = new LWJGL3TimerQuery(this.context, id);

    if (LWJGL3Timers.LOG.isDebugEnabled()) {
      LWJGL3Timers.LOG.debug("allocate {}", Integer.valueOf(t.getGLName()));
    }

    this.timerQueryResultAvailability(t);
    return t;
  }

  @Override
  public void timerQueryBegin(final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final LWJGL3TimerQuery tq =
      LWJGL3TimerQuery.checkTimerQuery(this.context, q);
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

    GL15.glBeginQuery(GL33.GL_TIME_ELAPSED, tq.getGLName());
    tq.setExecuted(true);
    this.running = tq;
  }

  @Override
  public void timerQueryFinish(final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final LWJGL3TimerQuery tq =
      LWJGL3TimerQuery.checkTimerQuery(this.context, q);
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

    GL15.glEndQuery(GL33.GL_TIME_ELAPSED);
    this.running = null;
  }

  @Override
  public JCGLQueryResultAvailability timerQueryResultAvailability(
    final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final LWJGL3TimerQuery tq =
      LWJGL3TimerQuery.checkTimerQuery(this.context, q);
    JCGLResources.checkNotDeleted(q);

    if (!tq.isExecuted()) {
      return JCGLQueryResultAvailability.QUERY_RESULT_NOT_YET_REQUESTED;
    }

    final int r =
      GL15.glGetQueryObjecti(tq.getGLName(), GL15.GL_QUERY_RESULT_AVAILABLE);
    final boolean available = r == GL11.GL_TRUE;
    if (available) {
      return JCGLQueryResultAvailability.QUERY_RESULT_AVAILABLE;
    }
    return JCGLQueryResultAvailability.QUERY_RESULT_NOT_YET_AVAILABLE;
  }

  @Override
  public long timerQueryResultGet(
    final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final LWJGL3TimerQuery tq =
      LWJGL3TimerQuery.checkTimerQuery(this.context, q);
    JCGLResources.checkNotDeleted(q);

    return GL33.glGetQueryObjecti64(tq.getGLName(), GL15.GL_QUERY_RESULT);
  }

  @Override
  public void timerQueryDelete(
    final JCGLTimerQueryType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final LWJGL3TimerQuery tq =
      LWJGL3TimerQuery.checkTimerQuery(this.context, q);
    JCGLResources.checkNotDeleted(q);

    GL15.glDeleteQueries(tq.getGLName());
    tq.setDeleted();

    if (Objects.equals(this.running, q)) {
      this.running = null;
    }

    if (LWJGL3Timers.LOG.isDebugEnabled()) {
      LWJGL3Timers.LOG.debug("delete {}", Integer.valueOf(tq.getGLName()));
    }
  }
}
