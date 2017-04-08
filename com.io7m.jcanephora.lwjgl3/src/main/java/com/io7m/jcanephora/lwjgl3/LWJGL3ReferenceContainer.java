/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jcanephora.core.JCGLReferenceContainerType;
import com.io7m.jnull.NullCheck;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

final class LWJGL3ReferenceContainer implements JCGLReferenceContainerType
{
  private final Set<JCGLReferableType> references_view;
  private final Set<JCGLReferableType> references;
  private final JCGLReferenceContainerType owner;

  LWJGL3ReferenceContainer(
    final JCGLReferenceContainerType in_owner,
    final int size)
  {
    this.owner = NullCheck.notNull(in_owner, "Owner");
    this.references = Collections.newSetFromMap(new ConcurrentHashMap<>(size));
    this.references_view = Collections.unmodifiableSet(this.references);
  }

  void referenceAdd(final LWJGL3Referable i)
  {
    this.references.add(i);
    i.containerReferenceAccept(this.owner);
  }

  void referenceRemove(final LWJGL3Referable i)
  {
    this.references.remove(i);
    i.containerReferenceRemove(this.owner);
  }

  @Override
  public Set<JCGLReferableType> getReferences()
  {
    return this.references_view;
  }
}
