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

package com.io7m.jcanephora.jogl;

import java.io.PrintStream;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.opengl.DebugGLES2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLES2;
import javax.media.opengl.TraceGLES2;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Option;
import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.ArrayBufferUpdateUnmappedType;
import com.io7m.jcanephora.BlendEquationGLES2;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.CubeMapFaceRH;
import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FaceWindingOrder;
import com.io7m.jcanephora.FramebufferColorAttachmentPoint;
import com.io7m.jcanephora.FramebufferDrawBuffer;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferUpdateUnmapped;
import com.io7m.jcanephora.JCGLExceptionCompileError;
import com.io7m.jcanephora.JCGLError;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLSLVersion;
import com.io7m.jcanephora.JCGLStateCache;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.JCGLVersion;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.RenderbufferFormat;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.Texture2DStaticUpdate;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureCubeWritableData;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.api.JCGLExtensionDepthCubeTexture;
import com.io7m.jcanephora.api.JCGLExtensionESDepthTexture;
import com.io7m.jcanephora.api.JCGLExtensionPackedDepthStencil;
import com.io7m.jcanephora.api.JCGLInterfaceGLES2;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jcanephora.api.JCGLSoftRestrictionsType;
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
 * A class implementing {@link JCGLInterfaceGLES2} that exposes only the
 * features of OpenGL ES2, using an OpenGL ES 2.0 context on JOGL.
 * </p>
 * <p>
 * A {@link javax.media.opengl.GLContext} is used to construct the interface,
 * and therefore the <code>GLInterfaceGLES2_JOGL_ES2</code> interface has the
 * same thread safe/unsafe behaviour.
 * </p>
 * <p>
 * The <code>GLInterfaceGLES2_JOGL_ES2</code> implementation does not call
 * {@link javax.media.opengl.GLContext#makeCurrent()} or
 * {@link javax.media.opengl.GLContext#release()}, so these calls must be made
 * by the programmer when necessary (typically, programs call
 * {@link javax.media.opengl.GLContext#makeCurrent()}, perform all rendering,
 * and then call {@link javax.media.opengl.GLContext#release()} at the end of
 * the frame). The JOGL library can also optionally manage this via the
 * {@link javax.media.opengl.GLAutoDrawable} interface.
 * </p>
 */

@NotThreadSafe final class JCGLInterfaceGLES2_JOGL_ES2 implements
  JCGLInterfaceGLES2
{
  private @Nonnull GLES2                                         cached_gl;
  private final @Nonnull Option<JCGLExtensionDepthCubeTexture>   ext_depth_cube_texture;
  private final @Nonnull Option<JCGLExtensionESDepthTexture>     ext_depth_texture;
  private final @Nonnull Option<JCGLExtensionPackedDepthStencil> ext_packed_depth_stencil;
  private final @Nonnull GLContext                               gl_context;
  private final @Nonnull Log                                     log;
  private final @Nonnull JCGLSLVersion                           sl_version;
  private final @Nonnull JCGLStateCache                          state;
  private final @Nonnull JCGLVersion                             version;
  private final @Nonnull JCGLNamedExtensionsType                     extensions;
  private final @Nonnull JCGLSoftRestrictionsType                    restrictions;

  JCGLInterfaceGLES2_JOGL_ES2(
    final @Nonnull GLContext context,
    final @Nonnull Log log1,
    final @Nonnull JCGLDebugging debug,
    final @CheckForNull PrintStream trace_out,
    final @Nonnull JCGLSoftRestrictionsType restrictions1)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    this.log =
      new Log(Constraints.constrainNotNull(log1, "log output"), "jogl-es2");
    this.gl_context = Constraints.constrainNotNull(context, "GL context");
    this.restrictions =
      Constraints.constrainNotNull(restrictions1, "Restrictions");
    Constraints.constrainNotNull(debug, "Debug");

    this.state = new JCGLStateCache();

    {
      final GLES2 g = this.gl_context.getGL().getGLES2();
      switch (debug) {
        case JCGL_DEBUGGING:
          this.cached_gl = new DebugGLES2(g);
          break;
        case JCGL_NONE:
          this.cached_gl = g;
          break;
        case JCGL_TRACING:
          Constraints.constrainNotNull(trace_out, "Trace output");
          this.cached_gl = new TraceGLES2(g, trace_out);
          break;
        case JCGL_TRACING_AND_DEBUGGING:
          Constraints.constrainNotNull(trace_out, "Trace output");
          this.cached_gl = new DebugGLES2(new TraceGLES2(g, trace_out));
          break;
        default:
          throw new UnreachableCodeException();
      }
    }
    assert this.cached_gl != null;
    final GL2ES2 g = this.contextGetGLES2();

    this.extensions = new Extensions(context, restrictions1, log1);
    this.version = JOGL_GL_Functions.metaGetVersion(g);
    this.sl_version = JOGL_GL_Functions.metaGetSLVersion(log1, g);

    /**
     * Initialize extensions.
     */

    this.ext_packed_depth_stencil =
      ExtPackedDepthStencil.create(
        g,
        this.state,
        this.extensions,
        this.version,
        log1);
    this.ext_depth_texture =
      ExtESDepthTexture.create(g, this.state, this.extensions, log1);
    this.ext_depth_cube_texture =
      ExtDepthCubeTexture.create(g, this.state, this.extensions, log1);

    /**
     * Initialize texture unit cache.
     */

    this.state.texture_units =
      JOGL_GL_Functions.textureGetUnitsActual(
        g,
        this.state,
        this.log,
        restrictions1);

    /**
     * Initialize color attachment point cache.
     */

    this.state.color_attachments =
      JOGL_GL_Functions.framebufferGetAttachmentPointsActual(
        g,
        this.state,
        this.log);

    /**
     * Initialize various constants.
     */

    {
      final IntBuffer cache = this.state.getIntegerCache();
      g.glGetIntegerv(GL.GL_ALIASED_POINT_SIZE_RANGE, cache);
      this.state.point_min_width = cache.get();
      this.state.point_max_width = cache.get();
      JCGLError.check(this);
    }
  }

  @Override public JOGLArrayBuffer arrayBufferAllocate(
    final long elements,
    final @Nonnull ArrayBufferTypeDescriptor descriptor,
    final @Nonnull UsageHint usage)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    return JOGL_GL_Functions.arrayBufferAllocate(
      this.contextGetGLES2(),
      this.log,
      this.state,
      elements,
      descriptor,
      usage);
  }

  @Override public void arrayBufferBind(
    final @Nonnull ArrayBufferUsable buffer)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferBind(this.contextGetGLES2(), buffer);
  }

  @Override public void arrayBufferDelete(
    final @Nonnull JOGLArrayBuffer id)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.arrayBufferDelete(
      this.contextGetGLES2(),
      this.log,
      this.state,
      id);
  }

  @Override public boolean arrayBufferIsBound(
    final @Nonnull ArrayBufferUsable id)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.arrayBufferIsBound(this.contextGetGLES2(), id);
  }

  @Override public void arrayBufferUnbind()
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferUnbind(this.contextGetGLES2());
  }

  @Override public void arrayBufferUpdate(
    final @Nonnull ArrayBufferUpdateUnmappedType data)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferUpdate(this.contextGetGLES2(), data);
  }

  @Override public void blendingDisable()
    throws JCGLExceptionRuntime
  {
    JOGL_GL_Functions.blendingDisable(this.contextGetGLES2());
  }

  @Override public void blendingEnable(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.blendingEnable(
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
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.blendingEnableSeparate(
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
    final @Nonnull BlendEquationGLES2 equation_rgb,
    final @Nonnull BlendEquationGLES2 equation_alpha)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparateES2(
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
    final @Nonnull BlendEquationGLES2 equation)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.blendingEnableWithEquationES2(
      this.contextGetGLES2(),
      source_factor,
      destination_factor,
      equation);
  }

  @Override public void blendingEnableWithEquationSeparateES2(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation_rgb,
    final @Nonnull BlendEquationGLES2 equation_alpha)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.blendingEnableWithEquationSeparateES2(
      this.contextGetGLES2(),
      source_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public boolean blendingIsEnabled()
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.blendingIsEnabled(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.colorBufferClear3f(this.contextGetGLES2(), r, g, b);
  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.colorBufferClear4f(this.contextGetGLES2(), r, g, b, a);
  }

  @Override public void colorBufferClearV3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.colorBufferClearV3f(this.contextGetGLES2(), color);
  }

  @Override public void colorBufferClearV4f(
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.colorBufferClearV4f(this.contextGetGLES2(), color);
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.colorBufferMask(this.contextGetGLES2(), r, g, b, a);
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.colorBufferMaskStatusAlpha(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.colorBufferMaskStatusBlue(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.colorBufferMaskStatusGreen(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.colorBufferMaskStatusRed(
      this.contextGetGLES2(),
      this.state);
  }

  private @Nonnull GL2ES2 contextGetGLES2()
  {
    return this.cached_gl;
  }

  @Override public void cullingDisable()
    throws JCGLExceptionRuntime
  {
    JOGL_GL_Functions.cullingDisable(this.contextGetGLES2());
  }

  @Override public void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.cullingEnable(this.contextGetGLES2(), faces, order);
  }

  @Override public boolean cullingIsEnabled()
    throws JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.cullingIsEnabled(this.contextGetGLES2());
  }

  @Override public void depthBufferClear(
    final float depth)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GLES2_Functions.depthBufferClear(
      this.contextGetGLES2(),
      this.state,
      depth);
  }

  @Override public int depthBufferGetBits()
    throws JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions.depthBufferGetBits(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public void depthBufferTestDisable()
    throws JCGLExceptionRuntime
  {
    JOGL_GLES2_Functions.depthBufferDisable(this.contextGetGLES2());
  }

  @Override public void depthBufferTestEnable(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GLES2_Functions.depthBufferEnable(
      this.contextGetGLES2(),
      this.state,
      function);
  }

  @Override public boolean depthBufferTestIsEnabled()
    throws JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions.depthBufferIsEnabled(this.contextGetGLES2());
  }

  @Override public void depthBufferWriteDisable()
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GLES2_Functions.depthBufferWriteDisable(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public void depthBufferWriteEnable()
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GLES2_Functions.depthBufferWriteEnable(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions.depthBufferWriteIsEnabled(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBufferUsable indices)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.drawElements(this.contextGetGLES2(), mode, indices);
  }

  @Override public boolean errorCodeIsInvalidOperation(
    final int code)
  {
    return code == GL.GL_INVALID_OPERATION;
  }

  @Override public
    Option<JCGLExtensionDepthCubeTexture>
    extensionDepthCubeTexture()
  {
    return this.ext_depth_cube_texture;
  }

  @Override public
    Option<JCGLExtensionESDepthTexture>
    extensionDepthTexture()
  {
    return this.ext_depth_texture;
  }

  @Override public
    Option<JCGLExtensionPackedDepthStencil>
    extensionPackedDepthStencil()
  {
    return this.ext_packed_depth_stencil;
  }

  @Override public FragmentShader fragmentShaderCompile(
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLExceptionCompileError,
      JCGLExceptionRuntime
  {
    return JOGL_GL2ES2_Functions.fragmentShaderCompile(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      lines);
  }

  @Override public void fragmentShaderDelete(
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.fragmentShaderDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      id);
  }

  @Override public @Nonnull FramebufferReference framebufferAllocate()
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log);
  }

  @Override public void framebufferDelete(
    final @Nonnull FramebufferReference framebuffer)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      framebuffer);
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions.framebufferDrawAnyIsBound(this
      .contextGetGLES2());
  }

  @Override public void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GLES2_Functions.framebufferDrawAttachColorRenderbuffer(
      this.contextGetGLES2(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachColorTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsableType texture)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GLES2_Functions.framebufferDrawAttachColorTexture2D(
      this.contextGetGLES2(),
      this.state,
      this.log,
      this.version,
      framebuffer,
      texture,
      this.extensions);
  }

  @Override public void framebufferDrawAttachColorTextureCube(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull TextureCubeStaticUsableType texture,
    final @Nonnull CubeMapFaceLH face)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GLES2_Functions.framebufferDrawAttachColorTextureCube(
      this.contextGetGLES2(),
      this.state,
      this.log,
      this.version,
      framebuffer,
      texture,
      face,
      this.extensions);
  }

  @Override public void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepth> renderbuffer)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GLES2_Functions.framebufferDrawAttachDepthRenderbuffer(
      this.contextGetGLES2(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachDepthTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsableType texture)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GLES2_Functions.framebufferDrawAttachDepthTexture2D(
      this.contextGetGLES2(),
      this.state,
      this.log,
      framebuffer,
      texture,
      this.extensions);
  }

  @Override public void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableStencil> renderbuffer)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GLES2_Functions.framebufferDrawAttachStencilRenderbuffer(
      this.contextGetGLES2(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawBind(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GLES2_Functions.framebufferDrawBind(
      this.contextGetGLES2(),
      framebuffer);
  }

  @Override public boolean framebufferDrawIsBound(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    return JOGL_GLES2_Functions.framebufferDrawIsBound(
      this.contextGetGLES2(),
      framebuffer);
  }

  @Override public void framebufferDrawUnbind()
    throws JCGLExceptionRuntime
  {
    JOGL_GLES2_Functions.framebufferDrawUnbind(this.contextGetGLES2());
  }

  @Override public @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    return JOGL_GLES2_Functions.framebufferDrawValidate(
      this.contextGetGLES2(),
      framebuffer);
  }

  @Override public @Nonnull
    List<FramebufferColorAttachmentPoint>
    framebufferGetColorAttachmentPoints()
      throws JCGLExceptionRuntime,
        ConstraintError
  {
    return Collections.unmodifiableList(this.state.color_attachments);
  }

  @Override public @Nonnull
    List<FramebufferDrawBuffer>
    framebufferGetDrawBuffers()
      throws JCGLExceptionRuntime,
        ConstraintError
  {
    return Collections.unmodifiableList(this.state.draw_buffers);
  }

  @Override public IndexBuffer indexBufferAllocate(
    final @Nonnull ArrayBufferUsable buffer,
    final int indices)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      buffer,
      indices);
  }

  @Override public @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull JCGLUnsignedType type,
    final int indices)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferAllocateType(
      this.contextGetGLES2(),
      this.state,
      this.log,
      type,
      indices);
  }

  @Override public void indexBufferDelete(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.indexBufferDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      id);
  }

  @Override public void indexBufferUpdate(
    final @Nonnull IndexBufferUpdateUnmapped data)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GL_Functions.indexBufferUpdate(this.contextGetGLES2(), data);
  }

  @Override public int metaGetError()
    throws JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.metaGetError(this.contextGetGLES2());
  }

  @Override public String metaGetRenderer()
    throws JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.metaGetRenderer(this.contextGetGLES2());
  }

  @Override public @Nonnull JCGLSLVersion metaGetSLVersion()
    throws JCGLExceptionRuntime
  {
    return this.sl_version;
  }

  @Override public String metaGetVendor()
    throws JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.metaGetVendor(this.contextGetGLES2());
  }

  @Override public @Nonnull JCGLVersion metaGetVersion()
    throws JCGLExceptionRuntime
  {
    return this.version;
  }

  @Override public void programActivate(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programActivate(this.contextGetGLES2(), program);
  }

  @Override public void programAttributeArrayAssociate(
    final @Nonnull ProgramAttributeType program_attribute,
    final @Nonnull ArrayBufferAttribute array_attribute)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributeArrayBind(
      this.contextGetGLES2(),
      this.state,
      program_attribute,
      array_attribute);
  }

  @Override public void programAttributeArrayDisassociate(
    final @Nonnull ProgramAttributeType program_attribute)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributeArrayDisassociate(
      this.contextGetGLES2(),
      this.state,
      program_attribute);
  }

  @Override public void programAttributePutFloat(
    final @Nonnull ProgramAttributeType program_attribute,
    final float x)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutFloat(
      this.contextGetGLES2(),
      this.state,
      program_attribute,
      x);
  }

  @Override public void programAttributePutVector2f(
    final @Nonnull ProgramAttributeType program_attribute,
    final @Nonnull VectorReadable2F x)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutVector2f(
      this.contextGetGLES2(),
      this.state,
      program_attribute,
      x);
  }

  @Override public void programAttributePutVector3f(
    final @Nonnull ProgramAttributeType program_attribute,
    final @Nonnull VectorReadable3F x)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutVector3f(
      this.contextGetGLES2(),
      this.state,
      program_attribute,
      x);
  }

  @Override public void programAttributePutVector4f(
    final @Nonnull ProgramAttributeType program_attribute,
    final @Nonnull VectorReadable4F x)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutVector4f(
      this.contextGetGLES2(),
      this.state,
      program_attribute,
      x);
  }

  @Override public JOGLProgram programCreateCommon(
    final @Nonnull String name,
    final @Nonnull VertexShader v,
    final @Nonnull FragmentShader f)
    throws ConstraintError,
      JCGLExceptionRuntime,
      JCGLExceptionCompileError
  {
    return JOGL_GL2ES2_Functions.programCreateCommon(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      v,
      f);
  }

  @Override public void programDeactivate()
    throws JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programDeactivate(this.contextGetGLES2());
  }

  @Override public void programDelete(
    final @Nonnull JOGLProgram program)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      program);
  }

  @Override public int programGetMaximumActiveAttributes()
    throws JCGLExceptionRuntime
  {
    return JOGL_GL2ES2_Functions.programGetMaximumActiveAttributes(
      this.contextGetGLES2(),
      this.state,
      this.log);
  }

  @Override public boolean programIsActive(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    return JOGL_GL2ES2_Functions.programIsActive(
      this.contextGetGLES2(),
      this.state,
      program);
  }

  @Override public void programUniformPutFloat(
    final @Nonnull ProgramUniformType uniform,
    final float value)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programPutUniformFloat(
      this.contextGetGLES2(),
      this.state,
      uniform,
      value);
  }

  @Override public void programUniformPutInteger(
    final @Nonnull ProgramUniformType uniform,
    final int value)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programPutUniformInteger(
      this.contextGetGLES2(),
      this.state,
      uniform,
      value);
  }

  @Override public void programUniformPutMatrix3x3f(
    final @Nonnull ProgramUniformType uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programPutUniformMatrix3x3f(
      this.contextGetGLES2(),
      this.state,
      uniform,
      matrix);
  }

  @Override public void programUniformPutMatrix4x4f(
    final @Nonnull ProgramUniformType uniform,
    final @Nonnull MatrixReadable4x4F matrix)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programPutUniformMatrix4x4f(
      this.contextGetGLES2(),
      this.state,
      uniform,
      matrix);
  }

  @Override public void programUniformPutTextureUnit(
    final @Nonnull ProgramUniformType uniform,
    final @Nonnull TextureUnitType unit)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programPutUniformTextureUnit(
      this.contextGetGLES2(),
      this.state,
      uniform,
      unit);
  }

  @Override public void programUniformPutVector2f(
    final @Nonnull ProgramUniformType uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programPutUniformVector2f(
      this.contextGetGLES2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector2i(
    final @Nonnull ProgramUniformType uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programPutUniformVector2i(
      this.contextGetGLES2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector3f(
    final @Nonnull ProgramUniformType uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programPutUniformVector3f(
      this.contextGetGLES2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector3i(
    final @Nonnull ProgramUniformType uniform,
    final @Nonnull VectorReadable3I vector)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programPutUniformVector3i(
      this.contextGetGLES2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector4f(
    final @Nonnull ProgramUniformType uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programPutUniformVector4f(
      this.contextGetGLES2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector4i(
    final @Nonnull ProgramUniformType uniform,
    final @Nonnull VectorReadable4I vector)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.programPutUniformVector4i(
      this.contextGetGLES2(),
      this.state,
      uniform,
      vector);
  }

  @Override public @Nonnull
    JOGLRenderbuffer<RenderableDepth>
    renderbufferAllocateDepth16(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLExceptionRuntime
  {
    return JOGLRenderbuffer.unsafeBrandDepth(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGLES2(),
        this.state,
        this.log,
        RenderbufferFormat.RENDERBUFFER_DEPTH_16,
        width,
        height));
  }

  @Override public @Nonnull
    JOGLRenderbuffer<RenderableColor>
    renderbufferAllocateRGB565(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLExceptionRuntime
  {
    return JOGLRenderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGLES2(),
        this.state,
        this.log,
        RenderbufferFormat.RENDERBUFFER_COLOR_RGB_565,
        width,
        height));
  }

  @Override public @Nonnull
    JOGLRenderbuffer<RenderableColor>
    renderbufferAllocateRGBA4444(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLExceptionRuntime
  {
    return JOGLRenderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGLES2(),
        this.state,
        this.log,
        RenderbufferFormat.RENDERBUFFER_COLOR_RGBA_4444,
        width,
        height));
  }

  @Override public @Nonnull
    JOGLRenderbuffer<RenderableColor>
    renderbufferAllocateRGBA5551(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLExceptionRuntime
  {
    return JOGLRenderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGLES2(),
        this.state,
        this.log,
        RenderbufferFormat.RENDERBUFFER_COLOR_RGBA_5551,
        width,
        height));
  }

  @Override public @Nonnull
    JOGLRenderbuffer<RenderableStencil>
    renderbufferAllocateStencil8(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLExceptionRuntime
  {
    return JOGLRenderbuffer.unsafeBrandStencil(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGLES2(),
        this.state,
        this.log,
        RenderbufferFormat.RENDERBUFFER_STENCIL_8,
        width,
        height));
  }

  @Override public void renderbufferDelete(
    final @Nonnull JOGLRenderbuffer<?> buffer)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.renderbufferDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      buffer);
  }

  @Override public void scissorDisable()
    throws JCGLExceptionRuntime
  {
    JOGL_GL_Functions.scissorDisable(this.contextGetGLES2());
  }

  @Override public void scissorEnable(
    final @Nonnull AreaInclusive area)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.scissorEnable(this.contextGetGLES2(), area);
  }

  @Override public boolean scissorIsEnabled()
    throws JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.scissorIsEnabled(this.contextGetGLES2());
  }

  @Override public void stencilBufferClear(
    final int stencil)
    throws JCGLExceptionRuntime,
      ConstraintError
  {
    JOGL_GLES2_Functions.stencilBufferClear(
      this.contextGetGLES2(),
      this.state,
      stencil);
  }

  @Override public void stencilBufferDisable()
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GLES2_Functions.stencilBufferDisable(this.contextGetGLES2());
  }

  @Override public void stencilBufferEnable()
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GLES2_Functions.stencilBufferEnable(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public void stencilBufferFunction(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.stencilBufferFunction(
      this.contextGetGLES2(),
      faces,
      function,
      reference,
      mask);
  }

  @Override public int stencilBufferGetBits()
    throws JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions.stencilBufferGetBits(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean stencilBufferIsEnabled()
    throws JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions
      .stencilBufferIsEnabled(this.contextGetGLES2());
  }

  @Override public void stencilBufferMask(
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.stencilBufferMask(
      this.contextGetGLES2(),
      faces,
      mask);
  }

  @Override public void stencilBufferOperation(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.stencilBufferOperation(
      this.contextGetGLES2(),
      faces,
      stencil_fail,
      depth_fail,
      pass);
  }

  @Override public @Nonnull JOGLTexture2DStatic texture2DStaticAllocateRGB565(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions.texture2DStaticAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull JOGLTexture2DStatic texture2DStaticAllocateRGBA4444(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions.texture2DStaticAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureFormat.TEXTURE_FORMAT_RGBA_4444_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull JOGLTexture2DStatic texture2DStaticAllocateRGBA5551(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions.texture2DStaticAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      width,
      height,
      TextureFormat.TEXTURE_FORMAT_RGBA_5551_2BPP,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public void texture2DStaticBind(
    final @Nonnull TextureUnitType unit,
    final @Nonnull Texture2DStaticUsableType texture)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.texture2DStaticBind(
      this.contextGetGLES2(),
      unit,
      texture);
  }

  @Override public void texture2DStaticDelete(
    final @Nonnull JOGLTexture2DStatic texture)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.texture2DStaticDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      texture);
  }

  @Override public boolean texture2DStaticIsBound(
    final @Nonnull TextureUnitType unit,
    final @Nonnull Texture2DStaticUsableType texture)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.texture2DStaticIsBound(
      this.contextGetGLES2(),
      this.state,
      unit,
      texture);
  }

  @Override public void texture2DStaticUnbind(
    final @Nonnull TextureUnitType unit)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.texture2DStaticUnbind(this.contextGetGLES2(), unit);
  }

  @Override public void texture2DStaticUpdate(
    final @Nonnull Texture2DStaticUpdate data)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GLES2_Functions.texture2DStaticUpdate(this.contextGetGLES2(), data);
  }

  @Override public @Nonnull
    JOGLTextureCubeStatic
    textureCubeStaticAllocateRGB565(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions.textureCubeStaticAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      size,
      TextureFormat.TEXTURE_FORMAT_RGB_565_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    JOGLTextureCubeStatic
    textureCubeStaticAllocateRGBA4444(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions.textureCubeStaticAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      size,
      TextureFormat.TEXTURE_FORMAT_RGBA_4444_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public @Nonnull
    JOGLTextureCubeStatic
    textureCubeStaticAllocateRGBA5551(
      final @Nonnull String name,
      final int size,
      final @Nonnull TextureWrapR wrap_r,
      final @Nonnull TextureWrapS wrap_s,
      final @Nonnull TextureWrapT wrap_t,
      final @Nonnull TextureFilterMinification min_filter,
      final @Nonnull TextureFilterMagnification mag_filter)
      throws ConstraintError,
        JCGLExceptionRuntime
  {
    return JOGL_GLES2_Functions.textureCubeStaticAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      size,
      TextureFormat.TEXTURE_FORMAT_RGBA_5551_2BPP,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public void textureCubeStaticBind(
    final @Nonnull TextureUnitType unit,
    final @Nonnull TextureCubeStaticUsableType texture)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.textureCubeStaticBind(
      this.contextGetGLES2(),
      unit,
      texture);
  }

  @Override public void textureCubeStaticDelete(
    final @Nonnull JOGLTextureCubeStatic texture)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.textureCubeStaticDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      texture);
  }

  @Override public boolean textureCubeStaticIsBound(
    final @Nonnull TextureUnitType unit,
    final @Nonnull TextureCubeStaticUsableType texture)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.textureCubeStaticIsBound(
      this.contextGetGLES2(),
      this.state,
      unit,
      texture);
  }

  @Override public void textureCubeStaticUnbind(
    final @Nonnull TextureUnitType unit)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.textureCubeStaticUnbind(this.contextGetGLES2(), unit);
  }

  @Override public void textureCubeStaticUpdateLH(
    final @Nonnull CubeMapFaceLH face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GLES2_Functions.textureCubeStaticUpdate(
      this.contextGetGLES2(),
      face,
      data);
  }

  @Override public void textureCubeStaticUpdateRH(
    final @Nonnull CubeMapFaceRH face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    Constraints.constrainNotNull(face, "Face");
    this.textureCubeStaticUpdateLH(CubeMapFaceLH.fromRH(face), data);
  }

  @Override public int textureGetMaximumSize()
    throws JCGLExceptionRuntime
  {
    return JOGL_GL_Functions.textureGetMaximumSize(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public List<TextureUnit> textureGetUnits()
    throws JCGLExceptionRuntime
  {
    return Collections.unmodifiableList(this.state.texture_units);
  }

  @Override public VertexShader vertexShaderCompile(
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLExceptionCompileError,
      JCGLExceptionRuntime
  {
    return JOGL_GL2ES2_Functions.vertexShaderCompile(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name,
      lines);
  }

  @Override public void vertexShaderDelete(
    final @Nonnull VertexShader id)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL2ES2_Functions.vertexShaderDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      id);
  }

  @Override public void viewportSet(
    final @Nonnull AreaInclusive area)
    throws ConstraintError,
      JCGLExceptionRuntime
  {
    JOGL_GL_Functions.viewportSet(this.contextGetGLES2(), area);
  }
}
