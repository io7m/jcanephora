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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;

/**
 * <p>
 * A class implementing {@link GLInterfaceGL3} that uses only non-deprecated
 * features of OpenGL 3.*, using JOGL as the backend.
 * </p>
 * <p>
 * A {@link javax.media.opengl.GLContext} is used to construct the interface,
 * and therefore the <code>GLInterfaceGL3_JOGL_GL3</code> interface has the
 * same thread safe/unsafe behaviour.
 * </p>
 * <p>
 * The <code>GLInterfaceGL3_JOGL_GL3</code> implementation does not call
 * {@link javax.media.opengl.GLContext#makeCurrent()} or
 * {@link javax.media.opengl.GLContext#release()}, so these calls must be made
 * by the programmer when necessary (typically, programs call
 * {@link javax.media.opengl.GLContext#makeCurrent()}, perform all rendering,
 * and then call {@link javax.media.opengl.GLContext#release()} at the end of
 * the frame). The JOGL library can also optionally manage this via the
 * {@link javax.media.opengl.GLAutoDrawable} interface.
 * </p>
 */

@NotThreadSafe final class GLInterfaceGL3_JOGL_GL2GL3 implements
  GLInterfaceGL3
{
  private final @Nonnull Log          log;
  private final @Nonnull GLContext    context;
  private final @Nonnull GLStateCache state;

  GLInterfaceGL3_JOGL_GL2GL3(
    final @Nonnull GLContext context,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "jogl30");
    this.context = Constraints.constrainNotNull(context, "GL context");
    this.state = new GLStateCache();

    final GL2GL3 g = this.context.getGL().getGL2GL3();

    Constraints.constrainArbitrary(
      this.framebufferDrawAnyIsBound() == false,
      "NOT BOUND!");

    /**
     * Initialize texture unit cache.
     */

    this.state.texture_units =
      JOGL_GL_Functions.textureGetUnitsActual(g, this.state, this.log);

    /**
     * Initialize color attachment point cache.
     */

    this.state.color_attachments =
      JOGL_GL_Functions.framebufferGetAttachmentPointsActual(
        g,
        this.state,
        this.log);

    /**
     * Get maximum draw buffers.
     */

    this.state.draw_buffers =
      JOGL_GL_Functions.framebufferGetDrawBuffersActual(
        g,
        this.state,
        this.log);

    /**
     * Initialize various constants.
     */

    {
      final IntBuffer cache = this.state.getIntegerCache();
      g.glGetIntegerv(GL2GL3.GL_POINT_SIZE_RANGE, cache);
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
    return JOGL_GL_Functions.arrayBufferAllocate(
      this.context.getGL(),
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
    JOGL_GL_Functions.arrayBufferBind(this.context.getGL(), buffer);
  }

  @Override public void arrayBufferBindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.arrayBufferBindVertexAttribute(this.context
      .getGL()
      .getGL2ES2(), buffer, buffer_attribute, program_attribute);
  }

  @Override public void arrayBufferDelete(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.arrayBufferDelete(
      this.context.getGL(),
      this.log,
      this.state,
      id);
  }

  @Override public boolean arrayBufferIsBound(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL_Functions.arrayBufferIsBound(this.context.getGL(), id);
  }

  @Override public ByteBuffer arrayBufferMapReadUntyped(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.arrayBufferMapRead(
      this.context.getGL(),
      this.state,
      this.log,
      id);
  }

  @Override public @Nonnull ByteBuffer arrayBufferMapReadUntypedRange(
    final @Nonnull ArrayBuffer id,
    final @Nonnull RangeInclusive range)
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.arrayBufferMapReadRange(
      this.context.getGL(),
      this.state,
      this.log,
      id,
      range);
  }

  @Override public ArrayBufferWritableMap arrayBufferMapWrite(
    final @Nonnull ArrayBuffer id)
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.arrayBufferMapWrite(
      this.context.getGL(),
      this.state,
      this.log,
      id);
  }

  @Override public void arrayBufferUnbind()
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferUnbind(this.context.getGL());
  }

  @Override public void arrayBufferUnbindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.arrayBufferUnbindVertexAttribute(this.context
      .getGL()
      .getGL2ES2(), buffer, buffer_attribute, program_attribute);
  }

  @Override public void arrayBufferUnmap(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.arrayBufferUnmap(
      this.context.getGL(),
      this.state,
      this.log,
      id);
  }

  @Override public void arrayBufferUpdate(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferWritableData data)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferUpdate(this.context.getGL(), buffer, data);
  }

  @Override public void blendingDisable()
    throws GLException
  {
    JOGL_GL_Functions.blendingDisable(this.context.getGL());
  }

  @Override public void blendingEnable(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnable(
      this.context.getGL(),
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
    JOGL_GL_Functions.blendingEnableSeparate(
      this.context.getGL(),
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor);
  }

  @Override public void blendingEnableSeparateWithEquationSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquationGL3 equation_rgb,
    final @Nonnull BlendEquationGL3 equation_alpha)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparate(
      this.context.getGL(),
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public void blendingEnableSeparateWithEquationSeparateES2(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquationGLES2 equation_rgb,
    final @Nonnull BlendEquationGLES2 equation_alpha)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparateES2(
      this.context.getGL(),
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public void blendingEnableWithEquation(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGL3 equation)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableWithEquation(
      this.context.getGL(),
      source_factor,
      destination_factor,
      equation);
  }

  @Override public void blendingEnableWithEquationES2(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableWithEquationES2(
      this.context.getGL(),
      source_factor,
      destination_factor,
      equation);
  }

  @Override public void blendingEnableWithEquationSeparate(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGL3 equation_rgb,
    final @Nonnull BlendEquationGL3 equation_alpha)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableWithEquationSeparate(
      this.context.getGL(),
      source_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public void blendingEnableWithEquationSeparateES2(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation_rgb,
    final @Nonnull BlendEquationGLES2 equation_alpha)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.blendingEnableWithEquationSeparateES2(
      this.context.getGL(),
      source_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public boolean blendingIsEnabled()
    throws ConstraintError,
      GLException
  {
    return JOGL_GL_Functions.blendingIsEnabled(
      this.context.getGL(),
      this.state);
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.colorBufferClear3f(this.context.getGL(), r, g, b);
  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.colorBufferClear4f(this.context.getGL(), r, g, b, a);
  }

  @Override public void colorBufferClearV3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.colorBufferClearV3f(this.context.getGL(), color);
  }

  @Override public void colorBufferClearV4f(
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.colorBufferClearV4f(this.context.getGL(), color);
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.colorBufferMask(this.context.getGL(), r, g, b, a);
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws GLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusAlpha(
      this.context.getGL(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws GLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusBlue(
      this.context.getGL(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws GLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusGreen(
      this.context.getGL(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws GLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusRed(
      this.context.getGL(),
      this.state);
  }

  @Override public void cullingDisable()
    throws GLException
  {
    JOGL_GL_Functions.cullingDisable(this.context.getGL());
  }

  @Override public void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.cullingEnable(this.context.getGL(), faces, order);
  }

  @Override public boolean cullingIsEnabled()
    throws GLException
  {
    return JOGL_GL_Functions.cullingIsEnabled(this.context.getGL());
  }

  @Override public void depthBufferClear(
    final float depth)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.depthBufferClear(
      this.context.getGL(),
      this.state,
      depth);
  }

  @Override public void depthBufferDisable()
    throws GLException
  {
    JOGL_GL_Functions.depthBufferDisable(this.context.getGL());
  }

  @Override public void depthBufferEnable(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.depthBufferEnable(
      this.context.getGL(),
      this.state,
      function);
  }

  @Override public int depthBufferGetBits()
    throws GLException
  {
    return JOGL_GL_Functions.depthBufferGetBits(
      this.context.getGL(),
      this.state);
  }

  @Override public boolean depthBufferIsEnabled()
    throws GLException
  {
    return JOGL_GL_Functions.depthBufferIsEnabled(this.context.getGL());
  }

  @Override public void depthBufferWriteDisable()
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.depthBufferWriteDisable(
      this.context.getGL(),
      this.state);
  }

  @Override public void depthBufferWriteEnable()
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions
      .depthBufferWriteEnable(this.context.getGL(), this.state);
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws GLException
  {
    return JOGL_GL_Functions.depthBufferWriteIsEnabled(
      this.context.getGL(),
      this.state);
  }

  @Override public void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBuffer indices)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.drawElements(this.context.getGL(), mode, indices);
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
    JOGL_GL2ES2_Functions.fragmentShaderAttach(this.context
      .getGL()
      .getGL2ES2(), this.state, this.log, program, shader);
  }

  @Override public FragmentShader fragmentShaderCompile(
    final @Nonnull String name,
    final @Nonnull InputStream stream)
    throws ConstraintError,
      GLCompileException,
      IOException,
      GLException
  {
    return JOGL_GL2ES2_Functions.fragmentShaderCompile(this.context
      .getGL()
      .getGL2ES2(), this.state, this.log, name, stream);
  }

  @Override public void fragmentShaderDelete(
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.fragmentShaderDelete(this.context
      .getGL()
      .getGL2ES2(), this.state, this.log, id);
  }

  @Override public @Nonnull FramebufferReference framebufferAllocate()
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferAllocate(
      this.context.getGL(),
      this.state,
      this.log);
  }

  @Override public void framebufferDelete(
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDelete(
      this.context.getGL(),
      this.state,
      this.log,
      framebuffer);
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferDrawAnyIsBound(this.context.getGL());
  }

  @Override public void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorRenderbuffer(
      this.context.getGL(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachColorRenderbufferAt(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorRenderbufferAt(
      this.context.getGL(),
      this.state,
      this.log,
      framebuffer,
      point,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachColorTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTexture2D(
      this.context.getGL(),
      this.state,
      this.log,
      framebuffer,
      texture);
  }

  @Override public void framebufferDrawAttachColorTexture2DAt(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull Texture2DStaticUsable texture)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTexture2DAt(
      this.context.getGL(),
      this.state,
      this.log,
      framebuffer,
      point,
      texture);
  }

  @Override public void framebufferDrawAttachColorTextureCube(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFace face)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTextureCube(
      this.context.getGL(),
      this.state,
      this.log,
      framebuffer,
      texture,
      face);
  }

  @Override public void framebufferDrawAttachColorTextureCubeAt(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFace face)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTextureCubeAt(
      this.context.getGL(),
      this.state,
      this.log,
      framebuffer,
      point,
      texture,
      face);
  }

  @Override public void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepth> renderbuffer)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachDepthRenderbuffer(
      this.context.getGL(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachDepthStencilRenderbuffer(
      this.context.getGL(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachDepthTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachDepthTexture2D(
      this.context.getGL(),
      this.state,
      this.log,
      framebuffer,
      texture);
  }

  @Override public void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableStencil> renderbuffer)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachStencilRenderbuffer(
      this.context.getGL(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawBind(
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawBind(this.context.getGL(), framebuffer);
  }

  @Override public boolean framebufferDrawIsBound(
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferDrawIsBound(
      this.context.getGL(),
      framebuffer);
  }

  @Override public
    void
    framebufferDrawSetBuffers(
      final @Nonnull FramebufferReference framebuffer,
      final @Nonnull Map<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> mappings)
      throws GLException,
        ConstraintError
  {
    JOGL_GL2ES3_Functions.framebufferDrawSetBuffers(this.context
      .getGL()
      .getGL2GL3(), this.state, this.log, framebuffer, mappings);
  }

  @Override public void framebufferDrawUnbind()
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawUnbind(this.context.getGL());
  }

  @Override public @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferDrawValidate(
      this.context.getGL(),
      framebuffer);
  }

  @Override public @Nonnull
    FramebufferColorAttachmentPoint[]
    framebufferGetColorAttachmentPoints()
      throws GLException,
        ConstraintError
  {
    return this.state.color_attachments;
  }

  @Override public @Nonnull
    FramebufferDrawBuffer[]
    framebufferGetDrawBuffers()
      throws GLException,
        ConstraintError
  {
    return this.state.draw_buffers;
  }

  @Override public IndexBuffer indexBufferAllocate(
    final @Nonnull ArrayBuffer buffer,
    final int indices)
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferAllocate(
      this.context.getGL(),
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
    return JOGL_GL_Functions.indexBufferAllocateType(
      this.context.getGL(),
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
    JOGL_GL_Functions.indexBufferDelete(
      this.context.getGL(),
      this.state,
      this.log,
      id);
  }

  @Override public IndexBufferReadableMap indexBufferMapRead(
    final @Nonnull IndexBuffer id)
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferMapRead(
      this.context.getGL(),
      this.state,
      this.log,
      id);
  }

  @Override public @Nonnull IndexBufferReadableMap indexBufferMapReadRange(
    final @Nonnull IndexBuffer id,
    final @Nonnull RangeInclusive range)
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferMapReadRange(
      this.context.getGL(),
      this.state,
      this.log,
      id,
      range);
  }

  @Override public IndexBufferWritableMap indexBufferMapWrite(
    final @Nonnull IndexBuffer id)
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferMapWrite(
      this.context.getGL(),
      this.state,
      this.log,
      id);
  }

  @Override public void indexBufferUnmap(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.indexBufferUnmap(
      this.context.getGL(),
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
    JOGL_GL_Functions.indexBufferUpdate(this.context.getGL(), buffer, data);
  }

  @Override public void logicOperationsDisable()
    throws GLException
  {
    JOGL_GL2GL3_Functions.logicOperationsDisable(this.context
      .getGL()
      .getGL2GL3());
  }

  @Override public void logicOperationsEnable(
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2GL3_Functions.logicOperationsEnable(this.context
      .getGL()
      .getGL2GL3(), operation);
  }

  @Override public boolean logicOperationsEnabled()
    throws GLException
  {
    return JOGL_GL2GL3_Functions.logicOperationsEnabled(this.context
      .getGL()
      .getGL2GL3());
  }

  @Override public int metaGetError()
    throws GLException
  {
    return JOGL_GL_Functions.metaGetError(this.context.getGL());
  }

  @Override public String metaGetRenderer()
    throws GLException
  {
    return JOGL_GL_Functions.metaGetRenderer(this.context.getGL());
  }

  @Override public String metaGetVendor()
    throws GLException
  {
    return JOGL_GL_Functions.metaGetVendor(this.context.getGL());
  }

  @Override public String metaGetVersion()
    throws GLException
  {
    return JOGL_GL_Functions.metaGetVersion(this.context.getGL());
  }

  @Override public int metaGetVersionMajor()
  {
    return JOGL_GL_Functions.metaGetVersionMajor(this.context.getGL());
  }

  @Override public int metaGetVersionMinor()
  {
    return JOGL_GL_Functions.metaGetVersionMinor(this.context.getGL());
  }

  @Override public boolean metaIsES()
  {
    return JOGL_GL_Functions.metaIsES(this.context.getGL());
  }

  @Override public @Nonnull PolygonMode polygonGetMode()
    throws ConstraintError,
      GLException
  {
    return this.state.polygon_mode;
  }

  @Override public void polygonSetMode(
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2GL3_Functions.polygonSetMode(
      this.context.getGL().getGL2GL3(),
      this.state,
      mode);
  }

  @Override public void polygonSmoothingDisable()
    throws GLException
  {
    JOGL_GL2GL3_Functions.polygonSmoothingDisable(this.context
      .getGL()
      .getGL2GL3());
  }

  @Override public void polygonSmoothingEnable()
    throws GLException
  {
    JOGL_GL2GL3_Functions.polygonSmoothingEnable(this.context
      .getGL()
      .getGL2GL3());
  }

  @Override public boolean polygonSmoothingIsEnabled()
    throws GLException
  {
    return JOGL_GL2GL3_Functions.polygonSmoothingIsEnabled(this.context
      .getGL()
      .getGL2GL3());
  }

  @Override public void programActivate(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    final GL2ES2 gl = this.context.getGL().getGL2ES2();

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
    return JOGL_GL2ES2_Functions.programCreate(this.context
      .getGL()
      .getGL2ES2(), this.state, this.log, name);
  }

  @Override public void programDeactivate()
    throws GLException
  {
    JOGL_GL2ES2_Functions.programDeactivate(this.context.getGL().getGL2ES2());
  }

  @Override public void programDelete(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.programDelete(
      this.context.getGL().getGL2ES2(),
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
    JOGL_GL2ES2_Functions.programGetAttributes(this.context
      .getGL()
      .getGL2ES2(), this.state, this.log, program, out);
  }

  @Override public int programGetMaximumActiveAttributes()
    throws GLException
  {
    return JOGL_GL2ES2_Functions.programGetMaximumActiveAttributes(
      this.context.getGL().getGL2ES2(),
      this.state,
      this.log);
  }

  @Override public void programGetUniforms(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramUniform> out)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.programGetUniforms(
      this.context.getGL().getGL2ES2(),
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
    return JOGL_GL2ES2_Functions.programIsActive(this.context
      .getGL()
      .getGL2ES2(), this.state, program);
  }

  @Override public void programLink(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    JOGL_GL2ES2_Functions.programLink(
      this.context.getGL().getGL2ES2(),
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
    JOGL_GL2ES2_Functions.programPutUniformFloat(this.context
      .getGL()
      .getGL2ES2(), this.state, uniform, value);
  }

  @Override public void programPutUniformMatrix3x3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.programPutUniformMatrix3x3f(this.context
      .getGL()
      .getGL2ES2(), this.state, uniform, matrix);
  }

  @Override public void programPutUniformMatrix4x4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable4x4F matrix)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.programPutUniformMatrix4x4f(this.context
      .getGL()
      .getGL2ES2(), this.state, uniform, matrix);
  }

  @Override public void programPutUniformTextureUnit(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.programPutUniformTextureUnit(this.context
      .getGL()
      .getGL2ES2(), this.state, uniform, unit);
  }

  @Override public void programPutUniformVector2f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector2f(this.context
      .getGL()
      .getGL2ES2(), this.state, uniform, vector);
  }

  @Override public void programPutUniformVector2i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector2i(this.context
      .getGL()
      .getGL2ES2(), this.state, uniform, vector);
  }

  @Override public void programPutUniformVector3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector3f(this.context
      .getGL()
      .getGL2ES2(), this.state, uniform, vector);
  }

  @Override public void programPutUniformVector4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector4f(this.context
      .getGL()
      .getGL2ES2(), this.state, uniform, vector);
  }

  @Override public @Nonnull
    Renderbuffer<RenderableDepthStencil>
    renderbufferAllocateDepth24Stencil8(
      final int width,
      final int height)
      throws ConstraintError,
        GLException
  {
    return Renderbuffer.unsafeBrandDepthStencil(JOGL_GL_Functions
      .renderbufferAllocate(
        this.context.getGL().getGL2ES2(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableColor>
    renderbufferAllocateRGB888(
      final int width,
      final int height)
      throws ConstraintError,
        GLException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.context.getGL().getGL2ES2(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_COLOR_RGB_888,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableColor>
    renderbufferAllocateRGBA8888(
      final int width,
      final int height)
      throws ConstraintError,
        GLException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.context.getGL().getGL2ES2(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_COLOR_RGBA_8888,
        width,
        height));
  }

  @Override public void renderbufferDelete(
    final @Nonnull Renderbuffer<?> buffer)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.renderbufferDelete(
      this.context.getGL().getGL2ES2(),
      this.state,
      this.log,
      buffer);
  }

  @Override public void scissorDisable()
    throws GLException
  {
    JOGL_GL_Functions.scissorDisable(this.context.getGL());
  }

  @Override public void scissorEnable(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.scissorEnable(
      this.context.getGL(),
      position,
      dimensions);
  }

  @Override public boolean scissorIsEnabled()
    throws GLException
  {
    return JOGL_GL_Functions.scissorIsEnabled(this.context.getGL());
  }

  @Override public void stencilBufferClear(
    final int stencil)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.stencilBufferClear(
      this.context.getGL(),
      this.state,
      stencil);
  }

  @Override public void stencilBufferDisable()
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.stencilBufferDisable(this.context.getGL());
  }

  @Override public void stencilBufferEnable()
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.stencilBufferEnable(this.context.getGL(), this.state);
  }

  @Override public void stencilBufferFunction(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.stencilBufferFunction(this.context
      .getGL()
      .getGL2ES2(), faces, function, reference, mask);
  }

  @Override public int stencilBufferGetBits()
    throws GLException
  {
    return JOGL_GL_Functions.stencilBufferGetBits(
      this.context.getGL(),
      this.state);
  }

  @Override public boolean stencilBufferIsEnabled()
    throws GLException
  {
    return JOGL_GL_Functions.stencilBufferIsEnabled(this.context.getGL());
  }

  @Override public void stencilBufferMask(
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.stencilBufferMask(
      this.context.getGL().getGL2ES2(),
      faces,
      mask);
  }

  @Override public void stencilBufferOperation(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.stencilBufferOperation(this.context
      .getGL()
      .getGL2ES2(), faces, stencil_fail, depth_fail, pass);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateDepth16(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_DEPTH_16_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateDepth24(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_DEPTH_24_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateDepth32f(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_DEPTH_32F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateR8(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_R_8_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRG88(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RG_88_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB565(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGB_565_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB888(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA4444(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_4444_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA5551(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_5551_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA8888(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public void texture2DStaticBind(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions
      .texture2DStaticBind(this.context.getGL(), unit, texture);
  }

  @Override public void texture2DStaticDelete(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.texture2DStaticDelete(
      this.context.getGL(),
      this.state,
      this.log,
      texture);
  }

  @Override public boolean texture2DStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL_Functions.texture2DStaticIsBound(
      this.context.getGL(),
      this.state,
      unit,
      texture);
  }

  @Override public void texture2DStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.texture2DStaticUnbind(this.context.getGL(), unit);
  }

  @Override public void texture2DStaticUpdate(
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2GL3_Functions.texture2DStaticUpdate(this.context.getGL(), data);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateDepth16(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        GLException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_DEPTH_16_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateDepth24(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        GLException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_DEPTH_24_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateDepth32f(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        GLException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_DEPTH_32F_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateR8(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_R_8_1BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG88(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RG_88_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGB565(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        GLException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGB_565_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGB888(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        GLException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGB_888_3BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGBA4444(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        GLException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_4444_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGBA5551(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        GLException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_5551_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGBA8888(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        GLException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.context.getGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_8888_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public void textureCubeStaticBind(
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticUsable texture)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.textureCubeStaticBind(
      this.context.getGL(),
      unit,
      texture);
  }

  @Override public void textureCubeStaticDelete(
    final @Nonnull TextureCubeStatic texture)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.textureCubeStaticDelete(
      this.context.getGL(),
      this.state,
      this.log,
      texture);
  }

  @Override public boolean textureCubeStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticUsable texture)
    throws ConstraintError,
      GLException
  {
    return JOGL_GL_Functions.textureCubeStaticIsBound(
      this.context.getGL(),
      this.state,
      unit,
      texture);
  }

  @Override public void textureCubeStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.textureCubeStaticUnbind(this.context.getGL(), unit);
  }

  @Override public void textureCubeStaticUpdate(
    final @Nonnull CubeMapFace face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2GL3_Functions.textureCubeStaticUpdate(
      this.context.getGL(),
      face,
      data);
  }

  @Override public int textureGetMaximumSize()
    throws GLException
  {
    return JOGL_GL_Functions.textureGetMaximumSize(
      this.context.getGL(),
      this.state);
  }

  @Override public TextureUnit[] textureGetUnits()
    throws GLException
  {
    return this.state.texture_units;
  }

  @Override public void vertexShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull VertexShader shader)
    throws ConstraintError,
      GLCompileException,
      GLException
  {
    JOGL_GL2ES2_Functions.vertexShaderAttach(
      this.context.getGL().getGL2ES2(),
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
    return JOGL_GL2ES2_Functions.vertexShaderCompile(this.context
      .getGL()
      .getGL2ES2(), this.state, this.log, name, stream);
  }

  @Override public void vertexShaderDelete(
    final @Nonnull VertexShader id)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.vertexShaderDelete(
      this.context.getGL().getGL2ES2(),
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
    JOGL_GL_Functions.viewportSet(this.context.getGL(), position, dimensions);
  }
}