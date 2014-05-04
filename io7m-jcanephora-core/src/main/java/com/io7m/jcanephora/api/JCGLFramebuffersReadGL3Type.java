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

package com.io7m.jcanephora.api;

import java.util.Set;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.FramebufferBlitBuffer;
import com.io7m.jcanephora.FramebufferBlitFilter;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.JCGLExceptionRuntime;

/**
 * Simplified interface to the (read) framebuffer functionality available on
 * OpenGL 3.* implementations.
 */

public interface JCGLFramebuffersReadGL3Type
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
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferBlit(
    final AreaInclusive source,
    final AreaInclusive target,
    final Set<FramebufferBlitBuffer> buffers,
    final FramebufferBlitFilter filter)
    throws JCGLExceptionRuntime;

  /**
   * @return <code>true</code> iff any application-created read framebuffer is
   *         currently bound.
   * 
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  boolean framebufferReadAnyIsBound()
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Bind the given framebuffer <code>framebuffer</code> to the read target.
   * </p>
   * 
   * @param framebuffer
   *          The framebuffer.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferReadBind(
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionRuntime;

  /**
   * @return <code>true</code> iff <code>framebuffer</code> is currently bound
   *         to the read target.
   * 
   * @param framebuffer
   *          The framebuffer.
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  boolean framebufferReadIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionRuntime;

  /**
   * <p>
   * Unbind the current framebuffer from the read target.
   * </p>
   * 
   * @throws JCGLExceptionRuntime
   *           Iff an OpenGL exception occurs.
   */

  void framebufferReadUnbind()
    throws JCGLExceptionRuntime;
}
