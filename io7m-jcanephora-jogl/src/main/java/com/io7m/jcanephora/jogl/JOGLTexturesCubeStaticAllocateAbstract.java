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
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;

import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.jogl.JOGL_TextureSpecs.TextureSpec;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheck;

abstract class JOGLTexturesCubeStaticAllocateAbstract
{
  private final GLContext               context;
  private final GL                      gl;
  private final JOGLIntegerCacheType    icache;
  private final LogUsableType           log;
  private final JOGLLogMessageCacheType tcache;

  public JOGLTexturesCubeStaticAllocateAbstract(
    final GL in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.icache = NullCheck.notNull(in_icache, "Integer cache");
    this.tcache = NullCheck.notNull(in_tcache, "Log message cache");
    this.log = NullCheck.notNull(in_log, "Log").with("textures-cube-static");
    this.context = NullCheck.notNull(this.gl.getContext());
  }

  protected final GLContext getContext()
  {
    return this.context;
  }

  protected final GL getGL()
  {
    return this.gl;
  }

  protected final JOGLIntegerCacheType getIcache()
  {
    return this.icache;
  }

  protected final LogUsableType getLog()
  {
    return this.log;
  }

  protected final JOGLLogMessageCacheType getTcache()
  {
    return this.tcache;
  }

  protected abstract TextureSpec getTextureSpec(
    final TextureFormat type);

  protected final JOGLTextureCubeStatic textureCubeStaticAllocate(
    final String name,
    final int size,
    final TextureFormat type,
    final TextureWrapR wrap_r,
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
    NullCheck.notNull(wrap_r, "Wrap R mode");
    NullCheck.notNull(min_filter, "Minification filter");
    NullCheck.notNull(mag_filter, "Magnification filter");
    RangeCheck.checkGreaterEqual(size, "Size", 2, "Valid sizes");

    final StringBuilder text = this.getTcache().getTextCache();
    final LogUsableType logx = this.getLog();

    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      final int bytes = size * (type.getBytesPerPixel() * size) * 6;
      text.setLength(0);
      text.append("allocate \"");
      text.append(name);
      text.append("\" ");
      text.append(type);
      text.append(" ");
      text.append(size);
      text.append("x");
      text.append(size);
      text.append(" ");
      text.append(bytes);
      text.append(" bytes");
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    final IntBuffer cache = this.getIcache().getIntegerCache();
    this.gl.glGenTextures(1, cache);
    final int texture_id = cache.get(0);

    this.gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texture_id);
    this.gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_WRAP_S,
      JOGLTypeConversions.textureWrapSToGL(wrap_s));
    this.gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_WRAP_T,
      JOGLTypeConversions.textureWrapTToGL(wrap_t));
    this.gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL2ES2.GL_TEXTURE_WRAP_R,
      JOGLTypeConversions.textureWrapRToGL(wrap_r));
    this.gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_MIN_FILTER,
      JOGLTypeConversions.textureFilterMinToGL(min_filter));
    this.gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_MAG_FILTER,
      JOGLTypeConversions.textureFilterMagToGL(mag_filter));

    final TextureSpec spec = this.getTextureSpec(type);
    JOGLTextures2DStaticAbstract.setPackUnpackAlignment1(this.gl);

    for (final CubeMapFaceLH face : CubeMapFaceLH.values()) {
      assert face != null;
      final int gface = JOGLTypeConversions.cubeFaceToGL(face);

      this.gl.glTexImage2D(
        gface,
        0,
        spec.getInternalFormat(),
        size,
        size,
        0,
        spec.getFormat(),
        spec.getType(),
        null);
    }

    switch (min_filter) {
      case TEXTURE_FILTER_NEAREST:
      case TEXTURE_FILTER_LINEAR:
        break;
      case TEXTURE_FILTER_LINEAR_MIPMAP_LINEAR:
      case TEXTURE_FILTER_LINEAR_MIPMAP_NEAREST:
      case TEXTURE_FILTER_NEAREST_MIPMAP_LINEAR:
      case TEXTURE_FILTER_NEAREST_MIPMAP_NEAREST:
      {
        this.gl.glGenerateMipmap(GL.GL_TEXTURE_CUBE_MAP);
        break;
      }
    }

    this.gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);

    final JOGLTextureCubeStatic t =
      new JOGLTextureCubeStatic(
        this.getContext(),
        texture_id,
        name,
        type,
        size,
        wrap_r,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter);

    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("allocated ");
      text.append(t);
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    return t;
  }
}
