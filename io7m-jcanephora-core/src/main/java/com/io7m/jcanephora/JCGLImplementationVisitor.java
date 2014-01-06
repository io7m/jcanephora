/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

/**
 * A visitor that is passed an instance of the current highest supported
 * OpenGL interface type.
 */

public interface JCGLImplementationVisitor<A, E extends Throwable>
{
  public A implementationIsGLES2(
    final @Nonnull JCGLInterfaceGLES2 gl)
    throws JCGLException,
      ConstraintError,
      E;

  public A implementationIsGLES3(
    final @Nonnull JCGLInterfaceGLES3 gl)
    throws JCGLException,
      ConstraintError,
      E;

  public A implementationIsGL2(
    final @Nonnull JCGLInterfaceGL2 gl)
    throws JCGLException,
      ConstraintError,
      E;

  public A implementationIsGL3(
    final @Nonnull JCGLInterfaceGL3 gl)
    throws JCGLException,
      ConstraintError,
      E;
}