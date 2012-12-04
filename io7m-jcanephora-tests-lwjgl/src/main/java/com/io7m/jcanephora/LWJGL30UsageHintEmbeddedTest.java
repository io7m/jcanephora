package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public class LWJGL30UsageHintEmbeddedTest
{
  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceEmbedded_LWJGL_ES2_Actual.usageHintEmbeddedFromGL(-1);
  }

  /**
   * ∀h. usageHintEmbeddedFromGL(usageHintEmbeddedToGL(h)) == h.
   */

  @SuppressWarnings("static-method") @Test public void testWrapBijection()
  {
    for (final UsageHintEmbedded h : UsageHintEmbedded.values()) {
      Assert.assertEquals(h, GLInterfaceEmbedded_LWJGL_ES2_Actual
        .usageHintEmbeddedFromGL(GLInterfaceEmbedded_LWJGL_ES2_Actual
          .usageHintEmbeddedToGL(h)));
    }
  }
}
