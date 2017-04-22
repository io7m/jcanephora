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

package com.io7m.jcanephora.core;

import net.jcip.annotations.ThreadSafe;

import java.util.Set;

/**
 * <p>The type of resources that can contain other resources.</p>
 *
 * <p>This is primarily used to track resources that can be deleted: When an
 * object is deleted by the programmer in OpenGL, the storage for an object
 * {@code X} is only deleted if there are no other non-deleted objects that have
 * references to {@code X}.</p>
 *
 * <p>As an example: Assume a non-deleted array object {@code A} and a freshly
 * created index buffer {@code I} that is not referenced by any other object.The
 * programmer binds {@code A}, and then binds a non-deleted index buffer {@code
 * I}. {@code A} now has a reference to {@code I}. If the programmer deletes
 * {@code I}, the underlying storage for {@code I} will not be deleted until the
 * programmer either unbinds {@code I} (breaking the reference) or deletes
 * {@code A} (destroying the container).</p>
 */

@ThreadSafe
public interface JCGLReferenceContainerType
{
  /**
   * @return A read-only view of the set of objects to which this container has
   * references
   */

  Set<JCGLReferableType> references();
}
