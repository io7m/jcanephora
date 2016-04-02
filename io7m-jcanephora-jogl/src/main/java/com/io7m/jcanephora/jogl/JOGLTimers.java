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

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionQueryAlreadyRunning;
import com.io7m.jcanephora.core.JCGLExceptionQueryNotRunning;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLTimerQueryType;
import com.io7m.jcanephora.core.JCGLTimerQueryUsableType;
import com.io7m.jcanephora.core.api.JCGLTimersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.util.Objects;

final class JOGLTimers implements JCGLTimersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLTimers.class);
  }

  private final JOGLContext context;
  private final GL3 g3;
  private final IntBuffer icache;
  private final LongBuffer lcache;
  private @Nullable JOGLTimerQuery running;

  JOGLTimers(final JOGLContext c)
  {
    this.context = NullCheck.notNull(c);
    this.g3 = c.getGL3();
    this.icache = Buffers.newDirectIntBuffer(1);
    this.lcache = Buffers.newDirectLongBuffer(1);
  }

  @Override
  public JCGLTimerQueryType timerQueryAllocate()
    throws JCGLException
  {
    this.icache.rewind();
    this.g3.glGenQueries(1, this.icache);
    final int id = this.icache.get(0);
    final JOGLTimerQuery t = new JOGLTimerQuery(
      this.context.getContext(), id);

    if (JOGLTimers.LOG.isDebugEnabled()) {
      JOGLTimers.LOG.debug("allocate {}", Integer.valueOf(t.getGLName()));
    }

    return t;
  }

  @Override
  public void timerQueryBegin(final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final JOGLTimerQuery tq =
      JOGLCompatibilityChecks.checkTimerQuery(this.context.getContext(), q);
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

    this.g3.glBeginQuery(GL3.GL_TIME_ELAPSED, tq.getGLName());
    this.running = tq;
  }

  @Override
  public void timerQueryFinish(final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final JOGLTimerQuery tq =
      JOGLCompatibilityChecks.checkTimerQuery(this.context.getContext(), q);
    JCGLResources.checkNotDeleted(q);

    if (!Objects.equals(q, this.running)) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Timer query is already running.");
      sb.append(System.lineSeparator());
      if (this.running != null) {
        sb.append("Currently running query: ");
        sb.append(this.running);
        sb.append(System.lineSeparator());
      }
      throw new JCGLExceptionQueryNotRunning(sb.toString());
    }

    this.g3.glEndQuery(GL3.GL_TIME_ELAPSED);
    this.running = null;
  }

  @Override
  public boolean timerQueryResultIsReady(
    final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final JOGLTimerQuery tq =
      JOGLCompatibilityChecks.checkTimerQuery(this.context.getContext(), q);
    JCGLResources.checkNotDeleted(q);

    this.icache.rewind();
    this.g3.glGetQueryObjectiv(
      tq.getGLName(), GL3.GL_QUERY_RESULT_AVAILABLE, this.icache);
    return this.icache.get(0) == GL.GL_TRUE;
  }

  @Override
  public long timerQueryResultGet(
    final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final JOGLTimerQuery tq =
      JOGLCompatibilityChecks.checkTimerQuery(this.context.getContext(), q);
    JCGLResources.checkNotDeleted(q);

    this.lcache.rewind();
    this.g3.glGetQueryObjecti64v(
      tq.getGLName(), GL3.GL_QUERY_RESULT, this.lcache);
    return this.lcache.get(0);
  }

  @Override
  public void timerQueryDelete(
    final JCGLTimerQueryType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final JOGLTimerQuery tq =
      JOGLCompatibilityChecks.checkTimerQuery(this.context.getContext(), q);
    JCGLResources.checkNotDeleted(q);

    this.icache.put(0, tq.getGLName());
    this.g3.glDeleteQueries(1, this.icache);
    tq.setDeleted();

    if (Objects.equals(this.running, q)) {
      this.running = null;
    }

    if (JOGLTimers.LOG.isDebugEnabled()) {
      JOGLTimers.LOG.debug("delete {}", Integer.valueOf(tq.getGLName()));
    }
  }
}
