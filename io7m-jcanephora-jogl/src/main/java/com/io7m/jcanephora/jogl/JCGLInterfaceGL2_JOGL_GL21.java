/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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
import java.util.List;
import java.util.Set;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import javax.media.opengl.TraceGL2;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateUnmappedType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.BlendEquationGL3;
import com.io7m.jcanephora.BlendEquationGLES2;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.ClearSpecification;
import com.io7m.jcanephora.CubeMapFaceLH;
import com.io7m.jcanephora.CubeMapFaceRH;
import com.io7m.jcanephora.DepthFunction;
import com.io7m.jcanephora.FaceSelection;
import com.io7m.jcanephora.FaceWindingOrder;
import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.FramebufferBlitBuffer;
import com.io7m.jcanephora.FramebufferBlitFilter;
import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.FramebufferStatus;
import com.io7m.jcanephora.FramebufferType;
import com.io7m.jcanephora.FramebufferUsableType;
import com.io7m.jcanephora.IndexBufferType;
import com.io7m.jcanephora.IndexBufferUpdateUnmappedType;
import com.io7m.jcanephora.IndexBufferUsableType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionBlendingMisconfigured;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.JCGLExceptionNoStencilBuffer;
import com.io7m.jcanephora.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.JCGLSLVersion;
import com.io7m.jcanephora.JCGLUnsignedType;
import com.io7m.jcanephora.JCGLVersion;
import com.io7m.jcanephora.LogicOperation;
import com.io7m.jcanephora.PolygonMode;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.ProgramUsableType;
import com.io7m.jcanephora.RenderableColorKind;
import com.io7m.jcanephora.RenderableDepthKind;
import com.io7m.jcanephora.RenderableDepthStencilKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.StencilFunction;
import com.io7m.jcanephora.StencilOperation;
import com.io7m.jcanephora.Texture2DStaticReadableType;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.Texture2DStaticUpdateType;
import com.io7m.jcanephora.Texture2DStaticUsableType;
import com.io7m.jcanephora.TextureCubeStaticReadableType;
import com.io7m.jcanephora.TextureCubeStaticType;
import com.io7m.jcanephora.TextureCubeStaticUpdateType;
import com.io7m.jcanephora.TextureCubeStaticUsableType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.TextureWrapR;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.api.JCGLExtensionDepthTextureType;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderGL3ES3Type;
import com.io7m.jcanephora.api.JCGLFramebufferBuilderType;
import com.io7m.jcanephora.api.JCGLInterfaceGL2Type;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jcanephora.api.JCGLShadersParametersType;
import com.io7m.jcanephora.api.JCGLSoftRestrictionsType;
import com.io7m.jfunctional.OptionType;
import com.io7m.jfunctional.Some;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.MatrixDirectReadable3x3FType;
import com.io7m.jtensors.MatrixDirectReadable4x4FType;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;

