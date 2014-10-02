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

package com.io7m.jcanephora.api;

import java.util.Map;

import com.io7m.jcanephora.FramebufferColorAttachmentPointType;
import com.io7m.jcanephora.FramebufferColorAttachmentType;
import com.io7m.jcanephora.FramebufferDepthAttachmentType;
import com.io7m.jcanephora.FramebufferDepthStencilAttachmentType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.FramebufferStencilAttachmentType;
import com.io7m.jfunctional.OptionType;

/**
 * The type of read-only interfaces to mutable builders.
 */

public interface JCGLFramebufferBuilderReadableType
{
  /**
   * @return A read-only map of the current color attachments, by attachment
   *         point.
   */

    Map<FramebufferColorAttachmentPointType, FramebufferColorAttachmentType>
    getColorAttachments();

  /**
   * @return The current depth attachment, if any.
   */

  OptionType<FramebufferDepthAttachmentType> getDepthAttachment();

  /**
   * @return The current depth+stencil attachment, if any.
   */

    OptionType<FramebufferDepthStencilAttachmentType>
    getDepthStencilAttachment();

  /**
   * @return A read-only map of the associations between draw buffers and
   *         color attachments.
   */

    Map<FramebufferDrawBufferType, FramebufferColorAttachmentPointType>
    getDrawBufferMappings();

  /**
   * @return The current stencil attachment, if any.
   */

  OptionType<FramebufferStencilAttachmentType> getStencilAttachment();
}
