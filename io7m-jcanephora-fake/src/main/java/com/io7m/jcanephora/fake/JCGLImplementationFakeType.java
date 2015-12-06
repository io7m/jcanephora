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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLExceptionUnsupported;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLImplementationType;

/**
 * Fake-specific interface to JCGL implementations.
 */

public interface JCGLImplementationFakeType extends JCGLImplementationType
{
  /**
   * Construct a new context.
   *
   * @param name        The name of the new context
   * @param in_listener A shader listener that will be notified of shader
   *                    compilations
   *
   * @return A new context
   *
   * @throws JCGLException             On errors
   * @throws JCGLExceptionUnsupported  If the context is of a version that is
   *                                   not supported
   * @throws JCGLExceptionNonCompliant If the implementation constraints yield
   *                                   an implementation that is not OpenGL
   *                                   compliant
   */

  JCGLContextType newContext(
    String name,
    FakeShaderListenerType in_listener)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant;

  /**
   * Construct a new context, shared with an existing context.
   *
   * @param c           The existing context
   * @param name        The name of the new context
   * @param in_listener A shader listener that will be notified of shader
   *                    compilations
   *
   * @return A new context
   *
   * @throws JCGLException             On errors
   * @throws JCGLExceptionUnsupported  If the context is of a version that is
   *                                   not supported
   * @throws JCGLExceptionNonCompliant If the implementation constraints yield
   *                                   an implementation that is not OpenGL
   *                                   compliant
   */

  JCGLContextType newContextSharedWith(
    JCGLContextType c,
    String name,
    FakeShaderListenerType in_listener)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant;
}
