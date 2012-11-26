package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public class JOGL30TextureFilterTest
{
  /**
   * ∀f. textureFilterFromGL(textureFilterToGL(f)) == f.
   */

  @SuppressWarnings("static-method") @Test public void testFilterBijection()
  {
    for (final TextureFilter f : TextureFilter.values()) {
      Assert.assertEquals(f, GLInterfaceEmbedded_JOGL_ES2_Actual
        .textureFilterFromGL(GLInterfaceEmbedded_JOGL_ES2_Actual.textureFilterToGL(f)));
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceEmbedded_JOGL_ES2_Actual.textureFilterFromGL(-1);
  }
}
