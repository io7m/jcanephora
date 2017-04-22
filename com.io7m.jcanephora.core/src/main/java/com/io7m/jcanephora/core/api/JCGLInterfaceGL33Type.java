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

  JCGLArrayBuffersType arrayBuffers();

  /**
   * @return The array objects interface
   */

  JCGLArrayObjectsType arrayObjects();

  /**
   * @return The blending interface
   */

  JCGLBlendingType blending();

  /**
   * @return The depth buffers interface
   */

  JCGLDepthBuffersType depthBuffers();

  /**
   * @return The shaders interface
   */

  JCGLShadersType shaders();

  /**
   * @return The index buffers interface
   */

  JCGLIndexBuffersType indexBuffers();

  /**
   * @return The drawing interface
   */

  JCGLDrawType drawing();

  /**
   * @return The framebuffer clearing interface
   */

  JCGLClearType clearing();

  /**
   * @return The texture interface
   */

  JCGLTexturesType textures();

  /**
   * @return The framebuffers interface
   */

  JCGLFramebuffersType framebuffers();

  /**
   * @return The polygon modes interface
   */

  JCGLPolygonModesType polygonModes();

  /**
   * @return The polygon culling interface
   */

  JCGLCullingType culling();

  /**
   * @return The color buffer masking interface
   */

  JCGLColorBufferMaskingType colorBufferMasking();

  /**
   * @return The viewports interface
   */

  JCGLViewportsType viewports();

  /**
   * @return The scissor interface
   */

  JCGLScissorType scissor();

  /**
   * @return The stencil buffers interface
   */

  JCGLStencilBuffersType stencilBuffers();

  /**
   * @return The timers interface
   */

  JCGLTimersType timers();
}
