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

import javax.annotation.Nonnull;

/**
 * Functions returning information about the OpenGL implementation.
 */

public interface JCGLMeta
{
  /**
   * Return and reset the current OpenGL error state.
   * 
   * @return The current OpenGL error code.
   */

  int metaGetError()
    throws JCGLException;

  /**
   * Return the name of the OpenGL renderer.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull String metaGetRenderer()
    throws JCGLException;

  /**
   * Return the name of the OpenGL vendor.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull String metaGetVendor()
    throws JCGLException;

  /**
   * Return the version string for the OpenGL implementation.
   * 
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  @Nonnull String metaGetVersion()
    throws JCGLException;

  /**
   * Return the major version number of the actual OpenGL implementation.
   */

  int metaGetVersionMajor();

  /**
   * Return the minor version number of the actual OpenGL implementation.
   */

  int metaGetVersionMinor();

  /**
   * Return <code>true</code> iff the underyling implementation is an OpenGL
   * ES implementation.
   */

  boolean metaIsES();
}
