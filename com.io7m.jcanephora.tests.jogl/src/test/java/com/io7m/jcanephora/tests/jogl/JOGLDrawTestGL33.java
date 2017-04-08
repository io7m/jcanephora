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

import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.tests.contracts.JCGLDrawContract;

public final class JOGLDrawTestGL33 extends JCGLDrawContract
{
  @Override
  public void onTestCompleted()
  {
    JOGLTestContexts.closeAllContexts();
  }

  @Override
  protected Interfaces getInterfaces(final String name)
  {
    final JCGLContextType c = JOGLTestContexts.newGL33Context(name, 24, 8);
    final JCGLInterfaceGL33Type cg = c.contextGetGL33();
    return new Interfaces(
      c,
      cg.getArrayBuffers(),
      cg.getIndexBuffers(),
      cg.getArrayObjects(),
      cg.getShaders(),
      cg.getDraw());
  }
}
