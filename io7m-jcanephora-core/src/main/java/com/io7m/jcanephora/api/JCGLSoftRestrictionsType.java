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

/**
 * <p>
 * An interface that specifies restrictions to be placed on the OpenGL
 * implementation, for testing purposes.
 * </p>
 * <p>
 * For example, the number of texture units can be restricted in order to test
 * how a renderer behaves when too few texture units are available for
 * rendering.
 * </p>
 */

public interface JCGLSoftRestrictionsType
{
  /**
   * <p>
   * Restrict whether or not the extension <code>name</code> is visible to the
   * OpenGL implementation.
   * </p>
   * <p>
   * Whether or not a given extension is visible is given by
   * <code>r &amp;&amp; e</code>, where <code>r</code> is the value returned
   * by the function and <code>e</code> indicates whether or not the extension
   * is actually available.
   * </p>
   *
   * @param name
   *          The name of the extension.
   * @return <code>true</code> if the extension should be visible.
   */

  boolean restrictExtensionVisibility(
    final String name);

  /**
   * <p>
   * Restrict the number of texture units available in the OpenGL
   * implementation.
   * </p>
   * <p>
   * The number of texture units exposed to the implementation will be taken
   * as <code>max(1, min (r, count))</code>, where <code>r</code> is the value
   * returned by the function.
   * </p>
   *
   * @param count
   *          The actual number of texture units that are available.
   * @return The maximum number of texture units that should be exposed.
   */

  int restrictTextureUnitCount(
    final int count);
}
