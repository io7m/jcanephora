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
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLTimerQueryType;
import com.io7m.jcanephora.core.JCGLTimerQueryUsableType;
import com.io7m.jcanephora.core.api.JCGLTimersType;
import com.io7m.jnull.NullCheck;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

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
    return new JOGLTimerQuery(
      this.context.getContext(), this.icache.get(0));
  }

  @Override
  public void timerQueryUpdate(
    final JCGLTimerQueryUsableType q)
    throws JCGLException
  {
    NullCheck.notNull(q);

    final JOGLTimerQuery tq =
      JOGLCompatibilityChecks.checkTimerQuery(this.context.getContext(), q);
    JCGLResources.checkNotDeleted(q);

    this.g3.glQueryCounter(tq.getGLName(), GL2ES2.GL_TIMESTAMP);
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
  }
}
