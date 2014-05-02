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

package com.io7m.jcanephora.jogl;

import javax.media.opengl.GLContext;

import com.io7m.jcanephora.JCGLNameType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;

/**
 * <p>
 * An object that cannot be shared across contexts, according to the OpenGL
 * specification.
 * </p>
 * <p>
 * As of 4.3, the list includes:
 * </p>
 * <ul>
 * <li>Framebuffer objects</li>
 * <li>Program pipeline objects</li>
 * <li>Query objects</li>
 * <li>Transform feedback objects</li>
 * <li>Vertex array objects</li>
 * </ul>
 */

abstract class JOGLObjectUnshared extends JOGLObjectDeletable implements
  JCGLNameType
{
  private final GLContext context;
  private final int       id;

  protected JOGLObjectUnshared(
    final GLContext in_context,
    final int in_id)
  {
    this.context = NullCheck.notNull(in_context, "Context");
    this.id =
      (int) RangeCheck.checkIncludedIn(
        in_id,
        "Identifier",
        RangeCheck.POSITIVE_INTEGER,
        "Valid OpenGL identifiers");
  }

  protected final GLContext getContext()
  {
    return this.context;
  }

  @Override public final int getGLName()
  {
    return this.id;
  }
}
