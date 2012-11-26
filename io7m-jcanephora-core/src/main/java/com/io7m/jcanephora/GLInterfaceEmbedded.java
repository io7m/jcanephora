package com.io7m.jcanephora;

/**
 * A simplified and type-safe interface to modern programmable-pipeline GPUs
 * on "embedded" systems.
 * 
 * The <code>GLInterfaceEmbedded</code> interface specifies a kind of
 * minimum-compatibility level: Programs that are written against the
 * <code>GLInterfaceEmbedded</code> interface have a reasonable assurance that
 * they will work on implementations that support the common subset of OpenGL
 * 3.0 (with none of the deprecated features - the "core" profile) and OpenGL
 * ES2.
 * 
 * This assurance obviously does not extend to shading language programs.
 */

public interface GLInterfaceEmbedded extends
  GLArrayBuffers,
  GLBlendingEmbedded,
  GLColorBuffer,
  GLCull,
  GLDepthBuffer,
  GLDraw,
  GLErrorCodes,
  GLFramebuffers,
  GLIndexBuffers,
  GLMeta,
  GLRasterization,
  GLScissor,
  GLShaders,
  GLStencilBuffer,
  GLTextureUnits,
  GLTextures2DRGBAStatic,
  GLViewport
{
  /*
   * All functions defined in the superinterfaces.
   */
}
