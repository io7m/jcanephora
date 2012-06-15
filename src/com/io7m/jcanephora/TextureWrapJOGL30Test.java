package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class TextureWrapJOGL30Test
{
  @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceJOGL30.textureWrapFromGL(-1);
  }

  @Test public void testWrapBijection()
  {
    for (final TextureWrap w : TextureWrap.values()) {
      Assert.assertEquals(w, GLInterfaceJOGL30
        .textureWrapFromGL(GLInterfaceJOGL30.textureWrapToGL(w)));
    }
  }
}