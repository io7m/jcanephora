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

package com.io7m.jcanephora.async;

import com.io7m.jcanephora.core.JCGLArrayBufferType;
import com.io7m.jcanephora.core.JCGLBufferUpdateType;
import com.io7m.jcanephora.core.JCGLIndexBufferType;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUpdateType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.texture.loader.core.JCGLTLTextureDataType;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <p>The type of asynchronous resource loaders.</p>
 *
 * <p>An asynchronous resource loader attempts to simplify the allocation and
 * population of OpenGL resources by evaluating user-provided functions on a
 * pool of worker threads, and creating shared OpenGL objects on a dedicated
 * OpenGL context.</p>
 *
 * <p>The interface tries to provide a generalized and type-safe interface to
 * allow programmers to conveniently load textures and 3D mesh data
 * asynchronously, without the interface itself having to know anything about
 * the source of the data.</p>
 */

public interface JCGLAsyncResourceLoaderType
{
  /**
   * <p>Asynchronously load a texture. The given {@code on_data} function is
   * evaluated, yielding texture data. A texture is allocated with the given
   * parameters, and then the {@code on_populate} function is evaluated,
   * yielding a {@link JCGLTexture2DUpdateType} which is then used to populate
   * the texture.</p>
   *
   * <p>All functions are evaluated on an arbitrary number of background worker
   * threads.</p>
   *
   * @param on_data     A function that returns texture data
   * @param format      The allocated texture format
   * @param wrap_s      The allocated texture wrapping mode on the S axis
   * @param wrap_t      The allocated texture wrapping mode on the T axis
   * @param min_filter  The allocated texture minification filter
   * @param mag_filter  The allocated texture magnification filter
   * @param on_populate A function that returns a texture update
   *
   * @return A future representing the loading in progress
   */

  CompletableFuture<JCGLTexture2DType>
  loadTexture(
    Supplier<JCGLTLTextureDataType> on_data,
    JCGLTextureFormat format,
    JCGLTextureWrapS wrap_s,
    JCGLTextureWrapT wrap_t,
    JCGLTextureFilterMinification min_filter,
    JCGLTextureFilterMagnification mag_filter,
    BiFunction<JCGLTexture2DType, JCGLTLTextureDataType,
      JCGLTexture2DUpdateType> on_populate);

  /**
   * <p>Asynchronously create an array and index buffer pair. The given {@code
   * on_data} function is evaluated on a worker thread, yielding user data
   * {@code d}. The {@code on_array_size} function is passed {@code d} and is
   * expected to return the size in bytes of the array buffer that should be
   * allocated. The {@code on_index_size} function is passed {@code d} and is
   * expected to return the size in bytes of the index buffer that should be
   * allocated. The {@code on_populate_array} function is evaluated, and is
   * passed {@code d} and the allocated array buffer, and is expected to yield
   * an {@link JCGLBufferUpdateType} value that can be used to populate the
   * array buffer with data. The {@code on_populate_index} function is
   * evaluated, and is passed {@code d} and the allocated index buffer, and is
   * expected to yield an {@link JCGLBufferUpdateType} value that can be used to
   * populate the index buffer with data.</p>
   *
   * <p>All functions are evaluated on an arbitrary number of background worker
   * threads.</p>
   *
   * @param on_data           A function that returns an abstract user data
   *                          value
   * @param on_array_size     A function that yields an array buffer size
   * @param array_usage       The array buffer usage hint
   * @param on_index_size     A function that yields an index buffer size
   * @param index_type        The type of index buffer indices
   * @param index_usage       The index buffer usage hint
   * @param on_populate_array A function that yields a buffer update, used to
   *                          populate the array buffer
   * @param on_populate_index A function that yields a buffer update, used to
   *                          populate the index buffer
   * @param <T>               The type of user data
   *
   * @return A future representing the loading in progress
   */

  <T> CompletableFuture<JCGLAsyncBufferPair>
  loadArrayIndexBuffers(
    Supplier<T> on_data,
    Function<T, Long> on_array_size,
    JCGLUsageHint array_usage,
    Function<T, Long> on_index_size,
    JCGLUnsignedType index_type,
    JCGLUsageHint index_usage,
    BiFunction<T, JCGLArrayBufferType,
      JCGLBufferUpdateType<JCGLArrayBufferType>> on_populate_array,
    BiFunction<T, JCGLIndexBufferType,
      JCGLBufferUpdateType<JCGLIndexBufferType>> on_populate_index);
}
