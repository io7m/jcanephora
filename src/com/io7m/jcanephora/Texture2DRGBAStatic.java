package com.io7m.jcanephora;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

/**
 * 2D, 32-bit RGBA textures (8 bits per channel).
 * 
 * Textures are backed by a single pixel unpack buffer which can be mapped and
 * replaced, allowing for complete texture replacements without pipeline
 * stalls. Programs wishing to stream partial texture updates should not use
 * this texture type.
 */

@Immutable public final class Texture2DRGBAStatic implements GLResource
{
  public static Texture2DRGBAStatic loadImage(
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

    final BufferedImage converted = TextureUtils.convertImageToABGR(image);
    Texture2DRGBAStatic t = null;
    ByteBuffer map = null;

    try {
      t =
        gl.texture2DRGBAStaticAllocate(
          name,
          image.getWidth(),
          image.getHeight(),
          wrap_s,
          wrap_t,
          min_filter,
          mag_filter);
      map = gl.pixelUnpackBufferMapWrite(t.getBuffer()).getByteBuffer();

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
        gl.pixelUnpackBufferUnmap(t.getBuffer());
      }
    }

    gl.texture2DRGBAStaticReplace(t);
    return t;
  }

  private final @Nonnull PixelUnpackBuffer buffer;

  private final int                        texture;
  private final @Nonnull String            name;
  private final int                        width;
  private final int                        height;

  Texture2DRGBAStatic(
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

  /**
   * Retrieve the pixel unpack buffer that backs the given texture.
   */

  public @Nonnull PixelUnpackBuffer getBuffer()
  {
    return this.buffer;
  }

  /**
   * Return the height in pixels of the texture.
   */

  public int getHeight()
  {
    return this.height;
  }

  /**
   * Retrieve the raw OpenGL 'location' of the texture.
   */

  public int getLocation()
  {
    return this.texture;
  }

  /**
   * Retrieve the name of the texture.
   */

  public @Nonnull String getName()
  {
    return this.name;
  }

  /**
   * Retrieve the contents of the current texture as an RGBA image.
   * 
   * @param gl
   *          The OpenGL interface.
   * @return An RGBA image.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff <code>gl == null</code>.
   */

  public @Nonnull BufferedImage getTextureImage(
    final GLInterface gl)
    throws GLException,
      ConstraintError
  {
    final ByteBuffer map = gl.texture2DRGBAStaticGetImage(this);

    final int offsets[] = { 0, 1, 2, 3 };
    final ComponentColorModel color_model =
      new ComponentColorModel(
        ColorSpace.getInstance(ColorSpace.CS_sRGB),
        new int[] { 8, 8, 8, 8 },
        true,
        false,
        Transparency.TRANSLUCENT,
        DataBuffer.TYPE_BYTE);

    final byte[] bytes = new byte[map.capacity()];
    map.get(bytes);

    final WritableRaster raster =
      Raster.createInterleavedRaster(
        new DataBufferByte(bytes, bytes.length),
        this.width,
        this.height,
        this.width * 4,
        4,
        offsets,
        null);

    return new BufferedImage(color_model, raster, false, null);
  }

  /**
   * Retrieve the width in pixels of the texture.
   */

  public int getWidth()
  {
    return this.width;
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    gl.texture2DRGBAStaticDelete(this);
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
