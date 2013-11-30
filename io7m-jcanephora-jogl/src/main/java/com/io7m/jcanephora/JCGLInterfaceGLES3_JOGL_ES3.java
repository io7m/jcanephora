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

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.opengl.DebugGLES3;
import javax.media.opengl.GL;
import javax.media.opengl.GL3ES3;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLES3;
import javax.media.opengl.TraceGLES3;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable3I;
import com.io7m.jtensors.VectorReadable4F;
import com.io7m.jtensors.VectorReadable4I;

/**
 * <p>
 * A class implementing {@link JCGLInterfaceGLES3} that uses only the features
 * of OpenGL ES3, using JOGL as the backend.
 * </p>
 * <p>
 * A {@link javax.media.opengl.GLContext} is used to construct the interface,
 * and therefore the <code>GLInterfaceGL3_JOGL_GLES3</code> interface has the
 * same thread safe/unsafe behaviour.
 * </p>
 * <p>
 * The <code>GLInterfaceGL3_JOGL_GLES3</code> implementation does not call
 * {@link javax.media.opengl.GLContext#makeCurrent()} or
 * {@link javax.media.opengl.GLContext#release()}, so these calls must be made
 * by the programmer when necessary (typically, programs call
 * {@link javax.media.opengl.GLContext#makeCurrent()}, perform all rendering,
 * and then call {@link javax.media.opengl.GLContext#release()} at the end of
 * the frame). The JOGL library can also optionally manage this via the
 * {@link javax.media.opengl.GLAutoDrawable} interface.
 * </p>
 */

