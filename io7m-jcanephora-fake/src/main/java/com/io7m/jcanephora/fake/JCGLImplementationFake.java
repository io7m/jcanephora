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
import com.io7m.jcanephora.core.JCGLExceptionWrongImplementation;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jnull.NullCheck;

/**
 * A fake implementation of the {@link JCGLImplementationFakeType} interface.
 */

public final class JCGLImplementationFake implements JCGLImplementationFakeType
{
  private static final JCGLImplementationFake INSTANCE =
    new JCGLImplementationFake();

  private JCGLImplementationFake()
  {

  }

  /**
   * @return A reference to the implementation
   */

  public static JCGLImplementationFakeType getInstance()
  {
    return JCGLImplementationFake.INSTANCE;
  }

  @Override public JCGLContextType newContext(
    final String name,
    final FakeShaderListenerType in_listener)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant
  {
    NullCheck.notNull(name);
    NullCheck.notNull(in_listener);

    return new FakeContext(this, name, in_listener);
  }

  @Override public JCGLContextType newContextSharedWith(
    final JCGLContextType c,
    final String name,
    final FakeShaderListenerType in_listener)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant
  {
    NullCheck.notNull(c);
    NullCheck.notNull(name);
    NullCheck.notNull(in_listener);

    if (c instanceof FakeContext) {
      final FakeContext c_orig = (FakeContext) c;
      c_orig.checkNotDestroyed();
      final FakeContext cn = new FakeContext(this, name, in_listener);
      cn.setSharedWith(c_orig);
      return cn;
    }

    throw new JCGLExceptionWrongImplementation(
      String.format(
        "Context %s does not belong to this implmentation", c));
  }
}
