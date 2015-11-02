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
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLContextUsableType;
import com.io7m.jcanephora.core.api.JCGLImplementationType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jnull.NullCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type of fake contexts.
 */

public final class FakeContext implements JCGLContextType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(FakeContext.class);
  }

  private final    FakeInterfaceGL33      gl33;
  private final    Set<FakeContext>       shared_with;
  private final    JCGLImplementationFake implementation;
  private final    String                 name;
  private final    AtomicInteger          next_id;
  private final    FakeShaderListenerType shader_listener;
  private volatile boolean                destroyed;
  private volatile boolean                current;

  /**
   * Construct a context.
   *
   * @param i           The implementation
   * @param in_name     The name
   * @param in_listener The shader listener
   *
   * @throws JCGLExceptionNonCompliant On non-compliant OpenGL implementations
   */

  public FakeContext(
    final JCGLImplementationFake i,
    final String in_name,
    final FakeShaderListenerType in_listener)
    throws JCGLExceptionNonCompliant
  {
    this.next_id = new AtomicInteger(1);
    this.shader_listener = NullCheck.notNull(in_listener);
    this.gl33 = new FakeInterfaceGL33(this);
    this.destroyed = false;
    this.shared_with = new HashSet<>(8);
    this.implementation = i;
    this.name = NullCheck.notNull(in_name);
  }

  int getFreshID()
  {
    return this.next_id.getAndIncrement();
  }

  void setSharedWith(final FakeContext other)
  {
    FakeContext.LOG.debug("sharing context {} with {}", this, other);
    this.shared_with.add(other);
    other.shared_with.add(this);
  }

  @Override public String toString()
  {
    return String.format("[FakeContext %s]", this.name);
  }

  @Override public String contextGetName()
  {
    return this.name;
  }

  @Override public List<JCGLContextUsableType> contextGetShares()
  {
    this.checkNotDestroyed();

    final List<JCGLContextUsableType> xs =
      new ArrayList<>(this.shared_with.size());
    xs.addAll(this.shared_with);
    return xs;
  }

  void checkNotDestroyed()
  {
    if (this.destroyed) {
      throw new JCGLExceptionDeleted(
        String.format("Context %s is destroyed", this));
    }
  }

  @Override public boolean contextIsSharedWith(final JCGLContextUsableType c)
  {
    this.checkNotDestroyed();

    if (c instanceof FakeContext) {
      final FakeContext jc = (FakeContext) c;
      jc.checkNotDestroyed();
      return this.shared_with.contains(c);
    }

    return false;
  }

  @Override public boolean contextIsCurrent()
  {
    this.checkNotDestroyed();
    return this.current;
  }

  @Override public void contextMakeCurrent()
  {
    this.checkNotDestroyed();
    FakeContext.LOG.trace("make current");
    this.current = true;
  }

  @Override public void contextReleaseCurrent()
  {
    this.checkNotDestroyed();
    FakeContext.LOG.trace("release current");
    this.current = false;
  }

  @Override public JCGLInterfaceGL33Type contextGetGL33()
  {
    this.checkNotDestroyed();
    return this.gl33;
  }

  @Override public JCGLImplementationType contextGetImplementation()
  {
    this.checkNotDestroyed();
    return this.implementation;
  }

  @Override public void contextDestroy()
    throws JCGLExceptionDeleted
  {
    this.checkNotDestroyed();
    this.destroyed = true;
  }

  @Override public boolean isDeleted()
  {
    return this.destroyed;
  }

  FakeShaderListenerType getShaderListener()
  {
    return this.shader_listener;
  }
}