@NotThreadSafe final class JCGLInterfaceGLES3_JOGL_ES3 implements
  JCGLInterfaceGLES3
{
  private @Nonnull GLES3                cached_gl;
  private final @Nonnull GLContext      gl_context;
  private final @Nonnull Log            log;
  private final @Nonnull JCGLSLVersion  sl_version;
  private final @Nonnull JCGLStateCache state;
  private final @Nonnull JCGLVersion    version;

  JCGLInterfaceGLES3_JOGL_ES3(
    final @Nonnull GLContext context,
    final @Nonnull Log log,
    final @Nonnull JCGLDebugging debug,
    final @CheckForNull PrintStream trace_out)
    throws ConstraintError,
      JCGLException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "jogl30");
    this.gl_context = Constraints.constrainNotNull(context, "GL context");
    Constraints.constrainNotNull(debug, "Debug");

    this.state = new JCGLStateCache();

    {
      final GLES3 g = this.gl_context.getGL().getGLES3();
      switch (debug) {
        case JCGL_DEBUGGING:
          this.cached_gl = new DebugGLES3(g);
          break;
        case JCGL_NONE:
          this.cached_gl = g;
          break;
        case JCGL_TRACING:
          Constraints.constrainNotNull(trace_out, "Trace output");
          this.cached_gl = new TraceGLES3(g, trace_out);
          break;
        case JCGL_TRACING_AND_DEBUGGING:
          Constraints.constrainNotNull(trace_out, "Trace output");
          this.cached_gl = new DebugGLES3(new TraceGLES3(g, trace_out));
          break;
        default:
          throw new UnreachableCodeException();
      }
    }
    assert this.cached_gl != null;
    final GL3ES3 g = this.contextGetGL();

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

    this.version = JOGL_GL_Functions.metaGetVersion(g);
    this.sl_version = JOGL_GL_Functions.metaGetSLVersion(log, g);
  }

  @Override public ArrayBuffer arrayBufferAllocate(
    final long elements,
    final @Nonnull ArrayBufferTypeDescriptor descriptor,
    final @Nonnull UsageHint usage)
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.arrayBufferAllocate(
      this.contextGetGL3(),
      this.log,
      this.state,
      elements,
      descriptor,
      usage);
  }

  @Override public void arrayBufferBind(
    final @Nonnull ArrayBufferUsable buffer)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferBind(this.contextGetGL3(), buffer);
  }

  @Override public void arrayBufferDelete(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.arrayBufferDelete(
      this.contextGetGL3(),
      this.log,
      this.state,
      id);
  }

  @Override public boolean arrayBufferIsBound(
    final @Nonnull ArrayBufferUsable id)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GL_Functions.arrayBufferIsBound(this.contextGetGL3(), id);
  }

  @Override public ByteBuffer arrayBufferMapReadUntyped(
    final @Nonnull ArrayBufferUsable id)
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.arrayBufferMapRead(
      this.contextGetGL3(),
      this.state,
      this.log,
      id);
  }

  @Override public @Nonnull ByteBuffer arrayBufferMapReadUntypedRange(
    final @Nonnull ArrayBufferUsable id,
    final @Nonnull RangeInclusive range)
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.arrayBufferMapReadRange(
      this.contextGetGL(),
      this.state,
      this.log,
      id,
      range);
  }

  @Override public ArrayBufferWritableMap arrayBufferMapWrite(
    final @Nonnull ArrayBuffer id)
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.arrayBufferMapWrite(
      this.contextGetGL3(),
      this.state,
      this.log,
      id);
  }

  @Override public void arrayBufferUnbind()
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferUnbind(this.contextGetGL3());
  }

  @Override public void arrayBufferUnmap(
    final @Nonnull ArrayBufferUsable id)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.arrayBufferUnmap(
      this.contextGetGL3(),
      this.state,
      this.log,
      id);
  }

  @Override public void arrayBufferUpdate(
    final @Nonnull ArrayBufferWritableData data)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferUpdate(this.contextGetGL3(), data);
  }

  @Override public void blendingDisable()
    throws JCGLException
  {
    JOGL_GL_Functions.blendingDisable(this.contextGetGL3());
  }

  @Override public void blendingEnable(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.blendingEnable(
      this.contextGetGL3(),
      source_factor,
      destination_factor);
  }

  @Override public void blendingEnableSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.blendingEnableSeparate(
      this.contextGetGL3(),
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
      JCGLException
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparate(
      this.contextGetGL3(),
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
      JCGLException
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparateES2(
      this.contextGetGL3(),
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
      JCGLException
  {
    JOGL_GL_Functions.blendingEnableWithEquation(
      this.contextGetGL3(),
      source_factor,
      destination_factor,
      equation);
  }

  @Override public void blendingEnableWithEquationES2(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.blendingEnableWithEquationES2(
      this.contextGetGL3(),
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
      JCGLException
  {
    JOGL_GL_Functions.blendingEnableWithEquationSeparate(
      this.contextGetGL3(),
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
      JCGLException
  {
    JOGL_GL_Functions.blendingEnableWithEquationSeparateES2(
      this.contextGetGL3(),
      source_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public boolean blendingIsEnabled()
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GL_Functions.blendingIsEnabled(
      this.contextGetGL3(),
      this.state);
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.colorBufferClear3f(this.contextGetGL3(), r, g, b);
  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.colorBufferClear4f(this.contextGetGL3(), r, g, b, a);
  }

  @Override public void colorBufferClearV3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.colorBufferClearV3f(this.contextGetGL3(), color);
  }

  @Override public void colorBufferClearV4f(
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.colorBufferClearV4f(this.contextGetGL3(), color);
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.colorBufferMask(this.contextGetGL3(), r, g, b, a);
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws JCGLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusAlpha(
      this.contextGetGL3(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws JCGLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusBlue(
      this.contextGetGL3(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws JCGLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusGreen(
      this.contextGetGL3(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws JCGLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusRed(
      this.contextGetGL3(),
      this.state);
  }

  private @Nonnull GL3ES3 contextGetGL()
  {
    return this.cached_gl;
  }

  private GL3ES3 contextGetGL3()
  {
    return this.contextGetGL().getGL3ES3();
  }

  @Override public void cullingDisable()
    throws JCGLException
  {
    JOGL_GL_Functions.cullingDisable(this.contextGetGL3());
  }

  @Override public void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.cullingEnable(this.contextGetGL3(), faces, order);
  }

  @Override public boolean cullingIsEnabled()
    throws JCGLException
  {
    return JOGL_GL_Functions.cullingIsEnabled(this.contextGetGL3());
  }

  @Override public void depthBufferClear(
    final float depth)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.depthBufferClear(
      this.contextGetGL3(),
      this.state,
      depth);
  }

  @Override public int depthBufferGetBits()
    throws JCGLException
  {
    return JOGL_GL_Functions.depthBufferGetBits(
      this.contextGetGL3(),
      this.state);
  }

  @Override public void depthBufferTestDisable()
    throws JCGLException
  {
    JOGL_GL_Functions.depthBufferDisable(this.contextGetGL3());
  }

  @Override public void depthBufferTestEnable(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.depthBufferEnable(
      this.contextGetGL3(),
      this.state,
      function);
  }

  @Override public boolean depthBufferTestIsEnabled()
    throws JCGLException
  {
    return JOGL_GL_Functions.depthBufferIsEnabled(this.contextGetGL3());
  }

  @Override public void depthBufferWriteDisable()
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.depthBufferWriteDisable(
      this.contextGetGL3(),
      this.state);
  }

  @Override public void depthBufferWriteEnable()
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions
      .depthBufferWriteEnable(this.contextGetGL3(), this.state);
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws JCGLException
  {
    return JOGL_GL_Functions.depthBufferWriteIsEnabled(
      this.contextGetGL3(),
      this.state);
  }

  @Override public void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBufferUsable indices)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.drawElements(this.contextGetGL3(), mode, indices);
  }

  @Override public boolean errorCodeIsInvalidOperation(
    final int code)
  {
    return code == GL.GL_INVALID_OPERATION;
  }

  @Override public FragmentShader fragmentShaderCompile(
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException
  {
    return JOGL_GL2ES2_Functions.fragmentShaderCompile(
      this.contextGetGL3(),
      this.state,
      this.log,
      name,
      lines);
  }

  @Override public void fragmentShaderDelete(
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.fragmentShaderDelete(
      this.contextGetGL3(),
      this.state,
      this.log,
      id);
  }

  @Override public @Nonnull FramebufferReference framebufferAllocate()
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferAllocate(
      this.contextGetGL3(),
      this.state,
      this.log);
  }

  @Override public void framebufferDelete(
    final @Nonnull FramebufferReference framebuffer)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDelete(
      this.contextGetGL3(),
      this.state,
      this.log,
      framebuffer);
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws JCGLException
  {
    return JOGL_GL_Functions.framebufferDrawAnyIsBound(this.contextGetGL3());
  }

  @Override public void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorRenderbuffer(
      this.contextGetGL3(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachColorRenderbufferAt(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorRenderbufferAt(
      this.contextGetGL3(),
      this.state,
      this.log,
      framebuffer,
      point,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachColorTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTexture2D(
      this.contextGetGL3(),
      this.state,
      this.log,
      this.version,
      framebuffer,
      texture);
  }

  @Override public void framebufferDrawAttachColorTexture2DAt(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTexture2DAt(
      this.contextGetGL3(),
      this.state,
      this.log,
      this.version,
      framebuffer,
      point,
      texture);
  }

  @Override public void framebufferDrawAttachColorTextureCube(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFaceLH face)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTextureCube(
      this.contextGetGL3(),
      this.state,
      this.log,
      this.version,
      framebuffer,
      texture,
      face);
  }

  @Override public void framebufferDrawAttachColorTextureCubeAt(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFaceLH face)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTextureCubeAt(
      this.contextGetGL3(),
      this.state,
      this.log,
      this.version,
      framebuffer,
      point,
      texture,
      face);
  }

  @Override public void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepth> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachDepthRenderbuffer(
      this.contextGetGL3(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachDepthStencilRenderbuffer(
      this.contextGetGL3(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachDepthTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachDepthTexture2D(
      this.contextGetGL3(),
      this.state,
      this.log,
      framebuffer,
      texture);
  }

  @Override public void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableStencil> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachStencilRenderbuffer(
      this.contextGetGL3(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawBind(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawBind(this.contextGetGL3(), framebuffer);
  }

  @Override public boolean framebufferDrawIsBound(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferDrawIsBound(
      this.contextGetGL3(),
      framebuffer);
  }

  @Override public
    void
    framebufferDrawSetBuffers(
      final @Nonnull FramebufferReference framebuffer,
      final @Nonnull Map<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> mappings)
      throws JCGLException,
        ConstraintError
  {
    JOGL_GL2ES3_Functions.framebufferDrawSetBuffers(
      this.contextGetGL3(),
      this.state,
      this.log,
      framebuffer,
      mappings);
  }

  @Override public void framebufferDrawUnbind()
    throws JCGLException
  {
    JOGL_GL_Functions.framebufferDrawUnbind(this.contextGetGL3());
  }

  @Override public @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferDrawValidate(
      this.contextGetGL3(),
      framebuffer);
  }

  @Override public @Nonnull
    List<FramebufferColorAttachmentPoint>
    framebufferGetColorAttachmentPoints()
      throws JCGLException,
        ConstraintError
  {
    return Collections.unmodifiableList(this.state.color_attachments);
  }

  @Override public @Nonnull
    List<FramebufferDrawBuffer>
    framebufferGetDrawBuffers()
      throws JCGLException,
        ConstraintError
  {
    return Collections.unmodifiableList(this.state.draw_buffers);
  }

  @Override public IndexBuffer indexBufferAllocate(
    final @Nonnull ArrayBufferUsable buffer,
    final int indices)
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferAllocate(
      this.contextGetGL3(),
      this.state,
      this.log,
      buffer,
      indices);
  }

  @Override public @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull JCGLUnsignedType type,
    final int indices)
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferAllocateType(
      this.contextGetGL3(),
      this.state,
      this.log,
      type,
      indices);
  }

  @Override public void indexBufferDelete(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.indexBufferDelete(
      this.contextGetGL3(),
      this.state,
      this.log,
      id);
  }

  @Override public IndexBufferReadableMap indexBufferMapRead(
    final @Nonnull IndexBufferUsable id)
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferMapRead(
      this.contextGetGL3(),
      this.state,
      this.log,
      id);
  }

  @Override public @Nonnull IndexBufferReadableMap indexBufferMapReadRange(
    final @Nonnull IndexBufferUsable id,
    final @Nonnull RangeInclusive range)
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferMapReadRange(
      this.contextGetGL(),
      this.state,
      this.log,
      id,
      range);
  }

  @Override public IndexBufferWritableMap indexBufferMapWrite(
    final @Nonnull IndexBuffer id)
    throws JCGLException,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferMapWrite(
      this.contextGetGL3(),
      this.state,
      this.log,
      id);
  }

  @Override public void indexBufferUnmap(
    final @Nonnull IndexBufferUsable id)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.indexBufferUnmap(
      this.contextGetGL3(),
      this.state,
      this.log,
      id);
  }

  @Override public void indexBufferUpdate(
    final @Nonnull IndexBufferWritableData data)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.indexBufferUpdate(this.contextGetGL3(), data);
  }

  @Override public int metaGetError()
    throws JCGLException
  {
    return JOGL_GL_Functions.metaGetError(this.contextGetGL3());
  }

  @Override public String metaGetRenderer()
    throws JCGLException
  {
    return JOGL_GL_Functions.metaGetRenderer(this.contextGetGL3());
  }

  @Override public @Nonnull JCGLSLVersion metaGetSLVersion()
    throws JCGLException
  {
    return this.sl_version;
  }

  @Override public String metaGetVendor()
    throws JCGLException
  {
    return JOGL_GL_Functions.metaGetVendor(this.contextGetGL3());
  }

  @Override public @Nonnull JCGLVersion metaGetVersion()
    throws JCGLException
  {
    return this.version;
  }

  @Override public void programActivate(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programActivate(this.contextGetGL3(), program);
  }

  @Override public void programAttributeArrayAssociate(
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull ArrayBufferAttribute array_attribute)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributeArrayBind(
      this.contextGetGL3(),
      this.state,
      program_attribute,
      array_attribute);
  }

  @Override public void programAttributeArrayDisassociate(
    final @Nonnull ProgramAttribute program_attribute)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributeArrayDisassociate(
      this.contextGetGL3(),
      this.state,
      program_attribute);
  }

  @Override public void programAttributePutFloat(
    final @Nonnull ProgramAttribute program_attribute,
    final float x)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutFloat(
      this.contextGetGL3(),
      this.state,
      program_attribute,
      x);
  }

  @Override public void programAttributePutVector2f(
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull VectorReadable2F x)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutVector2f(
      this.contextGetGL3(),
      this.state,
      program_attribute,
      x);
  }

  @Override public void programAttributePutVector3f(
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull VectorReadable3F x)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutVector3f(
      this.contextGetGL3(),
      this.state,
      program_attribute,
      x);
  }

  @Override public void programAttributePutVector4f(
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull VectorReadable4F x)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutVector4f(
      this.contextGetGL3(),
      this.state,
      program_attribute,
      x);
  }

  @Override public ProgramReference programCreateCommon(
    final @Nonnull String name,
    final @Nonnull VertexShader v,
    final @Nonnull FragmentShader f)
    throws ConstraintError,
      JCGLException,
      JCGLCompileException
  {
    return JOGL_GL2ES2_Functions.programCreateCommon(
      this.contextGetGL3(),
      this.state,
      this.log,
      name,
      v,
      f);
  }

  @Override public void programDeactivate()
    throws JCGLException
  {
    JOGL_GL2ES2_Functions.programDeactivate(this.contextGetGL3());
  }

  @Override public void programDelete(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programDelete(
      this.contextGetGL3(),
      this.state,
      this.log,
      program);
  }

  @Override public int programGetMaximumActiveAttributes()
    throws JCGLException
  {
    return JOGL_GL2ES2_Functions.programGetMaximumActiveAttributes(
      this.contextGetGL3(),
      this.state,
      this.log);
  }

  @Override public boolean programIsActive(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GL2ES2_Functions.programIsActive(
      this.contextGetGL3(),
      this.state,
      program);
  }

  @Override public void programUniformPutFloat(
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programPutUniformFloat(
      this.contextGetGL3(),
      this.state,
      uniform,
      value);
  }

  @Override public void programUniformPutMatrix3x3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programPutUniformMatrix3x3f(
      this.contextGetGL3(),
      this.state,
      uniform,
      matrix);
  }

  @Override public void programUniformPutMatrix4x4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable4x4F matrix)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programPutUniformMatrix4x4f(
      this.contextGetGL3(),
      this.state,
      uniform,
      matrix);
  }

  @Override public void programUniformPutTextureUnit(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programPutUniformTextureUnit(
      this.contextGetGL3(),
      this.state,
      uniform,
      unit);
  }

  @Override public void programUniformPutVector2f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector2f(
      this.contextGetGL3(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector2i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector2i(
      this.contextGetGL3(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector3f(
      this.contextGetGL3(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector3i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3I vector)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector3i(
      this.contextGetGL3(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector4f(
      this.contextGetGL3(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector4i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4I vector)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector4i(
      this.contextGetGL3(),
      this.state,
      uniform,
      vector);
  }

  @Override public @Nonnull
    Renderbuffer<RenderableDepth>
    renderbufferAllocateDepth16(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLException
  {
    return Renderbuffer.unsafeBrandDepth(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_DEPTH_16,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableDepthStencil>
    renderbufferAllocateDepth24Stencil8(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLException
  {
    return Renderbuffer.unsafeBrandDepthStencil(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL3(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableColor>
    renderbufferAllocateRGB565(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableColor>
    renderbufferAllocateRGB888(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL3(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_COLOR_RGB_888,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableColor>
    renderbufferAllocateRGBA4444(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_COLOR_RGBA_4444,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableColor>
    renderbufferAllocateRGBA5551(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_COLOR_RGBA_5551,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableColor>
    renderbufferAllocateRGBA8888(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL3(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_COLOR_RGBA_8888,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableStencil>
    renderbufferAllocateStencil8(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLException
  {
    return Renderbuffer.unsafeBrandStencil(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_STENCIL_8,
        width,
        height));
  }

  @Override public void renderbufferDelete(
    final @Nonnull Renderbuffer<?> buffer)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.renderbufferDelete(
      this.contextGetGL3(),
      this.state,
      this.log,
      buffer);
  }

  @Override public void scissorDisable()
    throws JCGLException
  {
    JOGL_GL_Functions.scissorDisable(this.contextGetGL3());
  }

  @Override public void scissorEnable(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.scissorEnable(
      this.contextGetGL3(),
      position,
      dimensions);
  }

  @Override public boolean scissorIsEnabled()
    throws JCGLException
  {
    return JOGL_GL_Functions.scissorIsEnabled(this.contextGetGL3());
  }

  @Override public void stencilBufferClear(
    final int stencil)
    throws JCGLException,
      ConstraintError
  {
    JOGL_GL_Functions.stencilBufferClear(
      this.contextGetGL3(),
      this.state,
      stencil);
  }

  @Override public void stencilBufferDisable()
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.stencilBufferDisable(this.contextGetGL3());
  }

  @Override public void stencilBufferEnable()
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.stencilBufferEnable(this.contextGetGL3(), this.state);
  }

  @Override public void stencilBufferFunction(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.stencilBufferFunction(
      this.contextGetGL3(),
      faces,
      function,
      reference,
      mask);
  }

  @Override public int stencilBufferGetBits()
    throws JCGLException
  {
    return JOGL_GL_Functions.stencilBufferGetBits(
      this.contextGetGL3(),
      this.state);
  }

  @Override public boolean stencilBufferIsEnabled()
    throws JCGLException
  {
    return JOGL_GL_Functions.stencilBufferIsEnabled(this.contextGetGL3());
  }

  @Override public void stencilBufferMask(
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions
      .stencilBufferMask(this.contextGetGL3(), faces, mask);
  }

  @Override public void stencilBufferOperation(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.stencilBufferOperation(
      this.contextGetGL3(),
      faces,
      stencil_fail,
      depth_fail,
      pass);
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
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL3(),
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
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL3(),
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

  @Override public Texture2DStatic texture2DStaticAllocateDepth24Stencil8(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP,
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
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL3(),
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

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateR16f(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_R_16F_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateR16I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_R_16I_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateR16U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_R_16U_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateR32f(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_R_32F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateR32I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_R_32I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateR32U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_R_32U_4BPP,
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
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL3(),
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

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateR8I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_R_8I_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateR8U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_R_8U_1BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRG16f(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RG_16F_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRG16I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RG_16I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRG16U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RG_16U_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRG32f(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RG_32F_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRG32I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RG_32I_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRG32U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RG_32U_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRG8(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RG_8_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRG8I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RG_8I_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRG8U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RG_8U_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB16f(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGB_16F_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB16I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGB_16I_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB16U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGB_16U_6BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB32f(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGB_32F_12BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB32I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGB_32I_12BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB32U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGB_32U_12BPP,
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
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL3(),
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

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB8(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGB_8_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB8I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGB_8I_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB8U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGB_8U_3BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    Texture2DStatic
    texture2DStaticAllocateRGBA1010102(
      final @Nonnull String name,
      final int width,
      final int height,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_1010102_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA16f(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_16F_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA16I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_16I_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA16U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_16U_8BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA32f(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL3(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_32F_16BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA32I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_32I_16BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA32U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_32U_16BPP,
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
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL3(),
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
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL3(),
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

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA8(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_8_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA8I(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_8I_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA8U(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.texture2DStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureType.TEXTURE_TYPE_RGBA_8U_4BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public void texture2DStaticBind(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions
      .texture2DStaticBind(this.contextGetGL3(), unit, texture);
  }

  @Override public void texture2DStaticDelete(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.texture2DStaticDelete(
      this.contextGetGL3(),
      this.state,
      this.log,
      texture);
  }

  @Override public boolean texture2DStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GL_Functions.texture2DStaticIsBound(
      this.contextGetGL3(),
      this.state,
      unit,
      texture);
  }

  @Override public void texture2DStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.texture2DStaticUnbind(this.contextGetGL3(), unit);
  }

  @Override public void texture2DStaticUpdate(
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GLES3_Functions.texture2DStaticUpdate(this.contextGetGL3(), data);
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
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL3(),
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
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL3(),
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
    textureCubeStaticAllocateDepth24Stencil8(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.contextGetGL3(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP,
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
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL3(),
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

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateR16f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_R_16F_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateR16I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_R_16I_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateR16U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_R_16U_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateR32f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_R_32F_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateR32I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_R_32I_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateR32U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_R_32U_4BPP,
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
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL3(),
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

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateR8I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_R_8I_1BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateR8U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_R_8U_1BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG16f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RG_16F_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG16I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RG_16I_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG16U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RG_16U_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG32f(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RG_32F_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG32I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RG_32I_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG32U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RG_32U_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG8(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RG_8_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG8I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RG_8I_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRG8U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RG_8U_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGB16f(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGB_16F_6BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGB16I(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGB_16I_6BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGB16U(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGB_16U_6BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGB32f(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGB_32F_12BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGB32I(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGB_32I_12BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGB32U(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGB_32U_12BPP,
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
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL3(),
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

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB8(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL3(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGB_8_3BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB8I(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGB_8I_3BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB8U(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGB_8U_3BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGBA16f(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_16F_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGBA16I(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_16I_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGBA16U(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_16U_8BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGBA32f(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_32F_16BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGBA32I(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_32I_16BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGBA32U(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_32U_16BPP,
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
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL3(),
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
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL3(),
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

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGBA8(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL3(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_8_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGBA8I(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_8I_4BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    TextureCubeStatic
    textureCubeStaticAllocateRGBA8U(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLException
  {
    return JOGL_GLES3_Functions.textureCubeStaticAllocate(
      this.contextGetGL(),
      this.state,
      this.log,
      name,
      size,
      TextureType.TEXTURE_TYPE_RGBA_8U_4BPP,
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
      JCGLException
  {
    JOGL_GL_Functions.textureCubeStaticBind(
      this.contextGetGL3(),
      unit,
      texture);
  }

  @Override public void textureCubeStaticDelete(
    final @Nonnull TextureCubeStatic texture)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.textureCubeStaticDelete(
      this.contextGetGL3(),
      this.state,
      this.log,
      texture);
  }

  @Override public boolean textureCubeStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticUsable texture)
    throws ConstraintError,
      JCGLException
  {
    return JOGL_GL_Functions.textureCubeStaticIsBound(
      this.contextGetGL3(),
      this.state,
      unit,
      texture);
  }

  @Override public void textureCubeStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.textureCubeStaticUnbind(this.contextGetGL3(), unit);
  }

  @Override public void textureCubeStaticUpdateLH(
    final @Nonnull CubeMapFaceLH face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GLES3_Functions.textureCubeStaticUpdate(
      this.contextGetGL3(),
      face,
      data);
  }

  @Override public void textureCubeStaticUpdateRH(
    final @Nonnull CubeMapFaceRH face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(face, "Face");
    this.textureCubeStaticUpdateLH(CubeMapFaceLH.fromRH(face), data);
  }

  @Override public int textureGetMaximumSize()
    throws JCGLException
  {
    return JOGL_GL_Functions.textureGetMaximumSize(
      this.contextGetGL3(),
      this.state);
  }

  @Override public List<TextureUnit> textureGetUnits()
    throws JCGLException
  {
    return Collections.unmodifiableList(this.state.texture_units);
  }

  @Override public VertexShader vertexShaderCompile(
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException
  {
    return JOGL_GL2ES2_Functions.vertexShaderCompile(
      this.contextGetGL3(),
      this.state,
      this.log,
      name,
      lines);
  }

  @Override public void vertexShaderDelete(
    final @Nonnull VertexShader id)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL2ES2_Functions.vertexShaderDelete(
      this.contextGetGL3(),
      this.state,
      this.log,
      id);
  }

  @Override public void viewportSet(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      JCGLException
  {
    JOGL_GL_Functions.viewportSet(this.contextGetGL3(), position, dimensions);
  }
}
