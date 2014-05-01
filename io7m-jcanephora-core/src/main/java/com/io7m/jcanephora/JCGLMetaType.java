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

package com.io7m.jcanephora;

/**
 * Functions returning information about the OpenGL implementation.
 */

public interface JCGLMetaType
{
  /**
   * Return and reset the current OpenGL error state.
   * 
   * @return The current OpenGL error code.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  int metaGetError()
    throws JCGLRuntimeException;

  /**
   * @return The name of the OpenGL renderer.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  String metaGetRenderer()
    throws JCGLRuntimeException;

  /**
   * @return The version string for the OpenGL shading language
   *         implementation.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  JCGLSLVersion metaGetSLVersion()
    throws JCGLRuntimeException;

  /**
   * @return The name of the OpenGL vendor.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  String metaGetVendor()
    throws JCGLRuntimeException;

  /**
   * @return The version string for the OpenGL implementation.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  JCGLVersion metaGetVersion()
    throws JCGLRuntimeException;
}
