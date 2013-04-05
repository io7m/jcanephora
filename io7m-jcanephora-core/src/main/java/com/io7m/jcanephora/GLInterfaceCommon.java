package com.io7m.jcanephora;

/**
 * The interface exposed by the common subset of OpenGL 3.* and OpenGL ES2
 * implementations.
 */

public interface GLInterfaceCommon extends
  GLArrayBuffers,
  GLBlendingCommon,
  GLColorBuffer,
  GLCull,
  GLDepthBuffer,
  GLDraw,
  GLErrorCodes,
  GLFramebuffersCommon,
  GLIndexBuffers,
  GLMeta,
  GLRasterization,
  GLRenderbuffersCommon,
  GLScissor,
  GLShaders,
  GLStencilBuffer,
  GLTextures2DStaticCommon,
  GLTexturesCubeStaticCommon,
  GLTextureUnits,
  GLViewport
{
  /*
   * All functions defined in the superinterfaces.
   */
}
