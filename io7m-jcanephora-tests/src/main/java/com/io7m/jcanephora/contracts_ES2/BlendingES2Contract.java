package com.io7m.jcanephora.contracts_ES2;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.BlendEquationES2;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;

public abstract class BlendingES2Contract implements GLES2TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Disabling blending works.
   */

  @Test public final void testBlendDisabled()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.blendingDisable();
    Assert.assertFalse(gl.blendingIsEnabled());
  }

  @Test(expected = ConstraintError.class) public
    void
    testBlendEnableWithEquationSeparateTargetNotSaturate()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.blendingEnableWithEquationSeparateES2(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      BlendEquationES2.BLEND_EQUATION_ADD,
      BlendEquationES2.BLEND_EQUATION_ADD);
  }

  /**
   * ∀x y. blendingEnable (x, y).
   */

  @Test public final void testBlendFunctions()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    // final Log log = this.getLog();

    for (final BlendFunction source : BlendFunction.values()) {
      for (final BlendFunction target : BlendFunction.values()) {
        if (target == BlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
          continue;
        }

        // log.debug("source: " + source);
        // log.debug("target: " + target);
        // log.debug("--");

        gl.blendingDisable();
        Assert.assertFalse(gl.blendingIsEnabled());
        gl.blendingEnable(source, target);
        Assert.assertTrue(gl.blendingIsEnabled());
      }
    }
  }

  /**
   * ∀a b c. blendingEnableWithEquationSeparate (a,b,c).
   */

  @Test public final void testBlendFunctionSeparateWithEquation()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    for (final BlendFunction source : BlendFunction.values()) {
      for (final BlendFunction target : BlendFunction.values()) {
        for (final BlendEquationES2 eq : BlendEquationES2.values()) {
          if (target == BlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
            continue;
          }

          gl.blendingDisable();
          Assert.assertFalse(gl.blendingIsEnabled());
          gl.blendingEnableWithEquationES2(source, target, eq);
          Assert.assertTrue(gl.blendingIsEnabled());
        }
      }
    }
  }

  /**
   * ∀a b c d. blendingEnableWithEquationSeparate (a,b,c,d).
   */

  @Test public final void testBlendFunctionSeparateWithEquationSeparate()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    for (final BlendFunction source : BlendFunction.values()) {
      for (final BlendFunction target : BlendFunction.values()) {
        for (final BlendEquationES2 eq_rgb : BlendEquationES2.values()) {
          for (final BlendEquationES2 eq_alp : BlendEquationES2.values()) {
            if (target == BlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
              continue;
            }

            gl.blendingDisable();
            Assert.assertFalse(gl.blendingIsEnabled());
            gl.blendingEnableWithEquationSeparateES2(
              source,
              target,
              eq_rgb,
              eq_alp);
            Assert.assertTrue(gl.blendingIsEnabled());
          }
        }
      }
    }
  }

  /**
   * ∀a b c d e f. blendingEnableSeparateWithEquationSeparate (a,b,c,d,e,f).
   */

  @Test public final void testBlendFunctionsSeparateWithEquationSeparate()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();
    // final Log log = this.getLog();

    for (final BlendFunction rgb_source : BlendFunction.values()) {
      for (final BlendFunction rgb_target : BlendFunction.values()) {
        for (final BlendFunction alp_source : BlendFunction.values()) {
          for (final BlendFunction alp_target : BlendFunction.values()) {
            for (final BlendEquationES2 eq_rgb : BlendEquationES2.values()) {
              for (final BlendEquationES2 eq_alp : BlendEquationES2.values()) {
                if (rgb_target == BlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
                  continue;
                }
                if (alp_target == BlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
                  continue;
                }

                // log.debug("rgb-source : " + rgb_source);
                // log.debug("rgb-target : " + rgb_target);
                // log.debug("alp-source : " + alp_source);
                // log.debug("alp-target : " + alp_target);
                // log.debug("eq_rgb     : " + eq_rgb);
                // log.debug("eq_alp     : " + eq_alp);
                // log.debug("--");

                gl.blendingDisable();
                Assert.assertFalse(gl.blendingIsEnabled());
                gl.blendingEnableSeparateWithEquationSeparateES2(
                  rgb_source,
                  alp_source,
                  rgb_target,
                  alp_target,
                  eq_rgb,
                  eq_alp);
                Assert.assertTrue(gl.blendingIsEnabled());
              }
            }
          }
        }
      }
    }
  }

  @Test(expected = ConstraintError.class) public
    void
    testBlendFunctionTargetNotSaturate()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.blendingEnable(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE);
  }

  @Test(expected = ConstraintError.class) public
    void
    testBlendFunctionWithEquationTargetNotSaturate()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.blendingEnableWithEquationES2(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      BlendEquationES2.BLEND_EQUATION_ADD);

  }

  @Test(expected = ConstraintError.class) public
    void
    testBlendSeparateWithEquationSeparateTargetNotSaturate0()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.blendingEnableSeparateWithEquationSeparateES2(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      BlendFunction.BLEND_ONE,
      BlendEquationES2.BLEND_EQUATION_ADD,
      BlendEquationES2.BLEND_EQUATION_ADD);
  }

  @Test(expected = ConstraintError.class) public
    void
    testBlendSeparateWithEquationSeparateTargetNotSaturate1()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.getTestContext();
    final GLInterfaceES2 gl =
      tc.getGLImplementation().implementationGetGLES2();

    gl.blendingEnableSeparateWithEquationSeparateES2(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      BlendEquationES2.BLEND_EQUATION_ADD,
      BlendEquationES2.BLEND_EQUATION_ADD);
  }
}
