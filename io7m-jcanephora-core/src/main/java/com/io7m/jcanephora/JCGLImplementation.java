/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;

/**
 * The interface exposed by the current OpenGL implementation.
 */

public interface JCGLImplementation
{
  /**
   * <p>
   * Return a reference to the OpenGL 2.1 interface provided by the
   * implementation, or <code>None</code> if it is not supported.
   * </p>
   * 
   * @deprecated Use {@link #implementationAccept(JCGLImplementationVisitor)}.
   */

  @Deprecated public @Nonnull Option<JCGLInterfaceGL2> getGL2();

  /**
   * <p>
   * Return a reference to the OpenGL 3.* interface provided by the
   * implementation, or <code>None</code> if it is not supported.
   * </p>
   * 
   * @deprecated Use {@link #implementationAccept(JCGLImplementationVisitor)}.
   */

  @Deprecated public @Nonnull Option<JCGLInterfaceGL3> getGL3();

  /**
   * <p>
   * Return a reference to the interface representing the common subset of
   * OpenGL 3.*, OpenGL ES2, OpenGL ES3, and OpenGL 2.1.
   * </p>
   */

  public @Nonnull JCGLInterfaceCommon getGLCommon();

  /**
   * <p>
   * Return a reference to the OpenGL ES2 interface provided by the
   * implementation, or <code>None</code> if it is not supported.
   * </p>
   * 
   * @deprecated Use {@link #implementationAccept(JCGLImplementationVisitor)}.
   */

  @Deprecated public @Nonnull Option<JCGLInterfaceGLES2> getGLES2();

  /**
   * <p>
   * Return a reference to the OpenGL ES3 interface provided by the
   * implementation, or <code>None</code> if it is not supported.
   * </p>
   * 
   * @deprecated Use {@link #implementationAccept(JCGLImplementationVisitor)}.
   */

  @Deprecated public @Nonnull Option<JCGLInterfaceGLES3> getGLES3();

  /**
   * A function that accepts implementation visitors. Returns the value of
   * type <code>A</code> returned by <code>v</code>.
   * 
   * @throws ConstraintError
   *           Iff <code>v == null</code> or a {@link ConstraintError} is
   *           propagated from <code>v</code>.
   * @throws JCGLException
   *           Iff a {@link JCGLException} is propagated from <code>v</code>.
   * @throws E
   *           Iff <code>v</code> throws an <code>E</code>.
   */

  public <A, E extends Throwable> A implementationAccept(
    final @Nonnull JCGLImplementationVisitor<A, E> v)
    throws JCGLException,
      ConstraintError,
      E;
}
