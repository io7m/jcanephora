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

package com.io7m.jcanephora.core.api;

/**
 * The interface exposed by OpenGL 3.3
 */

public interface JCGLInterfaceGL33Type
{
  /**
   * @return The array buffers interface
   */

  JCGLArrayBuffersType getArrayBuffers();

  /**
   * @return The array objects interface
   */

  JCGLArrayObjectsType getArrayObjects();

  /**
   * @return The blending interface
   */

  JCGLBlendingType getBlending();

  /**
   * @return The depth buffers interface
   */

  JCGLDepthBuffersType getDepthBuffers();

  /**
   * @return The shaders interface
   */

  JCGLShadersType getShaders();

  /**
   * @return The index buffers interface
   */

  JCGLIndexBuffersType getIndexBuffers();

  /**
   * @return The drawing interface
   */

  JCGLDrawType getDraw();

  /**
   * @return The framebuffer clearing interface
   */

  JCGLClearType getClear();

  /**
   * @return The texture interface
   */

  JCGLTexturesType getTextures();

  /**
   * @return The framebuffers interface
   */

  JCGLFramebuffersType getFramebuffers();

  /**
   * @return The polygon modes interface
   */

  JCGLPolygonModesType getPolygonModes();

  /**
   * @return The polygon culling interface
   */

  JCGLCullingType getCulling();

  /**
   * @return The color buffer masking interface
   */

  JCGLColorBufferMaskingType getColorBufferMasking();
}
