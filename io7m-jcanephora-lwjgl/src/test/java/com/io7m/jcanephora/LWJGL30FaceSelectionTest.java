package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public final class LWJGL30FaceSelectionTest
{
  /**
   * ∀f. faceSelectionFromGL(faceSelectionToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testFaceBijection()
  {
    for (final FaceSelection f : FaceSelection.values()) {
      Assert.assertEquals(GLInterfaceLWJGL30
        .faceSelectionFromGL(GLInterfaceLWJGL30.faceSelectionToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public
    void
    testNonsense()
  {
    GLInterfaceLWJGL30.faceSelectionFromGL(-1);
  }
}
