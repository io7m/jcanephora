package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jcanephora.GLType.Type;

public class GLTypeJOGL30Test
{
  @Test public void testTypeBijection()
  {
    for (final Type u : GLType.Type.values()) {
      Assert.assertEquals(
        GLInterfaceJOGL30.typeFromGL(GLInterfaceJOGL30.typeToGL(u)),
        u);
    }
  }

  @Test(expected = AssertionError.class) public void testTypeFailure()
  {
    GLInterfaceJOGL30.typeFromGL(-1);
  }
}
