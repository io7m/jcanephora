/*
 * Copyright © 2012 http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

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

@NotThreadSafe public final class GLInterfaceEmbedded_JOGL_ES2 extends
  GLInterfaceEmbedded_JOGL_ES2_Actual
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

  public GLInterfaceEmbedded_JOGL_ES2(
    final @Nonnull GLContext context,
    final @Nonnull Log log)
    throws ConstraintError,
      GLException
  {
    super(context, log);
  }
}
