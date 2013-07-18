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
import java.nio.IntBuffer;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixReadable3x3F;
import com.io7m.jtensors.MatrixReadable4x4F;
import com.io7m.jtensors.VectorReadable2F;
import com.io7m.jtensors.VectorReadable2I;
import com.io7m.jtensors.VectorReadable3F;
import com.io7m.jtensors.VectorReadable4F;

/**
 * <p>
 * A class implementing {@link GLInterfaceGLES2} that exposes only the
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

@NotThreadSafe final class GLInterfaceGLES2_JOGL_ES2 implements
  GLInterfaceGLES2
{
  /**
   * The packed depth/stencil extension.
   */

  private class ExtPackedDepthStencil implements
    GLExtensionPackedDepthStencil
  {
    ExtPackedDepthStencil()
    {
      // Nothing
    }

    @Override public void framebufferDrawAttachDepthStencilRenderbuffer(
      final @Nonnull FramebufferReference framebuffer,
      final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
      throws GLException,
        ConstraintError
    {
      JOGL_GL_Functions.framebufferDrawAttachDepthStencilRenderbuffer(
        GLInterfaceGLES2_JOGL_ES2.this.contextGetGLES2(),
        GLInterfaceGLES2_JOGL_ES2.this.state,
        GLInterfaceGLES2_JOGL_ES2.this.log,
        framebuffer,
        renderbuffer);
    }

    @Override public @Nonnull
      Renderbuffer<RenderableDepthStencil>
      renderbufferAllocateDepth24Stencil8(
        final int width,
        final int height)
        throws ConstraintError,
          GLException
    {
      return Renderbuffer.unsafeBrandDepthStencil(JOGL_GL2ES2_Functions
        .renderbufferAllocate(
          GLInterfaceGLES2_JOGL_ES2.this.contextGetGLES2(),
          GLInterfaceGLES2_JOGL_ES2.this.state,
          GLInterfaceGLES2_JOGL_ES2.this.log,
          RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
          width,
          height));
    }
  }

  /**
   * Support for the packed depth/stencil extension.
   */

  private class ExtPackedDepthStencilSupport implements
    GLExtensionSupport<GLExtensionPackedDepthStencil>
  {
    ExtPackedDepthStencilSupport()
    {
      // Nothing.
    }

    @Override public
      Option<GLExtensionPackedDepthStencil>
      extensionGetSupport()
        throws ConstraintError,
          GLException
    {
      return GLInterfaceGLES2_JOGL_ES2.this.ext_packed_depth_stencil_opt;
    }
  }

  final @Nonnull Log                                                       log;
  final @Nonnull GLContext                                                 context;
  final @Nonnull GLStateCache                                              state;

  private final @Nonnull GLExtensionSupport<GLExtensionPackedDepthStencil> ext_packed_depth_stencil_support;
  private final @Nonnull GLExtensionPackedDepthStencil                     ext_packed_depth_stencil;
  final @Nonnull Option<GLExtensionPackedDepthStencil>                     ext_packed_depth_stencil_opt;

  GLInterfaceGLES2_JOGL_ES2(
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

    /**
     * Initialize extensions.
     */

    this.ext_packed_depth_stencil_support =
      new ExtPackedDepthStencilSupport();
    this.ext_packed_depth_stencil = new ExtPackedDepthStencil();
    this.ext_packed_depth_stencil_opt = this.extPackedDepthStencilCheck();

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
     * Initialize various constants.
     */

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
    return JOGL_GL_Functions.arrayBufferAllocate(
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
    JOGL_GL_Functions.arrayBufferBind(this.contextGetGLES2(), buffer);
  }

  @Override public void arrayBufferBindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.arrayBufferBindVertexAttribute(
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
    JOGL_GL_Functions.arrayBufferDelete(
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
    return JOGL_GL_Functions.arrayBufferIsBound(this.contextGetGLES2(), id);
  }

  @Override public void arrayBufferUnbind()
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.arrayBufferUnbind(this.contextGetGLES2());
  }

  @Override public void arrayBufferUnbindVertexAttribute(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws GLException,
      ConstraintError
  {
    JOGL_GL2ES2_Functions.arrayBufferUnbindVertexAttribute(
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
    JOGL_GL_Functions.arrayBufferUpdate(this.contextGetGLES2(), buffer, data);
  }

  @Override public void blendingDisable()
    throws GLException
  {
    JOGL_GL_Functions.blendingDisable(this.contextGetGLES2());
  }

  @Override public void blendingEnable(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      GLException
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
      GLException
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
      GLException
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
      GLException
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
      GLException
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
      GLException
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
      GLException
  {
    JOGL_GL_Functions.colorBufferClear3f(this.contextGetGLES2(), r, g, b);
  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.colorBufferClear4f(this.contextGetGLES2(), r, g, b, a);
  }

  @Override public void colorBufferClearV3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.colorBufferClearV3f(this.contextGetGLES2(), color);
  }

  @Override public void colorBufferClearV4f(
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.colorBufferClearV4f(this.contextGetGLES2(), color);
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.colorBufferMask(this.contextGetGLES2(), r, g, b, a);
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws GLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusAlpha(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws GLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusBlue(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws GLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusGreen(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws GLException
  {
    return JOGL_GL_Functions.colorBufferMaskStatusRed(
      this.contextGetGLES2(),
      this.state);
  }

  @Nonnull GL2ES2 contextGetGLES2()
  {
    return this.context.getGL().getGL2ES2();
  }

  @Override public void cullingDisable()
    throws GLException
  {
    JOGL_GL_Functions.cullingDisable(this.contextGetGLES2());
  }

  @Override public void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.cullingEnable(this.contextGetGLES2(), faces, order);
  }

  @Override public boolean cullingIsEnabled()
    throws GLException
  {
    return JOGL_GL_Functions.cullingIsEnabled(this.contextGetGLES2());
  }

  @Override public void depthBufferClear(
    final float depth)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.depthBufferClear(
      this.contextGetGLES2(),
      this.state,
      depth);
  }

  @Override public void depthBufferDisable()
    throws GLException
  {
    JOGL_GL_Functions.depthBufferDisable(this.contextGetGLES2());
  }

  @Override public void depthBufferEnable(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.depthBufferEnable(
      this.contextGetGLES2(),
      this.state,
      function);
  }

  @Override public int depthBufferGetBits()
    throws GLException
  {
    return JOGL_GL_Functions.depthBufferGetBits(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean depthBufferIsEnabled()
    throws GLException
  {
    return JOGL_GL_Functions.depthBufferIsEnabled(this.contextGetGLES2());
  }

  @Override public void depthBufferWriteDisable()
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.depthBufferWriteDisable(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public void depthBufferWriteEnable()
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.depthBufferWriteEnable(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws GLException
  {
    return JOGL_GL_Functions.depthBufferWriteIsEnabled(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBuffer indices)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.drawElements(this.contextGetGLES2(), mode, indices);
  }

  @Override public boolean errorCodeIsInvalidOperation(
    final int code)
  {
    return code == GL.GL_INVALID_OPERATION;
  }

  @Override public @Nonnull
    GLExtensionSupport<GLExtensionPackedDepthStencil>
    extensionPackedDepthStencil()
  {
    return this.ext_packed_depth_stencil_support;
  }

  private Option<GLExtensionPackedDepthStencil> extPackedDepthStencilCheck()
  {
    final String names[] =
      { "GL_OES_packed_depth_stencil", "GL_EXT_packed_depth_stencil", };

    for (final String name : names) {
      if (GLInterfaceGLES2_JOGL_ES2.this.context.isExtensionAvailable(name)) {
        this.log.debug("Extension " + name + " is available");
        return new Option.Some<GLExtensionPackedDepthStencil>(
          new ExtPackedDepthStencil());
      }
    }

    return new Option.None<GLExtensionPackedDepthStencil>();
  }

  @Override public void fragmentShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull FragmentShader shader)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.fragmentShaderAttach(
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
    return JOGL_GL2ES2_Functions.fragmentShaderCompile(
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
    JOGL_GL2ES2_Functions.fragmentShaderDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      id);
  }

  @Override public @Nonnull FramebufferReference framebufferAllocate()
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferAllocate(
      this.contextGetGLES2(),
      this.state,
      this.log);
  }

  @Override public void framebufferDelete(
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      framebuffer);
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions
      .framebufferDrawAnyIsBound(this.contextGetGLES2());
  }

  @Override public void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorRenderbuffer(
      this.contextGetGLES2(),
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachColorTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawAttachColorTexture2D(
      this.contextGetGLES2(),
      this.state,
      this.log,
      framebuffer,
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
      this.contextGetGLES2(),
      this.state,
      this.log,
      framebuffer,
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
      this.contextGetGLES2(),
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
      this.contextGetGLES2(),
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
      this.contextGetGLES2(),
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
    JOGL_GL_Functions
      .framebufferDrawBind(this.contextGetGLES2(), framebuffer);
  }

  @Override public boolean framebufferDrawIsBound(
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferDrawIsBound(
      this.contextGetGLES2(),
      framebuffer);
  }

  @Override public void framebufferDrawUnbind()
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.framebufferDrawUnbind(this.contextGetGLES2());
  }

  @Override public @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    return JOGL_GL_Functions.framebufferDrawValidate(
      this.contextGetGLES2(),
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
      GLException
  {
    JOGL_GL_Functions.indexBufferDelete(
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
    JOGL_GL_Functions.indexBufferUpdate(this.contextGetGLES2(), buffer, data);
  }

  @Override public int metaGetError()
    throws GLException
  {
    return JOGL_GL_Functions.metaGetError(this.contextGetGLES2());
  }

  @Override public String metaGetRenderer()
    throws GLException
  {
    return JOGL_GL_Functions.metaGetRenderer(this.contextGetGLES2());
  }

  @Override public String metaGetVendor()
    throws GLException
  {
    return JOGL_GL_Functions.metaGetVendor(this.contextGetGLES2());
  }

  @Override public String metaGetVersion()
    throws GLException
  {
    return JOGL_GL_Functions.metaGetVersion(this.contextGetGLES2());
  }

  @Override public int metaGetVersionMajor()
  {
    return JOGL_GL_Functions.metaGetVersionMajor(this.contextGetGLES2());
  }

  @Override public int metaGetVersionMinor()
  {
    return JOGL_GL_Functions.metaGetVersionMinor(this.contextGetGLES2());
  }

  @Override public boolean metaIsES()
  {
    return JOGL_GL_Functions.metaIsES(this.contextGetGLES2());
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
    return JOGL_GL2ES2_Functions.programCreate(
      this.contextGetGLES2(),
      this.state,
      this.log,
      name);
  }

  @Override public void programDeactivate()
    throws GLException
  {
    JOGL_GL2ES2_Functions.programDeactivate(this.contextGetGLES2());
  }

  @Override public void programDelete(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.programDelete(
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
    JOGL_GL2ES2_Functions.programGetAttributes(
      this.contextGetGLES2(),
      this.state,
      this.log,
      program,
      out);
  }

  @Override public int programGetMaximumActiveAttributes()
    throws GLException
  {
    return JOGL_GL2ES2_Functions.programGetMaximumActiveAttributes(
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
    JOGL_GL2ES2_Functions.programGetUniforms(
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
    return JOGL_GL2ES2_Functions.programIsActive(
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
    JOGL_GL2ES2_Functions.programLink(
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
    JOGL_GL2ES2_Functions.programPutUniformFloat(
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
    JOGL_GL2ES2_Functions.programPutUniformMatrix3x3f(
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
    JOGL_GL2ES2_Functions.programPutUniformMatrix4x4f(
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
    JOGL_GL2ES2_Functions.programPutUniformTextureUnit(
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
    JOGL_GL2ES2_Functions.programPutUniformVector2f(
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
    JOGL_GL2ES2_Functions.programPutUniformVector2i(
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
    JOGL_GL2ES2_Functions.programPutUniformVector3f(
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
    JOGL_GL2ES2_Functions.programPutUniformVector4f(
      this.contextGetGLES2(),
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
        GLException
  {
    return Renderbuffer.unsafeBrandDepth(JOGL_GL2ES2_Functions
      .renderbufferAllocate(
        this.contextGetGLES2(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_DEPTH_16,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableColor>
    renderbufferAllocateRGB565(
      final int width,
      final int height)
      throws ConstraintError,
        GLException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL2ES2_Functions
      .renderbufferAllocate(
        this.contextGetGLES2(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_COLOR_RGB_565,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableColor>
    renderbufferAllocateRGBA4444(
      final int width,
      final int height)
      throws ConstraintError,
        GLException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL2ES2_Functions
      .renderbufferAllocate(
        this.contextGetGLES2(),
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
        GLException
  {
    return Renderbuffer.unsafeBrandColor(JOGL_GL2ES2_Functions
      .renderbufferAllocate(
        this.contextGetGLES2(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_COLOR_RGBA_5551,
        width,
        height));
  }

  @Override public @Nonnull
    Renderbuffer<RenderableStencil>
    renderbufferAllocateStencil8(
      final int width,
      final int height)
      throws ConstraintError,
        GLException
  {
    return Renderbuffer.unsafeBrandStencil(JOGL_GL2ES2_Functions
      .renderbufferAllocate(
        this.contextGetGLES2(),
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_STENCIL_8,
        width,
        height));
  }

  @Override public void renderbufferDelete(
    final @Nonnull Renderbuffer<?> buffer)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.renderbufferDelete(
      this.contextGetGLES2(),
      this.state,
      this.log,
      buffer);
  }

  @Override public void scissorDisable()
    throws GLException
  {
    JOGL_GL_Functions.scissorDisable(this.contextGetGLES2());
  }

  @Override public void scissorEnable(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.scissorEnable(
      this.contextGetGLES2(),
      position,
      dimensions);
  }

  @Override public boolean scissorIsEnabled()
    throws GLException
  {
    return JOGL_GL_Functions.scissorIsEnabled(this.contextGetGLES2());
  }

  @Override public void stencilBufferClear(
    final int stencil)
    throws GLException,
      ConstraintError
  {
    JOGL_GL_Functions.stencilBufferClear(
      this.contextGetGLES2(),
      this.state,
      stencil);
  }

  @Override public void stencilBufferDisable()
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.stencilBufferDisable(this.contextGetGLES2());
  }

  @Override public void stencilBufferEnable()
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.stencilBufferEnable(this.contextGetGLES2(), this.state);
  }

  @Override public void stencilBufferFunction(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      GLException
  {
    JOGL_GL2ES2_Functions.stencilBufferFunction(
      this.contextGetGLES2(),
      faces,
      function,
      reference,
      mask);
  }

  @Override public int stencilBufferGetBits()
    throws GLException
  {
    return JOGL_GL_Functions.stencilBufferGetBits(
      this.contextGetGLES2(),
      this.state);
  }

  @Override public boolean stencilBufferIsEnabled()
    throws GLException
  {
    return JOGL_GL_Functions.stencilBufferIsEnabled(this.contextGetGLES2());
  }

  @Override public void stencilBufferMask(
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      GLException
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
      GLException
  {
    JOGL_GL2ES2_Functions.stencilBufferOperation(
      this.contextGetGLES2(),
      faces,
      stencil_fail,
      depth_fail,
      pass);
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
    return JOGL_GLES2_Functions.texture2DStaticAllocate(
      this.contextGetGLES2(),
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
    return JOGL_GLES2_Functions.texture2DStaticAllocate(
      this.contextGetGLES2(),
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
    return JOGL_GLES2_Functions.texture2DStaticAllocate(
      this.contextGetGLES2(),
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
    return JOGL_GLES2_Functions.texture2DStaticAllocate(
      this.contextGetGLES2(),
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
    return JOGL_GLES2_Functions.texture2DStaticAllocate(
      this.contextGetGLES2(),
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
    JOGL_GL_Functions.texture2DStaticBind(
      this.contextGetGLES2(),
      unit,
      texture);
  }

  @Override public void texture2DStaticDelete(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.texture2DStaticDelete(
      this.contextGetGLES2(),
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
      this.contextGetGLES2(),
      this.state,
      unit,
      texture);
  }

  @Override public void texture2DStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.texture2DStaticUnbind(this.contextGetGLES2(), unit);
  }

  @Override public void texture2DStaticUpdate(
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      GLException
  {
    JOGL_GLES2_Functions.texture2DStaticUpdate(this.contextGetGLES2(), data);
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
    return JOGL_GLES2_Functions.textureCubeStaticAllocate(
      this.contextGetGLES2(),
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
    return JOGL_GLES2_Functions.textureCubeStaticAllocate(
      this.contextGetGLES2(),
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
    return JOGL_GLES2_Functions.textureCubeStaticAllocate(
      this.contextGetGLES2(),
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
    return JOGL_GLES2_Functions.textureCubeStaticAllocate(
      this.contextGetGLES2(),
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
    return JOGL_GLES2_Functions.textureCubeStaticAllocate(
      this.contextGetGLES2(),
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
      this.contextGetGLES2(),
      unit,
      texture);
  }

  @Override public void textureCubeStaticDelete(
    final @Nonnull TextureCubeStatic texture)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.textureCubeStaticDelete(
      this.contextGetGLES2(),
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
      this.contextGetGLES2(),
      this.state,
      unit,
      texture);
  }

  @Override public void textureCubeStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      GLException
  {
    JOGL_GL_Functions.textureCubeStaticUnbind(this.contextGetGLES2(), unit);
  }

  @Override public void textureCubeStaticUpdate(
    final @Nonnull CubeMapFace face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      GLException
  {
    JOGL_GLES2_Functions.textureCubeStaticUpdate(
      this.contextGetGLES2(),
      face,
      data);
  }

  @Override public int textureGetMaximumSize()
    throws GLException
  {
    return JOGL_GL_Functions.textureGetMaximumSize(
      this.contextGetGLES2(),
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
    return JOGL_GL2ES2_Functions.vertexShaderCompile(
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
    JOGL_GL2ES2_Functions.vertexShaderDelete(
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
    JOGL_GL_Functions.viewportSet(
      this.contextGetGLES2(),
      position,
      dimensions);
  }
}
