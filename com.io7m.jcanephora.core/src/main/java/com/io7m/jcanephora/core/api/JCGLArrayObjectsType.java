/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.core.api;

import com.io7m.jcanephora.core.JCGLArrayObjectBuilderType;
import com.io7m.jcanephora.core.JCGLArrayObjectType;
import com.io7m.jcanephora.core.JCGLArrayObjectUsableType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionObjectNotDeletable;

/**
 * The interface to OpenGL vertex array objects.
 */

public interface JCGLArrayObjectsType
{
  /**
   * Retrieve a new array object builder. The returned builder can be used
   * indefinitely but is tied to the {@link JCGLContextType} upon which it was
   * created.
   *
   * @return A new array object builder
   *
   * @throws JCGLException On OpenGL errors
   */

  JCGLArrayObjectBuilderType arrayObjectNewBuilder()
    throws JCGLException;

  /**
   * @param o An array object
   *
   * @return A new array object builder, initialized to the values used to
   * construct {@code o}
   *
   * @see #arrayObjectNewBuilder()
   */

  JCGLArrayObjectBuilderType arrayObjectNewBuilderFromObject(
    JCGLArrayObjectUsableType o);

  /**
   * <p>Allocate and bind an array object based on the values given in {@code
   * b}.</p>
   *
   * <p>Calling this method will unbind any currently bound array buffer.</p>
   * <p>Calling this method will unbind any currently bound array object.</p>
   *
   * @param b The array object builder
   *
   * @return A new array object
   *
   * @throws JCGLException On OpenGL errors
   * @see JCGLArrayBuffersType#arrayBufferUnbind()
   */

  JCGLArrayObjectType arrayObjectAllocate(
    JCGLArrayObjectBuilderType b)
    throws JCGLException;

  /**
   * @return The currently bound array object, or {@link #arrayObjectGetDefault()}
   * if the user has not bound an array object.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  JCGLArrayObjectUsableType arrayObjectGetCurrentlyBound()
    throws JCGLException;

  /**
   * Bind the given array object.
   *
   * @param a The array object
   *
   * @throws JCGLException        Iff an OpenGL error occurs
   * @throws JCGLExceptionDeleted Iff the given array object has already been
   *                              deleted
   */

  void arrayObjectBind(
    JCGLArrayObjectUsableType a)
    throws JCGLException, JCGLExceptionDeleted;

  /**
   * Unbind the current array object. If no object is bound, this call has no
   * effect.
   *
   * @throws JCGLException Iff an OpenGL error occurs
   */

  void arrayObjectUnbind()
    throws JCGLException;

  /**
   * <p>Deletes the object referenced by {@code a}.</p>
   *
   * <p>Calling this method will unbind {@code a} iff it is bound.</p>
   *
   * @param a The array buffer
   *
   * @throws JCGLExceptionDeleted            If the object is already deleted
   * @throws JCGLException                   Iff an OpenGL error occurs
   * @throws JCGLExceptionObjectNotDeletable Iff the object is the default array
   *                                         object
   * @see #arrayObjectGetDefault()
   */

  void arrayObjectDelete(
    JCGLArrayObjectType a)
    throws JCGLException, JCGLExceptionDeleted, JCGLExceptionObjectNotDeletable;

  /**
   * A default array object is provided in order to allow for code to create and
   * manipulate {@link com.io7m.jcanephora.core.JCGLIndexBufferType} values
   * without needing to explicitly create an array object just for this
   * purpose.
   *
   * @return A reference to the default array object
   */

  JCGLArrayObjectUsableType arrayObjectGetDefault();


}
