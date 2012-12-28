/*
 * Copyright © 2012 http://io7m.com
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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

/**
 * A shading language program supporting (re)compilation.
 */

public interface CompilableProgram
{
  /**
   * Add the fragment shader at <code>path</code> to the program.
   * 
   * @param path
   *          The path to the file containing a GLSL fragment shader.
   * @throws ConstraintError
   *           Iff <code>path == null</code>.
   */

  void addFragmentShader(
    final @Nonnull PathVirtual path)
    throws ConstraintError;

  /**
   * Add the vertex shader at <code>path</code> to the program.
   * 
   * @param path
   *          The path to the file containing a GLSL vertex shader.
   * @throws ConstraintError
   *           Iff <code>path == null</code>.
   */

  void addVertexShader(
    final @Nonnull PathVirtual path)
    throws ConstraintError;

  /**
   * Compile the program, reading source files from the filesystem referenced
   * by <code>fs</code>. The function intelligently (re)compiles the program
   * if any of the fragment or vertex shaders on disk have been modified. The
   * function only replaces the current program in the OpenGL implementation
   * if compilation and linking is successful. A real-time shader previewing
   * program could have the following structure:
   * 
   * <pre>
   * Program p;
   * int time = 0;
   * 
   * while (done == false) {
   *   ++time;
   *   try {
   *     if (time % 120 == 0) {
   *       p.compile(fs, gl);
   *     }
   *     renderWith(p);
   *   } catch (GLCompileException e) {
   *     showCompilationError(e);
   *   }
   * }
   * </pre>
   * 
   * @param fs
   *          A reference to a filesystem.
   * @param gl
   *          An OpenGL interface.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>fs == null</code>.</li>
   *           <li><code>gl == null</code>.</li>
   *           </ul>
   * @throws GLCompileException
   *           Iff a compilation error occurs.
   */

  void compile(
    final @Nonnull FilesystemAPI fs,
    final @Nonnull GLShaders gl)
    throws ConstraintError,
      GLCompileException;

  /**
   * Remove the fragment shader referenced by <code>path</code> from the
   * program. This operation marks the program as requiring recompilation.
   * 
   * @param path
   *          The path to the fragment shader.
   * @param gl
   *          An OpenGL interface.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>path == null</code>.</li>
   *           <li><code>gl == null</code>.</li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void removeFragmentShader(
    final @Nonnull PathVirtual path,
    final @Nonnull GLShaders gl)
    throws ConstraintError,
      GLException;

  /**
   * Remove the vertex shader referenced by <code>path</code> from the
   * program. This operation marks the program as requiring recompilation.
   * 
   * @param path
   *          The path to the vertex shader.
   * @param gl
   *          An OpenGL interface.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>path == null</code>.</li>
   *           <li><code>gl == null</code>.</li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void removeVertexShader(
    final @Nonnull PathVirtual path,
    final @Nonnull GLShaders gl)
    throws ConstraintError,
      GLException;

  /**
   * Returns <code>true</code> iff the program requires compilation.
   * 
   * @param fs
   *          A reference to a filesystem.
   * @param gl
   *          An OpenGL interface.
   * @throws FilesystemError
   *           If a filesystem error occurs whilst trying to determine the
   *           status of a file.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>path == null</code>.</li>
   *           <li><code>gl == null</code>.</li>
   *           </ul>
   */

  boolean requiresCompilation(
    final @Nonnull FilesystemAPI fs,
    final @Nonnull GLShaders gl)
    throws FilesystemError,
      ConstraintError;
}
