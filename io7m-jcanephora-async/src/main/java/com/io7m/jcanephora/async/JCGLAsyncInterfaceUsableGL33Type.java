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

package com.io7m.jcanephora.async;

import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jfunctional.Unit;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

/**
 * <p>An "asynchronous" wrapper around an {@link JCGLInterfaceGL33Type}
 * interface.</p>
 */

@FunctionalInterface
public interface JCGLAsyncInterfaceUsableGL33Type
{
  /**
   * Evaluate a function on the context's dedicated OpenGL thread.
   *
   * @param context A user-defined context value, passed to {@code f}
   * @param f       The function that will be evaluated
   * @param <A>     The type of user-defined context values
   * @param <B>     The type of returned values
   *
   * @return The value returned by {@code f}
   */

  <A, B> CompletableFuture<B> evaluateWith(
    A context,
    BiFunction<JCGLInterfaceGL33Type, A, B> f);

  /**
   * Evaluate a function on the context's dedicated OpenGL thread.
   *
   * @param f   The function that will be evaluated
   * @param <B> The type of returned values
   *
   * @return The value returned by {@code f}
   */

  default <B> CompletableFuture<B> evaluate(
    BiFunction<JCGLInterfaceGL33Type, Unit, B> f)
  {
    return this.evaluateWith(Unit.unit(), f);
  }
}
