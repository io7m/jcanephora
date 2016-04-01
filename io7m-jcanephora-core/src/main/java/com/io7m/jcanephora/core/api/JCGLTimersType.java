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

package com.io7m.jcanephora.core.api;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLTimerQueryType;
import com.io7m.jcanephora.core.JCGLTimerQueryUsableType;

/**
 * <p>Interface to OpenGL timer queries.</p>
 *
 * <p>A <i>timer query</i> allows for retrieving the current time as it was when
 * the query was executed on the GPU.</p>
 *
 * <p>In more practical terms: Because (almost) all OpenGL commands are executed
 * <i>asynchronously</i>, it's not possible for the CPU to determine when a
 * particular command has finished executing on the GPU. Therefore, it's not
 * possible for the CPU to determine how long it took the GPU to execute one or
 * more commands. A <i>timer query</i> essentially records the current time on
 * the GPU when requested. This makes it possible to, for example, determine how
 * long a set of OpenGL commands took to execute by enqueueing a timer query
 * {@code Q0}, enqueueing a set of OpenGL commands {@code S}, and then enqueuing
 * a timer query {@code Q1}. The time it took to execute {@code S} is then
 * obviously given by {@code get(Q1) - get(Q0)}.</p>
 */

public interface JCGLTimersType
{
  /**
   * @return A new timer query
   *
   * @throws JCGLException On errors
   */

  JCGLTimerQueryType timerQueryAllocate()
    throws JCGLException;

  /**
   * Queue an update to the timer.
   *
   * @param q The query
   *
   * @throws JCGLException On errors
   */

  void timerQueryUpdate(
    JCGLTimerQueryUsableType q)
    throws JCGLException;

  /**
   * <p>Ask the GPU if the timer query has executed and therefore has a result
   * that can be fetched.</p>
   *
   * <p>Note that this function necessarily implies a GPU/CPU sync, and
   * therefore (for performance reasons) should ideally be called only after all
   * other rendering commands have been submitted for execution (perhaps at the
   * end of a frame).</p>
   *
   * @param q The query
   *
   * @return {@code true} if the result of the timer query is ready
   *
   * @throws JCGLException On errors
   * @see #timerQueryResultGet(JCGLTimerQueryUsableType)
   */

  boolean timerQueryResultIsReady(
    JCGLTimerQueryUsableType q)
    throws JCGLException;

  /**
   * <p>Retrieve the result for the timer query.</p>
   *
   * <p>The result for the query is the what was the current time when {@link
   * #timerQueryUpdate(JCGLTimerQueryUsableType)} was executed by the GPU.</p>
   *
   * @param q The query
   *
   * @return The result of the timer query in nanoseconds
   *
   * @throws JCGLException On errors
   */

  long timerQueryResultGet(
    JCGLTimerQueryUsableType q)
    throws JCGLException;

  /**
   * Delete the given timer query.
   *
   * @param q The timer query
   *
   * @throws JCGLException On errors
   */

  void timerQueryDelete(JCGLTimerQueryType q)
    throws JCGLException;
}
