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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jaffirm.core.Preconditions;
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
import com.io7m.jcanephora.core.JCGLFramebufferDepthStencilAttachmentMatcherType;
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
import com.io7m.jregions.core.unparameterized.areas.AreaL;
import com.io7m.junreachable.UnreachableCodeException;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

final class LWJGL3Framebuffers implements JCGLFramebuffersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(LWJGL3Framebuffers.class);
  }

  private final List<JCGLFramebufferColorAttachmentPointType> color_points;
  private final LWJGL3Context context;
  private final List<JCGLFramebufferDrawBufferType> draw_buffers;
  private final LWJGL3Textures textures;
  private LWJGL3Framebuffer bind_draw;
  private LWJGL3Framebuffer bind_read;

  LWJGL3Framebuffers(
    final LWJGL3Context c,
    final LWJGL3Textures t)
    throws JCGLExceptionNonCompliant
  {
    this.context = NullCheck.notNull(c, "Context");
    this.textures = NullCheck.notNull(t, "Textures");

    this.color_points = makeColorPoints(c);
    this.draw_buffers = makeDrawBuffers(c);
    this.textures.setFramebuffers(this);

    /*
     * Configure baseline defaults.
     */

    GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
    GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, 0);
    LWJGL3ErrorChecking.checkErrors();
  }

  private static List<JCGLFramebufferDrawBufferType> makeDrawBuffers(
    final LWJGL3Context c)
    throws JCGLExceptionNonCompliant
  {
    final int max = GL11.glGetInteger(GL20.GL_MAX_DRAW_BUFFERS);
    LOG.debug(
      "implementation supports {} draw buffers",
      Integer.valueOf(max));

    if (max < 8) {
      final String message = String.format(
        "Reported number of draw buffers %d is less than the required %d",
        Integer.valueOf(max), Integer.valueOf(8));
      LOG.error(message);
      throw new JCGLExceptionNonCompliant(message);
    }

    final int clamped = Math.min(1024, max);
    if (clamped != max) {
      LOG.debug(
        "clamped unreasonable draw buffer count {} to {}",
        Integer.valueOf(max),
        Integer.valueOf(clamped));
    }

    final List<JCGLFramebufferDrawBufferType> u = new ArrayList<>(clamped);
    for (int index = 0; index < clamped; ++index) {
      final LWJGL3FramebufferDrawBuffer db =
        new LWJGL3FramebufferDrawBuffer(c, index);
      u.add(db);
    }

    return Collections.unmodifiableList(u);
  }

  private static List<JCGLFramebufferColorAttachmentPointType> makeColorPoints(
    final LWJGL3Context in_c)
    throws JCGLExceptionNonCompliant
  {
    final int max = GL11.glGetInteger(GL30.GL_MAX_COLOR_ATTACHMENTS);
    LOG.debug(
      "implementation supports {} color attachment points",
      Integer.valueOf(max));

    if (max < 8) {
      final String message = String.format(
        "Reported number of color attachments %d is less than the required %d",
        Integer.valueOf(max), Integer.valueOf(8));
      LOG.error(message);
      throw new JCGLExceptionNonCompliant(message);
    }

    final int clamped = Math.min(1024, max);
    if (clamped != max) {
      LOG.debug(
        "clamped unreasonable color attachment count {} to {}",
        Integer.valueOf(max),
        Integer.valueOf(clamped));
    }

    final List<JCGLFramebufferColorAttachmentPointType> u =
      new ArrayList<>(clamped);
    for (int index = 0; index < clamped; ++index) {
      final LWJGL3FramebufferColorAttachmentPoint cu =
        new LWJGL3FramebufferColorAttachmentPoint(in_c, index);
      u.add(cu);
    }

    return Collections.unmodifiableList(u);

  }

  static void checkColorAttachmentPoint(
    final LWJGL3Context c,
    final JCGLFramebufferColorAttachmentPointType point)
  {
    NullCheck.notNull(point, "Point");
    LWJGL3FramebufferColorAttachmentPoint.checkFramebufferColorAttachmentPoint(
      c,
      point);
  }

  static void checkDrawBuffer(
    final LWJGL3Context c,
    final JCGLFramebufferDrawBufferType buffer)
  {
    NullCheck.notNull(buffer, "Buffer");
    LWJGL3FramebufferDrawBuffer.checkDrawBuffer(c, buffer);
  }

  static LWJGL3Framebuffer checkFramebuffer(
    final LWJGL3Context c,
    final JCGLFramebufferUsableType framebuffer)
  {
    NullCheck.notNull(framebuffer, "Framebuffer");
    LWJGL3Framebuffer.checkFramebuffer(c, framebuffer);
    JCGLResources.checkNotDeleted(framebuffer);
    return (LWJGL3Framebuffer) framebuffer;
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

  private static Builder checkFramebufferBuilder(
    final LWJGL3Context context,
    final JCGLFramebufferBuilderType b)
  {
    NullCheck.notNull(context, "Context");
    NullCheck.notNull(b, "Builder");
    return (Builder) LWJGL3CompatibilityChecks.checkAny(context, b);
  }

  private static void framebufferAllocateConfigureColorAttachments(
    final LWJGL3Context c,
    final Builder bb,
    final int f_id,
    final LWJGL3Framebuffer fb,
    final int index)
  {
    final JCGLFramebufferColorAttachmentType a = bb.color_attaches.get(index);
    if (a != null) {
      a.matchColorAttachment(
        new JCGLFramebufferColorAttachmentMatcherType<Unit,
          UnreachableCodeException>()
        {
          @Override
          public Unit onTexture2D(final JCGLTexture2DUsableType t)
            throws JCGLException
          {
            if (LOG.isDebugEnabled()) {
              LOG.debug(
                "[{}] attach color {} {}",
                Integer.valueOf(f_id),
                Integer.valueOf(index),
                t);
            }

            LWJGL3Textures.checkTexture2D(c, t);
            final JCGLTextureFormat f = t.format();
            JCGLTextureFormats.checkColorRenderableTexture2D(f);

            GL30.glFramebufferTexture2D(
              GL30.GL_DRAW_FRAMEBUFFER,
              GL30.GL_COLOR_ATTACHMENT0 + index,
              GL11.GL_TEXTURE_2D,
              t.glName(),
              0);
            return Unit.unit();
          }

          @Override
          public Unit onTextureCube(
            final JCGLTextureCubeUsableType t,
            final JCGLCubeMapFaceLH face)
            throws JCGLException, UnreachableCodeException
          {
            if (LOG.isDebugEnabled()) {
              LOG.debug(
                "[{}] attach color {} {} (face {})",
                Integer.valueOf(f_id),
                Integer.valueOf(index),
                t,
                face);
            }

            LWJGL3Textures.checkTextureCube(c, t);
            final JCGLTextureFormat f = t.format();
            JCGLTextureFormats.checkColorRenderableTexture2D(f);

            final int gface = LWJGL3TypeConversions.cubeFaceToGL(face);
            GL30.glFramebufferTexture2D(
              GL30.GL_DRAW_FRAMEBUFFER,
              GL30.GL_COLOR_ATTACHMENT0 + index,
              gface,
              t.glName(),
              0);
            return Unit.unit();
          }
        });
      fb.setColorAttachment(bb.color_points.get(index), a);
    }
  }

  private static void framebufferAllocateConfigureDepthStencil(
    final LWJGL3Context c,
    final Builder bb,
    final int f_id,
    final LWJGL3Framebuffer fb)
  {
    Preconditions.checkPrecondition(
      bb.depth,
      bb.depth == null,
      ignored -> "Depth must be null");

    bb.depth_stencil.matchDepthStencilAttachment(
      new JCGLFramebufferDepthStencilAttachmentMatcherType<Unit,
        UnreachableCodeException>()
      {
        @Override
        public Unit onTexture2D(final JCGLTexture2DUsableType t)
          throws JCGLException
        {
          if (LOG.isDebugEnabled()) {
            LOG.debug(
              "[{}] attach depth+stencil {}",
              Integer.valueOf(f_id),
              t);
          }

          LWJGL3Textures.checkTexture2D(c, t);
          final JCGLTextureFormat f = t.format();
          JCGLTextureFormats.checkDepthStencilRenderableTexture2D(f);
          GL30.glFramebufferTexture2D(
            GL30.GL_DRAW_FRAMEBUFFER,
            GL30.GL_DEPTH_STENCIL_ATTACHMENT,
            GL11.GL_TEXTURE_2D,
            t.glName(),
            0);

          fb.setDepthStencilAttachment(
            t,
            JCGLTextureFormats.depthBits(f),
            JCGLTextureFormats.stencilBits(f));
          return Unit.unit();
        }
      });
  }

  private static void framebufferAllocateConfigureDepth(
    final LWJGL3Context c,
    final Builder bb,
    final int f_id,
    final LWJGL3Framebuffer fb)
  {
    Preconditions.checkPrecondition(
      bb.depth_stencil,
      bb.depth_stencil == null,
      ignored -> "Depth stencil must be null");

    bb.depth.matchDepthAttachment(
      new JCGLFramebufferDepthAttachmentMatcherType<Unit,
        UnreachableCodeException>()
      {
        @Override
        public Unit onTexture2D(
          final JCGLTexture2DUsableType t)
          throws JCGLException
        {
          if (LOG.isDebugEnabled()) {
            LOG.debug(
              "[{}] attach depth {}",
              Integer.valueOf(f_id),
              t);
          }

          final JCGLTextureFormat f = t.format();
          LWJGL3Textures.checkTexture2D(c, t);
          JCGLTextureFormats.checkDepthOnlyRenderableTexture2D(f);
          GL30.glFramebufferTexture2D(
            GL30.GL_DRAW_FRAMEBUFFER,
            GL30.GL_DEPTH_ATTACHMENT,
            GL11.GL_TEXTURE_2D,
            t.glName(),
            0);

          fb.setDepthAttachment(t, JCGLTextureFormats.depthBits(f));
          return Unit.unit();
        }
      });
  }

  LWJGL3Framebuffer getBindDraw()
  {
    return this.bind_draw;
  }

  @Override
  public JCGLFramebufferBuilderType framebufferNewBuilder()
    throws JCGLException
  {
    return new Builder(this.color_points, this.context);
  }

  @Override
  public JCGLFramebufferType framebufferAllocate(
    final JCGLFramebufferBuilderType b)
    throws JCGLException
  {
    NullCheck.notNull(b, "Builder");

    final LWJGL3Context c = this.context;
    checkFramebufferBuilder(c, b);

    Preconditions.checkPrecondition(
      b,
      b instanceof Builder,
      ignored -> "Builder must belong to this implementation");
    final Builder bb = (Builder) b;

    final int f_id = GL30.glGenFramebuffers();
    if (LOG.isDebugEnabled()) {
      LOG.debug("allocate {}", Integer.valueOf(f_id));
    }

    final LWJGL3Framebuffer fb = new LWJGL3Framebuffer(c, f_id);
    this.actualBindDraw(fb);

    /*
     * Configure depth/stencil attachments.
     */

    if (bb.depth != null) {
      framebufferAllocateConfigureDepth(
        c, bb, f_id, fb);
    }

    if (bb.depth_stencil != null) {
      framebufferAllocateConfigureDepthStencil(
        c, bb, f_id, fb);
    }

    /*
     * Configure color attachments.
     */

    for (int index = 0; index < bb.color_attaches.size(); ++index) {
      framebufferAllocateConfigureColorAttachments(
        c, bb, f_id, fb, index);
    }

    /*
     * Configure draw buffer mappings.
     */

    this.framebufferAllocateConfigureDrawBuffers(bb, f_id);

    /*
     * Validate.
     */

    final JCGLFramebufferStatus status = this.framebufferDrawValidate();
    switch (status) {
      case FRAMEBUFFER_STATUS_COMPLETE: {
        if (LOG.isDebugEnabled()) {
          LOG.debug("allocated {}", fb);
        }
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

  private void framebufferAllocateConfigureDrawBuffers(
    final Builder bb,
    final int f_id)
  {
    final IntBuffer draw_buffer_mappings =
      BufferUtils.createIntBuffer(this.draw_buffers.size());

    for (int index = 0; index < this.draw_buffers.size(); ++index) {
      final JCGLFramebufferDrawBufferType buffer = this.draw_buffers.get(index);
      if (bb.draw_buffers.containsKey(buffer)) {
        final JCGLFramebufferColorAttachmentPointType attach =
          bb.draw_buffers.get(buffer);

        if (LOG.isDebugEnabled()) {
          LOG.debug(
            "[{}] draw buffer {} -> color {}",
            Integer.valueOf(f_id),
            Integer.valueOf(index),
            Integer.valueOf(attach.colorAttachmentPointIndex()));
        }
        draw_buffer_mappings.put(
          index,
          GL30.GL_COLOR_ATTACHMENT0 + attach.colorAttachmentPointIndex());
      } else {
        if (LOG.isDebugEnabled()) {
          LOG.debug(
            "[{}] draw buffer {} -> none",
            Integer.valueOf(f_id),
            Integer.valueOf(index));
        }
        draw_buffer_mappings.put(index, GL11.GL_NONE);
      }
    }

    draw_buffer_mappings.rewind();
    GL20.glDrawBuffers(draw_buffer_mappings);
  }

  @Override
  public void framebufferDelete(
    final JCGLFramebufferType framebuffer)
    throws JCGLException
  {
    final LWJGL3Framebuffer fb =
      checkFramebuffer(this.context, framebuffer);

    GL30.glDeleteFramebuffers(framebuffer.glName());

    fb.setDeleted();
    if (Objects.equals(framebuffer, this.bind_draw)) {
      this.actualUnbindDraw();
    }
    if (Objects.equals(framebuffer, this.bind_read)) {
      this.actualUnbindRead();
    }
  }

  private void actualBindDraw(final LWJGL3Framebuffer f)
  {
    if (LOG.isTraceEnabled()) {
      LOG.trace("bind draw {} -> {}", this.bind_draw, f);
    }

    if (!Objects.equals(this.bind_draw, f)) {
      for (final JCGLReferableType r : f.references()) {
        if (r instanceof JCGLTexture2DUsableType) {
          final JCGLTexture2DUsableType t = (JCGLTexture2DUsableType) r;
          if (this.textures.texture2DIsBoundAnywhere(t)) {
            onFeedbackLoop(f, t);
          }
        }
      }

      GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, f.glName());
      this.bind_draw = f;
    } else {
      if (LOG.isTraceEnabled()) {
        LOG.trace(
          "redundant bind draw ignored {} -> {}",
          this.bind_draw,
          f);
      }
    }
  }

  private void actualBindRead(final LWJGL3Framebuffer f)
  {
    if (LOG.isTraceEnabled()) {
      LOG.trace("bind read {} -> {}", this.bind_read, f);
    }

    if (!Objects.equals(this.bind_read, f)) {
      GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, f.glName());
      this.bind_read = f;
    } else {
      if (LOG.isTraceEnabled()) {
        LOG.trace(
          "redundant bind read ignored {} -> {}",
          this.bind_read,
          f);
      }
    }
  }

  private void actualUnbindDraw()
  {
    if (LOG.isTraceEnabled()) {
      LOG.trace("unbind draw {} -> none", this.bind_draw);
    }

    if (this.bind_draw != null) {
      GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
      this.bind_draw = null;
    } else {
      if (LOG.isTraceEnabled()) {
        LOG.trace(
          "redundant unbind draw ignored {} -> none",
          this.bind_draw);
      }
    }
  }

  private void actualUnbindRead()
  {
    if (LOG.isTraceEnabled()) {
      LOG.trace("unbind read {} -> none", this.bind_read);
    }

    if (this.bind_read != null) {
      GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, 0);
      this.bind_read = null;
    } else {
      if (LOG.isTraceEnabled()) {
        LOG.trace(
          "redundant unbind read ignored {} -> none",
          this.bind_read);
      }
    }
  }

  @Override
  public boolean framebufferDrawAnyIsBound()
    throws JCGLException
  {
    return this.bind_draw != null;
  }

  @Override
  public void framebufferDrawBind(
    final JCGLFramebufferUsableType framebuffer)
    throws JCGLException
  {
    checkFramebuffer(this.context, framebuffer);
    this.actualBindDraw((LWJGL3Framebuffer) framebuffer);
  }

  @Override
  public Optional<JCGLFramebufferUsableType> framebufferDrawGetBound()
    throws JCGLException
  {
    return Optional.ofNullable(this.bind_draw);
  }

  @Override
  public boolean framebufferDrawIsBound(
    final JCGLFramebufferUsableType framebuffer)
    throws JCGLException
  {
    checkFramebuffer(this.context, framebuffer);
    return Objects.equals(framebuffer, this.bind_draw);
  }

  @Override
  public void framebufferDrawUnbind()
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

    return LWJGL3TypeConversions.framebufferStatusFromGL(
      GL30.glCheckFramebufferStatus(GL30.GL_DRAW_FRAMEBUFFER));
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

  @Override
  public boolean framebufferReadAnyIsBound()
    throws JCGLException
  {
    return this.bind_read != null;
  }

  @Override
  public void framebufferReadBind(final JCGLFramebufferUsableType framebuffer)
    throws JCGLException
  {
    checkFramebuffer(this.context, framebuffer);
    this.actualBindRead((LWJGL3Framebuffer) framebuffer);
  }

  @Override
  public Optional<JCGLFramebufferUsableType> framebufferReadGetBound()
    throws JCGLException
  {
    return Optional.ofNullable(this.bind_read);
  }

  @Override
  public JCGLFramebufferStatus framebufferReadValidate()
    throws JCGLException
  {
    if (this.bind_read == null) {
      throw new JCGLExceptionFramebufferNotBound(
        "No read framebuffer is bound");
    }

    return LWJGL3TypeConversions.framebufferStatusFromGL(
      GL30.glCheckFramebufferStatus(GL30.GL_READ_FRAMEBUFFER));
  }

  @Override
  public boolean framebufferReadIsBound(
    final JCGLFramebufferUsableType framebuffer)
    throws JCGLException
  {
    checkFramebuffer(this.context, framebuffer);
    return Objects.equals(framebuffer, this.bind_read);
  }

  @Override
  public void framebufferReadUnbind()
    throws JCGLException
  {
    this.actualUnbindRead();
  }

  @Override
  public void framebufferBlit(
    final AreaL source,
    final AreaL target,
    final Set<JCGLFramebufferBlitBuffer> buffers,
    final JCGLFramebufferBlitFilter filter)
    throws JCGLException
  {
    NullCheck.notNull(source, "Source");
    NullCheck.notNull(target, "Target");
    NullCheck.notNull(buffers, "Buffers");
    NullCheck.notNull(filter, "Filter");

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

    /*
     * Section 4.3.2 of the OpenGL 3.1 standard: "The lower bounds of the
     * rectangle are inclusive, while the upper bounds are exclusive".
     */

    final int src_x0 = Math.toIntExact(source.minimumX());
    final int src_y0 = Math.toIntExact(source.minimumY());
    final int src_x1 = Math.toIntExact(source.maximumX());
    final int src_y1 = Math.toIntExact(source.maximumY());

    final int dst_x0 = Math.toIntExact(target.minimumX());
    final int dst_y0 = Math.toIntExact(target.minimumY());
    final int dst_x1 = Math.toIntExact(target.maximumX());
    final int dst_y1 = Math.toIntExact(target.maximumY());

    final int mask =
      LWJGL3TypeConversions.framebufferBlitBufferSetToMask(buffers);
    final int filteri = LWJGL3TypeConversions.framebufferBlitFilterToGL(filter);

    GL30.glBlitFramebuffer(
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

  private static final class Builder extends LWJGL3ObjectPseudoUnshared
    implements JCGLFramebufferBuilderType
  {
    private final List<JCGLFramebufferColorAttachmentPointType> color_points;
    private final LWJGL3Context context;
    private final List<JCGLFramebufferColorAttachmentType> color_attaches;
    private final SortedMap<JCGLFramebufferDrawBufferType,
      JCGLFramebufferColorAttachmentPointType> draw_buffers;
    private JCGLFramebufferDepthAttachmentType depth;
    private JCGLFramebufferDepthStencilAttachmentType depth_stencil;

    Builder(
      final List<JCGLFramebufferColorAttachmentPointType> in_color_points,
      final LWJGL3Context in_context)
    {
      super(in_context);

      this.color_points = NullCheck.notNull(in_color_points, "Color points");
      this.context = NullCheck.notNull(in_context, "Context");
      this.color_attaches = new ArrayList<>(this.color_points.size());
      for (int index = 0; index < this.color_points.size(); ++index) {
        this.color_attaches.add(null);
      }
      this.draw_buffers = new TreeMap<>();
    }

    @Override
    public void attachColorTexture2DAt(
      final JCGLFramebufferColorAttachmentPointType point,
      final JCGLFramebufferDrawBufferType buffer,
      final JCGLTexture2DUsableType texture)
    {
      NullCheck.notNull(point, "Point");
      checkColorAttachmentPoint(this.context, point);
      checkDrawBuffer(this.context, buffer);
      LWJGL3Textures.checkTexture2D(this.context, texture);
      JCGLTextureFormats.checkColorRenderableTexture2D(
        texture.format());

      this.color_attaches.set(point.colorAttachmentPointIndex(), texture);
      this.draw_buffers.put(buffer, point);
    }

    @Override
    public void attachColorTextureCubeAt(
      final JCGLFramebufferColorAttachmentPointType point,
      final JCGLFramebufferDrawBufferType buffer,
      final JCGLTextureCubeUsableType texture,
      final JCGLCubeMapFaceLH face)
    {
      NullCheck.notNull(point, "Point");
      NullCheck.notNull(face, "Face");
      checkColorAttachmentPoint(this.context, point);
      checkDrawBuffer(this.context, buffer);
      LWJGL3Textures.checkTextureCube(this.context, texture);
      JCGLTextureFormats.checkColorRenderableTexture2D(
        texture.format());

      this.color_attaches.set(
        point.colorAttachmentPointIndex(),
        new CubeAttachment(texture, face));
      this.draw_buffers.put(buffer, point);
    }

    @Override
    public void attachDepthTexture2D(
      final JCGLTexture2DUsableType t)
    {
      LWJGL3Textures.checkTexture2D(this.context, t);
      JCGLTextureFormats.checkDepthOnlyRenderableTexture2D(
        t.format());
      this.depth = t;
      this.depth_stencil = null;
    }

    @Override
    public void attachDepthStencilTexture2D(final JCGLTexture2DUsableType t)
    {
      LWJGL3Textures.checkTexture2D(this.context, t);
      JCGLTextureFormats.checkDepthStencilRenderableTexture2D(
        t.format());
      this.depth = null;
      this.depth_stencil = t;
    }

    @Override
    public void detachDepth()
    {
      this.depth = null;
      this.depth_stencil = null;
    }

    @Override
    public void detachColorAttachment(
      final JCGLFramebufferColorAttachmentPointType point)
    {
      NullCheck.notNull(point, "Point");
      checkColorAttachmentPoint(this.context, point);

      final int index = point.colorAttachmentPointIndex();
      this.color_attaches.set(index, null);
      this.draw_buffers.values().remove(point);
    }

    private static final class CubeAttachment
      implements JCGLFramebufferColorAttachmentType
    {
      private final JCGLTextureCubeUsableType texture;
      private final JCGLCubeMapFaceLH face;

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

      @Override
      public Set<JCGLReferenceContainerType> referringContainers()
      {
        throw new UnreachableCodeException();
      }

      @Override
      public int glName()
      {
        return this.texture.glName();
      }
    }
  }
}
