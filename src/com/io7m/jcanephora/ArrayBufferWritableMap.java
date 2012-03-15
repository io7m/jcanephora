package com.io7m.jcanephora;

import java.nio.ByteBuffer;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

public final class ArrayBufferWritableMap
{
  private final @Nonnull ArrayBuffer buffer;
  private final @Nonnull ByteBuffer  map;

  ArrayBufferWritableMap(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ByteBuffer map)
    throws ConstraintError
  {
    this.buffer = Constraints.constrainNotNull(buffer, "Buffer");
    this.map = Constraints.constrainNotNull(map, "Map");
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
   *           <li><code>0 <= index < size == false</code>, where
   *           <code>size</code> is the number of elements in the mapped
   *           buffer.</li>
   *           </ul>
   */

  public @Nonnull ArrayBufferCursorWritable2f getCursor2f(
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

    return new ArrayBufferCursorWritable2f(
      d.getAttributeOffset(name),
      this,
      d);
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
   *           <li><code>0 <= index < size == false</code>, where
   *           <code>size</code> is the number of elements in the mapped
   *           buffer.</li>
   *           </ul>
   */

  public @Nonnull ArrayBufferCursorWritable3f getCursor3f(
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

    return new ArrayBufferCursorWritable3f(
      d.getAttributeOffset(name),
      this,
      d);
  }

}
