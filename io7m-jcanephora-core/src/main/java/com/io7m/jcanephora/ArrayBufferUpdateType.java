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

/**
 * <p>
 * The type of array buffer updates.
 * </p>
 * <p>
 * An array buffer update is essentially a mutable region of memory that will
 * be used to replace part of (or the entirety of) an array buffer.
 * </p>
 */

public interface ArrayBufferUpdateType
{
  /**
   * @return The array buffer to which the given data will be uploaded.
   */

  ArrayBufferUsableType getArrayBuffer();

  /**
   * @return A cursor that may only point to elements of the attribute
   *         <code>attribute_name</code> in the array data. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access
   *         (attribute existence and types are checked once on cursor
   *         creation).
   * 
   * @param attribute_name
   *          The name of the attribute.
   * @throws JCGLMissingAttributeException
   *           If the given attribute does not exist.
   * @throws JCGLTypingException
   *           If the attribute does not have the correct type for the cursor.
   */

  CursorWritable2fType getCursor2f(
    String attribute_name)
    throws JCGLMissingAttributeException,
      JCGLTypingException;

  /**
   * @return A cursor that may only point to elements of the attribute
   *         <code>attribute_name</code> in the array data. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access
   *         (attribute existence and types are checked once on cursor
   *         creation).
   * 
   * @param attribute_name
   *          The name of the attribute.
   * @throws JCGLMissingAttributeException
   *           If the given attribute does not exist.
   * @throws JCGLTypingException
   *           If the attribute does not have the correct type for the cursor.
   */

  CursorWritable3fType getCursor3f(
    String attribute_name)
    throws JCGLMissingAttributeException,
      JCGLTypingException;

  /**
   * @return A cursor that may only point to elements of the attribute
   *         <code>attribute_name</code> in the array data. The cursor
   *         interface allows constant time access to any element and also
   *         minimizes the number of checks performed for each access
   *         (attribute existence and types are checked once on cursor
   *         creation).
   * 
   * @param attribute_name
   *          The name of the attribute.
   * @throws JCGLMissingAttributeException
   *           If the given attribute does not exist.
   * @throws JCGLTypingException
   *           If the attribute does not have the correct type for the cursor.
   */

  CursorWritable4fType getCursor4f(
    String attribute_name)
    throws JCGLMissingAttributeException,
      JCGLTypingException;
}
