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

package com.io7m.jcanephora;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.FSCapabilityRead;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

/**
 * A high level abstraction over shading language programs, offering on-demand
 * recompilation and graceful handling of compilation errors.
 */

public final class Program extends JCGLResourceDeletable implements
  CompilableProgram,
  UsableProgram
{
  private static class FragmentShaderEntry
  {
    public @CheckForNull FragmentShader shader        = null;
    public long                         last_modified = 0;

    public FragmentShaderEntry()
    {

    }
  }

  private static class VertexShaderEntry
  {
    public @CheckForNull VertexShader shader        = null;
    public long                       last_modified = 0;

    public VertexShaderEntry()
    {

    }
  }

  private @CheckForNull ProgramReference                           program;
  private final @Nonnull String                                    name;
  private final @Nonnull HashMap<PathVirtual, VertexShaderEntry>   vertex_shaders;
  private final @Nonnull HashMap<PathVirtual, FragmentShaderEntry> fragment_shaders;
  private final @Nonnull TreeMap<String, ProgramUniform>           uniforms;
  private final @Nonnull TreeMap<String, ProgramAttribute>         attributes;
  private final @Nonnull Log                                       log;
  private boolean                                                  changed;

  public Program(
    final @Nonnull String name,
    final @Nonnull Log log)
    throws ConstraintError
  {
    Constraints.constrainNotNull(log, "Log output");

    this.log = new Log(log, "program");
    this.name = Constraints.constrainNotNull(name, "Program name");
    this.vertex_shaders = new HashMap<PathVirtual, VertexShaderEntry>();
    this.fragment_shaders = new HashMap<PathVirtual, FragmentShaderEntry>();
    this.uniforms = new TreeMap<String, ProgramUniform>();
    this.attributes = new TreeMap<String, ProgramAttribute>();
    this.changed = false;
  }

  /**
   * Make the program active.
   * 
   * @param gl
   *          An OpenGL interface.
   * @throws ConstraintError
   *           Iff <code>gl == null</code> or one of the constraints for
   *           {@link JCGLShaders#programActivate(ProgramReference)} does not
   *           hold.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  @Override public void activate(
    final @Nonnull JCGLShaders gl)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");

    gl.programActivate(this.program);
  }

  /**
   * Add the fragment shader at <code>path</code> to the program.
   * 
   * @param path
   *          The path to the file containing a GLSL fragment shader.
   * @throws ConstraintError
   *           Iff <code>path == null</code>.
   */

  @Override public void addFragmentShader(
    final @Nonnull PathVirtual path)
    throws ConstraintError
  {
    Constraints.constrainNotNull(path, "path");

    if (this.fragment_shaders.containsKey(path)) {
      return;
    }
    this.fragment_shaders.put(path, new FragmentShaderEntry());
    this.changed = true;
  }

  /**
   * Add the vertex shader at <code>path</code> to the program.
   * 
   * @param path
   *          The path to the file containing a GLSL vertex shader.
   * @throws ConstraintError
   *           Iff <code>path == null</code>.
   */

  @Override public void addVertexShader(
    final @Nonnull PathVirtual path)
    throws ConstraintError
  {
    Constraints.constrainNotNull(path, "path");

    if (this.vertex_shaders.containsKey(path)) {
      return;
    }
    this.vertex_shaders.put(path, new VertexShaderEntry());
    this.changed = true;
  }

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
   * @throws JCGLCompileException
   *           Iff a compilation error occurs.
   */

  @Override public
    <G extends JCGLShaders & JCGLMeta, F extends FSCapabilityRead>
    void
    compile(
      final @Nonnull F fs,
      final @Nonnull G gl)
      throws ConstraintError,
        JCGLCompileException
  {
    Constraints.constrainNotNull(fs, "Filesystem");
    Constraints.constrainNotNull(gl, "OpenGL interface");

    try {
      if (this.vertex_shaders.size() == 0) {
        throw new JCGLCompileException(
          "<none>",
          "at least one vertex shader is required");
      }
      if (this.vertex_shaders.size() > 1) {
        if (gl.metaIsES() && (gl.metaGetVersionMajor() == 2)) {
          throw new JCGLCompileException(
            "<none>",
            "ES2 forbids multiple vertex shader attachments");
        }
      }

      if (this.fragment_shaders.size() == 0) {
        throw new JCGLCompileException(
          "<none>",
          "at least one fragment shader is required");
      }
      if (this.fragment_shaders.size() > 1) {
        final int vm = gl.metaGetVersionMajor();
        if (gl.metaIsES() && ((vm >= 2) && (vm <= 3))) {
          throw new JCGLCompileException(
            "<none>",
            "ES2/ES3 forbids multiple fragment shader attachments");
        }
      }

      if (this.requiresCompilation(fs, gl) == false) {
        this.debug("program component(s) not modified - not recompiling");
        return;
      }

      this.debug("program component(s) modified - recompiling");
      final ProgramReference old_program = this.program;
      final ProgramReference new_program = gl.programCreate(this.name);

      /*
       * Recompile vertex shaders if necessary.
       */

      for (final Entry<PathVirtual, VertexShaderEntry> e : this.vertex_shaders
        .entrySet()) {
        final PathVirtual path = e.getKey();
        final VertexShaderEntry shader = e.getValue();
        final long time = fs.getModificationTime(path).getTimeInMillis();

        if (time != shader.last_modified) {
          InputStream stream = null;

          try {
            stream = fs.openFile(path);

            final VertexShader new_shader =
              gl.vertexShaderCompile(path.toString(), stream);

            if (shader.shader != null) {
              gl.vertexShaderDelete(shader.shader);
            }
            shader.last_modified = time;
            shader.shader = new_shader;
            gl.vertexShaderAttach(new_program, new_shader);

            final InputStream alt = stream;
            stream = null;
            alt.close();
          } finally {
            if (stream != null) {
              stream.close();
            }
          }
        } else {
          assert shader.shader != null;
          gl.vertexShaderAttach(new_program, shader.shader);
        }
      }

      /*
       * Recompile fragment shaders if necessary.
       */

      for (final Entry<PathVirtual, FragmentShaderEntry> e : this.fragment_shaders
        .entrySet()) {
        final PathVirtual path = e.getKey();
        final FragmentShaderEntry shader = e.getValue();
        final long time = fs.getModificationTime(path).getTimeInMillis();

        if (time != shader.last_modified) {
          InputStream stream = null;

          try {
            stream = fs.openFile(path);

            final FragmentShader new_shader =
              gl.fragmentShaderCompile(path.toString(), stream);

            if (shader.shader != null) {
              gl.fragmentShaderDelete(shader.shader);
            }
            shader.last_modified = time;
            shader.shader = new_shader;
            gl.fragmentShaderAttach(new_program, new_shader);

            final InputStream alt = stream;
            stream = null;
            alt.close();
          } finally {
            if (stream != null) {
              stream.close();
            }
          }
        } else {
          assert shader.shader != null;
          gl.fragmentShaderAttach(new_program, shader.shader);
        }
      }

      gl.programLink(new_program);

      this.program = new_program;
      if (old_program != null) {
        gl.programDelete(old_program);
      }

      gl.programActivate(new_program);

      this.uniforms.clear();
      gl.programGetUniforms(this.program, this.uniforms);
      this.attributes.clear();
      gl.programGetAttributes(this.program, this.attributes);
      this.changed = false;

      for (final Entry<String, ProgramUniform> e : this.uniforms.entrySet()) {
        this.debug("uniform " + e.getValue());
      }
      for (final Entry<String, ProgramAttribute> e : this.attributes
        .entrySet()) {
        this.debug("attribute " + e.getValue());
      }

      gl.programDeactivate();

    } catch (final FilesystemError e) {
      throw new JCGLCompileException(this.name, e.getMessage());
    } catch (final IOException e) {
      throw new JCGLCompileException(this.name, e.getMessage());
    } catch (final JCGLException e) {
      throw new JCGLCompileException(this.name, e.getMessage());
    }
  }

  /**
   * Deactivate the current program. If the current program is not active, the
   * function does nothing.
   * 
   * @param gl
   * @throws ConstraintError
   *           Iff <code>gl == null</code>.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  @Override public void deactivate(
    final @Nonnull JCGLShaders gl)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");

    gl.programDeactivate();
  }

  private void debug(
    final @Nonnull String message)
  {
    this.log.debug(this.name + " " + message);
  }

  /**
   * Delete the program and all attached shaders.
   * 
   * @param gl
   *          An OpenGL interface.
   * @throws ConstraintError
   *           Iff <code>gl == null</code>.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  public void delete(
    final @Nonnull JCGLShaders gl)
    throws ConstraintError,
      JCGLException
  {
    JCGLException error = null;

    for (final Entry<PathVirtual, VertexShaderEntry> e : this.vertex_shaders
      .entrySet()) {
      try {
        final VertexShaderEntry v = e.getValue();
        if (v.shader != null) {
          gl.vertexShaderDelete(v.shader);
        }
      } catch (final JCGLException x) {
        error = x;
      }
    }

    for (final Entry<PathVirtual, FragmentShaderEntry> e : this.fragment_shaders
      .entrySet()) {
      try {
        final FragmentShaderEntry f = e.getValue();
        if (f.shader != null) {
          gl.fragmentShaderDelete(f.shader);
        }
      } catch (final JCGLException x) {
        error = x;
      }
    }

    try {
      if (this.program != null) {
        gl.programDelete(this.program);
      }
    } catch (final JCGLException x) {
      error = x;
    }

    this.resourceSetDeleted();

    if (error != null) {
      throw error;
    }
  }

  @Override public boolean equals(
    final Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    final Program other = (Program) obj;
    if (this.program == null) {
      if (other.program != null) {
        return false;
      }
    } else if (!this.program.equals(other.program)) {
      return false;
    }
    return true;
  }

  /**
   * Retrieve a reference to the attribute variable named
   * <code>attribute_name</code> in the program.
   * 
   * @param attribute_name
   *          The attribute variable name.
   * @throws ConstraintError
   *           Iff <code>attribute_name == null</code>.
   */

  public @CheckForNull ProgramAttribute getAttribute(
    final @Nonnull String attribute_name)
    throws ConstraintError
  {
    return this.attributes.get(Constraints.constrainNotNull(
      attribute_name,
      "Attribute name"));
  }

  public @CheckForNull ProgramReference getID()
  {
    return this.program;
  }

  /**
   * Return the name of the program.
   */

  public @Nonnull String getName()
  {
    return this.name;
  }

  /**
   * Retrieve a reference to the uniform variable named
   * <code>uniform_name</code> in the program.
   * 
   * @param uniform_name
   *          The uniform variable name.
   * @throws ConstraintError
   *           Iff <code>uniform_name == null</code>.
   */

  public @CheckForNull ProgramUniform getUniform(
    final @Nonnull String uniform_name)
    throws ConstraintError
  {
    return this.uniforms.get(Constraints.constrainNotNull(
      uniform_name,
      "Uniform name"));
  }

  @Override public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result =
      (prime * result)
        + ((this.program == null) ? 0 : this.program.hashCode());
    return result;
  }

  /**
   * Return <code>true</code> iff the current program is active.
   * 
   * @param gl
   *          An OpenGL interface.
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff <code>gl == null</code>.
   */

  public boolean isActive(
    final @Nonnull JCGLShaders gl)
    throws JCGLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    if (this.program == null) {
      return false;
    }
    return gl.programIsActive(this.program);
  }

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  @Override public void removeFragmentShader(
    final @Nonnull PathVirtual path,
    final @Nonnull JCGLShaders gl)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(path, "path");

    if (this.fragment_shaders.containsKey(path) == false) {
      return;
    }
    final FragmentShaderEntry f = this.fragment_shaders.get(path);
    if (f.shader != null) {
      gl.fragmentShaderDelete(f.shader);
    }
    this.fragment_shaders.remove(path);
    this.changed = true;
  }

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
   * @throws JCGLException
   *           Iff an OpenGL error occurs.
   */

  @Override public void removeVertexShader(
    final @Nonnull PathVirtual path,
    final @Nonnull JCGLShaders gl)
    throws ConstraintError,
      JCGLException
  {
    Constraints.constrainNotNull(path, "path");

    if (this.vertex_shaders.containsKey(path) == false) {
      return;
    }
    final VertexShaderEntry v = this.vertex_shaders.get(path);
    if (v.shader != null) {
      gl.vertexShaderDelete(v.shader);
    }
    this.vertex_shaders.remove(path);
    this.changed = true;
  }

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

  @Override public <F extends FSCapabilityRead> boolean requiresCompilation(
    final @Nonnull F fs,
    final @Nonnull JCGLShaders gl)
    throws FilesystemError,
      ConstraintError
  {
    Constraints.constrainNotNull(fs, "Filesystem");
    Constraints.constrainNotNull(gl, "OpenGL interface");

    if (this.program == null) {
      return true;
    }

    if (this.changed) {
      return true;
    }

    for (final Entry<PathVirtual, VertexShaderEntry> e : this.vertex_shaders
      .entrySet()) {
      final PathVirtual path = e.getKey();
      final VertexShaderEntry shader = e.getValue();

      final long time = fs.getModificationTime(path).getTimeInMillis();
      if (time != shader.last_modified) {
        return true;
      }
    }

    for (final Entry<PathVirtual, FragmentShaderEntry> e : this.fragment_shaders
      .entrySet()) {
      final PathVirtual path = e.getKey();
      final FragmentShaderEntry shader = e.getValue();

      final long time = fs.getModificationTime(path).getTimeInMillis();
      if (time != shader.last_modified) {
        return true;
      }
    }

    return false;
  }

  @Override public String toString()
  {
    final StringBuilder builder = new StringBuilder();
    builder.append("[Program ");
    builder.append(this.program);
    builder.append(" ");
    builder.append(this.name);
    builder.append("]");
    return builder.toString();
  }
}
