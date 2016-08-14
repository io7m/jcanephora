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

package com.io7m.jcanephora.texture_loader.awt;

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jcanephora.texture_loader.core.JCGLTLTextureDataProviderType;
import com.io7m.jcanephora.texture_loader.core.JCGLTLTextureDataType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.VectorWritable4DType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * An AWT implementation of the {@link JCGLTLTextureDataProviderType}
 * interface.
 */

public final class JCGLAWTTextureDataProvider implements
  JCGLTLTextureDataProviderType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JCGLAWTTextureDataProvider.class);
  }

  private JCGLAWTTextureDataProvider()
  {
    JCGLAWTTextureDataProvider.LOG.debug("enumerating supported image formats");
    final String[] readers = ImageIO.getReaderFormatNames();
    final Set<String> supported = new HashSet<>();

    for (final String s : readers) {
      supported.add(s.toUpperCase());
    }

    for (final String s : supported) {
      JCGLAWTTextureDataProvider.LOG.debug("supported: {}", s);
    }
  }

  /**
   * @return A new texture data provider
   */

  public static JCGLTLTextureDataProviderType newProvider()
  {
    return new JCGLAWTTextureDataProvider();
  }

  @Override
  public JCGLTLTextureDataType loadFromStream(final InputStream is)
    throws IOException
  {
    NullCheck.notNull(is);

    final BufferedImage ib = ImageIO.read(is);
    if (ib == null) {
      throw new IOException("Image decoding error");
    }

    final ColorModel color_model = ib.getColorModel();
    if (color_model instanceof IndexColorModel) {
      return new TextureDataIndexed(ib);
    }

    if (color_model.getNumColorComponents() > 1) {
      return new TextureDataRGBA(ib);
    }

    if (color_model.hasAlpha()) {
      return new TextureDataGreyAlpha(ib);
    }

    return new TextureDataGrey(ib);
  }

  private static abstract class TextureDataAbstract implements
    JCGLTLTextureDataType
  {
    private final BufferedImage image;

    private TextureDataAbstract(
      final BufferedImage in_image)
    {
      this.image = NullCheck.notNull(in_image);
    }

    @Override
    public final boolean isPremultipliedAlpha()
    {
      return this.image.isAlphaPremultiplied();
    }

    @Override
    public final long getWidth()
    {
      return this.image.getWidth();
    }

    @Override
    public final long getHeight()
    {
      return this.image.getHeight();
    }
  }

  private static final class TextureDataGreyAlpha extends TextureDataAbstract
  {
    private final BufferedImage image;
    private final WritableRaster raster;
    private final SampleModel sample_model;
    private final int[] sample_sizes;
    private final double[] pixel;
    private final double[] div;
    private final ColorModel color_model;

    private TextureDataGreyAlpha(
      final BufferedImage in_image)
    {
      super(in_image);
      this.image = NullCheck.notNull(in_image);
      this.raster = this.image.getRaster();
      this.sample_model = this.raster.getSampleModel();
      this.sample_sizes = this.sample_model.getSampleSize();
      this.color_model = this.image.getColorModel();

      Preconditions.checkPreconditionI(
        this.sample_sizes.length,
        this.sample_sizes.length == 2,
        v -> "Pixels must have two channels");

      Preconditions.checkPrecondition(
        this.color_model.hasAlpha(),
        "Pixels must have alpha");

      this.pixel = new double[2];
      this.div = new double[2];

      for (int index = 0; index < this.sample_sizes.length; ++index) {
        this.div[index] = Math.pow(2.0, this.sample_sizes[index]) - 1.0;
      }
    }

    @Override
    public void getPixel(
      final int x,
      final int y,
      final VectorWritable4DType v)
    {
      this.raster.getPixel(x, y, this.pixel);

      v.set4D(
        this.pixel[0] / this.div[0],
        this.pixel[0] / this.div[0],
        this.pixel[0] / this.div[0],
        this.pixel[1] / this.div[1]
      );
    }
  }

  private static final class TextureDataGrey extends TextureDataAbstract
  {
    private final BufferedImage image;
    private final WritableRaster raster;
    private final SampleModel sample_model;
    private final int[] sample_sizes;
    private final double[] pixel;
    private final double[] div;
    private final ColorModel color_model;

    private TextureDataGrey(
      final BufferedImage in_image)
    {
      super(in_image);
      this.image = NullCheck.notNull(in_image);
      this.raster = this.image.getRaster();
      this.sample_model = this.raster.getSampleModel();
      this.sample_sizes = this.sample_model.getSampleSize();
      this.color_model = this.image.getColorModel();

      Preconditions.checkPreconditionI(
        this.sample_sizes.length,
        this.sample_sizes.length == 1,
        v -> "Pixels must have one channel");

      Preconditions.checkPrecondition(
        !this.color_model.hasAlpha(),
        "Pixels cannot have alpha");

      this.pixel = new double[1];
      this.div = new double[1];
      this.div[0] = Math.pow(2.0, this.sample_sizes[0]) - 1.0;
    }

    @Override
    public void getPixel(
      final int x,
      final int y,
      final VectorWritable4DType v)
    {
      this.raster.getPixel(x, y, this.pixel);

      v.set4D(
        this.pixel[0] / this.div[0],
        this.pixel[0] / this.div[0],
        this.pixel[0] / this.div[0],
        1.0
      );
    }
  }

  private static final class TextureDataRGBA extends TextureDataAbstract
  {
    private final BufferedImage image;
    private final double[] pixel;
    private final WritableRaster raster;
    private final int[] sample_sizes;
    private final double[] div;
    private final SampleModel sample_model;

    private TextureDataRGBA(
      final BufferedImage in_image)
    {
      super(in_image);
      this.image = NullCheck.notNull(in_image);
      this.raster = this.image.getRaster();

      this.sample_model = this.raster.getSampleModel();
      this.sample_sizes = this.sample_model.getSampleSize();

      this.pixel = new double[4];
      this.div = new double[4];

      for (int index = 0; index < this.sample_sizes.length; ++index) {
        this.div[index] = Math.pow(2.0, this.sample_sizes[index]) - 1.0;
      }
    }

    @Override
    public void getPixel(
      final int x,
      final int y,
      final VectorWritable4DType v)
    {
      this.raster.getPixel(x, y, this.pixel);

      switch (this.sample_sizes.length) {
        case 4: {
          this.pixel[0] = this.pixel[0] / this.div[0];
          this.pixel[1] = this.pixel[1] / this.div[1];
          this.pixel[2] = this.pixel[2] / this.div[2];
          this.pixel[3] = this.pixel[3] / this.div[3];
          break;
        }
        case 3: {
          this.pixel[0] = this.pixel[0] / this.div[0];
          this.pixel[1] = this.pixel[1] / this.div[1];
          this.pixel[2] = this.pixel[2] / this.div[2];
          this.pixel[3] = 1.0;
          break;
        }
        case 2: {
          this.pixel[0] = this.pixel[0] / this.div[0];
          this.pixel[1] = this.pixel[1] / this.div[1];
          this.pixel[2] = 0.0;
          this.pixel[3] = 1.0;
          break;
        }
        case 1: {
          this.pixel[0] = this.pixel[0] / this.div[0];
          this.pixel[1] = 0.0;
          this.pixel[2] = 0.0;
          this.pixel[3] = 1.0;
          break;
        }
      }

      v.set4D(
        this.pixel[0],
        this.pixel[1],
        this.pixel[2],
        this.pixel[3]
      );
    }
  }

  private static final class TextureDataIndexed extends TextureDataAbstract
  {
    private final BufferedImage image;

    private TextureDataIndexed(
      final BufferedImage in_image)
    {
      super(in_image);
      this.image = NullCheck.notNull(in_image);
    }

    @Override
    public void getPixel(
      final int x,
      final int y,
      final VectorWritable4DType v)
    {
      final int c = this.image.getRGB(x, y);
      final int r = (c & 0x00ff0000) >> 16;
      final int g = (c & 0x0000ff00) >> 8;
      final int b = (c & 0x000000ff);

      v.set4D(
        ((double) r) / 255.0,
        ((double) g) / 255.0,
        ((double) b) / 255.0,
        1.0
      );
    }
  }
}
