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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.BlendEquationGL3;
import com.io7m.jcanephora.BlendEquationGLES2;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.JCGLExceptionBlendingMisconfigured;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.api.JCGLBlendingGL3Type;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class FakeBlending implements JCGLBlendingGL3Type
{
  private boolean            blending;
  private final FakeContext  context;
  private BlendFunction      current_destination_alpha_factor;
  private BlendFunction      current_destination_rgb_factor;
  private BlendEquationGL3   current_equation_alpha_gl3;
  private BlendEquationGLES2 current_equation_alpha_gles2;
  private BlendEquationGL3   current_equation_rgb_gl3;
  private BlendEquationGLES2 current_equation_rgb_gles2;
  private BlendFunction      current_source_alpha_factor;
  private BlendFunction      current_source_rgb_factor;
  private final LogType      log;

  FakeBlending(
    final FakeContext in_gl,
    final LogUsableType in_log)
  {
    this.context = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("blending");
    this.current_equation_rgb_gl3 = BlendEquationGL3.BLEND_EQUATION_ADD;
    this.current_equation_alpha_gl3 = BlendEquationGL3.BLEND_EQUATION_ADD;
    this.current_source_rgb_factor = BlendFunction.BLEND_ONE;
    this.current_source_alpha_factor = BlendFunction.BLEND_ONE;
    this.current_destination_alpha_factor = BlendFunction.BLEND_ONE;
    this.current_destination_rgb_factor = BlendFunction.BLEND_ONE;
    this.current_equation_alpha_gles2 = BlendEquationGLES2.BLEND_EQUATION_ADD;
    this.current_equation_rgb_gles2 = BlendEquationGLES2.BLEND_EQUATION_ADD;
  }

  @Override public void blendingDisable()
    throws JCGLExceptionRuntime
  {
    this.blending = false;
  }

  @Override public void blendingEnable(
    final BlendFunction source_factor,
    final BlendFunction destination_factor)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blendingEnableSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor);
  }

  @Override public void blendingEnableSeparate(
    final BlendFunction source_rgb_factor,
    final BlendFunction source_alpha_factor,
    final BlendFunction destination_rgb_factor,
    final BlendFunction destination_alpha_factor)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blendingEnableSeparateWithEquationSeparateES2(
      source_rgb_factor,
      source_alpha_factor,
      destination_rgb_factor,
      destination_alpha_factor,
      BlendEquationGLES2.BLEND_EQUATION_ADD,
      BlendEquationGLES2.BLEND_EQUATION_ADD);
  }

  @Override public void blendingEnableSeparateWithEquationSeparate(
    final BlendFunction source_rgb_factor,
    final BlendFunction source_alpha_factor,
    final BlendFunction destination_rgb_factor,
    final BlendFunction destination_alpha_factor,
    final BlendEquationGL3 equation_rgb,
    final BlendEquationGL3 equation_alpha)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    NullCheck.notNull(source_rgb_factor, "Source RGB factor");
    NullCheck.notNull(source_alpha_factor, "Source alpha factor");
    NullCheck.notNull(destination_rgb_factor, "Destination RGB factor");
    NullCheck.notNull(destination_alpha_factor, "Destination alpha factor");
    NullCheck.notNull(equation_rgb, "Equation RGB");
    NullCheck.notNull(equation_alpha, "Equation alpha");

    if (destination_rgb_factor == BlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
      throw new JCGLExceptionBlendingMisconfigured(
        "Destination RGB factor not SOURCE_ALPHA_SATURATE");
    }
    if (destination_alpha_factor == BlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
      throw new JCGLExceptionBlendingMisconfigured(
        "Destination alpha factor not SOURCE_ALPHA_SATURATE");
    }

    this.blending = true;
    this.current_equation_rgb_gl3 = equation_rgb;
    this.current_equation_alpha_gl3 = equation_alpha;
    this.current_source_rgb_factor = source_rgb_factor;
    this.current_source_alpha_factor = source_alpha_factor;
    this.current_destination_rgb_factor = destination_rgb_factor;
    this.current_destination_alpha_factor = destination_alpha_factor;
  }

  @Override public void blendingEnableSeparateWithEquationSeparateES2(
    final BlendFunction source_rgb_factor,
    final BlendFunction source_alpha_factor,
    final BlendFunction destination_rgb_factor,
    final BlendFunction destination_alpha_factor,
    final BlendEquationGLES2 equation_rgb,
    final BlendEquationGLES2 equation_alpha)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    NullCheck.notNull(source_rgb_factor, "Source RGB factor");
    NullCheck.notNull(source_alpha_factor, "Source alpha factor");
    NullCheck.notNull(destination_rgb_factor, "Destination RGB factor");
    NullCheck.notNull(destination_alpha_factor, "Destination alpha factor");
    NullCheck.notNull(equation_rgb, "Equation RGB");
    NullCheck.notNull(equation_alpha, "Equation alpha");

    if (destination_rgb_factor == BlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
      throw new JCGLExceptionBlendingMisconfigured(
        "Destination RGB factor not SOURCE_ALPHA_SATURATE");
    }
    if (destination_alpha_factor == BlendFunction.BLEND_SOURCE_ALPHA_SATURATE) {
      throw new JCGLExceptionBlendingMisconfigured(
        "Destination alpha factor not SOURCE_ALPHA_SATURATE");
    }

    this.blending = true;
    this.current_equation_rgb_gles2 = equation_rgb;
    this.current_equation_alpha_gles2 = equation_alpha;
    this.current_source_rgb_factor = source_rgb_factor;
    this.current_source_alpha_factor = source_alpha_factor;
    this.current_destination_rgb_factor = destination_rgb_factor;
    this.current_destination_alpha_factor = destination_alpha_factor;
  }

  @Override public void blendingEnableWithEquation(
    final BlendFunction source_factor,
    final BlendFunction destination_factor,
    final BlendEquationGL3 equation)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blendingEnableSeparateWithEquationSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  @Override public void blendingEnableWithEquationES2(
    final BlendFunction source_factor,
    final BlendFunction destination_factor,
    final BlendEquationGLES2 equation)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blendingEnableSeparateWithEquationSeparateES2(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation,
      equation);
  }

  @Override public void blendingEnableWithEquationSeparate(
    final BlendFunction source_factor,
    final BlendFunction destination_factor,
    final BlendEquationGL3 equation_rgb,
    final BlendEquationGL3 equation_alpha)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blendingEnableSeparateWithEquationSeparate(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public void blendingEnableWithEquationSeparateES2(
    final BlendFunction source_factor,
    final BlendFunction destination_factor,
    final BlendEquationGLES2 equation_rgb,
    final BlendEquationGLES2 equation_alpha)
    throws JCGLExceptionRuntime,
      JCGLExceptionBlendingMisconfigured
  {
    this.blendingEnableSeparateWithEquationSeparateES2(
      source_factor,
      source_factor,
      destination_factor,
      destination_factor,
      equation_rgb,
      equation_alpha);
  }

  @Override public boolean blendingIsEnabled()
    throws JCGLExceptionRuntime
  {
    return this.blending;
  }
}
