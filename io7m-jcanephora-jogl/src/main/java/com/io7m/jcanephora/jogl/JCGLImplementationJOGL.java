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
import com.io7m.jcanephora.core.JCGLExceptionContextNotCurrent;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLExceptionUnsupported;
import com.io7m.jcanephora.core.JCGLVersionNumber;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jnull.NullCheck;
import com.jogamp.common.util.VersionNumber;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;

import java.util.function.Function;

/**
 * A JOGL implementation of the {@link JCGLImplementationJOGLType} interface.
 */

public final class JCGLImplementationJOGL implements JCGLImplementationJOGLType
{
  private static final JCGLImplementationJOGL INSTANCE =
    new JCGLImplementationJOGL();

  private JCGLImplementationJOGL()
  {

  }

  /**
   * @return A reference to the JOGL implementation
   */

  public static JCGLImplementationJOGLType getInstance()
  {
    return JCGLImplementationJOGL.INSTANCE;
  }

  private static void checkVersion(final VersionNumber v)
    throws JCGLExceptionUnsupported
  {
    final int maj = v.getMajor();
    final int min = v.getMinor();
    if (maj != 3 || min != 3) {
      final JCGLVersionNumber v_exp = new JCGLVersionNumber(3, 3, 0);
      final JCGLVersionNumber v_got =
        new JCGLVersionNumber(v.getMajor(), v.getMinor(), v.getSub());
      throw new JCGLExceptionUnsupported(v_exp, v_got);
    }
  }

  @Override
  public JCGLContextType newContextFrom(
    final GLContext c,
    final String name)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant
  {
    return this.newContextFromWithSupplier(c, g -> g.getGL().getGL3(), name);
  }

  @Override
  public JCGLContextType newContextFromWithSupplier(
    final GLContext c,
    final Function<GLContext, GL3> gl_supplier,
    final String name)
    throws JCGLException, JCGLExceptionUnsupported, JCGLExceptionNonCompliant
  {
    NullCheck.notNull(c);
    NullCheck.notNull(gl_supplier);
    NullCheck.notNull(name);

    if (!c.isCurrent()) {
      throw new JCGLExceptionContextNotCurrent(
        String.format("Context '%s' is not current", c));
    }

    final VersionNumber v = c.getGLVersionNumber();
    JCGLImplementationJOGL.checkVersion(v);
    return new JOGLContext(this, c, gl_supplier, name);
  }
}
