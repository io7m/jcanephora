package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public class JOGL30UsageHintES2Test
{
  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLTypeConversions.usageHintES2FromGL(-1);
  }

  /**
   * ∀h. usageHintFromGL(usageHintToGL(h)) == h.
   */

  @SuppressWarnings("static-method") @Test public void testWrapBijection()
  {
    for (final UsageHintES2 h : UsageHintES2.values()) {
      Assert.assertEquals(h, GLTypeConversions
        .usageHintES2FromGL(GLTypeConversions.usageHintES2ToGL(h)));
    }
  }
}
