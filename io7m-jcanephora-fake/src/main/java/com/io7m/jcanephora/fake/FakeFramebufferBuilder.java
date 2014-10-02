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

package com.io7m.jcanephora.fake;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jcanephora.FramebufferColorAttachmentType;
import com.io7m.jcanephora.FramebufferColorAttachmentVisitorType;
import com.io7m.jcanephora.FramebufferDepthAttachmentType;
import com.io7m.jcanephora.FramebufferDepthAttachmentVisitorType;
import com.io7m.jcanephora.FramebufferDepthStencilAttachmentType;
import com.io7m.jcanephora.FramebufferDepthStencilAttachmentVisitorType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.FramebufferStencilAttachmentType;
import com.io7m.jcanephora.FramebufferStencilAttachmentVisitorType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLVersion;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.RenderbufferUsableType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderGL3ES3Type;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jfunctional.Option;
import com.io7m.jfunctional.OptionType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;

/**
 * The default mutable framebuffer builder implementation.
 */

public final class FakeFramebufferBuilder implements
  JCGLFramebufferBuilderGL3ES3Type
{
  private static final class ColorRenderbuffer implements
    FramebufferColorAttachmentType
  {
    private final RenderbufferUsableType<RenderableColorKind> renderbuffer;

    ColorRenderbuffer(
      final RenderbufferUsableType<RenderableColorKind> in_renderbuffer)
    {
      this.renderbuffer = in_renderbuffer;
    }

    @Override public <A, E extends Throwable> A colorAttachmentAccept(
      final FramebufferColorAttachmentVisitorType<A, E> v)
      throws JCGLException,
        E
    {
      return v.renderbuffer(this.renderbuffer);
    }
  }

  private static final class ColorTexture2D implements
    FramebufferColorAttachmentType
  {
    private final Texture2DStaticUsableType texture;

    ColorTexture2D(
      final Texture2DStaticUsableType in_texture)
    {
      this.texture = in_texture;
    }

    @Override public <A, E extends Throwable> A colorAttachmentAccept(
      final FramebufferColorAttachmentVisitorType<A, E> v)
      throws JCGLException,
        E
    {
      return v.texture2DStatic(this.texture);
    }
  }

  private static final class ColorTextureCubeFace implements
    FramebufferColorAttachmentType
  {
    private final CubeMapFaceLH               face;
    private final TextureCubeStaticUsableType texture;

    ColorTextureCubeFace(
      final TextureCubeStaticUsableType in_texture,
      final CubeMapFaceLH in_face)
    {
      this.texture = in_texture;
      this.face = in_face;
    }

    @Override public <A, E extends Throwable> A colorAttachmentAccept(
      final FramebufferColorAttachmentVisitorType<A, E> v)
      throws JCGLException,
        E
    {
      return v.textureCubeStatic(this.texture, this.face);
    }
  }

  private static final class DepthRenderbuffer implements
    FramebufferDepthAttachmentType
  {
    private final RenderbufferUsableType<RenderableDepthKind> renderbuffer;

    DepthRenderbuffer(
      final RenderbufferUsableType<RenderableDepthKind> in_renderbuffer)
    {
      this.renderbuffer = in_renderbuffer;
    }

    @Override public <A, E extends Throwable> A depthAttachmentAccept(
      final FramebufferDepthAttachmentVisitorType<A, E> v)
      throws JCGLException,
        E
    {
      return v.renderbuffer(this.renderbuffer);
    }
  }

  private static final class DepthStencilRenderbuffer implements
    FramebufferDepthStencilAttachmentType
  {
    private final RenderbufferUsableType<RenderableDepthStencilKind> renderbuffer;

    DepthStencilRenderbuffer(
      final RenderbufferUsableType<RenderableDepthStencilKind> in_renderbuffer)
    {
      this.renderbuffer = in_renderbuffer;
    }

    @Override public <A, E extends Throwable> A depthStencilAttachmentAccept(
      final FramebufferDepthStencilAttachmentVisitorType<A, E> v)
      throws JCGLException,
        E
    {
      return v.renderbuffer(this.renderbuffer);
    }
  }

  private static final class DepthStencilTexture2D implements
    FramebufferDepthStencilAttachmentType
  {
    private final Texture2DStaticUsableType texture;

    DepthStencilTexture2D(
      final Texture2DStaticUsableType in_texture)
    {
      this.texture = in_texture;
    }

    @Override public <A, E extends Throwable> A depthStencilAttachmentAccept(
      final FramebufferDepthStencilAttachmentVisitorType<A, E> v)
      throws JCGLException,
        E
    {
      return v.texture2DStatic(this.texture);
    }
  }

  private static final class DepthTexture2D implements
    FramebufferDepthAttachmentType
  {
    private final Texture2DStaticUsableType texture;

    DepthTexture2D(
      final Texture2DStaticUsableType in_texture)
    {
      this.texture = in_texture;
    }

    @Override public <A, E extends Throwable> A depthAttachmentAccept(
      final FramebufferDepthAttachmentVisitorType<A, E> v)
      throws JCGLException,
        E
    {
      return v.texture2DStatic(this.texture);
    }
  }

  private static final class StencilRenderbuffer implements
    FramebufferStencilAttachmentType
  {
    private final RenderbufferUsableType<RenderableStencilKind> renderbuffer;

    StencilRenderbuffer(
      final RenderbufferUsableType<RenderableStencilKind> in_renderbuffer)
    {
      this.renderbuffer = in_renderbuffer;
    }

    @Override public <A, E extends Throwable> A stencilAttachmentAccept(
      final FramebufferStencilAttachmentVisitorType<A, E> v)
      throws JCGLException,
        E
    {
      return v.renderbuffer(this.renderbuffer);
    }
  }

  private final Map<FramebufferColorAttachmentPointType, FramebufferColorAttachmentType> color_attachments;
  private @Nullable FramebufferDepthAttachmentType                                       depth_attach;
  private @Nullable FramebufferDepthStencilAttachmentType                                depth_stencil_attach;
  private Map<FramebufferDrawBufferType, FramebufferColorAttachmentPointType>            draw_buffer_mappings;
  private final FramebufferDrawBufferType                                                draw_zero;
  private final JCGLNamedExtensionsType                                                  extensions;
  private final FramebufferColorAttachmentPointType                                      point_zero;
  private @Nullable FramebufferStencilAttachmentType                                     stencil_attach;
  private final JCGLVersion                                                              version;

  FakeFramebufferBuilder(
    final List<FramebufferColorAttachmentPointType> in_attachment_points,
    final List<FramebufferDrawBufferType> in_draw_buffers,
    final JCGLNamedExtensionsType in_extensions,
    final JCGLVersion in_version)
  {
    NullCheck.notNull(in_attachment_points, "Points");
    this.extensions = NullCheck.notNull(in_extensions, "Extensions");
    this.version = NullCheck.notNull(in_version, "Version");

    final FramebufferColorAttachmentPointType color_zero =
      in_attachment_points.get(0);
    assert color_zero != null;
    this.point_zero = color_zero;

    final FramebufferDrawBufferType in_draw_zero = in_draw_buffers.get(0);
    assert in_draw_zero != null;
    this.draw_zero = in_draw_zero;

    this.color_attachments =
      new HashMap<FramebufferColorAttachmentPointType, FramebufferColorAttachmentType>();
    this.depth_attach = null;
    this.depth_stencil_attach = null;
    this.stencil_attach = null;

    this.draw_buffer_mappings =
      new HashMap<FramebufferDrawBufferType, FramebufferColorAttachmentPointType>();
    this.draw_buffer_mappings.put(this.draw_zero, this.point_zero);
  }

  @Override public void attachColorRenderbuffer(
    final RenderbufferUsableType<RenderableColorKind> r)
  {
    NullCheck.notNull(r, "Renderbuffer");
    RenderbufferFormat.checkColorRenderableRenderbuffer(r);
    this.color_attachments.put(this.point_zero, new ColorRenderbuffer(r));
  }

  @Override public void attachColorRenderbufferAt(
    final FramebufferColorAttachmentPointType point,
    final RenderbufferUsableType<RenderableColorKind> r)
  {
    NullCheck.notNull(point, "Point");
    NullCheck.notNull(r, "Renderbuffer");
    RenderbufferFormat.checkColorRenderableRenderbuffer(r);
    this.color_attachments.put(point, new ColorRenderbuffer(r));
  }

  @Override public void attachColorTexture2D(
    final Texture2DStaticUsableType t)
  {
    NullCheck.notNull(t, "Texture");
    TextureFormatMeta.checkColorRenderableTexture2D(
      t,
      this.version,
      this.extensions);
    this.color_attachments.put(this.point_zero, new ColorTexture2D(t));
  }

  @Override public void attachColorTexture2DAt(
    final FramebufferColorAttachmentPointType point,
    final Texture2DStaticUsableType t)
  {
    NullCheck.notNull(point, "Point");
    NullCheck.notNull(t, "Texture");
    TextureFormatMeta.checkColorRenderableTexture2D(
      t,
      this.version,
      this.extensions);
    this.color_attachments.put(point, new ColorTexture2D(t));
  }

  @Override public void attachColorTextureCube(
    final TextureCubeStaticUsableType t,
    final CubeMapFaceLH face)
  {
    NullCheck.notNull(t, "Texture");
    NullCheck.notNull(face, "Face");
    TextureFormatMeta.checkColorRenderableTextureCube(
      t,
      this.version,
      this.extensions);
    this.color_attachments.put(this.point_zero, new ColorTextureCubeFace(
      t,
      face));
  }

  @Override public void attachColorTextureCubeAt(
    final FramebufferColorAttachmentPointType point,
    final TextureCubeStaticUsableType t,
    final CubeMapFaceLH face)
  {
    NullCheck.notNull(point, "Point");
    NullCheck.notNull(t, "Texture");
    NullCheck.notNull(face, "Face");
    TextureFormatMeta.checkColorRenderableTextureCube(
      t,
      this.version,
      this.extensions);
    this.color_attachments.put(point, new ColorTextureCubeFace(t, face));
  }

  @Override public void attachDepthRenderbuffer(
    final RenderbufferUsableType<RenderableDepthKind> r)
  {
    NullCheck.notNull(r, "Renderbuffer");
    RenderbufferFormat.checkDepthOnlyRenderableRenderbuffer(r);
    this.depth_attach = new DepthRenderbuffer(r);
    this.depth_stencil_attach = null;
  }

  @Override public void attachDepthStencilRenderbuffer(
    final RenderbufferUsableType<RenderableDepthStencilKind> r)
  {
    NullCheck.notNull(r, "Renderbuffer");
    RenderbufferFormat.checkDepthStencilRenderableRenderbuffer(r);
    this.depth_stencil_attach = new DepthStencilRenderbuffer(r);
    this.depth_attach = null;
    this.stencil_attach = null;
  }

  @Override public void attachDepthStencilTexture2D(
    final Texture2DStaticUsableType t)
  {
    NullCheck.notNull(t, "Texture");
    TextureFormatMeta
      .checkDepthStencilRenderableTexture2D(t, this.extensions);
    this.depth_stencil_attach = new DepthStencilTexture2D(t);
    this.depth_attach = null;
    this.stencil_attach = null;
  }

  @Override public void attachDepthTexture2D(
    final Texture2DStaticUsableType t)
  {
    NullCheck.notNull(t, "Texture");
    TextureFormatMeta.checkDepthOnlyRenderableTexture2D(t, this.extensions);
    this.depth_stencil_attach = null;
    this.depth_attach = new DepthTexture2D(t);
  }

  @Override public void attachStencilRenderbuffer(
    final RenderbufferUsableType<RenderableStencilKind> r)
  {
    NullCheck.notNull(r, "Renderbuffer");
    RenderbufferFormat.checkStencilRenderableRenderbuffer(r);
    this.depth_stencil_attach = null;
    this.stencil_attach = new StencilRenderbuffer(r);
  }

  @Override public
    Map<FramebufferColorAttachmentPointType, FramebufferColorAttachmentType>
    getColorAttachments()
  {
    final Map<FramebufferColorAttachmentPointType, FramebufferColorAttachmentType> r =
      Collections.unmodifiableMap(this.color_attachments);
    assert r != null;
    return r;
  }

  @Override public
    OptionType<FramebufferDepthAttachmentType>
    getDepthAttachment()
  {
    return Option.of(this.depth_attach);
  }

  @Override public
    OptionType<FramebufferDepthStencilAttachmentType>
    getDepthStencilAttachment()
  {
    return Option.of(this.depth_stencil_attach);
  }

  @Override public
    Map<FramebufferDrawBufferType, FramebufferColorAttachmentPointType>
    getDrawBufferMappings()
  {
    final Map<FramebufferDrawBufferType, FramebufferColorAttachmentPointType> r =
      Collections.unmodifiableMap(this.draw_buffer_mappings);
    assert r != null;
    return r;
  }

  @Override public
    OptionType<FramebufferStencilAttachmentType>
    getStencilAttachment()
  {
    return Option.of(this.stencil_attach);
  }

  @Override public
    void
    setDrawBuffers(
      final Map<FramebufferDrawBufferType, FramebufferColorAttachmentPointType> mappings)
  {
    NullCheck.notNull(mappings, "Mappings");
    this.draw_buffer_mappings =
      new HashMap<FramebufferDrawBufferType, FramebufferColorAttachmentPointType>(
        mappings);
  }
}
