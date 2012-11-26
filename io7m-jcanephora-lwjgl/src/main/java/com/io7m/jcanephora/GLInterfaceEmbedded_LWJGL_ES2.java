package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;

/**
 * A class implementing GLInterface that uses only the features of OpenGL ES2,
 * using LWJGL as the backend.
 * 
 * A {@link javax.media.opengl.GLContext} is used to construct the interface,
 * and therefore the <code>GLInterfaceJOGL30</code> interface has the same
 * thread safe/unsafe behaviour.
 * 
 * The <code>GLInterfaceJOGL30</code> implementation does not call
 * {@link javax.media.opengl.GLContext#makeCurrent()} or
 * {@link javax.media.opengl.GLContext#release()}, so these calls must be made
 * by the programmer when necessary (typically, programs call
 * {@link javax.media.opengl.GLContext#makeCurrent()}, perform all rendering,
 * and then call {@link javax.media.opengl.GLContext#release()} at the end of
 * the frame). The JOGL library can also optionally manage this via the
 * {@link javax.media.opengl.GLAutoDrawable} interface.
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
