package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;

/**
 * A class implementing GLInterface that uses only the features of OpenGL ES2,
 * using LWJGL as the backend.
 * 
 * As OpenGL ES2 is essentially a subset of 2.1, this class works on OpenGL
 * 2.1 implementations.
 */

@NotThreadSafe public final class GLInterfaceEmbedded_LWJGL_ES2 extends
  GLInterfaceEmbedded_LWJGL_ES2_Actual
{
  public GLInterfaceEmbedded_LWJGL_ES2(
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    super(log);
  }
}
