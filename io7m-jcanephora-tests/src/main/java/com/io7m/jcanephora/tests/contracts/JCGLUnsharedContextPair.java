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

import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jnull.NullCheck;

/**
 * A pair of contexts that are not shared.
 *
 * @param <T> The type of values associated with the contexts
 */

public final class JCGLUnsharedContextPair<T>
{
  private final JCGLContextType context_a;
  private final T value_a;
  private final JCGLContextType context_b;
  private final T value_b;

  /**
   * Construct a pair.
   *
   * @param in_value_a   The first value
   * @param in_context_a The first context
   * @param in_value_b   The second value
   * @param in_context_b The second context
   */

  public JCGLUnsharedContextPair(
    final T in_value_a,
    final JCGLContextType in_context_a,
    final T in_value_b,
    final JCGLContextType in_context_b)
  {
    this.value_a = NullCheck.notNull(in_value_a);
    this.context_a = NullCheck.notNull(in_context_a);
    this.value_b = NullCheck.notNull(in_value_b);
    this.context_b = NullCheck.notNull(in_context_b);
  }

  /**
   * @return The first context
   */

  public JCGLContextType getContextA()
  {
    return this.context_a;
  }

  /**
   * @return The second context
   */

  public JCGLContextType getContextB()
  {
    return this.context_b;
  }

  /**
   * @return The first value
   */

  public T getValueA()
  {
    return this.value_a;
  }

  /**
   * @return The second value
   */

  public T getValueB()
  {
    return this.value_b;
  }
}
