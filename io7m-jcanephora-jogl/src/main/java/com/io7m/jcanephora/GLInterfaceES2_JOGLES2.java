/*
 * Copyright © 2012 http://io7m.com
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;

/**
 * A class implementing <code>GLInterfaceES2</code> that exposes only the
 * features of OpenGL ES2, using an OpenGL ES 2.0 context on JOGL.
 * 
 * A {@link javax.media.opengl.GLContext} is used to construct the interface,
 * and therefore the <code>GLInterfaceES2_JOGLES2</code> interface has the
 * same thread safe/unsafe behaviour.
 * 
 * The <code>GLInterfaceES2_JOGLES2</code> implementation does not call
 * {@link javax.media.opengl.GLContext#makeCurrent()} or
 * {@link javax.media.opengl.GLContext#release()}, so these calls must be made
 * by the programmer when necessary (typically, programs call
 * {@link javax.media.opengl.GLContext#makeCurrent()}, perform all rendering,
 * and then call {@link javax.media.opengl.GLContext#release()} at the end of
 * the frame). The JOGL library can also optionally manage this via the
 * {@link javax.media.opengl.GLAutoDrawable} interface.
 */

@NotThreadSafe public class GLInterfaceES2_JOGLES2 implements GLInterfaceES2
{
  private final @Nonnull Log          log;
  private final @Nonnull GLContext    context;
  private final @Nonnull GLStateCache state;

  public GLInterfaceES2_JOGLES2(
    final @Nonnull GLContext context,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "jogl-es2");
    this.context = Constraints.constrainNotNull(context, "GL context");
    this.state = new GLStateCache();

    final GL2ES2 g = this.context.getGL().getGL2ES2();

    this.state.texture_units =
      GLES2Functions.textureGetUnitsActual(g, this.state, log);

    {
      final IntBuffer cache = this.state.getIntegerCache();
      g.glGetIntegerv(GL.GL_ALIASED_LINE_WIDTH_RANGE, cache);
      this.state.line_aliased_min_width = cache.get();
      this.state.line_aliased_max_width = cache.get();
      GLError.check(this);
    }

    {
      this.state.line_smooth_min_width = 1;
      this.state.line_smooth_max_width = 1;
    }

