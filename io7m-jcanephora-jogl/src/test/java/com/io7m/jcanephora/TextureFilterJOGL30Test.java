package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class TextureFilterJOGL30Test
{
  @SuppressWarnings("static-method") @Test public void testFilterBijection()
  {
    for (final TextureFilter f : TextureFilter.values()) {
      Assert.assertEquals(f, GLInterfaceJOGL30
        .textureFilterFromGL(GLInterfaceJOGL30.textureFilterToGL(f)));
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public
    void
    testNonsense()
  {
    GLInterfaceJOGL30.textureFilterFromGL(-1);
  }
}
