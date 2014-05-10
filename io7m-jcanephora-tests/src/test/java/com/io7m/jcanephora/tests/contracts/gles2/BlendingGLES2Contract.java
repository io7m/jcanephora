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

package com.io7m.jcanephora.tests.contracts.gles2;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.BlendEquationGLES2;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionBlendingMisconfigured;
import com.io7m.jcanephora.api.JCGLBlendingCommonType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.contracts.TestContract;

@SuppressWarnings("null") public abstract class BlendingGLES2Contract implements
  TestContract
{
  static JCGLBlendingCommonType getGLBlending(
    final TestContext context)
  {
    final JCGLImplementationType gi = context.getGLImplementation();
    return gi.getGLCommon();
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  /**
   * Disabling blending works.
   */

  @Test public final void testBlendDisabled()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLBlendingCommonType gl = BlendingGLES2Contract.getGLBlending(tc);

    gl.blendingDisable();
    Assert.assertFalse(gl.blendingIsEnabled());
  }

  @Test(expected = JCGLExceptionBlendingMisconfigured.class) public
    void
    testBlendEnableWithEquationSeparateTargetNotSaturate()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLBlendingCommonType gl = BlendingGLES2Contract.getGLBlending(tc);

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
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLBlendingCommonType gl = BlendingGLES2Contract.getGLBlending(tc);
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
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLBlendingCommonType gl = BlendingGLES2Contract.getGLBlending(tc);

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
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLBlendingCommonType gl = BlendingGLES2Contract.getGLBlending(tc);

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
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLBlendingCommonType gl = BlendingGLES2Contract.getGLBlending(tc);
    // final Log log = this.getLog();

    for (final BlendFunction rgb_source : BlendFunction.values()) {
      for (final BlendFunction rgb_target : BlendFunction.values()) {
        for (final BlendFunction alp_source : BlendFunction.values()) {
          for (final BlendFunction alp_target : BlendFunction.values()) {

            if ((rgb_target != BlendFunction.BLEND_SOURCE_ALPHA_SATURATE)
              && (alp_target != BlendFunction.BLEND_SOURCE_ALPHA_SATURATE)) {
              gl.blendingDisable();
              Assert.assertFalse(gl.blendingIsEnabled());
              gl.blendingEnableSeparate(
                rgb_source,
                alp_source,
                rgb_target,
                alp_target);
              Assert.assertTrue(gl.blendingIsEnabled());
            }

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

  @Test(expected = JCGLExceptionBlendingMisconfigured.class) public
    void
    testBlendFunctionTargetNotSaturate()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLBlendingCommonType gl = BlendingGLES2Contract.getGLBlending(tc);

    gl.blendingEnable(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE);
  }

  @Test(expected = JCGLExceptionBlendingMisconfigured.class) public
    void
    testBlendFunctionWithEquationTargetNotSaturate()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLBlendingCommonType gl = BlendingGLES2Contract.getGLBlending(tc);

    gl.blendingEnableWithEquationES2(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      BlendEquationGLES2.BLEND_EQUATION_ADD);

  }

  @Test(expected = JCGLExceptionBlendingMisconfigured.class) public
    void
    testBlendSeparateWithEquationSeparateTargetNotSaturate0()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLBlendingCommonType gl = BlendingGLES2Contract.getGLBlending(tc);

    gl.blendingEnableSeparateWithEquationSeparateES2(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      BlendFunction.BLEND_ONE,
      BlendEquationGLES2.BLEND_EQUATION_ADD,
      BlendEquationGLES2.BLEND_EQUATION_ADD);
  }

  @Test(expected = JCGLExceptionBlendingMisconfigured.class) public
    void
    testBlendSeparateWithEquationSeparateTargetNotSaturate1()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLBlendingCommonType gl = BlendingGLES2Contract.getGLBlending(tc);

    gl.blendingEnableSeparateWithEquationSeparateES2(
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_ONE,
      BlendFunction.BLEND_SOURCE_ALPHA_SATURATE,
      BlendEquationGLES2.BLEND_EQUATION_ADD,
      BlendEquationGLES2.BLEND_EQUATION_ADD);
  }
}