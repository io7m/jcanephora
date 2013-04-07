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

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentDepthStencilRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentSharedDepthRenderbuffer;
import com.io7m.jcanephora.AttachmentDepth.AttachmentSharedDepthStencilRenderbuffer;

public final class AttachmentDepthTest
{
  @SuppressWarnings("static-method") @Test public
    void
    testMetaDepthRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer<RenderableDepth> rb1 =
      new Renderbuffer<RenderableDepth>(
        RenderbufferType.RENDERBUFFER_DEPTH_16,
        1,
        128,
        128);
    final Renderbuffer<RenderableDepth> rb2 =
      new Renderbuffer<RenderableDepth>(
        RenderbufferType.RENDERBUFFER_DEPTH_16,
        2,
        128,
        128);

    final AttachmentDepthRenderbuffer a0 =
      new AttachmentDepthRenderbuffer(rb1);
    final AttachmentDepthRenderbuffer a1 =
      new AttachmentDepthRenderbuffer(rb1);
    final AttachmentDepthRenderbuffer a2 =
      new AttachmentDepthRenderbuffer(rb2);

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));
    Assert.assertFalse(a0.equals(a2));

    Assert.assertTrue(a0.hashCode() == a1.hashCode());
    Assert.assertFalse(a0.hashCode() == a2.hashCode());

    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertFalse(a0.toString().equals(a2.toString()));

    Assert.assertTrue(a0.getRenderbuffer() == rb1);
    Assert.assertTrue(a1.getRenderbuffer() == rb1);
    Assert.assertTrue(a2.getRenderbuffer() == rb2);

    Assert.assertTrue(a0.getRenderbufferWritable() == rb1);
    Assert.assertTrue(a1.getRenderbufferWritable() == rb1);
    Assert.assertTrue(a2.getRenderbufferWritable() == rb2);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testMetaDepthStencilRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer<RenderableDepthStencil> rb1 =
      new Renderbuffer<RenderableDepthStencil>(
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        1,
        128,
        128);
    final Renderbuffer<RenderableDepthStencil> rb2 =
      new Renderbuffer<RenderableDepthStencil>(
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        2,
        128,
        128);

    final AttachmentDepthStencilRenderbuffer a0 =
      new AttachmentDepthStencilRenderbuffer(rb1);
    final AttachmentDepthStencilRenderbuffer a1 =
      new AttachmentDepthStencilRenderbuffer(rb1);
    final AttachmentDepthStencilRenderbuffer a2 =
      new AttachmentDepthStencilRenderbuffer(rb2);

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));
    Assert.assertFalse(a0.equals(a2));

    Assert.assertTrue(a0.hashCode() == a1.hashCode());
    Assert.assertFalse(a0.hashCode() == a2.hashCode());

    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertFalse(a0.toString().equals(a2.toString()));

    Assert.assertTrue(a0.getRenderbuffer() == rb1);
    Assert.assertTrue(a1.getRenderbuffer() == rb1);
    Assert.assertTrue(a2.getRenderbuffer() == rb2);

    Assert.assertTrue(a0.getRenderbufferWritable() == rb1);
    Assert.assertTrue(a1.getRenderbufferWritable() == rb1);
    Assert.assertTrue(a2.getRenderbufferWritable() == rb2);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testMetaSharedDepthRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer<RenderableDepth> rb1 =
      new Renderbuffer<RenderableDepth>(
        RenderbufferType.RENDERBUFFER_DEPTH_16,
        1,
        128,
        128);
    final Renderbuffer<RenderableDepth> rb2 =
      new Renderbuffer<RenderableDepth>(
        RenderbufferType.RENDERBUFFER_DEPTH_16,
        2,
        128,
        128);

    final AttachmentSharedDepthRenderbuffer a0 =
      new AttachmentSharedDepthRenderbuffer(rb1);
    final AttachmentSharedDepthRenderbuffer a1 =
      new AttachmentSharedDepthRenderbuffer(rb1);
    final AttachmentSharedDepthRenderbuffer a2 =
      new AttachmentSharedDepthRenderbuffer(rb2);

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));
    Assert.assertFalse(a0.equals(a2));

    Assert.assertTrue(a0.hashCode() == a1.hashCode());
    Assert.assertFalse(a0.hashCode() == a2.hashCode());

    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertFalse(a0.toString().equals(a2.toString()));

    Assert.assertTrue(a0.getRenderbuffer() == rb1);
    Assert.assertTrue(a1.getRenderbuffer() == rb1);
    Assert.assertTrue(a2.getRenderbuffer() == rb2);
  }

  @SuppressWarnings("static-method") @Test public
    void
    testMetaSharedDepthStencilRenderbuffer()
      throws ConstraintError
  {
    final Renderbuffer<RenderableDepthStencil> rb1 =
      new Renderbuffer<RenderableDepthStencil>(
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        1,
        128,
        128);
    final Renderbuffer<RenderableDepthStencil> rb2 =
      new Renderbuffer<RenderableDepthStencil>(
        RenderbufferType.RENDERBUFFER_DEPTH_24_STENCIL_8,
        2,
        128,
        128);

    final AttachmentSharedDepthStencilRenderbuffer a0 =
      new AttachmentSharedDepthStencilRenderbuffer(rb1);
    final AttachmentSharedDepthStencilRenderbuffer a1 =
      new AttachmentSharedDepthStencilRenderbuffer(rb1);
    final AttachmentSharedDepthStencilRenderbuffer a2 =
      new AttachmentSharedDepthStencilRenderbuffer(rb2);

    Assert.assertTrue(a0.equals(a0));
    Assert.assertTrue(a0.equals(a1));
    Assert.assertFalse(a0.equals(null));
    Assert.assertFalse(a0.equals(Integer.valueOf(23)));
    Assert.assertFalse(a0.equals(a2));

    Assert.assertTrue(a0.hashCode() == a1.hashCode());
    Assert.assertFalse(a0.hashCode() == a2.hashCode());

    Assert.assertTrue(a0.toString().equals(a1.toString()));
    Assert.assertFalse(a0.toString().equals(a2.toString()));

    Assert.assertTrue(a0.getRenderbuffer() == rb1);
    Assert.assertTrue(a1.getRenderbuffer() == rb1);
    Assert.assertTrue(a2.getRenderbuffer() == rb2);
  }
}
