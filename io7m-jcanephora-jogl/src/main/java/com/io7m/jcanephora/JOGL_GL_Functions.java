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
import javax.media.opengl.GL2ES3;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;
import com.jogamp.common.util.VersionNumber;

final class JOGL_GL_Functions
{
  static final @Nonnull ArrayBuffer arrayBufferAllocate(
    final @Nonnull GL gl,
    final @Nonnull Log log,
    final @Nonnull GLStateCache state,
    final long elements,
    final @Nonnull ArrayBufferDescriptor descriptor,
    final @Nonnull UsageHint usage)
    throws GLException,
      ConstraintError
  {
    Constraints
      .constrainRange(elements, 1, Long.MAX_VALUE, "Buffer elements");
    Constraints.constrainNotNull(descriptor, "Buffer descriptor");
    Constraints.constrainNotNull(usage, "Usage hint");

    final long size = descriptor.getSize();
    final long bytes_total = elements * size;

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: allocate (");
      state.log_text.append(elements);
      state.log_text.append(" elements, ");
      state.log_text.append(size);
      state.log_text.append(" bytes per element, ");
      state.log_text.append(bytes_total);
      state.log_text.append(" bytes, usage ");
      state.log_text.append(usage);
      state.log_text.append("))");
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    gl.glGenBuffers(1, cache);

    final int id = cache.get(0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
    gl.glBufferData(
      GL.GL_ARRAY_BUFFER,
      bytes_total,
      null,
      JOGL_GLTypeConversions.usageHintES2ToGL(usage));

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    JOGL_GL_Functions.checkError(gl);
    return new ArrayBuffer(id, elements, descriptor);
  }

  static void arrayBufferBind(
    final @Nonnull GL gl,
    final @Nonnull ArrayBuffer buffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer.getGLName());
    JOGL_GL_Functions.checkError(gl);
  }

  static void arrayBufferDelete(
    final @Nonnull GL gl,
    final @Nonnull Log log,
    final @Nonnull GLStateCache state,
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: delete ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, id.getGLName());

    gl.glDeleteBuffers(1, cache);
    id.resourceSetDeleted();
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean arrayBufferIsBound(
    final @Nonnull GL gl,
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final int b = gl.glGetBoundBuffer(GL.GL_ARRAY_BUFFER);
    JOGL_GL_Functions.checkError(gl);
    return b == id.getGLName();
  }

  static ByteBuffer arrayBufferMapRead(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Array buffer");
    return JOGL_GL_Functions.arrayBufferMapReadRange(
      gl,
      state,
      log,
      id,
      id.getRange());
  }

  static ByteBuffer arrayBufferMapReadRange(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer id,
    final @Nonnull RangeInclusive range)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");
    Constraints.constrainNotNull(range, "Range");

    Constraints.constrainNotNull(range, "Range");
    Constraints.constrainArbitrary(
      range.isIncludedIn(id.getRange()),
      "Mapped range included in buffer range");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: map ");
      state.log_text.append(id);
      state.log_text.append(" range ");
      state.log_text.append(range);
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
    JOGL_GL_Functions.checkError(gl);

    final long offset = range.getLower() * id.getElementSizeBytes();
    final long length = range.getInterval() * id.getElementSizeBytes();

    final ByteBuffer b =
      gl.glMapBufferRange(
        GL.GL_ARRAY_BUFFER,
        offset,
        length,
        GL.GL_MAP_READ_BIT);
    JOGL_GL_Functions.checkError(gl);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    JOGL_GL_Functions.checkError(gl);

    return b;
  }

