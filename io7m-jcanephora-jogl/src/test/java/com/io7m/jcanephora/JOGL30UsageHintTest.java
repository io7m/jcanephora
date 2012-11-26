package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public class JOGL30UsageHintTest
{
  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterface_JOGL30.usageHintFromGL(-1);
  }

  /**
   * ∀h. usageHintFromGL(usageHintToGL(h)) == h.
   */

  @SuppressWarnings("static-method") @Test public void testWrapBijection()
  {
    for (final UsageHint h : UsageHint.values()) {
      Assert
        .assertEquals(h, GLInterface_JOGL30.usageHintFromGL(GLInterface_JOGL30
          .usageHintToGL(h)));
    }
  }
}
