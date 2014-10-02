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

package com.io7m.jcanephora.jogl;

import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExtensionNames;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.api.JCGLExtensionPackedDepthStencilType;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderExtensionPackedDepthStencilType;
import com.io7m.jcanephora.api.JCGLFramebuffersCommonType;
import com.io7m.jcanephora.api.JCGLMetaType;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jfunctional.Option;
import com.io7m.jfunctional.OptionType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;

/**
 * The packed depth/stencil extension.
 */

final class ExtPackedDepthStencil<G extends GL> implements
  JCGLExtensionPackedDepthStencilType
{
  public static
    <G extends GL>
    OptionType<JCGLExtensionPackedDepthStencilType>
    create(
      final GL in_gl,
      final LogUsableType in_log,
      final JCGLMetaType in_meta,
      final JCGLFramebuffersCommonType in_framebuffers,
      final JCGLNamedExtensionsType in_extensions,
      final JOGLIntegerCacheType in_icache,
      final JOGLLogMessageCacheType in_tcache)
      throws JCGLExceptionRuntime
  {
    final String[] names = new String[2];
    names[0] = JCGLExtensionNames.GL_OES_PACKED_DEPTH_STENCIL;
    names[1] = JCGLExtensionNames.GL_EXT_PACKED_DEPTH_STENCIL;

    for (final String name : names) {
      assert name != null;
      if (in_extensions.extensionIsVisible(name)) {
        final JCGLExtensionPackedDepthStencilType e =
          new ExtPackedDepthStencil<GL>(
            in_gl,
            in_log,
            in_meta,
            in_framebuffers,
            in_extensions,
            in_icache,
            in_tcache);
        return Option.some(e);
      }
    }

    return Option.none();
  }

  private final GLContext                  context;
  private final GL                         gl;
  private final JOGLIntegerCacheType       icache;
  private final LogUsableType              log;
  private final JOGLLogMessageCacheType    tcache;
  private final JCGLMetaType               meta;
  private final JCGLFramebuffersCommonType framebuffers;
  private final JCGLNamedExtensionsType    extensions;

  private ExtPackedDepthStencil(
    final GL in_gl,
    final LogUsableType in_log,
    final JCGLMetaType in_meta,
    final JCGLFramebuffersCommonType in_framebuffers,
    final JCGLNamedExtensionsType in_extensions,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
    throws JCGLExceptionRuntime
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.icache = NullCheck.notNull(in_icache, "Integer cache");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.log =
      NullCheck.notNull(in_log, "Log").with("ext-packed-depth-stencil");
    this.context = NullCheck.notNull(this.gl.getContext());
    this.meta = NullCheck.notNull(in_meta, "Meta");
    this.framebuffers = NullCheck.notNull(in_framebuffers, "Framebuffers");
    this.extensions = NullCheck.notNull(in_extensions, "Extensions");
  }

  @SuppressWarnings("unchecked") @Override public
    RenderbufferType<RenderableDepthStencilKind>
    renderbufferAllocateDepth24Stencil8(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    final RenderbufferFormat type =
      RenderbufferFormat.RENDERBUFFER_DEPTH_24_STENCIL_8;

    RangeCheck.checkIncludedIn(
      width,
      "Width",
      RangeCheck.POSITIVE_INTEGER,
      "Valid widths");
    RangeCheck.checkIncludedIn(
      height,
      "Height",
      RangeCheck.POSITIVE_INTEGER,
      "Valid heights");

    final StringBuilder text = this.tcache.getTextCache();

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("allocate ");
      text.append(width);
      text.append("x");
      text.append(height);
      text.append(" ");
      text.append(type);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    final IntBuffer cache = this.icache.getIntegerCache();
    this.gl.glGenRenderbuffers(1, cache);
    final int id = cache.get(0);

    final int gtype = JOGLTypeConversions.renderbufferTypeToGL(type);
    this.gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, id);
    this.gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, gtype, width, height);
    this.gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0);

    final JOGLRenderbuffer<?> r =
      new JOGLRenderbuffer<RenderableColorKind>(
        this.context,
        type,
        id,
        width,
        height);
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("allocated ");
      text.append(r);
      final String s = text.toString();
      assert s != null;
      this.log.debug(s);
    }

    return (RenderbufferType<RenderableDepthStencilKind>) r;
  }

  @Override public
    JCGLFramebufferBuilderExtensionPackedDepthStencilType
    framebufferNewBuilderExtensionPackedDepthStencil()
  {
    return new JOGLFramebufferBuilder(
      this.framebuffers.framebufferGetColorAttachmentPoints(),
      this.framebuffers.framebufferGetDrawBuffers(),
      this.extensions,
      this.meta.metaGetVersion());
  }
}
