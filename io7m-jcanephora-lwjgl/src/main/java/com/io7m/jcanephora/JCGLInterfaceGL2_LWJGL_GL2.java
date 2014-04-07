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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
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

/**
 * An implementation of the GL2 interface, running on an OpenGL 2.1
 * implementation, using LWJGL as the bindings.
 */

@NotThreadSafe public final class JCGLInterfaceGL2_LWJGL_GL2 implements
  JCGLInterfaceGL2
{
  private final @Nonnull Option<JCGLExtensionDepthTexture> ext_depth_texture;
  private final @Nonnull JCGLNamedExtensions               extensions;
  private final @Nonnull Log                               log;
  private final @Nonnull JCGLSoftRestrictions              restrictions;
  private final @Nonnull JCGLSLVersion                     sl_version;
  private final @Nonnull JCGLStateCache                    state;
  private final @Nonnull JCGLVersion                       version;

  JCGLInterfaceGL2_LWJGL_GL2(
    final @Nonnull Log log1,
    final @Nonnull JCGLSoftRestrictions r,
    final @Nonnull Set<String> extension_set)
    throws ConstraintError,
      JCGLRuntimeException
  {
    this.log =
      new Log(Constraints.constrainNotNull(log1, "log output"), "lwjgl-30");
    this.state = new JCGLStateCache();

    this.restrictions = r;

    this.extensions = new JCGLNamedExtensions() {
      private final StringBuilder message = new StringBuilder();

      @Override public boolean extensionIsSupported(
        final @Nonnull String name)
        throws ConstraintError
      {
        Constraints.constrainNotNull(name, "Name");
        return extension_set.contains(name);
      }

      @SuppressWarnings("synthetic-access") @Override public
        boolean
        extensionIsVisible(
          final @Nonnull String name)
          throws ConstraintError
      {
        final boolean supported = this.extensionIsSupported(name);

        if (supported) {
          if (log1.enabled(Level.LOG_DEBUG)) {
            this.message.setLength(0);
            this.message.append("Extension ");
            this.message.append(name);
            this.message.append(" is supported");
            log1.debug(this.message.toString());
          }

          final boolean visible =
            JCGLInterfaceGL2_LWJGL_GL2.this.restrictions
              .restrictExtensionVisibility(name);

          if (!visible) {
            if (log1.enabled(Level.LOG_DEBUG)) {
              this.message.setLength(0);
              this.message.append("Extension ");
              this.message.append(name);
              this.message.append(" is hidden by soft restrictions");
              log1.debug(this.message.toString());
            }
          }

          return visible;
        }

        if (log1.enabled(Level.LOG_DEBUG)) {
          this.message.setLength(0);
          this.message.append("Extension ");
          this.message.append(name);
          this.message.append(" is not supported");
          log1.debug(this.message.toString());
        }

        return supported;
      }
    };

    /**
     * Extensions.
     */

    this.ext_depth_texture =
      ExtDepthTexture.create(this.state, this.extensions, log1);

    /**
     * Initialize texture unit cache.
     */

    this.state.texture_units =
      LWJGL_GLES2Functions.textureGetUnitsActual(this.state, this.log, r);

    /**
     * Initialize color attachment point cache.
     */

    this.state.color_attachments =
      LWJGL_GLES2Functions.framebufferGetAttachmentPointsActual(
        this.state,
        this.log);

    /**
     * Get maximum draw buffers.
     */

    this.state.draw_buffers =
      LWJGL_GLES2Functions.framebufferGetDrawBuffersActual(
        this.state,
        this.log);

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

    this.version = LWJGL_GLES2Functions.metaGetVersion();
    this.sl_version = LWJGL_GLES2Functions.metaGetSLVersion(log1);
  }

  @Override public ArrayBuffer arrayBufferAllocate(
    final long elements,
    final @Nonnull ArrayBufferTypeDescriptor descriptor,
    final @Nonnull UsageHint usage)
    throws JCGLRuntimeException,
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
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GLES2Functions.arrayBufferBind(buffer);
  }

  @Override public void arrayBufferDelete(
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.arrayBufferDelete(this.log, this.state, id);
  }

  @Override public boolean arrayBufferIsBound(
    final @Nonnull ArrayBufferUsable id)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.arrayBufferIsBound(id);
  }

  @Override public void arrayBufferUnbind()
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GLES2Functions.arrayBufferUnbind();
  }

  @Override public void arrayBufferUpdate(
    final @Nonnull ArrayBufferWritableData data)
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GLES2Functions.arrayBufferUpdate(data);
  }

  @Override public void blendingDisable()
    throws JCGLRuntimeException
  {
    LWJGL_GLES2Functions.blendingDisable();
  }

  @Override public void blendingEnable(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor)
    throws ConstraintError,
      JCGLRuntimeException
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
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.blendingEnableSeparate(

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
    LWJGL_GL3Functions.blendingEnableSeparateWithEquationSeparate(

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
    LWJGL_GLES2Functions.blendingEnableSeparateWithEquationSeparateES2(

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
    LWJGL_GL3Functions.blendingEnableWithEquation(

    source_factor, destination_factor, equation);
  }

  @Override public void blendingEnableWithEquationES2(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.blendingEnableWithEquationES2(

    source_factor, destination_factor, equation);
  }

  @Override public void blendingEnableWithEquationSeparate(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGL3 equation_rgb,
    final @Nonnull BlendEquationGL3 equation_alpha)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GL3Functions.blendingEnableWithEquationSeparate(

    source_factor, destination_factor, equation_rgb, equation_alpha);
  }

  @Override public void blendingEnableWithEquationSeparateES2(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGLES2 equation_rgb,
    final @Nonnull BlendEquationGLES2 equation_alpha)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.blendingEnableWithEquationSeparateES2(

    source_factor, destination_factor, equation_rgb, equation_alpha);
  }

  @Override public boolean blendingIsEnabled()
    throws ConstraintError,
      JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.blendingIsEnabled();
  }

  @Override public void colorBufferClear3f(
    final float r,
    final float g,
    final float b)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.colorBufferClear3f(r, g, b);
  }

  @Override public void colorBufferClear4f(
    final float r,
    final float g,
    final float b,
    final float a)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.colorBufferClear4f(r, g, b, a);
  }

  @Override public void colorBufferClearV3f(
    final @Nonnull VectorReadable3F color)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.colorBufferClearV3f(color);
  }

  @Override public void colorBufferClearV4f(
    final @Nonnull VectorReadable4F color)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.colorBufferClearV4f(color);
  }

  @Override public void colorBufferMask(
    final boolean r,
    final boolean g,
    final boolean b,
    final boolean a)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.colorBufferMask(r, g, b, a);
  }

  @Override public boolean colorBufferMaskStatusAlpha()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.colorBufferMaskStatusAlpha(

    this.state);
  }

  @Override public boolean colorBufferMaskStatusBlue()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.colorBufferMaskStatusBlue(

    this.state);
  }

  @Override public boolean colorBufferMaskStatusGreen()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.colorBufferMaskStatusGreen(

    this.state);
  }

  @Override public boolean colorBufferMaskStatusRed()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.colorBufferMaskStatusRed(

    this.state);
  }

  @Override public void cullingDisable()
    throws JCGLRuntimeException
  {
    LWJGL_GLES2Functions.cullingDisable();
  }

  @Override public void cullingEnable(
    final @Nonnull FaceSelection faces,
    final @Nonnull FaceWindingOrder order)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.cullingEnable(faces, order);
  }

  @Override public boolean cullingIsEnabled()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.cullingIsEnabled();
  }

  @Override public void depthBufferClear(
    final float depth)
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GLES2Functions.depthBufferClear(this.state, depth);
  }

  @Override public int depthBufferGetBits()
    throws JCGLRuntimeException
  {
    return LWJGL_GL3Functions.depthBufferGetBits(this.state);
  }

  @Override public void depthBufferTestDisable()
    throws JCGLRuntimeException
  {
    LWJGL_GLES2Functions.depthBufferDisable();
  }

  @Override public void depthBufferTestEnable(
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.depthBufferEnable(

    this.state, function);
  }

  @Override public boolean depthBufferTestIsEnabled()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.depthBufferIsEnabled();
  }

  @Override public void depthBufferWriteDisable()
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.depthBufferWriteDisable(this.state);
  }

  @Override public void depthBufferWriteEnable()
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.depthBufferWriteEnable(this.state);
  }

  @Override public boolean depthBufferWriteIsEnabled()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.depthBufferWriteIsEnabled(

    this.state);
  }

  @Override public void drawElements(
    final @Nonnull Primitives mode,
    final @Nonnull IndexBufferUsable indices)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.drawElements(mode, indices);
  }

  @Override public boolean errorCodeIsInvalidOperation(
    final int code)
  {
    return code == GL11.GL_INVALID_OPERATION;
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
    return LWJGL_GLES2Functions.fragmentShaderCompile(
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
    LWJGL_GLES2Functions.fragmentShaderDelete(this.state, this.log, id);
  }

  @Override public @Nonnull FramebufferReference framebufferAllocate()
    throws JCGLRuntimeException,
      ConstraintError
  {
    return LWJGL_GLES2Functions.framebufferAllocate(this.state, this.log);
  }

  @Override public void framebufferBlit(
    final AreaInclusive source,
    final AreaInclusive target,
    final Set<FramebufferBlitBuffer> buffers,
    final FramebufferBlitFilter filter)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GL3Functions.framebufferBlit(source, target, buffers, filter);
  }

  @Override public void framebufferDelete(
    final @Nonnull FramebufferReference framebuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GL3Functions.framebufferDelete(this.state, this.log, framebuffer);
  }

  @Override public boolean framebufferDrawAnyIsBound()
    throws JCGLRuntimeException
  {
    return LWJGL_GL3Functions.framebufferDrawAnyIsBound();
  }

  @Override public void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GL3Functions.framebufferDrawAttachColorRenderbuffer(
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
    LWJGL_GL3Functions.framebufferDrawAttachColorRenderbufferAt(
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
    LWJGL_GL3Functions.framebufferDrawAttachColorTexture2D(
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
    LWJGL_GL3Functions.framebufferDrawAttachColorTexture2DAt(
      this.state,
      this.version,
      this.log,
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
    LWJGL_GL3Functions.framebufferDrawAttachColorTextureCube(
      this.state,
      this.version,
      this.log,
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
    LWJGL_GL3Functions.framebufferDrawAttachColorTextureCubeAt(
      this.state,
      this.version,
      this.log,
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
    LWJGL_GL3Functions.framebufferDrawAttachDepthRenderbuffer(
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
    LWJGL_GL3Functions.framebufferDrawAttachDepthStencilRenderbuffer(
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
    LWJGL_GL3Functions.framebufferDrawAttachDepthTexture2D(
      this.state,
      this.version,
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
    LWJGL_GL3Functions.framebufferDrawAttachStencilRenderbuffer(
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
    LWJGL_GL3Functions.framebufferDrawBind(framebuffer);
  }

  @Override public boolean framebufferDrawIsBound(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return LWJGL_GL3Functions.framebufferDrawIsBound(this.state, framebuffer);
  }

  @Override public boolean framebufferReadIsBound(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return LWJGL_GL3Functions.framebufferReadIsBound(this.state, framebuffer);
  }

  @Override public
    void
    framebufferDrawSetBuffers(
      @Nonnull final FramebufferReference framebuffer,
      @Nonnull final Map<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> mappings)
      throws JCGLRuntimeException,
        ConstraintError
  {
    LWJGL_GL3Functions.framebufferDrawSetBuffers(
      this.state,
      this.log,
      framebuffer,
      mappings);
  }

  @Override public void framebufferDrawUnbind()
    throws JCGLRuntimeException
  {
    LWJGL_GL3Functions.framebufferDrawUnbind();
  }

  @Override public @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull FramebufferReferenceUsable framebuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return LWJGL_GL3Functions
      .framebufferDrawValidate(this.state, framebuffer);
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
    return LWJGL_GL3Functions.framebufferReadAnyIsBound();
  }

  @Override public void framebufferReadBind(
    final FramebufferReferenceUsable framebuffer)
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GL3Functions.framebufferReadBind(framebuffer);
  }

  @Override public void framebufferReadUnbind()
    throws JCGLRuntimeException
  {
    LWJGL_GL3Functions.framebufferReadUnbind();
  }

  @Override public IndexBuffer indexBufferAllocate(
    final @Nonnull ArrayBufferUsable buffer,
    final int indices)
    throws JCGLRuntimeException,
      ConstraintError
  {
    return LWJGL_GLES2Functions.indexBufferAllocate(
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
    return LWJGL_GLES2Functions.indexBufferAllocateType(
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
    LWJGL_GLES2Functions.indexBufferDelete(this.state, this.log, id);
  }

  @Override public void indexBufferUpdate(
    final @Nonnull IndexBufferWritableData data)
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GLES2Functions.indexBufferUpdate(data);
  }

  @Override public void logicOperationsDisable()
    throws JCGLRuntimeException
  {
    LWJGL_GL3Functions.logicOperationsDisable();
  }

  @Override public void logicOperationsEnable(
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GL3Functions.logicOperationsEnable(operation);
  }

  @Override public boolean logicOperationsEnabled()
    throws JCGLRuntimeException
  {
    return LWJGL_GL3Functions.logicOperationsEnabled();
  }

  @Override public int metaGetError()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.metaGetError();
  }

  @Override public String metaGetRenderer()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.metaGetRenderer();
  }

  @Override public @Nonnull JCGLSLVersion metaGetSLVersion()
    throws JCGLRuntimeException
  {
    return this.sl_version;
  }

  @Override public String metaGetVendor()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.metaGetVendor();
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
    LWJGL_GL3Functions.polygonSetMode(this.state, mode);
  }

  @Override public final void polygonSmoothingDisable()
    throws JCGLRuntimeException
  {
    LWJGL_GL3Functions.polygonSmoothingDisable();
  }

  @Override public final void polygonSmoothingEnable()
    throws JCGLRuntimeException
  {
    LWJGL_GL3Functions.polygonSmoothingEnable();
  }

  @Override public final boolean polygonSmoothingIsEnabled()
    throws JCGLRuntimeException
  {
    return LWJGL_GL3Functions.polygonSmoothingIsEnabled();
  }

  @Override public void programActivate(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.programActivate(program);
  }

  @Override public void programAttributeArrayAssociate(
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull ArrayBufferAttribute array_attribute)
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GLES2Functions.programAttributeArrayAssociate(
      this.state,
      array_attribute,
      program_attribute);
  }

  @Override public void programAttributeArrayDisassociate(
    final @Nonnull ProgramAttribute program_attribute)
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GLES2Functions.programAttributeArrayDisassociate(
      this.state,
      program_attribute);
  }

  @Override public void programAttributePutFloat(
    final @Nonnull ProgramAttribute program_attribute,
    final float x)
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GLES2Functions.programAttributePutFloat(
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
    LWJGL_GLES2Functions.programAttributePutVector2f(
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
    LWJGL_GLES2Functions.programAttributePutVector3f(
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
    LWJGL_GLES2Functions.programAttributePutVector4f(
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
    return LWJGL_GLES2Functions.programCreateCommon(
      this.state,
      this.log,
      name,
      v,
      f);
  }

  @Override public void programDeactivate()
    throws JCGLRuntimeException
  {
    LWJGL_GLES2Functions.programDeactivate();
  }

  @Override public void programDelete(
    final @Nonnull ProgramReference program)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.programDelete(this.state, this.log, program);
  }

  @Override public int programGetMaximumActiveAttributes()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.programGetMaximumActiveAttributes(
      this.state,
      this.log);
  }

  @Override public boolean programIsActive(
    final @Nonnull ProgramReferenceUsable program)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.programIsActive(this.state, program);
  }

  @Override public void programUniformPutFloat(
    final @Nonnull ProgramUniform uniform,
    final float value)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.programPutUniformFloat(this.state, uniform, value);
  }

  @Override public void programUniformPutInteger(
    final @Nonnull ProgramUniform uniform,
    final int value)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.programPutUniformInteger(this.state, uniform, value);
  }

  @Override public void programUniformPutMatrix3x3f(
    final @Nonnull ProgramUniform uniform,
    final @Nonnull MatrixReadable3x3F matrix)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.programPutUniformMatrix3x3f(
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
    LWJGL_GLES2Functions.programPutUniformMatrix4x4f(
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
    LWJGL_GLES2Functions.programPutUniformTextureUnit(
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
    LWJGL_GLES2Functions.programPutUniformVector2f(
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
    LWJGL_GLES2Functions.programPutUniformVector2i(
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
    LWJGL_GLES2Functions.programPutUniformVector3f(
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
    LWJGL_GLES2Functions.programPutUniformVector3i(
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
    LWJGL_GLES2Functions.programPutUniformVector4f(
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
    LWJGL_GLES2Functions.programPutUniformVector4i(
      this.state,
      uniform,
      vector);
  }

  @Override public Renderbuffer<RenderableDepth> renderbufferAllocateDepth16(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return Renderbuffer.unsafeBrandDepth(LWJGL_GLES2Functions
      .renderbufferAllocate(
        this.state,
        this.log,
        RenderbufferType.RENDERBUFFER_DEPTH_16,
        width,
        height));
  }

  @Override public Renderbuffer<RenderableDepth> renderbufferAllocateDepth24(
    final int width,
    final int height)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return Renderbuffer.unsafeBrandDepth(LWJGL_GLES2Functions
      .renderbufferAllocate(
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
    return Renderbuffer.unsafeBrandDepthStencil(LWJGL_GLES2Functions
      .renderbufferAllocate(
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
    return Renderbuffer.unsafeBrandColor(LWJGL_GLES2Functions
      .renderbufferAllocate(
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
    return Renderbuffer.unsafeBrandColor(LWJGL_GLES2Functions
      .renderbufferAllocate(
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
    LWJGL_GLES2Functions.renderbufferDelete(this.state, this.log, buffer);
  }

  @Override public void scissorDisable()
    throws JCGLRuntimeException
  {
    LWJGL_GLES2Functions.scissorDisable();
  }

  @Override public void scissorEnable(
    final @Nonnull AreaInclusive area)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.scissorEnable(area);
  }

  @Override public boolean scissorIsEnabled()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.scissorIsEnabled();
  }

  @Override public void stencilBufferClear(
    final int stencil)
    throws JCGLRuntimeException,
      ConstraintError
  {
    LWJGL_GLES2Functions.stencilBufferClear(

    this.state, stencil);
  }

  @Override public void stencilBufferDisable()
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.stencilBufferDisable();
  }

  @Override public void stencilBufferEnable()
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.stencilBufferEnable(this.state);
  }

  @Override public void stencilBufferFunction(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.stencilBufferFunction(

    faces, function, reference, mask);
  }

  @Override public int stencilBufferGetBits()
    throws JCGLRuntimeException
  {
    return LWJGL_GL3Functions.stencilBufferGetBits(this.state);
  }

  @Override public boolean stencilBufferIsEnabled()
    throws JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.stencilBufferIsEnabled();
  }

  @Override public void stencilBufferMask(
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.stencilBufferMask(faces, mask);
  }

  @Override public void stencilBufferOperation(
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.stencilBufferOperation(

    faces, stencil_fail, depth_fail, pass);
  }

  @Override public @Nonnull
    Texture2DStatic
    texture2DStaticAllocateDepth24Stencil8(
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
    return LWJGL_GLES2Functions.texture2DStaticAllocate(
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
    return LWJGL_GLES2Functions.texture2DStaticAllocate(
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
    return LWJGL_GLES2Functions.texture2DStaticAllocate(
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
    LWJGL_GLES2Functions.texture2DStaticBind(unit, texture);
  }

  @Override public void texture2DStaticDelete(
    final @Nonnull Texture2DStatic texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.texture2DStaticDelete(

    this.state, this.log, texture);
  }

  @Override public Texture2DReadableData texture2DStaticGetImage(
    final Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return LWJGL_GL3Functions.texture2DStaticGetImage(texture);
  }

  @Override public boolean texture2DStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.texture2DStaticIsBound(

    this.state, unit, texture);
  }

  @Override public void texture2DStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.texture2DStaticUnbind(unit);
  }

  @Override public void texture2DStaticUpdate(
    final @Nonnull Texture2DWritableData data)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.texture2DStaticUpdate(data);
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
    return LWJGL_GLES2Functions.textureCubeStaticAllocate(
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
    return LWJGL_GLES2Functions.textureCubeStaticAllocate(
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
    return LWJGL_GLES2Functions.textureCubeStaticAllocate(
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
    LWJGL_GLES2Functions.textureCubeStaticBind(unit, texture);
  }

  @Override public void textureCubeStaticDelete(
    final @Nonnull TextureCubeStatic texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.textureCubeStaticDelete(
      this.state,
      this.log,
      texture);
  }

  @Override public TextureCubeReadableData textureCubeStaticGetImageLH(
    final TextureCubeStaticUsable texture,
    final CubeMapFaceLH face)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return LWJGL_GL3Functions.textureCubeStaticGetImageLH(texture, face);
  }

  @Override public TextureCubeReadableData textureCubeStaticGetImageRH(
    final TextureCubeStaticUsable texture,
    final CubeMapFaceRH face)
    throws ConstraintError,
      JCGLRuntimeException
  {
    Constraints.constrainNotNull(face, "Face");
    return LWJGL_GL3Functions.textureCubeStaticGetImageLH(
      texture,
      CubeMapFaceLH.fromRH(face));
  }

  @Override public boolean textureCubeStaticIsBound(
    final @Nonnull TextureUnit unit,
    final @Nonnull TextureCubeStaticUsable texture)
    throws ConstraintError,
      JCGLRuntimeException
  {
    return LWJGL_GLES2Functions.textureCubeStaticIsBound(
      this.state,
      unit,
      texture);
  }

  @Override public void textureCubeStaticUnbind(
    final @Nonnull TextureUnit unit)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.textureCubeStaticUnbind(unit);
  }

  @Override public void textureCubeStaticUpdateLH(
    final @Nonnull CubeMapFaceLH face,
    final @Nonnull TextureCubeWritableData data)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.textureCubeStaticUpdate(face, data);
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
    return LWJGL_GLES2Functions.textureGetMaximumSize(this.state);
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
    return LWJGL_GLES2Functions.vertexShaderCompile(
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
    LWJGL_GLES2Functions.vertexShaderDelete(this.state, this.log, id);
  }

  @Override public void viewportSet(
    final @Nonnull AreaInclusive area)
    throws ConstraintError,
      JCGLRuntimeException
  {
    LWJGL_GLES2Functions.viewportSet(area);
  }
}
