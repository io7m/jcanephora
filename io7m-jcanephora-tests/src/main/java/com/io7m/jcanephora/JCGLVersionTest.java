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

public class JCGLVersionTest
{
  @SuppressWarnings("static-method") @Test public void testEquals()
    throws ConstraintError
  {
    final JCGLVersion v0 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v1 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v2 =
      JCGLVersion.make(
        new JCGLVersionNumber(2, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v3 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_FULL,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v4 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.0x");

    Assert.assertTrue(v0.equals(v0));
    Assert.assertFalse(v0.equals(null));
    Assert.assertFalse(v0.equals(Integer.valueOf(23)));
    Assert.assertTrue(v0.equals(v1));
    Assert.assertFalse(v0.equals(v2));
    Assert.assertFalse(v0.equals(v3));
    Assert.assertFalse(v0.equals(v4));
  }

  @SuppressWarnings("static-method") @Test public void testHashCode()
    throws ConstraintError
  {
    final JCGLVersion v0 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v1 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v2 =
      JCGLVersion.make(
        new JCGLVersionNumber(2, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v3 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_FULL,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v4 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.0x");

    Assert.assertTrue(v0.hashCode() == (v0.hashCode()));
    Assert.assertTrue(v0.hashCode() == (v1.hashCode()));
    Assert.assertFalse(v0.hashCode() == (v2.hashCode()));
    Assert.assertFalse(v0.hashCode() == (v3.hashCode()));
    Assert.assertFalse(v0.hashCode() == (v4.hashCode()));
  }

  @SuppressWarnings("static-method") @Test public void testIdentities()
    throws ConstraintError
  {
    final JCGLVersion v0 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.00");

    Assert.assertEquals(JCGLApi.JCGL_ES, v0.getAPI());
    Assert.assertEquals("OpenGL ES GLSL ES 1.00", v0.getText());
    Assert.assertEquals(new JCGLVersionNumber(1, 0, 0), v0.getNumber());
  }

  @SuppressWarnings("static-method") @Test public void testToString()
    throws ConstraintError
  {
    final JCGLVersion v0 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v1 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v2 =
      JCGLVersion.make(
        new JCGLVersionNumber(2, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v3 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_FULL,
        "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v4 =
      JCGLVersion.make(
        new JCGLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES,
        "OpenGL ES GLSL ES 1.0x");

    Assert.assertTrue(v0.toString().equals(v0.toString()));
    Assert.assertTrue(v0.toString().equals(v1.toString()));
    Assert.assertFalse(v0.toString().equals(v2.toString()));
    Assert.assertFalse(v0.toString().equals(v3.toString()));
    Assert.assertFalse(v0.toString().equals(v4.toString()));
  }
}
