/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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
package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class JOGL30UsageHintES2Test
{
  /**
   * ∀h. usageHintFromGL(usageHintToGL(h)) == h.
   */

  @SuppressWarnings("static-method") @Test public void testWrapBijection()
  {
    {
      final UsageHint hints[] =
        {
          UsageHint.USAGE_DYNAMIC_COPY,
          UsageHint.USAGE_DYNAMIC_DRAW,
          UsageHint.USAGE_DYNAMIC_READ };

      for (final UsageHint h : hints) {
        Assert.assertEquals(
          JOGL_GLTypeConversions.usageHintFromGL(JOGL_GLTypeConversions
            .usageHintES2ToGL(h)),
          UsageHint.USAGE_DYNAMIC_DRAW);
      }
    }

    {
      final UsageHint hints[] =
        {
          UsageHint.USAGE_STATIC_COPY,
          UsageHint.USAGE_STATIC_DRAW,
          UsageHint.USAGE_STATIC_READ };

      for (final UsageHint h : hints) {
        Assert.assertEquals(
          JOGL_GLTypeConversions.usageHintFromGL(JOGL_GLTypeConversions
            .usageHintES2ToGL(h)),
          UsageHint.USAGE_STATIC_DRAW);
      }
    }

    {
      final UsageHint hints[] =
        {
          UsageHint.USAGE_STREAM_COPY,
          UsageHint.USAGE_STREAM_DRAW,
          UsageHint.USAGE_STREAM_READ };

      for (final UsageHint h : hints) {
        Assert.assertEquals(
          JOGL_GLTypeConversions.usageHintFromGL(JOGL_GLTypeConversions
            .usageHintES2ToGL(h)),
          UsageHint.USAGE_STREAM_DRAW);
      }
    }
  }
}
