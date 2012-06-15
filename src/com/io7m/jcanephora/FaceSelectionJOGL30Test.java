package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

public class FaceSelectionJOGL30Test
{
  @Test public void testFaceBijection()
  {
    for (final FaceSelection f : FaceSelection.values()) {
      Assert.assertEquals(GLInterfaceJOGL30
        .faceSelectionFromGL(GLInterfaceJOGL30.faceSelectionToGL(f)), f);
    }
  }

  @Test(expected = AssertionError.class) public void testNonsense()
  {
    GLInterfaceJOGL30.faceSelectionFromGL(-1);
  }
}
