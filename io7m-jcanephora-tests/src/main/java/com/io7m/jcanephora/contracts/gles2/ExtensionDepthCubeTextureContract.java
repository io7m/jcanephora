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

package com.io7m.jcanephora.contracts.gles2;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExtensionDepthCubeTexture;
import com.io7m.jcanephora.JCGLExtensionSupport;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.TextureCubeStatic;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.contracts.TestContract;

public abstract class ExtensionDepthCubeTextureContract implements
  TestContract
{
  public abstract
    JCGLExtensionSupport<JCGLExtensionDepthCubeTexture>
    getExtensionDepthCubeTexture(
      final @Nonnull TestContext tc);

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  @Test public void testAllocation()
    throws JCGLException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final JCGLExtensionSupport<JCGLExtensionDepthCubeTexture> es =
      this.getExtensionDepthCubeTexture(tc);
    Assume.assumeTrue(es.extensionGetSupport().isSome());

    final JCGLExtensionDepthCubeTexture e =
      ((Option.Some<JCGLExtensionDepthCubeTexture>) es.extensionGetSupport()).value;

    final TextureCubeStatic t =
      e.textureCubeStaticAllocateDepth16(
        "example",
        64,
        TextureWrapR.TEXTURE_WRAP_REPEAT,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    Assert.assertEquals(64, t.getRangeX().getInterval());
    Assert.assertEquals(64, t.getRangeY().getInterval());
    Assert.assertEquals(TextureWrapR.TEXTURE_WRAP_REPEAT, t.getWrapR());
    Assert.assertEquals(TextureWrapS.TEXTURE_WRAP_REPEAT, t.getWrapS());
    Assert
      .assertEquals(TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE, t.getWrapT());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_LINEAR,
      t.getMinificationFilter());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
      t.getMagnificationFilter());
  }
}
