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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLShadersType;

final class FakeInterfaceGL33 implements JCGLInterfaceGL33Type
{
  private final FakeArrayBuffers array_buffers;
  private final FakeArrayObjects array_objects;
  private final FakeShaders      shaders;

  FakeInterfaceGL33(final FakeContext c)
    throws JCGLExceptionNonCompliant
  {
    this.array_buffers = new FakeArrayBuffers(c);
    this.array_objects = new FakeArrayObjects(c, this.array_buffers);
    this.shaders = new FakeShaders(c);
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
}
