package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class VertexShaderTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final VertexShader pr0 = new VertexShader(1, "xyz");
    final VertexShader pr1 = new VertexShader(2, "xyz");
    final VertexShader pr2 = new VertexShader(1, "xyz");

    Assert.assertEquals(pr0, pr0);
    Assert.assertEquals(pr0, pr2);
    Assert.assertEquals(pr2, pr0);
    Assert.assertFalse(pr0.equals(pr1));
    Assert.assertFalse(pr0.equals(null));
    Assert.assertFalse(pr0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
    throws ConstraintError
  {
    final VertexShader pr0 = new VertexShader(1, "xyz");
    final VertexShader pr1 = new VertexShader(2, "xyz");
    final VertexShader pr2 = new VertexShader(1, "xyz");
    final VertexShader pr3 = new VertexShader(2, "abc");
    final VertexShader pr4 = new VertexShader(1, "abc");

    Assert.assertTrue(pr0.hashCode() == pr0.hashCode());
    Assert.assertTrue(pr0.hashCode() == pr2.hashCode());
    Assert.assertTrue(pr2.hashCode() == pr0.hashCode());

    Assert.assertTrue(pr0.hashCode() != pr1.hashCode());
    Assert.assertTrue(pr0.hashCode() != pr3.hashCode());
    Assert.assertTrue(pr0.hashCode() != pr4.hashCode());

    Assert.assertTrue(pr3.hashCode() != pr4.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
    throws ConstraintError
  {
    final VertexShader pr0 = new VertexShader(1, "xyz");

    Assert.assertEquals(1, pr0.getGLName());
    Assert.assertEquals("xyz", pr0.getName());
  }

  @SuppressWarnings("static-method") @Test public void testToString()
    throws ConstraintError
  {
    final VertexShader pr0 = new VertexShader(1, "xyz");
    final VertexShader pr1 = new VertexShader(2, "xyz");
    final VertexShader pr2 = new VertexShader(1, "xyz");

    Assert.assertEquals(pr0.toString(), pr0.toString());
    Assert.assertEquals(pr0.toString(), pr2.toString());
    Assert.assertEquals(pr2.toString(), pr0.toString());
    Assert.assertFalse(pr0.toString().equals(pr1.toString()));
    Assert.assertFalse(pr0.toString().equals(null));
    Assert.assertFalse(pr0.toString().equals(Integer.valueOf(23)));
  }
}
