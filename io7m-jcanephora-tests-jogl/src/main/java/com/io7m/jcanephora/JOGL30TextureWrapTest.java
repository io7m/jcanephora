package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public class JOGL30TextureWrapTest
{
  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testRNonsense()
  {
    GLTypeConversions.textureWrapRFromGL(-1);
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testSNonsense()
  {
    GLTypeConversions.textureWrapSFromGL(-1);
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testTNonsense()
  {
    GLTypeConversions.textureWrapTFromGL(-1);
  }

  /**
   * ∀w. textureWrapRFromGL(textureWrapRToGL(w)) == w.
   */

  @SuppressWarnings("static-method") @Test public void testWrapRBijection()
  {
    for (final TextureWrapR w : TextureWrapR.values()) {
      Assert.assertEquals(w, GLTypeConversions
        .textureWrapRFromGL(GLTypeConversions.textureWrapRToGL(w)));
    }
  }

  /**
   * ∀w. textureWrapSFromGL(textureWrapSToGL(w)) == w.
   */

  @SuppressWarnings("static-method") @Test public void testWrapSBijection()
  {
    for (final TextureWrapS w : TextureWrapS.values()) {
      Assert.assertEquals(w, GLTypeConversions
        .textureWrapSFromGL(GLTypeConversions.textureWrapSToGL(w)));
    }
  }

  /**
   * ∀w. textureWrapTFromGL(textureWrapTToGL(w)) == w.
   */

  @SuppressWarnings("static-method") @Test public void testWrapTBijection()
  {
    for (final TextureWrapT w : TextureWrapT.values()) {
      Assert.assertEquals(w, GLTypeConversions
        .textureWrapTFromGL(GLTypeConversions.textureWrapTToGL(w)));
    }
  }
}
