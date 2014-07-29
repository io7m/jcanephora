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

package com.io7m.jcanephora.fake;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateUnmappedType;
import com.io7m.jcanephora.ArrayBufferUsableType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.BlendEquationGL3;
import com.io7m.jcanephora.BlendEquationGLES2;
import com.io7m.jcanephora.BlendFunction;
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
import com.io7m.jcanephora.JCGLDebugging;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionBlendingMisconfigured;
import com.io7m.jcanephora.JCGLExceptionBufferNotBound;
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
import com.io7m.jcanephora.RenderableStencilKind;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.RenderbufferUsableType;
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
import com.io7m.jcanephora.api.JCGLInterfaceGL2Type;
import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jcanephora.api.JCGLSoftRestrictionsType;
import com.io7m.jfunctional.Option;
import com.io7m.jfunctional.OptionType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jtensors.MatrixReadable3x3FType;
import com.io7m.jtensors.MatrixReadable4x4FType;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;

final class JCGLInterfaceGL2_Fake implements JCGLInterfaceGL2Type
{
  private final FakeArrays                                arrays;

  private final FakeBlending                              blending;
  private final FakeColorBuffer                           color_buffer;
  private final FakeColorAttachmentPoints                 color_points;
  private final FakeCulling                               cull;
  private final FakeDepthBuffer                           depth;
  private final FakeDraw                                  draw;
  private final FakeDrawBuffers                           draw_buffers;
  private final FakeErrors                                errors;
  private final OptionType<JCGLExtensionDepthTextureType> ext_depth_texture;
  private final FakeFramebuffers                          framebuffers;
  private final FakeIndexBuffers                          index;
  private final LogUsableType                             log;
  private final FakeLogic                                 logic;
  private final FakeMeta                                  meta;
  private final FakePolygonMode                           polygon_modes;
  private final FakePolygonSmoothing                      polygon_smooth;
  private final FakeShaders                               program;
  private final FakeRenderbuffers                         renderbuffers;
  private final FakeScissor                               scissor;
  private final FakeStencil                               stencil;
  private final FakeLogMessageCacheType                   tcache;
  private final FakeTextureUnits                          texture_units;
  private final FakeTexturesCubeStatic                    textures_cube;
  private final FakeTextures2DStatic                      textures2d;
  private final FakeViewport                              viewport;

  JCGLInterfaceGL2_Fake(
    final FakeContext in_context,
    final FakeShaderControlType in_shader_control,
    final LogUsableType in_log,
    final JCGLDebugging in_debug,
    final @Nullable PrintStream in_trace,
    final JCGLSoftRestrictionsType in_restrictions)
  {
    this.tcache = new FakeLogMessageCacheType() {
      private final StringBuilder b = new StringBuilder();

      @Override public StringBuilder getTextCache()
      {
        return this.b;
      }
    };

    final JCGLNamedExtensionsType extensions = new JCGLNamedExtensionsType() {
      @Override public boolean extensionIsSupported(
        final String name)
      {
        return true;
      }

      @Override public boolean extensionIsVisible(
        final String name)
      {
        return true;
      }
    };

    this.log = NullCheck.notNull(in_log, "Log").with("fake-gl2");

    // XXX: Provide the extension!
    this.ext_depth_texture = Option.none();

    this.arrays = new FakeArrays(in_context, this.log, this.tcache);
    this.blending = new FakeBlending(in_context, this.log);
    this.color_buffer = new FakeColorBuffer(in_context, this.log);
    this.color_points =
      new FakeColorAttachmentPoints(in_context, this.log, this.tcache);
    this.cull = new FakeCulling(this.log);
    this.draw = new FakeDraw(in_context, this.arrays, this.log);
    this.draw_buffers =
      new FakeDrawBuffers(in_context, this.log, this.tcache);
    this.errors = new FakeErrors(in_context, this.log);
    this.meta = new FakeMeta(in_context, this.log);
    this.index = new FakeIndexBuffers(in_context, this.log, this.tcache);
    this.logic = new FakeLogic(in_context, this.log);
    this.polygon_modes = new FakePolygonMode(in_context, this.log);
    this.polygon_smooth = new FakePolygonSmoothing(in_context, this.log);
    this.program =
      new FakeShaders(
        in_context,
        in_shader_control,
        this.log,
        this.tcache,
        this.arrays,
        this.draw_buffers);
    this.renderbuffers =
      new FakeRenderbuffers(in_context, this.log, this.tcache);
    this.scissor = new FakeScissor(in_context, this.log);

    this.texture_units =
      new FakeTextureUnits(in_context, this.log, this.tcache, in_restrictions);
    this.textures2d =
      new FakeTextures2DStatic(
        in_context,
        this.texture_units,
        this.tcache,
        this.log);
    this.textures_cube =
      new FakeTexturesCubeStatic(
        in_context,
        this.texture_units,
        this.tcache,
        this.log);

    this.viewport = new FakeViewport(in_context, this.log);

    this.framebuffers =
      new FakeFramebuffers(
        in_context,
        this.log,
        this.tcache,
        this.color_points,
        this.draw_buffers,
        this.meta,
        this.renderbuffers,
        extensions,
        this.textures2d);
    this.depth = new FakeDepthBuffer(in_context, this.framebuffers);
    this.stencil = new FakeStencil(in_context, this.framebuffers, this.log);
  }

