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

package com.io7m.jcanephora.jogl;

import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES3;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.FramebufferBlitBuffer;
import com.io7m.jcanephora.FramebufferBlitFilter;
import com.io7m.jcanephora.FramebufferColorAttachmentPoint;
import com.io7m.jcanephora.FramebufferDrawBuffer;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLStateCache;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;
import com.jogamp.common.nio.Buffers;

final class JOGL_GL2ES3_Functions
{
  static void framebufferBlit(
    final @Nonnull GL2ES3 gl,
    final @Nonnull AreaInclusive source,
    final @Nonnull AreaInclusive target,
    final @Nonnull Set<FramebufferBlitBuffer> buffers,
    final @Nonnull FramebufferBlitFilter filter)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    Constraints.constrainNotNull(source, "Source area");
    Constraints.constrainNotNull(target, "Target area");
    Constraints.constrainNotNull(buffers, "Buffers");
    Constraints.constrainNotNull(filter, "Filter");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawAnyIsBound(gl),
      "Draw buffer is bound");
    Constraints.constrainArbitrary(
      JOGL_GL2ES3_Functions.framebufferReadAnyIsBound(gl),
      "Read buffer is bound");

    if (buffers.contains(FramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH)
      || buffers
        .contains(FramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_STENCIL)) {
      Constraints.constrainArbitrary(
        filter == FramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_NEAREST,
        "Filter is NEAREST for depth/stencil blit");
    }

    final RangeInclusive s_range_x = source.getRangeX();
    final RangeInclusive s_range_y = source.getRangeY();
    final RangeInclusive d_range_x = target.getRangeX();
    final RangeInclusive d_range_y = target.getRangeY();

    final int srcX0 = (int) s_range_x.getLower();
    final int srcY0 = (int) s_range_y.getLower();
    final int srcX1 = (int) s_range_x.getUpper();
    final int srcY1 = (int) s_range_y.getUpper();

    final int dstX0 = (int) d_range_x.getLower();
    final int dstY0 = (int) d_range_y.getLower();
    final int dstX1 = (int) d_range_x.getUpper();
    final int dstY1 = (int) d_range_y.getUpper();

    final int mask =
      JOGL_GLTypeConversions.framebufferBlitBufferSetToMask(buffers);
    final int filteri =
      JOGL_GLTypeConversions.framebufferBlitFilterToGL(filter);

    gl.glBlitFramebuffer(
      srcX0,
      srcY0,
      srcX1,
      srcY1,
      dstX0,
      dstY0,
      dstX1,
      dstY1,
      mask,
      filteri);
    JOGL_GL_Functions.checkError(gl);
  }

  static
    void
    framebufferDrawSetBuffers(
      final @Nonnull GL2ES3 gl,
      final @Nonnull JCGLStateCache state,
      final @Nonnull Log log,
      final @Nonnull FramebufferReference framebuffer,
      final @Nonnull Map<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> mappings)
      throws ConstraintError,
        JCGLExceptionRuntime
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");
    Constraints.constrainNotNull(mappings, "Draw buffer attachment mappings");

    final List<FramebufferDrawBuffer> buffers = state.draw_buffers;
    final IntBuffer out = Buffers.newDirectIntBuffer(buffers.size());

    for (int index = 0; index < buffers.size(); ++index) {
      final FramebufferDrawBuffer buffer = new FramebufferDrawBuffer(index);
      if (mappings.containsKey(buffer)) {
        final FramebufferColorAttachmentPoint attach = mappings.get(buffer);
        out.put(index, GL.GL_COLOR_ATTACHMENT0 + attach.getIndex());
        if (log.enabled(Level.LOG_DEBUG)) {
          state.log_text.setLength(0);
          state.log_text.append("draw-buffers: map ");
          state.log_text.append(buffer);
          state.log_text.append(" to ");
          state.log_text.append(attach);
          log.debug(state.log_text.toString());
        }
      } else {
        out.put(index, GL.GL_NONE);
        if (log.enabled(Level.LOG_DEBUG)) {
          state.log_text.setLength(0);
          state.log_text.append("draw-buffers: map ");
          state.log_text.append(buffer);
          state.log_text.append(" to none");
          log.debug(state.log_text.toString());
        }
      }
    }

    out.rewind();

    gl.glDrawBuffers(buffers.size(), out);
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean framebufferReadAnyIsBound(
    final @Nonnull GL2ES3 gl)
  {
    final int bound = gl.getBoundFramebuffer(GL2ES3.GL_READ_FRAMEBUFFER);
    final int default_fb = gl.getDefaultReadFramebuffer();
    return bound != default_fb;
  }

  static void framebufferReadBind(
    final @Nonnull GL2ES3 gl,
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    gl.glBindFramebuffer(GL2ES3.GL_READ_FRAMEBUFFER, framebuffer.getGLName());
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean framebufferReadIsBound(
    final @Nonnull GL2ES3 gl,
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    final int bound = gl.getBoundFramebuffer(GL2ES3.GL_READ_FRAMEBUFFER);
    return bound == framebuffer.getGLName();
  }

  static void framebufferReadUnbind(
    final @Nonnull GL2ES3 gl)
    throws JCGLExceptionRuntime
  {
    gl.glBindFramebuffer(GL2ES3.GL_READ_FRAMEBUFFER, 0);
    JOGL_GL_Functions.checkError(gl);
  }
}
