package com.io7m.jcanephora;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Integer16;
import com.io7m.jaux.Integer24;
import com.io7m.jaux.UnreachableCodeException;

/**
 * Convenient functions for implementing byte buffer cursors.
 */

@Immutable final class ByteBufferCursor
{
  static void packInteger16(
    final @Nonnull ByteBuffer data,
    final int index,
    final int i)
  {
    byte[] buffer;
    if (data.order() == ByteOrder.BIG_ENDIAN) {
      buffer = Integer16.packBigEndian(i);
    } else {
      buffer = Integer16.packLittleEndian(i);
    }
    data.put(index + 0, buffer[0]);
    data.put(index + 1, buffer[1]);
  }

  static void packInteger24(
    final @Nonnull ByteBuffer data,
    final int index,
    final int i)
  {
    byte[] buffer;
    if (data.order() == ByteOrder.BIG_ENDIAN) {
      buffer = Integer24.packBigEndian(i);
    } else {
      buffer = Integer24.packLittleEndian(i);
    }
    data.put(index + 0, buffer[0]);
    data.put(index + 1, buffer[1]);
    data.put(index + 2, buffer[2]);
  }

  static int unpackInteger16(
    final @Nonnull ByteBuffer data,
    final @Nonnull byte[] buffer,
    final int index)
  {
    buffer[0] = data.get(index);
    buffer[1] = data.get(index + 1);

    if (data.order() == ByteOrder.BIG_ENDIAN) {
      return Integer16.unpackBigEndian(buffer);
    }
    return Integer16.unpackLittleEndian(buffer);
  }

  static int unpackInteger24(
    final @Nonnull ByteBuffer data,
    final @Nonnull byte[] buffer,
    final int index)
  {
    buffer[0] = data.get(index);
    buffer[1] = data.get(index + 1);
    buffer[2] = data.get(index + 2);

    if (data.order() == ByteOrder.BIG_ENDIAN) {
      return Integer24.unpackBigEndian(buffer);
    }
    return Integer24.unpackLittleEndian(buffer);
  }

  static long unpackUnsigned32(
    final @Nonnull ByteBuffer data,
    final int index)
  {
    final int x = data.getInt(index);
    if (x < 0) {
      return 0x100000000L + x;
    }
    return x;
  }

  static int unpackUnsigned8(
    final @Nonnull ByteBuffer data,
    final int index)
  {
    final int x = data.get(index);
    if (x < 0) {
      return 0x100 + x;
    }
    return x;
  }

  private ByteBufferCursor()
  {
    throw new UnreachableCodeException();
  }
}
