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

package com.io7m.jcanephora.jogl;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLExceptionUnsupported;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLImplementationType;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import java.util.function.Function;

/**
 * JOGL-specific interface to JCGL implementations.
 */

public interface JCGLImplementationJOGLType extends JCGLImplementationType
{
  /**
   * Construct a new context from the given {@link GLContext}.
   *
   * @param c    An existing context
   * @param name The name assigned to the context, for debugging purposes
   *
   * @return A new context
   *
   * @throws JCGLException             On errors
   * @throws JCGLExceptionUnsupported  If the context is of a version that is
   *                                   not supported
   * @throws JCGLExceptionNonCompliant If the context violates the OpenGL
   *                                   specification
   */

  JCGLContextType newContextFrom(
    GLContext c,
    String name)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant;

  /**
   * Construct a new context from the given {@link GLContext}. The function
   * {@code gl_supplier} will be evaluated at least once to supply a {@link GL3}
   * instance to the implementation. This allows for the substitution of
   * alternate {@link GL3} instances for unit testing and debugging.
   *
   * @param c           An existing context
   * @param gl_supplier A function that yields a {@link GL3} implementation,
   *                    given a {@link GLContext}
   * @param name        The name assigned to the context, for debugging
   *                    purposes
   *
   * @return A new context
   *
   * @throws JCGLException             On errors
   * @throws JCGLExceptionUnsupported  If the context is of a version that is
   *                                   not supported
   * @throws JCGLExceptionNonCompliant If the context violates the OpenGL
   *                                   specification
   */

  JCGLContextType newContextFromWithSupplier(
    GLContext c,
    Function<GLContext, GL3> gl_supplier,
    String name)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant;
}
