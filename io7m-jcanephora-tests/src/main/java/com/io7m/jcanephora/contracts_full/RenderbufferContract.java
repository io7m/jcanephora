package com.io7m.jcanephora.contracts_full;

import junit.framework.Assert;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface3;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.Renderbuffer;
import com.io7m.jcanephora.RenderbufferType;
import com.io7m.jcanephora.TestContext;

public abstract class RenderbufferContract implements GLTestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

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
    final TestContext tc = this.getTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    final int width = 128;
    final int height = 128;

    for (final RenderbufferType t : RenderbufferType.values()) {
      Renderbuffer rb = null;

      switch (t) {
        case RENDERBUFFER_COLOR_RGBA_4444:
          rb = gl.renderbufferAllocateRGBA4444(width, height);
          break;
        case RENDERBUFFER_COLOR_RGBA_5551:
          rb = gl.renderbufferAllocateRGBA5551(width, height);
          break;
        case RENDERBUFFER_COLOR_RGB_565:
          rb = gl.renderbufferAllocateRGB565(width, height);
          break;
        case RENDERBUFFER_DEPTH_16:
          rb = gl.renderbufferAllocateDepth16(width, height);
          break;
        case RENDERBUFFER_DEPTH_24_STENCIL_8:
          rb = gl.renderbufferAllocateDepth24Stencil8(width, height);
          break;
        case RENDERBUFFER_STENCIL_8:
          rb = gl.renderbufferAllocateStencil8(width, height);
          break;
        case RENDERBUFFER_COLOR_RGBA_8888:
          rb = gl.renderbufferAllocateRGBA8888(width, height);
          break;
        case RENDERBUFFER_COLOR_RGB_888:
          rb = gl.renderbufferAllocateRGB888(width, height);
          break;
      }

      assert rb != null;
      Assert.assertFalse(rb.resourceIsDeleted());
      rb.resourceDelete(gl);
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
    final TestContext tc = this.getTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    final Renderbuffer rb = gl.renderbufferAllocateRGB565(128, 128);
    Assert.assertFalse(rb.resourceIsDeleted());
    rb.resourceDelete(gl);
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
    final TestContext tc = this.getTestContext();
    final GLInterface3 gl = tc.getGLImplementation().implementationGetGL3();

    final Renderbuffer rb = gl.renderbufferAllocateRGB565(128, 128);
    Assert.assertFalse(rb.resourceIsDeleted());
    rb.resourceDelete(gl);
    Assert.assertTrue(rb.resourceIsDeleted());
    rb.resourceDelete(gl);
  }

}
