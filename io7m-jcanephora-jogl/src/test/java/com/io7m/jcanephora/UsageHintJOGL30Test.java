package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class UsageHintJOGL30Test
{
  @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceJOGL30.usageHintFromGL(-1);
  }

  @Test public void testWrapBijection()
  {
    for (final UsageHint h : UsageHint.values()) {
      Assert
        .assertEquals(h, GLInterfaceJOGL30.usageHintFromGL(GLInterfaceJOGL30
          .usageHintToGL(h)));
    }
  }
}
