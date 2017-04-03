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
 * A pair of contexts that are shared.
 *
 * @param <T> The precise type of value associated with each context
 */

public final class JCGLSharedContextPair<T>
{
  private final JCGLContextType master_context;
  private final T master;
  private final JCGLContextType slave_context;
  private final T slave;

  /**
   * Construct a pair.
   *
   * @param in_master         The master value
   * @param in_master_context The master context
   * @param in_slave          The slave value
   * @param in_slave_context  The slave context
   */

  public JCGLSharedContextPair(
    final T in_master,
    final JCGLContextType in_master_context,
    final T in_slave,
    final JCGLContextType in_slave_context)
  {
    this.master = NullCheck.notNull(in_master);
    this.master_context = NullCheck.notNull(in_master_context);
    this.slave = NullCheck.notNull(in_slave);
    this.slave_context = NullCheck.notNull(in_slave_context);
  }

  /**
   * @return The master context
   */

  public JCGLContextType getMasterContext()
  {
    return this.master_context;
  }

  /**
   * @return The slave context
   */

  public JCGLContextType getSlaveContext()
  {
    return this.slave_context;
  }

  /**
   * @return The master value
   */

  public T getMasterValue()
  {
    return this.master;
  }

  /**
   * @return The slave value
   */

  public T getSlaveValue()
  {
    return this.slave;
  }
}
