package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

public final class ArrayBufferWritableData
{
  protected final @Nonnull ArrayBuffer buffer;
  protected final long                 element_start;
  protected final long                 element_count;

  protected final @Nonnull ByteBuffer  target_data;
  protected final long                 target_data_size;
  protected final long                 target_data_offset;

  /**
   * Construct a buffer of data that will be used to replace the entirety of
   * the data in <code>buffer</code> on the GPU.
   * 
   * @param buffer
   *          The array buffer.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           </ul>
   */

  public ArrayBufferWritableData(
    final @Nonnull ArrayBuffer buffer)
    throws ConstraintError
  {
    this.buffer = Constraints.constrainNotNull(buffer, "Array buffer");
    this.element_start = 0;
    this.element_count = buffer.getElements();
    this.checkElementRange();

    this.target_data_size = buffer.getSizeBytes();
    this.target_data_offset = 0;
    this.target_data =
      ByteBuffer.allocateDirect((int) this.target_data_size).order(
        ByteOrder.nativeOrder());
  }

  /**
   * Construct a buffer of data that will be used to replace
   * <code>element_count</code> elements of the data in <code>buffer</code> on
   * the GPU, starting at element <code>element_start</code>.
   * 
   * @param buffer
   *          The array buffer.
   * @param element_start
   *          The first element to replace.
   * @param element_count
   *          The number of elements to replace.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           <li><code>element_count < 1</code></li>
   *           <li>
   *           <code>0 <= element_start < buffer.getElements() == false</code>
   *           </li>
   *           <li>
   *           <code>element_start + element_count <= buffer.getElements() == false</code>
   *           </li>
   *           </ul>
   */

  public ArrayBufferWritableData(
    final @Nonnull ArrayBuffer buffer,
    final long element_start,
    final long element_count)
    throws ConstraintError
  {
    this.buffer = Constraints.constrainNotNull(buffer, "Array buffer");
    this.element_start =
      Constraints.constrainRange(element_start, 0, buffer.getElements() - 1);
    this.element_count =
      Constraints.constrainRange(element_count, 1, buffer.getElements() - 1);
    this.checkElementRange();

    this.target_data_size = element_count * buffer.getElementSizeBytes();
    this.target_data_offset = element_start * buffer.getElementSizeBytes();
    this.target_data =
      ByteBuffer.allocateDirect((int) this.target_data_size).order(
        ByteOrder.nativeOrder());
  }

  private void checkElementRange()
    throws ConstraintError
  {
    Constraints.constrainArbitrary(
      (this.element_start + this.element_count) <= this.buffer.getElements(),
      "Element start + count");
  }

  /**
   * Retrieve a cursor that may only point to elements of the attribute
   * <code>name</code> in the array data. The cursor interface allows constant
   * time access to any element and also minimizes the number of checks
   * performed for each access (attribute existence and types are checked once
   * on cursor creation).
   * 
   * @param name
   *          The name of the attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>name == null</code>.</li>
   *           <li><code>getAttribute(name).getElements() != 4</code></li>
   *           <li><code>getAttribute(name).getType() != TYPE_FLOAT</code></li>
   *           </ul>
   */

  public @Nonnull CursorWritable4f getCursor4f(
    final @Nonnull String attribute_name)
    throws ConstraintError
  {
    final ArrayBufferDescriptor d = this.buffer.getDescriptor();
    final ArrayBufferAttribute a = d.getAttribute(attribute_name);

    Constraints.constrainArbitrary(
      a.getElements() == 4,
      "Attribute has four elements");
    Constraints.constrainArbitrary(
      a.getType() == GLScalarType.TYPE_FLOAT,
      "Attribute elements are of type float");

    return new ByteBufferCursorWritable4f(
      this.target_data,
      d.getAttributeOffset(attribute_name),
      0,
      this.element_count - 1,
      d.getSize());
  }

  /**
   * Retrieve the data that will be used to update the array buffer.
   */

  @Nonnull ByteBuffer getTargetData()
  {
    return this.target_data;
  }

  /**
   * Return the offset in bytes of the area of the array buffer to be updated.
   */

  long getTargetDataOffset()
  {
    return this.target_data_offset;
  }

  /**
   * Return the size in bytes of the area of the array buffer to be updated.
   */

  long getTargetDataSize()
  {
    return this.target_data_size;
  }
}
