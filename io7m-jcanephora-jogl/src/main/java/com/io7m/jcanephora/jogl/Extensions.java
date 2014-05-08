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

package com.io7m.jcanephora.jogl;

import javax.media.opengl.GLContext;

import com.io7m.jcanephora.api.JCGLNamedExtensionsType;
import com.io7m.jcanephora.api.JCGLSoftRestrictionsType;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class Extensions implements JCGLNamedExtensionsType
{
  private final GLContext                context;
  private final LogUsableType            log;
  private final StringBuilder            message;
  private final JCGLSoftRestrictionsType restrictions;

  Extensions(
    final GLContext context1,
    final JCGLSoftRestrictionsType restrictions1,
    final LogUsableType log1)
  {
    this.message = new StringBuilder();
    this.context = context1;
    this.restrictions = restrictions1;
    this.log = log1;
  }

  @Override public boolean extensionIsSupported(
    final String name)
  {
    NullCheck.notNull(name, "Name");
    return this.context.isExtensionAvailable(name);
  }

  @Override public boolean extensionIsVisible(
    final String name)
  {
    final boolean supported = this.extensionIsSupported(name);

    if (supported) {
      if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
        this.message.setLength(0);
        this.message.append("Extension ");
        this.message.append(name);
        this.message.append(" is supported");
        this.log.debug(this.message.toString());
      }

      final boolean visible =
        this.restrictions.restrictExtensionVisibility(name);

      if (!visible) {
        if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
          this.message.setLength(0);
          this.message.append("Extension ");
          this.message.append(name);
          this.message.append(" is hidden by soft restrictions");
          this.log.debug(this.message.toString());
        }
      }

      return visible;
    }

    if (this.log.wouldLog(LogLevel.LOG_DEBUG)) {
      this.message.setLength(0);
      this.message.append("Extension ");
      this.message.append(name);
      this.message.append(" is not supported");
      this.log.debug(this.message.toString());
    }

    return supported;
  }
}
