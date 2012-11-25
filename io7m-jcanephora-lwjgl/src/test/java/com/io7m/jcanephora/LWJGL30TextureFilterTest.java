package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public class LWJGL30TextureFilterTest
{
  /**
   * ∀f. textureFilterFromGL(textureFilterToGL(f)) == f.
   */

  @SuppressWarnings("static-method") @Test public void testFilterBijection()
  {
    for (final TextureFilter f : TextureFilter.values()) {
      Assert.assertEquals(f, GLInterfaceLWJGL30
        .textureFilterFromGL(GLInterfaceLWJGL30.textureFilterToGL(f)));
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceLWJGL30.textureFilterFromGL(-1);
  }
}
