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

import com.io7m.jareas.core.AreaInclusiveUnsignedLType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLFramebufferBlitBuffer;
import com.io7m.jcanephora.core.JCGLFramebufferBlitFilter;
import com.io7m.jcanephora.core.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentPointType;
import com.io7m.jcanephora.core.JCGLFramebufferDrawBufferType;
import com.io7m.jcanephora.core.JCGLFramebufferStatus;
import com.io7m.jcanephora.core.JCGLFramebufferType;
import com.io7m.jcanephora.core.JCGLFramebufferUsableType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
   * <p>Calling this method will unbind any currently bound <i>draw</i>
   * framebuffer. The resulting framebuffer, if any, will remain bound when the
   * method returns.</p>
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
   * Delete the given framebuffer. The attachments are <i>not</i> deleted.
   *
   * @param f The framebuffer
   *
   * @throws JCGLException On OpenGL errors
   */

  void framebufferDelete(
    JCGLFramebufferType f)
    throws JCGLException;

  /**
   * @return {@code true} iff any application-created <i>draw</i> framebuffer is
   * currently bound.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  boolean framebufferDrawAnyIsBound()
    throws JCGLException;

  /**
   * Bind the given framebuffer {@code framebuffer} to the <i>draw</i> target.
   *
   * @param framebuffer The framebuffer.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  void framebufferDrawBind(
    JCGLFramebufferUsableType framebuffer)
    throws JCGLException;

  /**
   * @return {@code Some(framebuffer)} iff any application-created <i>draw</i>
   * framebuffer is currently bound.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  Optional<JCGLFramebufferUsableType> framebufferDrawGetBound()
    throws JCGLException;

  /**
   * @param framebuffer The framebuffer.
   *
   * @return {@code true} iff {@code framebuffer} is currently bound to the
   * <i>draw</i> target.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  boolean framebufferDrawIsBound(
    JCGLFramebufferUsableType framebuffer)
    throws JCGLException;

  /**
   * <p>Unbind the current framebuffer from the <i>draw</i> target.</p>
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  void framebufferDrawUnbind()
    throws JCGLException;

  /**
   * <p>Determine the validity of the currently bound <i>draw</i>
   * framebuffer.</p>
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

  /**
   * @return {@code true} iff any application-created <i>read</i> framebuffer is
   * currently bound.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  boolean framebufferReadAnyIsBound()
    throws JCGLException;

  /**
   * Bind the given framebuffer {@code framebuffer} to the <i>read</i> target.
   *
   * @param framebuffer The framebuffer.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  void framebufferReadBind(
    JCGLFramebufferUsableType framebuffer)
    throws JCGLException;

  /**
   * @return {@code Some(framebuffer)} iff any application-created <i>read</i>
   * framebuffer is currently bound.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  Optional<JCGLFramebufferUsableType> framebufferReadGetBound()
    throws JCGLException;

  /**
   * <p>Determine the validity of the currently bound <i>read</i>
   * framebuffer.</p>
   *
   * @return The status of the framebuffer.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  JCGLFramebufferStatus framebufferReadValidate()
    throws JCGLException;

  /**
   * @param framebuffer The framebuffer.
   *
   * @return {@code true} iff {@code framebuffer} is currently bound to the
   * <i>read</i> target.
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  boolean framebufferReadIsBound(
    JCGLFramebufferUsableType framebuffer)
    throws JCGLException;

  /**
   * <p>Unbind the current framebuffer from the <i>read</i> target.</p>
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  void framebufferReadUnbind()
    throws JCGLException;

  /**
   * Copy a region of the current <i>read</i> framebuffer to the current
   * <i>draw</i> framebuffer.
   *
   * @param source  The area of the <i>read</i> framebuffer from which to copy.
   * @param target  The area of the <i>draw</i> framebuffer to which to copy.
   * @param buffers The set of buffers that should be copied.
   * @param filter  The filter used when stretching (if {@code source !=
   *                target}).
   *
   * @throws JCGLException Iff an OpenGL exception occurs.
   */

  void framebufferBlit(
    final AreaInclusiveUnsignedLType source,
    final AreaInclusiveUnsignedLType target,
    final Set<JCGLFramebufferBlitBuffer> buffers,
    final JCGLFramebufferBlitFilter filter)
    throws JCGLException;
}
