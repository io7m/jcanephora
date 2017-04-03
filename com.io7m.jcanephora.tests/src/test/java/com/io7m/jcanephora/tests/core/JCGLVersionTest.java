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

package com.io7m.jcanephora.tests.core;

import com.io7m.jcanephora.core.JCGLVersion;
import com.io7m.jcanephora.core.JCGLVersionNumber;
import org.junit.Assert;
import org.junit.Test;

public final class JCGLVersionTest
{
  @Test
  public void testEquals()
  {
    final JCGLVersion v0 = JCGLVersion.make(
      new JCGLVersionNumber(1, 0, 0), "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v1 = JCGLVersion.make(
      new JCGLVersionNumber(1, 0, 0), "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v2 = JCGLVersion.make(
      new JCGLVersionNumber(2, 0, 0), "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v3 = JCGLVersion.make(
      new JCGLVersionNumber(1, 0, 0), "OpenGL ES GLSL ES 1.0x");

    Assert.assertEquals(v0, v0);
    Assert.assertNotEquals(v0, null);
    Assert.assertNotEquals(v0, (Integer.valueOf(23)));
    Assert.assertEquals(v0, v1);
    Assert.assertNotEquals(v0, v2);
    Assert.assertNotEquals(v0, v3);
  }

  @Test
  public void testHashCode()
  {
    final JCGLVersion v0 = JCGLVersion.make(
      new JCGLVersionNumber(1, 0, 0), "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v1 = JCGLVersion.make(
      new JCGLVersionNumber(1, 0, 0), "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v2 = JCGLVersion.make(
      new JCGLVersionNumber(2, 0, 0), "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v3 = JCGLVersion.make(
      new JCGLVersionNumber(1, 0, 0), "OpenGL ES GLSL ES 1.0x");

    Assert.assertEquals((long) v0.hashCode(), (long) (v0.hashCode()));
    Assert.assertEquals((long) v0.hashCode(), (long) (v1.hashCode()));
    Assert.assertNotEquals((long) v0.hashCode(), (long) (v2.hashCode()));
    Assert.assertNotEquals((long) v0.hashCode(), (long) (v3.hashCode()));
  }

  @Test
  public void testIdentities()
  {
    final JCGLVersion v0 = JCGLVersion.make(
      new JCGLVersionNumber(1, 2, 3), "OpenGL ES GLSL ES 1.00");

    Assert.assertEquals("OpenGL ES GLSL ES 1.00", v0.getText());
    Assert.assertEquals(new JCGLVersionNumber(1, 2, 3), v0.getNumber());

    Assert.assertEquals(1L, (long) v0.getVersionMajor());
    Assert.assertEquals(2L, (long) v0.getVersionMinor());
    Assert.assertEquals(3L, (long) v0.getVersionMicro());
  }

  @Test
  public void testToString()
  {
    final JCGLVersion v0 = JCGLVersion.make(
      new JCGLVersionNumber(1, 0, 0), "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v1 = JCGLVersion.make(
      new JCGLVersionNumber(1, 0, 0), "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v2 = JCGLVersion.make(
      new JCGLVersionNumber(2, 0, 0), "OpenGL ES GLSL ES 1.00");
    final JCGLVersion v3 = JCGLVersion.make(
      new JCGLVersionNumber(1, 0, 0), "OpenGL ES GLSL ES 1.0x");

    Assert.assertEquals(v0.toString(), v0.toString());
    Assert.assertEquals(v0.toString(), v1.toString());
    Assert.assertNotEquals(v0.toString(), v2.toString());
    Assert.assertNotEquals(v0.toString(), v3.toString());
  }
}
