package com.io7m.jcanephora;

/**
 * A simplified and type-safe interface to modern programmable-pipeline GPUs.
 */

public interface GLInterface extends
  GLArrayBuffers,
  GLBlend,
  GLColorBuffer,
  GLCull,
  GLDepthBuffer,
  GLDraw,
  GLErrorCodes,
  GLFramebuffers,
  GLIndexBuffers,
  GLLogic,
  GLMeta,
  GLPixelUnpackBuffers,
  GLRasterization,
  GLScissor,
  GLShaders,
  GLStencilBuffer,
  GLTextures,
  GLViewport
{
  /*
   * All functions defined in the superinterfaces.
   */
}
