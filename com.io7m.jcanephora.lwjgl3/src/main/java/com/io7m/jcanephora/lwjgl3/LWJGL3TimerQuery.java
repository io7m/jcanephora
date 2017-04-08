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

import com.io7m.jcanephora.core.JCGLTimerQueryType;
import com.io7m.jcanephora.core.JCGLTimerQueryUsableType;
import com.io7m.jnull.NullCheck;

final class LWJGL3TimerQuery extends LWJGL3ObjectUnshared
  implements JCGLTimerQueryType
{
  private boolean executed;

  LWJGL3TimerQuery(
    final LWJGL3Context ctx,
    final int id)
  {
    super(ctx, id);
  }

  static LWJGL3TimerQuery checkTimerQuery(
    final LWJGL3Context c,
    final JCGLTimerQueryUsableType q)
  {
    NullCheck.notNull(c, "Context");
    NullCheck.notNull(q, "Query");
    return (LWJGL3TimerQuery) LWJGL3CompatibilityChecks.checkAny(c, q);
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("[TimerQuery ");
    sb.append(super.getGLName());
    sb.append(']');
    return sb.toString();
  }

  public boolean isExecuted()
  {
    return this.executed;
  }

  public void setExecuted(final boolean e)
  {
    this.executed = e;
  }
}
