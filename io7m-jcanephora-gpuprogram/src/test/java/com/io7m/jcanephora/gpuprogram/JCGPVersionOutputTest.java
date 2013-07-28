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

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLUnsupportedException;

public class JCGPVersionOutputTest
{
  @SuppressWarnings("static-method") @Test(
    expected = JCGLUnsupportedException.class) public void testInvalid0()
    throws JCGLUnsupportedException
  {
    JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(2, 0, 0),
      JCGLApi.JCGL_ES);
  }

  @SuppressWarnings("static-method") @Test(
    expected = JCGLUnsupportedException.class) public void testInvalid1()
    throws JCGLUnsupportedException
  {
    JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(1, 0, 0),
      JCGLApi.JCGL_FULL);
  }

  @SuppressWarnings("static-method") @Test public void testValidStrings()
    throws JCGLUnsupportedException
  {
    Assert.assertEquals("#version 100", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(1, 0, 0),
      JCGLApi.JCGL_ES));
    Assert.assertEquals("#version 300", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(3, 0, 0),
      JCGLApi.JCGL_ES));

    Assert.assertEquals("#version 110", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(1, 10, 0),
      JCGLApi.JCGL_FULL));
    Assert.assertEquals("#version 120", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(1, 20, 0),
      JCGLApi.JCGL_FULL));
    Assert.assertEquals("#version 130", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(1, 30, 0),
      JCGLApi.JCGL_FULL));
    Assert.assertEquals("#version 140", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(1, 40, 0),
      JCGLApi.JCGL_FULL));
    Assert.assertEquals("#version 150", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(1, 50, 0),
      JCGLApi.JCGL_FULL));
    Assert.assertEquals("#version 330", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(3, 30, 0),
      JCGLApi.JCGL_FULL));
    Assert.assertEquals("#version 400", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(4, 0, 0),
      JCGLApi.JCGL_FULL));
    Assert.assertEquals("#version 410", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(4, 10, 0),
      JCGLApi.JCGL_FULL));
    Assert.assertEquals("#version 420", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(4, 20, 0),
      JCGLApi.JCGL_FULL));
    Assert.assertEquals("#version 430", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(4, 30, 0),
      JCGLApi.JCGL_FULL));
    Assert.assertEquals("#version 440", JCGPVersionOutput.toGLSL(
      new JCGLSLVersionNumber(4, 40, 0),
      JCGLApi.JCGL_FULL));
  }
}
