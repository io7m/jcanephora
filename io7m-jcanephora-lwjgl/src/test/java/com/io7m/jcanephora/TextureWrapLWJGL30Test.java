package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class TextureWrapLWJGL30Test
{
  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.textureWrapFromGL(-1);
  }

  @SuppressWarnings("static-method") @Test public void testWrapBijection()
  {
    for (final TextureWrap w : TextureWrap.values()) {
      Assert.assertEquals(w, GLInterfaceLWJGL30
        .textureWrapFromGL(GLInterfaceLWJGL30.textureWrapToGL(w)));
    }
  }
}
