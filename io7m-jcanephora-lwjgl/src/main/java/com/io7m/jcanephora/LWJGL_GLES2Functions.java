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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Nonnull;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jaux.functional.Pair;
import com.io7m.jcanephora.LWJGL_TextureSpecs.TextureSpec;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable3I;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jtensors.VectorReadable4I;

final class LWJGL_GLES2Functions
{
  static final @Nonnull ArrayBuffer arrayBufferAllocate(
    final @Nonnull Log log,
    final @Nonnull JCGLStateCache state,
    final long elements,
    final @Nonnull ArrayBufferTypeDescriptor descriptor,
    final @Nonnull UsageHint usage)
    throws JCGLException,
      ConstraintError
  {
    Constraints
      .constrainRange(elements, 1, Long.MAX_VALUE, "Buffer elements");
    Constraints.constrainNotNull(descriptor, "Buffer descriptor");

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

    int hint = GL15.GL_STATIC_DRAW;
    if (LWJGL_GLES2Functions.implementationReallyIsES2()) {
      hint = LWJGL_GLTypeConversions.usageHintES2ToGL(usage);
    } else {
      hint = LWJGL_GLTypeConversions.usageHintToGL(usage);
    }

    final int id = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, bytes_total, hint);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    LWJGL_GLES2Functions.checkError();
    return new ArrayBuffer(id, elements, descriptor);
  }

  static void arrayBufferBind(
    final @Nonnull ArrayBufferUsable buffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void arrayBufferDelete(
    final @Nonnull Log log,
    final @Nonnull JCGLStateCache state,
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      JCGLException
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

    GL15.glDeleteBuffers(id.getGLName());
    id.resourceSetDeleted();
    LWJGL_GLES2Functions.checkError();
  }

  static boolean arrayBufferIsBound(
    final @Nonnull ArrayBufferUsable id)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final int b = GL11.glGetInteger(GL15.GL_ARRAY_BUFFER_BINDING);
    LWJGL_GLES2Functions.checkError();
    return b == id.getGLName();
  }

  static void arrayBufferUnbind()
    throws JCGLException
  {
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    LWJGL_GLES2Functions.checkError();
  }

  static void arrayBufferUpdate(
    final @Nonnull ArrayBufferWritableData data)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(data, "Array data");

    final ArrayBufferUsable buffer = data.getArrayBuffer();
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final boolean bound = LWJGL_GLES2Functions.arrayBufferIsBound(buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    GL15.glBufferSubData(
      GL15.GL_ARRAY_BUFFER,
      data.getTargetDataOffset(),
      data.getTargetData());
    LWJGL_GLES2Functions.checkError();
  }

  static void blendingDisable()
    throws JCGLException
  {
    GL11.glDisable(GL11.GL_BLEND);
    LWJGL_GLES2Functions.checkError();
  }

  static void blendingEnable(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.blendingEnableSeparate(

    source_factor, source_factor, destination_factor, destination_factor);
  }

  static void blendingEnableSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(

      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      BlendEquationGLES2.BLEND_EQUATION_ADD,
      BlendEquationGLES2.BLEND_EQUATION_ADD);
  }

  static void blendingEnableSeparateWithEquationSeparateES2(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquationGLES2 equation_rgb,
    final @Nonnull BlendEquationGLES2 equation_alpha)
    throws ConstraintError,
      JCGLException
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

    GL11.glEnable(GL11.GL_BLEND);
    GL20.glBlendEquationSeparate(
      LWJGL_GLTypeConversions.blendEquationES2ToGL(equation_rgb),
      LWJGL_GLTypeConversions.blendEquationES2ToGL(equation_alpha));
    GL14.glBlendFuncSeparate(
      LWJGL_GLTypeConversions.blendFunctionToGL(source_rgb_factor),
      LWJGL_GLTypeConversions.blendFunctionToGL(destination_rgb_factor),
      LWJGL_GLTypeConversions.blendFunctionToGL(source_alpha_factor),
      LWJGL_GLTypeConversions.blendFunctionToGL(destination_alpha_factor));
    LWJGL_GLES2Functions.checkError();
  }

  static void blendingEnableWithEquationES2(

    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(

      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  static void blendingEnableWithEquationSeparateES2(

    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation_rgb,
    final @Nonnull BlendEquationGLES2 equation_alpha)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(

      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  static boolean blendingIsEnabled()
    throws JCGLException
  {
    final int e = GL11.glGetInteger(GL11.GL_BLEND);
    LWJGL_GLES2Functions.checkError();
    return e == GL11.GL_TRUE;
  }

  static void checkError()
    throws JCGLException
  {
    final int code = GL11.glGetError();

    if (code != 0) {
      throw new JCGLException(code, "OpenGL error: code " + code);
    }
  }

  static void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws JCGLException
  {
    GL11.glClearColor(r, g, b, 1.0f);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    LWJGL_GLES2Functions.checkError();
  }

  static void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws JCGLException
  {
    GL11.glClearColor(r, g, b, a);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    LWJGL_GLES2Functions.checkError();
  }

  static void colorBufferClearV3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(color, "Color vector");
    LWJGL_GLES2Functions.colorBufferClear3f(

    color.getXF(), color.getYF(), color.getZF());
  }

  static void colorBufferClearV4f(
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(color, "Color vector");
    LWJGL_GLES2Functions.colorBufferClear4f(

    color.getXF(), color.getYF(), color.getZF(), color.getWF());
  }

  static void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws JCGLException
  {
    GL11.glColorMask(r, g, b, a);
    LWJGL_GLES2Functions.checkError();
  }

  private static final ByteBuffer colorBufferMaskStatus(
    final @Nonnull JCGLStateCache state)
    throws JCGLException
  {
    final ByteBuffer cache = state.getColorMaskCache();
    GL11.glGetBoolean(GL11.GL_COLOR_WRITEMASK, cache);
    LWJGL_GLES2Functions.checkError();
    return cache;
  }

  static boolean colorBufferMaskStatusAlpha(
    final @Nonnull JCGLStateCache state)
    throws JCGLException
  {
    final int a = LWJGL_GLES2Functions.colorBufferMaskStatus(state).get(3);
    return a != 0;
  }

  static boolean colorBufferMaskStatusBlue(
    final @Nonnull JCGLStateCache state)
    throws JCGLException
  {
    final int b = LWJGL_GLES2Functions.colorBufferMaskStatus(state).get(2);
    return b != 0;
  }

  static boolean colorBufferMaskStatusGreen(

    final @Nonnull JCGLStateCache state)
    throws JCGLException
  {
    final int g = LWJGL_GLES2Functions.colorBufferMaskStatus(state).get(1);
    return g != 0;
  }

  static boolean colorBufferMaskStatusRed(

    final @Nonnull JCGLStateCache state)
    throws JCGLException
  {
    final int r = LWJGL_GLES2Functions.colorBufferMaskStatus(state).get(0);
    return r != 0;
  }

  static int contextGetInteger(

    final @Nonnull JCGLStateCache state,
    final int name)
    throws JCGLException
  {
    final IntBuffer cache = state.getIntegerCache();
    GL11.glGetInteger(name, cache);
    LWJGL_GLES2Functions.checkError();
    return cache.get(0);
  }

  static int contextGetProgramInteger(

    final @Nonnull JCGLStateCache state,
    final int program,
    final int name)
    throws JCGLException
  {
    final IntBuffer cache = state.getIntegerCache();
    GL20.glGetProgram(program, name, cache);
    LWJGL_GLES2Functions.checkError();
    return cache.get(0);
  }

  static int contextGetShaderInteger(

    final @Nonnull JCGLStateCache state,
    final int program,
    final int name)
    throws JCGLException
  {
    final IntBuffer cache = state.getIntegerCache();
    GL20.glGetShader(program, name, cache);
    LWJGL_GLES2Functions.checkError();
    return cache.get(0);
  }

  static void cullingDisable()
    throws JCGLException
  {
    GL11.glDisable(GL11.GL_CULL_FACE);
    LWJGL_GLES2Functions.checkError();
  }

  static void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(order, "Face winding order");

    final int fi = LWJGL_GLTypeConversions.faceSelectionToGL(faces);
    final int oi = LWJGL_GLTypeConversions.faceWindingOrderToGL(order);

    GL11.glEnable(GL11.GL_CULL_FACE);
    GL11.glCullFace(fi);
    GL11.glFrontFace(oi);
    LWJGL_GLES2Functions.checkError();
  }

  static boolean cullingIsEnabled()
    throws JCGLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_CULL_FACE);
    LWJGL_GLES2Functions.checkError();
    return e;
  }

  static void depthBufferClear(

    final @Nonnull JCGLStateCache state,
    final float depth)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainRange(
      LWJGL_GLES2Functions.depthBufferGetBits(state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    GL11.glClearDepth(depth);
    GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
    LWJGL_GLES2Functions.checkError();
  }

  static void depthBufferDisable()
    throws JCGLException
  {
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    LWJGL_GLES2Functions.checkError();
  }

  static void depthBufferEnable(

    final @Nonnull JCGLStateCache state,
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(function, "Depth function");
    Constraints.constrainRange(
      LWJGL_GLES2Functions.depthBufferGetBits(state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    final int d = LWJGL_GLTypeConversions.depthFunctionToGL(function);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
    GL11.glDepthFunc(d);
    LWJGL_GLES2Functions.checkError();
  }

  static int depthBufferGetBits(
    final @Nonnull JCGLStateCache state)
    throws JCGLException
  {
    final int bits =
      LWJGL_GLES2Functions.contextGetInteger(state, GL11.GL_DEPTH_BITS);
    LWJGL_GLES2Functions.checkError();
    return bits;
  }

  static boolean depthBufferIsEnabled()
    throws JCGLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
    LWJGL_GLES2Functions.checkError();
    return e;
  }

  static void depthBufferWriteDisable(

    final @Nonnull JCGLStateCache state)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainRange(
      LWJGL_GLES2Functions.depthBufferGetBits(state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    GL11.glDepthMask(false);
    LWJGL_GLES2Functions.checkError();
  }

  static void depthBufferWriteEnable(

    final @Nonnull JCGLStateCache state)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainRange(
      LWJGL_GLES2Functions.depthBufferGetBits(state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    GL11.glDepthMask(true);
    LWJGL_GLES2Functions.checkError();
  }

  static boolean depthBufferWriteIsEnabled(

    final @Nonnull JCGLStateCache state)
    throws JCGLException
  {
    final ByteBuffer cache = state.getDepthMaskCache();
    GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK, cache);
    LWJGL_GLES2Functions.checkError();

    final IntBuffer bi = cache.asIntBuffer();
    return bi.get(0) == 1;
  }

  static void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBufferUsable indices)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(mode, "Drawing mode");
    Constraints.constrainNotNull(indices, "Index buffer");
    Constraints.constrainArbitrary(
      indices.resourceIsDeleted() == false,
      "Index buffer not deleted");

    final int index_id = indices.getGLName();
    final int index_count = (int) indices.getRange().getInterval();
    final int mode_gl = LWJGL_GLTypeConversions.primitiveToGL(mode);
    final int type =
      LWJGL_GLTypeConversions.unsignedTypeToGL(indices.getType());

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, index_id);
    GL11.glDrawElements(mode_gl, index_count, type, 0L);
    LWJGL_GLES2Functions.checkError();
  }

  static FragmentShader fragmentShaderCompile(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException
  {
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(lines, "Input lines");
    Constraints.constrainArbitrary(
      ShaderUtilities.isEmpty(lines) == false,
      "Source lines not empty");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("fragment-shader: compile \"");
      state.log_text.append(name);
      state.log_text.append("\"");
      log.debug(state.log_text.toString());
    }

    final int id = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
    LWJGL_GLES2Functions.checkError();

    final String[] line_array = new String[lines.size()];
    for (int index = 0; index < lines.size(); ++index) {
      final String line = lines.get(index);
      line_array[index] = line;
    }

    GL20.glShaderSource(id, line_array);
    LWJGL_GLES2Functions.checkError();
    GL20.glCompileShader(id);
    LWJGL_GLES2Functions.checkError();
    final int status = LWJGL_GLES2Functions.contextGetShaderInteger(

    state, id, GL20.GL_COMPILE_STATUS);
    LWJGL_GLES2Functions.checkError();

    if (status == 0) {
      final ByteBuffer log_buffer = BufferUtils.createByteBuffer(8192);
      final IntBuffer buffer_length = BufferUtils.createIntBuffer(1);
      GL20.glGetShaderInfoLog(id, buffer_length, log_buffer);
      LWJGL_GLES2Functions.checkError();

      final byte raw[] = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String text = new String(raw);
      throw new JCGLCompileException(name, text);
    }

    return new FragmentShader(id, name);
  }

  static void fragmentShaderDelete(

    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      JCGLException
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

    GL20.glDeleteShader(id.getGLName());
    id.resourceSetDeleted();
    LWJGL_GLES2Functions.checkError();
  }

  static @Nonnull FramebufferReference framebufferAllocate(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log)
    throws ConstraintError,
      JCGLException
  {
    final int id = GL30.glGenFramebuffers();
    LWJGL_GLES2Functions.checkError();

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    return new FramebufferReference(id);
  }

  static void framebufferDelete(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference buffer)
    throws ConstraintError,
      JCGLException
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

    GL30.glDeleteFramebuffers(buffer.getGLName());
    LWJGL_GLES2Functions.checkError();
    buffer.resourceSetDeleted();
  }

  static boolean framebufferDrawAnyIsBound()
  {
    final int bound = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
    return bound != 0;
  }

  static void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.framebufferDrawIsBound(state, framebuffer),
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

    GL30.glFramebufferRenderbuffer(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_COLOR_ATTACHMENT0,
      GL30.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorTexture2D(
    final @Nonnull JCGLStateCache state,
    final @Nonnull JCGLVersion version,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      TextureTypeMeta.isColourRenderable(texture.getType(), version),
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

    GL30.glFramebufferTexture2D(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_COLOR_ATTACHMENT0,
      GL11.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorTextureCube(
    final @Nonnull JCGLStateCache state,
    final @Nonnull JCGLVersion version,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFaceLH face)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      TextureTypeMeta.isColourRenderable(texture.getType(), version),
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

    final int gface = LWJGL_GLTypeConversions.cubeFaceToGL(face);
    GL30.glFramebufferTexture2D(
      GL30.GL_FRAMEBUFFER,
      GL30.GL_COLOR_ATTACHMENT0,
      gface,
      texture.getGLName(),
      0);
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepth> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isDepthRenderable(),
      "Renderbuffer is depth renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at depth attachment");
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferRenderbuffer(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_DEPTH_ATTACHMENT,
      GL30.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isDepthRenderable(),
      "Renderbuffer is depth renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at depth attachment");
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferRenderbuffer(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_DEPTH_STENCIL_ATTACHMENT,
      GL30.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachDepthTexture2D(
    final @Nonnull JCGLStateCache state,
    final @Nonnull JCGLVersion version,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      TextureTypeMeta.isDepthRenderable(texture.getType()),
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

    GL30.glFramebufferTexture2D(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_DEPTH_ATTACHMENT,
      GL11.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableStencil> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(renderbuffer
      .getType()
      .isStencilRenderable(), "Renderbuffer is stencil renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at stencil attachment");
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferRenderbuffer(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_STENCIL_ATTACHMENT,
      GL30.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawBind(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, framebuffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static boolean framebufferDrawIsBound(
    final @Nonnull JCGLStateCache state,
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    final IntBuffer cache = state.getIntegerCache();
    GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING, cache);
    LWJGL_GLES2Functions.checkError();
    return cache.get(0) == framebuffer.getGLName();
  }

  static void framebufferDrawUnbind()
    throws JCGLException
  {
    GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
    LWJGL_GLES2Functions.checkError();
  }

  static @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull JCGLStateCache state,
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    final int status =
      GL30.glCheckFramebufferStatus(GL30.GL_DRAW_FRAMEBUFFER);
    LWJGL_GLES2Functions.checkError();

    return LWJGL_GLTypeConversions.framebufferStatusFromGL(status);
  }

  static @Nonnull
    List<FramebufferColorAttachmentPoint>
    framebufferGetAttachmentPointsActual(
      final @Nonnull JCGLStateCache state,
      final @Nonnull Log log)
      throws JCGLException
  {
    final int max =
      LWJGL_GLES2Functions.contextGetInteger(
        state,
        GL30.GL_MAX_COLOR_ATTACHMENTS);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("implementation supports ");
      state.log_text.append(max);
      state.log_text.append(" framebuffer color attachments");
      log.debug(state.log_text.toString());
    }

    final ArrayList<FramebufferColorAttachmentPoint> a =
      new ArrayList<FramebufferColorAttachmentPoint>();
    for (int index = 0; index < max; ++index) {
      a.add(new FramebufferColorAttachmentPoint(index));
    }

    return a;
  }

  static @Nonnull
    List<FramebufferDrawBuffer>
    framebufferGetDrawBuffersActual(
      final @Nonnull JCGLStateCache state,
      final @Nonnull Log log)
      throws JCGLException
  {
    final int max =
      LWJGL_GLES2Functions.contextGetInteger(state, GL20.GL_MAX_DRAW_BUFFERS);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("implementation supports ");
      state.log_text.append(max);
      state.log_text.append(" framebuffer draw buffers");
      log.debug(state.log_text.toString());
    }

    final ArrayList<FramebufferDrawBuffer> b =
      new ArrayList<FramebufferDrawBuffer>();
    for (int index = 0; index < max; ++index) {
      b.add(new FramebufferDrawBuffer(index));
    }

    return b;
  }

  private static boolean implementationReallyIsES2()
  {
    // XXX: Obviously not.
    return false;
  }

  static IndexBuffer indexBufferAllocate(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBufferUsable buffer,
    final int indices)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");
    Constraints.constrainRange(indices, 1, Integer.MAX_VALUE);

    JCGLUnsignedType type = JCGLUnsignedType.TYPE_UNSIGNED_BYTE;
    if (buffer.getRange().getInterval() > 0xff) {
      type = JCGLUnsignedType.TYPE_UNSIGNED_SHORT;
    }
    if (buffer.getRange().getInterval() > 0xffff) {
      type = JCGLUnsignedType.TYPE_UNSIGNED_INT;
    }

    return LWJGL_GLES2Functions.indexBufferAllocateType(
      state,
      log,
      type,
      indices);
  }

  static @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull JCGLUnsignedType type,
    final int indices)
    throws JCGLException,
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

    final int id = GL15.glGenBuffers();
    LWJGL_GLES2Functions.checkError();

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
    LWJGL_GLES2Functions.checkError();
    GL15.glBufferData(
      GL15.GL_ELEMENT_ARRAY_BUFFER,
      bytes_total,
      GL15.GL_STREAM_DRAW);
    LWJGL_GLES2Functions.checkError();

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    return new IndexBuffer(id, new RangeInclusive(0, indices - 1), type);
  }

  static void indexBufferDelete(

    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      JCGLException
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

    GL15.glDeleteBuffers(id.getGLName());
    id.resourceSetDeleted();
    LWJGL_GLES2Functions.checkError();
  }

  static void indexBufferUpdate(
    final @Nonnull IndexBufferWritableData data)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(data, "Index data");

    final IndexBufferUsable buffer = data.getIndexBuffer();
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Index buffer not deleted");

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer.getGLName());
    GL15.glBufferSubData(
      GL15.GL_ELEMENT_ARRAY_BUFFER,
      data.getTargetDataOffset(),
      data.getTargetData());
    LWJGL_GLES2Functions.checkError();
  }

  static int metaGetError()
  {
    return GL11.glGetError();
  }

  static String metaGetRenderer()
    throws JCGLException
  {
    final String x = GL11.glGetString(GL11.GL_RENDERER);
    LWJGL_GLES2Functions.checkError();
    return x;
  }

  public static @Nonnull JCGLSLVersion metaGetSLVersion(
    final @Nonnull Log log)
    throws JCGLException,
      ConstraintError
  {
    final String x = GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION);
    LWJGL_GLES2Functions.checkError();

    final boolean is_es = LWJGL_GLES2Functions.metaVersionIsES(x);
    LWJGL_GLES2Functions.checkError();

    final JCGLVersion v = LWJGL_GLES2Functions.metaGetVersion();
    LWJGL_GLES2Functions.checkError();

    final JCGLApi api = is_es ? JCGLApi.JCGL_ES : JCGLApi.JCGL_FULL;

    if ((v.getVersionMajor() == 3) && is_es) {
      if (v.getText().contains("Mesa 9.1.")) {
        final StringBuilder m = new StringBuilder();
        m.append("quirk: GL_VERSION is '");
        m.append(v.getText());
        m.append("' which lies about GLSL ES 3.0 support");
        m.append(" - downgrading GLSL ES version to 1.0.");
        log.debug(m.toString());
        return JCGLSLVersion.GLSL_ES_100;
      }
    }

    final Pair<Integer, Integer> p =
      LWJGL_GLES2Functions.metaParseSLVersion(x);

    return JCGLSLVersion.make(new JCGLSLVersionNumber(
      p.first.intValue(),
      p.second.intValue()), api, x);
  }

  static String metaGetVendor()
    throws JCGLException
  {
    final String x = GL11.glGetString(GL11.GL_VENDOR);
    LWJGL_GLES2Functions.checkError();
    return x;
  }

  static @Nonnull JCGLVersion metaGetVersion()
    throws JCGLException,
      ConstraintError
  {
    final String x = GL11.glGetString(GL11.GL_VERSION);
    LWJGL_GLES2Functions.checkError();
    final Pair<Integer, Integer> p = LWJGL_GLES2Functions.metaParseVersion(x);
    return JCGLVersion.make(new JCGLVersionNumber(
      p.first.intValue(),
      p.second.intValue(),
      0), LWJGL_GLES2Functions.metaVersionIsES(x)
      ? JCGLApi.JCGL_ES
      : JCGLApi.JCGL_FULL, x);
  }

  static @Nonnull Pair<Integer, Integer> metaParseSLVersion(
    final @Nonnull String v0)
  {
    final String v1 = v0.replaceFirst("^OpenGL ES GLSL ES ", "");
    final StringTokenizer tdot = new StringTokenizer(v1, ".");
    final String vmaj = tdot.nextToken();
    final String rest = tdot.nextToken();
    final StringTokenizer tspa = new StringTokenizer(rest, " ");
    final String vmin = tspa.nextToken();
    return new Pair<Integer, Integer>(
      Integer.valueOf(vmaj),
      Integer.valueOf(vmin));
  }

  static Pair<Integer, Integer> metaParseVersion(
    final @Nonnull String v0)
  {
    final String v1 = v0.replaceFirst("^OpenGL ES ", "");
    final StringTokenizer tdot = new StringTokenizer(v1, ".");
    final String vmaj = tdot.nextToken();
    final String rest = tdot.nextToken();
    final StringTokenizer tspa = new StringTokenizer(rest, " ");
    final String vmin = tspa.nextToken();
    return new Pair<Integer, Integer>(
      Integer.valueOf(vmaj),
      Integer.valueOf(vmin));
  }

  static boolean metaVersionIsES(
    final @Nonnull String v0)
  {
    return v0.startsWith("OpenGL ES");
  }

  static void programActivate(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    GL20.glUseProgram(program.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void programAttributeArrayAssociate(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(buffer_attribute, "Buffer attribute");
    Constraints.constrainNotNull(program_attribute, "Program attribute");

    final ArrayBufferUsable buffer = buffer_attribute.getArray();

    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Array buffer not deleted");

    final boolean bound = LWJGL_GLES2Functions.arrayBufferIsBound(buffer);
    Constraints.constrainArbitrary(bound, "Buffer is bound");

    Constraints.constrainArbitrary(
      buffer_attribute.getArray().equals(buffer),
      "Array attribute belongs to the given array");

    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(
        state,
        program_attribute.getProgram()),
      "Program for program attribute is not active");

    final ArrayBufferAttributeDescriptor buffer_attribute_type =
      buffer_attribute.getDescriptor();

    final JCGLScalarType buffer_attribute_gl_type =
      buffer_attribute_type.getType();
    final int buffer_attribute_elements = buffer_attribute_type.getElements();

    final boolean convertible =
      buffer_attribute_gl_type.shaderTypeConvertible(
        buffer_attribute_elements,
        program_attribute.getType());

    if (convertible == false) {
      final StringBuilder b = new StringBuilder();
      b.append("Array buffer attribute '");
      b.append(buffer_attribute_type.getName());
      b.append("' is of type ");
      b.append(buffer_attribute_gl_type);
      b.append(" with ");
      b.append(buffer_attribute_elements);
      b.append(" elements, but the program attribute '");
      b.append(program_attribute.getName());
      b.append("' is of type ");
      b.append(program_attribute.getType());
      b.append(", which is incompatible");
      Constraints.constrainArbitrary(convertible, b.toString());
    }

    final int program_attrib_id = program_attribute.getLocation();

    final int type =
      LWJGL_GLTypeConversions.scalarTypeToGL(buffer_attribute_gl_type);
    final boolean normalized = false;
    final int stride = (int) buffer.getElementSizeBytes();
    final int offset =
      buffer
        .getType()
        .getTypeDescriptor()
        .getAttributeOffset(buffer_attribute.getName());

    GL20.glEnableVertexAttribArray(program_attrib_id);
    GL20.glVertexAttribPointer(
      program_attrib_id,
      buffer_attribute_elements,
      type,
      normalized,
      stride,
      offset);
    LWJGL_GLES2Functions.checkError();
  }

  static void programAttributeArrayDisassociate(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramAttribute program_attribute)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(program_attribute, "Program attribute");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(
        state,
        program_attribute.getProgram()),
      "Program for program attribute is not active");

    GL20.glDisableVertexAttribArray(program_attribute.getLocation());
    LWJGL_GLES2Functions.checkError();
  }

  static void programAttributePutFloat(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramAttribute program_attribute,
    final float x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(program_attribute, "Program attribute");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(
        state,
        program_attribute.getProgram()),
      "Program for program attribute is not active");

    final boolean convertible =
      JCGLScalarType.TYPE_FLOAT.shaderTypeConvertible(
        1,
        program_attribute.getType());

    if (convertible == false) {
      final StringBuilder b = new StringBuilder();
      b.append("The program attribute '");
      b.append(program_attribute.getName());
      b.append("' is of type ");
      b.append(program_attribute.getType());
      b.append(" but the given value is of type float");
      Constraints.constrainArbitrary(false, b.toString());
    }

    final int program_attrib_id = program_attribute.getLocation();
    GL20.glVertexAttrib1f(program_attrib_id, x);
    LWJGL_GLES2Functions.checkError();
  }

  static void programAttributePutVector2f(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull VectorReadable2F x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(program_attribute, "Program attribute");
    Constraints.constrainNotNull(x, "Value");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(
        state,
        program_attribute.getProgram()),
      "Program for program attribute is not active");

    final boolean convertible =
      JCGLScalarType.TYPE_FLOAT.shaderTypeConvertible(
        2,
        program_attribute.getType());

    if (convertible == false) {
      final StringBuilder b = new StringBuilder();
      b.append("The program attribute '");
      b.append(program_attribute.getName());
      b.append("' is of type ");
      b.append(program_attribute.getType());
      b.append(" but the given value is of type ");
      b.append(JCGLType.TYPE_FLOAT_VECTOR_2);
      Constraints.constrainArbitrary(false, b.toString());
    }

    final int program_attrib_id = program_attribute.getLocation();
    GL20.glVertexAttrib2f(program_attrib_id, x.getXF(), x.getYF());
    LWJGL_GLES2Functions.checkError();
  }

  static void programAttributePutVector3f(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull VectorReadable3F x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(program_attribute, "Program attribute");
    Constraints.constrainNotNull(x, "Value");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(
        state,
        program_attribute.getProgram()),
      "Program for program attribute is not active");

    final boolean convertible =
      JCGLScalarType.TYPE_FLOAT.shaderTypeConvertible(
        3,
        program_attribute.getType());

    if (convertible == false) {
      final StringBuilder b = new StringBuilder();
      b.append("The program attribute '");
      b.append(program_attribute.getName());
      b.append("' is of type ");
      b.append(program_attribute.getType());
      b.append(" but the given value is of type ");
      b.append(JCGLType.TYPE_FLOAT_VECTOR_3);
      Constraints.constrainArbitrary(false, b.toString());
    }

    final int program_attrib_id = program_attribute.getLocation();
    GL20.glVertexAttrib3f(program_attrib_id, x.getXF(), x.getYF(), x.getZF());
    LWJGL_GLES2Functions.checkError();
  }

  static void programAttributePutVector4f(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull VectorReadable4F x)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(program_attribute, "Program attribute");
    Constraints.constrainNotNull(x, "Value");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(
        state,
        program_attribute.getProgram()),
      "Program for program attribute is not active");

    final boolean convertible =
      JCGLScalarType.TYPE_FLOAT.shaderTypeConvertible(
        4,
        program_attribute.getType());

    if (convertible == false) {
      final StringBuilder b = new StringBuilder();
      b.append("The program attribute '");
      b.append(program_attribute.getName());
      b.append("' is of type ");
      b.append(program_attribute.getType());
      b.append(" but the given value is of type ");
      b.append(JCGLType.TYPE_FLOAT_VECTOR_4);
      Constraints.constrainArbitrary(false, b.toString());
    }

    final int program_attrib_id = program_attribute.getLocation();
    GL20.glVertexAttrib4f(
      program_attrib_id,
      x.getXF(),
      x.getYF(),
      x.getZF(),
      x.getWF());
    LWJGL_GLES2Functions.checkError();
  }

  static ProgramReference programCreateCommon(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull VertexShader v,
    final @Nonnull FragmentShader f)
    throws ConstraintError,
      JCGLException,
      JCGLCompileException
  {
    Constraints.constrainNotNull(name, "Program name");
    Constraints.constrainNotNull(v, "Vertex shader");
    Constraints.constrainNotNull(f, "Fragment shader");
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

    final int id = GL20.glCreateProgram();
    if (id == 0) {
      throw new JCGLException(0, "glCreateProgram failed");
    }
    LWJGL_GLES2Functions.checkError();

    GL20.glAttachShader(id, v.getGLName());
    LWJGL_GLES2Functions.checkError();
    GL20.glAttachShader(id, f.getGLName());
    LWJGL_GLES2Functions.checkError();
    GL20.glLinkProgram(id);
    LWJGL_GLES2Functions.checkError();

    final int status =
      LWJGL_GLES2Functions.contextGetProgramInteger(
        state,
        id,
        GL20.GL_LINK_STATUS);

    if (status == 0) {
      final ByteBuffer buffer = BufferUtils.createByteBuffer(8192);
      final IntBuffer buffer_length = BufferUtils.createIntBuffer(1);
      GL20.glGetProgramInfoLog(id, buffer_length, buffer);
      LWJGL_GLES2Functions.checkError();

      final byte raw[] = new byte[buffer.remaining()];
      buffer.get(raw);
      final String text = new String(raw);
      throw new JCGLCompileException(name, text);
    }

    LWJGL_GLES2Functions.checkError();

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

    LWJGL_GLES2Functions
      .programGetAttributes(state, log, program, attributes);

    LWJGL_GLES2Functions.programGetUniforms(state, log, program, uniforms);

    return program;
  }

  static void programDeactivate()
    throws JCGLException
  {
    GL20.glUseProgram(0);
    LWJGL_GLES2Functions.checkError();
  }

  static void programDelete(

    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      JCGLException
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

    GL20.glDeleteProgram(program.getGLName());
    program.resourceSetDeleted();
    LWJGL_GLES2Functions.checkError();
  }

  static void programGetAttributes(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReferenceUsable program,
    final @Nonnull Map<String, ProgramAttribute> out)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getGLName();
    final int max = LWJGL_GLES2Functions.contextGetProgramInteger(

    state, program.getGLName(), GL20.GL_ACTIVE_ATTRIBUTES);
    final int length = LWJGL_GLES2Functions.contextGetProgramInteger(

    state, program.getGLName(), GL20.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH);

    final ByteBuffer buffer_name = BufferUtils.createByteBuffer(length);
    final IntBuffer buffer_length = BufferUtils.createIntBuffer(1);
    final IntBuffer buffer_size = BufferUtils.createIntBuffer(1);
    final IntBuffer buffer_type = BufferUtils.createIntBuffer(1);

    /*
     * Note: some drivers will return built-in attributes here (such as
     * "gl_Vertex") but their locations are -1, so must be explicitly ignored.
     */

    for (int index = 0; index < max; ++index) {
      buffer_length.rewind();
      buffer_size.rewind();
      buffer_type.rewind();
      buffer_name.rewind();

      GL20.glGetActiveAttrib(
        id,
        index,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);
      LWJGL_GLES2Functions.checkError();

      final int type_raw = buffer_type.get(0);
      final JCGLType type = LWJGL_GLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = GL20.glGetAttribLocation(id, name);
      LWJGL_GLES2Functions.checkError();

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

    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log)
    throws JCGLException
  {
    final int max = LWJGL_GLES2Functions.contextGetInteger(

    state, GL20.GL_MAX_VERTEX_ATTRIBS);

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
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ProgramReferenceUsable program,
    final @Nonnull Map<String, ProgramUniform> out)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");
    Constraints.constrainNotNull(out, "Output map");

    final int id = program.getGLName();
    final int max = LWJGL_GLES2Functions.contextGetProgramInteger(

    state, id, GL20.GL_ACTIVE_UNIFORMS);
    final int length = LWJGL_GLES2Functions.contextGetProgramInteger(

    state, id, GL20.GL_ACTIVE_UNIFORM_MAX_LENGTH);
    LWJGL_GLES2Functions.checkError();

    final ByteBuffer buffer_name = BufferUtils.createByteBuffer(length);
    final IntBuffer buffer_length = BufferUtils.createIntBuffer(1);
    final IntBuffer buffer_size = BufferUtils.createIntBuffer(1);
    final IntBuffer buffer_type = BufferUtils.createIntBuffer(1);

    for (int index = 0; index < max; ++index) {
      buffer_length.rewind();
      buffer_size.rewind();
      buffer_type.rewind();
      buffer_name.rewind();

      GL20.glGetActiveUniform(
        id,
        index,
        buffer_length,
        buffer_size,
        buffer_type,
        buffer_name);
      LWJGL_GLES2Functions.checkError();

      final int type_raw = buffer_type.get(0);
      final JCGLType type = LWJGL_GLTypeConversions.typeFromGL(type_raw);

      final int name_length = buffer_length.get(0);
      final byte temp_buffer[] = new byte[name_length];
      buffer_name.get(temp_buffer);
      final String name = new String(temp_buffer);

      final int location = GL20.glGetUniformLocation(id, name);
      LWJGL_GLES2Functions.checkError();

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
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    final int active =
      LWJGL_GLES2Functions.contextGetInteger(state, GL20.GL_CURRENT_PROGRAM);
    LWJGL_GLES2Functions.checkError();
    return active == program.getGLName();
  }

  static void programPutUniformFloat(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT,
      "Uniform type is float");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(state, uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform1f(uniform.getLocation(), value);
    LWJGL_GLES2Functions.checkError();
  }

  static void programPutUniformMatrix3x3f(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(matrix, "Matrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT_MATRIX_3,
      "Uniform type is mat3");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(state, uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniformMatrix3(
      uniform.getLocation(),
      false,
      matrix.getFloatBuffer());
    LWJGL_GLES2Functions.checkError();
  }

  static void programPutUniformMatrix4x4f(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable4x4F matrix)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(matrix, "Matrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT_MATRIX_4,
      "Uniform type is mat4");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(state, uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniformMatrix4(
      uniform.getLocation(),
      false,
      matrix.getFloatBuffer());
    LWJGL_GLES2Functions.checkError();
  }

  static void programPutUniformTextureUnit(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainArbitrary(
      uniform.getType().isSamplerType(),
      "Uniform type is sampler");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(state, uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform1i(uniform.getLocation(), unit.getIndex());
    LWJGL_GLES2Functions.checkError();
  }

  static void programPutUniformVector2f(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT_VECTOR_2,
      "Uniform type is vec2");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(state, uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform2f(uniform.getLocation(), vector.getXF(), vector.getYF());
    LWJGL_GLES2Functions.checkError();
  }

  static void programPutUniformVector2i(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_INTEGER_VECTOR_2,
      "Uniform type is ivec2");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(state, uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform2i(uniform.getLocation(), vector.getXI(), vector.getYI());
    LWJGL_GLES2Functions.checkError();
  }

  static void programPutUniformVector3f(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT_VECTOR_3,
      "Uniform type is vec3");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(state, uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform3f(
      uniform.getLocation(),
      vector.getXF(),
      vector.getYF(),
      vector.getZF());
    LWJGL_GLES2Functions.checkError();
  }

  static void programPutUniformVector3i(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3I vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_INTEGER_VECTOR_3,
      "Uniform type is ivec3");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(state, uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform3i(
      uniform.getLocation(),
      vector.getXI(),
      vector.getYI(),
      vector.getZI());
    LWJGL_GLES2Functions.checkError();
  }

  static void programPutUniformVector4f(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_FLOAT_VECTOR_4,
      "Uniform type is vec4");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(state, uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform4f(
      uniform.getLocation(),
      vector.getXF(),
      vector.getYF(),
      vector.getZF(),
      vector.getWF());
    LWJGL_GLES2Functions.checkError();
  }

  static void programPutUniformVector4i(
    final @Nonnull JCGLStateCache state,
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4I vector)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(vector, "Vatrix");
    Constraints.constrainNotNull(uniform, "Uniform");
    Constraints.constrainArbitrary(
      uniform.getType() == JCGLType.TYPE_INTEGER_VECTOR_4,
      "Uniform type is ivec4");
    Constraints.constrainArbitrary(
      LWJGL_GLES2Functions.programIsActive(state, uniform.getProgram()),
      "Program for uniform is active");

    GL20.glUniform4i(
      uniform.getLocation(),
      vector.getXI(),
      vector.getYI(),
      vector.getZI(),
      vector.getWI());
    LWJGL_GLES2Functions.checkError();
  }

  static Renderbuffer<?> renderbufferAllocate(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull RenderbufferType type,
    final int width,
    final int height)
    throws ConstraintError,
      JCGLException
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

    final int id = GL30.glGenRenderbuffers();
    LWJGL_GLES2Functions.checkError();

    final int gtype = LWJGL_GLTypeConversions.renderbufferTypeToGL(type);

    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, id);
    LWJGL_GLES2Functions.checkError();
    GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, gtype, width, height);
    LWJGL_GLES2Functions.checkError();
    GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
    LWJGL_GLES2Functions.checkError();

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
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull Renderbuffer<?> buffer)
    throws ConstraintError,
      JCGLException
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

    GL30.glDeleteRenderbuffers(buffer.getGLName());
    buffer.resourceSetDeleted();
    LWJGL_GLES2Functions.checkError();
  }

  static void scissorDisable()
    throws JCGLException
  {
    GL11.glDisable(GL11.GL_SCISSOR_TEST);
    LWJGL_GLES2Functions.checkError();
  }

  static void scissorEnable(

    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      JCGLException
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

    GL11.glEnable(GL11.GL_SCISSOR_TEST);
    GL11.glScissor(
      position.getXI(),
      position.getYI(),
      dimensions.getXI(),
      dimensions.getYI());
    LWJGL_GLES2Functions.checkError();
  }

  static boolean scissorIsEnabled()
    throws JCGLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
    LWJGL_GLES2Functions.checkError();
    return e;
  }

  static void stencilBufferClear(
    final @Nonnull JCGLStateCache state,
    final int stencil)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainRange(
      LWJGL_GLES2Functions.stencilBufferGetBits(state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    GL11.glClearStencil(stencil);
    LWJGL_GLES2Functions.checkError();
  }

  static void stencilBufferDisable()
    throws JCGLException
  {
    GL11.glDisable(GL11.GL_STENCIL_TEST);
    LWJGL_GLES2Functions.checkError();
  }

  static void stencilBufferEnable(

    final @Nonnull JCGLStateCache state)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainRange(
      LWJGL_GLES2Functions.stencilBufferGetBits(state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    GL11.glEnable(GL11.GL_STENCIL_TEST);
    LWJGL_GLES2Functions.checkError();
  }

  static void stencilBufferFunction(

    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(function, "Stencil function");

    final int func = LWJGL_GLTypeConversions.stencilFunctionToGL(function);
    GL20.glStencilFuncSeparate(
      LWJGL_GLTypeConversions.faceSelectionToGL(faces),
      func,
      reference,
      mask);
    LWJGL_GLES2Functions.checkError();
  }

  static int stencilBufferGetBits(
    final @Nonnull JCGLStateCache state)
    throws JCGLException
  {
    final int bits =
      LWJGL_GLES2Functions.contextGetInteger(state, GL11.GL_STENCIL_BITS);
    LWJGL_GLES2Functions.checkError();
    return bits;
  }

  static boolean stencilBufferIsEnabled()
    throws JCGLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_STENCIL_TEST);
    LWJGL_GLES2Functions.checkError();
    return e;
  }

  static void stencilBufferMask(

    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(faces, "Face selection");

    GL20.glStencilMaskSeparate(
      LWJGL_GLTypeConversions.faceSelectionToGL(faces),
      mask);
    LWJGL_GLES2Functions.checkError();
  }

  static void stencilBufferOperation(

    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(stencil_fail, "Stencil fail operation");
    Constraints.constrainNotNull(depth_fail, "Depth fail operation");
    Constraints.constrainNotNull(pass, "Pass operation");

    final int sfail =
      LWJGL_GLTypeConversions.stencilOperationToGL(stencil_fail);
    final int dfail =
      LWJGL_GLTypeConversions.stencilOperationToGL(depth_fail);
    final int dpass = LWJGL_GLTypeConversions.stencilOperationToGL(pass);
    GL20.glStencilOpSeparate(
      LWJGL_GLTypeConversions.faceSelectionToGL(faces),
      sfail,
      dfail,
      dpass);
    LWJGL_GLES2Functions.checkError();
  }

  static @Nonnull Texture2DStatic texture2DStaticAllocate(
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
      final int bytes = height * (type.getBytesPerPixel() * width);
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

    final int texture_id = GL11.glGenTextures();
    LWJGL_GLES2Functions.checkError();

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_id);
    LWJGL_GLES2Functions.checkError();
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_WRAP_S,
      LWJGL_GLTypeConversions.textureWrapSToGL(wrap_s));
    LWJGL_GLES2Functions.checkError();
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_WRAP_T,
      LWJGL_GLTypeConversions.textureWrapTToGL(wrap_t));
    LWJGL_GLES2Functions.checkError();
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_MIN_FILTER,
      LWJGL_GLTypeConversions.textureFilterMinToGL(min_filter));
    LWJGL_GLES2Functions.checkError();
    GL11.glTexParameteri(
      GL11.GL_TEXTURE_2D,
      GL11.GL_TEXTURE_MAG_FILTER,
      LWJGL_GLTypeConversions.textureFilterMagToGL(mag_filter));
    LWJGL_GLES2Functions.checkError();

    final TextureSpec spec = LWJGL_TextureSpecs.getGL3TextureSpec(type);
    LWJGL_GLES2Functions.textureSetPackUnpackAlignment1();

    GL11.glTexImage2D(
      GL11.GL_TEXTURE_2D,
      0,
      spec.internal_format,
      width,
      height,
      0,
      spec.format,
      spec.type,
      (ByteBuffer) null);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    LWJGL_GLES2Functions.checkError();

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
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit.getIndex());
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void texture2DStaticDelete(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      JCGLException
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

    GL11.glDeleteTextures(texture.getGLName());
    LWJGL_GLES2Functions.checkError();

    texture.resourceSetDeleted();
  }

  static boolean texture2DStaticIsBound(
    final @Nonnull JCGLStateCache state,
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit.getIndex());

    final IntBuffer cache = state.getIntegerCache();
    GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D, cache);
    final int e = cache.get(0);
    LWJGL_GLES2Functions.checkError();

    return e == texture.getGLName();
  }

  static void texture2DStaticUnbind(
    final TextureUnit unit)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");

    GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit.getIndex());
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    LWJGL_GLES2Functions.checkError();
  }

  static void texture2DStaticUpdate(

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
    final ByteBuffer buffer = data.targetData();

    final TextureSpec spec = LWJGL_TextureSpecs.getGL3TextureSpec(type);

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGLName());
    GL11.glTexSubImage2D(
      GL11.GL_TEXTURE_2D,
      0,
      x_offset,
      y_offset,
      width,
      height,
      spec.format,
      spec.type,
      buffer);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    LWJGL_GLES2Functions.checkError();
  }

  static @Nonnull TextureCubeStatic textureCubeStaticAllocate(
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
      final int bytes = size * (type.getBytesPerPixel() * size) * 6;
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

    final int texture_id = GL11.glGenTextures();
    LWJGL_GLES2Functions.checkError();

    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture_id);
    LWJGL_GLES2Functions.checkError();
    GL11.glTexParameteri(
      GL13.GL_TEXTURE_CUBE_MAP,
      GL11.GL_TEXTURE_WRAP_S,
      LWJGL_GLTypeConversions.textureWrapSToGL(wrap_s));
    LWJGL_GLES2Functions.checkError();
    GL11.glTexParameteri(
      GL13.GL_TEXTURE_CUBE_MAP,
      GL11.GL_TEXTURE_WRAP_T,
      LWJGL_GLTypeConversions.textureWrapTToGL(wrap_t));
    LWJGL_GLES2Functions.checkError();
    GL11.glTexParameteri(
      GL13.GL_TEXTURE_CUBE_MAP,
      GL12.GL_TEXTURE_WRAP_R,
      LWJGL_GLTypeConversions.textureWrapRToGL(wrap_r));
    LWJGL_GLES2Functions.checkError();
    GL11.glTexParameteri(
      GL13.GL_TEXTURE_CUBE_MAP,
      GL11.GL_TEXTURE_MIN_FILTER,
      LWJGL_GLTypeConversions.textureFilterMinToGL(min_filter));
    LWJGL_GLES2Functions.checkError();
    GL11.glTexParameteri(
      GL13.GL_TEXTURE_CUBE_MAP,
      GL11.GL_TEXTURE_MAG_FILTER,
      LWJGL_GLTypeConversions.textureFilterMagToGL(mag_filter));
    LWJGL_GLES2Functions.checkError();

    final TextureSpec spec = LWJGL_TextureSpecs.getGL3TextureSpec(type);

    LWJGL_GLES2Functions.textureSetPackUnpackAlignment1();

    for (final CubeMapFaceLH face : CubeMapFaceLH.values()) {
      final int gface = LWJGL_GLTypeConversions.cubeFaceToGL(face);

      GL11.glTexImage2D(
        gface,
        0,
        spec.internal_format,
        size,
        size,
        0,
        spec.format,
        spec.type,
        (ByteBuffer) null);
      LWJGL_GLES2Functions.checkError();
    }

    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
    LWJGL_GLES2Functions.checkError();

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
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticUsable texture)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit.getIndex());
    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void textureCubeStaticDelete(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull TextureCubeStatic texture)
    throws ConstraintError,
      JCGLException
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

    GL11.glDeleteTextures(texture.getGLName());
    LWJGL_GLES2Functions.checkError();

    texture.resourceSetDeleted();
  }

  static boolean textureCubeStaticIsBound(
    final @Nonnull JCGLStateCache state,
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticUsable texture)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");
    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");

    GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit.getIndex());

    final IntBuffer cache = state.getIntegerCache();
    GL11.glGetInteger(GL13.GL_TEXTURE_BINDING_CUBE_MAP, cache);
    final int e = cache.get(0);
    LWJGL_GLES2Functions.checkError();

    return e == texture.getGLName();
  }

  static void textureCubeStaticUnbind(
    final TextureUnit unit)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(unit, "Texture unit");

    GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit.getIndex());
    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
    LWJGL_GLES2Functions.checkError();
  }

  static void textureCubeStaticUpdate(
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

    final TextureSpec spec = LWJGL_TextureSpecs.getGL3TextureSpec(type);
    final ByteBuffer buffer = data.targetData();
    final int gface = LWJGL_GLTypeConversions.cubeFaceToGL(face);

    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture.getGLName());
    GL11.glTexSubImage2D(
      gface,
      0,
      x_offset,
      y_offset,
      width,
      height,
      spec.format,
      spec.type,
      buffer);
    GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
    LWJGL_GLES2Functions.checkError();
  }

  static int textureGetMaximumSize(

    final @Nonnull JCGLStateCache state)
    throws JCGLException
  {
    return LWJGL_GLES2Functions.contextGetInteger(

    state, GL11.GL_MAX_TEXTURE_SIZE);
  }

  static List<TextureUnit> textureGetUnitsActual(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull JCGLSoftRestrictions restrictions)
    throws JCGLException
  {
    final int max = LWJGL_GLES2Functions.contextGetInteger(

    state, GL20.GL_MAX_TEXTURE_IMAGE_UNITS);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("implementation supports ");
      state.log_text.append(max);
      state.log_text.append(" texture units");
      log.debug(state.log_text.toString());
    }

    final int count =
      Math.max(1, Math.min(restrictions.restrictTextureUnitCount(max), max));
    if (count < max) {
      state.log_text.setLength(0);
      state.log_text.append("implementation exposes ");
      state.log_text.append(count);
      state.log_text.append(" texture units after soft restrictions");
      log.debug(state.log_text.toString());
    }

    final ArrayList<TextureUnit> u = new ArrayList<TextureUnit>();
    for (int index = 0; index < count; ++index) {
      u.add(new TextureUnit(index));
    }

    return u;
  }

  static void textureSetPackUnpackAlignment1()
    throws JCGLException
  {
    GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
    LWJGL_GLES2Functions.checkError();
    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
    LWJGL_GLES2Functions.checkError();
  }

  static VertexShader vertexShaderCompile(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException
  {
    Constraints.constrainNotNull(name, "Shader name");
    Constraints.constrainNotNull(lines, "Input lines");
    Constraints.constrainArbitrary(
      ShaderUtilities.isEmpty(lines) == false,
      "Source lines not empty");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("vertex-shader: compile \"");
      state.log_text.append(name);
      state.log_text.append("\"");
      log.debug(state.log_text.toString());
    }

    final int id = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
    LWJGL_GLES2Functions.checkError();

    final String[] line_array = new String[lines.size()];
    for (int index = 0; index < lines.size(); ++index) {
      final String line = lines.get(index);
      line_array[index] = line;
    }

    GL20.glShaderSource(id, line_array);
    LWJGL_GLES2Functions.checkError();
    GL20.glCompileShader(id);
    LWJGL_GLES2Functions.checkError();
    final int status = LWJGL_GLES2Functions.contextGetShaderInteger(

    state, id, GL20.GL_COMPILE_STATUS);
    LWJGL_GLES2Functions.checkError();

    if (status == 0) {
      final ByteBuffer log_buffer = BufferUtils.createByteBuffer(8192);
      final IntBuffer buffer_length = BufferUtils.createIntBuffer(1);
      GL20.glGetShaderInfoLog(id, buffer_length, log_buffer);
      LWJGL_GLES2Functions.checkError();

      final byte raw[] = new byte[log_buffer.remaining()];
      log_buffer.get(raw);
      final String text = new String(raw);
      throw new JCGLCompileException(name, text);
    }

    return new VertexShader(id, name);
  }

  static void vertexShaderDelete(

    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull VertexShader id)
    throws ConstraintError,
      JCGLException
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

    GL20.glDeleteShader(id.getGLName());
    id.resourceSetDeleted();
    LWJGL_GLES2Functions.checkError();
  }

  static void viewportSet(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      JCGLException
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

    GL11.glViewport(
      position.getXI(),
      position.getYI(),
      dimensions.getXI(),
      dimensions.getYI());
    LWJGL_GLES2Functions.checkError();
  }
}
