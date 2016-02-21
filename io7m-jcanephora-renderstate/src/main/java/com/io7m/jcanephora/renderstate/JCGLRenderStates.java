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

import com.io7m.jareas.core.AreaInclusiveUnsignedLType;
import com.io7m.jcanephora.core.JCGLDepthFunction;
import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.api.JCGLBlendingType;
import com.io7m.jcanephora.core.api.JCGLColorBufferMaskingType;
import com.io7m.jcanephora.core.api.JCGLCullingType;
import com.io7m.jcanephora.core.api.JCGLDepthBuffersType;
import com.io7m.jcanephora.core.api.JCGLDepthClampingType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLScissorType;
import com.io7m.jcanephora.core.api.JCGLStencilBuffersType;
import com.io7m.jnull.NullCheck;
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
    NullCheck.notNull(g);
    NullCheck.notNull(r);

    JCGLRenderStates.configureBlending(g, r);
    JCGLRenderStates.configureCulling(g, r);
    JCGLRenderStates.configureColorBufferMasking(g, r);
    JCGLRenderStates.configureDepth(g, r);
    JCGLRenderStates.configureScissor(g, r);
    JCGLRenderStates.configureStencil(g, r);
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
        JCGLRenderStates.configureStencilActual(g_s, s);
      } else {
        g_s.stencilBufferDisable();
      }
    } else {
      if (g_s.stencilBufferGetBits() > 0) {
        if (s.getStencilEnabled()) {
          JCGLRenderStates.configureStencilActual(g_s, s);
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
    final Optional<AreaInclusiveUnsignedLType> scissor_opt = r.getScissor();
    if (scissor_opt.isPresent()) {
      final AreaInclusiveUnsignedLType area = scissor_opt.get();
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
    final JCGLDepthClampingType g_dc = g.getDepthBuffers();
    final JCGLDepthStateType ds = r.getDepthState();

    switch (ds.getDepthStrict()) {
      case DEPTH_STRICT_ENABLED: {

        /**
         * If there is no depth buffer, then only attempting to enable depth
         * writing, testing, or clamping is an error. Disabling it is not an
         * error, because it could never be enabled.
         */

        switch (ds.getDepthWrite()) {
          case DEPTH_WRITE_ENABLED:
            g_d.depthBufferWriteEnable();
            break;
          case DEPTH_WRITE_DISABLED:
            break;
        }

        final Optional<JCGLDepthFunction> dt_opt = ds.getDepthTest();
        if (dt_opt.isPresent()) {
          final JCGLDepthFunction dt = dt_opt.get();
          g_d.depthBufferTestEnable(dt);
        }

        switch (ds.getDepthClamp()) {
          case DEPTH_CLAMP_ENABLED:
            g_dc.depthClampingEnable();
            break;
          case DEPTH_CLAMP_DISABLED:
            break;
        }

        break;
      }
      case DEPTH_STRICT_DISABLED: {

        if (g_d.depthBufferGetBits() > 0) {

          switch (ds.getDepthWrite()) {
            case DEPTH_WRITE_ENABLED:
              g_d.depthBufferWriteEnable();
              break;
            case DEPTH_WRITE_DISABLED:
              g_d.depthBufferWriteDisable();
              break;
          }

          final Optional<JCGLDepthFunction> dt_opt = ds.getDepthTest();
          if (dt_opt.isPresent()) {
            final JCGLDepthFunction dt = dt_opt.get();
            g_d.depthBufferTestEnable(dt);
          } else {
            g_d.depthBufferTestDisable();
          }

          switch (ds.getDepthClamp()) {
            case DEPTH_CLAMP_ENABLED:
              g_dc.depthClampingEnable();
              break;
            case DEPTH_CLAMP_DISABLED:
              g_dc.depthClampingDisable();
              break;
          }
        }
        break;
      }
    }

  }
}
