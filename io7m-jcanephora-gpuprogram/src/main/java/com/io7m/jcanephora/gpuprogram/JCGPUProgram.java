/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.gpuprogram;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApiKindES;
import com.io7m.jcanephora.JCGLApiKindFull;

/**
 * A program running on the GPU, consisting of a single vertex and fragment
 * shader. The source of the vertex and fragment shaders are generated by
 * evaluating the tree of source units associated with the program.
 */

public final class JCGPUProgram
{
  /**
   * Construct a new empty program called <tt>name</tt> that will run on the
   * range of OpenGL ES versions given by <tt>v</tt>.
   * 
   * @throws ConstraintError
   *           Iff <code>name == null || v == null</code>.
   */

  public static @Nonnull JCGPUProgram newProgramES(
    final @Nonnull String name,
    final @Nonnull JCGPVersionRange<JCGLApiKindES> v)
    throws ConstraintError
  {
    return new JCGPUProgram(name, null, v);
  }

  /**
   * Construct a new empty program called <tt>name</tt> that will run on the
   * range of desktop OpenGL versions given by <tt>v</tt>.
   * 
   * @throws ConstraintError
   *           Iff <code>name == null || v == null</code>.
   */

  public static @Nonnull JCGPUProgram newProgramFull(
    final @Nonnull String name,
    final @Nonnull JCGPVersionRange<JCGLApiKindFull> v)
    throws ConstraintError
  {
    return new JCGPUProgram(name, v, null);
  }

  /**
   * Construct a new empty program called <tt>name</tt> that will run on the
   * range of OpenGL ES versions given by <tt>v_es</tt> and the range of
   * OpenGL desktop versions given by <tt>v_full</tt>.
   * 
   * @throws ConstraintError
   *           Iff <code>name == null || v_es == null || v_full == null</code>
   *           .
   */

  public static @Nonnull JCGPUProgram newProgramFullAndES(
    final @Nonnull String name,
    final @Nonnull JCGPVersionRange<JCGLApiKindFull> v_full,
    final @Nonnull JCGPVersionRange<JCGLApiKindES> v_es)
    throws ConstraintError
  {
    return new JCGPUProgram(name, v_full, v_es);
  }

  private final @CheckForNull JCGPVersionRange<JCGLApiKindFull> v_full;
  private final @CheckForNull JCGPVersionRange<JCGLApiKindES>   v_es;
  private final @Nonnull String                                 name;

  private JCGPUProgram(
    final @Nonnull String name,
    final @CheckForNull JCGPVersionRange<JCGLApiKindFull> v_full,
    final @CheckForNull JCGPVersionRange<JCGLApiKindES> v_es)
    throws ConstraintError
  {
    this.v_full =
      Constraints.constrainNotNull(v_full, "Profile version range");
    this.v_es =
      Constraints.constrainNotNull(v_es, "ES profile version range");

    Constraints.constrainArbitrary(
      (v_full != null) || (v_es != null),
      "At least one version range must be provided");

    this.name = Constraints.constrainNotNull(name, "Program name");
  }
}
