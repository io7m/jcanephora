/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora;

import javax.annotation.Nonnull;

/**
 * An enumerated type with the values representing the various subsets of the
 * OpenGL API.
 */

public enum JCGLApi
{
  /** The "ES" profile, as implemented by most embedded systems. */
  JCGL_ES("OpenGL ES"),

  /** The "full" OpenGL API, as implemented by most consumer GPUs. */
  JCGL_FULL("OpenGL");

  private final @Nonnull String name;

  private JCGLApi(
    final @Nonnull String name1)
  {
    assert name1 != null;
    this.name = name1;
  }

  public @Nonnull String getName()
  {
    return this.name;
  }
}
