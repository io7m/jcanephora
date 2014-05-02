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

package com.io7m.jcanephora;

import java.util.Map;

/**
 * <p>
 * A read-only interface to the {@link ProgramType} type that allows use of
 * the type but not mutation and/or deletion of the contents.
 * </p>
 */

public interface ProgramUsableType extends
  JCGLNameType,
  JCGLResourceUsableType
{
  /**
   * @return A read-only view of the set of available attribute inputs for the
   *         current program.
   */

  Map<String, ProgramAttributeType> programGetAttributes();

  /**
   * @return A read-only view of the set of available uniform inputs for the
   *         current program.
   */

  Map<String, ProgramUniformType> programGetUniforms();

  /**
   * @return The name of the program.
   */

  String programGetName();
}
