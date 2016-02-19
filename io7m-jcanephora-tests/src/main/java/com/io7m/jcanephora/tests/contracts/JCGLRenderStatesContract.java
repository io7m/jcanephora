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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jareas.core.AreaInclusiveUnsignedL;
import com.io7m.jcanephora.core.JCGLBlendEquation;
import com.io7m.jcanephora.core.JCGLBlendFunction;
import com.io7m.jcanephora.core.JCGLDepthFunction;
import com.io7m.jcanephora.core.JCGLExceptionNoDepthBuffer;
import com.io7m.jcanephora.core.JCGLFaceSelection;
import com.io7m.jcanephora.core.JCGLFaceWindingOrder;
import com.io7m.jcanephora.core.JCGLPolygonMode;
import com.io7m.jcanephora.core.JCGLStencilFunction;
import com.io7m.jcanephora.core.JCGLStencilOperation;
import com.io7m.jcanephora.core.api.JCGLBlendingType;
import com.io7m.jcanephora.core.api.JCGLColorBufferMaskingType;
import com.io7m.jcanephora.core.api.JCGLCullingType;
import com.io7m.jcanephora.core.api.JCGLDepthBuffersType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.core.api.JCGLPolygonModesType;
import com.io7m.jcanephora.core.api.JCGLScissorType;
import com.io7m.jcanephora.core.api.JCGLStencilBuffersType;
import com.io7m.jcanephora.renderstate.JCGLBlendStateMutable;
import com.io7m.jcanephora.renderstate.JCGLCullingStateMutable;
import com.io7m.jcanephora.renderstate.JCGLDepthStateMutable;
import com.io7m.jcanephora.renderstate.JCGLRenderStateMutable;
import com.io7m.jcanephora.renderstate.JCGLRenderStates;
import com.io7m.jcanephora.renderstate.JCGLStencilStateMutable;
import com.io7m.junsigned.ranges.UnsignedRangeInclusiveL;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

