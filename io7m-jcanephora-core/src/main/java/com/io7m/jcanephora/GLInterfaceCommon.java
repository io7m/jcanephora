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

/**
 * The interface exposed by the common subset of OpenGL 3.* and OpenGL ES2
 * implementations.
 */

public interface GLInterfaceCommon extends
  GLArrayBuffers,
  GLBlendingCommon,
  GLColorBuffer,
  GLCull,
  GLDepthBuffer,
  GLDraw,
  GLErrorCodes,
  GLFramebuffersCommon,
  GLIndexBuffers,
  GLMeta,
  GLRasterization,
  GLRenderbuffersCommon,
  GLScissor,
  GLShaders,
  GLStencilBuffer,
  GLTextures2DStaticCommon,
  GLTexturesCubeStaticCommon,
  GLTextureUnits,
  GLViewport
{
  /*
   * All functions defined in the superinterfaces.
   */
}