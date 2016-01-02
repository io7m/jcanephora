/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUsableType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLContextUsableType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jcanephora.texture_loader.core.JCGLTLAsyncTextureLoaderType;
import com.io7m.jcanephora.texture_loader.core.JCGLTLSoftAsyncTexture2DType;
import com.io7m.jcanephora.texture_loader.core.JCGLTLTextureDataProviderType;
import com.io7m.jcanephora.texture_loader.core.JCGLTLTextureUpdateProviderType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Texture loader contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLTLAsyncTextureLoaderContract extends JCGLContract
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JCGLTLAsyncTextureLoaderContract.class);
  }

  private ExecutorService              exec;
  private JCGLTLAsyncTextureLoaderType loader;
  private JCGLTexture2DType            texture_default;
  private JCGLTexture2DType            texture_error;

  protected abstract JCGLTLTextureDataProviderType getDataProvider();

  protected abstract JCGLTLTextureUpdateProviderType getUpdateProvider();

  protected abstract JCGLTLAsyncTextureLoaderType getLoader(
    JCGLTLTextureDataProviderType data,
    JCGLTLTextureUpdateProviderType updates,
    JCGLContextUsableType c,
    Executor e);

  protected abstract JCGLSharedContextPair<JCGLTexturesType>
  getTexturesSharedWith(
    String name,
    String shared);

  @Before
  public final void setupResources()
  {
    JCGLTLAsyncTextureLoaderContract.LOG.debug("setting up i/o executor");
    this.exec = Executors.newFixedThreadPool(1, r -> {
      final Thread t = new Thread(r);
      t.setName("io");
      return t;
    });

    final JCGLSharedContextPair<JCGLTexturesType> p =
      this.getTexturesSharedWith("main", "alt");

    final JCGLContextType ca = p.getMasterContext();
    final JCGLContextType cb = p.getSlaveContext();
    final JCGLTexturesType ga = p.getMasterValue();
    final JCGLTexturesType gb = p.getSlaveValue();

    Assert.assertTrue(ca.contextIsCurrent());
    Assert.assertFalse(cb.contextIsCurrent());

    final JCGLTextureUnitType unit0 = ga.textureGetUnits().get(0);
    this.texture_default = ga.texture2DAllocate(
      unit0,
      2L,
      2L,
      JCGLTextureFormat.TEXTURE_FORMAT_R_8_1BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    this.texture_error = ga.texture2DAllocate(
      unit0,
      2L,
      2L,
      JCGLTextureFormat.TEXTURE_FORMAT_R_8_1BPP,
      JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
      JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
      JCGLTextureFilterMinification.TEXTURE_FILTER_NEAREST,
      JCGLTextureFilterMagnification.TEXTURE_FILTER_NEAREST);
    ca.contextReleaseCurrent();

    final JCGLTLTextureDataProviderType data =
      this.getDataProvider();
    final JCGLTLTextureUpdateProviderType updates =
      this.getUpdateProvider();
    this.loader =
      this.getLoader(data, updates, ca, this.exec);
  }

  @After
  public final void shutdownResources()
    throws Exception
  {
    JCGLTLAsyncTextureLoaderContract.LOG.debug("shutting down i/o executor");
    this.exec.shutdownNow();
    this.exec.awaitTermination(30L, TimeUnit.SECONDS);

    if (!this.loader.isDeleted()) {
      this.loader.shutDownSynchronously(30L, TimeUnit.SECONDS);
      Assert.assertTrue(this.loader.isDeleted());
    }
  }

  @Test
  public final void testStartupShutdown()
    throws Exception
  {
    Assert.assertFalse(this.loader.isDeleted());
    this.loader.shutDownSynchronously(1L, TimeUnit.MINUTES);
    Assert.assertTrue(this.loader.isDeleted());
  }

  @Test
  public final void testLoadObserve()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final InputStream s = c.getResourceAsStream("basn4a08.png");

    final JCGLTLSoftAsyncTexture2DType at =
      this.loader.loadTextureFromStream(
        s,
        this.texture_default,
        this.texture_error,
        JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
        JCGLTextureWrapS.TEXTURE_WRAP_REPEAT,
        JCGLTextureWrapT.TEXTURE_WRAP_REPEAT,
        JCGLTextureFilterMinification.TEXTURE_FILTER_LINEAR,
        JCGLTextureFilterMagnification.TEXTURE_FILTER_LINEAR);

    final JCGLTexture2DUsableType pre_t = at.get();
    Assert.assertEquals(pre_t, this.texture_default);

    final Future<JCGLTexture2DType> f_at = at.future();
    f_at.get(30L, TimeUnit.SECONDS);

    final JCGLTexture2DUsableType att = at.get();
    Assert.assertEquals(
      JCGLTextureFormat.TEXTURE_FORMAT_RGB_8_3BPP,
      att.textureGetFormat());
    Assert.assertEquals(32L, att.textureGetWidth());
    Assert.assertEquals(32L, att.textureGetHeight());
    Assert.assertFalse(att.isDeleted());

    final Future<Void> f_del = at.delete();
    f_del.get(30L, TimeUnit.SECONDS);
    Assert.assertTrue(att.isDeleted());
  }
}
