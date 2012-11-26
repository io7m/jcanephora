package com.io7m.jcanephora;

/**
 * A simplified and type-safe interface to modern "embedded"
 * programmable-pipeline GPUs.
 */

public interface GLInterface extends
  GLArrayBuffersMapped,
  GLIndexBuffersMapped,
  GLInterfaceEmbedded,
  GLLogic,
  GLPixelUnpackBuffers,
  GLPolygonModes
{
  /*
   * All functions defined in the superinterfaces.
   */
}
