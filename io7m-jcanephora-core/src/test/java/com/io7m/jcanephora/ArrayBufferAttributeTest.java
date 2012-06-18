package com.io7m.jcanephora;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class ArrayBufferAttributeTest
{
  /**
   * Creating an attribute with no elements fails.
   */

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testAttributeElementsZero()
      throws ConstraintError
  {
    new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 0);
  }

  /**
   * Creating an attribute without a name fails.
   */

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testAttributeNameNull()
      throws ConstraintError
  {
    new ArrayBufferAttribute(null, GLScalarType.TYPE_FLOAT, 2);
  }
}
