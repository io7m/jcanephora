package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class TextureFilterJOGL30Test
{
  @Test public void testFilterBijection()
  {
    for (final TextureFilter f : TextureFilter.values()) {
      Assert.assertEquals(f, GLInterfaceJOGL30
        .textureFilterFromGL(GLInterfaceJOGL30.textureFilterToGL(f)));
    }
  }

  @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceJOGL30.textureFilterFromGL(-1);
  }
}
