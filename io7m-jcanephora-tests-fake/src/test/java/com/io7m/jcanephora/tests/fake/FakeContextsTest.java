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

package com.io7m.jcanephora.tests.fake;

import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLExceptionUnsupported;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.fake.JCGLImplementationFake;
import com.io7m.jcanephora.fake.JCGLImplementationFakeType;
import com.io7m.jcanephora.tests.contracts.JCGLContextContract;
import com.io7m.jcanephora.tests.contracts.JCGLSharedContextPair;
import com.io7m.junreachable.UnreachableCodeException;

public final class FakeContextsTest extends JCGLContextContract
{
  @Override protected JCGLContextType newContext(final String name)
  {
    try {
      final JCGLImplementationFakeType i = JCGLImplementationFake.getInstance();
      return i.newContext(name);
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }

  @Override protected JCGLSharedContextPair<JCGLContextType> newSharedContext(
    final String name,
    final String shared)
  {
    try {
      final JCGLImplementationFakeType i = JCGLImplementationFake.getInstance();
      final JCGLContextType c0 = i.newContext(name);
      final JCGLContextType c1 = i.newContextSharedWith(c0, name);
      return new JCGLSharedContextPair<>(c0, c0, c1, c1);
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }

  @Override public void onTestCompleted()
  {

  }
}
