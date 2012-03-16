package com.io7m.jcanephora;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * 2D, 32-bit RGBA textures (8 bits per channel).
 * 
 * Textures are backed by a pixel unpack buffer, allowing for streaming
 * texture updates.
 */

@Immutable public final class Texture2DRGBA implements GLResource
{
  private static final @Nonnull BufferedImage convertImageToABGR(
    final @Nonnull BufferedImage image)
  {
    BufferedImage converted = null;

    final int width = image.getWidth();
    final int height = image.getHeight();

    switch (image.getType()) {
      case BufferedImage.TYPE_4BYTE_ABGR:
      case BufferedImage.TYPE_4BYTE_ABGR_PRE:
      {
        converted = image;
        return converted;
      }
      case BufferedImage.TYPE_INT_ARGB_PRE:
      case BufferedImage.TYPE_INT_ARGB:
      case BufferedImage.TYPE_3BYTE_BGR:
      case BufferedImage.TYPE_BYTE_GRAY:
      case BufferedImage.TYPE_BYTE_BINARY:
      case BufferedImage.TYPE_BYTE_INDEXED:
      case BufferedImage.TYPE_USHORT_GRAY:
      case BufferedImage.TYPE_USHORT_565_RGB:
      case BufferedImage.TYPE_USHORT_555_RGB:
      case BufferedImage.TYPE_CUSTOM:
      case BufferedImage.TYPE_INT_RGB:
      case BufferedImage.TYPE_INT_BGR:
      default:
      {
        converted =
          new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        final ColorConvertOp op = new ColorConvertOp(null);
        op.filter(image, converted);
        return converted;
      }
    }
  }

  public static Texture2DRGBA loadImage(
    final @Nonnull String name,
    final @Nonnull BufferedImage image,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter min_filter,
    final @Nonnull TextureFilter mag_filter,
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainNotNull(image, "Image");
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");
    Constraints.constrainNotNull(min_filter, "Minification filter");

    final BufferedImage converted = Texture2DRGBA.convertImageToABGR(image);
    Texture2DRGBA t = null;
    ByteBuffer map = null;

    try {
      t =
        gl.allocateTextureRGBA(
          name,
          image.getWidth(),
          image.getHeight(),
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      map = gl.mapPixelUnpackBufferWrite(t.getBuffer());

      final WritableRaster raster = converted.getRaster();
      final DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
      final byte[] bytes = data.getData();
      for (int index = 0; index < bytes.length; index += 4) {
        map.put(bytes[index + 3]);
        map.put(bytes[index + 2]);
        map.put(bytes[index + 1]);
        map.put(bytes[index + 0]);
      }
    } finally {
      if (map != null) {
        assert t != null;
        gl.unmapPixelUnpackBuffer(t.getBuffer());
      }
    }

    gl.updateTexture2DRGBA(t);
    return t;
  }

  private final @Nonnull PixelUnpackBuffer buffer;
  private final int                        texture;
  private final @Nonnull String            name;
  private final int                        width;
  private final int                        height;

  Texture2DRGBA(
    final @Nonnull String name,
    final int texture_id,
    final @Nonnull PixelUnpackBuffer buffer,
    final int width,
    final int height)
    throws ConstraintError
  {
    this.name = Constraints.constrainNotNull(name, "Texture name");
    this.texture =
      (int) Constraints.constrainRange(texture_id, 1, Integer.MAX_VALUE);
    this.buffer = Constraints.constrainNotNull(buffer, "Pixel unpack buffer");
    this.width = width;
    this.height = height;
  }

  @Override public void delete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    gl.deleteTexture2DRGBA(this);
  }

  public @Nonnull PixelUnpackBuffer getBuffer()
  {
    return this.buffer;
  }

  public int getHeight()
  {
    return this.height;
  }

  public int getLocation()
  {
    return this.texture;
  }

  public @Nonnull String getName()
  {
    return this.name;
  }

  public int getWidth()
  {
    return this.width;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Texture2DRGBA ");
    builder.append(this.getBuffer().toString());
    builder.append(" ");
    builder.append(this.getLocation());
    builder.append(" '");
    builder.append(this.getName());
    builder.append("' ");
    builder.append(this.getWidth());
    builder.append(" ");
    builder.append(this.getHeight());
    builder.append("]");
    return builder.toString();
  }
}
