/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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
import com.io7m.jfunctional.Pair;

/**
 * A LWJGL3 implementation of the {@link JCGLImplementationLWJGL3Type}
 * interface.
 */

public final class JCGLImplementationLWJGL3 implements
  JCGLImplementationLWJGL3Type
{
  private static final JCGLImplementationLWJGL3 INSTANCE =
    new JCGLImplementationLWJGL3();

  private JCGLImplementationLWJGL3()
  {

  }

  /**
   * @return A reference to the LWJGL3 implementation
   */

  public static JCGLImplementationLWJGL3Type getInstance()
  {
    return INSTANCE;
  }

  @Override
  public JCGLContextType newUnsharedContextFrom(
    final long context,
    final String name)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant
  {
    return LWJGL3Context.createUnshared(this, context, name);
  }

  @Override
  public Pair<JCGLContextType, JCGLContextType> newSharedContextsFrom(
    final long master_context,
    final String master_name,
    final long slave_context,
    final String slave_name)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant
  {
    return LWJGL3Context.createShared(
      this,
      master_context,
      master_name,
      slave_context,
      slave_name);
  }
}
