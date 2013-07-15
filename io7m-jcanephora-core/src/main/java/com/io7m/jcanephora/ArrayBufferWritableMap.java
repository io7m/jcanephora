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

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

/**
 * An array buffer, mapped in writable mode.
 */

public final class ArrayBufferWritableMap
{
  private final @Nonnull ArrayBuffer    buffer;
  private final @Nonnull ByteBuffer     map;
  private final @Nonnull RangeInclusive target_range;

  ArrayBufferWritableMap(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ByteBuffer map)
    throws ConstraintError
  {
    this.buffer = Constraints.constrainNotNull(buffer, "Buffer");
    this.map = Constraints.constrainNotNull(map, "Map");
    this.target_range =
      new RangeInclusive(0, this.buffer.getRange().getInterval() - 1);
  }

  /**
   * Retrieve the array buffer for this map.
   */

  public ArrayBuffer getArrayBuffer()
  {
    return this.buffer;
  }

  /**
   * Retrieve the raw ByteBuffer that backs the array buffer. The memory
   * backing the buffer is mapped into the application address space from the
   * GPU. The function is provided for use by developers that have needs not
   * addressed by the cursor API.
   * 
   * Use of this buffer is discouraged for safety reasons.
   */

  public @Nonnull ByteBuffer getByteBuffer()
  {
    return this.map;
  }

  /**
   * Retrieve a cursor that may only point to elements of the attribute
   * <code>name</code> in the map. The cursor interface allows constant time
   * access to any element and also minimizes the number of checks performed
   * for each access (attribute existence and types are checked once on cursor
   * creation).
   * 
   * @param name
   *          The name of the attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>name == null</code>.</li>
   *           <li><code>getAttribute(name).getElements() != 2</code></li>
   *           <li><code>getAttribute(name).getType() != TYPE_FLOAT</code></li>
   *           </ul>
   */

  public @Nonnull CursorWritable2f getCursor2f(
    final @Nonnull String name)
    throws ConstraintError
  {
    final ArrayBufferDescriptor d = this.getArrayBuffer().getDescriptor();
    final ArrayBufferAttribute a = d.getAttribute(name);

    Constraints.constrainArbitrary(
      a.getElements() == 2,
      "Attribute is of type Vector2f");
    Constraints.constrainArbitrary(
      a.getType() == GLScalarType.TYPE_FLOAT,
      "Attribute is of type Vector2f");

    return new ByteBufferCursorWritable2f(
      this.map,
      this.target_range,
      d.getAttributeOffset(name),
      d.getSize());
  }

  /**
   * Retrieve a cursor that may only point to elements of the attribute
   * <code>name</code> in the map. The cursor interface allows constant time
   * access to any element and also minimizes the number of checks performed
   * for each access (attribute existence and types are checked once on cursor
   * creation).
   * 
   * @param name
   *          The name of the attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>name == null</code>.</li>
   *           <li><code>getAttribute(name).getElements() != 3</code></li>
   *           <li><code>getAttribute(name).getType() != TYPE_FLOAT</code></li>
   *           </ul>
   */

  public @Nonnull CursorWritable3f getCursor3f(
    final @Nonnull String name)
    throws ConstraintError
  {
    final ArrayBufferDescriptor d = this.getArrayBuffer().getDescriptor();
    final ArrayBufferAttribute a = d.getAttribute(name);

    Constraints.constrainArbitrary(
      a.getElements() == 3,
      "Attribute is of type Vector3f");
    Constraints.constrainArbitrary(
      a.getType() == GLScalarType.TYPE_FLOAT,
      "Attribute is of type Vector3f");

    return new ByteBufferCursorWritable3f(
      this.map,
      this.target_range,
      d.getAttributeOffset(name),
      d.getSize());
  }
}
