/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.utilities.ShaderUtilities;
import com.io7m.jnull.NullCheckException;

@SuppressWarnings("static-method") public class ShaderUtilitiesTest
{
  @Test public void testEmptyLines()
  {
    final LinkedList<String> xs = new LinkedList<String>();
    xs.add("");
    xs.add(" ");
    xs.add("\n");
    xs.add("\t");
    xs.add("\r\n");
    Assert.assertTrue(ShaderUtilities.isEmpty(xs));
  }

  @Test public void testEmptyList()
  {
    Assert.assertTrue(ShaderUtilities.isEmpty(new LinkedList<String>()));
  }

  @SuppressWarnings("unchecked") @Test(expected = NullCheckException.class) public
    void
    testEmptyNull()
  {
    ShaderUtilities.isEmpty((List<String>) TestUtilities.actuallyNull());
  }

  @Test(expected = NullCheckException.class) public void testEmptyNullLine()
  {
    final LinkedList<String> xs = new LinkedList<String>();
    xs.add(null);
    ShaderUtilities.isEmpty(xs);
  }

  @Test public void testNotEmpty()
  {
    final LinkedList<String> xs = new LinkedList<String>();
    xs.add("a");
    Assert.assertFalse(ShaderUtilities.isEmpty(xs));
  }
}
