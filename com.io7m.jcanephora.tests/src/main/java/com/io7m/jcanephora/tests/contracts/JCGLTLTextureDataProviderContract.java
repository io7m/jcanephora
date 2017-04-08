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

import com.io7m.jcanephora.texture.loader.core.JCGLTLTextureDataProviderType;
import com.io7m.jcanephora.texture.loader.core.JCGLTLTextureDataType;
import com.io7m.jtensors.storage.heap.VectorMutable4D;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Texture data provider contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLTLTextureDataProviderContract extends JCGLContract
{
  @Rule public ExpectedException expected = ExpectedException.none();

  protected abstract JCGLTLTextureDataProviderType getProvider();

  @Test
  public final void testCorners2x2_RGB()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("2x2_rgb.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(2L, d.width());
    Assert.assertEquals(2L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(1, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 1, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(1, 1, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCorners2x2_Grey()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("2x2_grey.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(2L, d.width());
    Assert.assertEquals(2L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(0.5, v.x(), 0.005);
    Assert.assertEquals(0.5, v.y(), 0.005);
    Assert.assertEquals(0.5, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(1, 0, v);
    Assert.assertEquals(0.75, v.x(), 0.005);
    Assert.assertEquals(0.75, v.y(), 0.005);
    Assert.assertEquals(0.75, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 1, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(1, 1, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCorrupt()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("empty.png");

    this.expected.expect(IOException.class);
    this.expected.expectMessage("Image decoding error");
    p.loadFromStream(s);
  }


  @Test
  public final void testCornersBASN0G01()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn0g01.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN0G02()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn0g02.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.66666666, v.x(), 0.005);
    Assert.assertEquals(0.66666666, v.y(), 0.005);
    Assert.assertEquals(0.66666666, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN0G04()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn0g04.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(0.4666666666666667, v.x(), 0.005);
    Assert.assertEquals(0.4666666666666667, v.y(), 0.005);
    Assert.assertEquals(0.4666666666666667, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(0.4666666666666667, v.x(), 0.005);
    Assert.assertEquals(0.4666666666666667, v.y(), 0.005);
    Assert.assertEquals(0.4666666666666667, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.9333333333333333, v.x(), 0.005);
    Assert.assertEquals(0.9333333333333333, v.y(), 0.005);
    Assert.assertEquals(0.9333333333333333, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN0G08()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn0g08.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(0.12156862745098039, v.x(), 0.005);
    Assert.assertEquals(0.12156862745098039, v.y(), 0.005);
    Assert.assertEquals(0.12156862745098039, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(0.10980392156862745, v.x(), 0.005);
    Assert.assertEquals(0.10980392156862745, v.y(), 0.005);
    Assert.assertEquals(0.10980392156862745, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.011764705882352941, v.x(), 0.005);
    Assert.assertEquals(0.011764705882352941, v.y(), 0.005);
    Assert.assertEquals(0.011764705882352941, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN0G16()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn0g16.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(0.7294117647058823, v.x(), 0.005);
    Assert.assertEquals(0.7294117647058823, v.y(), 0.005);
    Assert.assertEquals(0.7294117647058823, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(0.24313725490196078, v.x(), 0.005);
    Assert.assertEquals(0.24313725490196078, v.y(), 0.005);
    Assert.assertEquals(0.24313725490196078, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN2C08()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn2c08.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(0.8784313725490196, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(0.12156862745098039, v.x(), 0.005);
    Assert.assertEquals(0.12156862745098039, v.y(), 0.005);
    Assert.assertEquals(0.12156862745098039, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN2C16()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn2c16.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN4A08()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn4a08.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(0.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(0.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN3P01()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn3p01.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(0.9333333333333333, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(0.13333333333333333, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(0.13333333333333333, v.x(), 0.005);
    Assert.assertEquals(0.4, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(0.13333333333333333, v.x(), 0.005);
    Assert.assertEquals(0.4, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.9333333333333333, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(0.13333333333333333, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN3P02()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn3p02.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN3P04()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn3p04.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(0.6, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(0.6, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.7333333333333333, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN3P08()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn3p08.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 15, v);
    Assert.assertEquals(0.996078431372549, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(1.0, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN4A16()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn4a16.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(0.0, v.w(), 0.005);

    d.pixel(0, 15, v);
    Assert.assertEquals(0.9677271686884871, v.x(), 0.005);
    Assert.assertEquals(0.9677271686884871, v.y(), 0.005);
    Assert.assertEquals(0.9677271686884871, v.z(), 0.005);
    Assert.assertEquals(0.0, v.w(), 0.005);

    d.pixel(15, 15, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(0.9677424277103838, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(0.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(0.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.0, v.z(), 0.005);
    Assert.assertEquals(0.0, v.w(), 0.005);
  }

  @Test
  public final void testCornersBASN6A08()
    throws Exception
  {
    final Class<?> c = JCGLTLTextureDataProviderContract.class;
    final JCGLTLTextureDataProviderType p = this.getProvider();
    final InputStream s = c.getResourceAsStream("basn6a08.png");
    final JCGLTLTextureDataType d = p.loadFromStream(s);

    Assert.assertEquals(32L, d.width());
    Assert.assertEquals(32L, d.height());
    Assert.assertFalse(d.isPremultipliedAlpha());

    final VectorMutable4D v = new VectorMutable4D();

    d.pixel(0, 0, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.03137254901960784, v.z(), 0.005);
    Assert.assertEquals(0.0, v.w(), 0.005);

    d.pixel(31, 0, v);
    Assert.assertEquals(1.0, v.x(), 0.005);
    Assert.assertEquals(0.0, v.y(), 0.005);
    Assert.assertEquals(0.03137254901960784, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);

    d.pixel(0, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.12549019607843137, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(0.0, v.w(), 0.005);

    d.pixel(31, 31, v);
    Assert.assertEquals(0.0, v.x(), 0.005);
    Assert.assertEquals(0.12549019607843137, v.y(), 0.005);
    Assert.assertEquals(1.0, v.z(), 0.005);
    Assert.assertEquals(1.0, v.w(), 0.005);
  }
}
