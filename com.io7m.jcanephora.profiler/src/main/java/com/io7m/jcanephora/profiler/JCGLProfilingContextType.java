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

package com.io7m.jcanephora.profiler;

import com.io7m.jcanephora.core.JCGLTimerQueryUsableType;
import com.io7m.jcanephora.core.api.JCGLTimersType;

/**
 * A single profiling context.
 */

public interface JCGLProfilingContextType
{
  /**
   * @return The name of the context
   *
   * @see JCGLProfilingFrameType#childContext(String)
   * @see #childContext(String)
   */

  String contextName();

  /**
   * Create a new timer, or return the existing timer if this method has already
   * been called previously on this context.
   *
   * @return The OpenGL timer associated with the context
   */

  JCGLTimerQueryUsableType timer();

  /**
   * Create or return an existing child context with the given name.
   *
   * @param name The name of the child context
   *
   * @return A (possibly new) child context
   */

  JCGLProfilingContextType childContext(String name);

  /**
   * @return {@code true} iff profiling is enabled
   *
   * @see JCGLProfilingType#setEnabled(boolean)
   * @see JCGLProfilingType#isEnabled()
   */

  boolean isEnabled();

  /**
   * @return Access to the GL timers interface
   */

  JCGLTimersType timers();

  /**
   * @return {@code true} iff {@link #timer()} has ever been called for this
   * context
   */

  boolean hasTimer();

  /**
   * Start measuring the time if profiling is enabled.
   */

  default void startMeasuringIfEnabled()
  {
    // Checkstyle doesn't seem to understand "final" in interfaces
    // CHECKSTYLE:OFF
    if (this.isEnabled()) {
      final JCGLTimerQueryUsableType timer = this.timer();
      this.timers().timerQueryBegin(timer);
    }
    // CHECKSTYLE:ON
  }

  /**
   * Stop measuring the time if profiling is enabled.
   */

  default void stopMeasuringIfEnabled()
  {
    // Checkstyle doesn't seem to understand "final" in interfaces
    // CHECKSTYLE:OFF
    if (this.isEnabled()) {
      final JCGLTimerQueryUsableType timer = this.timer();
      this.timers().timerQueryFinish(timer);
    }
    // CHECKSTYLE:ON
  }
}
