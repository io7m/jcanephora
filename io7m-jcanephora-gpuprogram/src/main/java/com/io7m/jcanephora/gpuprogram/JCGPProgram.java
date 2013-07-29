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

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.ProgramAttribute;
import com.io7m.jcanephora.ProgramReference;
import com.io7m.jcanephora.ProgramUniform;

/**
 * <p>
 * An immutable reference to a compiled program.
 * </p>
 */

@Immutable public final class JCGPProgram
{
  private final @Nonnull ProgramReference              program;
  private final @Nonnull Map<String, ProgramUniform>   uniforms;
  private final @Nonnull Map<String, ProgramAttribute> attributes;

  JCGPProgram(
    final @Nonnull ProgramReference p,
    final @Nonnull Map<String, ProgramUniform> uniforms,
    final @Nonnull Map<String, ProgramAttribute> attributes)
    throws ConstraintError
  {
    this.program = Constraints.constrainNotNull(p, "Program");
    this.uniforms = Constraints.constrainNotNull(uniforms, "Uniforms");
    this.attributes = Constraints.constrainNotNull(attributes, "Attributes");
  }
}
