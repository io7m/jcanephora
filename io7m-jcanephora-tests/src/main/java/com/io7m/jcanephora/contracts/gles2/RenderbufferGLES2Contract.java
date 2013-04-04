package com.io7m.jcanephora.contracts.gles2;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLRenderbuffersGLES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.Renderbuffer;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.contracts.common.TestContract;

public abstract class RenderbufferGLES2Contract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  public abstract GLRenderbuffersGLES2 getGLRenderbuffers(
    TestContext tc);

  /**
   * Allocating all of the renderbuffer types works.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test public void testRenderbufferAllocate()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLRenderbuffersGLES2 gr = this.getGLRenderbuffers(tc);

    final int width = 128;
    final int height = 128;

    for (final RenderbufferType t : RenderbufferType.getES2Types()) {
      Renderbuffer<?> rb = null;

      switch (t) {
        case RENDERBUFFER_COLOR_RGBA_4444:
          rb = gr.renderbufferAllocateRGBA4444(width, height);
          break;
        case RENDERBUFFER_COLOR_RGBA_5551:
          rb = gr.renderbufferAllocateRGBA5551(width, height);
          break;
        case RENDERBUFFER_COLOR_RGB_565:
          rb = gr.renderbufferAllocateRGB565(width, height);
          break;
        case RENDERBUFFER_DEPTH_16:
          rb = gr.renderbufferAllocateDepth16(width, height);
          break;
        case RENDERBUFFER_STENCIL_8:
          rb = gr.renderbufferAllocateStencil8(width, height);
          break;
        case RENDERBUFFER_DEPTH_24_STENCIL_8:
        case RENDERBUFFER_COLOR_RGBA_8888:
        case RENDERBUFFER_COLOR_RGB_888:
          throw new UnreachableCodeException();
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
   *           , GLUnsupportedException
   */

  @Test public void testRenderbufferDelete()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLRenderbuffersGLES2 gr = this.getGLRenderbuffers(tc);

    final Renderbuffer<?> rb = gr.renderbufferAllocateRGB565(128, 128);
    Assert.assertFalse(rb.resourceIsDeleted());
    gr.renderbufferDelete(rb);
    Assert.assertTrue(rb.resourceIsDeleted());
  }

  /**
   * Deleting a renderbuffer twice fails.
   * 
   * @throws ConstraintError
   * @throws GLException
   *           , GLUnsupportedException
   */

  @Test(expected = ConstraintError.class) public
    void
    testRenderbufferDeleteTwice()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLRenderbuffersGLES2 gr = this.getGLRenderbuffers(tc);

    final Renderbuffer<?> rb = gr.renderbufferAllocateRGB565(128, 128);
    Assert.assertFalse(rb.resourceIsDeleted());
    gr.renderbufferDelete(rb);
    Assert.assertTrue(rb.resourceIsDeleted());
    gr.renderbufferDelete(rb);
  }
}
