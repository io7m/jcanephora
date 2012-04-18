package com.io7m.jcanephora;

/**
 * A simplified and type-safe interface to modern programmable-pipeline GPUs.
 */

public interface GLInterface extends
  GLArrayBuffers,
  GLBlend,
  GLCull,
  GLColorBuffer,
  GLDepthBuffer,
  GLDraw,
  GLFramebuffers,
  GLIndexBuffers,
  GLLogic,
  GLMeta,
  GLPixelUnpackBuffers,
  GLRasterization,
  GLShaders,
  GLScissor,
  GLTextures,
  GLViewport
{
  /*
   * All functions defined in the superinterfaces.
   */
}
