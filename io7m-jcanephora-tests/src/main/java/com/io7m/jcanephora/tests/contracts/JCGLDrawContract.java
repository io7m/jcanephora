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

import com.io7m.jcanephora.core.JCGLPrimitives;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLDrawType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheckException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Array buffer contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLDrawContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract Interfaces getInterfaces(String name);

  @Test public final void testDrawBadRange()
  {
    final Interfaces i = this.getInterfaces("main");

    this.expected.expect(RangeCheckException.class);
    i.getDraw().draw(JCGLPrimitives.PRIMITIVE_TRIANGLES, -1, -1);
  }

  protected static final class Interfaces
  {
    private final JCGLContextType      context;
    private final JCGLIndexBuffersType index_buffers;
    private final JCGLArrayBuffersType array_buffers;
    private final JCGLArrayObjectsType array_objects;
    private final JCGLShadersType      shaders;
    private final JCGLDrawType         draw;

    public Interfaces(
      final JCGLContextType in_context,
      final JCGLArrayBuffersType in_array_buffers,
      final JCGLIndexBuffersType in_index_buffers,
      final JCGLArrayObjectsType in_array_objects,
      final JCGLShadersType in_shaders,
      final JCGLDrawType in_draw)
    {
      this.context = NullCheck.notNull(in_context);
      this.array_buffers = NullCheck.notNull(in_array_buffers);
      this.array_objects = NullCheck.notNull(in_array_objects);
      this.index_buffers = NullCheck.notNull(in_index_buffers);
      this.shaders = NullCheck.notNull(in_shaders);
      this.draw = NullCheck.notNull(in_draw);
    }

    public JCGLDrawType getDraw()
    {
      return this.draw;
    }

    public JCGLShadersType getShaders()
    {
      return this.shaders;
    }

    public JCGLContextType getContext()
    {
      return this.context;
    }

    public JCGLIndexBuffersType getIndexBuffers()
    {
      return this.index_buffers;
    }

    public JCGLArrayBuffersType getArrayBuffers()
    {
      return this.array_buffers;
    }

    public JCGLArrayObjectsType getArrayObjects()
    {
      return this.array_objects;
    }
  }
}