    {
      final IntBuffer cache = this.state.getIntegerCache();
      g.glGetIntegerv(GL.GL_ALIASED_POINT_SIZE_RANGE, cache);
      this.state.point_min_width = cache.get();
      this.state.point_max_width = cache.get();
      GLError.check(this);
    }
  }

  @Override public ArrayBuffer arrayBufferAllocate(
    final long elements,
    final @Nonnull ArrayBufferDescriptor descriptor,
    final @Nonnull UsageHint usage)
    throws GLException,
      ConstraintError
  {
    return GLES2Functions.arrayBufferAllocate(
      this.contextGetGLES2(),
      this.log,
      this.state,
      elements,
      descriptor,
      usage);
  }

  @Override public void arrayBufferBind(
    final @Nonnull ArrayBuffer buffer)
    throws GLException,
      ConstraintError
  {
    GLES2Functions.arrayBufferBind(this.contextGetGLES2(), buffer);
  }

  @Override public void arrayBufferBindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    GLES2Functions.arrayBufferBindVertexAttribute(
      this.contextGetGLES2(),
      buffer,
      buffer_attribute,
      program_attribute);
  }

  @Override public void arrayBufferDelete(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.arrayBufferDelete(
      this.contextGetGLES2(),
      this.log,
      this.state,
      id);
  }

  @Override public boolean arrayBufferIsBound(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    return GLES2Functions.arrayBufferIsBound(this.contextGetGLES2(), id);
  }

  @Override public void arrayBufferUnbind()
    throws GLException,
      ConstraintError
  {
    GLES2Functions.arrayBufferUnbind(this.contextGetGLES2());
  }

  @Override public void arrayBufferUnbindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    GLES2Functions.arrayBufferUnbindVertexAttribute(
      this.contextGetGLES2(),
      buffer,
      buffer_attribute,
      program_attribute);
  }

  @Override public void arrayBufferUpdate(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferWritableData data)
    throws GLException,
      ConstraintError
  {
    GLES2Functions.arrayBufferUpdate(this.contextGetGLES2(), buffer, data);
  }

  @Override public void blendingDisable()
    throws GLException
  {
    GLES2Functions.blendingDisable(this.contextGetGLES2());
  }

  @Override public void blendingEnable(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.blendingEnable(
      this.contextGetGLES2(),
      source_factor,
      destination_factor);
  }

  @Override public void blendingEnableSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.blendingEnableSeparate(
      this.contextGetGLES2(),
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor);
  }

  @Override public void blendingEnableSeparateWithEquationSeparateES2(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquationES2 equation_rgb,
    final @Nonnull BlendEquationES2 equation_alpha)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(
      this.contextGetGLES2(),
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public void blendingEnableWithEquationES2(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationES2 equation)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.blendingEnableWithEquationES2(
      this.contextGetGLES2(),
      source_factor,
      destination_factor,
      equation);
  }

  @Override public void blendingEnableWithEquationSeparateES2(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationES2 equation_rgb,
    final @Nonnull BlendEquationES2 equation_alpha)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.blendingEnableWithEquationSeparateES2(
      this.contextGetGLES2(),
      source_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public boolean blendingIsEnabled()
    throws ConstraintError,
      GLException
  {
    return GLES2Functions.blendingIsEnabled(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.colorBufferClear3f(this.contextGetGLES2(), r, g, b);
  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.colorBufferClear4f(this.contextGetGLES2(), r, g, b, a);
  }

  @Override public void colorBufferClearV3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.colorBufferClearV3f(this.contextGetGLES2(), color);
  }

  @Override public void colorBufferClearV4f(
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.colorBufferClearV4f(this.contextGetGLES2(), color);
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.colorBufferMask(this.contextGetGLES2(), r, g, b, a);
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws GLException
  {
    return GLES2Functions.colorBufferMaskStatusAlpha(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws GLException
  {
    return GLES2Functions.colorBufferMaskStatusBlue(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws GLException
  {
    return GLES2Functions.colorBufferMaskStatusGreen(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws GLException
  {
    return GLES2Functions.colorBufferMaskStatusRed(
      this.contextGetGLES2(),
      this.state);
  }

  private GL2ES2 contextGetGLES2()
  {
    return this.context.getGL().getGL2ES2();
  }

  @Override public void cullingDisable()
    throws GLException
  {
    GLES2Functions.cullingDisable(this.contextGetGLES2());
  }

  @Override public void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.cullingEnable(this.contextGetGLES2(), faces, order);
  }

  @Override public boolean cullingIsEnabled()
    throws GLException
  {
    return GLES2Functions.cullingIsEnabled(this.contextGetGLES2());
  }

  @Override public void depthBufferClear(
    final float depth)
    throws GLException,
      ConstraintError
  {
    GLES2Functions
      .depthBufferClear(this.contextGetGLES2(), this.state, depth);
  }

  @Override public void depthBufferDisable()
    throws GLException
  {
    GLES2Functions.depthBufferDisable(this.contextGetGLES2());
  }

  @Override public void depthBufferEnable(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.depthBufferEnable(
      this.contextGetGLES2(),
      this.state,
      function);
  }

  @Override public int depthBufferGetBits()
    throws GLException
  {
    return GLES2Functions.depthBufferGetBits(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean depthBufferIsEnabled()
    throws GLException
  {
    return GLES2Functions.depthBufferIsEnabled(this.contextGetGLES2());
  }

  @Override public void depthBufferWriteDisable()
    throws ConstraintError,
      GLException
  {
    GLES2Functions
      .depthBufferWriteDisable(this.contextGetGLES2(), this.state);
  }

  @Override public void depthBufferWriteEnable()
    throws ConstraintError,
      GLException
  {
    GLES2Functions.depthBufferWriteEnable(this.contextGetGLES2(), this.state);
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws GLException
  {
    return GLES2Functions.depthBufferWriteIsEnabled(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBuffer indices)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.drawElements(this.contextGetGLES2(), mode, indices);
  }

  @Override public boolean errorCodeIsInvalidOperation(
    final int code)
  {
    return code == GL.GL_INVALID_OPERATION;
  }

  @Override public void fragmentShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull FragmentShader shader)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.fragmentShaderAttach(
      this.contextGetGLES2(),
      this.state,
      this.log,
      program,
      shader);
  }

  @Override public FragmentShader fragmentShaderCompile(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    return GLES2Functions.fragmentShaderCompile(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      stream);
  }

  @Override public void fragmentShaderDelete(
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.fragmentShaderDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      id);
  }

  @Override public Framebuffer framebufferAllocate(
    final @Nonnull FramebufferAttachment[] attachments)
    throws ConstraintError,
      GLException
  {
    return GLES2Functions.framebufferAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      attachments);
  }

  @Override public void framebufferBind(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.framebufferBind(this.contextGetGLES2(), buffer);
  }

  @Override public void framebufferDelete(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.framebufferDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      buffer);
  }

  @Override public void framebufferUnbind()
    throws GLException
  {
    GLES2Functions.framebufferUnbind(this.contextGetGLES2());
  }

  @Override public IndexBuffer indexBufferAllocate(
    final @Nonnull ArrayBuffer buffer,
    final int indices)
    throws GLException,
      ConstraintError
  {
    return GLES2Functions.indexBufferAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      buffer,
      indices);
  }

  @Override public @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull GLUnsignedType type,
    final int indices)
    throws GLException,
      ConstraintError
  {
    return GLES2Functions.indexBufferAllocateType(
      this.contextGetGLES2(),
      this.state,
      this.log,
      type,
      indices);
  }

  @Override public void indexBufferDelete(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.indexBufferDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      id);
  }

  @Override public void indexBufferUpdate(
    final @Nonnull IndexBuffer buffer,
    final @Nonnull IndexBufferWritableData data)
    throws GLException,
      ConstraintError
  {
    GLES2Functions.indexBufferUpdate(this.contextGetGLES2(), buffer, data);
  }

  @Override public int lineAliasedGetMaximumWidth()
  {
    return this.state.line_aliased_max_width;
  }

  @Override public int lineAliasedGetMinimumWidth()
  {
    return this.state.line_aliased_min_width;
  }

  @Override public void lineSetWidth(
    final float width)
    throws GLException,
      ConstraintError
  {
    GLES2Functions.lineSetWidth(this.contextGetGLES2(), this.state, width);
  }

  @Override public int lineSmoothGetMaximumWidth()
  {
    return this.state.line_smooth_max_width;
  }

  @Override public int lineSmoothGetMinimumWidth()
  {
    return this.state.line_smooth_min_width;
  }

  @Override public void lineSmoothingDisable()
    throws GLException
  {
    GLES2Functions.lineSmoothingDisable(this.contextGetGLES2(), this.state);
  }

  @Override public void lineSmoothingEnable()
    throws GLException
  {
    GLES2Functions.lineSmoothingEnable(this.contextGetGLES2(), this.state);
  }

  @Override public int metaGetError()
    throws GLException
  {
    return GLES2Functions.metaGetError(this.contextGetGLES2());
  }

  @Override public String metaGetRenderer()
    throws GLException
  {
    return GLES2Functions.metaGetRenderer(this.contextGetGLES2());
  }

  @Override public String metaGetVendor()
    throws GLException
  {
    return GLES2Functions.metaGetVendor(this.contextGetGLES2());
  }

  @Override public String metaGetVersion()
    throws GLException
  {
    return GLES2Functions.metaGetVersion(this.contextGetGLES2());
  }

  @Override public int pointGetMaximumWidth()
  {
    return this.state.point_max_width;
  }

  @Override public int pointGetMinimumWidth()
  {
    return this.state.point_min_width;
  }

  @Override public void programActivate(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.contextGetGLES2();

    Constraints.constrainNotNull(program, "Program ID");
    Constraints.constrainArbitrary(
      program.resourceIsDeleted() == false,
      "Program not deleted");

    gl.glUseProgram(program.getGLName());
    GLError.check(this);
  }

  @Override public ProgramReference programCreate(
    final @Nonnull String name)
    throws ConstraintError,
      GLException
  {
    return GLES2Functions.programCreate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name);
  }

  @Override public void programDeactivate()
    throws GLException
  {
    GLES2Functions.programDeactivate(this.contextGetGLES2());
  }

  @Override public void programDelete(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.programDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      program);
  }

  @Override public void programGetAttributes(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramAttribute> out)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.programGetAttributes(
      this.contextGetGLES2(),
      this.state,
      this.log,
      program,
      out);
  }

  @Override public int programGetMaximumActiveAttributes()
    throws GLException
  {
    return GLES2Functions.programGetMaximumActiveAttributes(
      this.contextGetGLES2(),
      this.state,
      this.log);
  }

  @Override public void programGetUniforms(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramUniform> out)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.programGetUniforms(
      this.contextGetGLES2(),
      this.state,
      this.log,
      program,
      out);
  }

  @Override public boolean programIsActive(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    return GLES2Functions.programIsActive(
      this.contextGetGLES2(),
      this.state,
      program);
  }

  @Override public void programLink(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    GLES2Functions.programLink(
      this.contextGetGLES2(),
      this.state,
      this.log,
      program);
  }

  @Override public void programPutUniformFloat(
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.programPutUniformFloat(
      this.contextGetGLES2(),
      this.state,
      uniform,
      value);
  }

  @Override public void programPutUniformMatrix3x3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.programPutUniformMatrix3x3f(
      this.contextGetGLES2(),
      this.state,
      uniform,
      matrix);
  }

  @Override public void programPutUniformMatrix4x4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable4x4F matrix)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.programPutUniformMatrix4x4f(
      this.contextGetGLES2(),
      this.state,
      uniform,
      matrix);
  }

  @Override public void programPutUniformTextureUnit(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.programPutUniformTextureUnit(
      this.contextGetGLES2(),
      this.state,
      uniform,
      unit);
  }

  @Override public void programPutUniformVector2f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.programPutUniformVector2f(
      this.contextGetGLES2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programPutUniformVector2i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.programPutUniformVector2i(
      this.contextGetGLES2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programPutUniformVector3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.programPutUniformVector3f(
      this.contextGetGLES2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programPutUniformVector4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.programPutUniformVector4f(
      this.contextGetGLES2(),
      this.state,
      uniform,
      vector);
  }

  @Override public RenderbufferD24S8 renderbufferD24S8Allocate(
    final int width,
    final int height)
    throws ConstraintError,
      GLException
  {
    return GLES2Functions.renderbufferD24S8Allocate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      width,
      height);
  }

  @Override public void renderbufferD24S8Delete(
    final @Nonnull RenderbufferD24S8 buffer)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.renderbufferD24S8Delete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      buffer);
  }

  @Override public void scissorDisable()
    throws GLException
  {
    GLES2Functions.scissorDisable(this.contextGetGLES2());
  }

  @Override public void scissorEnable(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    GLES2Functions
      .scissorEnable(this.contextGetGLES2(), position, dimensions);
  }

  @Override public boolean scissorIsEnabled()
    throws GLException
  {
    return GLES2Functions.scissorIsEnabled(this.contextGetGLES2());
  }

  @Override public void stencilBufferClear(
    final int stencil)
    throws GLException,
      ConstraintError
  {
    GLES2Functions.stencilBufferClear(
      this.contextGetGLES2(),
      this.state,
      stencil);
  }

  @Override public void stencilBufferDisable()
    throws ConstraintError,
      GLException
  {
    GLES2Functions.stencilBufferDisable(this.contextGetGLES2());
  }

  @Override public void stencilBufferEnable()
    throws ConstraintError,
      GLException
  {
    GLES2Functions.stencilBufferEnable(this.contextGetGLES2(), this.state);
  }

  @Override public void stencilBufferFunction(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.stencilBufferFunction(
      this.contextGetGLES2(),
      faces,
      function,
      reference,
      mask);
  }

  @Override public int stencilBufferGetBits()
    throws GLException
  {
    return GLES2Functions.stencilBufferGetBits(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean stencilBufferIsEnabled()
    throws GLException
  {
    return GLES2Functions.stencilBufferIsEnabled(this.contextGetGLES2());
  }

  @Override public void stencilBufferMask(
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.stencilBufferMask(this.contextGetGLES2(), faces, mask);
  }

  @Override public void stencilBufferOperation(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.stencilBufferOperation(
      this.contextGetGLES2(),
      faces,
      stencil_fail,
      depth_fail,
      pass);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocate(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureType type,
    final @Nonnull TextureWrap wrap_s,
    final @Nonnull TextureWrap wrap_t,
    final @Nonnull TextureFilter mag_filter,
    final @Nonnull TextureFilter min_filter)
    throws ConstraintError,
      GLException
  {
    return GLES2Functions.texture2DStaticAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      width,
      height,
      type,
      wrap_s,
      wrap_t,
      mag_filter,
      min_filter);
  }

  @Override public void texture2DStaticBind(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.texture2DStaticBind(this.contextGetGLES2(), unit, texture);
  }

  @Override public void texture2DStaticDelete(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.texture2DStaticDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      texture);
  }

  @Override public boolean texture2DStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    return GLES2Functions.texture2DStaticIsBound(
      this.contextGetGLES2(),
      this.state,
      unit,
      texture);
  }

  @Override public void texture2DStaticUpdate(
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.texture2DStaticUpdate(this.contextGetGLES2(), data);
  }

  @Override public int textureGetMaximumSize()
    throws GLException
  {
    return GLES2Functions.textureGetMaximumSize(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public TextureUnit[] textureGetUnits()
    throws GLException
  {
    return this.state.texture_units;
  }

  @Override public void textureUnitUnbind(
    final TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.textureUnitUnbind(this.contextGetGLES2(), unit);
  }

  @Override public void vertexShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull VertexShader shader)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    GLES2Functions.vertexShaderAttach(
      this.contextGetGLES2(),
      this.state,
      this.log,
      program,
      shader);
  }

  @Override public VertexShader vertexShaderCompile(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    return GLES2Functions.vertexShaderCompile(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      stream);
  }

  @Override public void vertexShaderDelete(
    final @Nonnull VertexShader id)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.vertexShaderDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      id);
  }

  @Override public void viewportSet(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    GLES2Functions.viewportSet(this.contextGetGLES2(), position, dimensions);
  }
}
