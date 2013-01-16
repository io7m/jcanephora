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
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;

final class GL3Functions
{
  static ByteBuffer arrayBufferMapRead(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer id)
    throws GLException,
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
    GLES2Functions.checkError();
    final ByteBuffer b =
      GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_READ_ONLY, null);
    GLES2Functions.checkError();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GLES2Functions.checkError();

    return b;
  }

  static ArrayBufferWritableMap arrayBufferMapWrite(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer id)
    throws GLException,
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
    GLES2Functions.checkError();
    GL15.glBufferData(
      GL15.GL_ARRAY_BUFFER,
      id.getSizeBytes(),
      GL15.GL_STREAM_DRAW);
    GLES2Functions.checkError();

    final ByteBuffer b =
      GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_WRITE_ONLY, null);
    GLES2Functions.checkError();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    GLES2Functions.checkError();

    return new ArrayBufferWritableMap(id, b);
  }

  static void arrayBufferUnmap(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull ArrayBuffer id)
    throws ConstraintError,
      GLException
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
    GLES2Functions.checkError();
  }

  static void blendingEnableSeparateWithEquationSeparate(
    final @Nonnull BlendFunction source_rgb_factor,
    final @Nonnull BlendFunction source_alpha_factor,
    final @Nonnull BlendFunction destination_rgb_factor,
    final @Nonnull BlendFunction destination_alpha_factor,
    final @Nonnull BlendEquation equation_rgb,
    final @Nonnull BlendEquation equation_alpha)
    throws ConstraintError,
      GLException
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
      GLTypeConversions.blendEquationToGL(equation_rgb),
      GLTypeConversions.blendEquationToGL(equation_alpha));
    GL14.glBlendFuncSeparate(
      GLTypeConversions.blendFunctionToGL(source_rgb_factor),
      GLTypeConversions.blendFunctionToGL(destination_rgb_factor),
      GLTypeConversions.blendFunctionToGL(source_alpha_factor),
      GLTypeConversions.blendFunctionToGL(destination_alpha_factor));
    GLES2Functions.checkError();
  }

  static void blendingEnableWithEquation(
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquation equation)
    throws ConstraintError,
      GLException
  {
    GL3Functions.blendingEnableSeparateWithEquationSeparate(

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
    final @Nonnull BlendEquation equation_rgb,
    final @Nonnull BlendEquation equation_alpha)
    throws ConstraintError,
      GLException
  {
    GL3Functions.blendingEnableSeparateWithEquationSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  static void framebufferDelete(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference buffer)
    throws ConstraintError,
      GLException
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
    GLES2Functions.checkError();
    buffer.setDeleted();
  }

  static void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
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
    GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorRenderbufferAt(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull RenderbufferUsable renderbuffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
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
    GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorTexture2D(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
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
    GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorTexture2DAt(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
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
    GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorTextureCube(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFace face)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
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

    final int gface = GLTypeConversions.cubeFaceToGL(face);
    GL30.glFramebufferTexture2D(
      GL30.GL_DRAW_FRAMEBUFFER,
      GL30.GL_COLOR_ATTACHMENT0,
      gface,
      texture.getGLName(),
      0);
    GLES2Functions.checkError();
  }

  static void framebufferDrawAttachColorTextureCubeAt(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFace face)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
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
      GLTypeConversions.cubeFaceToGL(face),
      texture.getGLName(),
      0);
    GLES2Functions.checkError();
  }

  static void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
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
    GLES2Functions.checkError();
  }

  static void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
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
    GLES2Functions.checkError();
  }

  static void framebufferDrawAttachDepthTexture2D(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull Texture2DStaticUsable texture)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
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
    GLES2Functions.checkError();
  }

  static void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
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
    GLES2Functions.checkError();
  }

  static void framebufferDrawBind(
    final @Nonnull FramebufferReference buffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, buffer.getGLName());
    GLES2Functions.checkError();
  }

  static boolean framebufferDrawIsBound(
    final @Nonnull GLStateCache state,
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    final IntBuffer cache = state.getIntegerCache();
    GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING, cache);
    GLES2Functions.checkError();
    return cache.get(0) == framebuffer.getGLName();
  }

  static
    void
    framebufferDrawSetBuffers(
      final @Nonnull GLStateCache state,
      final @Nonnull Log log,
      final @Nonnull FramebufferReference framebuffer,
      final @Nonnull Map<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> mappings)
      throws ConstraintError,
        GLException
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
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
    GLES2Functions.checkError();
  }

  static void framebufferDrawUnbind()
    throws GLException
  {
    GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
    GLES2Functions.checkError();
  }

  static @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull GLStateCache state,
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(state, framebuffer),
      "Framebuffer is bound");

    final int status =
      GL30.glCheckFramebufferStatus(GL30.GL_DRAW_FRAMEBUFFER);
    GLES2Functions.checkError();

    return GLTypeConversions.framebufferStatusFromGL(status);
  }

  static IndexBufferReadableMap indexBufferMapRead(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws GLException,
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
    GLES2Functions.checkError();
    final ByteBuffer b =
      GL15.glMapBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GL15.GL_READ_ONLY, null);
    GLES2Functions.checkError();
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLES2Functions.checkError();

    return new IndexBufferReadableMap(id, b);
  }

  static IndexBufferWritableMap indexBufferMapWrite(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws GLException,
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
    GLES2Functions.checkError();
    GL15.glBufferData(
      GL15.GL_ELEMENT_ARRAY_BUFFER,
      id.getSizeBytes(),
      GL15.GL_STREAM_DRAW);
    GLES2Functions.checkError();

    final ByteBuffer b =
      GL15
        .glMapBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GL15.GL_WRITE_ONLY, null);
    GLES2Functions.checkError();
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLES2Functions.checkError();

    return new IndexBufferWritableMap(id, b);
  }

  static void indexBufferUnmap(
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull IndexBuffer id)
    throws ConstraintError,
      GLException
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
    GLES2Functions.checkError();
  }

  static void logicOperationsDisable()
    throws GLException
  {
    GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
    GLES2Functions.checkError();
  }

  static void logicOperationsEnable(
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(operation, "Logic operation");
    GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
    GL11.glLogicOp(GLTypeConversions.logicOpToGL(operation));
    GLES2Functions.checkError();
  }

  static boolean logicOperationsEnabled()
    throws GLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_COLOR_LOGIC_OP);
    GLES2Functions.checkError();
    return e;
  }

  static void pointProgramSizeControlDisable()
    throws GLException
  {
    GL11.glDisable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLES2Functions.checkError();
  }

  static void pointProgramSizeControlEnable()
    throws GLException
  {
    GL11.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLES2Functions.checkError();
  }

  static boolean pointProgramSizeControlIsEnabled()
    throws GLException
  {
    final boolean e = GL11.glIsEnabled(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLES2Functions.checkError();
    return e;
  }

  static @Nonnull PolygonMode polygonGetMode(
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    GL11.glGetInteger(GL11.GL_POLYGON_MODE, cache);
    GLES2Functions.checkError();
    return GLTypeConversions.polygonModeFromGL(cache.get(1));
  }

  static void polygonSetMode(
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(mode, "Polygon mode");

    final int im = GLTypeConversions.polygonModeToGL(mode);
    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, im);
    GLES2Functions.checkError();
  }

  static void polygonSmoothingDisable()
    throws GLException
  {
    GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
    GLES2Functions.checkError();
  }

  static void polygonSmoothingEnable()
    throws GLException
  {
    GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
    GLES2Functions.checkError();
  }

  static boolean polygonSmoothingIsEnabled()
    throws GLException
  {
    final boolean e = GL11.glIsEnabled(GL11.GL_POLYGON_SMOOTH);
    GLES2Functions.checkError();
    return e;
  }
}
