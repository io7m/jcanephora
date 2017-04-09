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

package com.io7m.jcanephora.renderstate;

import com.io7m.jcanephora.core.JCGLDepthFunction;
import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.api.JCGLBlendingType;
import com.io7m.jcanephora.core.api.JCGLColorBufferMaskingType;
import com.io7m.jcanephora.core.api.JCGLCullingType;
import com.io7m.jcanephora.core.api.JCGLDepthBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLScissorType;
import com.io7m.jcanephora.core.api.JCGLStencilBuffersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jregions.core.unparameterized.areas.AreaL;
import com.io7m.junreachable.UnreachableCodeException;

import java.util.Optional;

/**
 * Functions to manipulate render states.
 */

public final class JCGLRenderStates
{
  private JCGLRenderStates()
  {
    throw new UnreachableCodeException();
  }

  /**
   * Activate the given render state.
   *
   * @param g An OpenGL interface
   * @param r A render state
   */

  public static void activate(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    NullCheck.notNull(g, "GL interface");
    NullCheck.notNull(r, "Render state");

    configureBlending(g, r);
    configureCulling(g, r);
    configureColorBufferMasking(g, r);
    configureDepth(g, r);
    configurePolygonMode(g, r);
    configureScissor(g, r);
    configureStencil(g, r);
  }

  private static void configurePolygonMode(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    g.polygonModes().polygonSetMode(r.polygonMode());
  }

  private static void configureStencil(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    final JCGLStencilBuffersType g_s = g.stencilBuffers();
    final JCGLStencilStateType s = r.stencilState();
    if (s.stencilStrict()) {
      if (s.stencilEnabled()) {
        g_s.stencilBufferEnable();
        configureStencilActual(g_s, s);
      } else {
        g_s.stencilBufferDisable();
      }
    } else {
      if (g_s.stencilBufferGetBits() > 0) {
        if (s.stencilEnabled()) {
          configureStencilActual(g_s, s);
          g_s.stencilBufferEnable();
        } else {
          g_s.stencilBufferDisable();
        }
      }
    }
  }