  @Override public ArrayBufferType arrayBufferAllocate(
    final long elements,
    final ArrayDescriptor descriptor,
    final UsageHint usage)
    throws JCGLException
  {
    return this.arrays.arrayBufferAllocate(elements, descriptor, usage);
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
    return this.color_buffer.colorBufferMaskStatusAlpha();
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws JCGLExceptionRuntime
  {
    return this.color_buffer.colorBufferMaskStatusBlue();
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws JCGLExceptionRuntime
  {
    return this.color_buffer.colorBufferMaskStatusGreen();
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws JCGLExceptionRuntime
  {
    return this.color_buffer.colorBufferMaskStatusRed();
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
      JCGLExceptionDeleted,
      JCGLExceptionBufferNotBound
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

  @Override public FramebufferType framebufferAllocate()
    throws JCGLException
  {
    return this.framebuffers.framebufferAllocate();
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

  @Override public void framebufferDrawAttachColorRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableColorKind> renderbuffer)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawAttachColorRenderbuffer(
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachColorRenderbufferAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final RenderbufferUsableType<RenderableColorKind> renderbuffer)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawAttachColorRenderbufferAt(
      framebuffer,
      point,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachColorTexture2D(
    final FramebufferType framebuffer,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawAttachColorTexture2D(
      framebuffer,
      texture);
  }

  @Override public void framebufferDrawAttachColorTexture2DAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawAttachColorTexture2DAt(
      framebuffer,
      point,
      texture);
  }

  @Override public void framebufferDrawAttachColorTextureCube(
    final FramebufferType framebuffer,
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawAttachColorTextureCube(
      framebuffer,
      texture,
      face);
  }

  @Override public void framebufferDrawAttachColorTextureCubeAt(
    final FramebufferType framebuffer,
    final FramebufferColorAttachmentPointType point,
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawAttachColorTextureCubeAt(
      framebuffer,
      point,
      texture,
      face);
  }

  @Override public void framebufferDrawAttachDepthRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableDepthKind> renderbuffer)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawAttachDepthRenderbuffer(
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachDepthStencilRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableDepthStencilKind> renderbuffer)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawAttachDepthStencilRenderbuffer(
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachDepthStencilTexture2D(
    final FramebufferType framebuffer,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawAttachDepthStencilTexture2D(
      framebuffer,
      texture);
  }

  @Override public void framebufferDrawAttachDepthTexture2D(
    final FramebufferType framebuffer,
    final Texture2DStaticUsableType texture)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawAttachDepthTexture2D(
      framebuffer,
      texture);
  }

  @Override public void framebufferDrawAttachStencilRenderbuffer(
    final FramebufferType framebuffer,
    final RenderbufferUsableType<RenderableStencilKind> renderbuffer)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawAttachStencilRenderbuffer(
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawBind(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    this.framebuffers.framebufferDrawBind(framebuffer);
  }

  @Override public boolean framebufferDrawIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    return this.framebuffers.framebufferDrawIsBound(framebuffer);
  }

  @Override public
    void
    framebufferDrawSetBuffers(
      final FramebufferType framebuffer,
      final Map<FramebufferDrawBufferType, FramebufferColorAttachmentPointType> mappings)
      throws JCGLException
  {
    this.framebuffers.framebufferDrawSetBuffers(framebuffer, mappings);
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

  @Override public boolean framebufferReadAnyIsBound()
    throws JCGLException
  {
    return this.framebuffers.framebufferReadAnyIsBound();
  }

  @Override public void framebufferReadBind(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    this.framebuffers.framebufferReadBind(framebuffer);
  }

  @Override public boolean framebufferReadIsBound(
    final FramebufferUsableType framebuffer)
    throws JCGLException
  {
    return this.framebuffers.framebufferReadIsBound(framebuffer);
  }

  @Override public void framebufferReadUnbind()
    throws JCGLException
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
    final MatrixReadable3x3FType matrix)
    throws JCGLException
  {
    this.program.programUniformPutMatrix3x3f(uniform, matrix);
  }

  @Override public void programUniformPutMatrix4x4f(
    final ProgramUniformType uniform,
    final MatrixReadable4x4FType matrix)
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
    return this.textures2d.texture2DStaticAllocateRGBA8(
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
      throws JCGLException
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
    throws JCGLException
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
    throws JCGLException
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
    throws JCGLException
  {
    this.textures_cube.textureCubeStaticBind(unit, texture);
  }

  @Override public void textureCubeStaticDelete(
    final TextureCubeStaticType texture)
    throws JCGLException
  {
    this.textures_cube.textureCubeStaticDelete(texture);
  }

  @Override public TextureCubeStaticReadableType textureCubeStaticGetImageLH(
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceLH face)
    throws JCGLException
  {
    return this.textures_cube.textureCubeStaticGetImageLH(texture, face);
  }

  @Override public TextureCubeStaticReadableType textureCubeStaticGetImageRH(
    final TextureCubeStaticUsableType texture,
    final CubeMapFaceRH face)
    throws JCGLException
  {
    return this.textures_cube.textureCubeStaticGetImageRH(texture, face);
  }

  @Override public boolean textureCubeStaticIsBound(
    final TextureUnitType unit,
    final TextureCubeStaticUsableType texture)
    throws JCGLException
  {
    return this.textures_cube.textureCubeStaticIsBound(unit, texture);
  }

  @Override public void textureCubeStaticUnbind(
    final TextureUnitType unit)
    throws JCGLException
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
