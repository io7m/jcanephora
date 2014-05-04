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

import java.nio.ByteBuffer;

/**
 * <p>
 * The type of index buffer <i>updates</i> that use mapped memory.
 * </p>
 */

public interface IndexBufferUpdateMappedType extends IndexBufferUpdateType
{
  /**
   * @return <p>
   *         The raw ByteBuffer that backs the index buffer. The memory
   *         backing the buffer is mapped into the application address space
   *         from the GPU. The function is provided for use by developers that
   *         have needs not addressed by the cursor API.
   *         </p>
   *         <p>
   *         Use of this buffer is discouraged for safety reasons. For
   *         example, if the buffer object is unmapped, then this
   *         {@link ByteBuffer} will point to invalid memory and attempting to
   *         read from or write to it will typically crash the virtual
   *         machine.
   *         </p>
   */

  ByteBuffer getByteBuffer();
}