  private static void configureStencilActual(
    final JCGLStencilBuffersType g_s,
    final JCGLStencilStateType s)
  {
    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_FRONT,
      s.operationStencilFailFront(),
      s.operationDepthFailFront(),
      s.operationPassFront());
    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      s.operationStencilFailBack(),
      s.operationDepthFailBack(),
      s.operationPassBack());

    g_s.stencilBufferMask(
      JCGLFaceSelection.FACE_FRONT, s.writeMaskFrontFaces());
    g_s.stencilBufferMask(
      JCGLFaceSelection.FACE_BACK, s.writeMaskBackFaces());

    g_s.stencilBufferFunction(
      JCGLFaceSelection.FACE_FRONT,
      s.testFunctionFront(),
      s.testReferenceFront(),
      s.testMaskFront());
    g_s.stencilBufferFunction(
      JCGLFaceSelection.FACE_BACK,
      s.testFunctionBack(),
      s.testReferenceBack(),
      s.testMaskBack());
  }

  private static void configureBlending(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    final JCGLBlendingType g_b = g.blending();
    final Optional<JCGLBlendStateType> blend_opt = r.blendState();
    if (blend_opt.isPresent()) {
      final JCGLBlendStateType blend_state = blend_opt.get();
      g_b.blendingEnableSeparateWithEquationSeparate(
        blend_state.blendFunctionSourceRGB(),
        blend_state.blendFunctionSourceAlpha(),
        blend_state.blendFunctionTargetRGB(),
        blend_state.blendFunctionTargetAlpha(),
        blend_state.blendEquationRGB(),
        blend_state.blendEquationAlpha());
    } else {
      g_b.blendingDisable();
    }
  }

  private static void configureScissor(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    final JCGLScissorType g_s = g.scissor();
    final Optional<AreaL> scissor_opt = r.scissor();
    if (scissor_opt.isPresent()) {
      final AreaL area = scissor_opt.get();
      g_s.scissorEnable(area);
    } else {
      g_s.scissorDisable();
    }
  }

  private static void configureCulling(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    final JCGLCullingType g_c = g.culling();
    final Optional<JCGLCullingStateType> cull_opt = r.cullingState();
    if (cull_opt.isPresent()) {
      final JCGLCullingStateType cull_state = cull_opt.get();
      g_c.cullingEnable(
        cull_state.faceSelection(), cull_state.faceWindingOrder());
    } else {
      g_c.cullingDisable();
    }
  }

  private static void configureColorBufferMasking(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    final JCGLColorBufferMaskingType g_c = g.colorBufferMasking();
    final JCGLColorBufferMaskingStateType gs = r.colorBufferMaskingState();
    g_c.colorBufferMask(
      gs.red(), gs.green(), gs.blue(), gs.alpha());
  }

  private static void configureDepth(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    final JCGLDepthBuffersType g_d = g.depthBuffers();
    final JCGLDepthStateType ds = r.depthState();

    configureDepthWriting(g_d, ds);
    configureDepthClamping(g_d, ds);
    configureDepthTesting(g_d, ds);
  }

  private static void configureDepthWriting(
    final JCGLDepthBuffersType g_d,
    final JCGLDepthStateType ds)
  {
    /*
     * If depth writing should be enabled, attempt to enable it.
     */

    switch (ds.depthWrite()) {
      case DEPTH_WRITE_ENABLED:
        switch (ds.depthStrict()) {

          /*
           * If strict checking is required, enable without checking
           * that there is a depth buffer - the function will raise an exception
           * if there isn't.
           */

          case DEPTH_STRICT_ENABLED:
            g_d.depthBufferWriteEnable();
            break;

          /*
           * If strict checking is disabled, only enable writing if there
           * actually is a depth buffer.
           */

          case DEPTH_STRICT_DISABLED:
            if (g_d.depthBufferGetBits() > 0) {
              g_d.depthBufferWriteEnable();
            }
            break;
        }
        break;

      case DEPTH_WRITE_DISABLED:

        /*
         * Disabling depth writing when there is no depth buffer available,
         * even with strict checking enabled, is never an error.
         */

        if (g_d.depthBufferGetBits() > 0) {
          g_d.depthBufferWriteDisable();
        }
        break;
    }
  }

  private static void configureDepthTesting(
    final JCGLDepthBuffersType g_d,
    final JCGLDepthStateType ds)
  {
    /*
     * If depth testing should be enabled, attempt to enable it.
     */

    final Optional<JCGLDepthFunction> d_opt = ds.depthTest();
    if (d_opt.isPresent()) {
      switch (ds.depthStrict()) {

        /*
         * If strict checking is required, enable without checking
         * that there is a depth buffer - the function will raise an exception
         * if there isn't.
         */

        case DEPTH_STRICT_ENABLED:
          g_d.depthBufferTestEnable(d_opt.get());
          break;

        /*
         * If strict checking is disabled, only enable testing if there
         * actually is a depth buffer.
         */

        case DEPTH_STRICT_DISABLED:
          if (g_d.depthBufferGetBits() > 0) {
            g_d.depthBufferTestEnable(d_opt.get());
          }
          break;
      }
    } else {

      /*
       * Disabling depth testing when there is no depth buffer, even with
       * strict checking enabled, is never an error.
       */

      if (g_d.depthBufferGetBits() > 0) {
        g_d.depthBufferTestDisable();
      }
    }
  }

  private static void configureDepthClamping(
    final JCGLDepthBuffersType g_d,
    final JCGLDepthStateType ds)
  {
    /*
     * If depth clamping should be enabled, attempt to enable it.
     */

    switch (ds.depthClamp()) {
      case DEPTH_CLAMP_ENABLED:
        switch (ds.depthStrict()) {

          /*
           * If strict checking is required, enable without checking
           * that there is a depth buffer - the function will raise an exception
           * if there isn't.
           */

          case DEPTH_STRICT_ENABLED:
            g_d.depthClampingEnable();
            break;

          /*
           * If strict checking is disabled, only enable clamping if there
           * actually is a depth buffer.
           */

          case DEPTH_STRICT_DISABLED:
            if (g_d.depthBufferGetBits() > 0) {
              g_d.depthClampingEnable();
            }
            break;
        }
        break;

      case DEPTH_CLAMP_DISABLED:

        /*
         * Disabling depth clamping when there is no depth buffer, even with
         * strict checking enabled, is never an error.
         */

        if (g_d.depthBufferGetBits() > 0) {
          g_d.depthClampingDisable();
        }
    }
  }
}
