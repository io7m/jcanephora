/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLExceptionContextIsCurrent;
import com.io7m.jcanephora.core.JCGLExceptionContextNotCurrent;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLContextUsableType;
import com.io7m.jcanephora.core.api.JCGLImplementationType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jfunctional.Pair;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.NullCheckException;
import com.io7m.jnull.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class LWJGL3Context implements JCGLContextType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(LWJGL3Context.class);
  }

  private final String name;
  private final List<LWJGL3Context> shares;
  private final List<JCGLContextUsableType> shares_view;
  private final JCGLImplementationLWJGL3 implementation;
  private final long context;
  private final LWJGL3InterfaceGL33 g33;
  private boolean destroyed;
  private volatile @Nullable GLCapabilities capabilities;

  LWJGL3Context(
    final JCGLImplementationLWJGL3 in_implementation,
    final long in_context,
    final String in_name)
    throws JCGLExceptionNonCompliant
  {
    this.implementation =
      NullCheck.notNull(in_implementation, "Implementation");

    if (in_context == MemoryUtil.NULL) {
      throw new NullCheckException("Null GLFW context");
    }

    this.name = NullCheck.notNull(in_name, "Context name");
    this.context = in_context;
    this.g33 = new LWJGL3InterfaceGL33(this);
    this.destroyed = false;
    this.shares = new ArrayList<>(1);
    this.shares_view = Collections.unmodifiableList(this.shares);

    LWJGL3Context.LOG.debug("created context {}", this.name);
  }

  static JCGLContextType createUnshared(
    final JCGLImplementationLWJGL3 in_implementation,
    final long context,
    final String context_name)
    throws JCGLExceptionNonCompliant
  {
    NullCheck.notNull(in_implementation, "Implementation");

    NullCheck.notNull(context_name, "Context name");
    if (context == MemoryUtil.NULL) {
      throw new NullCheckException("Context");
    }

    return new LWJGL3Context(in_implementation, context, context_name);
  }

  static Pair<JCGLContextType, JCGLContextType> createShared(
    final JCGLImplementationLWJGL3 in_implementation,
    final long master,
    final String master_name,
    final long slave,
    final String slave_name)
    throws JCGLExceptionNonCompliant
  {
    NullCheck.notNull(in_implementation, "Implementation");

    NullCheck.notNull(master_name, "Master context name");
    if (master == MemoryUtil.NULL) {
      throw new NullCheckException("Master context");
    }
    NullCheck.notNull(slave_name, "Slave context name");
    if (slave == MemoryUtil.NULL) {
      throw new NullCheckException("Slave context");
    }

    final LWJGL3Context c_master =
      new LWJGL3Context(in_implementation, master, master_name);
    final LWJGL3Context c_slave =
      new LWJGL3Context(in_implementation, slave, slave_name);

    c_master.setShared(c_slave);
    c_slave.setShared(c_master);
    return Pair.pair(c_master, c_slave);
  }

  @Override
  public String contextGetName()
  {
    return this.name;
  }

  @Override
  public List<JCGLContextUsableType> contextGetShares()
  {
    this.checkNotDestroyed();
    return this.shares_view;
  }

  @Override
  public boolean contextIsSharedWith(final JCGLContextUsableType c)
  {
    this.checkNotDestroyed();
    return this.shares_view.contains(c);
  }

  private void checkNotDestroyed()
  {
    if (this.isDeleted()) {
      throw new JCGLExceptionDeleted(
        String.format("Context %s is destroyed", this));
    }
  }

  @Override
  public boolean contextIsCurrent()
  {
    this.checkNotDestroyed();
    return GLFW.glfwGetCurrentContext() == this.context;
  }

  @Override
  public void contextMakeCurrent()
  {
    this.checkNotDestroyed();

    if (LWJGL3Context.LOG.isTraceEnabled()) {
      final Thread t = Thread.currentThread();
      LWJGL3Context.LOG.trace(
        "make current (thread {} {})", Long.valueOf(t.getId()), t.getName());
    }

    /**
     * If no context is current on this thread, make the context current.
     */

    final long actual_current = GLFW.glfwGetCurrentContext();
    if (actual_current == MemoryUtil.NULL) {
      GLFW.glfwMakeContextCurrent(this.context);

      /**
       * LWJGL requires a call to {@link GL#setCapabilities(GLCapabilities)}
       * each time a context is made current.
       */

      if (this.capabilities != null) {
        GL.setCapabilities(this.capabilities);
      } else {
        this.capabilities = GL.createCapabilities();
      }

    } else {

      /**
       * Any other situation is an error.
       */

      final StringBuilder sb = new StringBuilder(128);
      if (actual_current == this.context) {
        sb.append("The given context is already current.");
      } else {
        sb.append("The current thread already has a current context.");
      }

      sb.append(System.lineSeparator());
      sb.append("Context: 0x");
      sb.append(Long.toHexString(this.context));
      sb.append("Thread context: 0x");
      sb.append(Long.toHexString(actual_current));
      sb.append(System.lineSeparator());
      sb.append("Thread: ");
      sb.append(Thread.currentThread());
      sb.append(System.lineSeparator());
      final String m = sb.toString();
      LWJGL3Context.LOG.error(m);
      throw new JCGLExceptionContextIsCurrent(m);
    }
  }

  @Override
  public void contextReleaseCurrent()
  {
    this.checkNotDestroyed();

    if (LWJGL3Context.LOG.isTraceEnabled()) {
      final Thread t = Thread.currentThread();
      LWJGL3Context.LOG.trace(
        "release current (thread {} {})", Long.valueOf(t.getId()), t.getName());
    }

    final long actual_current = GLFW.glfwGetCurrentContext();
    if (actual_current == this.context) {
      GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
    } else {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Attempted to release a context that is not current.");
      sb.append(System.lineSeparator());
      sb.append("Context: 0x");
      sb.append(Long.toHexString(this.context));
      sb.append(System.lineSeparator());
      sb.append("Current context: 0x");
      sb.append(Long.toHexString(actual_current));
      sb.append(System.lineSeparator());
      sb.append("Thread: ");
      sb.append(Thread.currentThread());
      sb.append(System.lineSeparator());
      final String m = sb.toString();
      LWJGL3Context.LOG.error(m);
      throw new JCGLExceptionContextNotCurrent(m);
    }
  }

  @Override
  public JCGLInterfaceGL33Type contextGetGL33()
  {
    this.checkNotDestroyed();
    return this.g33;
  }

  @Override
  public JCGLImplementationType contextGetImplementation()
  {
    this.checkNotDestroyed();
    return this.implementation;
  }

  @Override
  public void contextDestroy()
    throws JCGLExceptionDeleted
  {
    this.checkNotDestroyed();

    final long actual_current = GLFW.glfwGetCurrentContext();
    if (actual_current == this.context) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Attempted to destroy a context that is still current.");
      sb.append(System.lineSeparator());
      sb.append("Context: 0x");
      sb.append(Long.toHexString(this.context));
      sb.append(System.lineSeparator());
      sb.append("Thread: ");
      sb.append(Thread.currentThread());
      sb.append(System.lineSeparator());
      final String m = sb.toString();
      LWJGL3Context.LOG.error(m);
      throw new JCGLExceptionContextIsCurrent(m);
    }

    GLFW.glfwDestroyWindow(this.context);
    this.destroyed = true;
  }

  @Override
  public boolean isDeleted()
  {
    return this.destroyed;
  }

  boolean isShared()
  {
    return !this.shares_view.isEmpty();
  }

  private void setShared(final LWJGL3Context new_shared)
  {
    this.shares.add(new_shared);
  }
}
