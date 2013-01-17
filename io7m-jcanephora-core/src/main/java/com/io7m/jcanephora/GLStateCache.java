package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Structure used to cache certain state on the client side, and used to
 * preallocate native buffers for various OpenGL functions (avoiding the need
 * to constantly allocate and garbage collect native buffers).
 * 
 * Not visible to user code.
 */

@NotThreadSafe final class GLStateCache
{
  @Nonnull FramebufferColorAttachmentPoint[] color_attachments;
  @Nonnull TextureUnit[]                     texture_units;
  @Nonnull FramebufferDrawBuffer[]           draw_buffers;

  int                                        point_min_width;
  int                                        point_max_width;
  private final @Nonnull ByteBuffer          integer_cache_buffer;
  private final @Nonnull IntBuffer           integer_cache;
  private final @Nonnull ByteBuffer          color_buffer_mask_cache;
  private final @Nonnull ByteBuffer          depth_buffer_mask_cache;
  final @Nonnull StringBuilder               log_text;
  @Nonnull PolygonMode                       polygon_mode;

  /**
   * The size of the integer cache, in bytes.
   */

  private static final int                   INTEGER_CACHE_SIZE = 16 * 4;

  GLStateCache()
  {
    this.color_buffer_mask_cache =
      ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder());
    this.depth_buffer_mask_cache =
      ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder());

    this.integer_cache_buffer =
      ByteBuffer.allocateDirect(GLStateCache.INTEGER_CACHE_SIZE).order(
        ByteOrder.nativeOrder());
    this.integer_cache = this.integer_cache_buffer.asIntBuffer();

    this.color_attachments = null;
    this.texture_units = null;
    this.point_max_width = 0;
    this.point_min_width = 0;

    this.log_text = new StringBuilder();
    this.polygon_mode = PolygonMode.POLYGON_FILL;
  }

  @Nonnull ByteBuffer getColorMaskCache()
  {
    this.color_buffer_mask_cache.rewind();
    return this.color_buffer_mask_cache;
  }

  @Nonnull ByteBuffer getDepthMaskCache()
  {
    this.depth_buffer_mask_cache.rewind();
    return this.depth_buffer_mask_cache;
  }

  @Nonnull IntBuffer getIntegerCache()
  {
    this.integerCacheReset();
    return this.integer_cache;
  }

  private void integerCacheReset()
  {
    this.integer_cache.rewind();
    this.integer_cache_buffer.rewind();
  }
}
