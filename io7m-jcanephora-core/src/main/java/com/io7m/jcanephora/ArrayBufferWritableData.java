package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

public final class ArrayBufferWritableData
{
  protected final @Nonnull ArrayBuffer  buffer;
  protected final @Nonnull ByteBuffer   target_data;
  protected final long                  target_data_size;
  protected final long                  target_data_offset;
  private final @Nonnull RangeInclusive target_range;
  private final @Nonnull RangeInclusive range;

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
    this(buffer, buffer.getRange());
  }

  /**
   * Construct a buffer of data that will be used to replace elements in the
   * range <code>range</code> of the data in <code>buffer</code> on the GPU.
   * 
   * @param buffer
   *          The array buffer.
   * @param range
   *          The inclusive range defining the area of the array buffer that
   *          will be modified.
   * @throws ConstraintError
   *           Iff any of the following conditions hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           <li><code>range == null/code></li>
   *           <li><code>range.isIncludedIn(buffer.getRange()) == false</code>
   *           </li>
   *           </ul>
   */

  public ArrayBufferWritableData(
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull RangeInclusive range)
    throws ConstraintError
  {
    this.buffer = Constraints.constrainNotNull(buffer, "Array buffer");
    this.range = range;

    Constraints.constrainArbitrary(
      range.isIncludedIn(buffer.getRange()),
      "Given range is in for the given buffer");

    this.target_range =
      new RangeInclusive(0, this.buffer.getRange().getInterval() - 1);
    this.target_data_size =
      range.getInterval() * buffer.getElementSizeBytes();
    this.target_data_offset = range.getLower() * buffer.getElementSizeBytes();
    this.target_data =
      ByteBuffer.allocateDirect((int) this.target_data_size).order(
        ByteOrder.nativeOrder());
  }

  /**
   * Retrieve a cursor that may only point to elements of the attribute
   * <code>attribute_name</code> in the array data. The cursor interface
   * allows constant time access to any element and also minimizes the number
   * of checks performed for each access (attribute existence and types are
   * checked once on cursor creation).
   * 
   * @param attribute_name
   *          The name of the attribute.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>attribute_name == null</code>.</li>
   *           <li>
   *           <code>getAttribute(attribute_name).getElements() != 4</code></li>
   *           <li>
   *           <code>getAttribute(attribute_name).getType() != TYPE_FLOAT</code>
   *           </li>
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
      this.target_range,
      d.getAttributeOffset(attribute_name),
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
