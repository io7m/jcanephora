package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class TextureFilterLWJGL30Test
{
  @Test public void testFilterBijection()
  {
    for (final TextureFilter f : TextureFilter.values()) {
      Assert.assertEquals(f, GLInterfaceLWJGL30
        .textureFilterFromGL(GLInterfaceLWJGL30.textureFilterToGL(f)));
    }
  }
}
