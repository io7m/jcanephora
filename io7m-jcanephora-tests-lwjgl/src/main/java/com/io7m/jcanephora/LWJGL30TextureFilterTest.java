package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public class LWJGL30TextureFilterTest
{
  /**
   * ∀f. textureFilterMagFromGL(textureFilterMagToGL(f)) == f.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testFilterMagBijection()
  {
    for (final TextureFilterMagnification f : TextureFilterMagnification
      .values()) {
      Assert.assertEquals(f, GLTypeConversions
        .textureFilterMagFromGL(GLTypeConversions.textureFilterMagToGL(f)));
    }
  }

  /**
   * ∀f. textureFilterMinFromGL(textureFilterMinToGL(f)) == f.
   */

  @SuppressWarnings("static-method") @Test public
    void
    testFilterMinBijection()
  {
    for (final TextureFilterMinification f : TextureFilterMinification
      .values()) {
      Assert.assertEquals(f, GLTypeConversions
        .textureFilterMinFromGL(GLTypeConversions.textureFilterMinToGL(f)));
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testMagNonsense()
  {
    GLTypeConversions.textureFilterMagFromGL(-1);
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testMinNonsense()
  {
    GLTypeConversions.textureFilterMinFromGL(-1);
  }
}
