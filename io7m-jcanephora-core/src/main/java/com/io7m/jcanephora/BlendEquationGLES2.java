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

package com.io7m.jcanephora;

/**
 * Blend equations supported on OpenGL ES2
 */

public enum BlendEquationGLES2
{
  /**
   * Add the source and destination colors; <code>s + d</code>;
   * <code>GL_FUNC_ADD</code>
   */

  BLEND_EQUATION_ADD,

  /**
   * Subtract the source from the destination colors; <code>d - s</code>;
   * <code>GL_FUNC_REVERSE_SUBTRACT</code>
   */

  BLEND_EQUATION_REVERSE_SUBTRACT,

  /**
   * Subtract the destination from the source colors; <code>s - d</code>;
   * <code>GL_FUNC_SUBTRACT</code>
   */

  BLEND_EQUATION_SUBTRACT
}
