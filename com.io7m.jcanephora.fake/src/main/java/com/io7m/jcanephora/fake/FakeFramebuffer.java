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

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentPointType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentType;
import com.io7m.jcanephora.core.JCGLFramebufferDepthAttachmentType;
import com.io7m.jcanephora.core.JCGLFramebufferDepthStencilAttachmentType;
import com.io7m.jcanephora.core.JCGLFramebufferType;
import com.io7m.jcanephora.core.JCGLReferableType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

final class FakeFramebuffer extends FakeObjectUnshared implements
  JCGLFramebufferType
{
  private final String image;
  private final
  Map<JCGLFramebufferColorAttachmentPointType,
    JCGLFramebufferColorAttachmentType> colors;
  private final FakeReferenceContainer refs;
  private int depth_bits;
  private int stencil_bits;

  FakeFramebuffer(
    final FakeContext in_context,
    final int in_id)
  {
    super(in_context, in_id);
    this.depth_bits = 0;
    this.stencil_bits = 0;
    this.colors = new HashMap<>(16);
    this.refs = new FakeReferenceContainer(this, 16);

    {
      final StringBuilder sb = new StringBuilder("[Framebuffer ");
      sb.append(super.glName());
      sb.append(']');
      this.image = sb.toString();
    }
  }

  @Override
  public String toString()
  {
    return this.image;
  }

  @Override
  public Optional<JCGLFramebufferColorAttachmentType>
  framebufferGetColorAttachment(
    final JCGLFramebufferColorAttachmentPointType c)
  {
    return Optional.ofNullable(this.colors.get(c));
  }

  @Override
  public int framebufferDepthBits()
  {
    return this.depth_bits;
  }

  @Override
  public int framebufferStencilBits()
  {
    return this.stencil_bits;
  }

  void setColorAttachment(
    final JCGLFramebufferColorAttachmentPointType p,
    final JCGLFramebufferColorAttachmentType a)
  {
    this.colors.put(p, a);
    this.refs.referenceAdd((FakeReferable) a);
  }

  @Override
  public Set<JCGLReferableType> references()
  {
    return this.refs.references();
  }

  void setDepthAttachment(
    final JCGLFramebufferDepthAttachmentType a,
    final int bits)
  {
    Preconditions.checkPreconditionI(
      this.depth_bits,
      this.depth_bits == 0,
      ignored -> "Depth bits must be zero");
    Preconditions.checkPreconditionI(
      this.stencil_bits,
      this.stencil_bits == 0,
      ignored -> "Stencil bits must be zero");

    this.refs.referenceAdd((FakeReferable) a);
    this.depth_bits = bits;
  }

  void setDepthStencilAttachment(
    final JCGLFramebufferDepthStencilAttachmentType a,
    final int d_bits,
    final int s_bits)
  {
    Preconditions.checkPreconditionI(
      this.depth_bits,
      this.depth_bits == 0,
      ignored -> "Depth bits must be zero");
    Preconditions.checkPreconditionI(
      this.stencil_bits,
      this.stencil_bits == 0,
      ignored -> "Stencil bits must be zero");

    this.refs.referenceAdd((FakeReferable) a);
    this.depth_bits = d_bits;
    this.stencil_bits = s_bits;
  }
}
