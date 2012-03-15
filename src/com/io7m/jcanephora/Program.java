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
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.PathVirtual;

/**
 * A high level abstraction over shading language programs, offering on-demand
 * recompilation and graceful handling of compilation errors.
 */

public final class Program
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
  private final @Nonnull TreeMap<String, Uniform>                  uniforms;
  private final @Nonnull TreeMap<String, Attribute>                attributes;
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
    this.uniforms = new TreeMap<String, Uniform>();
    this.attributes = new TreeMap<String, Attribute>();
    this.changed = false;
  }

  /**
   * Make the program active.
   * 
   * @param gl
   *          An OpenGL interface.
   * @throws ConstraintError
   *           Iff <code>gl == null</code> or one of the constraints for
   *           {@link GLInterface#useProgram(ProgramReference)} does not hold.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void activate(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");

    gl.useProgram(this.program);
  }

  /**
   * Add the fragment shader at <code>path</code> to the program.
   * 
   * @param path
   *          The path to the file containing a GLSL fragment shader.
   * @throws ConstraintError
   *           Iff <code>path == null</code>.
   */

  public void addFragmentShader(
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

  public void addVertexShader(
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
   * @throws GLCompileException
   *           Iff a compilation error occurs.
   */

  public void compile(
    final @Nonnull FilesystemAPI fs,
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLCompileException
  {
    Constraints.constrainNotNull(fs, "Filesystem");
    Constraints.constrainNotNull(gl, "OpenGL interface");

    try {
      if ((this.fragment_shaders.size() == 0)
        && (this.vertex_shaders.size() == 0)) {
        throw new GLCompileException("<none>", "empty program");
      }

      if (this.requiresCompilation(fs, gl) == false) {
        this.debug("program component(s) not modified - not recompiling");
        return;
      }

      this.debug("program component(s) modified - recompiling");
      final ProgramReference old_program = this.program;
      final ProgramReference new_program = gl.createProgram(this.name);

      /*
       * Recompile vertex shaders if necessary.
       */

      for (final Entry<PathVirtual, VertexShaderEntry> e : this.vertex_shaders
        .entrySet()) {
        final PathVirtual path = e.getKey();
        final VertexShaderEntry shader = e.getValue();
        final long time = fs.modificationTime(path);

        if (time != shader.last_modified) {
          final InputStream stream = fs.openFile(path);
          final VertexShader new_shader =
            gl.compileVertexShader(path.toString(), stream);

          if (shader.shader != null) {
            gl.deleteVertexShader(shader.shader);
          }
          shader.last_modified = time;
          shader.shader = new_shader;
          gl.attachVertexShader(new_program, new_shader);
        } else {
          assert shader.shader != null;
          gl.attachVertexShader(new_program, shader.shader);
        }
      }

      /*
       * Recompile fragment shaders if necessary.
       */

      for (final Entry<PathVirtual, FragmentShaderEntry> e : this.fragment_shaders
        .entrySet()) {
        final PathVirtual path = e.getKey();
        final FragmentShaderEntry shader = e.getValue();
        final long time = fs.modificationTime(path);

        if (time != shader.last_modified) {
          final InputStream stream = fs.openFile(path);
          final FragmentShader new_shader =
            gl.compileFragmentShader(path.toString(), stream);

          if (shader.shader != null) {
            gl.deleteFragmentShader(shader.shader);
          }
          shader.last_modified = time;
          shader.shader = new_shader;
          gl.attachFragmentShader(new_program, new_shader);
        } else {
          assert shader.shader != null;
          gl.attachFragmentShader(new_program, shader.shader);
        }
      }

      gl.linkProgram(new_program);
      GLError.check(gl);

      this.program = new_program;
      if (old_program != null) {
        gl.deleteProgram(old_program);
      }

      gl.useProgram(new_program);

      this.uniforms.clear();
      gl.getUniforms(this.program, this.uniforms);
      this.attributes.clear();
      gl.getAttributes(this.program, this.attributes);
      this.changed = false;

      for (final Entry<String, Uniform> e : this.uniforms.entrySet()) {
        this.debug("uniform " + e.getValue());
      }
      for (final Entry<String, Attribute> e : this.attributes.entrySet()) {
        this.debug("attribute " + e.getValue());
      }

      gl.noProgram();

    } catch (final FilesystemError e) {
      throw new GLCompileException(this.name, e.getMessage());
    } catch (final IOException e) {
      throw new GLCompileException(this.name, e.getMessage());
    } catch (final GLException e) {
      throw new GLCompileException(this.name, e.getMessage());
    }
  }

  /**
   * Deactivate the current program. If the current program is not active, the
   * function does nothing.
   * 
   * @param gl
   * @throws ConstraintError
   *           Iff <code>gl == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  @SuppressWarnings("static-method") public void deactivate(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");

    gl.noProgram();
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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void delete(
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    GLException error = null;

    for (final Entry<PathVirtual, VertexShaderEntry> e : this.vertex_shaders
      .entrySet()) {
      try {
        final VertexShaderEntry v = e.getValue();
        if (v.shader != null) {
          v.shader.delete(gl);
        }
      } catch (final GLException x) {
        error = x;
      }
    }

    for (final Entry<PathVirtual, FragmentShaderEntry> e : this.fragment_shaders
      .entrySet()) {
      try {
        final FragmentShaderEntry f = e.getValue();
        if (f.shader != null) {
          f.shader.delete(gl);
        }
      } catch (final GLException x) {
        error = x;
      }
    }

    try {
      if (this.program != null) {
        this.program.delete(gl);
      }
    } catch (final GLException x) {
      error = x;
    }

    if (error != null) {
      throw error;
    }
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

  public @CheckForNull Attribute getAttribute(
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

  public @CheckForNull Uniform getUniform(
    final @Nonnull String uniform_name)
    throws ConstraintError
  {
    return this.uniforms.get(Constraints.constrainNotNull(
      uniform_name,
      "Uniform name"));
  }

  /**
   * Return <code>true</code> iff the current program is active.
   * 
   * @param gl
   *          An OpenGL interface.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   * @throws ConstraintError
   *           Iff <code>gl == null</code>.
   */

  public boolean isActive(
    final @Nonnull GLInterface gl)
    throws GLException,
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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void removeFragmentShader(
    final @Nonnull PathVirtual path,
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(path, "path");

    if (this.fragment_shaders.containsKey(path) == false) {
      return;
    }
    final FragmentShaderEntry f = this.fragment_shaders.get(path);
    if (f.shader != null) {
      gl.deleteFragmentShader(f.shader);
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
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  public void removeVertexShader(
    final @Nonnull PathVirtual path,
    final @Nonnull GLInterface gl)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(path, "path");

    if (this.vertex_shaders.containsKey(path) == false) {
      return;
    }
    final VertexShaderEntry v = this.vertex_shaders.get(path);
    if (v.shader != null) {
      gl.deleteVertexShader(v.shader);
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

  public boolean requiresCompilation(
    final @Nonnull FilesystemAPI fs,
    final @Nonnull GLInterface gl)
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

      final long time = fs.modificationTime(path);
      if (time != shader.last_modified) {
        return true;
      }
    }

    for (final Entry<PathVirtual, FragmentShaderEntry> e : this.fragment_shaders
      .entrySet()) {
      final PathVirtual path = e.getKey();
      final FragmentShaderEntry shader = e.getValue();

      final long time = fs.modificationTime(path);
      if (time != shader.last_modified) {
        return true;
      }
    }

    return false;
  }
}
