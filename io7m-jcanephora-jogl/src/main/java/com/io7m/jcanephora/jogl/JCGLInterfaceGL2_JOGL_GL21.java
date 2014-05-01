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
import java.util.Map;
import java.util.Set;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;
import javax.media.opengl.TraceGL2;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Option;
import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.ArrayBufferUpdateUnmappedType;
import com.io7m.jcanephora.BlendEquationGL3;
import com.io7m.jcanephora.BlendEquationGLES2;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.CubeMapFaceRH;
import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FaceWindingOrder;
import com.io7m.jcanephora.FragmentShader;
import com.io7m.jcanephora.FramebufferBlitBuffer;
import com.io7m.jcanephora.FramebufferBlitFilter;
import com.io7m.jcanephora.FramebufferColorAttachmentPoint;
import com.io7m.jcanephora.FramebufferDrawBuffer;
import com.io7m.jcanephora.FramebufferReference;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.IndexBuffer;
import com.io7m.jcanephora.IndexBufferWritableData;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLError;
import com.io7m.jcanephora.JCGLExtensionDepthTexture;
import com.io7m.jcanephora.JCGLInterfaceGL2;
import com.io7m.jcanephora.JCGLNamedExtensionsType;
import com.io7m.jcanephora.JCGLRuntimeException;
import com.io7m.jcanephora.JCGLSLVersion;
import com.io7m.jcanephora.JCGLSoftRestrictionsType;
import com.io7m.jcanephora.JCGLStateCache;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.JCGLVersion;
import com.io7m.jcanephora.LogicOperation;
import com.io7m.jcanephora.PolygonMode;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramUniform;
import com.io7m.jcanephora.Renderbuffer;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.Texture2DReadableData;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.Texture2DStaticUsable;
import com.io7m.jcanephora.Texture2DWritableData;
import com.io7m.jcanephora.TextureCubeReadableData;
import com.io7m.jcanephora.TextureCubeStatic;
import com.io7m.jcanephora.TextureCubeStaticUsable;
import com.io7m.jcanephora.TextureCubeWritableData;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShader;
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
 * A class implementing {@link JCGLInterfaceGL2} that uses only non-deprecated
 * features of OpenGL 3.* that are present on OpenGL 2.1 (with extensions),
 * using JOGL as the backend.
 * </p>
 * <p>
 * A {@link javax.media.opengl.GLContext} is used to construct the interface,
 * and therefore the <code>GLInterfaceGL2_JOGL_GL21</code> interface has the
 * same thread safe/unsafe behaviour.
 * </p>
 * <p>
 * The <code>GLInterfaceGL2_JOGL_GL21</code> implementation does not call
 * {@link javax.media.opengl.GLContext#makeCurrent()} or
 * {@link javax.media.opengl.GLContext#release()}, so these calls must be made
 * by the programmer when necessary (typically, programs call
 * {@link javax.media.opengl.GLContext#makeCurrent()}, perform all rendering,
 * and then call {@link javax.media.opengl.GLContext#release()} at the end of
 * the frame). The JOGL library can also optionally manage this via the
 * {@link javax.media.opengl.GLAutoDrawable} interface.
 * </p>
 */

@NotThreadSafe final class JCGLInterfaceGL2_JOGL_GL21 implements
  JCGLInterfaceGL2
{
  private final @Nonnull GL2                               cached_gl2;
  private final @Nonnull Option<JCGLExtensionDepthTexture> ext_depth_texture;
  private final @Nonnull JCGLNamedExtensionsType               extensions;
  private final @Nonnull GLContext                         gl_context;
  private final @Nonnull Log                               log;
  private final JCGLSoftRestrictionsType                       restrictions;
  private final @Nonnull JCGLSLVersion                     sl_version;
  private final @Nonnull JCGLStateCache                    state;
  private final @Nonnull JCGLVersion                       version;

  JCGLInterfaceGL2_JOGL_GL21(
    final @Nonnull GLContext context,
    final @Nonnull Log log1,
    final @Nonnull JCGLDebugging debug,
    final @CheckForNull PrintStream trace_out,
    final @Nonnull JCGLSoftRestrictionsType restrictions1)
    throws ConstraintError,
      JCGLRuntimeException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log1, "log output"), "jogl21");
    this.gl_context = Constraints.constrainNotNull(context, "GL context");
    this.restrictions =
      Constraints.constrainNotNull(restrictions1, "Restrictions");
    Constraints.constrainNotNull(debug, "Debug");

    this.state = new JCGLStateCache();

    {
      final GL2 g2 = this.gl_context.getGL().getGL2();
      switch (debug) {
        case JCGL_DEBUGGING:
          this.cached_gl2 = new DebugGL2(g2);
          break;
        case JCGL_NONE:
          this.cached_gl2 = g2;
          break;
        case JCGL_TRACING:
          Constraints.constrainNotNull(trace_out, "Trace output");
          this.cached_gl2 = new TraceGL2(g2, trace_out);
          break;
        case JCGL_TRACING_AND_DEBUGGING:
          Constraints.constrainNotNull(trace_out, "Trace output");
          this.cached_gl2 = new DebugGL2(new TraceGL2(g2, trace_out));
          break;
        default:
          throw new UnreachableCodeException();
      }
    }
    assert this.cached_gl2 != null;

    final GL2 g = this.contextGetGL2();

    Constraints.constrainArbitrary(
      this.framebufferDrawAnyIsBound() == false,
      "NOT BOUND!");

    this.extensions = new Extensions(context, restrictions1, log1);

    /**
     * Initialize extensions.
     */

    this.ext_depth_texture =
      ExtDepthTexture.create(g, this.state, this.extensions, log1);

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
      JCGLError.check(this);
    }

    this.version = JOGL_GL_Functions.metaGetVersion(g);
    this.sl_version = JOGL_GL_Functions.metaGetSLVersion(log1, g);
  }

  @Override public JOGLArrayBuffer arrayBufferAllocate(
    final long elements,
    final @Nonnull ArrayBufferTypeDescriptor descriptor,
    final @Nonnull UsageHint usage)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return JOGL_GL_Functions.arrayBufferAllocate(
      this.contextGetGL2(),
      this.log,
      this.state,
      elements,
      descriptor,
      usage);
  }

  @Override public void arrayBufferBind(
    final @Nonnull ArrayBufferUsable buffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferBind(this.contextGetGL2(), buffer);
  }

  @Override public void arrayBufferDelete(
    final @Nonnull JOGLArrayBuffer id)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.arrayBufferDelete(
      this.contextGetGL2(),
      this.log,
      this.state,
      id);
  }

  @Override public boolean arrayBufferIsBound(
    final @Nonnull ArrayBufferUsable id)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return JOGL_GL_Functions.arrayBufferIsBound(this.contextGetGL2(), id);
  }

  @Override public void arrayBufferUnbind()
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferUnbind(this.contextGetGL2());
  }

  @Override public void arrayBufferUpdate(
    final @Nonnull ArrayBufferUpdateUnmappedType data)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferUpdate(this.contextGetGL2(), data);
  }

  @Override public void blendingDisable()
    throws JCGLRuntimeException
  {
    JOGL_GL_Functions.blendingDisable(this.contextGetGL2());
  }

  @Override public void blendingEnable(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.blendingEnable(
      this.contextGetGL2(),
      source_factor,
      destination_factor);
  }

  @Override public void blendingEnableSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.blendingEnableSeparate(
      this.contextGetGL2(),
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
      JCGLRuntimeException
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparate(
      this.contextGetGL2(),
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
      JCGLRuntimeException
  {
    JOGL_GL_Functions.blendingEnableSeparateWithEquationSeparateES2(
      this.contextGetGL2(),
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
      JCGLRuntimeException
  {
    JOGL_GL_Functions.blendingEnableWithEquation(
      this.contextGetGL2(),
      source_factor,
      destination_factor,
      equation);
  }

  @Override public void blendingEnableWithEquationES2(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.blendingEnableWithEquationES2(
      this.contextGetGL2(),
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
      JCGLRuntimeException
  {
    JOGL_GL_Functions.blendingEnableWithEquationSeparate(
      this.contextGetGL2(),
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
      JCGLRuntimeException
  {
    JOGL_GL_Functions.blendingEnableWithEquationSeparateES2(
      this.contextGetGL2(),
      source_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public boolean blendingIsEnabled()
    throws ConstraintError,
      JCGLRuntimeException
  {
    return JOGL_GL_Functions.blendingIsEnabled(
      this.contextGetGL2(),
      this.state);
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.colorBufferClear3f(this.contextGetGL2(), r, g, b);
  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.colorBufferClear4f(this.contextGetGL2(), r, g, b, a);
  }

  @Override public void colorBufferClearV3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.colorBufferClearV3f(this.contextGetGL2(), color);
  }

  @Override public void colorBufferClearV4f(
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.colorBufferClearV4f(this.contextGetGL2(), color);
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.colorBufferMask(this.contextGetGL2(), r, g, b, a);
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusAlpha(
      this.contextGetGL2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusBlue(
      this.contextGetGL2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusGreen(
      this.contextGetGL2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusRed(
      this.contextGetGL2(),
      this.state);
  }

  private GL2 contextGetGL2()
  {
    return this.cached_gl2;
  }

  @Override public void cullingDisable()
    throws JCGLRuntimeException
  {
    JOGL_GL_Functions.cullingDisable(this.contextGetGL2());
  }

  @Override public void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.cullingEnable(this.contextGetGL2(), faces, order);
  }

  @Override public boolean cullingIsEnabled()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.cullingIsEnabled(this.contextGetGL2());
  }

  @Override public void depthBufferClear(
    final float depth)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.depthBufferClear(
      this.contextGetGL2(),
      this.state,
      depth);
  }

  @Override public int depthBufferGetBits()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.depthBufferGetBits(
      this.contextGetGL2(),
      this.state);
  }

  @Override public void depthBufferTestDisable()
    throws JCGLRuntimeException
  {
    JOGL_GL_Functions.depthBufferDisable(this.contextGetGL2());
  }

  @Override public void depthBufferTestEnable(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.depthBufferEnable(
      this.contextGetGL2(),
      this.state,
      function);
  }

  @Override public boolean depthBufferTestIsEnabled()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.depthBufferIsEnabled(this.contextGetGL2());
  }

  @Override public void depthBufferWriteDisable()
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.depthBufferWriteDisable(
      this.contextGetGL2(),
      this.state);
  }

  @Override public void depthBufferWriteEnable()
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions
      .depthBufferWriteEnable(this.contextGetGL2(), this.state);
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.depthBufferWriteIsEnabled(
      this.contextGetGL2(),
      this.state);
  }

  @Override public void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBufferUsable indices)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.drawElements(this.contextGetGL2(), mode, indices);
  }

  @Override public boolean errorCodeIsInvalidOperation(
    final int code)
  {
    return code == GL.GL_INVALID_OPERATION;
  }

  @Override public Option<JCGLExtensionDepthTexture> extensionDepthTexture()
  {
    return this.ext_depth_texture;
  }

  @Override public FragmentShader fragmentShaderCompile(
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLRuntimeException
  {
    return JOGL_GL2ES2_Functions.fragmentShaderCompile(
      this.contextGetGL2(),
      this.state,
      this.log,
      name,
      lines);
  }

  @Override public void fragmentShaderDelete(
    final @Nonnull FragmentShader id)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.fragmentShaderDelete(
      this.contextGetGL2(),
      this.state,
      this.log,
      id);
  }

  @Override public @Nonnull FramebufferReference framebufferAllocate()
    throws JCGLRuntimeException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferAllocate(
      this.contextGetGL2(),
      this.state,
      this.log);
  }

  @Override public void framebufferBlit(
    final @Nonnull AreaInclusive source,
    final @Nonnull AreaInclusive target,
    final @Nonnull Set<FramebufferBlitBuffer> buffers,
    final @Nonnull FramebufferBlitFilter filter)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES3_Functions.framebufferBlit(
      this.contextGetGL2(),
      source,
      target,
      buffers,
      filter);
  }

  @Override public void framebufferDelete(
    final @Nonnull FramebufferReference framebuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDelete(
      this.contextGetGL2(),
      this.state,
      this.log,
      framebuffer);
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.framebufferDrawAnyIsBound(this.contextGetGL2());
  }

  @Override public void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorRenderbuffer(
      this.contextGetGL2(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachColorRenderbufferAt(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorRenderbufferAt(
      this.contextGetGL2(),
      this.state,
      this.log,
      framebuffer,
      point,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachColorTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTexture2D(
      this.contextGetGL2(),
      this.state,
      this.log,
      this.version,
      framebuffer,
      texture,
      this.extensions);
  }

  @Override public void framebufferDrawAttachColorTexture2DAt(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTexture2DAt(
      this.contextGetGL2(),
      this.state,
      this.log,
      this.version,
      framebuffer,
      point,
      texture,
      this.extensions);
  }

  @Override public void framebufferDrawAttachColorTextureCube(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFaceLH face)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTextureCube(
      this.contextGetGL2(),
      this.state,
      this.log,
      this.version,
      framebuffer,
      texture,
      face,
      this.extensions);
  }

  @Override public void framebufferDrawAttachColorTextureCubeAt(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFaceLH face)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTextureCubeAt(
      this.contextGetGL2(),
      this.state,
      this.log,
      this.version,
      framebuffer,
      point,
      texture,
      face,
      this.extensions);
  }

  @Override public void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepth> renderbuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachDepthRenderbuffer(
      this.contextGetGL2(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachDepthStencilRenderbuffer(
      this.contextGetGL2(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachDepthTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachDepthTexture2D(
      this.contextGetGL2(),
      this.state,
      this.log,
      framebuffer,
      texture,
      this.extensions);
  }

  @Override public void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableStencil> renderbuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachStencilRenderbuffer(
      this.contextGetGL2(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawBind(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawBind(this.contextGetGL2(), framebuffer);
  }

  @Override public boolean framebufferDrawIsBound(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferDrawIsBound(
      this.contextGetGL2(),
      framebuffer);
  }

  @Override public
    void
    framebufferDrawSetBuffers(
      final @Nonnull FramebufferReference framebuffer,
      final @Nonnull Map<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> mappings)
      throws JCGLRuntimeException,
        ConstraintError
  {
    JOGL_GL2ES3_Functions.framebufferDrawSetBuffers(
      this.contextGetGL2(),
      this.state,
      this.log,
      framebuffer,
      mappings);
  }

  @Override public void framebufferDrawUnbind()
    throws JCGLRuntimeException
  {
    JOGL_GL_Functions.framebufferDrawUnbind(this.contextGetGL2());
  }

  @Override public @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferDrawValidate(
      this.contextGetGL2(),
      framebuffer);
  }

  @Override public @Nonnull
    List<FramebufferColorAttachmentPoint>
    framebufferGetColorAttachmentPoints()
      throws JCGLRuntimeException,
        ConstraintError
  {
    return Collections.unmodifiableList(this.state.color_attachments);
  }

  @Override public @Nonnull
    List<FramebufferDrawBuffer>
    framebufferGetDrawBuffers()
      throws JCGLRuntimeException,
        ConstraintError
  {
    return Collections.unmodifiableList(this.state.draw_buffers);
  }

  @Override public boolean framebufferReadAnyIsBound()
    throws JCGLRuntimeException
  {
    return JOGL_GL2ES3_Functions.framebufferReadAnyIsBound(this
      .contextGetGL2());
  }

  @Override public void framebufferReadBind(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL2ES3_Functions.framebufferReadBind(
      this.contextGetGL2(),
      framebuffer);
  }

  @Override public boolean framebufferReadIsBound(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return JOGL_GL2ES3_Functions.framebufferReadIsBound(
      this.contextGetGL2(),
      framebuffer);
  }

  @Override public void framebufferReadUnbind()
    throws JCGLRuntimeException
  {
    JOGL_GL2ES3_Functions.framebufferReadUnbind(this.contextGetGL2());
  }

  @Override public IndexBuffer indexBufferAllocate(
    final @Nonnull ArrayBufferUsable buffer,
    final int indices)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferAllocate(
      this.contextGetGL2(),
      this.state,
      this.log,
      buffer,
      indices);
  }

  @Override public @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull JCGLUnsignedType type,
    final int indices)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return JOGL_GL_Functions.indexBufferAllocateType(
      this.contextGetGL2(),
      this.state,
      this.log,
      type,
      indices);
  }

  @Override public void indexBufferDelete(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.indexBufferDelete(
      this.contextGetGL2(),
      this.state,
      this.log,
      id);
  }

  @Override public void indexBufferUpdate(
    final @Nonnull IndexBufferWritableData data)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.indexBufferUpdate(this.contextGetGL2(), data);
  }

  @Override public void logicOperationsDisable()
    throws JCGLRuntimeException
  {
    JOGL_GL2GL3_Functions.logicOperationsDisable(this.contextGetGL2());
  }

  @Override public void logicOperationsEnable(
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2GL3_Functions.logicOperationsEnable(
      this.contextGetGL2(),
      operation);
  }

  @Override public boolean logicOperationsEnabled()
    throws JCGLRuntimeException
  {
    return JOGL_GL2GL3_Functions.logicOperationsEnabled(this.contextGetGL2());
  }

  @Override public int metaGetError()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.metaGetError(this.contextGetGL2());
  }

  @Override public String metaGetRenderer()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.metaGetRenderer(this.contextGetGL2());
  }

  @Override public @Nonnull JCGLSLVersion metaGetSLVersion()
    throws JCGLRuntimeException
  {
    return this.sl_version;
  }

  @Override public String metaGetVendor()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.metaGetVendor(this.contextGetGL2());
  }

  @Override public @Nonnull JCGLVersion metaGetVersion()
    throws JCGLRuntimeException
  {
    return this.version;
  }

  @Override public @Nonnull PolygonMode polygonGetMode()
    throws ConstraintError,
      JCGLRuntimeException
  {
    return this.state.polygon_mode;
  }

  @Override public void polygonSetMode(
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2GL3_Functions.polygonSetMode(
      this.contextGetGL2(),
      this.state,
      mode);
  }

  @Override public void polygonSmoothingDisable()
    throws JCGLRuntimeException
  {
    JOGL_GL2GL3_Functions.polygonSmoothingDisable(this.contextGetGL2());
  }

  @Override public void polygonSmoothingEnable()
    throws JCGLRuntimeException
  {
    JOGL_GL2GL3_Functions.polygonSmoothingEnable(this.contextGetGL2());
  }

  @Override public boolean polygonSmoothingIsEnabled()
    throws JCGLRuntimeException
  {
    return JOGL_GL2GL3_Functions.polygonSmoothingIsEnabled(this
      .contextGetGL2());
  }

  @Override public void programActivate(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programActivate(this.contextGetGL2(), program);
  }

  @Override public void programAttributeArrayAssociate(
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull ArrayBufferAttribute array_attribute)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributeArrayBind(
      this.contextGetGL2(),
      this.state,
      program_attribute,
      array_attribute);
  }

  @Override public void programAttributeArrayDisassociate(
    final @Nonnull ProgramAttribute program_attribute)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributeArrayDisassociate(
      this.contextGetGL2(),
      this.state,
      program_attribute);
  }

  @Override public void programAttributePutFloat(
    final @Nonnull ProgramAttribute program_attribute,
    final float x)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutFloat(
      this.contextGetGL2(),
      this.state,
      program_attribute,
      x);
  }

  @Override public void programAttributePutVector2f(
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull VectorReadable2F x)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutVector2f(
      this.contextGetGL2(),
      this.state,
      program_attribute,
      x);
  }

  @Override public void programAttributePutVector3f(
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull VectorReadable3F x)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutVector3f(
      this.contextGetGL2(),
      this.state,
      program_attribute,
      x);
  }

  @Override public void programAttributePutVector4f(
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull VectorReadable4F x)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.programAttributePutVector4f(
      this.contextGetGL2(),
      this.state,
      program_attribute,
      x);
  }

  @Override public ProgramReference programCreateCommon(
    final @Nonnull String name,
    final @Nonnull VertexShader v,
    final @Nonnull FragmentShader f)
    throws ConstraintError,
      JCGLRuntimeException,
      JCGLCompileException
  {
    return JOGL_GL2ES2_Functions.programCreateCommon(
      this.contextGetGL2(),
      this.state,
      this.log,
      name,
      v,
      f);
  }

  @Override public void programDeactivate()
    throws JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programDeactivate(this.contextGetGL2());
  }

  @Override public void programDelete(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programDelete(
      this.contextGetGL2(),
      this.state,
      this.log,
      program);
  }

  @Override public int programGetMaximumActiveAttributes()
    throws JCGLRuntimeException
  {
    return JOGL_GL2ES2_Functions.programGetMaximumActiveAttributes(
      this.contextGetGL2(),
      this.state,
      this.log);
  }

  @Override public boolean programIsActive(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return JOGL_GL2ES2_Functions.programIsActive(
      this.contextGetGL2(),
      this.state,
      program);
  }

  @Override public void programUniformPutFloat(
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programPutUniformFloat(
      this.contextGetGL2(),
      this.state,
      uniform,
      value);
  }

  @Override public void programUniformPutInteger(
    final @Nonnull ProgramUniform uniform,
    final int value)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programPutUniformInteger(
      this.contextGetGL2(),
      this.state,
      uniform,
      value);
  }

  @Override public void programUniformPutMatrix3x3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programPutUniformMatrix3x3f(
      this.contextGetGL2(),
      this.state,
      uniform,
      matrix);
  }

  @Override public void programUniformPutMatrix4x4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable4x4F matrix)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programPutUniformMatrix4x4f(
      this.contextGetGL2(),
      this.state,
      uniform,
      matrix);
  }

  @Override public void programUniformPutTextureUnit(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programPutUniformTextureUnit(
      this.contextGetGL2(),
      this.state,
      uniform,
      unit);
  }

  @Override public void programUniformPutVector2f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector2f(
      this.contextGetGL2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector2i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector2i(
      this.contextGetGL2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector3f(
      this.contextGetGL2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector3i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3I vector)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector3i(
      this.contextGetGL2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector4f(
      this.contextGetGL2(),
      this.state,
      uniform,
      vector);
  }

  @Override public void programUniformPutVector4i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4I vector)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.programPutUniformVector4i(
      this.contextGetGL2(),
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
        JCGLRuntimeException
  {
    return Renderbuffer.unsafeBrandDepth(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL2(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_DEPTH_16,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableDepth>
    renderbufferAllocateDepth24(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLRuntimeException
  {
    return Renderbuffer.unsafeBrandDepth(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL2(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_DEPTH_24,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableDepthStencil>
    renderbufferAllocateDepth24Stencil8(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLRuntimeException
  {
    return Renderbuffer.unsafeBrandDepthStencil(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL2(),
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
        JCGLRuntimeException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL2(),
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
        JCGLRuntimeException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL_Functions
      .renderbufferAllocate(
        this.contextGetGL2(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_COLOR_RGBA_8888,
        width,
        height));
  }

  @Override public void renderbufferDelete(
    final @Nonnull Renderbuffer<?> buffer)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.renderbufferDelete(
      this.contextGetGL2(),
      this.state,
      this.log,
      buffer);
  }

  @Override public void scissorDisable()
    throws JCGLRuntimeException
  {
    JOGL_GL_Functions.scissorDisable(this.contextGetGL2());
  }

  @Override public void scissorEnable(
    final @Nonnull AreaInclusive area)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.scissorEnable(this.contextGetGL2(), area);
  }

  @Override public boolean scissorIsEnabled()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.scissorIsEnabled(this.contextGetGL2());
  }

  @Override public void stencilBufferClear(
    final int stencil)
    throws JCGLRuntimeException,
      ConstraintError
  {
    JOGL_GL_Functions.stencilBufferClear(
      this.contextGetGL2(),
      this.state,
      stencil);
  }

  @Override public void stencilBufferDisable()
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.stencilBufferDisable(this.contextGetGL2());
  }

  @Override public void stencilBufferEnable()
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.stencilBufferEnable(this.contextGetGL2(), this.state);
  }

  @Override public void stencilBufferFunction(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.stencilBufferFunction(
      this.contextGetGL2(),
      faces,
      function,
      reference,
      mask);
  }

  @Override public int stencilBufferGetBits()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.stencilBufferGetBits(
      this.contextGetGL2(),
      this.state);
  }

  @Override public boolean stencilBufferIsEnabled()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.stencilBufferIsEnabled(this.contextGetGL2());
  }

  @Override public void stencilBufferMask(
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions
      .stencilBufferMask(this.contextGetGL2(), faces, mask);
  }

  @Override public void stencilBufferOperation(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.stencilBufferOperation(
      this.contextGetGL2(),
      faces,
      stencil_fail,
      depth_fail,
      pass);
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
      JCGLRuntimeException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.contextGetGL2(),
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

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGB8(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.contextGetGL2(),
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

  @Override public @Nonnull Texture2DStatic texture2DStaticAllocateRGBA8(
    final @Nonnull String name,
    final int width,
    final int height,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticAllocate(
      this.contextGetGL2(),
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

  @Override public void texture2DStaticBind(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions
      .texture2DStaticBind(this.contextGetGL2(), unit, texture);
  }

  @Override public void texture2DStaticDelete(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.texture2DStaticDelete(
      this.contextGetGL2(),
      this.state,
      this.log,
      texture);
  }

  @Override public @Nonnull Texture2DReadableData texture2DStaticGetImage(
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return JOGL_GL2GL3_Functions.texture2DStaticGetImage(
      this.contextGetGL2(),
      texture);
  }

  @Override public boolean texture2DStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return JOGL_GL_Functions.texture2DStaticIsBound(
      this.contextGetGL2(),
      this.state,
      unit,
      texture);
  }

  @Override public void texture2DStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.texture2DStaticUnbind(this.contextGetGL2(), unit);
  }

  @Override public void texture2DStaticUpdate(
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2GL3_Functions.texture2DStaticUpdate(this.contextGetGL2(), data);
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
        JCGLRuntimeException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.contextGetGL2(),
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

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGB8(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.contextGetGL2(),
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

  @Override public @Nonnull TextureCubeStatic textureCubeStaticAllocateRGBA8(
    final @Nonnull String name,
    final int size,
    final @Nonnull TextureWrapR wrap_r,
    final @Nonnull TextureWrapS wrap_s,
    final @Nonnull TextureWrapT wrap_t,
    final @Nonnull TextureFilterMinification min_filter,
    final @Nonnull TextureFilterMagnification mag_filter)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticAllocate(
      this.contextGetGL2(),
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

  @Override public void textureCubeStaticBind(
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticUsable texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.textureCubeStaticBind(
      this.contextGetGL2(),
      unit,
      texture);
  }

  @Override public void textureCubeStaticDelete(
    final @Nonnull TextureCubeStatic texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.textureCubeStaticDelete(
      this.contextGetGL2(),
      this.state,
      this.log,
      texture);
  }

  @Override public @Nonnull
    TextureCubeReadableData
    textureCubeStaticGetImageLH(
      final @Nonnull TextureCubeStaticUsable texture,
      final @Nonnull CubeMapFaceLH face)
      throws ConstraintError,
        JCGLRuntimeException
  {
    return JOGL_GL2GL3_Functions.textureCubeStaticGetImageLH(
      this.contextGetGL2(),
      texture,
      face);
  }

  @Override public @Nonnull
    TextureCubeReadableData
    textureCubeStaticGetImageRH(
      final @Nonnull TextureCubeStaticUsable texture,
      final @Nonnull CubeMapFaceRH face)
      throws ConstraintError,
        JCGLRuntimeException
  {
    Constraints.constrainNotNull(face, "Face");
    return JOGL_GL2GL3_Functions.textureCubeStaticGetImageLH(
      this.contextGetGL2(),
      texture,
      CubeMapFaceLH.fromRH(face));
  }

  @Override public boolean textureCubeStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticUsable texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return JOGL_GL_Functions.textureCubeStaticIsBound(
      this.contextGetGL2(),
      this.state,
      unit,
      texture);
  }

  @Override public void textureCubeStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.textureCubeStaticUnbind(this.contextGetGL2(), unit);
  }

  @Override public void textureCubeStaticUpdateLH(
    final @Nonnull CubeMapFaceLH face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2GL3_Functions.textureCubeStaticUpdate(
      this.contextGetGL2(),
      face,
      data);
  }

  @Override public void textureCubeStaticUpdateRH(
    final @Nonnull CubeMapFaceRH face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      JCGLRuntimeException
  {
    Constraints.constrainNotNull(face, "Face");
    this.textureCubeStaticUpdateLH(CubeMapFaceLH.fromRH(face), data);
  }

  @Override public int textureGetMaximumSize()
    throws JCGLRuntimeException
  {
    return JOGL_GL_Functions.textureGetMaximumSize(
      this.contextGetGL2(),
      this.state);
  }

  @Override public List<TextureUnit> textureGetUnits()
    throws JCGLRuntimeException
  {
    return Collections.unmodifiableList(this.state.texture_units);
  }

  @Override public VertexShader vertexShaderCompile(
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLRuntimeException
  {
    return JOGL_GL2ES2_Functions.vertexShaderCompile(
      this.contextGetGL2(),
      this.state,
      this.log,
      name,
      lines);
  }

  @Override public void vertexShaderDelete(
    final @Nonnull VertexShader id)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL2ES2_Functions.vertexShaderDelete(
      this.contextGetGL2(),
      this.state,
      this.log,
      id);
  }

  @Override public void viewportSet(
    final @Nonnull AreaInclusive area)
    throws ConstraintError,
      JCGLRuntimeException
  {
    JOGL_GL_Functions.viewportSet(this.contextGetGL2(), area);
  }
}
