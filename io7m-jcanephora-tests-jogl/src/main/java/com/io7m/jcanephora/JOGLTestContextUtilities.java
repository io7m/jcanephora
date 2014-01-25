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
import com.io7m.jaux.UnreachableCodeException;

final class JOGLTestContextUtilities
{
  static @Nonnull JCGLInterfaceGL2 getGL2(
    final @Nonnull TestContext context)
  {
    try {
      return context
        .getGLImplementation()
        .implementationAccept(
          new JCGLImplementationVisitor<JCGLInterfaceGL2, UnreachableCodeException>() {
            @Override public JCGLInterfaceGL2 implementationIsGL2(
              final JCGLInterfaceGL2 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              return gl;
            }

            @Override public JCGLInterfaceGL2 implementationIsGL3(
              final JCGLInterfaceGL3 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }

            @Override public JCGLInterfaceGL2 implementationIsGLES2(
              final JCGLInterfaceGLES2 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }

            @Override public JCGLInterfaceGL2 implementationIsGLES3(
              final JCGLInterfaceGLES3 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }
          });
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }

  static @Nonnull JCGLInterfaceGL3 getGL3(
    final @Nonnull TestContext context)
  {
    try {
      return context
        .getGLImplementation()
        .implementationAccept(
          new JCGLImplementationVisitor<JCGLInterfaceGL3, UnreachableCodeException>() {
            @Override public JCGLInterfaceGL3 implementationIsGL2(
              final JCGLInterfaceGL2 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }

            @Override public JCGLInterfaceGL3 implementationIsGL3(
              final JCGLInterfaceGL3 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              return gl;
            }

            @Override public JCGLInterfaceGL3 implementationIsGLES2(
              final JCGLInterfaceGLES2 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }

            @Override public JCGLInterfaceGL3 implementationIsGLES3(
              final JCGLInterfaceGLES3 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }
          });
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }

  static @Nonnull JCGLInterfaceGLES2 getGLES2(
    final @Nonnull TestContext context)
  {
    try {
      return context
        .getGLImplementation()
        .implementationAccept(
          new JCGLImplementationVisitor<JCGLInterfaceGLES2, UnreachableCodeException>() {
            @Override public JCGLInterfaceGLES2 implementationIsGL2(
              final JCGLInterfaceGL2 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }

            @Override public JCGLInterfaceGLES2 implementationIsGL3(
              final JCGLInterfaceGL3 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }

            @Override public JCGLInterfaceGLES2 implementationIsGLES2(
              final JCGLInterfaceGLES2 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              return gl;
            }

            @Override public JCGLInterfaceGLES2 implementationIsGLES3(
              final JCGLInterfaceGLES3 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }
          });
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }

  static @Nonnull JCGLInterfaceGLES3 getGLES3(
    final @Nonnull TestContext context)
  {
    try {
      return context
        .getGLImplementation()
        .implementationAccept(
          new JCGLImplementationVisitor<JCGLInterfaceGLES3, UnreachableCodeException>() {
            @Override public JCGLInterfaceGLES3 implementationIsGL2(
              final JCGLInterfaceGL2 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }

            @Override public JCGLInterfaceGLES3 implementationIsGL3(
              final JCGLInterfaceGL3 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }

            @Override public JCGLInterfaceGLES3 implementationIsGLES2(
              final JCGLInterfaceGLES2 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              throw new UnreachableCodeException();
            }

            @Override public JCGLInterfaceGLES3 implementationIsGLES3(
              final JCGLInterfaceGLES3 gl)
              throws JCGLException,
                ConstraintError,
                UnreachableCodeException
            {
              return gl;
            }
          });
    } catch (final JCGLException e) {
      throw new UnreachableCodeException(e);
    } catch (final ConstraintError e) {
      throw new UnreachableCodeException(e);
    }
  }
}
