package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import javax.media.opengl.GLContext;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;

/**
 * A class implementing GLInterface that uses only the features of OpenGL ES2,
 * using JOGL as the backend.
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

@NotThreadSafe public final class GLEmbeddedJOGLES2 extends
  GLEmbeddedJOGLES2Actual
{
  /**
   * Construct an interface using context <code>context</code> and the log
   * interface <code>log</code>.
   * 
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>context == null</code></li>
   *           <li><code>log == null</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an internal OpenGL error occurs.
   */

  public GLEmbeddedJOGLES2(
    final @Nonnull GLContext context,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    super(context, log);
  }
}
