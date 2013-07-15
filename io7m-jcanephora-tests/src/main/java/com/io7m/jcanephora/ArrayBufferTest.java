/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.io7m.jcanephora;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;

public class ArrayBufferTest
{
  private static ArrayBufferDescriptor type;

  static {
    try {
      final ArrayBufferAttribute[] at = new ArrayBufferAttribute[1];
      at[0] =
        new ArrayBufferAttribute("position", GLScalarType.TYPE_FLOAT, 3);
      ArrayBufferTest.type = new ArrayBufferDescriptor(at);
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException();
    }
  }

  private static ArrayBuffer makeArrayBuffer(
    final int id)
  {
    try {
      return new ArrayBuffer(id, 8, ArrayBufferTest.type);
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException();
    }
  }

  @SuppressWarnings("static-method") @Test public void testEquals()
  {
    final ArrayBuffer tu0 = ArrayBufferTest.makeArrayBuffer(1);
    final ArrayBuffer tu1 = ArrayBufferTest.makeArrayBuffer(2);
    final ArrayBuffer tu2 = ArrayBufferTest.makeArrayBuffer(1);

    Assert.assertEquals(tu0, tu0);
    Assert.assertEquals(tu0, tu2);
    Assert.assertEquals(tu2, tu0);
    Assert.assertFalse(tu0.equals(tu1));
    Assert.assertFalse(tu0.equals(null));
    Assert.assertFalse(tu0.equals(Integer.valueOf(23)));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
  {
    final ArrayBuffer tu0 = ArrayBufferTest.makeArrayBuffer(1);
    final ArrayBuffer tu1 = ArrayBufferTest.makeArrayBuffer(2);

    Assert.assertTrue(tu0.hashCode() == tu0.hashCode());
    Assert.assertTrue(tu0.hashCode() != tu1.hashCode());
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
    throws ConstraintError
  {
    final ArrayBuffer tu0 = ArrayBufferTest.makeArrayBuffer(1);

    Assert.assertEquals(1, tu0.getGLName());
    Assert.assertTrue(tu0.getDescriptor() == ArrayBufferTest.type);
    Assert.assertEquals(0, tu0.getRange().getLower());
    Assert.assertEquals(7, tu0.getRange().getUpper());
    Assert.assertEquals(3 * 4, tu0.getElementSizeBytes());

    Assert.assertEquals(0 * (3 * 4), tu0.getElementOffset(0));
    Assert.assertEquals(1 * (3 * 4), tu0.getElementOffset(1));
    Assert.assertEquals(2 * (3 * 4), tu0.getElementOffset(2));
    Assert.assertEquals(3 * (3 * 4), tu0.getElementOffset(3));
    Assert.assertEquals(4 * (3 * 4), tu0.getElementOffset(4));
    Assert.assertEquals(5 * (3 * 4), tu0.getElementOffset(5));
    Assert.assertEquals(6 * (3 * 4), tu0.getElementOffset(6));
  }

  @SuppressWarnings("static-method") @Test public void testToString()
  {
    final ArrayBuffer tu0 = ArrayBufferTest.makeArrayBuffer(1);
    final ArrayBuffer tu1 = ArrayBufferTest.makeArrayBuffer(2);
    final ArrayBuffer tu2 = ArrayBufferTest.makeArrayBuffer(1);

    Assert.assertEquals(tu0.toString(), tu0.toString());
    Assert.assertEquals(tu0.toString(), tu2.toString());
    Assert.assertEquals(tu2.toString(), tu0.toString());
    Assert.assertFalse(tu0.toString().equals(tu1.toString()));
    Assert.assertFalse(tu0.toString().equals(null));
    Assert.assertFalse(tu0.toString().equals(Integer.valueOf(23)));
  }
}
