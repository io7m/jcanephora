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

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * A "readable" interface to the {@link Framebuffer} type that allows use of
 * the type but not mutation and/or deletion of the contents.
 */

public interface FramebufferUsable extends GLResourceUsable
{
  /**
   * Return the color attachment at attachment point <code>point</code>.
   * 
   * @throws ConstraintError
   *           Iff
   *           <code>point == null || hasColorAttachment(point) == false</code>
   *           .
   */

  public @Nonnull AttachmentColor getColorAttachment(
    final @Nonnull FramebufferColorAttachmentPoint point)
    throws ConstraintError;

  /**
   * Return the depth attachment.
   * 
   * @throws ConstraintError
   *           Iff <code>hasDepthAttachment() == false</code>.
   */

  public @Nonnull AttachmentDepth getDepthAttachment()
    throws ConstraintError;

  /**
   * Retrieve the height of the framebuffer in pixels.
   */

  public int getHeight();

  /**
   * Return the stencil attachment.
   * 
   * @throws ConstraintError
   *           Iff <code>hasStencilAttachment() == false</code>.
   */

  public @Nonnull AttachmentStencil getStencilAttachment()
    throws ConstraintError;

  /**
   * Retrieve the width of the framebuffer in pixels.
   */

  public int getWidth();

  /**
   * Return <code>true</code> iff the framebuffer has a color attachment at
   * point <code>point</code>.
   * 
   * @throws ConstraintError
   *           Iff <code>point == null</code>.
   */

  public boolean hasColorAttachment(
    final @Nonnull FramebufferColorAttachmentPoint point)
    throws ConstraintError;

  /**
   * Return <code>true</code> iff the framebuffer has a depth attachment.
   */

  public boolean hasDepthAttachment();

  /**
   * Return <code>true</code> iff the framebuffer has a stencil attachment.
   */

  public boolean hasStencilAttachment();
}
