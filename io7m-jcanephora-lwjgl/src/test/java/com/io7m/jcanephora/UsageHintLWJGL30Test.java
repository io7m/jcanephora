package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class UsageHintLWJGL30Test
{
  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.usageHintFromGL(-1);
  }

  @SuppressWarnings("static-method") @Test public void testWrapBijection()
  {
    for (final UsageHint h : UsageHint.values()) {
      Assert.assertEquals(h, GLInterfaceLWJGL30
        .usageHintFromGL(GLInterfaceLWJGL30.usageHintToGL(h)));
    }
  }
}
