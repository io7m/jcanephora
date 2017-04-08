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
    g.getPolygonModes().polygonSetMode(r.getPolygonMode());
  }

  private static void configureStencil(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    final JCGLStencilBuffersType g_s = g.getStencilBuffers();
    final JCGLStencilStateType s = r.getStencilState();
    if (s.getStencilStrict()) {
      if (s.getStencilEnabled()) {
        g_s.stencilBufferEnable();
        configureStencilActual(g_s, s);
      } else {
        g_s.stencilBufferDisable();
      }
    } else {
      if (g_s.stencilBufferGetBits() > 0) {
        if (s.getStencilEnabled()) {
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
      s.getOperationStencilFailFront(),
      s.getOperationDepthFailFront(),
      s.getOperationPassFront());
    g_s.stencilBufferOperation(
      JCGLFaceSelection.FACE_BACK,
      s.getOperationStencilFailBack(),
      s.getOperationDepthFailBack(),
      s.getOperationPassBack());

    g_s.stencilBufferMask(
      JCGLFaceSelection.FACE_FRONT, s.getWriteMaskFrontFaces());
    g_s.stencilBufferMask(
      JCGLFaceSelection.FACE_BACK, s.getWriteMaskBackFaces());

    g_s.stencilBufferFunction(
      JCGLFaceSelection.FACE_FRONT,
      s.getTestFunctionFront(),
      s.getTestReferenceFront(),
      s.getTestMaskFront());
    g_s.stencilBufferFunction(
      JCGLFaceSelection.FACE_BACK,
      s.getTestFunctionBack(),
      s.getTestReferenceBack(),
      s.getTestMaskBack());
  }

  private static void configureBlending(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    final JCGLBlendingType g_b = g.getBlending();
    final Optional<JCGLBlendStateType> blend_opt = r.getBlendState();
    if (blend_opt.isPresent()) {
      final JCGLBlendStateType blend_state = blend_opt.get();
      g_b.blendingEnableSeparateWithEquationSeparate(
        blend_state.getBlendFunctionSourceRGB(),
        blend_state.getBlendFunctionSourceAlpha(),
        blend_state.getBlendFunctionTargetRGB(),
        blend_state.getBlendFunctionTargetAlpha(),
        blend_state.getBlendEquationRGB(),
        blend_state.getBlendEquationAlpha());
    } else {
      g_b.blendingDisable();
    }
  }

  private static void configureScissor(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    final JCGLScissorType g_s = g.getScissor();
    final Optional<AreaL> scissor_opt = r.getScissor();
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
    final JCGLCullingType g_c = g.getCulling();
    final Optional<JCGLCullingStateType> cull_opt = r.getCullingState();
    if (cull_opt.isPresent()) {
      final JCGLCullingStateType cull_state = cull_opt.get();
      g_c.cullingEnable(
        cull_state.getFaceSelection(), cull_state.getFaceWindingOrder());
    } else {
      g_c.cullingDisable();
    }
  }

  private static void configureColorBufferMasking(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    final JCGLColorBufferMaskingType g_c = g.getColorBufferMasking();
    final JCGLColorBufferMaskingStateType gs = r.getColorBufferMaskingState();
    g_c.colorBufferMask(
      gs.getRed(), gs.getGreen(), gs.getBlue(), gs.getAlpha());
  }

  private static void configureDepth(
    final JCGLInterfaceGL33Type g,
    final JCGLRenderStateType r)
  {
    final JCGLDepthBuffersType g_d = g.getDepthBuffers();
    final JCGLDepthStateType ds = r.getDepthState();

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

    switch (ds.getDepthWrite()) {
      case DEPTH_WRITE_ENABLED:
        switch (ds.getDepthStrict()) {

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

    final Optional<JCGLDepthFunction> d_opt = ds.getDepthTest();
    if (d_opt.isPresent()) {
      switch (ds.getDepthStrict()) {

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

    switch (ds.getDepthClamp()) {
      case DEPTH_CLAMP_ENABLED:
        switch (ds.getDepthStrict()) {

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
