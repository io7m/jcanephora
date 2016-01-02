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

/**
 * The type of usable "soft" async textures.
 *
 * A soft async texture actually consists of three textures (although two of
 * those textures may be the same texture): A <i>loaded</i> texture, a
 * <i>default</i> texture, and an <i>error</i> texture. The user may retrieve a
 * valid texture at any time using the {@link #get()} method, but will receive
 * the <i>default</i> texture until the asynchronous load operation has
 * succeeded. If the operation succeeds, subsequent calls to {@link #get()} will
 * return the <i>loaded</i> texture. If the operation fails, subsequent calls to
 * {@link #get()} will return the <i>error</i> texture.
 */

public interface JCGLTLSoftAsyncTexture2DUsableType extends
  JCGLResourceUsableType
{
  /**
   * @return The current texture
   */

  JCGLTexture2DUsableType get();
}
