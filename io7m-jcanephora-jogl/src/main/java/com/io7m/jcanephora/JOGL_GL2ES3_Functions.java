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

import java.nio.IntBuffer;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES3;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;
import com.jogamp.common.nio.Buffers;

final class JOGL_GL2ES3_Functions
{
  static
    void
    framebufferDrawSetBuffers(
      final @Nonnull GL2ES3 gl,
      final @Nonnull JCGLStateCache state,
      final @Nonnull Log log,
      final @Nonnull FramebufferReference framebuffer,
      final @Nonnull Map<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> mappings)
      throws ConstraintError,
        JCGLException
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

    final FramebufferDrawBuffer[] buffers = state.draw_buffers;
    final IntBuffer out = Buffers.newDirectIntBuffer(buffers.length);

    for (int index = 0; index < buffers.length; ++index) {
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

    gl.glDrawBuffers(buffers.length, out);
    JOGL_GL_Functions.checkError(gl);
  }
}
