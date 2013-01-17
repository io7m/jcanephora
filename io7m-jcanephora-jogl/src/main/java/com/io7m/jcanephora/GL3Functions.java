package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GL3;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;
import com.jogamp.common.nio.Buffers;

final class GL3Functions
{
  static final @Nonnull ArrayBuffer arrayBufferAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull Log log,
    final @Nonnull GLStateCache state,
    final long elements,
    final @Nonnull ArrayBufferDescriptor descriptor,
    final @Nonnull UsageHint usage)
    throws GLException,
      ConstraintError
  {
    Constraints
      .constrainRange(elements, 1, Long.MAX_VALUE, "Buffer elements");
    Constraints.constrainNotNull(descriptor, "Buffer descriptor");
    Constraints.constrainNotNull(usage, "Usage hint");

    final long size = descriptor.getSize();
    final long bytes_total = elements * size;

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: allocate (");
      state.log_text.append(elements);
      state.log_text.append(" elements, ");
      state.log_text.append(size);
      state.log_text.append(" bytes per element, ");
      state.log_text.append(bytes_total);
      state.log_text.append(" bytes, usage ");
      state.log_text.append(usage);
      state.log_text.append("))");
      log.debug(state.log_text.toString());
    }

    final IntBuffer cache = state.getIntegerCache();
    gl.glGenBuffers(1, cache);

    final int id = cache.get(0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id);
    gl.glBufferData(
      GL.GL_ARRAY_BUFFER,
      bytes_total,
      null,
      GLTypeConversions.usageHintToGL(usage));

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("array-buffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    GLES2Functions.checkError(gl);
    return new ArrayBuffer(id, elements, descriptor);
  }

  static ByteBuffer arrayBufferMapRead(
    final @Nonnull GL3 gl,
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

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
    GLES2Functions.checkError(gl);
    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ARRAY_BUFFER, GL2GL3.GL_READ_ONLY);
    GLES2Functions.checkError(gl);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);

    return b;
  }

  static ArrayBufferWritableMap arrayBufferMapWrite(
    final @Nonnull GL3 gl,
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

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
    GLES2Functions.checkError(gl);
    gl.glBufferData(
      GL.GL_ARRAY_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLES2Functions.checkError(gl);

    final ByteBuffer b = gl.glMapBuffer(GL.GL_ARRAY_BUFFER, GL.GL_WRITE_ONLY);
    GLES2Functions.checkError(gl);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);

    return new ArrayBufferWritableMap(id, b);
  }

  static void arrayBufferUnmap(
    final @Nonnull GL3 gl,
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

    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, id.getGLName());
    gl.glUnmapBuffer(GL.GL_ARRAY_BUFFER);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);
  }

  static void blendingEnableSeparateWithEquationSeparate(
    final @Nonnull GL3 gl,
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

    gl.glEnable(GL.GL_BLEND);
    gl.glBlendEquationSeparate(
      GLTypeConversions.blendEquationToGL(equation_rgb),
      GLTypeConversions.blendEquationToGL(equation_alpha));
    gl.glBlendFuncSeparate(
      GLTypeConversions.blendFunctionToGL(source_rgb_factor),
      GLTypeConversions.blendFunctionToGL(destination_rgb_factor),
      GLTypeConversions.blendFunctionToGL(source_alpha_factor),
      GLTypeConversions.blendFunctionToGL(destination_alpha_factor));
    GLES2Functions.checkError(gl);
  }

  static void blendingEnableWithEquation(
    final @Nonnull GL3 gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquation equation)
    throws ConstraintError,
      GLException
  {
    GL3Functions.blendingEnableSeparateWithEquationSeparate(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  static void blendingEnableWithEquationSeparate(
    final @Nonnull GL3 gl,
    final @Nonnull BlendFunction source_factor,
    final @Nonnull BlendFunction destination_factor,
    final @Nonnull BlendEquation equation_rgb,
    final @Nonnull BlendEquation equation_alpha)
    throws ConstraintError,
      GLException
  {
    GL3Functions.blendingEnableSeparateWithEquationSeparate(
      gl,
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  static void depthBufferClear(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final float depth)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainRange(
      GL3Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glClearDepth(depth);
    gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
    GLES2Functions.checkError(gl);
  }

  static void depthBufferDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_DEPTH_TEST);
    GLES2Functions.checkError(gl);
  }

  static void depthBufferEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull DepthFunction function)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(function, "Depth function");
    Constraints.constrainRange(
      GL3Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    final int d = GLTypeConversions.depthFunctionToGL(function);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(d);
    GLES2Functions.checkError(gl);
  }

  static int depthBufferGetBits(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    /**
     * If a framebuffer is bound, check to see if there's a depth attachment.
     */

    if (GL3Functions.framebufferDrawAnyIsBound(gl)) {

      {
        final IntBuffer cache = state.getIntegerCache();
        gl.glGetFramebufferAttachmentParameteriv(
          GL2GL3.GL_DRAW_FRAMEBUFFER,
          GL.GL_DEPTH_ATTACHMENT,
          GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE,
          cache);
        GLES2Functions.checkError(gl);
        if (cache.get(0) == GL.GL_NONE) {
          return 0;
        }
      }

      /**
       * If there's a depth attachment, check the size of it.
       */

      final IntBuffer cache = state.getIntegerCache();
      gl.glGetFramebufferAttachmentParameteriv(
        GL2GL3.GL_DRAW_FRAMEBUFFER,
        GL.GL_DEPTH_ATTACHMENT,
        GL2GL3.GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE,
        cache);
      GLES2Functions.checkError(gl);
      return cache.get(0);
    }

    /**
     * If no framebuffer is bound, use the default glGet query.
     */

    final int bits =
      GLES2Functions.contextGetInteger(gl, state, GL.GL_DEPTH_BITS);
    GLES2Functions.checkError(gl);
    return bits;
  }

  static boolean depthBufferIsEnabled(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_DEPTH_TEST);
    GLES2Functions.checkError(gl);
    return e;
  }

  static void depthBufferWriteDisable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      GL3Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glDepthMask(false);
    GLES2Functions.checkError(gl);
  }

  static void depthBufferWriteEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      GL3Functions.depthBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Depth buffer bits available");

    gl.glDepthMask(true);
    GLES2Functions.checkError(gl);
  }

  static boolean depthBufferWriteIsEnabled(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final ByteBuffer cache = state.getDepthMaskCache();
    gl.glGetBooleanv(GL.GL_DEPTH_WRITEMASK, cache);
    GLES2Functions.checkError(gl);

    final IntBuffer bi = cache.asIntBuffer();
    return bi.get(0) == 1;
  }

  static @Nonnull FramebufferReference framebufferAllocate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGenFramebuffers(1, cache);
    GLES2Functions.checkError(gl);
    final int id = cache.get(0);

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer: allocated ");
      state.log_text.append(id);
      log.debug(state.log_text.toString());
    }

    return new FramebufferReference(id);
  }

  static void framebufferDelete(
    final @Nonnull GL2ES2 gl,
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

    final IntBuffer cache = state.getIntegerCache();
    cache.put(0, buffer.getGLName());
    gl.glDeleteFramebuffers(1, cache);
    GLES2Functions.checkError(gl);
    buffer.setDeleted();
  }

  static boolean framebufferDrawAnyIsBound(
    final @Nonnull GL2ES2 gl)
  {
    final int bound = gl.getBoundFramebuffer(GL2GL3.GL_DRAW_FRAMEBUFFER);
    final int default_fb = gl.getDefaultDrawFramebuffer();
    return bound != default_fb;
  }

  static void framebufferDrawAttachColorRenderbuffer(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(gl, framebuffer),
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

    gl.glFramebufferRenderbuffer(
      GL2GL3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorRenderbufferAt(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull RenderbufferUsable renderbuffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GLES2Functions.framebufferDrawIsBound(gl, framebuffer),
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

    gl.glFramebufferRenderbuffer(
      GL2GL3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0 + point.getIndex(),
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTexture2D(
    final @Nonnull GL2ES2 gl,
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
      GL3Functions.framebufferDrawIsBound(gl, framebuffer),
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

    gl.glFramebufferTexture2D(
      GL2GL3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      GL.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTexture2DAt(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull Texture2DStaticUsable texture)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GLES2Functions.framebufferDrawIsBound(gl, framebuffer),
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

    gl.glFramebufferTexture2D(
      GL2GL3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0 + point.getIndex(),
      GL.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTextureCube(
    final @Nonnull GL2ES2 gl,
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
      GL3Functions.framebufferDrawIsBound(gl, framebuffer),
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
    gl.glFramebufferTexture2D(
      GL2GL3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0,
      gface,
      texture.getGLName(),
      0);
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachColorTextureCubeAt(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull FramebufferColorAttachmentPoint point,
    final @Nonnull TextureCubeStaticUsable texture,
    final @Nonnull CubeMapFace face)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(point, "Attachment point");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GLES2Functions.framebufferDrawIsBound(gl, framebuffer),
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

    gl.glFramebufferTexture2D(
      GL2GL3.GL_DRAW_FRAMEBUFFER,
      GL.GL_COLOR_ATTACHMENT0 + point.getIndex(),
      GLTypeConversions.cubeFaceToGL(face),
      texture.getGLName(),
      0);
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachDepthRenderbuffer(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isDepthRenderable(),
      "Renderbuffer is depth renderable");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isStencilRenderable() == false,
      "Renderbuffer is not also stencil renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at depth attachment");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferRenderbuffer(
      GL2GL3.GL_DRAW_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachDepthStencilRenderbuffer(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(gl, framebuffer),
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

    gl.glFramebufferRenderbuffer(
      GL2GL3.GL_DRAW_FRAMEBUFFER,
      GL2GL3.GL_DEPTH_STENCIL_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachDepthTexture2D(
    final @Nonnull GL2ES2 gl,
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
      GL3Functions.framebufferDrawIsBound(gl, framebuffer),
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

    gl.glFramebufferTexture2D(
      GL2GL3.GL_DRAW_FRAMEBUFFER,
      GL.GL_DEPTH_ATTACHMENT,
      GL.GL_TEXTURE_2D,
      texture.getGLName(),
      0);
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawAttachStencilRenderbuffer(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final @Nonnull Log log,
    final @Nonnull FramebufferReference framebuffer,
    final @Nonnull RenderbufferUsable renderbuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    Constraints.constrainNotNull(renderbuffer, "Renderbuffer");
    Constraints.constrainArbitrary(
      renderbuffer.resourceIsDeleted() == false,
      "Renderbuffer not deleted");
    Constraints.constrainArbitrary(renderbuffer
      .getType()
      .isStencilRenderable(), "Renderbuffer is stencil renderable");
    Constraints.constrainArbitrary(
      renderbuffer.getType().isDepthRenderable() == false,
      "Renderbuffer is not also depth renderable");

    if (log.enabled(Level.LOG_DEBUG)) {
      state.log_text.setLength(0);
      state.log_text.append("framebuffer-draw: attach ");
      state.log_text.append(framebuffer);
      state.log_text.append(" ");
      state.log_text.append(renderbuffer);
      state.log_text.append(" at stencil attachment");
      log.debug(state.log_text.toString());
    }

    gl.glFramebufferRenderbuffer(
      GL2GL3.GL_DRAW_FRAMEBUFFER,
      GL.GL_STENCIL_ATTACHMENT,
      GL.GL_RENDERBUFFER,
      renderbuffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawBind(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FramebufferReference buffer)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(buffer, "Framebuffer");
    Constraints.constrainArbitrary(
      buffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    gl.glBindFramebuffer(GL2GL3.GL_DRAW_FRAMEBUFFER, buffer.getGLName());
    GLES2Functions.checkError(gl);
  }

  static boolean framebufferDrawIsBound(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FramebufferReference framebuffer)
    throws ConstraintError
  {
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");

    final int bound = gl.getBoundFramebuffer(GL2GL3.GL_DRAW_FRAMEBUFFER);
    return bound == framebuffer.getGLName();
  }

  static
    void
    framebufferDrawSetBuffers(
      final @Nonnull GL3 gl,
      final @Nonnull GLStateCache state,
      final @Nonnull Log log,
      final @Nonnull FramebufferReference framebuffer,
      final @Nonnull Map<FramebufferDrawBuffer, FramebufferColorAttachmentPoint> mappings)
      throws ConstraintError,
        GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");
    Constraints.constrainNotNull(mappings, "Draw buffer attachment mappings");

    final FramebufferDrawBuffer[] buffers = state.draw_buffers;
    final IntBuffer out = Buffers.newDirectIntBuffer(buffers.length);

    for (int index = 0; index < buffers.length; ++index) {
      final FramebufferDrawBuffer buffer = new FramebufferDrawBuffer(index);
      if (mappings.containsKey(buffer)) {
        final FramebufferColorAttachmentPoint attach = mappings.get(buffer);
        out.put(index, GL.GL_COLOR_ATTACHMENT0 + attach.getIndex());
        if (log.enabled(Level.LOG_DEBUG)) {
          state.log_text.setLength(0);
          state.log_text.append("draw-buffers: map ");
          state.log_text.append(buffer);
          state.log_text.append(" to ");
          state.log_text.append(attach);
          log.debug(state.log_text.toString());
        }
      } else {
        out.put(index, GL.GL_NONE);
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

    gl.glDrawBuffers(buffers.length, out);
    GLES2Functions.checkError(gl);
  }

  static void framebufferDrawUnbind(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glBindFramebuffer(GL2GL3.GL_DRAW_FRAMEBUFFER, 0);
    GLES2Functions.checkError(gl);
  }

  static @Nonnull FramebufferStatus framebufferDrawValidate(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FramebufferReference framebuffer)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(framebuffer, "Framebuffer");
    Constraints.constrainArbitrary(
      framebuffer.resourceIsDeleted() == false,
      "Framebuffer not deleted");
    Constraints.constrainArbitrary(
      GL3Functions.framebufferDrawIsBound(gl, framebuffer),
      "Framebuffer is bound");

    final int status =
      gl.glCheckFramebufferStatus(GL2GL3.GL_DRAW_FRAMEBUFFER);
    GLES2Functions.checkError(gl);

    return GLTypeConversions.framebufferStatusFromGL(status);
  }

  static IndexBufferReadableMap indexBufferMapRead(
    final @Nonnull GL3 gl,
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

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    GLES2Functions.checkError(gl);
    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, GL2GL3.GL_READ_ONLY);
    GLES2Functions.checkError(gl);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);

    return new IndexBufferReadableMap(id, b);
  }

  static IndexBufferWritableMap indexBufferMapWrite(
    final @Nonnull GL3 gl,
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

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    GLES2Functions.checkError(gl);
    gl.glBufferData(
      GL.GL_ELEMENT_ARRAY_BUFFER,
      id.getSizeBytes(),
      null,
      GL2ES2.GL_STREAM_DRAW);
    GLES2Functions.checkError(gl);

    final ByteBuffer b =
      gl.glMapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, GL.GL_WRITE_ONLY);
    GLES2Functions.checkError(gl);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);

    return new IndexBufferWritableMap(id, b);
  }

  static void indexBufferUnmap(
    final @Nonnull GL3 gl,
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

    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, id.getGLName());
    gl.glUnmapBuffer(GL.GL_ELEMENT_ARRAY_BUFFER);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    GLES2Functions.checkError(gl);
  }

  static void logicOperationsDisable(
    final @Nonnull GL3 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_COLOR_LOGIC_OP);
    GLES2Functions.checkError(gl);
  }

  static void logicOperationsEnable(
    final @Nonnull GL3 gl,
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(operation, "Logic operation");
    gl.glEnable(GL.GL_COLOR_LOGIC_OP);
    gl.glLogicOp(GLTypeConversions.logicOpToGL(operation));
    GLES2Functions.checkError(gl);
  }

  static boolean logicOperationsEnabled(
    final @Nonnull GL3 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_COLOR_LOGIC_OP);
    GLES2Functions.checkError(gl);
    return e;
  }

  static void pointProgramSizeControlDisable(
    final @Nonnull GL3 gl)
    throws GLException
  {
    gl.glDisable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLES2Functions.checkError(gl);
  }

  static void pointProgramSizeControlEnable(
    final @Nonnull GL3 gl)
    throws GLException
  {
    gl.glEnable(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLES2Functions.checkError(gl);
  }

  static boolean pointProgramSizeControlIsEnabled(
    final @Nonnull GL3 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL2GL3.GL_VERTEX_PROGRAM_POINT_SIZE);
    GLES2Functions.checkError(gl);
    return e;
  }

  static @Nonnull PolygonMode polygonGetMode(
    final @Nonnull GL3 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    final IntBuffer cache = state.getIntegerCache();
    gl.glGetIntegerv(GL2.GL_POLYGON_MODE, cache);
    GLES2Functions.checkError(gl);
    return GLTypeConversions.polygonModeFromGL(cache.get(1));
  }

  static void polygonSetMode(
    final @Nonnull GL3 gl,
    final @Nonnull PolygonMode mode)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(mode, "Polygon mode");

    final int im = GLTypeConversions.polygonModeToGL(mode);
    gl.glPolygonMode(GL.GL_FRONT_AND_BACK, im);
    GLES2Functions.checkError(gl);
  }

  static void polygonSmoothingDisable(
    final @Nonnull GL3 gl)
    throws GLException
  {
    gl.glDisable(GL2GL3.GL_POLYGON_SMOOTH);
    GLES2Functions.checkError(gl);
  }

  static void polygonSmoothingEnable(
    final @Nonnull GL3 gl)
    throws GLException
  {
    gl.glEnable(GL2GL3.GL_POLYGON_SMOOTH);
    GLES2Functions.checkError(gl);
  }

  static boolean polygonSmoothingIsEnabled(
    final @Nonnull GL3 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL2GL3.GL_POLYGON_SMOOTH);
    GLES2Functions.checkError(gl);
    return e;
  }

  static void stencilBufferClear(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state,
    final int stencil)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainRange(
      GL3Functions.stencilBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    gl.glClearStencil(stencil);
    GLES2Functions.checkError(gl);
  }

  static void stencilBufferDisable(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    gl.glDisable(GL.GL_STENCIL_TEST);
    GLES2Functions.checkError(gl);
  }

  static void stencilBufferEnable(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainRange(
      GL3Functions.stencilBufferGetBits(gl, state),
      1,
      Integer.MAX_VALUE,
      "Stencil buffer bits available");

    gl.glEnable(GL.GL_STENCIL_TEST);
    GLES2Functions.checkError(gl);
  }

  static void stencilBufferFunction(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilFunction function,
    final int reference,
    final int mask)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(function, "Stencil function");

    final int func = GLTypeConversions.stencilFunctionToGL(function);
    gl.glStencilFuncSeparate(
      GLTypeConversions.faceSelectionToGL(faces),
      func,
      reference,
      mask);
    GLES2Functions.checkError(gl);
  }

  static int stencilBufferGetBits(
    final @Nonnull GL2ES2 gl,
    final @Nonnull GLStateCache state)
    throws GLException
  {
    if (GL3Functions.framebufferDrawAnyIsBound(gl)) {
      /**
       * If a framebuffer is bound, check to see if there's a pure stencil
       * attachment.
       */

      {
        final IntBuffer c0 = state.getIntegerCache();
        gl.glGetFramebufferAttachmentParameteriv(
          GL2GL3.GL_DRAW_FRAMEBUFFER,
          GL.GL_STENCIL_ATTACHMENT,
          GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE,
          c0);
        GLES2Functions.checkError(gl);

        /**
         * The only available pure stencil format is STENCIL8, so the stencil
         * buffer must have 8 bits.
         */

        final int type = c0.get(0);
        if (type != GL.GL_NONE) {
          return 8;
        }
      }

      /**
       * If a framebuffer is bound, check to see if there's a depth+stencil
       * attachment.
       */

      {
        final IntBuffer c0 = state.getIntegerCache();
        gl.glGetFramebufferAttachmentParameteriv(
          GL2GL3.GL_DRAW_FRAMEBUFFER,
          GL2GL3.GL_DEPTH_STENCIL_ATTACHMENT,
          GL.GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE,
          c0);
        GLES2Functions.checkError(gl);

        /**
         * The only available depth/stencil format is DEPTH24_STENCIL8, so the
         * stencil buffer must have 8 bits.
         */

        final int type = c0.get(0);
        if (type != GL.GL_NONE) {
          return 8;
        }
      }

    }

    /**
     * If no framebuffer is bound, use the default glGet query.
     */

    final int bits =
      GLES2Functions.contextGetInteger(gl, state, GL.GL_STENCIL_BITS);
    GLES2Functions.checkError(gl);
    return bits;
  }

  static boolean stencilBufferIsEnabled(
    final @Nonnull GL2ES2 gl)
    throws GLException
  {
    final boolean e = gl.glIsEnabled(GL.GL_STENCIL_TEST);
    GLES2Functions.checkError(gl);
    return e;
  }

  static void stencilBufferMask(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final int mask)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");

    gl
      .glStencilMaskSeparate(GLTypeConversions.faceSelectionToGL(faces), mask);
    GLES2Functions.checkError(gl);
  }

  static void stencilBufferOperation(
    final @Nonnull GL2ES2 gl,
    final @Nonnull FaceSelection faces,
    final @Nonnull StencilOperation stencil_fail,
    final @Nonnull StencilOperation depth_fail,
    final @Nonnull StencilOperation pass)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(faces, "Face selection");
    Constraints.constrainNotNull(stencil_fail, "Stencil fail operation");
    Constraints.constrainNotNull(depth_fail, "Depth fail operation");
    Constraints.constrainNotNull(pass, "Pass operation");

    final int sfail = GLTypeConversions.stencilOperationToGL(stencil_fail);
    final int dfail = GLTypeConversions.stencilOperationToGL(depth_fail);
    final int dpass = GLTypeConversions.stencilOperationToGL(pass);
    gl.glStencilOpSeparate(
      GLTypeConversions.faceSelectionToGL(faces),
      sfail,
      dfail,
      dpass);
    GLES2Functions.checkError(gl);
  }

}
