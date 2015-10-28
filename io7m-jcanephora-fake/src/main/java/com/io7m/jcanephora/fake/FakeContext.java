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

import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLImplementationType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

final class FakeContext implements JCGLContextType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(FakeContext.class);
  }

  private final    FakeInterfaceGL33  gl33;
  private final    Set<FakeContext>   shared_with;
  private final    JCGLImplementationFake implementation;
  private volatile boolean                destroyed;
  private volatile boolean                current;

  FakeContext(final JCGLImplementationFake i)
  {
    this.gl33 = new FakeInterfaceGL33(this);
    this.destroyed = false;
    this.shared_with = new HashSet<>(8);
    this.implementation = i;
  }

  @Override public boolean contextIsCurrent()
  {
    return this.current;
  }

  @Override public void contextMakeCurrent()
  {
    FakeContext.LOG.trace("make current");
    this.current = true;
  }

  @Override public void contextReleaseCurrent()
  {
    FakeContext.LOG.trace("release current");
    this.current = false;
  }

  @Override public JCGLInterfaceGL33Type contextGetGL33()
  {
    return this.gl33;
  }

  @Override public JCGLImplementationType contextGetImplementation()
  {
    return this.implementation;
  }

  @Override public void contextDestroy()
    throws JCGLExceptionDeleted
  {
    if (this.destroyed) {
      throw new JCGLExceptionDeleted("Context is already destroyed");
    }

    this.destroyed = true;
  }

  @Override public boolean isDeleted()
  {
    return this.destroyed;
  }

  boolean isSharedWith(final FakeContext target)
  {
    return this.shared_with.contains(target);
  }
}
