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

import com.io7m.jcanephora.async.JCGLAsyncInterfaceGL33;
import com.io7m.jcanephora.async.JCGLAsyncInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.tests.contracts.JCGLAsyncInterfaceGL33Contract;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class JOGLAsyncInterfaceGL33TestGL33 extends
  JCGLAsyncInterfaceGL33Contract
{
  private final List<JCGLAsyncInterfaceGL33Type> async_gs = new ArrayList<>();

  @Override
  protected JCGLAsyncInterfaceGL33Type getLoader(final String name)
  {
    final JCGLContextType c =
      JOGLTestContexts.newGL33Context(name, 24, 8);
    Assert.assertTrue(c.contextIsCurrent());
    c.contextReleaseCurrent();
    Assert.assertFalse(c.contextIsCurrent());

    final JCGLAsyncInterfaceGL33Type g =
      JCGLAsyncInterfaceGL33.newAsync(() -> c);

    this.async_gs.add(g);
    return g;
  }

  @Override
  public void onTestCompleted()
  {
    final Iterator<JCGLAsyncInterfaceGL33Type> iter = this.async_gs.iterator();
    while (iter.hasNext()) {
      final JCGLAsyncInterfaceGL33Type g = iter.next();
      try {
        g.shutDown().get();
      } catch (final InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
      iter.remove();
    }

    JOGLTestContexts.closeAllContexts();
  }
}
