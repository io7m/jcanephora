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

import com.io7m.jcanephora.core.JCGLExceptionContextIsCurrent;
import com.io7m.jcanephora.core.JCGLExceptionContextNotCurrent;

import java.util.List;

/**
 * The usable interface to OpenGL contexts.
 */

public interface JCGLContextUsableType
{
  /**
   * @return The context name, for debugging purposes
   */

  String contextGetName();

  /**
   * @return The list of contexts with which this context is shared
   */

  List<JCGLContextUsableType> contextGetShares();

  /**
   * @param c The context
   *
   * @return {@code true} iff {@code c} is shared with this context
   */

  boolean contextIsSharedWith(JCGLContextUsableType c);

  /**
   * @return {@code true} iff the current context is current
   *
   * @see #contextMakeCurrent()
   */

  boolean contextIsCurrent();

  /**
   * Make this context current on the current thread.
   *
   * @throws JCGLExceptionContextIsCurrent If a context (including this
   *                                            one) is already current on this
   *                                            thread
   */

  void contextMakeCurrent()
    throws JCGLExceptionContextIsCurrent;

  /**
   * Release this context on the current thread.
   *
   * @throws JCGLExceptionContextNotCurrent If the context is not current on any
   *                                        thread
   */

  void contextReleaseCurrent()
    throws JCGLExceptionContextNotCurrent;

  /**
   * @return The OpenGL 3.3 interface for the context
   */

  JCGLInterfaceGL33Type contextGetGL33();

  /**
   * @return The implementation that owns the context
   */

  JCGLImplementationType contextGetImplementation();
}
