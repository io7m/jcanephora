/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.api;

/**
 * <p>
 * The interface exposed by OpenGL 3.* implementations.
 * </p>
 */

public interface JCGLInterfaceGL3Type extends
  JCGLArrayBuffersMappedType,
  JCGLBlendingGL3Type,
  JCGLDepthClampingType,
  JCGLFramebuffersGL3Type,
  JCGLIndexBuffersMappedType,
  JCGLInterfaceCommonType,
  JCGLLogicType,
  JCGLPolygonModesType,
  JCGLPolygonSmoothingType,
  JCGLRenderbuffersGL3Type,
  JCGLShadersGL3Type,
  JCGLTextures2DStaticGL3Type,
  JCGLTexturesCubeStaticGL3Type
{
  /*
   * All functions defined in the superinterfaces.
   */
}
