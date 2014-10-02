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

import java.util.List;

import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jfunctional.OptionType;

/**
 * Simplified interface to the subset of framebuffer functionality available
 * on all OpenGL implementations.
 */

public interface JCGLFramebuffersCommonType
{
  /**
   * <p>
   * Allocate a new framebuffer based on the framebuffer parameters given in
   * the builder.
   * </p>
   *
   * @param b
   *          The builder
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   * @return A freshly allocated framebuffer.
   */

  FramebufferType framebufferAllocate(
    JCGLFramebufferBuilderType b)
    throws JCGLException;

  /**
   * <p>
   * Delete the framebuffer <code>framebuffer</code>.
   * </p>
   *
   * @param framebuffer
   *          The framebuffer.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDelete(
    final FramebufferType framebuffer)
    throws JCGLException;

  /**
   * @return <code>true</code> iff any application-created draw framebuffer is
   *         currently bound.
   *
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  boolean framebufferDrawAnyIsBound()
    throws JCGLException;

  /**
   * <p>
   * Bind the given framebuffer <code>framebuffer</code> to the draw target.
   * </p>
   *
   * @param framebuffer
   *          The framebuffer.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawBind(
    final FramebufferUsableType framebuffer)
    throws JCGLException;

  /**
   * @return <code>Some(framebuffer)</code> iff any application-created draw
   *         framebuffer is currently bound.
   *
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  OptionType<FramebufferUsableType> framebufferDrawGetBound()
    throws JCGLException;

  /**
   * @return <code>true</code> iff <code>framebuffer</code> is currently bound
   *         to the draw target.
   *
   * @param framebuffer
   *          The framebuffer.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  boolean framebufferDrawIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLException;

  /**
   * <p>
   * Unbind the current framebuffer from the draw target.
   * </p>
   *
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  void framebufferDrawUnbind()
    throws JCGLException;

  /**
   * <p>
   * Determine the validity of the framebuffer <code>framebuffer</code>.
   * </p>
   *
   * @return The status of the framebuffer.
   * @param framebuffer
   *          The framebuffer.
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  FramebufferStatus framebufferDrawValidate(
    final FramebufferUsableType framebuffer)
    throws JCGLException;

  /**
   * <p>
   * On ES2 implementations, the returned list will be a single item (as ES2
   * only allows single color attachments for framebuffers).
   * </p>
   *
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   * @return The available set of color attachment points for framebuffers.
   */

    List<FramebufferColorAttachmentPointType>
    framebufferGetColorAttachmentPoints()
      throws JCGLException;

  /**
   * <p>
   * On ES2 implementations, the returned list will be a single item (as ES2
   * only allows a single color draw buffer).
   * </p>
   *
   * @return The available set of draw buffers framebuffers.
   *
   *
   * @throws JCGLException
   *           Iff an OpenGL exception occurs.
   */

  List<FramebufferDrawBufferType> framebufferGetDrawBuffers()
    throws JCGLException;

  /**
   * @return A new mutable framebuffer builder for OpenGL framebuffers.
   */

  JCGLFramebufferBuilderType framebufferNewBuilder();
}
