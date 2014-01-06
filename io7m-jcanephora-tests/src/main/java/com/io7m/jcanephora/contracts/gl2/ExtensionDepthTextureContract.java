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

package com.io7m.jcanephora.contracts.gl2;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLExtensionDepthTexture;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.contracts.TestContract;

public abstract class ExtensionDepthTextureContract implements TestContract
{
  public abstract Option<JCGLExtensionDepthTexture> getExtensionDepthTexture(
    final @Nonnull TestContext tc);

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  @Test public void testAllocation16()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final Option<JCGLExtensionDepthTexture> es =
      this.getExtensionDepthTexture(tc);
    Assume.assumeTrue(es.isSome());

    final JCGLExtensionDepthTexture e =
      ((Option.Some<JCGLExtensionDepthTexture>) es).value;

    final Texture2DStatic t =
      e.texture2DStaticAllocateDepth16(
        "example",
        64,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    Assert.assertEquals(TextureType.TEXTURE_TYPE_DEPTH_16_2BPP, t.getType());
    Assert.assertEquals(64, t.getRangeX().getInterval());
    Assert.assertEquals(128, t.getRangeY().getInterval());
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

  @Test public void testAllocation24()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final Option<JCGLExtensionDepthTexture> es =
      this.getExtensionDepthTexture(tc);
    Assume.assumeTrue(es.isSome());

    final JCGLExtensionDepthTexture e =
      ((Option.Some<JCGLExtensionDepthTexture>) es).value;

    final Texture2DStatic t =
      e.texture2DStaticAllocateDepth24(
        "example",
        64,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    Assert.assertEquals(TextureType.TEXTURE_TYPE_DEPTH_24_4BPP, t.getType());
    Assert.assertEquals(64, t.getRangeX().getInterval());
    Assert.assertEquals(128, t.getRangeY().getInterval());
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
