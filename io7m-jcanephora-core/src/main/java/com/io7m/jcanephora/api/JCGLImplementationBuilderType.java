/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.api;

import java.io.PrintStream;

import com.io7m.jfunctional.OptionType;

/**
 * The type of mutable builders for constructing JCGL implementations.
 */

public interface JCGLImplementationBuilderType
{
  /**
   * <p>
   * Enable or disable debugging. When debugging is enabled, errors will be
   * thrown when OpenGL calls fail.
   * </p>
   * <p>
   * By default, debugging is disabled.
   * </p>
   *
   * @param enabled
   *          <code>true</code> if debugging should be enabled.
   */

  void setDebugging(
    boolean enabled);

  /**
   * <p>
   * Enable or disable soft restrictions. When soft restrictions are enabled,
   * certain OpenGL resources will be shown or hidden. This is useful for
   * constructing test environments: How well does the program run on an
   * implementation that only has four texture units?
   * </p>
   * <p>
   * By default, soft restrictions are disabled.
   * </p>
   *
   * @param r
   *          {@link com.io7m.jfunctional.Some} if soft restrictions should be
   *          enabled.
   */

  void setRestrictions(
    OptionType<JCGLSoftRestrictionsType> r);

  /**
   * <p>
   * Enable or disable state caching. When caching is enabled, multiple calls
   * to any function that sets some kind of state on the OpenGL implementation
   * (with the same argument) will result in only a single call.
   * </p>
   * <p>
   * By default, state caching is enabled.
   * </p>
   *
   * @param enabled
   *          <code>true</code> if state caching should be enabled.
   */

  void setStateCaching(
    boolean enabled);

  /**
   * <p>
   * Enable or disable tracing. Passing {@link com.io7m.jfunctional.Some} with
   * an output stream enables tracing to that stream (replacing any previous
   * stream).
   * </p>
   * <p>
   * By default, tracing is disabled.
   * </p>
   *
   * @param stream
   *          {@link com.io7m.jfunctional.Some} if tracing should be enabled.
   */

  void setTracing(
    OptionType<PrintStream> stream);
}
