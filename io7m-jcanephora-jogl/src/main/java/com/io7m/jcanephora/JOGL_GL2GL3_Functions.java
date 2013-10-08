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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JOGL_TextureSpecs.TextureSpec;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;
import com.jogamp.common.nio.Buffers;

/**
 * Functions that are only usable on a strictly GL2 or GL3 context.
 */

final class JOGL_GL2GL3_Functions
{
  static void logicOperationsDisable(
    final @Nonnull GL2GL3 gl)
    throws JCGLException
  {
    gl.glDisable(GL.GL_COLOR_LOGIC_OP);
    JOGL_GL_Functions.checkError(gl);
  }

  static void logicOperationsEnable(
    final @Nonnull GL2GL3 gl,
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(operation, "Logic operation");
    gl.glEnable(GL.GL_COLOR_LOGIC_OP);
    gl.glLogicOp(JOGL_GLTypeConversions.logicOpToGL(operation));
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean logicOperationsEnabled(
    final @Nonnull GL2GL3 gl)
    throws JCGLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_COLOR_LOGIC_OP);
    JOGL_GL_Functions.checkError(gl);
    return e;
  }

  static void polygonSetMode(
    final @Nonnull GL2GL3 gl,
    final @Nonnull JCGLStateCache cache,
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(mode, "Polygon mode");

    final int im = JOGL_GLTypeConversions.polygonModeToGL(mode);
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, im);
    JOGL_GL_Functions.checkError(gl);
    cache.polygon_mode = mode;
  }

  static void polygonSmoothingDisable(
    final @Nonnull GL2GL3 gl)
    throws JCGLException
  {
    gl.glDisable(GL2GL3.GL_POLYGON_SMOOTH);
    JOGL_GL_Functions.checkError(gl);
  }

  static void polygonSmoothingEnable(
    final @Nonnull GL2GL3 gl)
    throws JCGLException
  {
    gl.glEnable(GL2GL3.GL_POLYGON_SMOOTH);
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean polygonSmoothingIsEnabled(
    final @Nonnull GL2GL3 gl)
    throws JCGLException
  {
    final boolean e = gl.glIsEnabled(GL2GL3.GL_POLYGON_SMOOTH);
    JOGL_GL_Functions.checkError(gl);
    return e;
  }

  static @Nonnull ProgramReference programCreateWithOutputs(
    final @Nonnull GL2GL3 g,
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull VertexShader v,
    final @Nonnull FragmentShader f,
    final @Nonnull Map<String, FramebufferDrawBuffer> outputs)
    throws ConstraintError,
      JCGLException,
      JCGLCompileException
  {
    Constraints.constrainNotNull(name, "Program name");
    Constraints.constrainNotNull(v, "Vertex shader");
    Constraints.constrainNotNull(f, "Fragment shader");
    Constraints.constrainNotNull(outputs, "Outputs");
    Constraints.constrainArbitrary(
      outputs.isEmpty() == false,
      "Draw buffer mappings not empty");
    Constraints.constrainLessThan(
      outputs.size(),
      state.draw_buffers.length,
      "Draw buffer mapping count");

    for (final Entry<String, FramebufferDrawBuffer> e : outputs.entrySet()) {
      Constraints.constrainNotNull(e.getValue(), "Draw buffer");
    }

    Constraints.constrainArbitrary(
      v.resourceIsDeleted() == false,
      "Vertex shader not deleted");
    Constraints.constrainArbitrary(
      f.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: create \"");
      state.log_text.append(name);
      state.log_text.append("\" with ");
      state.log_text.append(v);
      state.log_text.append(" ");
      state.log_text.append(f);
      log.debug(state.log_text.toString());
    }

    final int id = g.glCreateProgram();
    if (id == 0) {
      throw new JCGLException(0, "glCreateProgram failed");
    }
    JOGL_GL_Functions.checkError(g);

    g.glAttachShader(id, v.getGLName());
    JOGL_GL_Functions.checkError(g);
    g.glAttachShader(id, f.getGLName());
    JOGL_GL_Functions.checkError(g);

    for (final Entry<String, FramebufferDrawBuffer> e : outputs.entrySet()) {
      final String output = e.getKey();
      final FramebufferDrawBuffer buffer = e.getValue();
      g.glBindFragDataLocation(id, buffer.getIndex(), output);
      JOGL_GL_Functions.checkError(g);

      if (log.enabled(Level.LOG_DEBUG)) {
        state.log_text.setLength(0);
        state.log_text.append("program: bound output '");
        state.log_text.append(output);
        state.log_text.append("' to draw buffer ");
        state.log_text.append(buffer);
        log.debug(state.log_text.toString());
      }
    }

    g.glLinkProgram(id);
    JOGL_GL_Functions.checkError(g);

    final int status =
      JOGL_GL2ES2_Functions.contextGetProgramInteger(
        g,
        state,
        id,
        GL2ES2.GL_LINK_STATUS);

    if (status == 0) {
      final ByteBuffer buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      g.glGetProgramInfoLog(id, 8192, buffer_length, buffer);
      JOGL_GL_Functions.checkError(g);

      final byte raw[] = new byte[buffer.remaining()];
      buffer.get(raw);
      final String text = new String(raw);
      throw new JCGLCompileException(name, text);
    }

    JOGL_GL_Functions.checkError(g);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: created ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    final Map<String, ProgramAttribute> attributes =
      new HashMap<String, ProgramAttribute>();
    final Map<String, ProgramUniform> uniforms =
      new HashMap<String, ProgramUniform>();

    final ProgramReference program =
      new ProgramReference(id, name, uniforms, attributes);

    JOGL_GL2ES2_Functions.programGetAttributes(
      g,
      state,
      log,
      program,
      attributes);

    JOGL_GL2ES2_Functions
      .programGetUniforms(g, state, log, program, uniforms);

    return program;
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
      JCGLException
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
      final int bytes = height * (type.bytesPerPixel() * width);
      state.log_text.setLength(0);
      state.log_text.append("texture-2D-static (gl2/gl3): allocate \"");
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

    final TextureSpec spec = JOGL_TextureSpecs.getGL3TextureSpec(type);
    JOGL_GL_Functions.textureSetPackUnpackAlignment1(gl);

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
      state.log_text.append("texture-2D-static (gl2/gl3): allocated ");
      state.log_text.append(t);
      log.debug(state.log_text.toString());
    }

    return t;
  }

  static void texture2DStaticUpdate(
    final @Nonnull GL gl,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(data, "Texture data");

    final AreaInclusive area = data.targetArea();
    final Texture2DStatic texture = data.getTexture();

    final TextureType type = texture.getType();
    final int x_offset = (int) area.getRangeX().getLower();
    final int y_offset = (int) area.getRangeY().getLower();
    final int width = (int) area.getRangeX().getInterval();
    final int height = (int) area.getRangeY().getInterval();
    final TextureSpec spec = JOGL_TextureSpecs.getGL3TextureSpec(type);
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
      JCGLException
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
      final int bytes = size * (type.bytesPerPixel() * size) * 6;
      state.log_text.setLength(0);
      state.log_text.append("texture-cube-static (gl2/gl3): allocate \"");
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

    final TextureSpec spec = JOGL_TextureSpecs.getGL3TextureSpec(type);
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
      state.log_text.append("texture-cube-static (gl2/gl3): allocated ");
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
      JCGLException
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
    final TextureSpec spec = JOGL_TextureSpecs.getGL3TextureSpec(type);
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
