package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public class JOGL30UsageHintTest
{
  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceJOGL30.usageHintFromGL(-1);
  }

  /**
   * ∀h. usageHintFromGL(usageHintToGL(h)) == h.
   */

  @SuppressWarnings("static-method") @Test public void testWrapBijection()
  {
    for (final UsageHint h : UsageHint.values()) {
      Assert
        .assertEquals(h, GLInterfaceJOGL30.usageHintFromGL(GLInterfaceJOGL30
          .usageHintToGL(h)));
    }
  }
}