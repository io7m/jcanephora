package com.io7m.jcanephora;

/**
 * A simplified and type-safe interface to modern programmable-pipeline GPUs.
 * 
 * The <code>GLInterface</code> interface specifies a kind of
 * minimum-compatibility level: Programs that are written against the
 * <code>GLInterface</code> interface have a reasonable assurance that they
 * will work on implementations that support the common subset of OpenGL 3.0
 * (with none of the deprecated features - the "core" profile) and OpenGL 2.1.
 * 
 * This assurance obviously does not extend to shading language programs.
 */

public interface GLInterface extends
  GLArrayBuffersMapped,
  GLBlending,
  GLInterfaceEmbedded,
  GLIndexBuffersMapped,
  GLLogic,
  GLPolygonModes,
  GLPolygonSmoothing,
  GLProgramPointSizeControl,
  GLTextures2DBuffered
{
  /*
   * All functions defined in the superinterfaces.
   */
}
