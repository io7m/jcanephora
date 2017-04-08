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
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.fake.JCGLImplementationFake;
import com.io7m.jcanephora.fake.JCGLImplementationFakeType;
import com.io7m.jcanephora.tests.contracts.JCGLIndexBuffersContract;
import com.io7m.jcanephora.tests.contracts.JCGLSharedContextPair;
import com.io7m.jcanephora.tests.contracts.JCGLUnsharedContextPair;
import com.io7m.junreachable.UnreachableCodeException;

public final class FakeIndexBuffersTest extends JCGLIndexBuffersContract
{
  @Override
  public void onTestCompleted()
  {

  }

  @Override
  protected Interfaces getIndexBuffers(final String name)
  {
    try {
      final JCGLImplementationFakeType i = JCGLImplementationFake.getInstance();
      final JCGLContextType c =
        i.newContext(name, new FakeDefaultShaderListener());
      final JCGLInterfaceGL33Type cg = c.contextGetGL33();
      return new Interfaces(
        c, cg.getArrayBuffers(), cg.getIndexBuffers(), cg.getArrayObjects());
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }

  @Override
  protected JCGLUnsharedContextPair<JCGLIndexBuffersType>
  getIndexBuffersUnshared(
    final String main,
    final String alt)
  {
    try {
      final JCGLImplementationFakeType i = JCGLImplementationFake.getInstance();
      final JCGLContextType c0 =
        i.newContext(main, new FakeDefaultShaderListener());
      final JCGLContextType c1 =
        i.newContext(alt, new FakeDefaultShaderListener());
      final JCGLInterfaceGL33Type g33_0 = c0.contextGetGL33();
      final JCGLInterfaceGL33Type g33_1 = c1.contextGetGL33();
      return new JCGLUnsharedContextPair<>(
        g33_0.getIndexBuffers(), c0, g33_1.getIndexBuffers(), c1);
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }

  @Override
  protected JCGLSharedContextPair<JCGLIndexBuffersType>
  getIndexBuffersSharedWith(
    final String name,
    final String shared)
  {
    try {
      final JCGLImplementationFakeType i = JCGLImplementationFake.getInstance();
      final JCGLContextType c0 =
        i.newContext(name, new FakeDefaultShaderListener());
      final JCGLContextType c1 =
        i.newContextSharedWith(c0, name, new FakeDefaultShaderListener());
      final JCGLInterfaceGL33Type g0 = c0.contextGetGL33();
      final JCGLInterfaceGL33Type g1 = c1.contextGetGL33();

      return new JCGLSharedContextPair<>(
        g0.getIndexBuffers(), c0, g1.getIndexBuffers(), c1);
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }
}
