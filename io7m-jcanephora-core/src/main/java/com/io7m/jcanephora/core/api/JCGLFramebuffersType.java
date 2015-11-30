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

package com.io7m.jcanephora.core.api;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentPointType;
import com.io7m.jcanephora.core.JCGLFramebufferDrawBufferType;
import com.io7m.jcanephora.core.JCGLFramebufferStatus;
import com.io7m.jcanephora.core.JCGLFramebufferType;
import com.io7m.jcanephora.core.JCGLFramebufferUsableType;

import java.util.List;
import java.util.Optional;

/**
 * The interface to OpenGL framebuffer objects.
 */

public interface JCGLFramebuffersType
{
  /**
   * Retrieve a new framebuffer object builder. The returned builder can be used
   * indefinitely but is tied to the {@link JCGLContextType} upon which it was
   * created.
   *
   * @return A new framebuffer object builder
   *
   * @throws JCGLException On OpenGL errors
   */

  JCGLFramebufferBuilderType framebufferNewBuilder()
    throws JCGLException;

  /**
   * <p>Allocate and bind a framebuffer object based on the values given in
   * {@code b}.</p>
   *
   * <p>Calling this method will unbind any currently bound framebuffer.</p>
   *
   * @param b The framebuffer object builder
   *
   * @return A new framebuffer object
   *
   * @throws JCGLException On OpenGL errors
   */

  JCGLFramebufferType framebufferAllocate(
    JCGLFramebufferBuilderType b)
    throws JCGLException;

  /**
   * @return {@code true} iff any application-created draw framebuffer is
   * currently bound.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  boolean framebufferDrawAnyIsBound()
    throws JCGLException;

  /**
   * <p> Bind the given framebuffer {@code framebuffer} to the draw target.
   * </p>
   *
   * @param framebuffer The framebuffer.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  void framebufferDrawBind(
    JCGLFramebufferUsableType framebuffer)
    throws JCGLException;

  /**
   * @return {@code Some(framebuffer)} iff any application-created draw
   * framebuffer is currently bound.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  Optional<JCGLFramebufferUsableType> framebufferDrawGetBound()
    throws JCGLException;

  /**
   * @param framebuffer The framebuffer.
   *
   * @return {@code true} iff {@code framebuffer} is currently bound to the draw
   * target.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  boolean framebufferDrawIsBound(
    JCGLFramebufferUsableType framebuffer)
    throws JCGLException;

  /**
   * <p> Unbind the current framebuffer from the draw target. </p>
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  void framebufferDrawUnbind()
    throws JCGLException;

  /**
   * <p>Determine the validity of the currently bound draw framebuffer</p>
   *
   * @return The status of the framebuffer.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  JCGLFramebufferStatus framebufferDrawValidate()
    throws JCGLException;

  /**
   * @return The available set of draw buffers.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  List<JCGLFramebufferDrawBufferType> framebufferGetDrawBuffers()
    throws JCGLException;

  /**
   * @return The available set of color attachment points.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  List<JCGLFramebufferColorAttachmentPointType> framebufferGetColorAttachments()
    throws JCGLException;
}
