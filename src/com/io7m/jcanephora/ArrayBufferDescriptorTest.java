package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class ArrayBufferDescriptorTest
{
  /**
   * Trying to pass an attribute without any elements fails.
   */

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testDescriptorAttributeElementsZero()
      throws ConstraintError
  {
    new ArrayBufferDescriptor(
      new ArrayBufferAttribute[] { new ArrayBufferAttribute(
        "position",
        GLScalarType.TYPE_FLOAT,
        0) });
  }

  /**
   * Trying to pass an attribute without a name fails.
   */

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testDescriptorAttributeNameNull()
      throws ConstraintError
  {
    new ArrayBufferDescriptor(
      new ArrayBufferAttribute[] { new ArrayBufferAttribute(
        null,
        GLScalarType.TYPE_FLOAT,
        3) });
  }

  /**
   * Trying to pass two attributes with the same name fails.
   */

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testDescriptorAttributeNamesNotUnique()
      throws ConstraintError
  {
    ArrayBufferAttribute ap = null;
    ArrayBufferAttribute an = null;

    try {
      ap = new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3);
      an = new ArrayBufferAttribute("position", GLScalarType.TYPE_INT, 4);
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    new ArrayBufferDescriptor(new ArrayBufferAttribute[] { ap, an });
  }

  /**
   * Sizes, offsets, and types are correct.
   */

  @Test public void testDescriptorAttributeOffsetsSizes()
    throws ConstraintError
  {
    final ArrayBufferAttribute ap =
      new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3);
    final ArrayBufferAttribute an =
      new ArrayBufferAttribute("normal", GLScalarType.TYPE_FLOAT, 3);
    final ArrayBufferAttribute au =
      new ArrayBufferAttribute("uv", GLScalarType.TYPE_FLOAT, 2);

    final ArrayBufferDescriptor a =
      new ArrayBufferDescriptor(new ArrayBufferAttribute[] { ap, an, au });

    Assert.assertEquals(0, a.getAttributeOffset("position"));
    Assert.assertEquals(
      GLScalarType.TYPE_FLOAT,
      a.getAttributeType("position"));
    Assert.assertEquals(3, a.getAttributeElements("position"));
    Assert.assertEquals(0, a.getElementOffset("position", 0));
    Assert.assertEquals(4, a.getElementOffset("position", 1));
    Assert.assertEquals(8, a.getElementOffset("position", 2));

    Assert.assertEquals(3 * 4, a.getAttributeOffset("normal"));
    Assert
      .assertEquals(GLScalarType.TYPE_FLOAT, a.getAttributeType("normal"));
    Assert.assertEquals(3, a.getAttributeElements("normal"));
    Assert.assertEquals((3 * 4) + 0, a.getElementOffset("normal", 0));
    Assert.assertEquals((3 * 4) + 4, a.getElementOffset("normal", 1));
    Assert.assertEquals((3 * 4) + 8, a.getElementOffset("normal", 2));

    Assert.assertEquals(6 * 4, a.getAttributeOffset("uv"));
    Assert.assertEquals(GLScalarType.TYPE_FLOAT, a.getAttributeType("uv"));
    Assert.assertEquals(2, a.getAttributeElements("uv"));
    Assert.assertEquals((6 * 4) + 0, a.getElementOffset("uv", 0));
    Assert.assertEquals((6 * 4) + 4, a.getElementOffset("uv", 1));

    Assert.assertEquals(8 * 4, a.getSize());
  }

  @Test(expected = ConstraintError.class) public
    void
    testDescriptorElementOffsetOutOfRange()
      throws ConstraintError
  {
    ArrayBufferDescriptor a = null;

    try {
      final ArrayBufferAttribute ap =
        new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3);
      a = new ArrayBufferDescriptor(new ArrayBufferAttribute[] { ap });
    } catch (final Exception e) {
      Assert.fail(e.getMessage());
    }

    assert a != null;
    a.getElementOffset("position", 3);
  }

  /**
   * Trying to declare an array buffer descriptor without any elements fails.
   */

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testDescriptorNone()
      throws ConstraintError
  {
    new ArrayBufferDescriptor(new ArrayBufferAttribute[] {});
  }
}