  static ArrayBufferWritableMap arrayBufferMapWrite(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: map ");
      state.log_text.append(id);
      state.log_text.append(" range ");
      state.log_text.append(id.getRange());
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
    JOGL_GL_Functions.checkError(gl);

    gl.glBufferData(
      GL.GL_ARRAY_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    JOGL_GL_Functions.checkError(gl);

    final RangeInclusive range = id.getRange();
    final long offset = range.getLower() * id.getElementSizeBytes();
    final long length = range.getInterval() * id.getElementSizeBytes();

    final ByteBuffer b =
      gl.glMapBufferRange(
        GL.GL_ARRAY_BUFFER,
        offset,
        length,
        GL.GL_MAP_WRITE_BIT);
    JOGL_GL_Functions.checkError(gl);

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    JOGL_GL_Functions.checkError(gl);

    return new ArrayBufferWritableMap(id, b);
  }

  static void arrayBufferUnbind(
    final @Nonnull GL gl)
    throws GLException
  {
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    JOGL_GL_Functions.checkError(gl);
  }

  static void arrayBufferUnmap(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: unmap ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
    gl.glUnmapBuffer(GL.GL_ARRAY_BUFFER);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    JOGL_GL_Functions.checkError(gl);
  }

  static void arrayBufferUpdate(
    final @Nonnull GL gl,
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferWritableData data)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainNotNull(data, "Array data");

    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final boolean bound = JOGL_GL_Functions.arrayBufferIsBound(gl, buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    gl.glBufferSubData(
      GL.GL_ARRAY_BUFFER,
      data.getTargetDataOffset(),
      data.getTargetDataSize(),
      data.getTargetData());
    JOGL_GL_Functions.checkError(gl);
  }

  static void blendingDisable(
    final @Nonnull GL gl)
    throws GLException
  {
    gl.glDisable(GL.GL_BLEND);
    JOGL_GL_Functions.checkError(gl);
  }

  static void blendingEnable(
    final @Nonnull GL gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableSeparate(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor);
  }

  static void blendingEnableSeparate(
    final @Nonnull GL gl,
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparateES2(
      gl,
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      BlendEquationGLES2.BLEND_EQUATION_ADD,
      BlendEquationGLES2.BLEND_EQUATION_ADD);
  }

  static void blendingEnableSeparateWithEquationSeparate(
    final @Nonnull GL gl,
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquationGL3 equation_rgb,
    final @Nonnull BlendEquationGL3 equation_alpha)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(source_rgb_factor, "Source RGB factor");
    Constraints.constrainNotNull(source_alpha_factor, "Source alpha factor");
    Constraints.constrainNotNull(
      destination_rgb_factor,
      "Destination RGB factor");
    Constraints.constrainNotNull(
      destination_alpha_factor,
      "Destination alpha factor");
    Constraints.constrainNotNull(equation_rgb, "Equation RGB");
    Constraints.constrainNotNull(equation_alpha, "Equation alpha");

    Constraints.constrainArbitrary(
      destination_rgb_factor != BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      "Destination RGB factor not SOURCE_ALPHA_SATURATE");
    Constraints.constrainArbitrary(
      destination_alpha_factor != BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      "Destination alpha factor not SOURCE_ALPHA_SATURATE");

    gl.glEnable(GL.GL_BLEND);
    gl.glBlendEquationSeparate(
      JOGL_GLTypeConversions.blendEquationToGL(equation_rgb),
      JOGL_GLTypeConversions.blendEquationToGL(equation_alpha));
    gl.glBlendFuncSeparate(
      JOGL_GLTypeConversions.blendFunctionToGL(source_rgb_factor),
      JOGL_GLTypeConversions.blendFunctionToGL(destination_rgb_factor),
      JOGL_GLTypeConversions.blendFunctionToGL(source_alpha_factor),
      JOGL_GLTypeConversions.blendFunctionToGL(destination_alpha_factor));
    JOGL_GL_Functions.checkError(gl);
  }

  static void blendingEnableSeparateWithEquationSeparateES2(
    final @Nonnull GL gl,
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquationGLES2 equation_rgb,
    final @Nonnull BlendEquationGLES2 equation_alpha)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(source_rgb_factor, "Source RGB factor");
    Constraints.constrainNotNull(source_alpha_factor, "Source alpha factor");
    Constraints.constrainNotNull(
      destination_rgb_factor,
      "Destination RGB factor");
    Constraints.constrainNotNull(
      destination_alpha_factor,
      "Destination alpha factor");
    Constraints.constrainNotNull(equation_rgb, "Equation RGB");
    Constraints.constrainNotNull(equation_alpha, "Equation alpha");

