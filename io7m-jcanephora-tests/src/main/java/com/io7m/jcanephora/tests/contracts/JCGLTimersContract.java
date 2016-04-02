/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionQueryAlreadyRunning;
import com.io7m.jcanephora.core.JCGLExceptionQueryNotRunning;
import com.io7m.jcanephora.core.JCGLQueryResultAvailability;
import com.io7m.jcanephora.core.JCGLTimerQueryType;
import com.io7m.jcanephora.core.api.JCGLTimersType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Timer contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLTimersContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract JCGLTimersType getTimers(String name);

  @Test
  public final void testIdentities()
  {
    final JCGLTimersType g_t = this.getTimers("main");

    final JCGLTimerQueryType q = g_t.timerQueryAllocate();
    Assert.assertFalse(q.isDeleted());
    g_t.timerQueryDelete(q);
    Assert.assertTrue(q.isDeleted());
  }

  @Test
  public final void testOrdering()
    throws Exception
  {
    final JCGLTimersType g_t = this.getTimers("main");

    final JCGLTimerQueryType q0 = g_t.timerQueryAllocate();

    g_t.timerQueryBegin(q0);

    // Do some work to waste time
    g_t.timerQueryAllocate();
    g_t.timerQueryAllocate();
    g_t.timerQueryAllocate();
    g_t.timerQueryAllocate();
    g_t.timerQueryAllocate();

    g_t.timerQueryFinish(q0);

    while (g_t.timerQueryResultAvailability(q0) == JCGLQueryResultAvailability.QUERY_RESULT_NOT_YET_AVAILABLE) {
      System.out.println("Waiting for q0: " + q0);
      Thread.sleep(1000L);
    }

    final long q0_r = g_t.timerQueryResultGet(q0);

    System.out.println("q0_r: " + q0_r);

    Assert.assertTrue(q0_r > 0);
  }

  @Test
  public final void testNotAvailable()
  {
    final JCGLTimersType g_t = this.getTimers("main");
    final JCGLTimerQueryType q = g_t.timerQueryAllocate();
    Assert.assertEquals(
      JCGLQueryResultAvailability.QUERY_RESULT_NOT_YET_REQUESTED,
      g_t.timerQueryResultAvailability(q));
  }

  @Test
  public final void testDeleteDeleted()
  {
    final JCGLTimersType g_t = this.getTimers("main");

    final JCGLTimerQueryType q = g_t.timerQueryAllocate();
    Assert.assertFalse(q.isDeleted());
    g_t.timerQueryDelete(q);
    Assert.assertTrue(q.isDeleted());

    this.expected.expect(JCGLExceptionDeleted.class);
    g_t.timerQueryDelete(q);
  }

  @Test
  public final void testBeginRunning()
  {
    final JCGLTimersType g_t = this.getTimers("main");

    final JCGLTimerQueryType q = g_t.timerQueryAllocate();

    g_t.timerQueryBegin(q);
    this.expected.expect(JCGLExceptionQueryAlreadyRunning.class);
    g_t.timerQueryBegin(q);
  }

  @Test
  public final void testBeginRunningOther()
  {
    final JCGLTimersType g_t = this.getTimers("main");

    final JCGLTimerQueryType q0 = g_t.timerQueryAllocate();
    final JCGLTimerQueryType q1 = g_t.timerQueryAllocate();

    g_t.timerQueryBegin(q0);
    this.expected.expect(JCGLExceptionQueryAlreadyRunning.class);
    g_t.timerQueryBegin(q1);
  }

  @Test
  public final void testFinishNotRunning()
  {
    final JCGLTimersType g_t = this.getTimers("main");

    final JCGLTimerQueryType q = g_t.timerQueryAllocate();

    this.expected.expect(JCGLExceptionQueryNotRunning.class);
    g_t.timerQueryFinish(q);
  }

  @Test
  public final void testFinishNotRunningOther()
  {
    final JCGLTimersType g_t = this.getTimers("main");

    final JCGLTimerQueryType q0 = g_t.timerQueryAllocate();
    final JCGLTimerQueryType q1 = g_t.timerQueryAllocate();

    g_t.timerQueryBegin(q0);

    this.expected.expect(JCGLExceptionQueryNotRunning.class);
    g_t.timerQueryFinish(q1);
  }
}