/**
 * Render states contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLRenderStatesContract extends JCGLContract
{
  @Rule public ExpectedException expected = ExpectedException.none();

  protected abstract JCGLInterfaceGL33Type getGL33(
    String name,
    int depth_bits,
    int stencil_bits);

  @Test
  public final void testInitialState()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 24, 8);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();
    JCGLRenderStates.activate(g, r);

    final JCGLBlendingType g_b = g.getBlending();
    final JCGLCullingType g_c = g.getCulling();
    final JCGLDepthBuffersType g_d = g.getDepthBuffers();
    final JCGLColorBufferMaskingType g_cm = g.getColorBufferMasking();
    final JCGLPolygonModesType g_p = g.getPolygonModes();
    final JCGLScissorType g_s = g.getScissor();

    Assert.assertFalse(g_b.blendingIsEnabled());
    Assert.assertFalse(g_c.cullingIsEnabled());
    Assert.assertTrue(g_cm.colorBufferMaskStatusRed());
    Assert.assertTrue(g_cm.colorBufferMaskStatusGreen());
    Assert.assertTrue(g_cm.colorBufferMaskStatusBlue());
    Assert.assertTrue(g_cm.colorBufferMaskStatusAlpha());
    Assert.assertFalse(g_d.depthBufferTestIsEnabled());
    Assert.assertFalse(g_d.depthBufferWriteIsEnabled());
    Assert.assertFalse(g_d.depthClampingIsEnabled());
    Assert.assertEquals(JCGLPolygonMode.POLYGON_FILL, g_p.polygonGetMode());
    Assert.assertFalse(g_s.scissorIsEnabled());
  }

  @Test
  public final void testStencilNonStrict()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 24, 8);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLStencilStateMutable ss = JCGLStencilStateMutable.create();
    ss.setOperationDepthFailBack(JCGLStencilOperation.STENCIL_OP_DECREMENT);
    ss.setOperationDepthFailFront(JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP);
    ss.setOperationPassBack(JCGLStencilOperation.STENCIL_OP_INCREMENT);
    ss.setOperationPassFront(JCGLStencilOperation.STENCIL_OP_INCREMENT_WRAP);
    ss.setOperationStencilFailBack(JCGLStencilOperation.STENCIL_OP_INVERT);
    ss.setOperationStencilFailFront(JCGLStencilOperation.STENCIL_OP_KEEP);
    ss.setTestFunctionBack(JCGLStencilFunction.STENCIL_ALWAYS);
    ss.setTestFunctionFront(JCGLStencilFunction.STENCIL_EQUAL);
    ss.setTestMaskBack(0b1);
    ss.setTestMaskFront(0b11);
    ss.setTestReferenceBack(0b111);
    ss.setTestReferenceFront(0b1111);
    ss.setStencilStrict(false);
    ss.setStencilEnabled(true);

    r.setStencilState(ss);

    JCGLRenderStates.activate(g, r);

    final JCGLStencilBuffersType g_s = g.getStencilBuffers();

    Assert.assertTrue(g_s.stencilBufferIsEnabled());

    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      g_s.stencilBufferGetOperationDepthFailBack());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP,
      g_s.stencilBufferGetOperationDepthFailFront());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_INCREMENT,
      g_s.stencilBufferGetOperationPassBack());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_INCREMENT_WRAP,
      g_s.stencilBufferGetOperationPassFront());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_INVERT,
      g_s.stencilBufferGetOperationStencilFailBack());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_s.stencilBufferGetOperationStencilFailFront());

    Assert.assertEquals(
      JCGLStencilFunction.STENCIL_ALWAYS,
      g_s.stencilBufferGetFunctionBack());
    Assert.assertEquals(
      JCGLStencilFunction.STENCIL_EQUAL,
      g_s.stencilBufferGetFunctionFront());

    Assert.assertEquals(
      0b1L, (long) g_s.stencilBufferGetFunctionMaskBack());
    Assert.assertEquals(
      0b11L, (long) g_s.stencilBufferGetFunctionMaskFront());
    Assert.assertEquals(
      0b111L, (long) g_s.stencilBufferGetFunctionReferenceBack());
    Assert.assertEquals(
      0b1111L, (long) g_s.stencilBufferGetFunctionReferenceFront());
  }

  @Test
  public final void testStencilStrict()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 24, 8);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLStencilStateMutable ss = JCGLStencilStateMutable.create();
    ss.setOperationDepthFailBack(JCGLStencilOperation.STENCIL_OP_DECREMENT);
    ss.setOperationDepthFailFront(JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP);
    ss.setOperationPassBack(JCGLStencilOperation.STENCIL_OP_INCREMENT);
    ss.setOperationPassFront(JCGLStencilOperation.STENCIL_OP_INCREMENT_WRAP);
    ss.setOperationStencilFailBack(JCGLStencilOperation.STENCIL_OP_INVERT);
    ss.setOperationStencilFailFront(JCGLStencilOperation.STENCIL_OP_KEEP);
    ss.setTestFunctionBack(JCGLStencilFunction.STENCIL_ALWAYS);
    ss.setTestFunctionFront(JCGLStencilFunction.STENCIL_EQUAL);
    ss.setTestMaskBack(0b1);
    ss.setTestMaskFront(0b11);
    ss.setTestReferenceBack(0b111);
    ss.setTestReferenceFront(0b1111);
    ss.setStencilStrict(true);
    ss.setStencilEnabled(true);

    r.setStencilState(ss);

    JCGLRenderStates.activate(g, r);

    final JCGLStencilBuffersType g_s = g.getStencilBuffers();

    Assert.assertTrue(g_s.stencilBufferIsEnabled());

    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_DECREMENT,
      g_s.stencilBufferGetOperationDepthFailBack());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_DECREMENT_WRAP,
      g_s.stencilBufferGetOperationDepthFailFront());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_INCREMENT,
      g_s.stencilBufferGetOperationPassBack());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_INCREMENT_WRAP,
      g_s.stencilBufferGetOperationPassFront());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_INVERT,
      g_s.stencilBufferGetOperationStencilFailBack());
    Assert.assertEquals(
      JCGLStencilOperation.STENCIL_OP_KEEP,
      g_s.stencilBufferGetOperationStencilFailFront());

    Assert.assertEquals(
      JCGLStencilFunction.STENCIL_ALWAYS,
      g_s.stencilBufferGetFunctionBack());
    Assert.assertEquals(
      JCGLStencilFunction.STENCIL_EQUAL,
      g_s.stencilBufferGetFunctionFront());

    Assert.assertEquals(
      0b1L, (long) g_s.stencilBufferGetFunctionMaskBack());
    Assert.assertEquals(
      0b11L, (long) g_s.stencilBufferGetFunctionMaskFront());
    Assert.assertEquals(
      0b111L, (long) g_s.stencilBufferGetFunctionReferenceBack());
    Assert.assertEquals(
      0b1111L, (long) g_s.stencilBufferGetFunctionReferenceFront());
  }

  @Test
  public final void testBlend()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 24, 8);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLBlendStateMutable bs = JCGLBlendStateMutable.create();
    bs.setBlendEquationRGB(JCGLBlendEquation.BLEND_EQUATION_ADD);
    bs.setBlendEquationAlpha(JCGLBlendEquation.BLEND_EQUATION_MAXIMUM);
    bs.setBlendFunctionSourceAlpha(JCGLBlendFunction.BLEND_SOURCE_ALPHA);
    bs.setBlendFunctionSourceRGB(JCGLBlendFunction.BLEND_ONE);
    bs.setBlendFunctionTargetAlpha(JCGLBlendFunction.BLEND_CONSTANT_ALPHA);
    bs.setBlendFunctionTargetRGB(JCGLBlendFunction.BLEND_DESTINATION_COLOR);
    r.setBlendState(Optional.of(bs));

    JCGLRenderStates.activate(g, r);

    final JCGLBlendingType g_b = g.getBlending();
    Assert.assertTrue(g_b.blendingIsEnabled());
  }

  @Test
  public final void testCulling()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 24, 8);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLCullingStateMutable cs = JCGLCullingStateMutable.create();
    cs.setFaceSelection(JCGLFaceSelection.FACE_FRONT);
    cs.setFaceWindingOrder(JCGLFaceWindingOrder.FRONT_FACE_CLOCKWISE);
    r.setCullingState(Optional.of(cs));

    JCGLRenderStates.activate(g, r);

    final JCGLCullingType g_c = g.getCulling();
    Assert.assertTrue(g_c.cullingIsEnabled());
  }

  @Test
  public final void testScissor()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 24, 8);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    r.setScissor(Optional.of(AreaInclusiveUnsignedL.of(
      new UnsignedRangeInclusiveL(0L, 99L),
      new UnsignedRangeInclusiveL(0L, 99L))));

    JCGLRenderStates.activate(g, r);

    final JCGLScissorType g_s = g.getScissor();
    Assert.assertTrue(g_s.scissorIsEnabled());
  }

  @Test
  public final void testDepthTestWriteStrict()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 24, 8);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLDepthStateMutable ds = JCGLDepthStateMutable.create();
    ds.setDepthStrict(true);
    ds.setDepthClamp(true);
    ds.setDepthTest(Optional.of(JCGLDepthFunction.DEPTH_EQUAL));
    ds.setDepthWrite(true);
    r.setDepthState(ds);

    JCGLRenderStates.activate(g, r);

    final JCGLDepthBuffersType g_d = g.getDepthBuffers();
    Assert.assertTrue(g_d.depthBufferTestIsEnabled());
    Assert.assertTrue(g_d.depthBufferWriteIsEnabled());
    Assert.assertTrue(g_d.depthClampingIsEnabled());
  }

  @Test
  public final void testDepthTestWriteNonStrict()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 24, 8);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLDepthStateMutable ds = JCGLDepthStateMutable.create();
    ds.setDepthStrict(false);
    ds.setDepthClamp(true);
    ds.setDepthTest(Optional.of(JCGLDepthFunction.DEPTH_EQUAL));
    ds.setDepthWrite(true);
    r.setDepthState(ds);

    JCGLRenderStates.activate(g, r);

    final JCGLDepthBuffersType g_d = g.getDepthBuffers();
    Assert.assertTrue(g_d.depthBufferTestIsEnabled());
    Assert.assertTrue(g_d.depthBufferWriteIsEnabled());
    Assert.assertTrue(g_d.depthClampingIsEnabled());
  }

  @Test
  public final void testDepthStrictNoBufferTest()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 0, 0);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLDepthStateMutable ds = JCGLDepthStateMutable.create();
    ds.setDepthStrict(true);
    ds.setDepthClamp(false);
    ds.setDepthTest(Optional.of(JCGLDepthFunction.DEPTH_EQUAL));
    ds.setDepthWrite(false);
    r.setDepthState(ds);

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    JCGLRenderStates.activate(g, r);
  }

  @Test
  public final void testDepthStrictNoBufferWrite()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 0, 0);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLDepthStateMutable ds = JCGLDepthStateMutable.create();
    ds.setDepthStrict(true);
    ds.setDepthClamp(false);
    ds.setDepthTest(Optional.empty());
    ds.setDepthWrite(true);
    r.setDepthState(ds);

    this.expected.expect(JCGLExceptionNoDepthBuffer.class);
    JCGLRenderStates.activate(g, r);
  }

  @Test
  public final void testDepthStrictNoBuffer()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 0, 0);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLDepthStateMutable ds = JCGLDepthStateMutable.create();
    ds.setDepthStrict(true);
    ds.setDepthClamp(false);
    ds.setDepthTest(Optional.empty());
    ds.setDepthWrite(false);
    r.setDepthState(ds);

    JCGLRenderStates.activate(g, r);
  }

  @Test
  public final void testDepthNonStrictNoBufferTest()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 0, 0);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLDepthStateMutable ds = JCGLDepthStateMutable.create();
    ds.setDepthStrict(false);
    ds.setDepthClamp(false);
    ds.setDepthTest(Optional.of(JCGLDepthFunction.DEPTH_EQUAL));
    ds.setDepthWrite(false);
    r.setDepthState(ds);

    JCGLRenderStates.activate(g, r);
  }

  @Test
  public final void testDepthNonStrictNoBufferWrite()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 0, 0);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLDepthStateMutable ds = JCGLDepthStateMutable.create();
    ds.setDepthStrict(false);
    ds.setDepthClamp(false);
    ds.setDepthTest(Optional.empty());
    ds.setDepthWrite(true);
    r.setDepthState(ds);

    JCGLRenderStates.activate(g, r);
  }

  @Test
  public final void testDepthNonStrictNoBuffer()
  {
    final JCGLInterfaceGL33Type g = this.getGL33("main", 0, 0);
    final JCGLRenderStateMutable r = JCGLRenderStateMutable.create();

    final JCGLDepthStateMutable ds = JCGLDepthStateMutable.create();
    ds.setDepthStrict(false);
    ds.setDepthClamp(false);
    ds.setDepthTest(Optional.empty());
    ds.setDepthWrite(false);
    r.setDepthState(ds);

    JCGLRenderStates.activate(g, r);
  }
}
