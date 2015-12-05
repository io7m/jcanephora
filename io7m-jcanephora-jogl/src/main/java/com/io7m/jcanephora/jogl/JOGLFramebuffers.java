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

package com.io7m.jcanephora.jogl;

import com.io7m.jareas.core.AreaInclusiveUnsignedLType;
import com.io7m.jcanephora.core.JCGLCubeMapFaceLH;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionFeedback;
import com.io7m.jcanephora.core.JCGLExceptionFramebufferInvalid;
import com.io7m.jcanephora.core.JCGLExceptionFramebufferNotBound;
import com.io7m.jcanephora.core.JCGLExceptionFramebufferReadDrawSame;
import com.io7m.jcanephora.core.JCGLExceptionFramebufferWrongBlitFilter;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLFramebufferBlitBuffer;
import com.io7m.jcanephora.core.JCGLFramebufferBlitFilter;
import com.io7m.jcanephora.core.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentMatcherType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentPointType;
import com.io7m.jcanephora.core.JCGLFramebufferColorAttachmentType;
import com.io7m.jcanephora.core.JCGLFramebufferDepthAttachmentMatcherType;
import com.io7m.jcanephora.core.JCGLFramebufferDepthAttachmentType;
import com.io7m.jcanephora.core
  .JCGLFramebufferDepthStencilAttachmentMatcherType;
import com.io7m.jcanephora.core.JCGLFramebufferDepthStencilAttachmentType;
import com.io7m.jcanephora.core.JCGLFramebufferDrawBufferType;
import com.io7m.jcanephora.core.JCGLFramebufferStatus;
import com.io7m.jcanephora.core.JCGLFramebufferType;
import com.io7m.jcanephora.core.JCGLFramebufferUsableType;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jcanephora.core.JCGLReferenceContainerType;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLTexture2DUsableType;
import com.io7m.jcanephora.core.JCGLTextureCubeUsableType;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureFormats;
import com.io7m.jcanephora.core.JCGLTextureUsableType;
import com.io7m.jcanephora.core.api.JCGLFramebuffersType;
import com.io7m.jfunctional.Unit;
import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

