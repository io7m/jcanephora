package com.io7m.jcanephora.contracts.gl3;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLRenderbuffersGL3;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.Renderbuffer;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.contracts.common.TestContract;

public abstract class RenderbufferGL3Contract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract GLRenderbuffersGL3 getGLRenderbuffers(
    TestContext tc);

  /**
   * Allocating all of the renderbuffer types works.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test public void testRenderbufferAllocate()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLRenderbuffersGL3 gr = this.getGLRenderbuffers(tc);

    final int width = 128;
    final int height = 128;

    for (final RenderbufferType t : RenderbufferType.values()) {
      Renderbuffer<?> rb = null;

      switch (t) {
        case RENDERBUFFER_DEPTH_24_STENCIL_8:
          rb = gr.renderbufferAllocateDepth24Stencil8(width, height);
          break;
        case RENDERBUFFER_COLOR_RGBA_8888:
          rb = gr.renderbufferAllocateRGBA8888(width, height);
          break;
        case RENDERBUFFER_COLOR_RGB_888:
          rb = gr.renderbufferAllocateRGB888(width, height);
          break;
        case RENDERBUFFER_COLOR_RGBA_4444:
        case RENDERBUFFER_COLOR_RGBA_5551:
        case RENDERBUFFER_COLOR_RGB_565:
        case RENDERBUFFER_DEPTH_16:
        case RENDERBUFFER_STENCIL_8:
          break;
      }

      assert rb != null;
      Assert.assertFalse(rb.resourceIsDeleted());
      gr.renderbufferDelete(rb);
      Assert.assertTrue(rb.resourceIsDeleted());
    }
  }

  /**
   * Deleting a renderbuffer works.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test public void testRenderbufferDelete()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLRenderbuffersGL3 gr = this.getGLRenderbuffers(tc);

    final Renderbuffer<?> rb = gr.renderbufferAllocateRGB888(128, 128);
    Assert.assertFalse(rb.resourceIsDeleted());
    gr.renderbufferDelete(rb);
    Assert.assertTrue(rb.resourceIsDeleted());
  }

  /**
   * Deleting a renderbuffer twice fails.
   * 
   * @throws ConstraintError
   * @throws GLException
   */

  @Test(expected = ConstraintError.class) public
    void
    testRenderbufferDeleteTwice()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLRenderbuffersGL3 gr = this.getGLRenderbuffers(tc);

    final Renderbuffer<?> rb = gr.renderbufferAllocateRGB888(128, 128);
    Assert.assertFalse(rb.resourceIsDeleted());
    gr.renderbufferDelete(rb);
    Assert.assertTrue(rb.resourceIsDeleted());
    gr.renderbufferDelete(rb);
  }

}
