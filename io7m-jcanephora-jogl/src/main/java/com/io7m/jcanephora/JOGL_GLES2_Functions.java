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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JOGL_TextureSpecs.TextureSpec;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;

/**
 * Functions that are only usable on a strictly ES2 context.
 */

final class JOGL_GLES2_Functions
{
  static void depthBufferClear(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache state,
    final float depth)
    throws JCGLRuntimeException,
      ConstraintError
  {
    Constraints.constrainRange(
      JOGL_GLES2_Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glClearDepth(depth);
    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
    JOGL_GL_Functions.checkError(gl);
  }

  static void depthBufferDisable(
    final @Nonnull GL gl)
    throws JCGLRuntimeException
  {
    gl.glDisable(GL.GL_DEPTH_TEST);
    JOGL_GL_Functions.checkError(gl);
  }

  static void depthBufferEnable(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      JCGLRuntimeException
  {
    Constraints.constrainNotNull(function, "Depth function");
    Constraints.constrainRange(
      JOGL_GLES2_Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    final int d = JOGL_GLTypeConversions.depthFunctionToGL(function);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(d);
    JOGL_GL_Functions.checkError(gl);
  }

  static int depthBufferGetBits(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache state)
    throws JCGLRuntimeException
  {
    final IntBuffer cache = state.getIntegerCache();
    cache.rewind();
    gl.glGetIntegerv(GL.GL_DEPTH_BITS, cache);
    JOGL_GL_Functions.checkError(gl);
    return cache.get(0);
  }

  static boolean depthBufferIsEnabled(
    final @Nonnull GL gl)
    throws JCGLRuntimeException
  {
    final boolean e = gl.glIsEnabled(GL.GL_DEPTH_TEST);
    JOGL_GL_Functions.checkError(gl);
    return e;
  }

  static void depthBufferWriteDisable(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache state)
    throws ConstraintError,
      JCGLRuntimeException
  {
    Constraints.constrainRange(
      JOGL_GLES2_Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glDepthMask(false);
    JOGL_GL_Functions.checkError(gl);
  }

  static void depthBufferWriteEnable(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache state)
    throws ConstraintError,
      JCGLRuntimeException
  {
    Constraints.constrainRange(
      JOGL_GLES2_Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glDepthMask(true);
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean depthBufferWriteIsEnabled(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache state)
    throws JCGLRuntimeException
  {
    final ByteBuffer cache = state.getDepthMaskCache();
    gl.glGetBooleanv(GL.GL_DEPTH_WRITEMASK, cache);
    JOGL_GL_Functions.checkError(gl);

    final IntBuffer bi = cache.asIntBuffer();
    return bi.get(0) == 1;
  }

  static void stencilBufferClear(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache state,
    final int stencil)
    throws JCGLRuntimeException,
      ConstraintError
  {
    Constraints.constrainRange(
      JOGL_GLES2_Functions.stencilBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    gl.glClearStencil(stencil);
    JOGL_GL_Functions.checkError(gl);
  }

  static void stencilBufferDisable(
    final @Nonnull GL gl)
    throws JCGLRuntimeException
  {
    gl.glDisable(GL.GL_STENCIL_TEST);
    JOGL_GL_Functions.checkError(gl);
  }

  static void stencilBufferEnable(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache state)
    throws ConstraintError,
      JCGLRuntimeException
  {
    Constraints.constrainRange(
      JOGL_GLES2_Functions.stencilBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    gl.glEnable(GL.GL_STENCIL_TEST);
    JOGL_GL_Functions.checkError(gl);
  }

  static int stencilBufferGetBits(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache state)
    throws JCGLRuntimeException
  {
    final IntBuffer cache = state.getIntegerCache();
    cache.rewind();
    gl.glGetIntegerv(GL.GL_STENCIL_BITS, cache);
    JOGL_GL_Functions.checkError(gl);
    return cache.get(0);
  }

  static boolean stencilBufferIsEnabled(
    final @Nonnull GL gl)
    throws JCGLRuntimeException
  {
    final boolean e = gl.glIsEnabled(GL.GL_STENCIL_TEST);
    JOGL_GL_Functions.checkError(gl);
    return e;
  }

  static @Nonnull Texture2DStatic texture2DStaticAllocate(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLRuntimeException
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainRange(width, 2, Integer.MAX_VALUE, "Width");
    Constraints.constrainRange(height, 2, Integer.MAX_VALUE, "Height");
    Constraints.constrainNotNull(type, "Texture type");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(min_filter, "Minification filter");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");

    if (log.enabled(Level.LOG_DEBUG)) {
      final int bytes = height * (type.getBytesPerPixel() * width);
      state.log_text.setLength(0);
      state.log_text.append("texture-2D-static (es2): allocate \"");
      state.log_text.append(name);
      state.log_text.append("\" ");
      state.log_text.append(type);
      state.log_text.append(" ");
      state.log_text.append(width);
      state.log_text.append("x");
      state.log_text.append(height);
      state.log_text.append(" ");
      state.log_text.append(bytes);
      state.log_text.append(" bytes");
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    gl.glGenTextures(1, cache);
    JOGL_GL_Functions.checkError(gl);
    final int texture_id = cache.get(0);

    gl.glBindTexture(GL.GL_TEXTURE_2D, texture_id);
    JOGL_GL_Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_S,
      JOGL_GLTypeConversions.textureWrapSToGL(wrap_s));
    JOGL_GL_Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_T,
      JOGL_GLTypeConversions.textureWrapTToGL(wrap_t));
    JOGL_GL_Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MIN_FILTER,
      JOGL_GLTypeConversions.textureFilterMinToGL(min_filter));
    JOGL_GL_Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MAG_FILTER,
      JOGL_GLTypeConversions.textureFilterMagToGL(mag_filter));
    JOGL_GL_Functions.checkError(gl);

    final TextureSpec spec = JOGL_TextureSpecs.getGLES2TextureSpec(type);
    JOGL_GL_Functions.textureSetPackUnpackAlignment1(gl);

    gl.glTexImage2D(
      GL.GL_TEXTURE_2D,
      0,
      spec.internal_format,
      width,
      height,
      0,
      spec.format,
      spec.type,
      null);
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    JOGL_GL_Functions.checkError(gl);

    final Texture2DStatic t =
      new Texture2DStatic(
        name,
        type,
        texture_id,
        width,
        height,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("texture-2D-static (es2): allocated ");
      state.log_text.append(t);
      log.debug(state.log_text.toString());
    }

    return t;
  }

  static void texture2DStaticUpdate(
    final @Nonnull GL gl,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      JCGLRuntimeException
  {
    Constraints.constrainNotNull(data, "Texture data");

    final AreaInclusive area = data.targetArea();
    final Texture2DStatic texture = data.getTexture();

    final TextureType type = texture.getType();
    final int x_offset = (int) area.getRangeX().getLower();
    final int y_offset = (int) area.getRangeY().getLower();
    final int width = (int) area.getRangeX().getInterval();
    final int height = (int) area.getRangeY().getInterval();
    final TextureSpec spec = JOGL_TextureSpecs.getGLES2TextureSpec(type);
    final ByteBuffer buffer = data.targetData();

    gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getGLName());
    gl.glTexSubImage2D(
      GL.GL_TEXTURE_2D,
      0,
      x_offset,
      y_offset,
      width,
      height,
      spec.format,
      spec.type,
      buffer);
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    JOGL_GL_Functions.checkError(gl);
  }

  static @Nonnull TextureCubeStatic textureCubeStaticAllocate(
    final @Nonnull GL gl,
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLRuntimeException
  {
    Constraints.constrainNotNull(name, "Name");
    Constraints.constrainRange(size, 2, Integer.MAX_VALUE, "Size");
    Constraints.constrainNotNull(type, "Texture type");
    Constraints.constrainNotNull(wrap_s, "Wrap S mode");
    Constraints.constrainNotNull(wrap_t, "Wrap T mode");
    Constraints.constrainNotNull(wrap_r, "Wrap R mode");
    Constraints.constrainNotNull(min_filter, "Minification filter");
    Constraints.constrainNotNull(mag_filter, "Magnification filter");

    if (log.enabled(Level.LOG_DEBUG)) {
      final int bytes = size * (type.getBytesPerPixel() * size) * 6;
      state.log_text.setLength(0);
      state.log_text.append("texture-cube-static (es2): allocate \"");
      state.log_text.append(name);
      state.log_text.append("\" ");
      state.log_text.append(type);
      state.log_text.append(" ");
      state.log_text.append(size);
      state.log_text.append("x");
      state.log_text.append(size);
      state.log_text.append(" ");
      state.log_text.append(bytes);
      state.log_text.append(" bytes");
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    gl.glGenTextures(1, cache);
    JOGL_GL_Functions.checkError(gl);
    final int texture_id = cache.get(0);

    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texture_id);
    JOGL_GL_Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_WRAP_S,
      JOGL_GLTypeConversions.textureWrapSToGL(wrap_s));
    JOGL_GL_Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_WRAP_T,
      JOGL_GLTypeConversions.textureWrapTToGL(wrap_t));
    JOGL_GL_Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL2ES2.GL_TEXTURE_WRAP_R,
      JOGL_GLTypeConversions.textureWrapRToGL(wrap_r));
    JOGL_GL_Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_MIN_FILTER,
      JOGL_GLTypeConversions.textureFilterMinToGL(min_filter));
    JOGL_GL_Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_MAG_FILTER,
      JOGL_GLTypeConversions.textureFilterMagToGL(mag_filter));
    JOGL_GL_Functions.checkError(gl);

    final TextureSpec spec = JOGL_TextureSpecs.getGLES2TextureSpec(type);
    JOGL_GL_Functions.textureSetPackUnpackAlignment1(gl);

    for (final CubeMapFaceLH face : CubeMapFaceLH.values()) {
      final int gface = JOGL_GLTypeConversions.cubeFaceToGL(face);
      gl.glTexImage2D(
        gface,
        0,
        spec.internal_format,
        size,
        size,
        0,
        spec.format,
        spec.type,
        null);
      JOGL_GL_Functions.checkError(gl);
    }

    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
    JOGL_GL_Functions.checkError(gl);

    final TextureCubeStatic t =
      new TextureCubeStatic(
        name,
        type,
        texture_id,
        size,
        wrap_r,
        wrap_s,
        wrap_t,
        min_filter,
        mag_filter);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("texture-cube-static (es2): allocated ");
      state.log_text.append(t);
      log.debug(state.log_text.toString());
    }

    return t;
  }

  static void textureCubeStaticUpdate(
    final @Nonnull GL gl,
    final @Nonnull CubeMapFaceLH face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      JCGLRuntimeException
  {
    Constraints.constrainNotNull(face, "Cube map face");
    Constraints.constrainNotNull(data, "Texture data");

    final AreaInclusive area = data.targetArea();
    final TextureCubeStatic texture = data.getTexture();

    final TextureType type = texture.getType();
    final int x_offset = (int) area.getRangeX().getLower();
    final int y_offset = (int) area.getRangeY().getLower();
    final int width = (int) area.getRangeX().getInterval();
    final int height = (int) area.getRangeY().getInterval();
    final TextureSpec spec = JOGL_TextureSpecs.getGLES2TextureSpec(type);
    final ByteBuffer buffer = data.targetData();
    final int gface = JOGL_GLTypeConversions.cubeFaceToGL(face);

    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texture.getGLName());
    gl.glTexSubImage2D(
      gface,
      0,
      x_offset,
      y_offset,
      width,
      height,
      spec.format,
      spec.type,
      buffer);
    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
    JOGL_GL_Functions.checkError(gl);
  }

}
