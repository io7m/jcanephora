/*
 * Copyright © 2012 http://io7m.com
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

/**
 * The result of validating a given framebuffer configuration.
 */

public enum FramebufferStatus
{
  FRAMEBUFFER_STATUS_COMPLETE,

  /**
   * One or more of the framebuffer attachments are "framebuffer incomplete".
   */

  FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_ATTACHMENT,

  /**
   * The framebuffer does not have any attached images.
   */

  FRAMEBUFFER_STATUS_ERROR_MISSING_IMAGE_ATTACHMENT,

  /**
   * One of the framebuffer attachments selected for drawing is nonexistent.
   */

  FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_DRAW_BUFFER,

  /**
   * One of the framebuffer attachments selected for reading is nonexistent.
   */

  FRAMEBUFFER_STATUS_ERROR_INCOMPLETE_READ_BUFFER,

  /**
   * The current combination of attached image formats is unsupported by the
   * implementation.
   */

  FRAMEBUFFER_STATUS_ERROR_UNSUPPORTED,

  /**
   * Framebuffer validation failed for unknown reasons (this should never
   * occur).
   */

  FRAMEBUFFER_STATUS_ERROR_UNKNOWN
}
