package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

public class TextureUnitTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
  {
    final TextureUnit tu0 = new TextureUnit(1);
    final TextureUnit tu1 = new TextureUnit(2);
    final TextureUnit tu2 = new TextureUnit(1);

    Assert.assertEquals(tu0, tu0);
    Assert.assertEquals(tu0, tu2);
    Assert.assertEquals(tu2, tu0);
    Assert.assertFalse(tu0.equals(tu1));
    Assert.assertFalse(tu0.equals(null));
    Assert.assertFalse(tu0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
  {
    final TextureUnit tu0 = new TextureUnit(1);
    final TextureUnit tu1 = new TextureUnit(2);

    Assert.assertTrue(tu0.hashCode() == tu0.hashCode());
    Assert.assertTrue(tu0.hashCode() != tu1.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
  {
    final TextureUnit tu0 = new TextureUnit(1);

    Assert.assertEquals(1, tu0.getIndex());
  }

  @SuppressWarnings("static-method") @Test public void testToString()
  {
    final TextureUnit tu0 = new TextureUnit(1);
    final TextureUnit tu1 = new TextureUnit(2);
    final TextureUnit tu2 = new TextureUnit(1);

    Assert.assertEquals(tu0.toString(), tu0.toString());
    Assert.assertEquals(tu0.toString(), tu2.toString());
    Assert.assertEquals(tu2.toString(), tu0.toString());
    Assert.assertFalse(tu0.toString().equals(tu1.toString()));
    Assert.assertFalse(tu0.toString().equals(null));
    Assert.assertFalse(tu0.toString().equals(Integer.valueOf(23)));
  }
}
