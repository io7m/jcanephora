/*
 * Copyright © 2012 http://io7m.com
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
 * Simplified interface to framebuffers.
 */

public interface GLFramebuffers
{
  /**
   * Allocate a framebuffer and attach a set of buffers to it. Each element of
   * <code>attachments</code> specifies the type of buffer (color, depth,
   * etc), and possibly the attachment location (for color buffers).
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>attachments == null</code></li>
   *           <li><code>attachments.length == 0</code></li>
   *           <li><code>∃n. attachments[n] == null</code></li>
   *           <li>Multiple depth buffers are specified.</li>
   *           <li>The number of specified color buffers is larger than an
   *           implementation-defined maximum.</li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL exception occurs.
   * @return A freshly allocated and initialized framebuffer.
   */

  @Nonnull Framebuffer framebufferAllocate(
    final @Nonnull FramebufferAttachment[] attachments)
    throws ConstraintError,
      GLException;

  /**
   * Bind the given framebuffer as the current framebuffer.
   * 
   * @param buffer
   *          The framebuffer.
   * @throws ConstraintError
   *           Iff <code>buffer == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void framebufferBind(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException;

  /**
   * Delete the framebuffer specified by <code>buffer</code>.
   * 
   * @param buffer
   *          The buffer.
   * @throws ConstraintError
   *           Iff <code>buffer == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void framebufferDelete(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException;

  /**
   * Unbind the current framebuffer (if any).
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void framebufferUnbind()
    throws GLException;
}
