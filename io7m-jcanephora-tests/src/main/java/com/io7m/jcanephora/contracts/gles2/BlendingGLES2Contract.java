package com.io7m.jcanephora.contracts.gles2;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Option;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.BlendEquationGLES2;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.GLBlendingCommon;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLImplementation;
import com.io7m.jcanephora.GLInterfaceGL3;
import com.io7m.jcanephora.GLInterfaceGLES2;
import com.io7m.jcanephora.GLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.contracts.common.TestContract;

public abstract class BlendingGLES2Contract implements TestContract
{
  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  static GLBlendingCommon getGLBlending(
    final @Nonnull TestContext context)
  {
    final GLImplementation gi = context.getGLImplementation();

    final Option<GLInterfaceGL3> opt3 = gi.getGL3();
    switch (opt3.type) {
      case OPTION_NONE:
      {
        break;
      }
      case OPTION_SOME:
      {
        final Some<GLInterfaceGL3> some = (Option.Some<GLInterfaceGL3>) opt3;
        return some.value;
      }
    }

    final Option<GLInterfaceGLES2> optes2 = gi.getGLES2();
    switch (optes2.type) {
      case OPTION_NONE:
      {
        break;
      }
      case OPTION_SOME:
      {
        final Some<GLInterfaceGLES2> some =
          (Option.Some<GLInterfaceGLES2>) optes2;
        return some.value;
      }
    }

    throw new UnreachableCodeException();
  }

  /**
   * Disabling blending works.
   */

  @Test public final void testBlendDisabled()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLBlendingCommon gl = BlendingGLES2Contract.getGLBlending(tc);

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
    final TestContext tc = this.newTestContext();
    final GLBlendingCommon gl = BlendingGLES2Contract.getGLBlending(tc);

    gl.blendingEnableWithEquationSeparateES2(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      BlendEquationGLES2.BLEND_EQUATION_ADD,
      BlendEquationGLES2.BLEND_EQUATION_ADD);
  }

  /**
   * ∀x y. blendingEnable (x, y).
   */

  @Test public final void testBlendFunctions()
    throws GLException,
      GLUnsupportedException,
      ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLBlendingCommon gl = BlendingGLES2Contract.getGLBlending(tc);
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
    final TestContext tc = this.newTestContext();
    final GLBlendingCommon gl = BlendingGLES2Contract.getGLBlending(tc);

    for (final BlendFunction source : BlendFunction.values()) {
      for (final BlendFunction target : BlendFunction.values()) {
        for (final BlendEquationGLES2 eq : BlendEquationGLES2.values()) {
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
    final TestContext tc = this.newTestContext();
    final GLBlendingCommon gl = BlendingGLES2Contract.getGLBlending(tc);

    for (final BlendFunction source : BlendFunction.values()) {
      for (final BlendFunction target : BlendFunction.values()) {
        for (final BlendEquationGLES2 eq_rgb : BlendEquationGLES2.values()) {
          for (final BlendEquationGLES2 eq_alp : BlendEquationGLES2.values()) {
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
    final TestContext tc = this.newTestContext();
    final GLBlendingCommon gl = BlendingGLES2Contract.getGLBlending(tc);
    // final Log log = this.getLog();

    for (final BlendFunction rgb_source : BlendFunction.values()) {
      for (final BlendFunction rgb_target : BlendFunction.values()) {
        for (final BlendFunction alp_source : BlendFunction.values()) {
          for (final BlendFunction alp_target : BlendFunction.values()) {
            for (final BlendEquationGLES2 eq_rgb : BlendEquationGLES2
              .values()) {
              for (final BlendEquationGLES2 eq_alp : BlendEquationGLES2
                .values()) {
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
    final TestContext tc = this.newTestContext();
    final GLBlendingCommon gl = BlendingGLES2Contract.getGLBlending(tc);

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
    final TestContext tc = this.newTestContext();
    final GLBlendingCommon gl = BlendingGLES2Contract.getGLBlending(tc);

    gl.blendingEnableWithEquationES2(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      BlendEquationGLES2.BLEND_EQUATION_ADD);

  }

  @Test(expected = ConstraintError.class) public
    void
    testBlendSeparateWithEquationSeparateTargetNotSaturate0()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLBlendingCommon gl = BlendingGLES2Contract.getGLBlending(tc);

    gl.blendingEnableSeparateWithEquationSeparateES2(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      BlendFunction.BLEND_ONE,
      BlendEquationGLES2.BLEND_EQUATION_ADD,
      BlendEquationGLES2.BLEND_EQUATION_ADD);
  }

  @Test(expected = ConstraintError.class) public
    void
    testBlendSeparateWithEquationSeparateTargetNotSaturate1()
      throws GLException,
        GLUnsupportedException,
        ConstraintError
  {
    final TestContext tc = this.newTestContext();
    final GLBlendingCommon gl = BlendingGLES2Contract.getGLBlending(tc);

    gl.blendingEnableSeparateWithEquationSeparateES2(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      BlendEquationGLES2.BLEND_EQUATION_ADD,
      BlendEquationGLES2.BLEND_EQUATION_ADD);
  }
}
