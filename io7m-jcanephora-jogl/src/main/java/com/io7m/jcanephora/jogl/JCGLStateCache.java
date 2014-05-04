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
package com.io7m.jcanephora.jogl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jcanephora.FramebufferColorAttachmentPoint;
import com.io7m.jcanephora.FramebufferDrawBuffer;
import com.io7m.jcanephora.PolygonMode;

/**
 * Structure used to cache certain state on the client side, and used to
 * preallocate native buffers for various OpenGL functions (avoiding the need
 * to constantly allocate and garbage collect native buffers).
 * 
 * Not visible to user code.
 */

@NotThreadSafe final class JCGLStateCache
{
  /**
   * The size of the integer cache, in bytes.
   */

  private static final int                       INTEGER_CACHE_SIZE = 16 * 4;
   List<FramebufferColorAttachmentPoint> color_attachments;
  private final  ByteBuffer              color_buffer_mask_cache;
  private final  ByteBuffer              depth_buffer_mask_cache;
   List<FramebufferDrawBuffer>           draw_buffers;
  private final  IntBuffer               integer_cache;
  private final  ByteBuffer              integer_cache_buffer;
  final  StringBuilder                   log_text;
  int                                            point_max_width;
  int                                            point_min_width;
   PolygonMode                           polygon_mode;
   List<TextureUnit>                     texture_units;

  JCGLStateCache()
  {
    this.color_buffer_mask_cache =
      ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder());
    this.depth_buffer_mask_cache =
      ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder());

    this.integer_cache_buffer =
      ByteBuffer.allocateDirect(JCGLStateCache.INTEGER_CACHE_SIZE).order(
        ByteOrder.nativeOrder());
    this.integer_cache = this.integer_cache_buffer.asIntBuffer();

    this.color_attachments = null;
    this.texture_units = null;
    this.point_max_width = 0;
    this.point_min_width = 0;

    this.log_text = new StringBuilder();
    this.polygon_mode = PolygonMode.POLYGON_FILL;
  }

   ByteBuffer getColorMaskCache()
  {
    this.color_buffer_mask_cache.rewind();
    return this.color_buffer_mask_cache;
  }

   ByteBuffer getDepthMaskCache()
  {
    this.depth_buffer_mask_cache.rewind();
    return this.depth_buffer_mask_cache;
  }

   IntBuffer getIntegerCache()
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
