package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.RangeInclusive;

public final class IndexBufferWritableData
{
  protected final @Nonnull IndexBuffer  buffer;
  protected final @Nonnull ByteBuffer   target_data;
  protected final long                  target_data_size;
  protected final long                  target_data_offset;
  private final @Nonnull RangeInclusive range;
  private final RangeInclusive          target_range;

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

  public IndexBufferWritableData(
    final @Nonnull IndexBuffer buffer)
    throws ConstraintError
  {
    this(buffer, buffer.getRange());
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
   *           <li>
   *           <code>0 <= element_start < buffer.getElements() == false</code>
   *           </li>
   *           <li>
   *           <code>element_start + element_count <= buffer.getElements() == false</code>
   *           </li>
   *           </ul>
   */

  public IndexBufferWritableData(
    final @Nonnull IndexBuffer buffer,
    final @Nonnull RangeInclusive range)
    throws ConstraintError
  {
    this.buffer = Constraints.constrainNotNull(buffer, "Array buffer");
    this.range = range;

    Constraints.constrainArbitrary(
      range.isIncludedIn(buffer.getRange()),
      "Given range is in for the given buffer");

    this.target_range = new RangeInclusive(0, range.getInterval() - 1);
    this.target_data_size =
      range.getInterval() * buffer.getElementSizeBytes();
    this.target_data_offset = range.getLower() * buffer.getElementSizeBytes();
    this.target_data =
      ByteBuffer.allocateDirect((int) this.target_data_size).order(
        ByteOrder.nativeOrder());
  }

  /**
   * Retrieve a cursor that points to elements of the index buffer. The cursor
   * interface allows constant time access to any element and also minimizes
   * the number of checks performed for each access.
   */

  public CursorWritableIndex getCursor()
  {
    return new ByteBufferCursorWritableIndex(
      this.target_data,
      this.target_range,
      this.buffer.getType());
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
