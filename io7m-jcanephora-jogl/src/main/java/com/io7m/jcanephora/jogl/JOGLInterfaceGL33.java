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

package com.io7m.jcanephora.jogl;

import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLDrawType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jnull.NullCheck;

final class JOGLInterfaceGL33 implements JCGLInterfaceGL33Type
{
  private final JOGLArrayBuffers     array_buffers;
  private final JCGLArrayObjectsType array_objects;
  private final JOGLShaders          shaders;
  private final JOGLIndexBuffers     index_buffers;
  private final JOGLDraw             draw;

  JOGLInterfaceGL33(
    final JOGLContext c)
    throws JCGLExceptionNonCompliant
  {
    NullCheck.notNull(c);

    this.array_buffers = new JOGLArrayBuffers(c);
    this.index_buffers = new JOGLIndexBuffers(c);
    this.array_objects =
      new JOGLArrayObjects(c, this.array_buffers, this.index_buffers);
    this.shaders = new JOGLShaders(c);
    this.draw = new JOGLDraw(c, this.array_objects, this.index_buffers);
  }

  @Override public JCGLArrayBuffersType getArrayBuffers()
  {
    return this.array_buffers;
  }

  @Override public JCGLArrayObjectsType getArrayObjects()
  {
    return this.array_objects;
  }

  @Override public JCGLShadersType getShaders()
  {
    return this.shaders;
  }

  @Override public JCGLIndexBuffersType getIndexBuffers()
  {
    return this.index_buffers;
  }

  @Override public JCGLDrawType getDraw()
  {
    return this.draw;
  }
}
