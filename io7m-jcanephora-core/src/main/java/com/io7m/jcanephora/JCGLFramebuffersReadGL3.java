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

package com.io7m.jcanephora;

import java.util.Set;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to the (read) framebuffer functionality available on
 * OpenGL 3.* implementations.
 */

public interface JCGLFramebuffersReadGL3
{
  /**
   * Copy a region of the current read framebuffer to the current draw
   * framebuffer.
   * 
   * @param source
   *          The area of the read framebuffer from which to copy.
   * @param target
   *          The area of the draw framebuffer to which to copy.
   * @param buffers
   *          The set of buffers that should be copied.
   * @param filter
   *          The filter used when stretching (if
   *          <code>source != target</code>).
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li>Any parameter is <code>null</code></li>
   *           <li>Either of the read or draw framebuffers is unbound</li>
   *           <li><code>buffers</code> contains
   *           {@link FramebufferBlitBuffer#FRAMEBUFFER_BLIT_BUFFER_DEPTH} or
   *           {@link FramebufferBlitBuffer#FRAMEBUFFER_BLIT_BUFFER_STENCIL}
   *           and
   *           <code>filter != {@link FramebufferBlitFilter#FRAMEBUFFER_BLIT_FILTER_NEAREST}</code>
   *           </li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferBlit(
    final @Nonnull AreaInclusive source,
    final @Nonnull AreaInclusive target,
    final @Nonnull Set<FramebufferBlitBuffer> buffers,
    final @Nonnull FramebufferBlitFilter filter)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * <p>
   * Return <code>true</code> iff any application-created read framebuffer is
   * currently bound.
   * </p>
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

  boolean framebufferReadAnyIsBound()
    throws JCGLRuntimeException;

  /**
   * <p>
   * Bind the given framebuffer <code>framebuffer</code> to the read target.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferReadBind(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLRuntimeException,
      ConstraintError;

  /**
   * <p>
   * Return <code>true</code> iff <code>framebuffer</code> is currently bound
   * to the read target.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>framebuffer == null</code></li>
   *           <li><code>framebuffer.resourceIsDeleted()</code></li>
   *           </ul>
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

  boolean framebufferReadIsBound(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLRuntimeException,
      ConstraintError;

  /**
   * <p>
   * Unbind the current framebuffer from the read target.
   * </p>
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferReadUnbind()
    throws JCGLRuntimeException;
}
