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
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.api.JCGLExtensionDepthTextureType;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jcanephora.jogl.JOGL_TextureSpecs.TextureSpec;
import com.io7m.jfunctional.Option;
import com.io7m.jfunctional.OptionType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;

/**
 * The depth texture extension.
 */

final class ExtDepthTexture<G extends GL> implements
  JCGLExtensionDepthTextureType
{
  public static
    <G extends GL>
    OptionType<JCGLExtensionDepthTextureType>
    create(
      final GL in_gl,
      final LogUsableType in_log,
      final JCGLNamedExtensionsType in_extensions,
      final JOGLIntegerCacheType in_icache,
      final JOGLLogMessageCacheType in_tcache)
  {
    final String[] names = new String[1];
    names[0] = JCGLExtensionNames.GL_ARB_DEPTH_TEXTURE;

    for (final String name : names) {
      assert name != null;
      if (in_extensions.extensionIsVisible(name)) {
        final JCGLExtensionDepthTextureType et =
          new ExtDepthTexture<GL>(in_gl, in_log, in_icache, in_tcache);
        return Option.some(et);
      }
    }

    return Option.none();
  }

  private final GLContext               context;
  private final GL                      gl;
  private final JOGLIntegerCacheType    icache;
  private final LogUsableType           log;
  private final JOGLLogMessageCacheType tcache;

  private ExtDepthTexture(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.icache = NullCheck.notNull(in_icache, "Integer cache");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.log = NullCheck.notNull(in_log, "Log").with("ext-depth-texture");
    this.context = NullCheck.notNull(this.gl.getContext());
  }

  private JOGLTexture2DStatic allocate(
    final TextureFormat type,
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNull(type, "Texture type");
    NullCheck.notNull(wrap_s, "Wrap S mode");
    NullCheck.notNull(wrap_t, "Wrap T mode");
    NullCheck.notNull(min_filter, "Minification filter");
    NullCheck.notNull(mag_filter, "Magnification filter");
    RangeCheck.checkGreaterEqual(width, "Width", 2, "Valid widths");
    RangeCheck.checkGreaterEqual(height, "Height", 2, "Valid heights");

    final StringBuilder text = this.tcache.getTextCache();
    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      final int bytes = height * (type.getBytesPerPixel() * width);
      text.setLength(0);
      text.append("allocate \"");
      text.append(name);
      text.append("\" ");
      text.append(type);
      text.append(" ");
      text.append(width);
      text.append("x");
      text.append(height);
      text.append(" ");
      text.append(bytes);
      text.append(" bytes");
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    final IntBuffer cache = this.icache.getIntegerCache();
    this.gl.glGenTextures(1, cache);
    JOGLErrors.check(this.gl);
    final int texture_id = cache.get(0);

    this.gl.glBindTexture(GL.GL_TEXTURE_2D, texture_id);
    JOGLErrors.check(this.gl);
    this.gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_S,
      JOGL_GLTypeConversions.textureWrapSToGL(wrap_s));
    JOGLErrors.check(this.gl);
    this.gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_T,
      JOGL_GLTypeConversions.textureWrapTToGL(wrap_t));
    JOGLErrors.check(this.gl);
    this.gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MIN_FILTER,
      JOGL_GLTypeConversions.textureFilterMinToGL(min_filter));
    JOGLErrors.check(this.gl);
    this.gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MAG_FILTER,
      JOGL_GLTypeConversions.textureFilterMagToGL(mag_filter));
    JOGLErrors.check(this.gl);

    final TextureSpec spec = JOGL_TextureSpecs.getGL3TextureSpec(type);
    JOGLTextures2DStaticAbstract.setPackUnpackAlignment1(this.gl);

    this.gl.glTexImage2D(
      GL.GL_TEXTURE_2D,
      0,
      spec.getInternalFormat(),
      width,
      height,
      0,
      spec.getFormat(),
      spec.getType(),
      null);
    this.gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    JOGLErrors.check(this.gl);

    final JOGLTexture2DStatic t =
      new JOGLTexture2DStatic(
        this.context,
        texture_id,
        name,
        type,
        width,
        height,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter);

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("allocated ");
      text.append(t);
      final String r = text.toString();
      assert r != null;
      this.log.debug(r);
    }

    return t;
  }

  @Override public JOGLTexture2DStatic texture2DStaticAllocateDepth16(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.allocate(
      TextureFormat.TEXTURE_FORMAT_DEPTH_16_2BPP,
      name,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public JOGLTexture2DStatic texture2DStaticAllocateDepth24(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.allocate(
      TextureFormat.TEXTURE_FORMAT_DEPTH_24_4BPP,
      name,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }
}
