package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class TextureWrapLWJGL30Test
{
  @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.textureWrapFromGL(-1);
  }

  @Test public void testWrapBijection()
  {
    for (final TextureWrap w : TextureWrap.values()) {
      Assert.assertEquals(w, GLInterfaceLWJGL30
        .textureWrapFromGL(GLInterfaceLWJGL30.textureWrapToGL(w)));
    }
  }
}
