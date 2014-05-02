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

/**
 * The type of uniforms in compiled programs.
 */

public interface ProgramUniformType
{
  /**
   * @return The raw OpenGL 'location' of the uniform value.
   */

  int uniformGetLocation();

  /**
   * @return The name of the uniform. This is the name of the uniform as
   *         declared in the respective shading program.
   */

  String uniformGetName();

  /**
   * @return A reference to the program to which the uniform belongs.
   */

  ProgramUsableType uniformGetProgram();

  /**
   * @return The OpenGL type of the uniform.
   */

  JCGLType uniformGetType();

}