/**
 * <p>
 * A class implementing {@link JCGLInterfaceGL2Type} that uses only
 * non-deprecated features of OpenGL 3.* that are present on OpenGL 2.1 (with
 * extensions), using JOGL as the backend.
 * </p>
 * <p>
 * A {@link javax.media.opengl.GLContext} is used to construct the interface,
 * and therefore the {@link JCGLInterfaceGL2_JOGL_GL21} interface has the same
 * thread safe/unsafe behaviour.
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

final class JCGLInterfaceGL2_JOGL_GL21 implements JCGLInterfaceGL2Type
{
  private static GL2 makeCachedGL2(
    final boolean in_debugging,
    final OptionType<PrintStream> in_tracing,
    final GL2 g)
  {
    if (in_debugging) {
      if (in_tracing.isSome()) {
        final Some<PrintStream> s = (Some<PrintStream>) in_tracing;
        return new DebugGL2(new TraceGL2(g, s.get()));
      }
      return new DebugGL2(g);
    }

    if (in_tracing.isSome()) {
      final Some<PrintStream> s = (Some<PrintStream>) in_tracing;
      return new TraceGL2(g, s.get());
    }

    return g;
  }

  private final JOGLArrays                                arrays;
  private final JOGLBlending                              blending;
  private final GL2                                       cached_gl2;
  private final JOGLClear                                 clear;
  private final JOGLColorBuffer                           color_buffer;
  private final JOGLColorAttachmentPoints                 color_points;
  private final JOGLCulling                               cull;
  private final JOGLDepthBuffer                           depth;
  private final JOGLDraw                                  draw;
  private final JOGLDrawBuffers                           draw_buffers;
  private final JOGLErrors                                errors;
  private final OptionType<JCGLExtensionDepthTextureType> ext_depth_texture;
  private final JCGLNamedExtensionsType                   extensions;
  private final JOGLFramebuffersGL2GL3                    framebuffers;
  private final JOGLIntegerCacheType                      icache;
  private final JOGLIndexBuffers                          index;
  private final LogUsableType                             log;
  private final JOGLLogic                                 logic;
  private final JOGLMeta                                  meta;
  private final JOGLPolygonMode                           polygon_modes;
  private final JOGLPolygonSmoothing                      polygon_smooth;
  private final JOGLShadersGL2GL3                         program;
  private final JOGLRenderbuffersGL2                      renderbuffers;
  private final JOGLScissor                               scissor;
  private final JOGLStencil                               stencil;
  private final JOGLLogMessageCacheType                   tcache;
  private final JOGLTextureUnits                          texture_units;
  private final JOGLTexturesCubeStaticGL3                 textures_cube;
  private final JOGLTextures2DStaticGL2                   textures2d;
  private final JOGLViewport                              viewport;

  JCGLInterfaceGL2_JOGL_GL21(
    final GLContext in_context,
    final LogUsableType in_log,
    final boolean in_debugging,
    final OptionType<PrintStream> in_tracing,
    final boolean in_caching,
    final JCGLSoftRestrictionsType in_restrictions)
    throws JCGLException
  {
    this.log = NullCheck.notNull(in_log, "Log").with("gl2-jogl_21");

    NullCheck.notNull(in_context, "Context");
    NullCheck.notNull(in_restrictions, "Soft restrictions");
    NullCheck.notNull(in_tracing, "Tracing");

    this.cached_gl2 =
      JCGLInterfaceGL2_JOGL_GL21.makeCachedGL2(
        in_debugging,
        in_tracing,
        in_context.getGL().getGL2());
    this.extensions = new Extensions(in_context, in_restrictions, in_log);

    /**
     * Initialize subsystems.
     */

    this.icache = JOGLIntegerCache.newCache();
    this.tcache = JOGLLogMessageCache.newCache();

    this.arrays =
      new JOGLArrays(
        this.cached_gl2,
        this.log,
        in_caching,
        this.icache,
        this.tcache);
    this.blending = new JOGLBlending(this.cached_gl2, this.log, this.icache);
    this.color_buffer = new JOGLColorBuffer(this.cached_gl2, this.log);
    this.color_points =
      new JOGLColorAttachmentPoints(
        this.cached_gl2,
        this.log,
        this.icache,
        this.tcache);
    this.cull = new JOGLCulling(this.cached_gl2, this.icache, in_caching);
    this.draw = new JOGLDraw(this.cached_gl2, this.arrays, this.log);
    this.draw_buffers =
      new JOGLDrawBuffers(this.cached_gl2, this.log, this.icache, this.tcache);
    this.errors = new JOGLErrors(this.cached_gl2, this.log);
    this.meta = new JOGLMeta(this.cached_gl2, this.tcache, this.log);
    this.framebuffers =
      new JOGLFramebuffersGL2GL3(
        this.cached_gl2,
        this.log,
        this.icache,
        this.tcache,
        this.draw_buffers,
        this.color_points,
        this.meta,
        this.extensions);
    this.depth = new JOGLDepthBuffer(this.cached_gl2, this.framebuffers);
    this.index =
      new JOGLIndexBuffers(
        this.cached_gl2,
        this.log,
        this.icache,
        this.tcache);
    this.logic = new JOGLLogic(this.cached_gl2, this.log);
    this.polygon_modes = new JOGLPolygonMode(this.cached_gl2, this.log);
    this.polygon_smooth = new JOGLPolygonSmoothing(this.cached_gl2, this.log);
    this.program =
      new JOGLShadersGL2GL3(
        this.cached_gl2,
        this.log,
        this.icache,
        this.tcache,
        this.arrays,
        this.draw_buffers);
    this.renderbuffers =
      new JOGLRenderbuffersGL2(
        this.cached_gl2,
        this.log,
        this.icache,
        this.tcache);
    this.scissor = new JOGLScissor(this.cached_gl2, this.log);
    this.stencil = new JOGLStencil(this.cached_gl2, this.framebuffers);
    this.textures2d =
      new JOGLTextures2DStaticGL2(
        this.cached_gl2,
        this.log,
        this.icache,
        this.tcache);
    this.textures_cube =
      new JOGLTexturesCubeStaticGL3(
        this.cached_gl2,
        this.log,
        this.icache,
        this.tcache);
    this.texture_units =
      new JOGLTextureUnits(
        this.cached_gl2,
        this.log,
        this.icache,
        this.tcache,
        in_restrictions);
    this.viewport = new JOGLViewport(this.cached_gl2, this.log);
    this.clear = new JOGLClear(this.cached_gl2, this.depth, this.stencil);

    /**
     * Initialize extensions.
     */

    this.ext_depth_texture =
      ExtDepthTexture.create(
        this.cached_gl2,
        this.log,
        this.extensions,
        this.icache,
        this.tcache);
  }

  @Override public ArrayBufferType arrayBufferAllocate(
    final long elements,
    final ArrayDescriptor descriptor,
    final UsageHint usage)
    throws JCGLException
  {
    return this.arrays.arrayBufferAllocate(elements, descriptor, usage);
  }

  @Override public boolean arrayBufferAnyIsBound()
    throws JCGLException
  {
    return this.arrays.arrayBufferAnyIsBound();
  }

  @Override public void arrayBufferBind(
    final ArrayBufferUsableType buffer)
    throws JCGLException
  {
    this.arrays.arrayBufferBind(buffer);
  }

  @Override public void arrayBufferDelete(
    final ArrayBufferType id)
    throws JCGLException,
      JCGLExceptionDeleted
  {
    this.arrays.arrayBufferDelete(id);
  }

  @Override public boolean arrayBufferIsBound(
    final ArrayBufferUsableType id)
    throws JCGLException
  {
    return this.arrays.arrayBufferIsBound(id);
  }

  @Override public void arrayBufferUnbind()
    throws JCGLException
  {
    this.arrays.arrayBufferUnbind();
  }

  @Override public void arrayBufferUpdate(
    final ArrayBufferUpdateUnmappedType data)
    throws JCGLException
  {
    this.arrays.arrayBufferUpdate(data);
  }

  @Override public void blendingDisable()
    throws JCGLExceptionRuntime
  {
    this.blending.blendingDisable();
  }

  @Override public void blendingEnable(
    final BlendFunction source_factor,
    final BlendFunction destination_factor)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blending.blendingEnable(source_factor, destination_factor);
  }

  @Override public void blendingEnableSeparate(
    final BlendFunction source_rgb_factor,
    final BlendFunction source_alpha_factor,
    final BlendFunction destination_rgb_factor,
    final BlendFunction destination_alpha_factor)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blending.blendingEnableSeparate(
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor);
  }

  @Override public void blendingEnableSeparateWithEquationSeparate(
    final BlendFunction source_rgb_factor,
    final BlendFunction source_alpha_factor,
    final BlendFunction destination_rgb_factor,
    final BlendFunction destination_alpha_factor,
    final BlendEquationGL3 equation_rgb,
    final BlendEquationGL3 equation_alpha)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blending.blendingEnableSeparateWithEquationSeparate(
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public void blendingEnableSeparateWithEquationSeparateES2(
    final BlendFunction source_rgb_factor,
    final BlendFunction source_alpha_factor,
    final BlendFunction destination_rgb_factor,
    final BlendFunction destination_alpha_factor,
    final BlendEquationGLES2 equation_rgb,
    final BlendEquationGLES2 equation_alpha)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blending.blendingEnableSeparateWithEquationSeparateES2(
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public void blendingEnableWithEquation(
    final BlendFunction source_factor,
    final BlendFunction destination_factor,
    final BlendEquationGL3 equation)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blending.blendingEnableWithEquation(
      source_factor,
      destination_factor,
      equation);
  }

  @Override public void blendingEnableWithEquationES2(
    final BlendFunction source_factor,
    final BlendFunction destination_factor,
    final BlendEquationGLES2 equation)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blending.blendingEnableWithEquationES2(
      source_factor,
      destination_factor,
      equation);
  }

  @Override public void blendingEnableWithEquationSeparate(
    final BlendFunction source_factor,
    final BlendFunction destination_factor,
    final BlendEquationGL3 equation_rgb,
    final BlendEquationGL3 equation_alpha)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blending.blendingEnableWithEquationSeparate(
      source_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public void blendingEnableWithEquationSeparateES2(
    final BlendFunction source_factor,
    final BlendFunction destination_factor,
    final BlendEquationGLES2 equation_rgb,
    final BlendEquationGLES2 equation_alpha)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blending.blendingEnableWithEquationSeparateES2(
      source_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public boolean blendingIsEnabled()
    throws JCGLExceptionRuntime
  {
    return this.blending.blendingIsEnabled();
  }

  @Override public void clear(
    final ClearSpecification c)
    throws JCGLException
  {
    this.clear.clear(c);
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws JCGLExceptionRuntime
  {
    this.color_buffer.colorBufferClear3f(r, g, b);
  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws JCGLExceptionRuntime
  {
    this.color_buffer.colorBufferClear4f(r, g, b, a);
  }

  @Override public void colorBufferClearV3f(
    final VectorReadable3FType color)
    throws JCGLExceptionRuntime
  {
    this.color_buffer.colorBufferClearV3f(color);
  }

  @Override public void colorBufferClearV4f(
    final VectorReadable4FType color)
    throws JCGLExceptionRuntime
  {
    this.color_buffer.colorBufferClearV4f(color);
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws JCGLExceptionRuntime
  {
    this.color_buffer.colorBufferMask(r, g, b, a);
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws JCGLExceptionRuntime
  {
    return this.colorBufferMaskStatusAlpha();
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws JCGLExceptionRuntime
  {
    return this.colorBufferMaskStatusBlue();
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws JCGLExceptionRuntime
  {
    return this.colorBufferMaskStatusGreen();
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws JCGLExceptionRuntime
  {
    return this.colorBufferMaskStatusRed();
  }

  @Override public void cullingDisable()
    throws JCGLExceptionRuntime
  {
    this.cull.cullingDisable();
  }

  @Override public void cullingEnable(
    final FaceSelection faces,
    final FaceWindingOrder order)
    throws JCGLExceptionRuntime
  {
    this.cull.cullingEnable(faces, order);
  }

  @Override public boolean cullingIsEnabled()
    throws JCGLExceptionRuntime
  {
    return this.cull.cullingIsEnabled();
  }

  @Override public void depthBufferClear(
    final float in_depth)
    throws JCGLException,
      JCGLExceptionNoDepthBuffer
  {
    this.depth.depthBufferClear(in_depth);
  }

  @Override public int depthBufferGetBits()
    throws JCGLException
  {
    return this.depth.depthBufferGetBits();
  }

  @Override public void depthBufferTestDisable()
    throws JCGLException,
      JCGLExceptionNoDepthBuffer
  {
    this.depth.depthBufferTestDisable();
  }

  @Override public void depthBufferTestEnable(
    final DepthFunction function)
    throws JCGLException,
      JCGLExceptionNoDepthBuffer
  {
    this.depth.depthBufferTestEnable(function);
  }

  @Override public boolean depthBufferTestIsEnabled()
    throws JCGLException
  {
    return this.depth.depthBufferTestIsEnabled();
  }

  @Override public void depthBufferWriteDisable()
    throws JCGLException
  {
    this.depth.depthBufferWriteDisable();
  }

  @Override public void depthBufferWriteEnable()
    throws JCGLException
  {
    this.depth.depthBufferWriteEnable();
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws JCGLException
  {
    return this.depth.depthBufferWriteIsEnabled();
  }

  @Override public void drawElements(
    final Primitives mode,
    final IndexBufferUsableType indices)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    this.draw.drawElements(mode, indices);
  }

  @Override public boolean errorCodeIsInvalidOperation(
    final int code)
  {
    return this.errors.errorCodeIsInvalidOperation(code);
  }

  @Override public
    OptionType<JCGLExtensionDepthTextureType>
    extensionDepthTexture()
  {
    return this.ext_depth_texture;
  }

  @Override public FragmentShaderType fragmentShaderCompile(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError,
      JCGLException
  {
    return this.program.fragmentShaderCompile(name, lines);
  }

  @Override public void fragmentShaderDelete(
    final FragmentShaderType id)
    throws JCGLException
  {
    this.program.fragmentShaderDelete(id);
  }

  @Override public FramebufferType framebufferAllocate(
    final JCGLFramebufferBuilderType b)
    throws JCGLException
  {
    return this.framebuffers.framebufferAllocate(b);
  }

  @Override public void framebufferBlit(
    final AreaInclusive source,
    final AreaInclusive target,
    final Set<FramebufferBlitBuffer> buffers,
    final FramebufferBlitFilter filter)
    throws JCGLException
  {
    this.framebuffers.framebufferBlit(source, target, buffers, filter);
  }

  @Override public void framebufferDelete(
    final FramebufferType framebuffer)
    throws JCGLException
  {
    this.framebuffers.framebufferDelete(framebuffer);
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws JCGLException
  {
    return this.framebuffers.framebufferDrawAnyIsBound();
  }

  @Override public void framebufferDrawBind(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawBind(framebuffer);
  }

  @Override public
    OptionType<FramebufferUsableType>
    framebufferDrawGetBound()
      throws JCGLException
  {
    return this.framebuffers.framebufferDrawGetBound();
  }

  @Override public boolean framebufferDrawIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    return this.framebuffers.framebufferDrawIsBound(framebuffer);
  }

  @Override public void framebufferDrawUnbind()
    throws JCGLException
  {
    this.framebuffers.framebufferDrawUnbind();
  }

  @Override public FramebufferStatus framebufferDrawValidate(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    return this.framebuffers.framebufferDrawValidate(framebuffer);
  }

  @Override public
    List<FramebufferColorAttachmentPointType>
    framebufferGetColorAttachmentPoints()
      throws JCGLException
  {
    return this.framebuffers.framebufferGetColorAttachmentPoints();
  }

  @Override public
    List<FramebufferDrawBufferType>
    framebufferGetDrawBuffers()
      throws JCGLException
  {
    return this.framebuffers.framebufferGetDrawBuffers();
  }

  @Override public JCGLFramebufferBuilderType framebufferNewBuilder()
  {
    return this.framebuffers.framebufferNewBuilder();
  }

  @Override public
    JCGLFramebufferBuilderGL3ES3Type
    framebufferNewBuilderGL3ES3()
  {
    return this.framebuffers.framebufferNewBuilderGL3ES3();
  }

  @Override public boolean framebufferReadAnyIsBound()
    throws JCGLExceptionRuntime
  {
    return this.framebuffers.framebufferReadAnyIsBound();
  }

  @Override public void framebufferReadBind(
    final FramebufferUsableType framebuffer)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    this.framebuffers.framebufferReadBind(framebuffer);
  }

  @Override public
    OptionType<FramebufferUsableType>
    framebufferReadGetBound()
      throws JCGLException
  {
    return this.framebuffers.framebufferReadGetBound();
  }

  @Override public boolean framebufferReadIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    return this.framebuffers.framebufferReadIsBound(framebuffer);
  }

  @Override public void framebufferReadUnbind()
    throws JCGLExceptionRuntime
  {
    this.framebuffers.framebufferReadUnbind();
  }

  @Override public IndexBufferType indexBufferAllocate(
    final ArrayBufferUsableType buffer,
    final long indices,
    final UsageHint usage)
    throws JCGLException
  {
    return this.index.indexBufferAllocate(buffer, indices, usage);
  }

  @Override public IndexBufferType indexBufferAllocateType(
    final JCGLUnsignedType type,
    final long indices,
    final UsageHint usage)
    throws JCGLException
  {
    return this.index.indexBufferAllocateType(type, indices, usage);
  }

  @Override public void indexBufferDelete(
    final IndexBufferType id)
    throws JCGLException
  {
    this.index.indexBufferDelete(id);
  }

  @Override public void indexBufferUpdate(
    final IndexBufferUpdateUnmappedType data)
    throws JCGLException
  {
    this.index.indexBufferUpdate(data);
  }

  @Override public void logicOperationsDisable()
    throws JCGLExceptionRuntime
  {
    this.logic.logicOperationsDisable();
  }

  @Override public void logicOperationsEnable(
    final LogicOperation operation)
    throws JCGLExceptionRuntime
  {
    this.logic.logicOperationsEnable(operation);
  }

  @Override public boolean logicOperationsEnabled()
    throws JCGLExceptionRuntime
  {
    return this.logic.logicOperationsEnabled();
  }

  @Override public int metaGetError()
    throws JCGLExceptionRuntime
  {
    return this.meta.metaGetError();
  }

  @Override public String metaGetRenderer()
    throws JCGLExceptionRuntime
  {
    return this.meta.metaGetRenderer();
  }

  @Override public JCGLSLVersion metaGetSLVersion()
    throws JCGLExceptionRuntime
  {
    return this.meta.metaGetSLVersion();
  }

  @Override public String metaGetVendor()
    throws JCGLExceptionRuntime
  {
    return this.meta.metaGetVendor();
  }

  @Override public JCGLVersion metaGetVersion()
    throws JCGLExceptionRuntime
  {
    return this.meta.metaGetVersion();
  }

  @Override public PolygonMode polygonGetMode()
    throws JCGLExceptionRuntime
  {
    return this.polygon_modes.polygonGetMode();
  }

  @Override public void polygonSetMode(
    final PolygonMode mode)
    throws JCGLExceptionRuntime
  {
    this.polygon_modes.polygonSetMode(mode);
  }

  @Override public void polygonSmoothingDisable()
    throws JCGLExceptionRuntime
  {
    this.polygon_smooth.polygonSmoothingDisable();
  }

  @Override public void polygonSmoothingEnable()
    throws JCGLExceptionRuntime
  {
    this.polygon_smooth.polygonSmoothingEnable();
  }

  @Override public boolean polygonSmoothingIsEnabled()
    throws JCGLExceptionRuntime
  {
    return this.polygon_smooth.polygonSmoothingIsEnabled();
  }

  @Override public void programActivate(
    final ProgramUsableType p)
    throws JCGLException
  {
    this.program.programActivate(p);
  }

  @Override public void programAttributeArrayAssociate(
    final ProgramAttributeType program_attribute,
    final ArrayAttributeType array_attribute)
    throws JCGLException
  {
    this.program.programAttributeArrayAssociate(
      program_attribute,
      array_attribute);
  }

  @Override public void programAttributeArrayDisassociate(
    final ProgramAttributeType program_attribute)
    throws JCGLException
  {
    this.program.programAttributeArrayDisassociate(program_attribute);
  }

  @Override public void programAttributePutFloat(
    final ProgramAttributeType program_attribute,
    final float x)
    throws JCGLException
  {
    this.program.programAttributePutFloat(program_attribute, x);
  }

  @Override public void programAttributePutVector2f(
    final ProgramAttributeType program_attribute,
    final VectorReadable2FType x)
    throws JCGLException
  {
    this.program.programAttributePutVector2f(program_attribute, x);
  }

  @Override public void programAttributePutVector3f(
    final ProgramAttributeType program_attribute,
    final VectorReadable3FType x)
    throws JCGLException
  {
    this.program.programAttributePutVector3f(program_attribute, x);
  }

  @Override public void programAttributePutVector4f(
    final ProgramAttributeType program_attribute,
    final VectorReadable4FType x)
    throws JCGLException
  {
    this.program.programAttributePutVector4f(program_attribute, x);
  }

  @Override public ProgramType programCreateCommon(
    final String name,
    final VertexShaderType v,
    final FragmentShaderType f)
    throws JCGLException,
      JCGLExceptionProgramCompileError
  {
    return this.program.programCreateCommon(name, v, f);
  }

  @Override public void programDeactivate()
    throws JCGLException
  {
    this.program.programDeactivate();
  }

  @Override public void programDelete(
    final ProgramType p)
    throws JCGLException
  {
    this.program.programDelete(p);
  }

  @Override public int programGetMaximumActiveAttributes()
    throws JCGLException
  {
    return this.program.programGetMaximumActiveAttributes();
  }

  @Override public JCGLShadersParametersType programGetUncheckedInterface()
  {
    return this.program.programGetUncheckedInterface();
  }

  @Override public boolean programIsActive(
    final ProgramUsableType p)
    throws JCGLException
  {
    return this.program.programIsActive(p);
  }

  @Override public void programUniformPutFloat(
    final ProgramUniformType uniform,
    final float value)
    throws JCGLException
  {
    this.program.programUniformPutFloat(uniform, value);
  }

  @Override public void programUniformPutInteger(
    final ProgramUniformType uniform,
    final int value)
    throws JCGLException
  {
    this.program.programUniformPutInteger(uniform, value);
  }

  @Override public void programUniformPutMatrix3x3f(
    final ProgramUniformType uniform,
    final MatrixDirectReadable3x3FType matrix)
    throws JCGLException
  {
    this.program.programUniformPutMatrix3x3f(uniform, matrix);
  }

  @Override public void programUniformPutMatrix4x4f(
    final ProgramUniformType uniform,
    final MatrixDirectReadable4x4FType matrix)
    throws JCGLException
  {
    this.program.programUniformPutMatrix4x4f(uniform, matrix);
  }

  @Override public void programUniformPutTextureUnit(
    final ProgramUniformType uniform,
    final TextureUnitType unit)
    throws JCGLException
  {
    this.program.programUniformPutTextureUnit(uniform, unit);
  }

  @Override public void programUniformPutVector2f(
    final ProgramUniformType uniform,
    final VectorReadable2FType vector)
    throws JCGLException
  {
    this.program.programUniformPutVector2f(uniform, vector);
  }

  @Override public void programUniformPutVector2i(
    final ProgramUniformType uniform,
    final VectorReadable2IType vector)
    throws JCGLException
  {
    this.program.programUniformPutVector2i(uniform, vector);
  }

  @Override public void programUniformPutVector3f(
    final ProgramUniformType uniform,
    final VectorReadable3FType vector)
    throws JCGLException
  {
    this.program.programUniformPutVector3f(uniform, vector);
  }

  @Override public void programUniformPutVector3i(
    final ProgramUniformType uniform,
    final VectorReadable3IType vector)
    throws JCGLException
  {
    this.program.programUniformPutVector3i(uniform, vector);
  }

  @Override public void programUniformPutVector4f(
    final ProgramUniformType uniform,
    final VectorReadable4FType vector)
    throws JCGLException
  {
    this.program.programUniformPutVector4f(uniform, vector);
  }

  @Override public void programUniformPutVector4i(
    final ProgramUniformType uniform,
    final VectorReadable4IType vector)
    throws JCGLException
  {
    this.program.programUniformPutVector4i(uniform, vector);
  }

  @Override public
    RenderbufferType<RenderableDepthKind>
    renderbufferAllocateDepth16(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return this.renderbuffers.renderbufferAllocateDepth16(width, height);
  }

  @Override public
    RenderbufferType<RenderableDepthKind>
    renderbufferAllocateDepth24(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return this.renderbuffers.renderbufferAllocateDepth24(width, height);
  }

  @Override public
    RenderbufferType<RenderableDepthStencilKind>
    renderbufferAllocateDepth24Stencil8(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return this.renderbuffers.renderbufferAllocateDepth24Stencil8(
      width,
      height);
  }

  @Override public
    RenderbufferType<RenderableColorKind>
    renderbufferAllocateRGB888(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return this.renderbuffers.renderbufferAllocateRGB888(width, height);
  }

  @Override public
    RenderbufferType<RenderableColorKind>
    renderbufferAllocateRGBA8888(
      final int width,
      final int height)
      throws JCGLExceptionRuntime
  {
    return this.renderbuffers.renderbufferAllocateRGBA8888(width, height);
  }

  @Override public void renderbufferDelete(
    final RenderbufferType<?> buffer)
    throws JCGLException
  {
    this.renderbuffers.renderbufferDelete(buffer);
  }

  @Override public void scissorDisable()
    throws JCGLExceptionRuntime
  {
    this.scissor.scissorDisable();
  }

  @Override public void scissorEnable(
    final AreaInclusive area)
    throws JCGLExceptionRuntime
  {
    this.scissor.scissorEnable(area);
  }

  @Override public boolean scissorIsEnabled()
    throws JCGLExceptionRuntime
  {
    return this.scissor.scissorIsEnabled();
  }

  @Override public void stencilBufferClear(
    final int s)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.stencil.stencilBufferClear(s);
  }

  @Override public void stencilBufferDisable()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.stencil.stencilBufferDisable();
  }

  @Override public void stencilBufferEnable()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.stencil.stencilBufferEnable();
  }

  @Override public void stencilBufferFunction(
    final FaceSelection faces,
    final StencilFunction function,
    final int reference,
    final int mask)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.stencil.stencilBufferFunction(faces, function, reference, mask);
  }

  @Override public int stencilBufferGetBits()
    throws JCGLException
  {
    return this.stencil.stencilBufferGetBits();
  }

  @Override public boolean stencilBufferIsEnabled()
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    return this.stencil.stencilBufferIsEnabled();
  }

  @Override public void stencilBufferMask(
    final FaceSelection faces,
    final int mask)
    throws JCGLException,
      JCGLExceptionNoStencilBuffer
  {
    this.stencil.stencilBufferMask(faces, mask);
  }

  @Override public void stencilBufferOperation(
    final FaceSelection faces,
    final StencilOperation stencil_fail,
    final StencilOperation depth_fail,
    final StencilOperation pass)
    throws JCGLException
  {
    this.stencil
      .stencilBufferOperation(faces, stencil_fail, depth_fail, pass);
  }

  @Override public
    Texture2DStaticType
    texture2DStaticAllocateDepth24Stencil8(
      final String name,
      final int width,
      final int height,
      final TextureWrapS wrap_s,
      final TextureWrapT wrap_t,
      final TextureFilterMinification min_filter,
      final TextureFilterMagnification mag_filter)
      throws JCGLExceptionRuntime
  {
    return this.textures2d.texture2DStaticAllocateDepth24Stencil8(
      name,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGB8(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.textures2d.texture2DStaticAllocateRGB8(
      name,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public Texture2DStaticType texture2DStaticAllocateRGBA8(
    final String name,
    final int width,
    final int height,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.texture2DStaticAllocateRGBA8(
      name,
      width,
      height,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public void texture2DStaticBind(
    final TextureUnitType unit,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.textures2d.texture2DStaticBind(unit, texture);
  }

  @Override public void texture2DStaticDelete(
    final Texture2DStaticType texture)
    throws JCGLException
  {
    this.textures2d.texture2DStaticDelete(texture);
  }

  @Override public Texture2DStaticReadableType texture2DStaticGetImage(
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    return this.textures2d.texture2DStaticGetImage(texture);
  }

  @Override public boolean texture2DStaticIsBound(
    final TextureUnitType unit,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    return this.textures2d.texture2DStaticIsBound(unit, texture);
  }

  @Override public void texture2DStaticRegenerateMipmaps(
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.textures2d.texture2DStaticRegenerateMipmaps(texture);
  }

  @Override public void texture2DStaticUnbind(
    final TextureUnitType unit)
    throws JCGLException
  {
    this.textures2d.texture2DStaticUnbind(unit);
  }

  @Override public void texture2DStaticUpdate(
    final Texture2DStaticUpdateType data)
    throws JCGLException
  {
    this.textures2d.texture2DStaticUpdate(data);
  }

  @Override public
    TextureCubeStaticType
    textureCubeStaticAllocateDepth24Stencil8(
      final String name,
      final int size,
      final TextureWrapR wrap_r,
      final TextureWrapS wrap_s,
      final TextureWrapT wrap_t,
      final TextureFilterMinification min_filter,
      final TextureFilterMagnification mag_filter)
      throws JCGLExceptionRuntime
  {
    return this.textures_cube.textureCubeStaticAllocateDepth24Stencil8(
      name,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGB8(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.textures_cube.textureCubeStaticAllocateRGB8(
      name,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public TextureCubeStaticType textureCubeStaticAllocateRGBA8(
    final String name,
    final int size,
    final TextureWrapR wrap_r,
    final TextureWrapS wrap_s,
    final TextureWrapT wrap_t,
    final TextureFilterMinification min_filter,
    final TextureFilterMagnification mag_filter)
    throws JCGLExceptionRuntime
  {
    return this.textures_cube.textureCubeStaticAllocateRGBA8(
      name,
      size,
      wrap_r,
      wrap_s,
      wrap_t,
      min_filter,
      mag_filter);
  }

  @Override public void textureCubeStaticBind(
    final TextureUnitType unit,
    final TextureCubeStaticUsableType texture)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    this.textures_cube.textureCubeStaticBind(unit, texture);
  }

  @Override public void textureCubeStaticDelete(
    final TextureCubeStaticType texture)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    this.textures_cube.textureCubeStaticDelete(texture);
  }

  @Override public TextureCubeStaticReadableType textureCubeStaticGetImageLH(
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    return this.textures_cube.textureCubeStaticGetImageLH(texture, face);
  }

  @Override public TextureCubeStaticReadableType textureCubeStaticGetImageRH(
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceRH face)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    return this.textures_cube.textureCubeStaticGetImageRH(texture, face);
  }

  @Override public boolean textureCubeStaticIsBound(
    final TextureUnitType unit,
    final TextureCubeStaticUsableType texture)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted
  {
    return this.textures_cube.textureCubeStaticIsBound(unit, texture);
  }

  @Override public void textureCubeStaticUnbind(
    final TextureUnitType unit)
    throws JCGLExceptionRuntime,
      JCGLExceptionWrongContext
  {
    this.textures_cube.textureCubeStaticUnbind(unit);
  }

  @Override public void textureCubeStaticUpdateLH(
    final CubeMapFaceLH face,
    final TextureCubeStaticUpdateType data)
    throws JCGLException
  {
    this.textures_cube.textureCubeStaticUpdateLH(face, data);
  }

  @Override public void textureCubeStaticUpdateRH(
    final CubeMapFaceRH face,
    final TextureCubeStaticUpdateType data)
    throws JCGLException
  {
    this.textures_cube.textureCubeStaticUpdateRH(face, data);
  }

  @Override public int textureGetMaximumSize()
    throws JCGLExceptionRuntime
  {
    return this.texture_units.textureGetMaximumSize();
  }

  @Override public List<TextureUnitType> textureGetUnits()
    throws JCGLExceptionRuntime
  {
    return this.texture_units.textureGetUnits();
  }

  @Override public boolean textureUnitIsBound(
    final TextureUnitType unit)
    throws JCGLException
  {
    return this.texture_units.textureUnitIsBound(unit);
  }

  @Override public VertexShaderType vertexShaderCompile(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError,
      JCGLException
  {
    return this.program.vertexShaderCompile(name, lines);
  }

  @Override public void vertexShaderDelete(
    final VertexShaderType id)
    throws JCGLException
  {
    this.program.vertexShaderDelete(id);
  }

  @Override public void viewportSet(
    final AreaInclusive area)
    throws JCGLExceptionRuntime
  {
    this.viewport.viewportSet(area);
  }
}
