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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLNamedType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.Ranges;

//@formatter:off

/**
 * <p>
 * An object that cannot be shared across contexts, according to the OpenGL
 * specification.
 * </p>
 *
 * <p>
 * As of 4.3, the list includes:
 * </p>
 *
 * <ul>
 * <li>Framebuffer objects</li>
 * <li>Program pipeline objects</li>
 * <li>Query objects</li>
 * <li>Transform feedback objects</li>
 * <li>Vertex array objects</li>
 * </ul>
 */

// @formatter:on

abstract class LWJGL3ObjectUnshared extends LWJGL3ObjectDeletable
  implements JCGLNamedType
{
  private final LWJGL3Context context;
  private final int id;

  protected LWJGL3ObjectUnshared(
    final LWJGL3Context in_context,
    final int in_id)
  {
    this.context = NullCheck.notNull(in_context, "Context");
    this.id = RangeCheck.checkIncludedInInteger(
      in_id, "Identifier", Ranges.POSITIVE_INTEGER, "Valid OpenGL identifiers");
  }

  public final LWJGL3Context getContext()
  {
    return this.context;
  }

  @Override
  public final int getGLName()
  {
    return this.id;
  }
}
