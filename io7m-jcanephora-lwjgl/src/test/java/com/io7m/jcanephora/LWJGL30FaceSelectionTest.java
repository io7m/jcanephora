package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.UnreachableCodeException;

public final class LWJGL30FaceSelectionTest
{
  /**
   * ∀f. faceSelectionFromGL(faceSelectionToGL(f)) = f.
   */

  @SuppressWarnings("static-method") @Test public void testFaceBijection()
  {
    for (final FaceSelection f : FaceSelection.values()) {
      Assert.assertEquals(GLInterfaceEmbedded_LWJGL_ES2_Actual
        .faceSelectionFromGL(GLInterfaceEmbedded_LWJGL_ES2_Actual
          .faceSelectionToGL(f)), f);
    }
  }

  @SuppressWarnings("static-method") @Test(
    expected = UnreachableCodeException.class) public void testNonsense()
  {
    GLInterfaceEmbedded_LWJGL_ES2_Actual.faceSelectionFromGL(-1);
  }
}
