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

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class ShaderUtilitiesTest
{
  @SuppressWarnings("static-method") @Test public void testEmptyLines()
    throws ConstraintError
  {
    final LinkedList<String> xs = new LinkedList<String>();
    xs.add("");
    xs.add(" ");
    xs.add("\n");
    xs.add("\t");
    xs.add("\r\n");
    Assert.assertTrue(ShaderUtilities.isEmpty(xs));
  }

  @SuppressWarnings("static-method") @Test public void testEmptyList()
    throws ConstraintError
  {
    Assert.assertTrue(ShaderUtilities.isEmpty(new LinkedList<String>()));
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testEmptyNull()
      throws ConstraintError
  {
    ShaderUtilities.isEmpty(null);
  }

  @SuppressWarnings("static-method") @Test(expected = ConstraintError.class) public
    void
    testEmptyNullLine()
      throws ConstraintError
  {
    final LinkedList<String> xs = new LinkedList<String>();
    xs.add(null);
    ShaderUtilities.isEmpty(xs);
  }

  @SuppressWarnings("static-method") @Test public void testNotEmpty()
    throws ConstraintError
  {
    final LinkedList<String> xs = new LinkedList<String>();
    xs.add("a");
    Assert.assertFalse(ShaderUtilities.isEmpty(xs));
  }
}
