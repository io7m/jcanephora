package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class LWJGL30UsageHintES2Test
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
          GLTypeConversions.usageHintFromGL(GLTypeConversions
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
          GLTypeConversions.usageHintFromGL(GLTypeConversions
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
          GLTypeConversions.usageHintFromGL(GLTypeConversions
            .usageHintES2ToGL(h)),
          UsageHint.USAGE_STREAM_DRAW);
      }
    }
  }
}
