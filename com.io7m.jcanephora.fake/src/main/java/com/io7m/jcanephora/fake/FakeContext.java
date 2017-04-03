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

import com.io7m.jcanephora.core.JCGLExceptionContextIsCurrent;
import com.io7m.jcanephora.core.JCGLExceptionContextNotCurrent;
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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The type of fake contexts.
 */

public final class FakeContext implements JCGLContextType
{
  private static final Logger                   LOG;
  private static final ThreadLocal<FakeContext> CURRENT;

  static {
    LOG = LoggerFactory.getLogger(FakeContext.class);
    CURRENT = new ThreadLocal<>();
  }

  private final    FakeInterfaceGL33      gl33;
  private final    Set<FakeContext>       shared_with;
  private final    JCGLImplementationFake implementation;
  private final    String                 name;
  private final    AtomicInteger          next_id;
  private final    FakeShaderListenerType shader_listener;
  private volatile boolean                destroyed;

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

  static FakeContext getCurrentContext()
  {
    return CURRENT.get();
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
    return Objects.equals(CURRENT.get(), this);
  }

  @Override public void contextMakeCurrent()
  {
    this.checkNotDestroyed();
    FakeContext.LOG.trace("make current");

    /**
     * If no context is current on this thread, make the context current.
     */

    final FakeContext actual_current = CURRENT.get();
    if (actual_current == null) {
      CURRENT.set(this);
    } else {

      /**
       * Any other situation is an error.
       */

      final StringBuilder sb = new StringBuilder(128);
      if (Objects.equals(this, actual_current)) {
        sb.append("The given context is already current.");
      } else {
        sb.append("The current thread already has a current context.");
      }

      sb.append(System.lineSeparator());
      sb.append("Context: ");
      sb.append(this);
      sb.append("Thread context: ");
      sb.append(actual_current);
      sb.append(System.lineSeparator());
      sb.append("Thread: ");
      sb.append(Thread.currentThread());
      sb.append(System.lineSeparator());
      final String m = sb.toString();
      FakeContext.LOG.error(m);
      throw new JCGLExceptionContextIsCurrent(m);
    }
  }

  @Override public void contextReleaseCurrent()
  {
    this.checkNotDestroyed();
    FakeContext.LOG.trace("release current");
    if (this.contextIsCurrent()) {
      CURRENT.set(null);
    } else {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Attempted to release a context that is not current.");
      sb.append(System.lineSeparator());
      sb.append("Context: ");
      sb.append(this);
      sb.append(System.lineSeparator());
      sb.append("Current context: ");
      sb.append(CURRENT.get());
      sb.append(System.lineSeparator());
      sb.append("Thread: ");
      sb.append(Thread.currentThread());
      sb.append(System.lineSeparator());
      final String m = sb.toString();
      FakeContext.LOG.error(m);
      throw new JCGLExceptionContextNotCurrent(m);
    }
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

    if (this.contextIsCurrent()) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Attempted to destroy a context that is still current.");
      sb.append(System.lineSeparator());
      sb.append("Context: ");
      sb.append(this);
      sb.append(System.lineSeparator());
      sb.append("Thread: ");
      sb.append(Thread.currentThread());
      sb.append(System.lineSeparator());
      final String m = sb.toString();
      FakeContext.LOG.error(m);
      throw new JCGLExceptionContextIsCurrent(m);
    }

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
