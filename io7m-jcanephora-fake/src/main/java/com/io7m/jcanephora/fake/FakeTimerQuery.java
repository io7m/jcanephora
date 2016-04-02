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

import com.io7m.jcanephora.core.JCGLTimerQueryType;

final class FakeTimerQuery extends FakeObjectUnshared
  implements JCGLTimerQueryType
{
  private long time_start;
  private long time_end;

  FakeTimerQuery(
    final FakeContext ctx,
    final int id)
  {
    super(ctx, id);
  }

  long getTimeStart()
  {
    return this.time_start;
  }

  void setTimeStart(final long t)
  {
    this.time_start = t;
  }

  long getTimeEnd()
  {
    return this.time_end;
  }

  void setTimeEnd(final long t)
  {
    this.time_end = t;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("[TimerQuery ");
    sb.append(super.getGLName());
    sb.append(']');
    return sb.toString();
  }
}
