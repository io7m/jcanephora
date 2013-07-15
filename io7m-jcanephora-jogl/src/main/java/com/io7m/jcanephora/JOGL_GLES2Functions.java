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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jcanephora.GLType.Type;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;
import com.jogamp.common.nio.Buffers;
import com.jogamp.common.util.VersionNumber;

final class JOGL_GLES2Functions
{
  static final @Nonnull ArrayBuffer arrayBufferAllocate(
    final @Nonnull GL2ES2 gl,
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

    JOGL_GLES2Functions.checkError(gl);
    return new ArrayBuffer(id, elements, descriptor);
  }

  static void arrayBufferBind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull ArrayBuffer buffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer.getGLName());
    JOGL_GLES2Functions.checkError(gl);
  }

  static void arrayBufferBindVertexAttribute(
    final @Nonnull GL2ES2 gl,
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    Constraints.constrainNotNull(buffer_attribute, "Buffer attribute");
    Constraints.constrainNotNull(program_attribute, "Program attribute");

    final boolean bound = JOGL_GLES2Functions.arrayBufferIsBound(gl, buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    final ArrayBufferDescriptor d = buffer.getDescriptor();
    final ArrayBufferAttribute dba =
      d.getAttribute(buffer_attribute.getName());

    final boolean same_array = dba == buffer_attribute;
    Constraints.constrainArbitrary(
      same_array,
      "Buffer attribute belongs to the array buffer");

    final boolean same_type =
      dba.getType().shaderTypeConvertible(
        dba.getElements(),
        program_attribute.getType());
    Constraints.constrainArbitrary(
      same_type,
      "Buffer attribute is of the same type as the program attribute");

    final int program_attrib_id = program_attribute.getLocation();
    final int count = buffer_attribute.getElements();
    final int type =
      JOGL_GLTypeConversions.scalarTypeToGL(buffer_attribute.getType());
    final boolean normalized = false;
    final int stride = (int) buffer.getElementSizeBytes();
    final int offset = d.getAttributeOffset(buffer_attribute.getName());

    gl.glEnableVertexAttribArray(program_attrib_id);
    JOGL_GLES2Functions.checkError(gl);
    gl.glVertexAttribPointer(
      program_attrib_id,
      count,
      type,
      normalized,
      stride,
      offset);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void arrayBufferDelete(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static boolean arrayBufferIsBound(
    final @Nonnull GL2ES2 gl,
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final int b = gl.glGetBoundBuffer(GL.GL_ARRAY_BUFFER);
    JOGL_GLES2Functions.checkError(gl);
    return b == id.getGLName();
  }

  static void arrayBufferUnbind(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void arrayBufferUnbindVertexAttribute(
    final @Nonnull GL2ES2 gl,
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final boolean bound = JOGL_GLES2Functions.arrayBufferIsBound(gl, buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    Constraints.constrainNotNull(buffer_attribute, "Buffer attribute");
    Constraints.constrainNotNull(program_attribute, "Program attribute");

    final ArrayBufferDescriptor d = buffer.getDescriptor();
    final ArrayBufferAttribute ba =
      d.getAttribute(buffer_attribute.getName());

    final boolean same_array = ba == buffer_attribute;
    Constraints.constrainArbitrary(
      same_array,
      "Buffer attribute belongs to the array buffer");

    gl.glDisableVertexAttribArray(program_attribute.getLocation());
    JOGL_GLES2Functions.checkError(gl);
  }

  static void arrayBufferUpdate(
    final @Nonnull GL2ES2 gl,
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

    final boolean bound = JOGL_GLES2Functions.arrayBufferIsBound(gl, buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    gl.glBufferSubData(
      GL.GL_ARRAY_BUFFER,
      data.getTargetDataOffset(),
      data.getTargetDataSize(),
      data.getTargetData());
    JOGL_GLES2Functions.checkError(gl);
  }

  static void blendingDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_BLEND);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void blendingEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      GLException
  {
    JOGL_GLES2Functions.blendingEnableSeparate(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor);
  }

  static void blendingEnableSeparate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor)
    throws ConstraintError,
      GLException
  {
    JOGL_GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(
      gl,
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      BlendEquationGLES2.BLEND_EQUATION_ADD,
      BlendEquationGLES2.BLEND_EQUATION_ADD);
  }

  static void blendingEnableSeparateWithEquationSeparateES2(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static void blendingEnableWithEquationES2(
    final @Nonnull GL2ES2 gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation)
    throws ConstraintError,
      GLException
  {
    JOGL_GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  static void blendingEnableWithEquationSeparateES2(
    final @Nonnull GL2ES2 gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation_rgb,
    final @Nonnull BlendEquationGLES2 equation_alpha)
    throws ConstraintError,
      GLException
  {
    JOGL_GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  static boolean blendingIsEnabled(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(GL.GL_BLEND, cache);
    JOGL_GLES2Functions.checkError(gl);
    return cache.get(0) == GL.GL_TRUE;
  }

  static void checkError(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final int code = gl.glGetError();

    if (code != 0) {
      throw new GLException(code, "OpenGL error: code " + code);
    }
  }

  static void colorBufferClear3f(
    final @Nonnull GL2ES2 gl,
    final float r,
    final float g,
    final float b)
    throws GLException
  {
    gl.glClearColor(r, g, b, 1.0f);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void colorBufferClear4f(
    final @Nonnull GL2ES2 gl,
    final float r,
    final float g,
    final float b,
    final float a)
    throws GLException
  {
    gl.glClearColor(r, g, b, a);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void colorBufferClearV3f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(color, "Color vector");
    JOGL_GLES2Functions.colorBufferClear3f(
      gl,
      color.getXF(),
      color.getYF(),
      color.getZF());
  }

  static void colorBufferClearV4f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(color, "Color vector");
    JOGL_GLES2Functions.colorBufferClear4f(
      gl,
      color.getXF(),
      color.getYF(),
      color.getZF(),
      color.getWF());
  }

  static void colorBufferMask(
    final @Nonnull GL2ES2 gl,
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws GLException
  {
    gl.glColorMask(r, g, b, a);
    JOGL_GLES2Functions.checkError(gl);
  }

  private static final ByteBuffer colorBufferMaskStatus(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final ByteBuffer cache = state.getColorMaskCache();
    gl.glGetBooleanv(GL.GL_COLOR_WRITEMASK, cache);
    JOGL_GLES2Functions.checkError(gl);
    return cache;
  }

  static boolean colorBufferMaskStatusAlpha(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int a = JOGL_GLES2Functions.colorBufferMaskStatus(gl, state).get(3);
    return a != 0;
  }

  static boolean colorBufferMaskStatusBlue(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int b = JOGL_GLES2Functions.colorBufferMaskStatus(gl, state).get(2);
    return b != 0;
  }

  static boolean colorBufferMaskStatusGreen(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int g = JOGL_GLES2Functions.colorBufferMaskStatus(gl, state).get(1);
    return g != 0;
  }

  static boolean colorBufferMaskStatusRed(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int r = JOGL_GLES2Functions.colorBufferMaskStatus(gl, state).get(0);
    return r != 0;
  }

  static int contextGetInteger(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final int name)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(name, cache);
    JOGL_GLES2Functions.checkError(gl);
    return cache.get(0);
  }

  static int contextGetProgramInteger(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final int program,
    final int name)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetProgramiv(program, name, cache);
    JOGL_GLES2Functions.checkError(gl);
    return cache.get(0);
  }

  static int contextGetShaderInteger(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final int program,
    final int name)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetShaderiv(program, name, cache);
    JOGL_GLES2Functions.checkError(gl);
    return cache.get(0);
  }

  static void cullingDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_CULL_FACE);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void cullingEnable(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static boolean cullingIsEnabled(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_CULL_FACE);
    JOGL_GLES2Functions.checkError(gl);
    return e;
  }

  static void depthBufferClear(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final float depth)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainRange(
      JOGL_GLES2Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glClearDepth(depth);
    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void depthBufferDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_DEPTH_TEST);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void depthBufferEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(function, "Depth function");
    Constraints.constrainRange(
      JOGL_GLES2Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    final int d = JOGL_GLTypeConversions.depthFunctionToGL(function);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(d);
    JOGL_GLES2Functions.checkError(gl);
  }

  static int depthBufferGetBits(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int bits =
      JOGL_GLES2Functions.contextGetInteger(gl, state, GL.GL_DEPTH_BITS);
    JOGL_GLES2Functions.checkError(gl);
    return bits;
  }

  static boolean depthBufferIsEnabled(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_DEPTH_TEST);
    JOGL_GLES2Functions.checkError(gl);
    return e;
  }

  static void depthBufferWriteDisable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      JOGL_GLES2Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glDepthMask(false);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void depthBufferWriteEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      JOGL_GLES2Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glDepthMask(true);
    JOGL_GLES2Functions.checkError(gl);
  }

  static boolean depthBufferWriteIsEnabled(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final ByteBuffer cache = state.getDepthMaskCache();
    gl.glGetBooleanv(GL.GL_DEPTH_WRITEMASK, cache);
    JOGL_GLES2Functions.checkError(gl);

    final IntBuffer bi = cache.asIntBuffer();
    return bi.get(0) == 1;
  }

  static void drawElements(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static void fragmentShaderAttach(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program,
    final @Nonnull FragmentShader shader)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    Constraints.constrainNotNull(shader, "Fragment shader");
    Constraints.constrainArbitrary(
      shader.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("fragment-shader: attach ");
      state.log_text.append(program);
      state.log_text.append(" ");
      state.log_text.append(shader);
      log.debug(state.log_text.toString());
    }

    gl.glAttachShader(program.getGLName(), shader.getGLName());
    JOGL_GLES2Functions.checkError(gl);
  }

  static FragmentShader fragmentShaderCompile(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "input stream");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("fragment-shader: compile \"");
      state.log_text.append(name);
      state.log_text.append("\"");
      log.debug(state.log_text.toString());
    }

    final int id = gl.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
    JOGL_GLES2Functions.checkError(gl);

    final ArrayList<Integer> lengths = new ArrayList<Integer>();
    final ArrayList<String> lines = new ArrayList<String>();
    JOGL_GLES2Functions.shaderReadSource(stream, lines, lengths);
    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());

    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    gl.glShaderSource(id, line_array.length, line_array, line_lengths);
    JOGL_GLES2Functions.checkError(gl);
    gl.glCompileShader(id);
    JOGL_GLES2Functions.checkError(gl);
    final int status =
      JOGL_GLES2Functions.contextGetShaderInteger(
        gl,
        state,
        id,
        GL2ES2.GL_COMPILE_STATUS);
    JOGL_GLES2Functions.checkError(gl);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      gl.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);
      JOGL_GLES2Functions.checkError(gl);

      final byte raw[] = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String text = new String(raw);
      throw new GLCompileException(name, text);
    }

    return new FragmentShader(id, name);
  }

  static void fragmentShaderDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Fragment shader");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Fragment shader not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("fragment-shader: delete ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glDeleteShader(id.getGLName());
    id.resourceSetDeleted();
    JOGL_GLES2Functions.checkError(gl);
  }

  static @Nonnull FramebufferReference framebufferAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGenFramebuffers(1, cache);
    JOGL_GLES2Functions.checkError(gl);
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
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);
    buffer.resourceSetDeleted();
  }

  static boolean framebufferDrawAnyIsBound(
    final @Nonnull GL2ES2 gl)
  {
    final int bound = gl.getBoundFramebuffer(GL.GL_FRAMEBUFFER);
    final int default_fb = gl.getDefaultDrawFramebuffer();
    return bound != default_fb;
  }

  static void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull GL2ES2 gl,
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
      JOGL_GLES2Functions.framebufferDrawIsBound(gl, framebuffer),
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTexture2D(
    final @Nonnull GL2ES2 gl,
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
      JOGL_GLES2Functions.framebufferDrawIsBound(gl, framebuffer),
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTextureCube(
    final @Nonnull GL2ES2 gl,
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
      JOGL_GLES2Functions.framebufferDrawIsBound(gl, framebuffer),
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull GL2ES2 gl,
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
      JOGL_GLES2Functions.framebufferDrawIsBound(gl, framebuffer),
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
    JOGL_GLES2Functions.checkError(gl);
  }

  /**
   * Available as an extension in ES2 (OES_packed_depth_stencil).
   */

  static void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull GL2ES2 gl,
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
      JOGL_GLES2Functions.framebufferDrawIsBound(gl, framebuffer),
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachDepthTexture2D(
    final @Nonnull GL2ES2 gl,
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
      JOGL_GLES2Functions.framebufferDrawIsBound(gl, framebuffer),
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull GL2ES2 gl,
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
      JOGL_GLES2Functions.framebufferDrawIsBound(gl, framebuffer),
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static void framebufferDrawBind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FramebufferReference buffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, buffer.getGLName());
    JOGL_GLES2Functions.checkError(gl);
  }

  static boolean framebufferDrawIsBound(
    final @Nonnull GL2ES2 gl,
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
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
    JOGL_GLES2Functions.checkError(gl);
  }

  static @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull GL2ES2 gl,
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
      JOGL_GLES2Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    final int status = gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
    JOGL_GLES2Functions.checkError(gl);

    return JOGL_GLTypeConversions.framebufferStatusFromGL(status);
  }

  static @Nonnull
    FramebufferColorAttachmentPoint[]
    framebufferGetAttachmentPointsActual(
      final @Nonnull GL2ES2 gl,
      final @Nonnull GLStateCache state,
      final @Nonnull Log log)
      throws GLException
  {
    final int max =
      JOGL_GLES2Functions.contextGetInteger(
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
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log)
    throws GLException
  {
    final int max =
      JOGL_GLES2Functions.contextGetInteger(
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
    final @Nonnull GL2ES2 gl,
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

    return JOGL_GLES2Functions.indexBufferAllocateType(
      gl,
      state,
      log,
      type,
      indices);
  }

  static @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);

    final int id = cache.get(0);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id);
    JOGL_GLES2Functions.checkError(gl);
    gl.glBufferData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      bytes_total,
      null,
      GL2ES2.GL_STREAM_DRAW);
    JOGL_GLES2Functions.checkError(gl);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    return new IndexBuffer(id, new RangeInclusive(0, indices - 1), type);
  }

  static void indexBufferDelete(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static void indexBufferUpdate(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static int metaGetError(
    final @Nonnull GL2ES2 gl)
  {
    return gl.glGetError();
  }

  static String metaGetRenderer(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final String x = gl.glGetString(GL.GL_RENDERER);
    JOGL_GLES2Functions.checkError(gl);
    return x;
  }

  static String metaGetVendor(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final String x = gl.glGetString(GL.GL_VENDOR);
    JOGL_GLES2Functions.checkError(gl);
    return x;
  }

  static String metaGetVersion(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final String x = gl.glGetString(GL.GL_VERSION);
    JOGL_GLES2Functions.checkError(gl);
    return x;
  }

  static int metaGetVersionMajor(
    final GL2ES2 gl)
  {
    final VersionNumber number = gl.getContext().getGLVersionNumber();
    return number.getMajor();
  }

  static int metaGetVersionMinor(
    final GL2ES2 gl)
  {
    final VersionNumber number = gl.getContext().getGLVersionNumber();
    return number.getMinor();
  }

  static boolean metaIsES(
    final GL2ES2 gl)
  {
    return gl.isGLES();
  }

  static void programActivate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    gl.glUseProgram(program.getGLName());
    JOGL_GLES2Functions.checkError(gl);
  }

  static ProgramReference programCreate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(name, "Program name");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: create \"");
      state.log_text.append(name);
      state.log_text.append("\"");
      log.debug(state.log_text.toString());
    }

    final int id = gl.glCreateProgram();
    if (id == 0) {
      throw new GLException(0, "glCreateProgram failed");
    }
    JOGL_GLES2Functions.checkError(gl);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: created ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    return new ProgramReference(id, name);
  }

  static void programDeactivate(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glUseProgram(0);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void programDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: delete ");
      state.log_text.append(program);
      log.debug(state.log_text.toString());
    }

    gl.glDeleteProgram(program.getGLName());
    program.resourceSetDeleted();
    JOGL_GLES2Functions.checkError(gl);
  }

  static void programGetAttributes(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramAttribute> out)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getGLName();
    final int max =
      JOGL_GLES2Functions.contextGetProgramInteger(
        gl,
        state,
        program.getGLName(),
        GL2ES2.GL_ACTIVE_ATTRIBUTES);
    final int length =
      JOGL_GLES2Functions.contextGetProgramInteger(
        gl,
        state,
        program.getGLName(),
        GL2ES2.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);

    final ByteBuffer buffer_name = Buffers.newDirectByteBuffer(length);
    final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_size = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_type = Buffers.newDirectIntBuffer(1);

    /*
     * Note: some drivers will return built-in attributes here (such as
     * "gl_Vertex") but their locations are -1, so must be explicitly ignored.
     */

    for (int index = 0; index < max; ++index) {
      buffer_length.rewind();
      buffer_size.rewind();
      buffer_type.rewind();
      buffer_name.rewind();

      gl.glGetActiveAttrib(
        id,
        index,
        length,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);
      JOGL_GLES2Functions.checkError(gl);

      final int type_raw = buffer_type.get(0);
      final GLType.Type type = JOGL_GLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = gl.glGetAttribLocation(id, name);
      JOGL_GLES2Functions.checkError(gl);

      if (location == -1) {
        if (log.enabled(Level.LOG_DEBUG)) {
          state.log_text.setLength(0);
          state.log_text.append("driver returned active attribute \"");
          state.log_text.append(name);
          state.log_text.append("\" with location -1, ignoring");
          log.debug(state.log_text.toString());
        }
        continue;
      }

      assert out.containsKey(name) == false;
      out.put(
        name,
        new ProgramAttribute(program, index, location, name, type));
    }
  }

  static int programGetMaximumActiveAttributes(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log)
    throws GLException
  {
    final int max =
      JOGL_GLES2Functions.contextGetInteger(
        gl,
        state,
        GL2ES2.GL_MAX_VERTEX_ATTRIBS);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("implementation supports ");
      state.log_text.append(max);
      state.log_text.append(" active attributes");
      log.debug(state.log_text.toString());
    }

    return max;
  }

  static void programGetUniforms(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramUniform> out)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getGLName();
    final int max =
      JOGL_GLES2Functions.contextGetProgramInteger(
        gl,
        state,
        id,
        GL2ES2.GL_ACTIVE_UNIFORMS);
    final int length =
      JOGL_GLES2Functions.contextGetProgramInteger(
        gl,
        state,
        id,
        GL2ES2.GL_ACTIVE_UNIFORM_MAX_LENGTH);
    JOGL_GLES2Functions.checkError(gl);

    final ByteBuffer buffer_name = Buffers.newDirectByteBuffer(length);
    final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_size = Buffers.newDirectIntBuffer(1);
    final IntBuffer buffer_type = Buffers.newDirectIntBuffer(1);

    for (int index = 0; index < max; ++index) {
      buffer_length.rewind();
      buffer_size.rewind();
      buffer_type.rewind();
      buffer_name.rewind();

      gl.glGetActiveUniform(
        id,
        index,
        length,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);
      JOGL_GLES2Functions.checkError(gl);

      final int type_raw = buffer_type.get(0);
      final GLType.Type type = JOGL_GLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = gl.glGetUniformLocation(id, name);
      JOGL_GLES2Functions.checkError(gl);

      if (location == -1) {
        if (log.enabled(Level.LOG_DEBUG)) {
          state.log_text.setLength(0);
          state.log_text.append("driver returned active uniform \"");
          state.log_text.append(name);
          state.log_text.append("\" with location -1, ignoring");
          log.debug(state.log_text.toString());
        }
        continue;
      }

      assert (out.containsKey(name) == false);
      out.put(name, new ProgramUniform(program, index, location, name, type));
    }
  }

  static boolean programIsActive(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    final int active =
      JOGL_GLES2Functions.contextGetInteger(
        gl,
        state,
        GL2ES2.GL_CURRENT_PROGRAM);
    JOGL_GLES2Functions.checkError(gl);
    return active == program.getGLName();
  }

  static void programLink(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("program: link ");
      state.log_text.append(program);
      log.debug(state.log_text.toString());
    }

    gl.glLinkProgram(program.getGLName());
    JOGL_GLES2Functions.checkError(gl);

    final int status =
      JOGL_GLES2Functions.contextGetProgramInteger(
        gl,
        state,
        program.getGLName(),
        GL2ES2.GL_LINK_STATUS);

    if (status == 0) {
      final ByteBuffer buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      gl
        .glGetProgramInfoLog(program.getGLName(), 8192, buffer_length, buffer);
      JOGL_GLES2Functions.checkError(gl);

      final byte raw[] = new byte[buffer.remaining()];
      buffer.get(raw);
      final String text = new String(raw);
      throw new GLCompileException(program.getName(), text);
    }

    JOGL_GLES2Functions.checkError(gl);
  }

  static void programPutUniformFloat(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT,
      "Uniform type is float");
    Constraints.constrainArbitrary(
      JOGL_GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform1f(uniform.getLocation(), value);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void programPutUniformMatrix3x3f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(matrix, "Matrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_MATRIX_3,
      "Uniform type is mat3");
    Constraints.constrainArbitrary(
      JOGL_GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniformMatrix3fv(
      uniform.getLocation(),
      1,
      false,
      matrix.getFloatBuffer());
    JOGL_GLES2Functions.checkError(gl);
  }

  static void programPutUniformMatrix4x4f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable4x4F matrix)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(matrix, "Matrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_MATRIX_4,
      "Uniform type is mat4");
    Constraints.constrainArbitrary(
      JOGL_GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniformMatrix4fv(
      uniform.getLocation(),
      1,
      false,
      matrix.getFloatBuffer());
    JOGL_GLES2Functions.checkError(gl);
  }

  static void programPutUniformTextureUnit(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_SAMPLER_2D,
      "Uniform type is sampler_2d");
    Constraints.constrainArbitrary(
      JOGL_GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform1i(uniform.getLocation(), unit.getIndex());
    JOGL_GLES2Functions.checkError(gl);
  }

  static void programPutUniformVector2f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_VECTOR_2,
      "Uniform type is vec2");
    Constraints.constrainArbitrary(
      JOGL_GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform2f(uniform.getLocation(), vector.getXF(), vector.getYF());
    JOGL_GLES2Functions.checkError(gl);
  }

  static void programPutUniformVector2i(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_INTEGER_VECTOR_2,
      "Uniform type is vec2");
    Constraints.constrainArbitrary(
      JOGL_GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform2i(uniform.getLocation(), vector.getXI(), vector.getYI());
    JOGL_GLES2Functions.checkError(gl);
  }

  static void programPutUniformVector3f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_VECTOR_3,
      "Uniform type is vec3");
    Constraints.constrainArbitrary(
      JOGL_GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform3f(
      uniform.getLocation(),
      vector.getXF(),
      vector.getYF(),
      vector.getZF());
    JOGL_GLES2Functions.checkError(gl);
  }

  static void programPutUniformVector4f(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == Type.TYPE_FLOAT_VECTOR_4,
      "Uniform type is vec4");
    Constraints.constrainArbitrary(
      JOGL_GLES2Functions.programIsActive(gl, state, uniform.getProgram()),
      "Program for uniform is active");

    gl.glUniform4f(
      uniform.getLocation(),
      vector.getXF(),
      vector.getYF(),
      vector.getZF(),
      vector.getWF());
    JOGL_GLES2Functions.checkError(gl);
  }

  static Renderbuffer<?> renderbufferAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull RenderbufferType type,
    final int width,
    final int height)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(type, "Renderbuffer type");
    Constraints.constrainRange(width, 1, Integer.MAX_VALUE);
    Constraints.constrainRange(height, 1, Integer.MAX_VALUE);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("renderbuffer: allocate ");
      state.log_text.append(width);
      state.log_text.append("x");
      state.log_text.append(height);
      state.log_text.append(" ");
      state.log_text.append(type);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    gl.glGenRenderbuffers(1, cache);
    JOGL_GLES2Functions.checkError(gl);
    final int id = cache.get(0);

    final int gtype = JOGL_GLTypeConversions.renderbufferTypeToGL(type);

    gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, id);
    JOGL_GLES2Functions.checkError(gl);
    gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, gtype, width, height);
    JOGL_GLES2Functions.checkError(gl);
    gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0);
    JOGL_GLES2Functions.checkError(gl);

    /**
     * The phantom type is set to RenderableColor here and then deliberately
     * discarded. The caller will cast to the correct type.
     */

    final Renderbuffer<?> r =
      new Renderbuffer<RenderableColor>(type, id, width, height);
    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("renderbuffer: allocated ");
      state.log_text.append(r);
      log.debug(state.log_text.toString());
    }

    return r;
  }

  static void renderbufferDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull Renderbuffer<?> buffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(buffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("renderbuffer: delete ");
      state.log_text.append(buffer);
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, buffer.getGLName());
    gl.glDeleteRenderbuffers(1, cache);
    buffer.resourceSetDeleted();
    JOGL_GLES2Functions.checkError(gl);
  }

  static void scissorDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_SCISSOR_TEST);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void scissorEnable(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static boolean scissorIsEnabled(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_SCISSOR_TEST);
    JOGL_GLES2Functions.checkError(gl);
    return e;
  }

  private static final void shaderReadSource(
    final @Nonnull InputStream stream,
    final @Nonnull ArrayList<String> lines,
    final @Nonnull ArrayList<Integer> lengths)
    throws IOException
  {
    final BufferedReader reader =
      new BufferedReader(new InputStreamReader(stream));

    for (;;) {
      final String line = reader.readLine();
      if (line == null) {
        break;
      }
      lines.add(line + "\n");
      lengths.add(Integer.valueOf(line.length() + 1));
    }

    assert (lines.size() == lengths.size());
  }

  static void stencilBufferClear(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final int stencil)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainRange(
      JOGL_GLES2Functions.stencilBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    gl.glClearStencil(stencil);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void stencilBufferDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_STENCIL_TEST);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void stencilBufferEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      JOGL_GLES2Functions.stencilBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    gl.glEnable(GL.GL_STENCIL_TEST);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void stencilBufferFunction(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(function, "Stencil function");

    final int func = JOGL_GLTypeConversions.stencilFunctionToGL(function);
    gl.glStencilFuncSeparate(
      JOGL_GLTypeConversions.faceSelectionToGL(faces),
      func,
      reference,
      mask);
    JOGL_GLES2Functions.checkError(gl);
  }

  static int stencilBufferGetBits(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final int bits =
      JOGL_GLES2Functions.contextGetInteger(gl, state, GL.GL_STENCIL_BITS);
    JOGL_GLES2Functions.checkError(gl);
    return bits;
  }

  static boolean stencilBufferIsEnabled(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_STENCIL_TEST);
    JOGL_GLES2Functions.checkError(gl);
    return e;
  }

  static void stencilBufferMask(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");

    gl.glStencilMaskSeparate(
      JOGL_GLTypeConversions.faceSelectionToGL(faces),
      mask);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void stencilBufferOperation(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(stencil_fail, "Stencil fail operation");
    Constraints.constrainNotNull(depth_fail, "Depth fail operation");
    Constraints.constrainNotNull(pass, "Pass operation");

    final int sfail =
      JOGL_GLTypeConversions.stencilOperationToGL(stencil_fail);
    final int dfail = JOGL_GLTypeConversions.stencilOperationToGL(depth_fail);
    final int dpass = JOGL_GLTypeConversions.stencilOperationToGL(pass);
    gl.glStencilOpSeparate(
      JOGL_GLTypeConversions.faceSelectionToGL(faces),
      sfail,
      dfail,
      dpass);
    JOGL_GLES2Functions.checkError(gl);
  }

  static @Nonnull Texture2DStatic texture2DStaticAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
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
      GLException
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
      state.log_text.append("texture-2D-static: allocate \"");
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
    JOGL_GLES2Functions.checkError(gl);
    final int texture_id = cache.get(0);

    gl.glBindTexture(GL.GL_TEXTURE_2D, texture_id);
    JOGL_GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_S,
      JOGL_GLTypeConversions.textureWrapSToGL(wrap_s));
    JOGL_GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_WRAP_T,
      JOGL_GLTypeConversions.textureWrapTToGL(wrap_t));
    JOGL_GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MIN_FILTER,
      JOGL_GLTypeConversions.textureFilterMinToGL(min_filter));
    JOGL_GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_2D,
      GL.GL_TEXTURE_MAG_FILTER,
      JOGL_GLTypeConversions.textureFilterMagToGL(mag_filter));
    JOGL_GLES2Functions.checkError(gl);

    final int internal =
      JOGL_GLTypeConversions.textureTypeToInternalFormatGL(type);
    final int format = JOGL_GLTypeConversions.textureTypeToFormatGL(type);
    final int itype = JOGL_GLTypeConversions.textureTypeToTypeGL(type);

    gl.glTexImage2D(
      GL.GL_TEXTURE_2D,
      0,
      internal,
      width,
      height,
      0,
      format,
      itype,
      null);
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    JOGL_GLES2Functions.checkError(gl);

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
      state.log_text.append("texture-2D-static: allocated ");
      state.log_text.append(t);
      log.debug(state.log_text.toString());
    }

    return t;
  }

  static void texture2DStaticBind(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static void texture2DStaticDelete(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);

    texture.resourceSetDeleted();
  }

  static boolean texture2DStaticIsBound(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);

    return e == texture.getGLName();
  }

  static void texture2DStaticUnbind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void texture2DStaticUpdate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(data, "Texture data");

    final AreaInclusive area = data.targetArea();
    final Texture2DStatic texture = data.getTexture();

    final TextureType type = texture.getType();
    final int x_offset = (int) area.getRangeX().getLower();
    final int y_offset = (int) area.getRangeY().getLower();
    final int width = (int) area.getRangeX().getInterval();
    final int height = (int) area.getRangeY().getInterval();
    final int format = JOGL_GLTypeConversions.textureTypeToFormatGL(type);
    final int gl_type = JOGL_GLTypeConversions.textureTypeToTypeGL(type);
    final ByteBuffer buffer = data.targetData();

    gl.glBindTexture(GL.GL_TEXTURE_2D, texture.getGLName());
    gl.glTexSubImage2D(
      GL.GL_TEXTURE_2D,
      0,
      x_offset,
      y_offset,
      width,
      height,
      format,
      gl_type,
      buffer);
    gl.glBindTexture(GL.GL_TEXTURE_2D, 0);
    JOGL_GLES2Functions.checkError(gl);
  }

  static @Nonnull TextureCubeStatic textureCubeStaticAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
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
      GLException
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
      state.log_text.append("texture-cube-static: allocate \"");
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
    JOGL_GLES2Functions.checkError(gl);
    final int texture_id = cache.get(0);

    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, texture_id);
    JOGL_GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_WRAP_S,
      JOGL_GLTypeConversions.textureWrapSToGL(wrap_s));
    JOGL_GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_WRAP_T,
      JOGL_GLTypeConversions.textureWrapTToGL(wrap_t));
    JOGL_GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL2ES2.GL_TEXTURE_WRAP_R,
      JOGL_GLTypeConversions.textureWrapRToGL(wrap_r));
    JOGL_GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_MIN_FILTER,
      JOGL_GLTypeConversions.textureFilterMinToGL(min_filter));
    JOGL_GLES2Functions.checkError(gl);
    gl.glTexParameteri(
      GL.GL_TEXTURE_CUBE_MAP,
      GL.GL_TEXTURE_MAG_FILTER,
      JOGL_GLTypeConversions.textureFilterMagToGL(mag_filter));
    JOGL_GLES2Functions.checkError(gl);

    final int internal =
      JOGL_GLTypeConversions.textureTypeToInternalFormatGL(type);
    final int format = JOGL_GLTypeConversions.textureTypeToFormatGL(type);
    final int itype = JOGL_GLTypeConversions.textureTypeToTypeGL(type);

    for (final CubeMapFace face : CubeMapFace.values()) {
      final int gface = JOGL_GLTypeConversions.cubeFaceToGL(face);

      gl.glTexImage2D(gface, 0, internal, size, size, 0, format, itype, null);
      JOGL_GLES2Functions.checkError(gl);
    }

    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
    JOGL_GLES2Functions.checkError(gl);

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
      state.log_text.append("texture-cube-static: allocated ");
      state.log_text.append(t);
      log.debug(state.log_text.toString());
    }

    return t;
  }

  static void textureCubeStaticBind(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);
  }

  static void textureCubeStaticDelete(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);

    texture.resourceSetDeleted();
  }

  static boolean textureCubeStaticIsBound(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);

    return e == texture.getGLName();
  }

  static void textureCubeStaticUnbind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");

    gl.glActiveTexture(GL.GL_TEXTURE0 + unit.getIndex());
    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
    JOGL_GLES2Functions.checkError(gl);
  }

  static void textureCubeStaticUpdate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull CubeMapFace face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      GLException
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
    final int format = JOGL_GLTypeConversions.textureTypeToFormatGL(type);
    final int gl_type = JOGL_GLTypeConversions.textureTypeToTypeGL(type);
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
      format,
      gl_type,
      buffer);
    gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, 0);
    JOGL_GLES2Functions.checkError(gl);
  }

  static int textureGetMaximumSize(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    return JOGL_GLES2Functions.contextGetInteger(
      gl,
      state,
      GL.GL_MAX_TEXTURE_SIZE);
  }

  static TextureUnit[] textureGetUnitsActual(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log)
    throws GLException
  {
    final int max =
      JOGL_GLES2Functions.contextGetInteger(
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

  static void vertexShaderAttach(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program,
    final @Nonnull VertexShader shader)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    Constraints.constrainNotNull(shader, "Vertex shader");
    Constraints.constrainArbitrary(
      shader.resourceIsDeleted() == false,
      "Vertex shader not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("vertex-shader: attach ");
      state.log_text.append(program);
      state.log_text.append(" ");
      state.log_text.append(shader);
      log.debug(state.log_text.toString());
    }

    gl.glAttachShader(program.getGLName(), shader.getGLName());
    JOGL_GLES2Functions.checkError(gl);
  }

  static VertexShader vertexShaderCompile(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(stream, "input stream");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("vertex-shader: compile \"");
      state.log_text.append(name);
      state.log_text.append("\"");
      log.debug(state.log_text.toString());
    }

    final int id = gl.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
    JOGL_GLES2Functions.checkError(gl);

    final ArrayList<Integer> lengths = new ArrayList<Integer>();
    final ArrayList<String> lines = new ArrayList<String>();
    JOGL_GLES2Functions.shaderReadSource(stream, lines, lengths);
    final String[] line_array = new String[lines.size()];
    final IntBuffer line_lengths = Buffers.newDirectIntBuffer(lines.size());

    for (int index = 0; index < lines.size(); ++index) {
      line_array[index] = lines.get(index);
      final int len = line_array[index].length();
      line_lengths.put(index, len);
    }

    gl.glShaderSource(id, line_array.length, line_array, line_lengths);
    JOGL_GLES2Functions.checkError(gl);
    gl.glCompileShader(id);
    JOGL_GLES2Functions.checkError(gl);
    final int status =
      JOGL_GLES2Functions.contextGetShaderInteger(
        gl,
        state,
        id,
        GL2ES2.GL_COMPILE_STATUS);
    JOGL_GLES2Functions.checkError(gl);

    if (status == 0) {
      final ByteBuffer log_buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      gl.glGetShaderInfoLog(id, 8192, buffer_length, log_buffer);
      JOGL_GLES2Functions.checkError(gl);

      final byte raw[] = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String text = new String(raw);
      throw new GLCompileException(name, text);
    }

    return new VertexShader(id, name);
  }

  static void vertexShaderDelete(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull VertexShader id)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(id, "Vertex shader");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Vertex shader not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("vertex-shader: delete ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    gl.glDeleteShader(id.getGLName());
    id.resourceSetDeleted();
    JOGL_GLES2Functions.checkError(gl);
  }

  static void viewportSet(
    final @Nonnull GL2ES2 gl,
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
    JOGL_GLES2Functions.checkError(gl);
  }
}
