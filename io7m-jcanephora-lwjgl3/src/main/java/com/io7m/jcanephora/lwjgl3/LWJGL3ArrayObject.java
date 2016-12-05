/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

import com.io7m.jcanephora.core.JCGLArrayObjectType;
import com.io7m.jcanephora.core.JCGLArrayObjectUsableType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeType;
import com.io7m.jcanephora.core.JCGLIndexBufferUsableType;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jnull.NullCheck;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

final class LWJGL3ArrayObject extends LWJGL3ObjectUnshared
  implements JCGLArrayObjectType
{
  private final JCGLArrayVertexAttributeType[] attribs;
  private final String image;
  private final LWJGL3ReferenceContainer reference_container;
  private Optional<JCGLIndexBufferUsableType> index_buffer;

  LWJGL3ArrayObject(
    final LWJGL3Context in_context,
    final int in_id,
    final JCGLArrayVertexAttributeType[] in_attribs)
  {
    super(in_context, in_id);
    this.attribs = NullCheck.notNull(in_attribs);
    this.index_buffer = Optional.empty();
    this.image = String.format(
      "[ArrayObject %d]", Integer.valueOf(this.getGLName()));

    this.reference_container = new LWJGL3ReferenceContainer(this, 8);
    for (int index = 0; index < in_attribs.length; ++index) {
      final JCGLArrayVertexAttributeType a = in_attribs[index];
      if (a != null) {
        this.reference_container.referenceAdd(
          (LWJGL3Referable) a.getArrayBuffer());
      }
    }
  }

  static LWJGL3ArrayObject checkArrayObject(
    final LWJGL3Context c,
    final JCGLArrayObjectUsableType a)
  {
    return (LWJGL3ArrayObject) LWJGL3CompatibilityChecks.checkAny(c, a);
  }

  JCGLArrayVertexAttributeType[] getAttribs()
  {
    return this.attribs;
  }

  @Override
  public String toString()
  {
    return this.image;
  }

  LWJGL3ReferenceContainer getReferenceContainer()
  {
    return this.reference_container;
  }

  @Override
  public Optional<JCGLArrayVertexAttributeType> getAttributeAt(final int index)
  {
    return Optional.ofNullable(this.attribs[index]);
  }

  @Override
  public int getMaximumVertexAttributes()
  {
    return this.attribs.length;
  }

  @Override
  public Optional<JCGLIndexBufferUsableType> getIndexBufferBound()
  {
    synchronized (this.index_buffer) {
      return this.index_buffer;
    }
  }

  void setIndexBuffer(
    final Function<Optional<JCGLIndexBufferUsableType>,
      Optional<JCGLIndexBufferUsableType>> f)
  {
    synchronized (this.index_buffer) {
      final Optional<JCGLIndexBufferUsableType> r = f.apply(this.index_buffer);
      this.index_buffer.ifPresent(
        i -> this.reference_container.referenceRemove((LWJGL3Referable) i));
      r.ifPresent(
        i -> this.reference_container.referenceAdd((LWJGL3Referable) i));
      this.index_buffer = r;
    }
  }

  @Override
  public Set<JCGLReferableType> getReferences()
  {
    return this.reference_container.getReferences();
  }

  LWJGL3IndexBuffer getIndexBufferUnsafe()
  {
    synchronized (this.index_buffer) {
      return (LWJGL3IndexBuffer) this.index_buffer.get();
    }
  }
}