    Constraints.constrainArbitrary(
      destination_rgb_factor != BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      "Destination RGB factor not SOURCE_ALPHA_SATURATE");
    Constraints.constrainArbitrary(
      destination_alpha_factor != BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      "Destination alpha factor not SOURCE_ALPHA_SATURATE");

    gl.glEnable(GL.GL_BLEND);
    gl.glBlendEquationSeparate(
      JOGL_GLTypeConversions.blendEquationES2ToGL(equation_rgb),
      JOGL_GLTypeConversions.blendEquationES2ToGL(equation_alpha));
    gl.glBlendFuncSeparate(
      JOGL_GLTypeConversions.blendFunctionToGL(source_rgb_factor),
      JOGL_GLTypeConversions.blendFunctionToGL(destination_rgb_factor),
      JOGL_GLTypeConversions.blendFunctionToGL(source_alpha_factor),
      JOGL_GLTypeConversions.blendFunctionToGL(destination_alpha_factor));
    JOGL_GL_Functions.checkError(gl);
  }

  static void blendingEnableWithEquation(
    final @Nonnull GL gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGL3 equation)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparate(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  static void blendingEnableWithEquationES2(
    final @Nonnull GL gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparateES2(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  static void blendingEnableWithEquationSeparate(
    final @Nonnull GL gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGL3 equation_rgb,
    final @Nonnull BlendEquationGL3 equation_alpha)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparate(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  static void blendingEnableWithEquationSeparateES2(
    final @Nonnull GL gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation_rgb,
    final @Nonnull BlendEquationGLES2 equation_alpha)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparateES2(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  static boolean blendingIsEnabled(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(GL.GL_BLEND, cache);
    JOGL_GL_Functions.checkError(gl);
    return cache.get(0) == GL.GL_TRUE;
  }

  static void checkError(
    final @Nonnull GL gl)
    throws GLException
  {
    final int code = gl.glGetError();

    if (code != 0) {
      throw new GLException(code, "OpenGL error: code " + code);
    }
  }

  static void colorBufferClear3f(
    final @Nonnull GL gl,
    final float r,
    final float g,
    final float b)
    throws GLException
  {
    gl.glClearColor(r, g, b, 1.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    JOGL_GL_Functions.checkError(gl);
  }

  static void colorBufferClear4f(
    final @Nonnull GL gl,
    final float r,
    final float g,
    final float b,
    final float a)
    throws GLException
  {
    gl.glClearColor(r, g, b, a);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    JOGL_GL_Functions.checkError(gl);
  }

  static void colorBufferClearV3f(
    final @Nonnull GL gl,
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(color, "Color vector");
    JOGL_GL_Functions.colorBufferClear3f(
      gl,
      color.getXF(),
      color.getYF(),
      color.getZF());
  }

  static void colorBufferClearV4f(
    final @Nonnull GL gl,
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(color, "Color vector");
    JOGL_GL_Functions.colorBufferClear4f(
      gl,
      color.getXF(),
      color.getYF(),
      color.getZF(),
      color.getWF());
  }

  static void colorBufferMask(
    final @Nonnull GL gl,
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws GLException
  {
    gl.glColorMask(r, g, b, a);
    JOGL_GL_Functions.checkError(gl);
  }

  private static final ByteBuffer colorBufferMaskStatus(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final ByteBuffer cache = state.getColorMaskCache();
    gl.glGetBooleanv(GL.GL_COLOR_WRITEMASK, cache);
    JOGL_GL_Functions.checkError(gl);
    return cache;
  }

  static boolean colorBufferMaskStatusAlpha(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int a = JOGL_GL_Functions.colorBufferMaskStatus(gl, state).get(3);
    return a != 0;
  }

  static boolean colorBufferMaskStatusBlue(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int b = JOGL_GL_Functions.colorBufferMaskStatus(gl, state).get(2);
    return b != 0;
  }

  static boolean colorBufferMaskStatusGreen(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int g = JOGL_GL_Functions.colorBufferMaskStatus(gl, state).get(1);
    return g != 0;
  }

  static boolean colorBufferMaskStatusRed(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int r = JOGL_GL_Functions.colorBufferMaskStatus(gl, state).get(0);
    return r != 0;
  }

  static int contextGetInteger(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final int name)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(name, cache);
    JOGL_GL_Functions.checkError(gl);
    return cache.get(0);
  }

  static void cullingDisable(
    final @Nonnull GL gl)
    throws GLException
  {
    gl.glDisable(GL.GL_CULL_FACE);
    JOGL_GL_Functions.checkError(gl);
  }

  static void cullingEnable(
    final @Nonnull GL gl,
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(order, "Face winding order");

    final int fi = JOGL_GLTypeConversions.faceSelectionToGL(faces);
    final int oi = JOGL_GLTypeConversions.faceWindingOrderToGL(order);

    gl.glEnable(GL.GL_CULL_FACE);
    gl.glCullFace(fi);
    gl.glFrontFace(oi);
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean cullingIsEnabled(
    final @Nonnull GL gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_CULL_FACE);
    JOGL_GL_Functions.checkError(gl);
    return e;
  }

  static void depthBufferClear(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final float depth)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainRange(
      JOGL_GL_Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glClearDepth(depth);
    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
    JOGL_GL_Functions.checkError(gl);
  }

  static void depthBufferDisable(
    final @Nonnull GL gl)
    throws GLException
  {
    gl.glDisable(GL.GL_DEPTH_TEST);
    JOGL_GL_Functions.checkError(gl);
  }

  static void depthBufferEnable(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(function, "Depth function");
    Constraints.constrainRange(
      JOGL_GL_Functions.depthBufferGetBits(gl, state),
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
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int bits =
      JOGL_GL_Functions.contextGetInteger(gl, state, GL.GL_DEPTH_BITS);
    JOGL_GL_Functions.checkError(gl);
    return bits;
  }

  static boolean depthBufferIsEnabled(
    final @Nonnull GL gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_DEPTH_TEST);
    JOGL_GL_Functions.checkError(gl);
    return e;
  }

  static void depthBufferWriteDisable(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      JOGL_GL_Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glDepthMask(false);
    JOGL_GL_Functions.checkError(gl);
  }

  static void depthBufferWriteEnable(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      JOGL_GL_Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glDepthMask(true);
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean depthBufferWriteIsEnabled(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final ByteBuffer cache = state.getDepthMaskCache();
    gl.glGetBooleanv(GL.GL_DEPTH_WRITEMASK, cache);
    JOGL_GL_Functions.checkError(gl);

    final IntBuffer bi = cache.asIntBuffer();
    return bi.get(0) == 1;
  }

  static void drawElements(
    final @Nonnull GL gl,
    final @Nonnull Primitives mode,
    final @Nonnull IndexBuffer indices)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(mode, "Drawing mode");
    Constraints.constrainNotNull(indices, "Index buffer");
    Constraints.constrainArbitrary(
      indices.resourceIsDeleted() == false,
      "Index buffer not deleted");

    final int index_id = indices.getGLName();
    final int index_count = (int) indices.getRange().getInterval();
    final int mode_gl = JOGL_GLTypeConversions.primitiveToGL(mode);
    final int type =
      JOGL_GLTypeConversions.unsignedTypeToGL(indices.getType());

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, index_id);
    gl.glDrawElements(mode_gl, index_count, type, 0L);
    JOGL_GL_Functions.checkError(gl);
  }

  static @Nonnull FramebufferReference framebufferAllocate(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGenFramebuffers(1, cache);
    JOGL_GL_Functions.checkError(gl);
    final int id = cache.get(0);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    return new FramebufferReference(id);
  }

  static void framebufferDelete(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference buffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer: delete ");
      state.log_text.append(buffer);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, buffer.getGLName());
    gl.glDeleteFramebuffers(1, cache);
    JOGL_GL_Functions.checkError(gl);
    buffer.resourceSetDeleted();
  }

  static boolean framebufferDrawAnyIsBound(
    final @Nonnull GL gl)
  {
    final int bound = gl.getBoundFramebuffer(GL.GL_FRAMEBUFFER);
    final int default_fb = gl.getDefaultDrawFramebuffer();
    return bound != default_fb;
  }

  static void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isColorRenderable(),
      "Renderbuffer is color renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at color attachment 0");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    JOGL_GL_Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorRenderbufferAt(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isColorRenderable(),
      "Renderbuffer is color renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at color attachment ");
      state.log_text.append(point);
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferRenderbuffer(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0 + point.getIndex(),
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    JOGL_GL_Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTexture2D(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isColorRenderable(),
      "Texture is color renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" at color attachment 0");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferTexture2D(
      GL.GL_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      GL.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    JOGL_GL_Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTexture2DAt(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isColorRenderable(),
      "Texture is color renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" at color attachment ");
      state.log_text.append(point);
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferTexture2D(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0 + point.getIndex(),
      GL.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    JOGL_GL_Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTextureCube(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFace face)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isColorRenderable(),
      "Texture is color renderable");

    Constraints.constrainNotNull(face, "Cube map face");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" at color attachment 0");
      log.debug(state.log_text.toString());
    }

    final int gface = JOGL_GLTypeConversions.cubeFaceToGL(face);
    gl.glFramebufferTexture2D(
      GL.GL_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      gface,
      texture.getGLName(),
      0);
    JOGL_GL_Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTextureCubeAt(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFace face)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isColorRenderable(),
      "Texture is color renderable");

    Constraints.constrainNotNull(face, "Cube map face");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" face ");
      state.log_text.append(face);
      state.log_text.append(" at color attachment ");
      state.log_text.append(point);
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferTexture2D(
      GL2ES3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0 + point.getIndex(),
      JOGL_GLTypeConversions.cubeFaceToGL(face),
      texture.getGLName(),
      0);
    JOGL_GL_Functions.checkError(gl);
  }

  static void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepth> renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isDepthRenderable(),
      "Renderbuffer is depth renderable");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isStencilRenderable() == false,
      "Renderbuffer is not also stencil renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at depth attachment");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    JOGL_GL_Functions.checkError(gl);
  }

  /**
   * Available as an extension in ES2 (OES_packed_depth_stencil).
   */

  static void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");

    final RenderbufferType type = renderbuffer.getType();
    Constraints.constrainArbitrary(
      type.isDepthRenderable(),
      "Renderbuffer is depth renderable");
    Constraints.constrainArbitrary(
      type.isStencilRenderable(),
      "Renderbuffer is stencil renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at depth+stencil attachment");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    gl.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    JOGL_GL_Functions.checkError(gl);
  }

  static void framebufferDrawAttachDepthTexture2D(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isDepthRenderable(),
      "Texture is depth renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" at depth attachment");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferTexture2D(
      GL.GL_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    JOGL_GL_Functions.checkError(gl);
  }

  static void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableStencil> renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(renderbuffer
      .getType()
      .isStencilRenderable(), "Renderbuffer is stencil renderable");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isDepthRenderable() == false,
      "Renderbuffer is not also depth renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at stencil attachment");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferRenderbuffer(
      GL.GL_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    JOGL_GL_Functions.checkError(gl);
  }

  static void framebufferDrawBind(
    final @Nonnull GL gl,
    final @Nonnull FramebufferReference buffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, buffer.getGLName());
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean framebufferDrawIsBound(
    final @Nonnull GL gl,
    final @Nonnull FramebufferReference framebuffer)
    throws ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    final int bound = gl.getBoundFramebuffer(GL.GL_FRAMEBUFFER);
    return bound == framebuffer.getGLName();
  }

  static void framebufferDrawUnbind(
    final @Nonnull GL gl)
    throws GLException
  {
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    JOGL_GL_Functions.checkError(gl);
  }

  static @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull GL gl,
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      JOGL_GL_Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    final int status = gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
    JOGL_GL_Functions.checkError(gl);

    return JOGL_GLTypeConversions.framebufferStatusFromGL(status);
  }

  static @Nonnull
    FramebufferColorAttachmentPoint[]
    framebufferGetAttachmentPointsActual(
      final @Nonnull GL gl,
      final @Nonnull GLStateCache state,
      final @Nonnull Log log)
      throws GLException
  {
    final int max =
      JOGL_GL_Functions.contextGetInteger(
        gl,
        state,
        GL2ES2.GL_MAX_COLOR_ATTACHMENTS);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("implementation supports ");
      state.log_text.append(max);
      state.log_text.append(" framebuffer color attachments");
      log.debug(state.log_text.toString());
    }

    final FramebufferColorAttachmentPoint[] a =
      new FramebufferColorAttachmentPoint[max];
    for (int index = 0; index < max; ++index) {
      a[index] = new FramebufferColorAttachmentPoint(index);
    }

    return a;
  }

  static @Nonnull FramebufferDrawBuffer[] framebufferGetDrawBuffersActual(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log)
    throws GLException
  {
    final int max =
      JOGL_GL_Functions.contextGetInteger(
        gl,
        state,
        GL2ES2.GL_MAX_DRAW_BUFFERS);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("implementation supports ");
      state.log_text.append(max);
      state.log_text.append(" framebuffer draw buffers");
      log.debug(state.log_text.toString());
    }

    final FramebufferDrawBuffer[] b = new FramebufferDrawBuffer[max];
    for (int index = 0; index < max; ++index) {
      b[index] = new FramebufferDrawBuffer(index);
    }

    return b;
  }

  static IndexBuffer indexBufferAllocate(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer buffer,
    final int indices)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");
    Constraints.constrainRange(indices, 1, Integer.MAX_VALUE);

    GLUnsignedType type = GLUnsignedType.TYPE_UNSIGNED_BYTE;
    if (buffer.getRange().getInterval() > 0xff) {
      type = GLUnsignedType.TYPE_UNSIGNED_SHORT;
    }
    if (buffer.getRange().getInterval() > 0xffff) {
      type = GLUnsignedType.TYPE_UNSIGNED_INT;
    }

    return JOGL_GL_Functions.indexBufferAllocateType(
      gl,
      state,
      log,
      type,
      indices);
  }

  static @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull GLUnsignedType type,
    final int indices)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(type, "Index type");
    Constraints.constrainRange(indices, 1, Integer.MAX_VALUE);

    final long size = type.getSizeBytes();
    final long bytes_total = indices * size;

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: allocate (");
      state.log_text.append(indices);
      state.log_text.append(" elements, ");
      state.log_text.append(size);
      state.log_text.append(" bytes per element, ");
      state.log_text.append(bytes_total);
      state.log_text.append(" bytes)");
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    gl.glGenBuffers(1, cache);
    JOGL_GL_Functions.checkError(gl);

    final int id = cache.get(0);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id);
    JOGL_GL_Functions.checkError(gl);
    gl.glBufferData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      bytes_total,
      null,
      GL2ES2.GL_STREAM_DRAW);
    JOGL_GL_Functions.checkError(gl);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    return new IndexBuffer(id, new RangeInclusive(0, indices - 1), type);
  }

  static void indexBufferDelete(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: delete ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, id.getGLName());
    gl.glDeleteBuffers(1, cache);
    id.resourceSetDeleted();
    JOGL_GL_Functions.checkError(gl);
  }

  static @Nonnull IndexBufferReadableMap indexBufferMapRead(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Index buffer");
    return JOGL_GL_Functions.indexBufferMapReadRange(
      gl,
      state,
      log,
      id,
      id.getRange());
  }

  static IndexBufferReadableMap indexBufferMapReadRange(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id,
    final @Nonnull RangeInclusive range)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");
    Constraints.constrainNotNull(range, "Range");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    JOGL_GL_Functions.checkError(gl);

    final long offset = range.getLower() * id.getElementSizeBytes();
    final long length = range.getInterval() * id.getElementSizeBytes();

    final ByteBuffer b =
      gl.glMapBufferRange(
        GL.GL_ELEMENT_ARRAY_BUFFER,
        offset,
        length,
        GL.GL_MAP_READ_BIT);
    JOGL_GL_Functions.checkError(gl);

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    JOGL_GL_Functions.checkError(gl);

    return new IndexBufferReadableMap(id, b);
  }

  static IndexBufferWritableMap indexBufferMapWrite(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    JOGL_GL_Functions.checkError(gl);
    gl.glBufferData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    JOGL_GL_Functions.checkError(gl);

    final RangeInclusive range = id.getRange();
    final long offset = range.getLower() * id.getElementSizeBytes();
    final long length = range.getInterval() * id.getElementSizeBytes();

    final ByteBuffer b =
      gl.glMapBufferRange(
        GL.GL_ELEMENT_ARRAY_BUFFER,
        offset,
        length,
        GL.GL_MAP_WRITE_BIT);
    JOGL_GL_Functions.checkError(gl);

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    JOGL_GL_Functions.checkError(gl);

    return new IndexBufferWritableMap(id, b);
  }

  static void indexBufferUnmap(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: unmap ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    gl.glUnmapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    JOGL_GL_Functions.checkError(gl);
  }

  static void indexBufferUpdate(
    final @Nonnull GL gl,
    final @Nonnull IndexBuffer buffer,
    final @Nonnull IndexBufferWritableData data)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Index buffer");
    Constraints.constrainNotNull(data, "Index data");

    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Index buffer not deleted");

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, buffer.getGLName());
    gl.glBufferSubData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      data.getTargetDataOffset(),
      data.getTargetDataSize(),
      data.getTargetData());
    JOGL_GL_Functions.checkError(gl);
  }

  static int metaGetError(
    final @Nonnull GL gl)
  {
    return gl.glGetError();
  }

  static String metaGetRenderer(
    final @Nonnull GL gl)
    throws GLException
  {
    final String x = gl.glGetString(GL.GL_RENDERER);
    JOGL_GL_Functions.checkError(gl);
    return x;
  }

  static String metaGetVendor(
    final @Nonnull GL gl)
    throws GLException
  {
    final String x = gl.glGetString(GL.GL_VENDOR);
    JOGL_GL_Functions.checkError(gl);
    return x;
  }

  static String metaGetVersion(
    final @Nonnull GL gl)
    throws GLException
  {
    final String x = gl.glGetString(GL.GL_VERSION);
    JOGL_GL_Functions.checkError(gl);
    return x;
  }

  static int metaGetVersionMajor(
    final GL gl)
  {
    final VersionNumber number = gl.getContext().getGLVersionNumber();
    return number.getMajor();
  }

  static int metaGetVersionMinor(
    final GL gl)
  {
    final VersionNumber number = gl.getContext().getGLVersionNumber();
    return number.getMinor();
  }

  static boolean metaIsES(
    final GL gl)
  {
    return gl.isGLES();
  }

  static void scissorDisable(
    final @Nonnull GL gl)
    throws GLException
  {
    gl.glDisable(GL.GL_SCISSOR_TEST);
    JOGL_GL_Functions.checkError(gl);
  }

  static void scissorEnable(
    final @Nonnull GL gl,
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(position, "Scissor region position");
    Constraints.constrainNotNull(dimensions, "Scissor region dimensions");
    Constraints.constrainRange(
      dimensions.getXI(),
      0,
      Integer.MAX_VALUE,
      "Scissor width");
    Constraints.constrainRange(
      dimensions.getYI(),
      0,
      Integer.MAX_VALUE,
      "Scissor height");

    gl.glEnable(GL.GL_SCISSOR_TEST);
    gl.glScissor(
      position.getXI(),
      position.getYI(),
      dimensions.getXI(),
      dimensions.getYI());
    JOGL_GL_Functions.checkError(gl);
  }

  static boolean scissorIsEnabled(
    final @Nonnull GL gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_SCISSOR_TEST);
    JOGL_GL_Functions.checkError(gl);
    return e;
  }

  static void stencilBufferClear(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final int stencil)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainRange(
      JOGL_GL_Functions.stencilBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    gl.glClearStencil(stencil);
    JOGL_GL_Functions.checkError(gl);
  }

  static void stencilBufferDisable(
    final @Nonnull GL gl)
    throws GLException
  {
    gl.glDisable(GL.GL_STENCIL_TEST);
    JOGL_GL_Functions.checkError(gl);
  }

  static void stencilBufferEnable(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      JOGL_GL_Functions.stencilBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    gl.glEnable(GL.GL_STENCIL_TEST);
    JOGL_GL_Functions.checkError(gl);
  }

  static int stencilBufferGetBits(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int bits =
      JOGL_GL_Functions.contextGetInteger(gl, state, GL.GL_STENCIL_BITS);
    JOGL_GL_Functions.checkError(gl);
    return bits;
  }

  static boolean stencilBufferIsEnabled(
    final @Nonnull GL gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_STENCIL_TEST);
    JOGL_GL_Functions.checkError(gl);
    return e;
  }

  static void texture2DStaticBind(
    final @Nonnull GL gl,
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getGLName());
    JOGL_GL_Functions.checkError(gl);
  }

  static void texture2DStaticDelete(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("texture-2D-static: delete ");
      state.log_text.append(texture);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, texture.getGLName());
    gl.glDeleteTextures(1, cache);
    JOGL_GL_Functions.checkError(gl);

    texture.resourceSetDeleted();
  }

  static boolean texture2DStaticIsBound(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());

    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(GL.GL_TEXTURE_BINDING_2D, cache);
    final int e = cache.get(0);
    JOGL_GL_Functions.checkError(gl);

    return e == texture.getGLName();
  }

  static void texture2DStaticUnbind(
    final @Nonnull GL gl,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    JOGL_GL_Functions.checkError(gl);
  }

  static void textureCubeStaticBind(
    final @Nonnull GL gl,
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticUsable texture)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texture.getGLName());
    JOGL_GL_Functions.checkError(gl);
  }

  static void textureCubeStaticDelete(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull TextureCubeStatic texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("texture-cube-static: delete ");
      state.log_text.append(texture);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, texture.getGLName());
    gl.glDeleteTextures(1, cache);
    JOGL_GL_Functions.checkError(gl);

    texture.resourceSetDeleted();
  }

  static boolean textureCubeStaticIsBound(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticUsable texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());

    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(GL.GL_TEXTURE_BINDING_CUBE_MAP, cache);
    final int e = cache.get(0);
    JOGL_GL_Functions.checkError(gl);

    return e == texture.getGLName();
  }

  static void textureCubeStaticUnbind(
    final @Nonnull GL gl,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
    JOGL_GL_Functions.checkError(gl);
  }

  static int textureGetMaximumSize(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    return JOGL_GL_Functions.contextGetInteger(
      gl,
      state,
      GL.GL_MAX_TEXTURE_SIZE);
  }

  static TextureUnit[] textureGetUnitsActual(
    final @Nonnull GL gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log)
    throws GLException
  {
    final int max =
      JOGL_GL_Functions.contextGetInteger(
        gl,
        state,
        GL2ES2.GL_MAX_TEXTURE_IMAGE_UNITS);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("implementation supports ");
      state.log_text.append(max);
      state.log_text.append(" texture units");
      log.debug(state.log_text.toString());
    }

    final TextureUnit[] u = new TextureUnit[max];
    for (int index = 0; index < max; ++index) {
      u[index] = new TextureUnit(index);
    }

    return u;
  }

  static void viewportSet(
    final @Nonnull GL gl,
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(position, "Viewport position");
    Constraints.constrainNotNull(dimensions, "Viewport dimensions");
    Constraints.constrainRange(
      dimensions.getXI(),
      0,
      Integer.MAX_VALUE,
      "Viewport width");
    Constraints.constrainRange(
      dimensions.getYI(),
      0,
      Integer.MAX_VALUE,
      "Viewport height");

    gl.glViewport(
      position.getXI(),
      position.getYI(),
      dimensions.getXI(),
      dimensions.getYI());
    JOGL_GL_Functions.checkError(gl);
  }
}
