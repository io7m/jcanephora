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

package com.io7m.jcanephora.fake;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

final class FakeFramebuffers implements JCGLFramebuffersType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(FakeFramebuffers.class);
  }

  private final List<JCGLFramebufferColorAttachmentPointType> color_points;
  private final FakeContext context;
  private final List<JCGLFramebufferDrawBufferType> draw_buffers;
  private final FakeTextures textures;
  private FakeFramebuffer bind_draw;
  private FakeFramebuffer bind_read;

  FakeFramebuffers(
    final FakeContext c,
    final FakeTextures t)
    throws JCGLExceptionNonCompliant
  {
    this.context = NullCheck.notNull(c);
    this.textures = NullCheck.notNull(t);

    this.color_points = makeColorPoints(c);
    this.draw_buffers = makeDrawBuffers(c);
    this.textures.setFramebuffers(this);
  }

  private static List<JCGLFramebufferDrawBufferType> makeDrawBuffers(
    final FakeContext c)
    throws JCGLExceptionNonCompliant
  {
    final List<JCGLFramebufferDrawBufferType> u = new ArrayList<>(8);
    for (int index = 0; index < 8; ++index) {
      final FakeFramebufferDrawBuffer db =
        new FakeFramebufferDrawBuffer(c, index);
      u.add(db);
    }

    return Collections.unmodifiableList(u);
  }

  private static List<JCGLFramebufferColorAttachmentPointType> makeColorPoints(
    final FakeContext in_c)
    throws JCGLExceptionNonCompliant
  {
    final List<JCGLFramebufferColorAttachmentPointType> u =
      new ArrayList<>(8);
    for (int index = 0; index < 8; ++index) {
      final FakeFramebufferColorAttachmentPoint cu =
        new FakeFramebufferColorAttachmentPoint(in_c, index);
      u.add(cu);
    }

    return Collections.unmodifiableList(u);
  }

  static void checkColorAttachmentPoint(
    final FakeContext c,
    final JCGLFramebufferColorAttachmentPointType point)
  {
    NullCheck.notNull(point, "Point");
    FakeCompatibilityChecks.checkFramebufferColorAttachmentPoint(c, point);
  }

  static void checkDrawBuffer(
    final FakeContext c,
    final JCGLFramebufferDrawBufferType buffer)
  {
    NullCheck.notNull(buffer, "Buffer");
    FakeCompatibilityChecks.checkDrawBuffer(c, buffer);
  }

  static FakeFramebuffer checkFramebuffer(
    final FakeContext c,
    final JCGLFramebufferUsableType framebuffer)
  {
    NullCheck.notNull(framebuffer, "Framebuffer");
    FakeCompatibilityChecks.checkFramebuffer(c, framebuffer);
    JCGLResources.checkNotDeleted(framebuffer);
    return (FakeFramebuffer) framebuffer;
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

  FakeFramebuffer getBindDraw()
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
    FakeCompatibilityChecks.checkFramebufferBuilder(this.context, b);

    Preconditions.checkPrecondition(
      b,
      b instanceof Builder,
      ignored -> "Builder must belong to this implementation");

    final Builder bb = (Builder) b;
    final int f_id = this.context.getFreshID();

    LOG.debug("allocate {}", Integer.valueOf(f_id));

    final FakeFramebuffer fb =
      new FakeFramebuffer(this.context, f_id);
    this.actualBindDraw(fb);

    /*
     * Configure depth/stencil attachments.
     */

    if (bb.depth != null) {
      this.framebufferAllocateConfigureDepth(bb, f_id, fb);
    }

    if (bb.depth_stencil != null) {
      this.framebufferAllocateConfigureDepthStencil(bb, f_id, fb);
    }

    /*
     * Configure color attachments.
     */

    for (int index = 0; index < bb.color_attaches.size(); ++index) {
      this.framebufferAllocateConfigureColorAttachments(bb, f_id, fb, index);
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
    for (int index = 0; index < this.draw_buffers.size(); ++index) {
      final JCGLFramebufferDrawBufferType buffer = this.draw_buffers.get(index);
      if (bb.draw_buffers.containsKey(buffer)) {
        final JCGLFramebufferColorAttachmentPointType attach =
          bb.draw_buffers.get(buffer);
        LOG.debug(
          "[{}] draw buffer {} -> color {}",
          Integer.valueOf(f_id),
          Integer.valueOf(index),
          Integer.valueOf(attach.colorAttachmentPointGetIndex()));
      } else {
        LOG.debug(
          "[{}] draw buffer {} -> none",
          Integer.valueOf(f_id),
          Integer.valueOf(index));
      }
    }
  }

  private void framebufferAllocateConfigureColorAttachments(
    final Builder bb,
    final int f_id,
    final FakeFramebuffer fb,
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
            LOG.debug(
              "[{}] attach {} at color {}",
              Integer.valueOf(f_id),
              t,
              Integer.valueOf(index));

            FakeTextures.checkTexture2D(FakeFramebuffers.this.context, t);
            final JCGLTextureFormat f = t.textureGetFormat();
            JCGLTextureFormats.checkColorRenderableTexture2D(f);
            return Unit.unit();
          }

          @Override
          public Unit onTextureCube(
            final JCGLTextureCubeUsableType t,
            final JCGLCubeMapFaceLH face)
            throws JCGLException, UnreachableCodeException
          {
            LOG.debug(
              "[{}] attach {} (face {}) at color {}",
              Integer.valueOf(f_id),
              t,
              face,
              Integer.valueOf(index));

            FakeTextures.checkTextureCube(FakeFramebuffers.this.context, t);
            final JCGLTextureFormat f = t.textureGetFormat();
            JCGLTextureFormats.checkColorRenderableTexture2D(f);
            return Unit.unit();
          }
        });
      fb.setColorAttachment(bb.color_points.get(index), a);
    }
  }

  private void framebufferAllocateConfigureDepthStencil(
    final Builder bb,
    final int f_id,
    final FakeFramebuffer fb)
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
          LOG.debug(
            "[{}] attach {} at depth+stencil",
            Integer.valueOf(f_id),
            t);

          FakeTextures.checkTexture2D(FakeFramebuffers.this.context, t);
          final JCGLTextureFormat f = t.textureGetFormat();
          JCGLTextureFormats.checkDepthStencilRenderableTexture2D(f);
          fb.setDepthStencilAttachment(
            t,
            JCGLTextureFormats.getDepthBits(f),
            JCGLTextureFormats.getStencilBits(f));
          return Unit.unit();
        }
      });
  }

  private void framebufferAllocateConfigureDepth(
    final Builder bb,
    final int f_id,
    final FakeFramebuffer fb)
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
          LOG.debug(
            "[{}] attach {} at depth",
            Integer.valueOf(f_id),
            t);

          final JCGLTextureFormat f = t.textureGetFormat();
          FakeTextures.checkTexture2D(FakeFramebuffers.this.context, t);
          JCGLTextureFormats.checkDepthOnlyRenderableTexture2D(f);
          fb.setDepthAttachment(t, JCGLTextureFormats.getDepthBits(f));
          return Unit.unit();
        }
      });
  }

  @Override
  public void framebufferDelete(
    final JCGLFramebufferType framebuffer)
    throws JCGLException
  {
    final FakeFramebuffer fb =
      checkFramebuffer(this.context, framebuffer);

    fb.setDeleted();
    if (Objects.equals(framebuffer, this.bind_draw)) {
      this.actualUnbindDraw();
    }
    if (Objects.equals(framebuffer, this.bind_read)) {
      this.actualUnbindRead();
    }
  }

  private void actualBindDraw(final FakeFramebuffer f)
  {
    LOG.trace("bind draw {} -> {}", this.bind_draw, f);

    for (final JCGLReferableType r : f.getReferences()) {
      if (r instanceof JCGLTexture2DUsableType) {
        final JCGLTexture2DUsableType t = (JCGLTexture2DUsableType) r;
        if (this.textures.texture2DIsBoundAnywhere(t)) {
          onFeedbackLoop(f, t);
        }
      }
    }

    this.bind_draw = f;
  }

  private void actualBindRead(final FakeFramebuffer f)
  {
    LOG.trace("bind read {} -> {}", this.bind_read, f);
    this.bind_read = f;
  }

  private void actualUnbindDraw()
  {
    LOG.trace("unbind {} -> none", this.bind_draw);
    this.bind_draw = null;
  }

  private void actualUnbindRead()
  {
    LOG.trace("unbind {} -> none", this.bind_read);
    this.bind_read = null;
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
    this.actualBindDraw((FakeFramebuffer) framebuffer);
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

    if (this.bind_draw.getReferences().isEmpty()) {
      return JCGLFramebufferStatus
        .FRAMEBUFFER_STATUS_ERROR_MISSING_IMAGE_ATTACHMENT;
    }

    return JCGLFramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
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
    this.actualBindRead((FakeFramebuffer) framebuffer);
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

    if (this.bind_draw.getReferences().isEmpty()) {
      return JCGLFramebufferStatus
        .FRAMEBUFFER_STATUS_ERROR_MISSING_IMAGE_ATTACHMENT;
    }

    return JCGLFramebufferStatus.FRAMEBUFFER_STATUS_COMPLETE;
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
  }

  private static final class Builder extends FakeObjectPseudoUnshared
    implements JCGLFramebufferBuilderType
  {
    private final List<JCGLFramebufferColorAttachmentPointType> color_points;
    private final FakeContext context;
    private final List<JCGLFramebufferColorAttachmentType> color_attaches;
    private final SortedMap<JCGLFramebufferDrawBufferType,
      JCGLFramebufferColorAttachmentPointType> draw_buffers;
    private JCGLFramebufferDepthAttachmentType depth;
    private JCGLFramebufferDepthStencilAttachmentType depth_stencil;

    Builder(
      final List<JCGLFramebufferColorAttachmentPointType> in_color_points,
      final FakeContext in_context)
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
      FakeTextures.checkTexture2D(this.context, texture);
      JCGLTextureFormats.checkColorRenderableTexture2D(
        texture.textureGetFormat());

      this.color_attaches.set(point.colorAttachmentPointGetIndex(), texture);
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
      FakeTextures.checkTextureCube(this.context, texture);
      JCGLTextureFormats.checkColorRenderableTexture2D(
        texture.textureGetFormat());

      this.color_attaches.set(
        point.colorAttachmentPointGetIndex(),
        new CubeAttachment(texture, face));
      this.draw_buffers.put(buffer, point);
    }

    @Override
    public void attachDepthTexture2D(
      final JCGLTexture2DUsableType t)
    {
      FakeTextures.checkTexture2D(this.context, t);
      JCGLTextureFormats.checkDepthOnlyRenderableTexture2D(
        t.textureGetFormat());
      this.depth = t;
      this.depth_stencil = null;
    }

    @Override
    public void attachDepthStencilTexture2D(final JCGLTexture2DUsableType t)
    {
      FakeTextures.checkTexture2D(this.context, t);
      JCGLTextureFormats.checkDepthStencilRenderableTexture2D(
        t.textureGetFormat());
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

      final int index = point.colorAttachmentPointGetIndex();
      this.color_attaches.set(index, null);
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
      public Set<JCGLReferenceContainerType> getReferringContainers()
      {
        throw new UnreachableCodeException();
      }

      @Override
      public int getGLName()
      {
        return this.texture.getGLName();
      }
    }
  }
}
