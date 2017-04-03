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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLExceptionUnsupported;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLImplementationType;
import com.io7m.jfunctional.Pair;

/**
 * LWJGL3-specific interface to JCGL implementations.
 */

public interface JCGLImplementationLWJGL3Type extends JCGLImplementationType
{
  /**
   * <p>Construct a new context from the given context. The {@code long} value
   * is a context value returned from {@link org.lwjgl.glfw.GLFW#glfwCreateWindow(int,
   * int, CharSequence, long, long)}.</p>
   *
   * <p>The context is assumed not to be shared with any other context. The
   * implementation trusts that this is the case and has no way to verify
   * otherwise.</p>
   *
   * @param context An existing context
   * @param name    The name assigned to the context, for debugging purposes
   *
   * @return A new context
   *
   * @throws JCGLException             On errors
   * @throws JCGLExceptionUnsupported  If the context is of a version that is
   *                                   not supported
   * @throws JCGLExceptionNonCompliant If the context violates the OpenGL
   *                                   specification
   */

  JCGLContextType newUnsharedContextFrom(
    long context,
    String name)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant;

  /**
   * <p>Construct a new context from the given context. The {@code long} values
   * are context values returned from {@link org.lwjgl.glfw.GLFW#glfwCreateWindow(int,
   * int, CharSequence, long, long)}.</p>
   *
   * <p>The pair of contexts are assumed to be shared with each other. The
   * implementation trusts that this is the case and has no way to verify
   * otherwise.</p>
   *
   * <p>On successfully returning, the {@code master_context} context will be
   * current on the current thread.</p>
   *
   * @param master_context An existing context
   * @param master_name    The name assigned to the master context, for
   *                       debugging purposes
   * @param slave_context  An existing slave context
   * @param slave_name     The name assigned to the slave context, for debugging
   *                       purposes
   *
   * @return A new context
   *
   * @throws JCGLException             On errors
   * @throws JCGLExceptionUnsupported  If the context is of a version that is
   *                                   not supported
   * @throws JCGLExceptionNonCompliant If the context violates the OpenGL
   *                                   specification
   */

  Pair<JCGLContextType, JCGLContextType> newSharedContextsFrom(
    long master_context,
    String master_name,
    long slave_context,
    String slave_name)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant;
}
