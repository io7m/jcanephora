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

package com.io7m.jcanephora.tests.contracts.gl2;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLExtensionDepthTextureType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.TestContract;
import com.io7m.jfunctional.OptionType;
import com.io7m.jfunctional.Some;

public abstract class ExtensionDepthTextureContract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract
    OptionType<JCGLExtensionDepthTextureType>
    getExtensionDepthTexture(
      final TestContext tc);

  @Test public void testAllocation16()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final OptionType<JCGLExtensionDepthTextureType> es =
      this.getExtensionDepthTexture(tc);
    Assume.assumeTrue(es.isSome());

    final JCGLExtensionDepthTextureType e =
      ((Some<JCGLExtensionDepthTextureType>) es).get();

    final Texture2DStaticType t =
      e.texture2DStaticAllocateDepth16(
        "example",
        64,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    Assert.assertEquals(
      TextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      t.textureGetFormat());
    Assert.assertEquals(64, t.textureGetRangeX().getInterval());
    Assert.assertEquals(128, t.textureGetRangeY().getInterval());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_REPEAT, t.textureGetWrapS());
    Assert.assertEquals(
      TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapT());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_LINEAR,
      t.textureGetMinificationFilter());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
      t.textureGetMagnificationFilter());
  }

  @Test public void testAllocation24()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final OptionType<JCGLExtensionDepthTextureType> es =
      this.getExtensionDepthTexture(tc);
    Assume.assumeTrue(es.isSome());

    final JCGLExtensionDepthTextureType e =
      ((Some<JCGLExtensionDepthTextureType>) es).get();

    final Texture2DStaticType t =
      e.texture2DStaticAllocateDepth24(
        "example",
        64,
        128,
        TextureWrapS.TEXTURE_WRAP_REPEAT,
        TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE,
        TextureFilterMinification.TEXTURE_FILTER_LINEAR,
        TextureFilterMagnification.TEXTURE_FILTER_NEAREST);

    Assert.assertEquals(
      TextureFormat.TEXTURE_FORMAT_DEPTH_24_4BPP,
      t.textureGetFormat());
    Assert.assertEquals(64, t.textureGetRangeX().getInterval());
    Assert.assertEquals(128, t.textureGetRangeY().getInterval());
    Assert
      .assertEquals(TextureWrapS.TEXTURE_WRAP_REPEAT, t.textureGetWrapS());
    Assert.assertEquals(
      TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE,
      t.textureGetWrapT());
    Assert.assertEquals(
      TextureFilterMinification.TEXTURE_FILTER_LINEAR,
      t.textureGetMinificationFilter());
    Assert.assertEquals(
      TextureFilterMagnification.TEXTURE_FILTER_NEAREST,
      t.textureGetMagnificationFilter());
  }
}
