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

import com.io7m.jcanephora.async.JCGLAsyncInterfaceUsableGL33Type;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Async GL33 contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLAsyncInterfaceGL33Contract extends JCGLContract
{
  @Rule public ExpectedException expected = ExpectedException.none();

  protected abstract JCGLAsyncInterfaceUsableGL33Type getLoader(String name);

  @Test
  public final void testExecuteThreadDoesNotDie()
    throws Exception
  {
    final AtomicReference<Thread> thread_0 = new AtomicReference<>();

    final JCGLAsyncInterfaceUsableGL33Type ai = this.getLoader("main");
    final CompletableFuture<Object> f0 = ai.evaluate((g33, unused) -> {
      final Thread thread = Thread.currentThread();
      thread_0.set(thread);
      throw new RuntimeException();
    });

    try {
      f0.get(30L, TimeUnit.SECONDS);
    } catch (final ExecutionException e) {
      // Nothing
    } catch (final InterruptedException | TimeoutException e) {
      throw new UnreachableCodeException(e);
    }

    final CompletableFuture<Thread> f1 =
      ai.evaluate((g33, unused) -> Thread.currentThread());

    final Thread thread_1 = f1.get(30L, TimeUnit.SECONDS);
    Assert.assertEquals(thread_0.get(), thread_1);
  }
}
