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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Writable cursor addressing areas consisting of two-component elements. The
 * first component of the element is an unsigned fixed point value, and the
 * second component is an integer. An example of a texture type that conforms
 * to this assumption is the
 * {@link TextureType#TEXTURE_TYPE_DEPTH_24_STENCIL_8_4BPP} type.
 */

public interface SpatialCursorWritable2pfi extends SpatialCursor
{
  /**
   * Put the value <code>v</code> at the current cursor location and seek the
   * cursor to the next element iff there is one.
   * 
   * @throws ConstraintError
   *           Iff attempting to write to the cursor would write outside of
   *           the valid area for the cursor, or <code>v == null</code>.
   */

  void put2pfi(
    final @Nonnull PackedM2FI v)
    throws ConstraintError;
}
