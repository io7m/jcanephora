package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public class JOGL30TextureWrapTest
{
  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceJOGL30.textureWrapFromGL(-1);
  }

  /**
   * ∀w. textureWrapFromGL(textureWrapToGL(w)) == w.
   */

  @SuppressWarnings("static-method") @Test public void testWrapBijection()
  {
    for (final TextureWrap w : TextureWrap.values()) {
      Assert.assertEquals(w, GLInterfaceJOGL30
        .textureWrapFromGL(GLInterfaceJOGL30.textureWrapToGL(w)));
    }
  }
}
