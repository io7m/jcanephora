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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.api.JCGLContextType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Context contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLContextContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract JCGLContextType newContext(String name);

  protected abstract JCGLSharedContextPair<JCGLContextType> newSharedContext(
    String name,
    String shared);

  @Test public final void testContextIdentities()
  {
    final JCGLContextType c0 = this.newContext("main");
    final JCGLContextType c1 = this.newContext("alt");

    Assert.assertFalse(c0.contextIsSharedWith(c1));
    Assert.assertSame(
      c0.contextGetImplementation(), c1.contextGetImplementation());

    c0.contextReleaseCurrent();
    c1.contextReleaseCurrent();
    Assert.assertFalse(c0.contextIsCurrent());
    Assert.assertFalse(c1.contextIsCurrent());
    Assert.assertEquals("main", c0.contextGetName());
    Assert.assertEquals("alt", c1.contextGetName());
  }

  @Test public final void testContextIdentitiesShared()
  {
    final JCGLSharedContextPair<JCGLContextType> p =
      this.newSharedContext("main", "alt");

    final JCGLContextType c0 = p.getMasterContext();
    final JCGLContextType c1 = p.getSlaveContext();

    Assert.assertTrue(c0.contextIsSharedWith(c1));
    Assert.assertSame(
      c0.contextGetImplementation(), c1.contextGetImplementation());
    Assert.assertTrue(c0.contextGetShares().contains(c1));
  }

  @Test public final void testContextSharedDestroyed()
  {
    final JCGLSharedContextPair<JCGLContextType> p =
      this.newSharedContext("main", "alt");

    final JCGLContextType c0 = p.getMasterContext();
    final JCGLContextType c1 = p.getSlaveContext();

    c1.contextDestroy();
    this.expected.expect(JCGLExceptionDeleted.class);
    c0.contextIsSharedWith(c1);
  }

  @Test public final void testContextDestroyedMakeCurrent()
  {
    final JCGLContextType c0 = this.newContext("main");

    c0.contextDestroy();
    this.expected.expect(JCGLExceptionDeleted.class);
    c0.contextMakeCurrent();
  }

  @Test public final void testContextDestroyedReleaseCurrent()
  {
    final JCGLContextType c0 = this.newContext("main");

    c0.contextDestroy();
    this.expected.expect(JCGLExceptionDeleted.class);
    c0.contextReleaseCurrent();
  }

  @Test public final void testContextDestroyedGetGL33()
  {
    final JCGLContextType c0 = this.newContext("main");

    c0.contextDestroy();
    this.expected.expect(JCGLExceptionDeleted.class);
    c0.contextGetGL33();
  }

  @Test public final void testContextDestroyedGetImplementation()
  {
    final JCGLContextType c0 = this.newContext("main");

    c0.contextDestroy();
    this.expected.expect(JCGLExceptionDeleted.class);
    c0.contextGetImplementation();
  }

  @Test public final void testContextDestroyedGetShares()
  {
    final JCGLContextType c0 = this.newContext("main");

    c0.contextDestroy();
    this.expected.expect(JCGLExceptionDeleted.class);
    c0.contextGetShares();
  }

  @Test public final void testContextDestroyedDestroy()
  {
    final JCGLContextType c0 = this.newContext("main");

    c0.contextDestroy();
    this.expected.expect(JCGLExceptionDeleted.class);
    c0.contextDestroy();
  }

  @Test public final void testContextDestroyedIsCurrent()
  {
    final JCGLContextType c0 = this.newContext("main");

    c0.contextDestroy();
    this.expected.expect(JCGLExceptionDeleted.class);
    c0.contextIsCurrent();
  }
}