final class JOGLFramebuffers implements JCGLFramebuffersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JOGLFramebuffers.class);
  }

  private final GL3                                           gl;
  private final IntBuffer                                     int_cache;
  private final List<JCGLFramebufferColorAttachmentPointType> color_points;
  private final JOGLContext                                   context;
  private final List<JCGLFramebufferDrawBufferType>           draw_buffers;
  private final JOGLTextures                                  textures;
  private       JOGLFramebuffer                               bind_draw;
  private       JOGLFramebuffer                               bind_read;

  JOGLFramebuffers(
    final JOGLContext c,
    final JOGLTextures t)
    throws JCGLExceptionNonCompliant
  {
    this.context = NullCheck.notNull(c);
    this.textures = NullCheck.notNull(t);
    this.gl = c.getGL3();
    this.int_cache = Buffers.newDirectIntBuffer(1);
    this.color_points =
      JOGLFramebuffers.makeColorPoints(c, this.gl, this.int_cache);
    this.draw_buffers =
      JOGLFramebuffers.makeDrawBuffers(c, this.gl, this.int_cache);
    this.textures.setFramebuffers(this);

    /**
     * Configure baseline defaults.
     */

    this.gl.glBindFramebuffer(
      GL.GL_DRAW_FRAMEBUFFER,
      this.gl.getDefaultDrawFramebuffer());
    this.gl.glBindFramebuffer(
      GL.GL_READ_FRAMEBUFFER,
      this.gl.getDefaultReadFramebuffer());
    JOGLErrorChecking.checkErrors(this.gl);
  }

  private static List<JCGLFramebufferDrawBufferType> makeDrawBuffers(
    final JOGLContext c,
    final GL3 gl,
    final IntBuffer in_cache)
    throws JCGLExceptionNonCompliant
  {
    in_cache.rewind();
    gl.glGetIntegerv(GL3.GL_MAX_DRAW_BUFFERS, in_cache);

    final int max = in_cache.get(0);

    JOGLFramebuffers.LOG.debug(
      "implementation supports {} draw buffers",
      Integer.valueOf(max));

    if (max < 8) {
      final String message = String.format(
        "Reported number of draw buffers %d is less than the required %d",
        Integer.valueOf(max), Integer.valueOf(8));
      JOGLFramebuffers.LOG.error(message);
      throw new JCGLExceptionNonCompliant(message);
    }

    final int clamped = Math.min(1024, max);
    if (clamped != max) {
      JOGLFramebuffers.LOG.debug(
        "clamped unreasonable draw buffer count {} to {}",
        Integer.valueOf(max),
        Integer.valueOf(clamped));
    }

    final List<JCGLFramebufferDrawBufferType> u = new ArrayList<>(clamped);
    for (int index = 0; index < clamped; ++index) {
      final JOGLFramebufferDrawBuffer db =
        new JOGLFramebufferDrawBuffer(c.getContext(), index);
      u.add(db);
    }

    return Collections.unmodifiableList(u);
  }

  private static List<JCGLFramebufferColorAttachmentPointType> makeColorPoints(
    final JOGLContext in_c,
    final GL3 in_g,
    final IntBuffer in_cache)
    throws JCGLExceptionNonCompliant
  {
    in_cache.rewind();
    in_g.glGetIntegerv(GL3.GL_MAX_COLOR_ATTACHMENTS, in_cache);

    final int max = in_cache.get(0);

    JOGLFramebuffers.LOG.debug(
      "implementation supports {} color attachment points",
      Integer.valueOf(max));

    if (max < 8) {
      final String message = String.format(
        "Reported number of color attachments %d is less than the required %d",
        Integer.valueOf(max), Integer.valueOf(8));
      JOGLFramebuffers.LOG.error(message);
      throw new JCGLExceptionNonCompliant(message);
    }

    final int clamped = Math.min(1024, max);
    if (clamped != max) {
      JOGLFramebuffers.LOG.debug(
        "clamped unreasonable color attachment count {} to {}",
        Integer.valueOf(max),
        Integer.valueOf(clamped));
    }

    final List<JCGLFramebufferColorAttachmentPointType> u =
      new ArrayList<>(clamped);
    for (int index = 0; index < clamped; ++index) {
      final JOGLFramebufferColorAttachmentPoint cu =
        new JOGLFramebufferColorAttachmentPoint(in_c.getContext(), index);
      u.add(cu);
    }

    return Collections.unmodifiableList(u);

  }

  static void checkColorAttachmentPoint(
    final GLContext c,
    final JCGLFramebufferColorAttachmentPointType point)
  {
    NullCheck.notNull(point);
    JOGLCompatibilityChecks.checkFramebufferColorAttachmentPoint(c, point);
  }

  static void checkDrawBuffer(
    final GLContext c,
    final JCGLFramebufferDrawBufferType buffer)
  {
    NullCheck.notNull(buffer);
    JOGLCompatibilityChecks.checkDrawBuffer(c, buffer);
  }

  static void checkFramebuffer(
    final JOGLContext c,
    final JCGLFramebufferUsableType framebuffer)
  {
    NullCheck.notNull(framebuffer);
    JOGLCompatibilityChecks.checkFramebuffer(c.getContext(), framebuffer);
    JCGLResources.checkNotDeleted(framebuffer);
  }

  static void onFeedbackLoop(
    final JCGLFramebufferUsableType f,
    final JCGLTextureUsableType t)
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("Feedback loop detected: ");
    sb.append("Framebuffer refers to a currently bound texture.");
    sb.append(System.lineSeparator());
    sb.append("Framebuffer: ");
    sb.append(f);
    sb.append(System.lineSeparator());
    sb.append("Texture: ");
    sb.append(t);
    sb.append(System.lineSeparator());
    throw new JCGLExceptionFeedback(sb.toString());
  }

  JOGLFramebuffer getBindDraw()
  {
    return this.bind_draw;
  }

  @Override public JCGLFramebufferBuilderType framebufferNewBuilder()
    throws JCGLException
  {
    return new Builder(this.color_points, this.context);
  }

  @Override
  public JCGLFramebufferType framebufferAllocate(
    final JCGLFramebufferBuilderType b)
    throws JCGLException
  {
    final GL3 g3 = this.gl;
    final GLContext c = g3.getContext();
    JOGLCompatibilityChecks.checkFramebufferBuilder(c, b);

    Assertive.ensure(b instanceof Builder);
    final Builder bb = (Builder) b;

    this.int_cache.rewind();
    g3.glGenFramebuffers(1, this.int_cache);
    final int f_id = this.int_cache.get(0);

    JOGLFramebuffers.LOG.debug("allocate {}", Integer.valueOf(f_id));

    final JOGLFramebuffer fb =
      new JOGLFramebuffer(this.context.getContext(), f_id);
    this.actualBindDraw(fb);

    /**
     * Configure depth/stencil attachments.
     */

    if (bb.depth != null) {
      Assertive.ensure(bb.depth_stencil == null);
      bb.depth.matchDepthAttachment(
        new JCGLFramebufferDepthAttachmentMatcherType<Unit,
          UnreachableCodeException>()
        {
          @Override public Unit onTexture2D(
            final JCGLTexture2DUsableType t)
            throws JCGLException
          {
            JOGLFramebuffers.LOG.debug(
              "[{}] attach depth {}",
              Integer.valueOf(f_id),
              t);

            final JCGLTextureFormat f = t.textureGetFormat();
            JOGLTextures.checkTexture2D(c, t);
            JCGLTextureFormats.checkDepthOnlyRenderableTexture2D(f);
            g3.glFramebufferTexture2D(
              GL3.GL_DRAW_FRAMEBUFFER,
              GL.GL_DEPTH_ATTACHMENT,
              GL.GL_TEXTURE_2D,
              t.getGLName(),
              0);

            fb.setDepthAttachment(t, JCGLTextureFormats.getDepthBits(f));
            return Unit.unit();
          }
        });
    }

    if (bb.depth_stencil != null) {
      Assertive.ensure(bb.depth == null);
      bb.depth_stencil.matchDepthStencilAttachment(
        new JCGLFramebufferDepthStencilAttachmentMatcherType<Unit,
          UnreachableCodeException>()
        {
          @Override public Unit onTexture2D(final JCGLTexture2DUsableType t)
            throws JCGLException
          {
            JOGLFramebuffers.LOG.debug(
              "[{}] attach depth+stencil {}",
              Integer.valueOf(f_id),
              t);

            JOGLTextures.checkTexture2D(c, t);
            final JCGLTextureFormat f = t.textureGetFormat();
            JCGLTextureFormats.checkDepthStencilRenderableTexture2D(f);
            g3.glFramebufferTexture2D(
              GL3.GL_DRAW_FRAMEBUFFER,
              GL3.GL_DEPTH_STENCIL_ATTACHMENT,
              GL.GL_TEXTURE_2D,
              t.getGLName(),
              0);

            fb.setDepthStencilAttachment(
              t,
              JCGLTextureFormats.getDepthBits(f),
              JCGLTextureFormats.getStencilBits(f));
            return Unit.unit();
          }
        });
    }

    /**
     * Configure color attachments.
     */

    for (int index = 0; index < bb.color_attaches.size(); ++index) {
      final int f_index = index;

      final JCGLFramebufferColorAttachmentType a = bb.color_attaches.get(index);
      if (a != null) {
        a.matchColorAttachment(
          new JCGLFramebufferColorAttachmentMatcherType<Unit,
            UnreachableCodeException>()
          {
            @Override public Unit onTexture2D(final JCGLTexture2DUsableType t)
              throws JCGLException
            {
              JOGLFramebuffers.LOG.debug(
                "[{}] attach color {} {}",
                Integer.valueOf(f_id),
                Integer.valueOf(f_index),
                t);

              JOGLTextures.checkTexture2D(c, t);
              final JCGLTextureFormat f = t.textureGetFormat();
              JCGLTextureFormats.checkColorRenderableTexture2D(f);

              g3.glFramebufferTexture2D(
                GL3.GL_DRAW_FRAMEBUFFER,
                GL.GL_COLOR_ATTACHMENT0 + f_index,
                GL.GL_TEXTURE_2D,
                t.getGLName(),
                0);
              return Unit.unit();
            }

            @Override public Unit onTextureCube(
              final JCGLTextureCubeUsableType t,
              final JCGLCubeMapFaceLH face)
              throws JCGLException, UnreachableCodeException
            {
              JOGLFramebuffers.LOG.debug(
                "[{}] attach color {} {} (face {})",
                Integer.valueOf(f_id),
                Integer.valueOf(f_index),
                t,
                face);

              JOGLTextures.checkTextureCube(c, t);
              final JCGLTextureFormat f = t.textureGetFormat();
              JCGLTextureFormats.checkColorRenderableTexture2D(f);

              final int gface = JOGLTypeConversions.cubeFaceToGL(face);
              g3.glFramebufferTexture2D(
                GL3.GL_DRAW_FRAMEBUFFER,
                GL.GL_COLOR_ATTACHMENT0 + f_index,
                gface,
                t.getGLName(),
                0);
              return Unit.unit();
            }
          });
        fb.setColorAttachment(bb.color_points.get(f_index), a);
      }
    }

    /**
     * Configure draw buffer mappings.
     */

    final IntBuffer draw_buffer_mappings =
      Buffers.newDirectIntBuffer(this.draw_buffers.size());

    for (int index = 0; index < this.draw_buffers.size(); ++index) {
      final JCGLFramebufferDrawBufferType buffer = this.draw_buffers.get(index);
      if (bb.draw_buffers.containsKey(buffer)) {
        final JCGLFramebufferColorAttachmentPointType attach =
          bb.draw_buffers.get(buffer);
        JOGLFramebuffers.LOG.debug(
          "[{}] draw buffer {} -> color {}",
          Integer.valueOf(f_id),
          Integer.valueOf(index),
          Integer.valueOf(attach.colorAttachmentPointGetIndex()));
        draw_buffer_mappings.put(
          index,
          GL.GL_COLOR_ATTACHMENT0 + attach.colorAttachmentPointGetIndex());
      } else {
        JOGLFramebuffers.LOG.debug(
          "[{}] draw buffer {} -> none",
          Integer.valueOf(f_id),
          Integer.valueOf(index));
        draw_buffer_mappings.put(index, GL.GL_NONE);
      }
    }

    draw_buffer_mappings.rewind();
    g3.glDrawBuffers(this.draw_buffers.size(), draw_buffer_mappings);

    /**
     * Validate.
     */

    final JCGLFramebufferStatus status = this.framebufferDrawValidate();
    switch (status) {
      case FRAMEBUFFER_STATUS_COMPLETE: {
        return fb;
      }
      case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_ATTACHMENT:
      case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_DRAW_BUFFER:
      case FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_READ_BUFFER:
      case FRAMEBUFFER_STATUS_ERROR_MISSING_IMAGE_ATTACHMENT:
      case FRAMEBUFFER_STATUS_ERROR_UNKNOWN:
      case FRAMEBUFFER_STATUS_ERROR_UNSUPPORTED: {
        throw new JCGLExceptionFramebufferInvalid(status);
      }
    }

    throw new UnreachableCodeException();
  }

  private void actualBindDraw(final JOGLFramebuffer f)
  {
    JOGLFramebuffers.LOG.trace("bind draw {} -> {}", this.bind_draw, f);

    for (final JCGLReferableType r : f.getReferences()) {
      if (r instanceof JCGLTexture2DUsableType) {
        final JCGLTexture2DUsableType t = (JCGLTexture2DUsableType) r;
        if (this.textures.texture2DIsBoundAnywhere(t)) {
          JOGLFramebuffers.onFeedbackLoop(f, t);
        }
      }
    }

    this.gl.glBindFramebuffer(GL.GL_DRAW_FRAMEBUFFER, f.getGLName());
    this.bind_draw = f;
  }

  private void actualBindRead(final JOGLFramebuffer f)
  {
    JOGLFramebuffers.LOG.trace("bind read {} -> {}", this.bind_read, f);

    this.gl.glBindFramebuffer(GL.GL_READ_FRAMEBUFFER, f.getGLName());
    this.bind_read = f;
  }

  private void actualUnbindDraw()
  {
    JOGLFramebuffers.LOG.trace("unbind {} -> none", this.bind_draw);
    this.gl.glBindFramebuffer(
      GL.GL_DRAW_FRAMEBUFFER,
      this.gl.getDefaultDrawFramebuffer());
    this.bind_draw = null;
  }

  private void actualUnbindRead()
  {
    JOGLFramebuffers.LOG.trace("unbind {} -> none", this.bind_read);
    this.gl.glBindFramebuffer(
      GL.GL_READ_FRAMEBUFFER,
      this.gl.getDefaultReadFramebuffer());
    this.bind_read = null;
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws JCGLException
  {
    return this.bind_draw != null;
  }

  @Override
  public void framebufferDrawBind(
    final JCGLFramebufferUsableType framebuffer)
    throws JCGLException
  {
    JOGLFramebuffers.checkFramebuffer(this.context, framebuffer);
    this.actualBindDraw((JOGLFramebuffer) framebuffer);
  }

  @Override public Optional<JCGLFramebufferUsableType> framebufferDrawGetBound()
    throws JCGLException
  {
    return Optional.ofNullable(this.bind_draw);
  }

  @Override
  public boolean framebufferDrawIsBound(
    final JCGLFramebufferUsableType framebuffer)
    throws JCGLException
  {
    return framebuffer.equals(this.bind_draw);
  }

  @Override public void framebufferDrawUnbind()
    throws JCGLException
  {
    this.actualUnbindDraw();
  }

  @Override
  public JCGLFramebufferStatus framebufferDrawValidate()
    throws JCGLException
  {
    if (this.bind_draw == null) {
      throw new JCGLExceptionFramebufferNotBound(
        "No draw framebuffer is bound");
    }

    return JOGLTypeConversions.framebufferStatusFromGL(
      this.gl.glCheckFramebufferStatus(GL3.GL_DRAW_FRAMEBUFFER));
  }

  @Override
  public List<JCGLFramebufferDrawBufferType> framebufferGetDrawBuffers()
    throws JCGLException
  {
    return this.draw_buffers;
  }

  @Override
  public List<JCGLFramebufferColorAttachmentPointType>
  framebufferGetColorAttachments()
    throws JCGLException
  {
    return this.color_points;
  }

  @Override public boolean framebufferReadAnyIsBound()
    throws JCGLException
  {
    return this.bind_read != null;
  }

  @Override
  public void framebufferReadBind(final JCGLFramebufferUsableType framebuffer)
    throws JCGLException
  {
    JOGLFramebuffers.checkFramebuffer(this.context, framebuffer);
    this.actualBindRead((JOGLFramebuffer) framebuffer);
  }

  @Override public Optional<JCGLFramebufferUsableType> framebufferReadGetBound()
    throws JCGLException
  {
    return Optional.ofNullable(this.bind_read);
  }

  @Override public JCGLFramebufferStatus framebufferReadValidate()
    throws JCGLException
  {
    if (this.bind_read == null) {
      throw new JCGLExceptionFramebufferNotBound(
        "No read framebuffer is bound");
    }

    return JOGLTypeConversions.framebufferStatusFromGL(
      this.gl.glCheckFramebufferStatus(GL3.GL_READ_FRAMEBUFFER));
  }

  @Override
  public boolean framebufferReadIsBound(
    final JCGLFramebufferUsableType framebuffer)
    throws JCGLException
  {
    return framebuffer.equals(this.bind_read);
  }

  @Override public void framebufferReadUnbind()
    throws JCGLException
  {
    this.actualUnbindRead();
  }

  @Override public void framebufferBlit(
    final AreaInclusiveUnsignedLType source,
    final AreaInclusiveUnsignedLType target,
    final Set<JCGLFramebufferBlitBuffer> buffers,
    final JCGLFramebufferBlitFilter filter)
    throws JCGLException
  {
    NullCheck.notNull(source);
    NullCheck.notNull(target);
    NullCheck.notNull(buffers);
    NullCheck.notNull(filter);

    if (!this.framebufferReadAnyIsBound()) {
      throw new JCGLExceptionFramebufferNotBound(
        "No read framebuffer is bound");
    }
    if (!this.framebufferDrawAnyIsBound()) {
      throw new JCGLExceptionFramebufferNotBound(
        "No draw framebuffer is bound");
    }
    if (Objects.equals(this.bind_draw, this.bind_read)) {
      throw new JCGLExceptionFramebufferReadDrawSame(
        "Attempted to blit a framebuffer to itself.");
    }

    final boolean want_depth =
      buffers.contains(
        JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_DEPTH);
    final boolean want_stenc =
      buffers.contains(
        JCGLFramebufferBlitBuffer.FRAMEBUFFER_BLIT_BUFFER_STENCIL);
    if (want_depth || want_stenc) {
      if (filter != JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_NEAREST) {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("Blit specifies incorrect filter for depth/stencil buffers.");
        sb.append(System.lineSeparator());
        sb.append("Required: ");
        sb.append(JCGLFramebufferBlitFilter.FRAMEBUFFER_BLIT_FILTER_NEAREST);
        sb.append(System.lineSeparator());
        sb.append("Actual: ");
        sb.append(filter);
        sb.append(System.lineSeparator());
        throw new JCGLExceptionFramebufferWrongBlitFilter(sb.toString());
      }
    }

    final UnsignedRangeInclusiveL s_range_x = source.getRangeX();
    final UnsignedRangeInclusiveL s_range_y = source.getRangeY();
    final UnsignedRangeInclusiveL d_range_x = target.getRangeX();
    final UnsignedRangeInclusiveL d_range_y = target.getRangeY();

    /**
     * Section 4.3.2 of the OpenGL 3.1 standard: "The lower bounds of the
     * rectangle are inclusive, while the upper bounds are exclusive".
     */

    final int src_x0 = (int) s_range_x.getLower();
    final int src_y0 = (int) s_range_y.getLower();
    final int src_x1 = (int) s_range_x.getUpper() + 1;
    final int src_y1 = (int) s_range_y.getUpper() + 1;

    final int dst_x0 = (int) d_range_x.getLower();
    final int dst_y0 = (int) d_range_y.getLower();
    final int dst_x1 = (int) d_range_x.getUpper() + 1;
    final int dst_y1 = (int) d_range_y.getUpper() + 1;

    final int mask =
      JOGLTypeConversions.framebufferBlitBufferSetToMask(buffers);
    final int filteri = JOGLTypeConversions.framebufferBlitFilterToGL(filter);

    this.gl.glBlitFramebuffer(
      src_x0,
      src_y0,
      src_x1,
      src_y1,
      dst_x0,
      dst_y0,
      dst_x1,
      dst_y1,
      mask,
      filteri);
  }

  private static final class Builder extends JOGLObjectPseudoUnshared
    implements JCGLFramebufferBuilderType
  {
    private final List<JCGLFramebufferColorAttachmentPointType> color_points;
    private final JOGLContext                                   context;
    private final List<JCGLFramebufferColorAttachmentType>      color_attaches;
    private final SortedMap<JCGLFramebufferDrawBufferType,
      JCGLFramebufferColorAttachmentPointType>                  draw_buffers;
    private       JCGLFramebufferDepthAttachmentType            depth;
    private       JCGLFramebufferDepthStencilAttachmentType     depth_stencil;

    Builder(
      final List<JCGLFramebufferColorAttachmentPointType> in_color_points,
      final JOGLContext in_context)
    {
      super(in_context.getContext());

      this.color_points = NullCheck.notNull(in_color_points);
      this.context = NullCheck.notNull(in_context);
      this.color_attaches = new ArrayList<>(this.color_points.size());
      for (int index = 0; index < this.color_points.size(); ++index) {
        this.color_attaches.add(null);
      }
      this.draw_buffers = new TreeMap<>();
    }

    @Override public void attachColorTexture2DAt(
      final JCGLFramebufferColorAttachmentPointType point,
      final JCGLFramebufferDrawBufferType buffer,
      final JCGLTexture2DUsableType texture)
    {
      NullCheck.notNull(point);
      final GLContext c = this.context.getContext();
      JOGLFramebuffers.checkColorAttachmentPoint(c, point);
      JOGLFramebuffers.checkDrawBuffer(c, buffer);
      JOGLTextures.checkTexture2D(c, texture);
      JCGLTextureFormats.checkColorRenderableTexture2D(
        texture.textureGetFormat());

      this.color_attaches.set(point.colorAttachmentPointGetIndex(), texture);
      this.draw_buffers.put(buffer, point);
    }

    @Override public void attachColorTextureCubeAt(
      final JCGLFramebufferColorAttachmentPointType point,
      final JCGLFramebufferDrawBufferType buffer,
      final JCGLTextureCubeUsableType texture,
      final JCGLCubeMapFaceLH face)
    {
      NullCheck.notNull(point);
      NullCheck.notNull(face);
      final GLContext c = this.context.getContext();
      JOGLFramebuffers.checkColorAttachmentPoint(c, point);
      JOGLFramebuffers.checkDrawBuffer(c, buffer);
      JOGLTextures.checkTextureCube(c, texture);
      JCGLTextureFormats.checkColorRenderableTexture2D(
        texture.textureGetFormat());

      this.color_attaches.set(
        point.colorAttachmentPointGetIndex(),
        new CubeAttachment(texture, face));
      this.draw_buffers.put(buffer, point);
    }

    @Override public void attachDepthTexture2D(
      final JCGLTexture2DUsableType t)
    {
      final GLContext c = this.context.getContext();
      JOGLTextures.checkTexture2D(c, t);
      JCGLTextureFormats.checkDepthOnlyRenderableTexture2D(
        t.textureGetFormat());
      this.depth = t;
      this.depth_stencil = null;
    }

    @Override
    public void attachDepthStencilTexture2D(final JCGLTexture2DUsableType t)
    {
      final GLContext c = this.context.getContext();
      JOGLTextures.checkTexture2D(c, t);
      JCGLTextureFormats.checkDepthStencilRenderableTexture2D(
        t.textureGetFormat());
      this.depth = null;
      this.depth_stencil = t;
    }

    @Override public void detachDepth()
    {
      this.depth = null;
      this.depth_stencil = null;
    }

    @Override
    public void detachColorAttachment(
      final JCGLFramebufferColorAttachmentPointType point)
    {
      NullCheck.notNull(point);
      final GLContext c = this.context.getContext();
      JOGLFramebuffers.checkColorAttachmentPoint(c, point);

      final int index = point.colorAttachmentPointGetIndex();
      this.color_attaches.set(index, null);
      this.draw_buffers.values().remove(point);
    }

    private static final class CubeAttachment
      implements JCGLFramebufferColorAttachmentType
    {
      private final JCGLTextureCubeUsableType texture;
      private final JCGLCubeMapFaceLH         face;

      CubeAttachment(
        final JCGLTextureCubeUsableType in_texture,
        final JCGLCubeMapFaceLH in_face)
      {
        this.texture = in_texture;
        this.face = in_face;
      }

      @Override
      public <A, E extends Throwable> A matchColorAttachment(
        final JCGLFramebufferColorAttachmentMatcherType<A, E> m)
        throws JCGLException, E
      {
        return m.onTextureCube(this.texture, this.face);
      }

      @Override public Set<JCGLReferenceContainerType> getReferringContainers()
      {
        throw new UnreachableCodeException();
      }

      @Override public int getGLName()
      {
        return this.texture.getGLName();
      }
    }
  }
}
