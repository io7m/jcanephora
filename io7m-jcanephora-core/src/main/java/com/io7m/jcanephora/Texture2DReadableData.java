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

package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;

/**
 * <p>
 * An allocated read-only region of data, typically created by downloading a
 * texture from the GPU.
 * </p>
 * <p>
 * The cursors exposed by this interface treat <tt>(0, 0)</tt> as the bottom
 * left corner of the image, which is consistent with OpenGL's conventions.
 * </p>
 */

public final class Texture2DReadableData
{
  private final @Nonnull TextureType   type;
  private final @Nonnull AreaInclusive area;
  private final @Nonnull ByteBuffer    data;

  Texture2DReadableData(
    final @Nonnull TextureType type,
    final @Nonnull AreaInclusive area)
    throws ConstraintError
  {
    this.type = Constraints.constrainNotNull(type, "Texture type");
    this.area = Constraints.constrainNotNull(area, "Area");

    final long width = this.area.getRangeX().getInterval();
    final long height = this.area.getRangeY().getInterval();
    final int bpp = type.bytesPerPixel();
    this.data =
      ByteBuffer.allocate((int) (height * width * bpp)).order(
        ByteOrder.nativeOrder());
  }

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 1.
   */

  public @Nonnull SpatialCursorReadable1f getCursor1f()
    throws ConstraintError
  {
    switch (this.type) {
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      {
        Constraints
          .constrainArbitrary(
            false,
            "Number of texture components is 1 and textures are floating point");
        break;
      }
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        return new ByteBufferTextureCursorReadable1f_4_32(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 1.
   */

  public @Nonnull SpatialCursorReadable1i getCursor1i()
    throws ConstraintError
  {
    switch (this.type) {
      case TEXTURE_TYPE_R_8_1BPP:
      {
        return new ByteBufferTextureCursorReadable1i_1_8(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        Constraints.constrainArbitrary(
          false,
          "Number of texture components is 1 and components are integers");
        break;
      }
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      {
        return new ByteBufferTextureCursorReadable1i_2_16(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      {
        return new ByteBufferTextureCursorReadable1i_3_24(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 2.
   */

  public @Nonnull SpatialCursorReadable2i getCursor2i()
    throws ConstraintError
  {
    switch (this.type) {
      case TEXTURE_TYPE_RG_88_2BPP:
      {
        return new ByteBufferTextureCursorReadable2i_2_88(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      {
        Constraints.constrainArbitrary(
          false,
          "Number of texture components is 2 and components are integers");
        break;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 3.
   */

  public @Nonnull SpatialCursorReadable3i getCursor3i()
    throws ConstraintError
  {
    switch (this.type) {
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      {
        Constraints.constrainArbitrary(
          false,
          "Number of texture components is 3 and components are integers");
        break;
      }
      case TEXTURE_TYPE_RGB_565_2BPP:
      {
        return new ByteBufferTextureCursorReadable3i_2_565(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        return new ByteBufferTextureCursorReadable3i_3_888(
          this.data,
          this.area,
          this.area);
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * <p>
   * Retrieve a cursor that points to elements of the texture. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   * </p>
   * 
   * @throws ConstraintError
   *           If the number of components in the texture is not 4.
   */

  public @Nonnull SpatialCursorReadable4i getCursor4i()
    throws ConstraintError
  {
    switch (this.type) {
      case TEXTURE_TYPE_RGBA_4444_2BPP:
      {
        return new ByteBufferTextureCursorReadable4i_2_4444(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_5551_2BPP:
      {
        return new ByteBufferTextureCursorReadable4i_2_5551(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_RGBA_8888_4BPP:
      {
        return new ByteBufferTextureCursorReadable4i_4_8888(
          this.data,
          this.area,
          this.area);
      }
      case TEXTURE_TYPE_DEPTH_16_2BPP:
      case TEXTURE_TYPE_DEPTH_24_4BPP:
      case TEXTURE_TYPE_DEPTH_32F_4BPP:
      case TEXTURE_TYPE_R_8_1BPP:
      case TEXTURE_TYPE_RG_88_2BPP:
      case TEXTURE_TYPE_RGB_565_2BPP:
      case TEXTURE_TYPE_RGB_888_3BPP:
      {
        Constraints.constrainArbitrary(
          false,
          "Number of texture components is 4 and components are integers");
      }
    }

    throw new UnreachableCodeException();
  }

  @Nonnull AreaInclusive targetArea()
  {
    return this.area;
  }

  @Nonnull ByteBuffer targetData()
  {
    return this.data;
  }
}
