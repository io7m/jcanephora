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

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jcanephora.core.JCGLArrayObjectType;
import com.io7m.jcanephora.core.JCGLArrayObjectUsableType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeType;
import com.io7m.jcanephora.core.JCGLIndexBufferUsableType;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import net.jcip.annotations.GuardedBy;

import java.util.Optional;
import java.util.Set;

final class LWJGL3ArrayObject extends LWJGL3ObjectUnshared
  implements JCGLArrayObjectType
{
  private final JCGLArrayVertexAttributeType[] attribs;
  private final String image;
  private final LWJGL3ReferenceContainer reference_container;
  private final boolean index_buffer_rebind;
  private final Object index_buffer_lock;
  private @GuardedBy("index_buffer_lock") @Nullable
  LWJGL3IndexBuffer index_buffer;

  LWJGL3ArrayObject(
    final LWJGL3Context in_context,
    final int in_id,
    final boolean in_index_buffer_allow_rebind,
    final LWJGL3IndexBuffer in_index_buffer,
    final JCGLArrayVertexAttributeType[] in_attribs)
  {
    super(in_context, in_id);
    this.attribs = NullCheck.notNull(in_attribs, "Attributes");

    this.index_buffer_lock = new Object();
    this.index_buffer = in_index_buffer;
    this.index_buffer_rebind = in_index_buffer_allow_rebind;

    this.image = String.format(
      "[ArrayObject %d]", Integer.valueOf(this.glName()));

    this.reference_container = new LWJGL3ReferenceContainer(this, 8);
    if (in_index_buffer != null) {
      this.reference_container.referenceAdd(in_index_buffer);
    }

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

  @Nullable
  LWJGL3IndexBuffer indexBuffer()
  {
    synchronized (this.index_buffer_lock) {
      return this.index_buffer;
    }
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
  public Optional<JCGLArrayVertexAttributeType> attributeAt(final int index)
  {
    return Optional.ofNullable(this.attribs[index]);
  }

  @Override
  public int attributeMaximumSupported()
  {
    return this.attribs.length;
  }

  @Override
  public Optional<JCGLIndexBufferUsableType> indexBufferBound()
  {
    synchronized (this.index_buffer_lock) {
      return Optional.ofNullable(this.index_buffer);
    }
  }

  @Override
  public Set<JCGLReferableType> references()
  {
    return this.reference_container.references();
  }

  void indexBufferIntroduce(
    final LWJGL3IndexBuffer target_ib)
  {
    synchronized (this.index_buffer_lock) {
      Preconditions.checkPrecondition(
        this.index_buffer_rebind,
        "Index buffer rebinding must be allowed");

      final LWJGL3IndexBuffer ib = this.index_buffer;
      if (ib != null) {
        this.reference_container.referenceRemove(ib);
      }
      if (target_ib != null) {
        this.reference_container.referenceAdd(target_ib);
      }
      this.index_buffer = target_ib;
    }
  }

  void indexBufferDelete()
  {
    synchronized (this.index_buffer_lock) {
      final LWJGL3IndexBuffer ib = this.index_buffer;
      if (ib != null) {
        this.reference_container.referenceRemove(ib);
      }
      this.index_buffer = null;
    }
  }
}
