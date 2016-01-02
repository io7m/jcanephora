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

package com.io7m.jcanephora.texture_loader.core;

import com.io7m.jcanephora.core.JCGLResourceUsableType;
import com.io7m.jcanephora.core.JCGLTexture2DUsableType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * The type of asynchronous texture loaders.
 */

public interface JCGLTLAsyncTextureLoaderType extends JCGLResourceUsableType
{
  /**
   * Asynchronously load a texture from the given stream.
   *
   * @param is        The stream
   * @param t_default The texture that will be used as the image for the async
   *                  texture until the async texture is fully loaded
   * @param t_error   The texture that will be used as the image for the async
   *                  texture when the async texture fails to load
   * @param format    The loaded texture format
   * @param wrap_s    The desired wrapping mode on the S axis
   * @param wrap_t    The desired wrapping mode on the T axis
   * @param mini      The minification filter
   * @param mag       The magnification filter
   *
   * @return A soft async texture
   */

  JCGLTLSoftAsyncTexture2DType loadTextureFromStream(
    InputStream is,
    JCGLTexture2DUsableType t_default,
    JCGLTexture2DUsableType t_error,
    JCGLTextureFormat format,
    JCGLTextureWrapS wrap_s,
    JCGLTextureWrapT wrap_t,
    JCGLTextureFilterMinification mini,
    JCGLTextureFilterMagnification mag);

  /**
   * Attempt to shut down the texture loader.
   *
   * Blocks until all tasks have completed execution after a shutdown request,
   * or the timeout occurs, or the current thread is interrupted, whichever
   * happens first.
   *
   * @param t    the maximum time to wait
   * @param unit the time unit of the timeout argument
   *
   * @return {@code true} if this executor terminated and {@code false} if the
   * timeout elapsed before termination
   *
   * @throws InterruptedException if interrupted while waiting
   */

  boolean shutDownSynchronously(
    long t,
    TimeUnit unit)
    throws InterruptedException;
}
