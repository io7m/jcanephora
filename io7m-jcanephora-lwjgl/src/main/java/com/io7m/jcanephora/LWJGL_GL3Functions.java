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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;

import javax.annotation.Nonnull;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;

final class LWJGL_GL3Functions
{
  static ByteBuffer arrayBufferMapRead(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBufferUsable id)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id.getGLName());
    LWJGL_GLES2Functions.checkError();
    final ByteBuffer b =
      GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_READ_ONLY, null);
    LWJGL_GLES2Functions.checkError();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    LWJGL_GLES2Functions.checkError();

    return b;
  }

  static ByteBuffer arrayBufferMapReadRange(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBufferUsable id,
    final @Nonnull RangeInclusive range)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    Constraints.constrainNotNull(range, "Range");
    Constraints.constrainArbitrary(
      range.isIncludedIn(id.getRange()),
      "Mapped range included in buffer range");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id.getGLName());
    LWJGL_GLES2Functions.checkError();

    final long offset = range.getLower() * id.getElementSizeBytes();
    final long length = range.getInterval() * id.getElementSizeBytes();

    final ByteBuffer b =
      GL30.glMapBufferRange(
        GL15.GL_ARRAY_BUFFER,
        offset,
        length,
        GL30.GL_MAP_READ_BIT,
        null);
    LWJGL_GLES2Functions.checkError();

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    LWJGL_GLES2Functions.checkError();

    return b;
  }

  static ArrayBufferWritableMap arrayBufferMapWrite(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer id)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id.getGLName());
    LWJGL_GLES2Functions.checkError();
    GL15.glBufferData(
      GL15.GL_ARRAY_BUFFER,
      id.getSizeBytes(),
      GL15.GL_STREAM_DRAW);
    LWJGL_GLES2Functions.checkError();

    final ByteBuffer b =
      GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_WRITE_ONLY, null);
    LWJGL_GLES2Functions.checkError();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    LWJGL_GLES2Functions.checkError();

    return new ArrayBufferWritableMap(id, b);
  }

  static void arrayBufferUnmap(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBufferUsable id)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(id, "Array buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Array buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: unmap ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id.getGLName());
    GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    LWJGL_GLES2Functions.checkError();
  }

  static void blendingEnableSeparateWithEquationSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquationGL3 equation_rgb,
    final @Nonnull BlendEquationGL3 equation_alpha)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(source_rgb_factor, "Source RGB factor");
    Constraints.constrainNotNull(source_alpha_factor, "Source alpha factor");
    Constraints.constrainNotNull(
      destination_rgb_factor,
      "Destination RGB factor");
    Constraints.constrainNotNull(
      destination_alpha_factor,
      "Destination alpha factor");
    Constraints.constrainNotNull(equation_rgb, "Equation RGB");
    Constraints.constrainNotNull(equation_alpha, "Equation alpha");

    Constraints.constrainArbitrary(
      destination_rgb_factor != BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      "Destination RGB factor not SOURCE_ALPHA_SATURATE");
    Constraints.constrainArbitrary(
      destination_alpha_factor != BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      "Destination alpha factor not SOURCE_ALPHA_SATURATE");

    GL11.glEnable(GL11.GL_BLEND);
    GL20.glBlendEquationSeparate(
      LWJGL_GLTypeConversions.blendEquationToGL(equation_rgb),
      LWJGL_GLTypeConversions.blendEquationToGL(equation_alpha));
    GL14.glBlendFuncSeparate(
      LWJGL_GLTypeConversions.blendFunctionToGL(source_rgb_factor),
      LWJGL_GLTypeConversions.blendFunctionToGL(destination_rgb_factor),
      LWJGL_GLTypeConversions.blendFunctionToGL(source_alpha_factor),
      LWJGL_GLTypeConversions.blendFunctionToGL(destination_alpha_factor));
    LWJGL_GLES2Functions.checkError();
  }

  static void blendingEnableWithEquation(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGL3 equation)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GL3Functions.blendingEnableSeparateWithEquationSeparate(

      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  static void blendingEnableWithEquationSeparate(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquationGL3 equation_rgb,
    final @Nonnull BlendEquationGL3 equation_alpha)
    throws ConstraintError,
      JCGLException
  {
    LWJGL_GL3Functions.blendingEnableSeparateWithEquationSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  static int depthBufferGetBits(
    final @Nonnull JCGLStateCache state)
    throws JCGLException
  {
    /**
     * If a framebuffer is bound, check to see if there's a depth attachment.
     */

    if (LWJGL_GL3Functions.framebufferDrawAnyIsBound()) {

      {
        final int p =
          GL30.glGetFramebufferAttachmentParameteri(
            GL30.GL_DRAW_FRAMEBUFFER,
            GL30.GL_DEPTH_ATTACHMENT,
            GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE);
        LWJGL_GLES2Functions.checkError();
        if (p == GL11.GL_NONE) {
          return 0;
        }
      }

      /**
       * If there's a depth attachment, check the size of it.
       */

      final int p =
        GL30.glGetFramebufferAttachmentParameteri(
          GL30.GL_DRAW_FRAMEBUFFER,
          GL30.GL_DEPTH_ATTACHMENT,
          GL30.GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE);
      LWJGL_GLES2Functions.checkError();
      return p;
    }

    /**
     * If no framebuffer is bound, use the default glGet query.
     */

    final int bits =
      LWJGL_GLES2Functions.contextGetInteger(state, GL11.GL_DEPTH_BITS);
    LWJGL_GLES2Functions.checkError();
    return bits;
  }

  static void framebufferDelete(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference buffer)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer: delete ");
      state.log_text.append(buffer);
      log.debug(state.log_text.toString());
    }

    GL30.glDeleteFramebuffers(buffer.getGLName());
    LWJGL_GLES2Functions.checkError();
    buffer.resourceSetDeleted();
  }

  static boolean framebufferDrawAnyIsBound()
  {
    final int bound = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
    return bound != 0;
  }

  static void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isColorRenderable(),
      "Renderbuffer is color renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at color attachment 0");
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferRenderbuffer(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_COLOR_ATTACHMENT0,
      GL30.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorRenderbufferAt(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull RenderbufferUsable<RenderableColor> renderbuffer)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isColorRenderable(),
      "Renderbuffer is color renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at color attachment ");
      state.log_text.append(point);
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferRenderbuffer(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_COLOR_ATTACHMENT0 + point.getIndex(),
      GL30.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorTexture2D(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isColorRenderable(),
      "Texture is color renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" at color attachment 0");
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferTexture2D(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_COLOR_ATTACHMENT0,
      GL11.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorTexture2DAt(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isColorRenderable(),
      "Texture is color renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" at color attachment ");
      state.log_text.append(point);
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferTexture2D(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_COLOR_ATTACHMENT0 + point.getIndex(),
      GL11.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorTextureCube(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFace face)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isColorRenderable(),
      "Texture is color renderable");

    Constraints.constrainNotNull(face, "Cube map face");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" at color attachment 0");
      log.debug(state.log_text.toString());
    }

    final int gface = LWJGL_GLTypeConversions.cubeFaceToGL(face);
    GL30.glFramebufferTexture2D(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_COLOR_ATTACHMENT0,
      gface,
      texture.getGLName(),
      0);
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorTextureCubeAt(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFace face)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isColorRenderable(),
      "Texture is color renderable");

    Constraints.constrainNotNull(face, "Cube map face");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" face ");
      state.log_text.append(face);
      state.log_text.append(" at color attachment ");
      state.log_text.append(point);
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferTexture2D(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_COLOR_ATTACHMENT0 + point.getIndex(),
      LWJGL_GLTypeConversions.cubeFaceToGL(face),
      texture.getGLName(),
      0);
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepth> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isDepthRenderable(),
      "Renderbuffer is depth renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at depth attachment");
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferRenderbuffer(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_DEPTH_ATTACHMENT,
      GL30.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableDepthStencil> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");

    final RenderbufferType type = renderbuffer.getType();
    Constraints.constrainArbitrary(
      type.isDepthRenderable(),
      "Renderbuffer is depth renderable");
    Constraints.constrainArbitrary(
      type.isStencilRenderable(),
      "Renderbuffer is stencil renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at depth+stencil attachment");
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferRenderbuffer(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_DEPTH_ATTACHMENT,
      GL30.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    GL30.glFramebufferRenderbuffer(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_STENCIL_ATTACHMENT,
      GL30.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachDepthTexture2D(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(texture, "Texture");
    Constraints.constrainArbitrary(
      texture.resourceIsDeleted() == false,
      "Texture not deleted");
    Constraints.constrainArbitrary(
      texture.getType().isDepthRenderable(),
      "Texture is depth renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(texture);
      state.log_text.append(" at depth attachment");
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferTexture2D(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_DEPTH_ATTACHMENT,
      GL11.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable<RenderableStencil> renderbuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(renderbuffer
      .getType()
      .isStencilRenderable(), "Renderbuffer is stencil renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at stencil attachment");
      log.debug(state.log_text.toString());
    }

    GL30.glFramebufferRenderbuffer(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_STENCIL_ATTACHMENT,
      GL30.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawBind(
    final @Nonnull FramebufferReference buffer)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, buffer.getGLName());
    LWJGL_GLES2Functions.checkError();
  }

  static boolean framebufferDrawIsBound(
    final @Nonnull JCGLStateCache state,
    final @Nonnull FramebufferReference framebuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    final IntBuffer cache = state.getIntegerCache();
    GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING, cache);
    LWJGL_GLES2Functions.checkError();
    return cache.get(0) == framebuffer.getGLName();
  }

  static
    void
    framebufferDrawSetBuffers(
      final @Nonnull JCGLStateCache state,
      final @Nonnull Log log,
      final @Nonnull FramebufferReference framebuffer,
      final @Nonnull Map<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> mappings)
      throws ConstraintError,
        JCGLException
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");
    Constraints.constrainNotNull(mappings, "Draw buffer attachment mappings");

    final FramebufferDrawBuffer[] buffers = state.draw_buffers;
    final IntBuffer out = BufferUtils.createIntBuffer(buffers.length);

    for (int index = 0; index < buffers.length; ++index) {
      final FramebufferDrawBuffer buffer = new FramebufferDrawBuffer(index);
      if (mappings.containsKey(buffer)) {
        final FramebufferColorAttachmentPoint attach = mappings.get(buffer);
        out.put(index, GL30.GL_COLOR_ATTACHMENT0 + attach.getIndex());
        if (log.enabled(Level.LOG_DEBUG)) {
          state.log_text.setLength(0);
          state.log_text.append("draw-buffers: map ");
          state.log_text.append(buffer);
          state.log_text.append(" to ");
          state.log_text.append(attach);
          log.debug(state.log_text.toString());
        }
      } else {
        out.put(index, GL11.GL_NONE);
        if (log.enabled(Level.LOG_DEBUG)) {
          state.log_text.setLength(0);
          state.log_text.append("draw-buffers: map ");
          state.log_text.append(buffer);
          state.log_text.append(" to none");
          log.debug(state.log_text.toString());
        }
      }
    }

    out.rewind();

    GL20.glDrawBuffers(out);
    LWJGL_GLES2Functions.checkError();
  }

  static void framebufferDrawUnbind()
    throws JCGLException
  {
    GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
    LWJGL_GLES2Functions.checkError();
  }

  static @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull JCGLStateCache state,
    final @Nonnull FramebufferReference framebuffer)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      LWJGL_GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    final int status =
      GL30.glCheckFramebufferStatus(GL30.GL_DRAW_FRAMEBUFFER);
    LWJGL_GLES2Functions.checkError();

    return LWJGL_GLTypeConversions.framebufferStatusFromGL(status);
  }

  static IndexBufferReadableMap indexBufferMapRead(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBufferUsable id)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    LWJGL_GLES2Functions.checkError();

    final ByteBuffer b =
      GL15.glMapBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GL15.GL_READ_ONLY, null);
    LWJGL_GLES2Functions.checkError();

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    LWJGL_GLES2Functions.checkError();

    return new IndexBufferReadableMap(id, b);
  }

  static IndexBufferReadableMap indexBufferMapReadRange(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBufferUsable id,
    final @Nonnull RangeInclusive range)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    Constraints.constrainNotNull(range, "Range");
    Constraints.constrainArbitrary(
      range.isIncludedIn(id.getRange()),
      "Mapped range included in buffer range");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    LWJGL_GLES2Functions.checkError();

    final long offset = range.getLower() * id.getElementSizeBytes();
    final long length = range.getInterval() * id.getElementSizeBytes();

    final ByteBuffer b =
      GL30.glMapBufferRange(
        GL15.GL_ELEMENT_ARRAY_BUFFER,
        offset,
        length,
        GL30.GL_MAP_READ_BIT,
        null);
    LWJGL_GLES2Functions.checkError();

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    LWJGL_GLES2Functions.checkError();

    return new IndexBufferReadableMap(id, b);
  }

  static IndexBufferWritableMap indexBufferMapWrite(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: map ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    LWJGL_GLES2Functions.checkError();
    GL15.glBufferData(
      GL15.GL_ELEMENT_ARRAY_BUFFER,
      id.getSizeBytes(),
      GL15.GL_STREAM_DRAW);
    LWJGL_GLES2Functions.checkError();

    final ByteBuffer b =
      GL15
        .glMapBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GL15.GL_WRITE_ONLY, null);
    LWJGL_GLES2Functions.checkError();
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    LWJGL_GLES2Functions.checkError();

    return new IndexBufferWritableMap(id, b);
  }

  static void indexBufferUnmap(
    final @Nonnull JCGLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBufferUsable id)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(id, "Index buffer");
    Constraints.constrainArbitrary(
      id.resourceIsDeleted() == false,
      "Index buffer not deleted");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("index-buffer: unmap ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    GL15.glUnmapBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER);
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    LWJGL_GLES2Functions.checkError();
  }

  static void logicOperationsDisable()
    throws JCGLException
  {
    GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
    LWJGL_GLES2Functions.checkError();
  }

  static void logicOperationsEnable(
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(operation, "Logic operation");
    GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
    GL11.glLogicOp(LWJGL_GLTypeConversions.logicOpToGL(operation));
    LWJGL_GLES2Functions.checkError();
  }

  static boolean logicOperationsEnabled()
    throws JCGLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_COLOR_LOGIC_OP);
    LWJGL_GLES2Functions.checkError();
    return e;
  }

  static void pointProgramSizeControlDisable()
    throws JCGLException
  {
    GL11.glDisable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
    LWJGL_GLES2Functions.checkError();
  }

  static void pointProgramSizeControlEnable()
    throws JCGLException
  {
    GL11.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
    LWJGL_GLES2Functions.checkError();
  }

  static boolean pointProgramSizeControlIsEnabled()
    throws JCGLException
  {
    final boolean e = GL11.glIsEnabled(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
    LWJGL_GLES2Functions.checkError();
    return e;
  }

  static void polygonSetMode(
    final @Nonnull JCGLStateCache cache,
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(mode, "Polygon mode");

    final int im = LWJGL_GLTypeConversions.polygonModeToGL(mode);
    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, im);
    LWJGL_GLES2Functions.checkError();
    cache.polygon_mode = mode;
  }

  static void polygonSmoothingDisable()
    throws JCGLException
  {
    GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
    LWJGL_GLES2Functions.checkError();
  }

  static void polygonSmoothingEnable()
    throws JCGLException
  {
    GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
    LWJGL_GLES2Functions.checkError();
  }

  static boolean polygonSmoothingIsEnabled()
    throws JCGLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_POLYGON_SMOOTH);
    LWJGL_GLES2Functions.checkError();
    return e;
  }

  static int stencilBufferGetBits(
    final @Nonnull JCGLStateCache state)
    throws JCGLException
  {
    if (LWJGL_GL3Functions.framebufferDrawAnyIsBound()) {
      /**
       * If a framebuffer is bound, check to see if there's a stencil
       * attachment.
       */

      {
        final int p =
          GL30.glGetFramebufferAttachmentParameteri(
            GL30.GL_DRAW_FRAMEBUFFER,
            GL30.GL_STENCIL_ATTACHMENT,
            GL30.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE);
        LWJGL_GLES2Functions.checkError();
        if (p == GL11.GL_NONE) {
          return 0;
        }
      }

      /**
       * If there's a stencil attachment, check the size of it.
       */

      {
        final int p =
          GL30.glGetFramebufferAttachmentParameteri(
            GL30.GL_DRAW_FRAMEBUFFER,
            GL30.GL_STENCIL_ATTACHMENT,
            GL30.GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE);
        LWJGL_GLES2Functions.checkError();
        return p;
      }
    }

    /**
     * If no framebuffer is bound, use the default glGet query.
     */

    final int bits =
      LWJGL_GLES2Functions.contextGetInteger(state, GL11.GL_STENCIL_BITS);
    LWJGL_GLES2Functions.checkError();
    return bits;
  }
}
