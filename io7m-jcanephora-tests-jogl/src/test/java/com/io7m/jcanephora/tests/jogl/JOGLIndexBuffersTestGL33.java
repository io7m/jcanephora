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

package com.io7m.jcanephora.tests.jogl;

import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.tests.contracts.JCGLIndexBuffersContract;
import com.io7m.jcanephora.tests.contracts.JCGLSharedContextPair;
import com.io7m.jcanephora.tests.contracts.JCGLUnsharedContextPair;

public final class JOGLIndexBuffersTestGL33 extends JCGLIndexBuffersContract
{
  @Override public void onTestCompleted()
  {
    JOGLTestContexts.closeAllContexts();
  }

  @Override protected Interfaces getIndexBuffers(final String name)
  {
    final JCGLContextType c = JOGLTestContexts.newGL33Context(name, 24, 8);
    final JCGLInterfaceGL33Type cg = c.contextGetGL33();
    final JCGLArrayBuffersType ga = cg.getArrayBuffers();
    final JCGLIndexBuffersType gi = cg.getIndexBuffers();
    final JCGLArrayObjectsType go = cg.getArrayObjects();
    return new Interfaces(c, ga, gi, go);
  }

  @Override
  protected JCGLUnsharedContextPair<JCGLIndexBuffersType>
  getIndexBuffersUnshared(
    final String main,
    final String alt)
  {
    final JCGLContextType c0 = JOGLTestContexts.newGL33Context(main, 24, 8);
    final JCGLContextType c1 = JOGLTestContexts.newGL33Context(alt, 24, 8);
    return new JCGLUnsharedContextPair<>(
      c0.contextGetGL33().getIndexBuffers(),
      c0,
      c1.contextGetGL33().getIndexBuffers(),
      c1);
  }

  @Override
  protected JCGLSharedContextPair<JCGLIndexBuffersType>
  getIndexBuffersSharedWith(
    final String name,
    final String shared)
  {
    final JCGLSharedContextPair<JCGLContextType> p =
      JOGLTestContexts.newGL33ContextSharedWith(
        name, shared);

    final JCGLContextType mc = p.getMasterContext();
    final JCGLContextType sc = p.getSlaveContext();
    return new JCGLSharedContextPair<>(
      mc.contextGetGL33().getIndexBuffers(),
      mc,
      sc.contextGetGL33().getIndexBuffers(),
      sc);
  }
}
