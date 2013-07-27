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

import org.junit.Assume;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;

public class JOGLTestContextTest
{
  @SuppressWarnings("static-method") @Test public void testGL_ES2()
  {
    System.out.println("GL_ES2: " + JOGLTestContext.isOpenGLES2Supported());
  }

  @SuppressWarnings("static-method") @Test public void testGL_ES2_Open()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    Assume.assumeTrue(JOGLTestContext.isOpenGLES2Supported());
    JOGLTestContext.makeContextWithOpenGL_ES2();
  }

  @SuppressWarnings("static-method") @Test public void testGL_ES3()
  {
    System.out.println("GL_ES3: " + JOGLTestContext.isOpenGLES3Supported());
  }

  @SuppressWarnings("static-method") @Test public void testGL_ES3_Open()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    Assume.assumeTrue(JOGLTestContext.isOpenGLES3Supported());
    JOGLTestContext.makeContextWithOpenGL_ES3();
  }

  @SuppressWarnings("static-method") @Test public void testGL2_1()
  {
    System.out.println("GL2_1: "
      + JOGLTestContext.isOpenGL21WithExtensionsSupported());
  }

  @SuppressWarnings("static-method") @Test public void testGL2_1_Open()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    Assume.assumeTrue(JOGLTestContext.isOpenGL21WithExtensionsSupported());
    JOGLTestContext.makeContextWithOpenGL2_1();
  }

  @SuppressWarnings("static-method") @Test public void testGL3_0()
  {
    System.out.println("GL3_0: " + JOGLTestContext.isOpenGL30Supported());
  }

  @SuppressWarnings("static-method") @Test public void testGL3_0_Open()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    Assume.assumeTrue(JOGLTestContext.isOpenGL30Supported());
    JOGLTestContext.makeContextWithOpenGL3_0();
  }

  @SuppressWarnings("static-method") @Test public void testGL3_p()
  {
    System.out.println("GL3_p: " + JOGLTestContext.isOpenGL3pSupported());
  }

  @SuppressWarnings("static-method") @Test public void testGL3_p_Open()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    Assume.assumeTrue(JOGLTestContext.isOpenGL3pSupported());
    JOGLTestContext.makeContextWithOpenGL3_p();
  }
}
