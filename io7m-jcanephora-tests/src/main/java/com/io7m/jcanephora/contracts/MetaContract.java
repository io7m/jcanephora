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

package com.io7m.jcanephora.contracts;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLMeta;
import com.io7m.jcanephora.JCGLSLVersion;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.JCGLVersion;
import com.io7m.jcanephora.TestContext;

public abstract class MetaContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract JCGLMeta getGLMeta(
    TestContext tc);

  @Test public void testMetaStrings()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLMeta gl = this.getGLMeta(tc);

    final String vn = gl.metaGetVendor();
    final JCGLVersion vr = gl.metaGetVersion();
    final String r = gl.metaGetRenderer();
    final JCGLSLVersion sv = gl.metaGetSLVersion();

    Assert.assertNotNull(vn);
    Assert.assertNotNull(vr);
    Assert.assertNotNull(r);

    System.out.println("Vendor        : " + vn);
    System.out.println("Version       : " + vr);
    System.out.println("Renderer      : " + r);

    System.out.println("Version major : " + vr.getVersionMajor());
    System.out.println("Version minor : " + vr.getVersionMinor());
    System.out.println("API           : " + vr.getAPI());

    System.out.println("Shading language version major : "
      + sv.getVersionMajor());
    System.out.println("Shading language version minor : "
      + sv.getVersionMinor());
    System.out.println("Shading language API           : " + sv.getAPI());
    System.out.println("Shading language version text  : " + sv.getText());
  }
}
