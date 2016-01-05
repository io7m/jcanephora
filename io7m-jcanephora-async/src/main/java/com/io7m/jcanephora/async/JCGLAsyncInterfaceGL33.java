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

package com.io7m.jcanephora.async;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionConcurrency;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jnull.NullCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The default implementation of the {@link JCGLAsyncInterfaceGL33Type}
 * interface.
 */

public final class JCGLAsyncInterfaceGL33 implements JCGLAsyncInterfaceGL33Type
{
  private static final AtomicInteger ID_POOL;
  private static final Logger        LOG;

  static {
    ID_POOL = new AtomicInteger(0);
    LOG = LoggerFactory.getLogger(JCGLAsyncInterfaceGL33.class);
  }

  private final JCGLContextType       context;
  private final JCGLInterfaceGL33Type g33;
  private final ExecutorService       exec;
  private final AtomicBoolean         closing;

  private JCGLAsyncInterfaceGL33(
    final JCGLContextType in_context,
    final JCGLInterfaceGL33Type in_g33,
    final ExecutorService in_exec)
  {
    this.context = NullCheck.notNull(in_context);
    this.g33 = NullCheck.notNull(in_g33);
    this.exec = NullCheck.notNull(in_exec);
    this.closing = new AtomicBoolean(false);
  }

  /**
   * Construct an async interface, evaluating the given function to obtain a
   * context that will be used exclusively for async calls. A new dedicated
   * thread will be created for GL calls, and the context returned from {@code
   * c} will be made current on that thread.
   *
   * @param c The context supplier
   *
   * @return A new async interface
   */

  public static JCGLAsyncInterfaceGL33Type newAsync(
    final Supplier<JCGLContextType> c)
  {
    try {
      NullCheck.notNull(c);

      final ThreadFactory tf = r -> {
        final int id = JCGLAsyncInterfaceGL33.ID_POOL.getAndIncrement();
        final Thread t = new Thread(r);
        t.setDaemon(false);
        t.setName("jcgl-async-gl-" + id);
        return t;
      };

      final AtomicReference<JCGLContextType> context =
        new AtomicReference<>();
      final AtomicReference<JCGLInterfaceGL33Type> g33 =
        new AtomicReference<>();

      final ExecutorService exec = Executors.newFixedThreadPool(1, tf);

      exec.submit(() -> {
        final JCGLContextType cc = c.get();
        cc.contextMakeCurrent();
        context.set(cc);
        g33.set(cc.contextGetGL33());
      }).get();

      return new JCGLAsyncInterfaceGL33(context.get(), g33.get(), exec);
    } catch (final InterruptedException e) {
      throw new JCGLExceptionConcurrency(e);
    } catch (final ExecutionException e) {
      throw new JCGLException(e.getCause());
    }
  }

  private static JCGLExceptionDeleted alreadyClosing()
  {
    return new JCGLExceptionDeleted("Async interface is shutting down");
  }

  @Override
  public <T> CompletableFuture<T> evaluate(
    final Function<JCGLInterfaceGL33Type, T> c)
  {
    NullCheck.notNull(c);

    if (!this.closing.get()) {
      return CompletableFuture.supplyAsync(
        () -> {
          if (!this.closing.get()) {
            return c.apply(this.g33);
          }
          throw JCGLAsyncInterfaceGL33.alreadyClosing();
        }, this.exec);
    }

    throw JCGLAsyncInterfaceGL33.alreadyClosing();
  }

  @Override
  public CompletableFuture<Void> shutDown()
    throws JCGLException
  {
    if (this.closing.compareAndSet(false, true)) {
      JCGLAsyncInterfaceGL33.LOG.debug("scheduling shutdown");
      return CompletableFuture.runAsync(
        () -> {
          JCGLAsyncInterfaceGL33.LOG.debug(
            "attempting to release context");
          this.context.contextReleaseCurrent();
        }, this.exec)
        .handle((v, ex) -> {
          JCGLAsyncInterfaceGL33.LOG.debug(
            "attempting to shut down executor");
          this.exec.shutdown();
          return v;
        });
    }

    throw JCGLAsyncInterfaceGL33.alreadyClosing();
  }

  @Override
  public boolean isDeleted()
  {
    return this.closing.get();
  }
}
