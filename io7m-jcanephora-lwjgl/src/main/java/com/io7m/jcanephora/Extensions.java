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

import java.util.Set;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Level;
import com.io7m.jlog.Log;

final class Extensions implements JCGLNamedExtensions
{
  private final @Nonnull StringBuilder        message;
  private final @Nonnull Log                  log;
  private final @Nonnull JCGLSoftRestrictions restrictions;
  private final @Nonnull Set<String>          extension_set;

  Extensions(
    final @Nonnull Set<String> extension_set,
    final @Nonnull JCGLSoftRestrictions restrictions,
    final @Nonnull Log log)
  {
    this.message = new StringBuilder();
    this.restrictions = restrictions;
    this.extension_set = extension_set;
    this.log = log;
  }

  @Override public boolean extensionIsSupported(
    final @Nonnull String name)
    throws ConstraintError
  {
    Constraints.constrainNotNull(name, "Name");
    return this.extension_set.contains(name);
  }

  @Override public boolean extensionIsVisible(
    final @Nonnull String name)
    throws ConstraintError
  {
    final boolean supported = this.extensionIsSupported(name);

    if (supported) {
      if (this.log.enabled(Level.LOG_DEBUG)) {
        this.message.setLength(0);
        this.message.append("Extension ");
        this.message.append(name);
        this.message.append(" is supported");
        this.log.debug(this.message.toString());
      }

      final boolean visible =
        this.restrictions.restrictExtensionVisibility(name);

      if (!visible) {
        if (this.log.enabled(Level.LOG_DEBUG)) {
          this.message.setLength(0);
          this.message.append("Extension ");
          this.message.append(name);
          this.message.append(" is hidden by soft restrictions");
          this.log.debug(this.message.toString());
        }
      }

      return visible;
    }

    if (this.log.enabled(Level.LOG_DEBUG)) {
      this.message.setLength(0);
      this.message.append("Extension ");
      this.message.append(name);
      this.message.append(" is not supported");
      this.log.debug(this.message.toString());
    }

    return supported;
  }
}