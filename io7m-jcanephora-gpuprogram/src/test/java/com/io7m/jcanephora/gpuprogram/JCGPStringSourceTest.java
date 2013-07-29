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

package com.io7m.jcanephora.gpuprogram;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLSLVersionNumber;

public class JCGPStringSourceTest
{
  @SuppressWarnings("static-method") @Test public void testChanged()
    throws ConstraintError,
      Exception
  {
    final JCGPStringSource fs = new JCGPStringSource("hello");
    Assert.assertFalse(fs.sourceChanged());

    final JCGPGeneratorContext context =
      new JCGPGeneratorContext(
        new JCGLSLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES);
    final ArrayList<String> output = new ArrayList<String>();
    fs.sourceGet(context, output);

    Assert.assertFalse(fs.sourceChanged());
  }

  @SuppressWarnings("static-method") @Test public void testFileEvaluate()
    throws ConstraintError,
      Exception
  {
    final JCGPStringSource fs = new JCGPStringSource("hello");
    final JCGPGeneratorContext context =
      new JCGPGeneratorContext(
        new JCGLSLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES);
    final ArrayList<String> output = new ArrayList<String>();
    fs.sourceGet(context, output);

    Assert.assertEquals("hello", output.get(0));
  }
}
