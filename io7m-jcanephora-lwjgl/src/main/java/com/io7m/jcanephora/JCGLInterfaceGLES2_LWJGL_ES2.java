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

import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
 * An implementation of the GLES2 interface, running on an OpenGL ES2
 * implementation, using LWJGL as the bindings.
 */

@NotThreadSafe public final class JCGLInterfaceGLES2_LWJGL_ES2 implements
  JCGLInterfaceGLES2
{
  /**
   * The packed depth/stencil extension.
   */

  private class ExtPackedDepthStencil implements
    JCGLExtensionPackedDepthStencil
  {
    ExtPackedDepthStencil()
    {
      // Nothing
    }

    @Override public void framebufferDrawAttachDepthStencilRenderbuffer(
      final @Nonnull FramebufferReference framebuffer,
      final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
      throws JCGLException,
        ConstraintError
    {
      LWJGL_GLES2Functions.framebufferDrawAttachDepthStencilRenderbuffer(
        JCGLInterfaceGLES2_LWJGL_ES2.this.state,
        JCGLInterfaceGLES2_LWJGL_ES2.this.log,
        framebuffer,
        renderbuffer);
    }

    @Override public @Nonnull
      Renderbuffer<RenderableDepthStencil>
      renderbufferAllocateDepth24Stencil8(
        final int width,
        final int height)
        throws ConstraintError,
          JCGLException
    {
      return Renderbuffer.unsafeBrandDepthStencil(LWJGL_GLES2Functions
        .renderbufferAllocate(
          JCGLInterfaceGLES2_LWJGL_ES2.this.state,
          JCGLInterfaceGLES2_LWJGL_ES2.this.log,
          RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
          width,
          height));
    }
  }

  /**
   * Support for the packed depth/stencil extension.
   */

  private class ExtPackedDepthStencilSupport implements
    JCGLExtensionSupport<JCGLExtensionPackedDepthStencil>
  {
    ExtPackedDepthStencilSupport()
    {
      // Nothing.
    }

    @Override public
      Option<JCGLExtensionPackedDepthStencil>
      extensionGetSupport()
        throws ConstraintError,
          JCGLException
    {
      final String names[] =
        { "GL_OES_packed_depth_stencil", "GL_EXT_packed_depth_stencil", };

      final String all = GL11.glGetString(GL11.GL_EXTENSIONS);
      final StringTokenizer tok = new StringTokenizer(all);

      while (tok.hasMoreTokens()) {
        final String extension = tok.nextToken();
        for (final String name : names) {
          if (extension.equals(name)) {
            return new Option.Some<JCGLExtensionPackedDepthStencil>(
              new ExtPackedDepthStencil());
          }
        }
      }

      return new Option.None<JCGLExtensionPackedDepthStencil>();
    }
  }

  final @Nonnull Log                                                           log;
  final @Nonnull JCGLStateCache                                                state;
  private final @Nonnull JCGLVersion                                           version;
  private final @Nonnull JCGLExtensionSupport<JCGLExtensionPackedDepthStencil> ext_packed_depth_stencil_support;
  private final @Nonnull JCGLExtensionPackedDepthStencil                       ext_packed_depth_stencil;

  JCGLInterfaceGLES2_LWJGL_ES2(
    final @Nonnull Log log)
    throws ConstraintError,
      JCGLException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log, "log output"), "lwjgl-es2");
    this.state = new JCGLStateCache();

    /**
     * Initialize texture unit cache.
     */

    this.state.texture_units =
      LWJGL_GLES2Functions.textureGetUnitsActual(this.state, this.log);

    /**
     * Initialize color attachment point cache.
     */

    this.state.color_attachments =
      LWJGL_GLES2Functions.framebufferGetAttachmentPointsActual(
        this.state,
        this.log);

    /**
     * Initialize extensions.
     */

    this.ext_packed_depth_stencil_support =
      new ExtPackedDepthStencilSupport();
    this.ext_packed_depth_stencil = new ExtPackedDepthStencil();

    /**
     * Initialize various constants.
     */

    {
      final IntBuffer cache = this.state.getIntegerCache();
      GL11.glGetInteger(GL12.GL_ALIASED_POINT_SIZE_RANGE, cache);
      this.state.point_min_width = cache.get();
      this.state.point_max_width = cache.get();
      JCGLError.check(this);
    }

    /**
     * Initialize version info.
     */

    this.version = LWJGL_GLES2Functions.metaGetVersion();
  }

  @Override public ArrayBuffer arrayBufferAllocate(
    final long elements,
    final @Nonnull ArrayBufferTypeDescriptor descriptor,
    final @Nonnull UsageHint usage)
    throws JCGLException,
      ConstraintError
  {
    return LWJGL_GLES2Functions.arrayBufferAllocate(
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
    LWJGL_GLES2Functions.arrayBufferBind(buffer);
  }

  @Override public void arrayBufferBindVertexAttribute(
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.arrayBufferBindVertexAttribute(
      this.state,
      buffer_attribute,
      program_attribute);
  }

  @Override public void arrayBufferDelete(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.arrayBufferDelete(

    this.log, this.state, id);
  }

  @Override public boolean arrayBufferIsBound(
    final @Nonnull ArrayBufferUsable id)
    throws ConstraintError,
      JCGLException
  {
    return LWJGL_GLES2Functions.arrayBufferIsBound(id);
  }

  @Override public void arrayBufferUnbind()
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.arrayBufferUnbind();
  }

  @Override public void arrayBufferUnbindVertexAttribute(
    final @Nonnull ArrayBufferAttribute buffer_attribute,
    final @Nonnull ProgramAttribute program_attribute)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.arrayBufferUnbindVertexAttribute(
      this.state,
      buffer_attribute,
      program_attribute);
  }

  @Override public void arrayBufferUpdate(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferWritableData data)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.arrayBufferUpdate(buffer, data);
  }

  @Override public void blendingDisable()
    throws JCGLException
  {
    LWJGL_GLES2Functions.blendingDisable();
  }

  @Override public void blendingEnable(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.blendingEnable(

    source_factor, destination_factor);
  }

  @Override public void blendingEnableSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.blendingEnableSeparate(

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
      JCGLException
  {
    LWJGL_GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(

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
      JCGLException
  {
    LWJGL_GLES2Functions.blendingEnableWithEquationES2(

    source_factor, destination_factor, equation);
  }

  @Override public void blendingEnableWithEquationSeparateES2(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation_rgb,
    final @Nonnull BlendEquationGLES2 equation_alpha)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.blendingEnableWithEquationSeparateES2(

    source_factor, destination_factor, equation_rgb, equation_alpha);
  }

  @Override public boolean blendingIsEnabled()
    throws ConstraintError,
      JCGLException
  {
    return LWJGL_GLES2Functions.blendingIsEnabled();
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.colorBufferClear3f(r, g, b);
  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.colorBufferClear4f(r, g, b, a);
  }

  @Override public void colorBufferClearV3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.colorBufferClearV3f(color);
  }

  @Override public void colorBufferClearV4f(
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.colorBufferClearV4f(color);
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.colorBufferMask(r, g, b, a);
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.colorBufferMaskStatusAlpha(

    this.state);
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.colorBufferMaskStatusBlue(

    this.state);
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.colorBufferMaskStatusGreen(

    this.state);
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.colorBufferMaskStatusRed(

    this.state);
  }

  @Override public void cullingDisable()
    throws JCGLException
  {
    LWJGL_GLES2Functions.cullingDisable();
  }

  @Override public void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.cullingEnable(faces, order);
  }

  @Override public boolean cullingIsEnabled()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.cullingIsEnabled();
  }

  @Override public void depthBufferClear(
    final float depth)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.depthBufferClear(this.state, depth);
  }

  @Override public void depthBufferDisable()
    throws JCGLException
  {
    LWJGL_GLES2Functions.depthBufferDisable();
  }

  @Override public void depthBufferEnable(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.depthBufferEnable(

    this.state, function);
  }

  @Override public int depthBufferGetBits()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.depthBufferGetBits(this.state);
  }

  @Override public boolean depthBufferIsEnabled()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.depthBufferIsEnabled();
  }

  @Override public void depthBufferWriteDisable()
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.depthBufferWriteDisable(this.state);
  }

  @Override public void depthBufferWriteEnable()
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.depthBufferWriteEnable(this.state);
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.depthBufferWriteIsEnabled(

    this.state);
  }

  @Override public void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBuffer indices)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.drawElements(mode, indices);
  }

  @Override public boolean errorCodeIsInvalidOperation(
    final int code)
  {
    return code == GL11.GL_INVALID_OPERATION;
  }

  @Override public @Nonnull
    JCGLExtensionSupport<JCGLExtensionPackedDepthStencil>
    extensionPackedDepthStencil()
  {
    return this.ext_packed_depth_stencil_support;
  }

  @Override public void fragmentShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull FragmentShader shader)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.fragmentShaderAttach(

    this.state, this.log, program, shader);
  }

  @Override public FragmentShader fragmentShaderCompile(
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException
  {
    return LWJGL_GLES2Functions.fragmentShaderCompile(
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
    LWJGL_GLES2Functions.fragmentShaderDelete(

    this.state, this.log, id);
  }

  @Override public @Nonnull FramebufferReference framebufferAllocate()
    throws JCGLException,
      ConstraintError
  {
    return LWJGL_GLES2Functions.framebufferAllocate(this.state, this.log);
  }

  @Override public void framebufferDelete(
    final @Nonnull FramebufferReference framebuffer)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.framebufferDelete(this.state, this.log, framebuffer);
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.framebufferDrawAnyIsBound();
  }

  @Override public void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.framebufferDrawAttachColorRenderbuffer(
      this.state,
      this.log,
      framebuffer,
      renderbuffer);
  }

  @Override public void framebufferDrawAttachColorTexture2D(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.framebufferDrawAttachColorTexture2D(
      this.state,
      this.log,
      framebuffer,
      texture);
  }

  @Override public void framebufferDrawAttachColorTextureCube(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFace face)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.framebufferDrawAttachColorTextureCube(
      this.state,
      this.log,
      framebuffer,
      texture,
      face);
  }

  @Override public void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepth> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.framebufferDrawAttachDepthRenderbuffer(
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
    LWJGL_GLES2Functions.framebufferDrawAttachDepthTexture2D(
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
    LWJGL_GLES2Functions.framebufferDrawAttachStencilRenderbuffer(
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
    LWJGL_GLES2Functions.framebufferDrawBind(framebuffer);
  }

  @Override public boolean framebufferDrawIsBound(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLException,
      ConstraintError
  {
    return LWJGL_GLES2Functions.framebufferDrawIsBound(
      this.state,
      framebuffer);
  }

  @Override public void framebufferDrawUnbind()
    throws JCGLException
  {
    LWJGL_GLES2Functions.framebufferDrawUnbind();
  }

  @Override public @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLException,
      ConstraintError
  {
    return LWJGL_GLES2Functions.framebufferDrawValidate(
      this.state,
      framebuffer);
  }

  @Override public @Nonnull
    FramebufferColorAttachmentPoint[]
    framebufferGetColorAttachmentPoints()
      throws JCGLException,
        ConstraintError
  {
    return this.state.color_attachments;
  }

  @Override public @Nonnull
    FramebufferDrawBuffer[]
    framebufferGetDrawBuffers()
      throws JCGLException,
        ConstraintError
  {
    return this.state.draw_buffers;
  }

  @Override public IndexBuffer indexBufferAllocate(
    final @Nonnull ArrayBufferUsable buffer,
    final int indices)
    throws JCGLException,
      ConstraintError
  {
    return LWJGL_GLES2Functions.indexBufferAllocate(

    this.state, this.log, buffer, indices);
  }

  @Override public @Nonnull IndexBuffer indexBufferAllocateType(
    final @Nonnull JCGLUnsignedType type,
    final int indices)
    throws JCGLException,
      ConstraintError
  {
    return LWJGL_GLES2Functions.indexBufferAllocateType(

    this.state, this.log, type, indices);
  }

  @Override public void indexBufferDelete(
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.indexBufferDelete(

    this.state, this.log, id);
  }

  @Override public void indexBufferUpdate(
    final @Nonnull IndexBuffer buffer,
    final @Nonnull IndexBufferWritableData data)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.indexBufferUpdate(buffer, data);
  }

  @Override public int metaGetError()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.metaGetError();
  }

  @Override public String metaGetRenderer()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.metaGetRenderer();
  }

  @Override public String metaGetVendor()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.metaGetVendor();
  }

  @Override public JCGLVersion metaGetVersion()
    throws JCGLException
  {
    return this.version;
  }

  @Override public void programActivate(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programActivate(program);
  }

  @Override public ProgramReference programCreate(
    final @Nonnull String name)
    throws ConstraintError,
      JCGLException
  {
    return LWJGL_GLES2Functions.programCreate(

    this.state, this.log, name);
  }

  @Override public void programDeactivate()
    throws JCGLException
  {
    LWJGL_GLES2Functions.programDeactivate();
  }

  @Override public void programDelete(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programDelete(

    this.state, this.log, program);
  }

  @Override public void programGetAttributes(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramAttribute> out)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programGetAttributes(

    this.state, this.log, program, out);
  }

  @Override public int programGetMaximumActiveAttributes()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.programGetMaximumActiveAttributes(

    this.state, this.log);
  }

  @Override public void programGetUniforms(
    final @Nonnull ProgramReference program,
    final @Nonnull Map<String, ProgramUniform> out)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programGetUniforms(

    this.state, this.log, program, out);
  }

  @Override public boolean programIsActive(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      JCGLException
  {
    return LWJGL_GLES2Functions.programIsActive(

    this.state, program);
  }

  @Override public void programLink(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException
  {
    LWJGL_GLES2Functions.programLink(

    this.state, this.log, program);
  }

  @Override public void programPutUniformFloat(
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programPutUniformFloat(

    this.state, uniform, value);
  }

  @Override public void programPutUniformMatrix3x3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programPutUniformMatrix3x3f(

    this.state, uniform, matrix);
  }

  @Override public void programPutUniformMatrix4x4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable4x4F matrix)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programPutUniformMatrix4x4f(

    this.state, uniform, matrix);
  }

  @Override public void programPutUniformTextureUnit(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programPutUniformTextureUnit(

    this.state, uniform, unit);
  }

  @Override public void programPutUniformVector2f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2F vector)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programPutUniformVector2f(

    this.state, uniform, vector);
  }

  @Override public void programPutUniformVector2i(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable2I vector)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programPutUniformVector2i(

    this.state, uniform, vector);
  }

  @Override public void programPutUniformVector3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable3F vector)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programPutUniformVector3f(

    this.state, uniform, vector);
  }

  @Override public void programPutUniformVector4f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull VectorReadable4F vector)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.programPutUniformVector4f(

    this.state, uniform, vector);
  }

  @Override public @Nonnull
    Renderbuffer<RenderableDepth>
    renderbufferAllocateDepth16(
      final int width,
      final int height)
      throws ConstraintError,
        JCGLException
  {
    return Renderbuffer.unsafeBrandDepth(LWJGL_GLES2Functions
      .renderbufferAllocate(
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_DEPTH_16,
        width,
        height));
  }

  @Override public Renderbuffer<RenderableColor> renderbufferAllocateRGB565(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLException
  {
    return Renderbuffer.unsafeBrandColor(LWJGL_GLES2Functions
      .renderbufferAllocate(
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
        JCGLException
  {
    return Renderbuffer.unsafeBrandColor(LWJGL_GLES2Functions
      .renderbufferAllocate(
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
    return Renderbuffer.unsafeBrandColor(LWJGL_GLES2Functions
      .renderbufferAllocate(
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
        JCGLException
  {
    return Renderbuffer.unsafeBrandStencil(LWJGL_GLES2Functions
      .renderbufferAllocate(
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
    LWJGL_GLES2Functions.renderbufferDelete(this.state, this.log, buffer);
  }

  @Override public void scissorDisable()
    throws JCGLException
  {
    LWJGL_GLES2Functions.scissorDisable();
  }

  @Override public void scissorEnable(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.scissorEnable(position, dimensions);
  }

  @Override public boolean scissorIsEnabled()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.scissorIsEnabled();
  }

  @Override public void stencilBufferClear(
    final int stencil)
    throws JCGLException,
      ConstraintError
  {
    LWJGL_GLES2Functions.stencilBufferClear(

    this.state, stencil);
  }

  @Override public void stencilBufferDisable()
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.stencilBufferDisable();
  }

  @Override public void stencilBufferEnable()
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.stencilBufferEnable(this.state);
  }

  @Override public void stencilBufferFunction(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.stencilBufferFunction(

    faces, function, reference, mask);
  }

  @Override public int stencilBufferGetBits()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.stencilBufferGetBits(this.state);
  }

  @Override public boolean stencilBufferIsEnabled()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.stencilBufferIsEnabled();
  }

  @Override public void stencilBufferMask(
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.stencilBufferMask(faces, mask);
  }

  @Override public void stencilBufferOperation(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.stencilBufferOperation(

    faces, stencil_fail, depth_fail, pass);
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
    return LWJGL_GLES2Functions.texture2DStaticAllocate(
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
      JCGLException
  {
    return LWJGL_GLES2Functions.texture2DStaticAllocate(
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
      JCGLException
  {
    return LWJGL_GLES2Functions.texture2DStaticAllocate(
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
    return LWJGL_GLES2Functions.texture2DStaticAllocate(
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
      JCGLException
  {
    return LWJGL_GLES2Functions.texture2DStaticAllocate(
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
      JCGLException
  {
    LWJGL_GLES2Functions.texture2DStaticBind(unit, texture);
  }

  @Override public void texture2DStaticDelete(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.texture2DStaticDelete(this.state, this.log, texture);
  }

  @Override public boolean texture2DStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLException
  {
    return LWJGL_GLES2Functions.texture2DStaticIsBound(
      this.state,
      unit,
      texture);
  }

  @Override public void texture2DStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.texture2DStaticUnbind(unit);
  }

  @Override public void texture2DStaticUpdate(
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.texture2DStaticUpdate(data);
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
    return LWJGL_GLES2Functions.textureCubeStaticAllocate(
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
        JCGLException
  {
    return LWJGL_GLES2Functions.textureCubeStaticAllocate(
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
        JCGLException
  {
    return LWJGL_GLES2Functions.textureCubeStaticAllocate(
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
    return LWJGL_GLES2Functions.textureCubeStaticAllocate(
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
        JCGLException
  {
    return LWJGL_GLES2Functions.textureCubeStaticAllocate(
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
      JCGLException
  {
    LWJGL_GLES2Functions.textureCubeStaticBind(unit, texture);
  }

  @Override public void textureCubeStaticDelete(
    final @Nonnull TextureCubeStatic texture)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.textureCubeStaticDelete(
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
    return LWJGL_GLES2Functions.textureCubeStaticIsBound(
      this.state,
      unit,
      texture);
  }

  @Override public void textureCubeStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.textureCubeStaticUnbind(unit);
  }

  @Override public void textureCubeStaticUpdate(
    final @Nonnull CubeMapFace face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.textureCubeStaticUpdate(face, data);
  }

  @Override public int textureGetMaximumSize()
    throws JCGLException
  {
    return LWJGL_GLES2Functions.textureGetMaximumSize(

    this.state);
  }

  @Override public TextureUnit[] textureGetUnits()
    throws JCGLException
  {
    return this.state.texture_units;
  }

  @Override public void vertexShaderAttach(
    final @Nonnull ProgramReference program,
    final @Nonnull VertexShader shader)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException
  {
    LWJGL_GLES2Functions.vertexShaderAttach(

    this.state, this.log, program, shader);
  }

  @Override public VertexShader vertexShaderCompile(
    final @Nonnull String name,
    final @Nonnull List<String> lines)
    throws ConstraintError,
      JCGLCompileException,
      JCGLException
  {
    return LWJGL_GLES2Functions.vertexShaderCompile(
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
    LWJGL_GLES2Functions.vertexShaderDelete(

    this.state, this.log, id);
  }

  @Override public void viewportSet(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I dimensions)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GLES2Functions.viewportSet(position, dimensions);
  }
}
