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

package com.io7m.jcanephora.jogl;

import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jcanephora.core.JCGLReferenceContainerType;
import com.jogamp.opengl.GLContext;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

abstract class JOGLReferable extends JOGLObjectShared
  implements JCGLReferableType
{
  private final Set<JCGLReferenceContainerType> references;
  private final Set<JCGLReferenceContainerType> references_view;

  JOGLReferable(
    final GLContext in_context,
    final int in_id)
  {
    super(in_context, in_id);
    this.references = Collections.newSetFromMap(new ConcurrentHashMap<>(8));
    this.references_view = Collections.unmodifiableSet(this.references);
  }

  @Override
  public final Set<JCGLReferenceContainerType> getReferringContainers()
  {
    return this.references_view;
  }

  final void containerReferenceAccept(final JCGLReferenceContainerType c)
  {
    this.references.add(c);
  }

  final void containerReferenceRemove(final JCGLReferenceContainerType c)
  {
    this.references.remove(c);
  }
}
