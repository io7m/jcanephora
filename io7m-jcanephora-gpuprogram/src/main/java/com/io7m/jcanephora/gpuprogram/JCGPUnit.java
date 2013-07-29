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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLApiKindES;
import com.io7m.jcanephora.JCGLApiKindFull;
import com.io7m.jcanephora.JCGLSLVersionNumber;

abstract class JCGPUnit
{
  static final class JCGPUnitFragmentShader extends JCGPUnit
  {
    private final @Nonnull Map<String, JCGPFragmentShaderInput>  inputs;
    private final @Nonnull Map<String, JCGPFragmentShaderOutput> outputs;
    private final @Nonnull Map<String, JCGPUniform>              uniforms;

    @SuppressWarnings("synthetic-access") private JCGPUnitFragmentShader(
      final @Nonnull String name,
      final @Nonnull JCGPSource source,
      final @Nonnull List<String> imports,
      final @CheckForNull JCGPVersionRange<JCGLApiKindES> versions_es,
      final @CheckForNull JCGPVersionRange<JCGLApiKindFull> versions_full)
      throws ConstraintError
    {
      super(
        Type.UNIT_FRAGMENT_SHADER_MAIN,
        source,
        name,
        imports,
        versions_es,
        versions_full);

      this.inputs = new HashMap<String, JCGPFragmentShaderInput>();
      this.outputs = new HashMap<String, JCGPFragmentShaderOutput>();
      this.uniforms = new HashMap<String, JCGPUniform>();
    }

    /**
     * <p>
     * Explicitly declare that the given fragment shader has an input (or
     * "attribute" parameter in archaic OpenGL terms) <tt>input</tt>. An input
     * declaration for the given input will be generated and placed in the
     * resulting GLSL source code, using whatever syntax is appropriate for
     * the given version.
     * </p>
     * 
     * @throws ConstraintError
     *           Iff any of the following hold:
     *           <ul>
     *           <li><code>input == null</code></li>
     *           <li>Something with the same name as <code>input</code> has
     *           already been added as an input, uniform, or output.</li>
     */

    public void declareInput(
      final @Nonnull JCGPFragmentShaderInput input)
      throws ConstraintError
    {
      Constraints.constrainNotNull(input, "Name");
      Constraints.constrainArbitrary(
        this.outputs.containsKey(input.getName()) == false,
        "Output does not exist with the given name");
      Constraints.constrainArbitrary(
        this.inputs.containsKey(input.getName()) == false,
        "Input does not exist with the given name");
      Constraints.constrainArbitrary(
        this.uniforms.containsKey(input.getName()) == false,
        "Uniform input does not exist with the given name");
      this.inputs.put(input.getName(), input);
    }

    /**
     * <p>
     * Explicitly declare that the given fragment shader has an output (or
     * "varying" parameter in archaic OpenGL terms) <tt>output</tt>. An output
     * declaration for the given output will be generated and placed in the
     * resulting GLSL source code, using whatever syntax is appropriate for
     * the given version.
     * </p>
     * 
     * @throws ConstraintError
     *           Iff any of the following hold:
     *           <ul>
     *           <li><code>output == null</code></li>
     *           <li>Something with the same name as <code>output</code> has
     *           already been added as an input, uniform, or output.</li>
     */

    public void declareOutput(
      final @Nonnull JCGPFragmentShaderOutput output)
      throws ConstraintError
    {
      Constraints.constrainNotNull(output, "Name");
      Constraints.constrainArbitrary(
        this.outputs.containsKey(output.getName()) == false,
        "Output does not exist with the given name");
      Constraints.constrainArbitrary(
        this.inputs.containsKey(output.getName()) == false,
        "Input does not exist with the given name");
      Constraints.constrainArbitrary(
        this.uniforms.containsKey(output.getName()) == false,
        "Uniform input does not exist with the given name");
      this.outputs.put(output.getName(), output);
    }

    /**
     * <p>
     * Explicitly declare that the given fragment shader has a uniform input
     * <tt>input</tt>. A uniform declaration for the given parameter will be
     * generated and placed in the resulting GLSL source code, using whatever
     * syntax is appropriate for the given version.
     * </p>
     * 
     * @throws ConstraintError
     *           Iff any of the following hold:
     *           <ul>
     *           <li><code>input == null</code></li>
     *           <li><code>input</code> has already been added as an input,
     *           uniform, or output.</li>
     */

    public void declareUniformInput(
      final @Nonnull JCGPUniform input)
      throws ConstraintError
    {
      Constraints.constrainNotNull(input, "Name");
      Constraints.constrainArbitrary(
        this.outputs.containsKey(input.getName()) == false,
        "Output does not exist with the given name");
      Constraints.constrainArbitrary(
        this.inputs.containsKey(input.getName()) == false,
        "Input does not exist with the given name");
      Constraints.constrainArbitrary(
        this.uniforms.containsKey(input.getName()) == false,
        "Uniform input does not exist with the given name");
      this.uniforms.put(input.getName(), input);
    }

    @Override protected void evaluateActual(
      final @Nonnull JCGPGeneratorContext context,
      final @Nonnull ArrayList<String> output)
      throws Exception,
        ConstraintError
    {
      if (context.isDebugging()) {
        output.add("// unit: " + this.getName());
      }

      final JCGLSLVersionNumber v = context.getVersion();
      final JCGLApi a = context.getApi();

      for (final Entry<String, JCGPUniform> e : this.uniforms.entrySet()) {
        output.add(e.getValue().toGLSL(v, a));
      }

      for (final Entry<String, JCGPFragmentShaderInput> e : this.inputs
        .entrySet()) {
        output.add(e.getValue().toGLSL(v, a));
      }

      /**
       * Only OpenGL >= 2.1, and ES >= 3 support fragment shader outputs.
       */

      if (v.getVersionMajor() > 2) {
        for (final Entry<String, JCGPFragmentShaderOutput> e : this.outputs
          .entrySet()) {
          output.add(e.getValue().toGLSL(v, a));
        }
      }

      this.source.sourceGet(context, output);
    }

    /**
     * Retrieve the set of explicitly declared fragment shader inputs from the
     * current unit.
     */

    public final @Nonnull
      Map<String, JCGPFragmentShaderInput>
      getDeclaredInputs()
    {
      return Collections.unmodifiableMap(this.inputs);
    }

    /**
     * Retrieve the set of explicitly declared fragment shader outputs from
     * the current unit.
     */

    public @Nonnull
      Map<String, JCGPFragmentShaderOutput>
      getDeclaredOutputs()
    {
      return Collections.unmodifiableMap(this.outputs);
    }

    /**
     * Retrieve the set of explicitly declared fragment shader uniform inputs
     * from the current unit.
     */

    public @Nonnull Map<String, JCGPUniform> getDeclaredUniformInputs()
    {
      return Collections.unmodifiableMap(this.uniforms);
    }

    @Override public boolean updatedSince(
      final @Nonnull Calendar since)
      throws ConstraintError,
        Exception
    {
      Constraints.constrainNotNull(since, "Since");
      return this.source.sourceChangedSince(since);
    }
  }

  static final class JCGPUnitGeneric extends JCGPUnit
  {
    @SuppressWarnings("synthetic-access") private JCGPUnitGeneric(
      final @Nonnull String name,
      final @Nonnull JCGPSource source,
      final @Nonnull List<String> imports,
      final @CheckForNull JCGPVersionRange<JCGLApiKindES> versions_es,
      final @CheckForNull JCGPVersionRange<JCGLApiKindFull> versions_full)
      throws ConstraintError
    {
      super(
        Type.UNIT_GENERIC,
        source,
        name,
        imports,
        versions_es,
        versions_full);
    }

    @Override protected void evaluateActual(
      final @Nonnull JCGPGeneratorContext context,
      final @Nonnull ArrayList<String> output)
      throws Exception,
        ConstraintError
    {
      if (context.isDebugging()) {
        output.add("// unit: " + this.getName());
      }
      this.source.sourceGet(context, output);
    }

    @Override public boolean updatedSince(
      final @Nonnull Calendar since)
      throws ConstraintError,
        Exception
    {
      Constraints.constrainNotNull(since, "Since");
      return this.source.sourceChangedSince(since);
    }
  }

  static final class JCGPUnitVertexShader extends JCGPUnit
  {
    private final @Nonnull Map<String, JCGPVertexShaderInput>  inputs;
    private final @Nonnull Map<String, JCGPVertexShaderOutput> outputs;
    private final @Nonnull Map<String, JCGPUniform>            uniforms;

    @SuppressWarnings("synthetic-access") private JCGPUnitVertexShader(
      final @Nonnull String name,
      final @Nonnull JCGPSource source,
      final @Nonnull List<String> imports,
      final @CheckForNull JCGPVersionRange<JCGLApiKindES> versions_es,
      final @CheckForNull JCGPVersionRange<JCGLApiKindFull> versions_full)
      throws ConstraintError
    {
      super(
        Type.UNIT_VERTEX_SHADER_MAIN,
        source,
        name,
        imports,
        versions_es,
        versions_full);

      this.inputs = new HashMap<String, JCGPVertexShaderInput>();
      this.outputs = new HashMap<String, JCGPVertexShaderOutput>();
      this.uniforms = new HashMap<String, JCGPUniform>();
    }

    /**
     * <p>
     * Explicitly declare that the given vertex shader has an input (or
     * "attribute" parameter in archaic OpenGL terms) <tt>input</tt>. An input
     * declaration for the given input will be generated and placed in the
     * resulting GLSL source code, using whatever syntax is appropriate for
     * the given version.
     * </p>
     * 
     * @throws ConstraintError
     *           Iff any of the following hold:
     *           <ul>
     *           <li><code>input == null</code></li>
     *           <li>Something with the same name as <code>input</code> has
     *           already been added as an input, uniform, or output.</li>
     */

    public void declareInput(
      final @Nonnull JCGPVertexShaderInput input)
      throws ConstraintError
    {
      Constraints.constrainNotNull(input, "Name");
      Constraints.constrainArbitrary(
        this.outputs.containsKey(input.getName()) == false,
        "Output does not exist with the given name");
      Constraints.constrainArbitrary(
        this.inputs.containsKey(input.getName()) == false,
        "Input does not exist with the given name");
      Constraints.constrainArbitrary(
        this.uniforms.containsKey(input.getName()) == false,
        "Uniform input does not exist with the given name");
      this.inputs.put(input.getName(), input);
    }

    /**
     * <p>
     * Explicitly declare that the given vertex shader has an output (or
     * "varying" parameter in archaic OpenGL terms) <tt>output</tt>. An output
     * declaration for the given output will be generated and placed in the
     * resulting GLSL source code, using whatever syntax is appropriate for
     * the given version.
     * </p>
     * 
     * @throws ConstraintError
     *           Iff any of the following hold:
     *           <ul>
     *           <li><code>output == null</code></li>
     *           <li>Something with the same name as <code>output</code> has
     *           already been added as an input, uniform, or output.</li>
     */

    public void declareOutput(
      final @Nonnull JCGPVertexShaderOutput output)
      throws ConstraintError
    {
      Constraints.constrainNotNull(output, "Name");
      Constraints.constrainArbitrary(
        this.outputs.containsKey(output.getName()) == false,
        "Output does not exist with the given name");
      Constraints.constrainArbitrary(
        this.inputs.containsKey(output.getName()) == false,
        "Input does not exist with the given name");
      Constraints.constrainArbitrary(
        this.uniforms.containsKey(output.getName()) == false,
        "Uniform input does not exist with the given name");
      this.outputs.put(output.getName(), output);
    }

    /**
     * <p>
     * Explicitly declare that the given vertex shader has a uniform input
     * <tt>input</tt>. A uniform declaration for the given parameter will be
     * generated and placed in the resulting GLSL source code, using whatever
     * syntax is appropriate for the given version.
     * </p>
     * 
     * @throws ConstraintError
     *           Iff any of the following hold:
     *           <ul>
     *           <li><code>input == null</code></li>
     *           <li><code>input</code> has already been added as an input,
     *           uniform, or output.</li>
     */

    public void declareUniformInput(
      final @Nonnull JCGPUniform input)
      throws ConstraintError
    {
      Constraints.constrainNotNull(input, "Name");
      Constraints.constrainArbitrary(
        this.outputs.containsKey(input.getName()) == false,
        "Output does not exist with the given name");
      Constraints.constrainArbitrary(
        this.inputs.containsKey(input.getName()) == false,
        "Input does not exist with the given name");
      Constraints.constrainArbitrary(
        this.uniforms.containsKey(input.getName()) == false,
        "Uniform input does not exist with the given name");
      this.uniforms.put(input.getName(), input);
    }

    @Override protected void evaluateActual(
      final @Nonnull JCGPGeneratorContext context,
      final @Nonnull ArrayList<String> output)
      throws Exception,
        ConstraintError
    {
      if (context.isDebugging()) {
        output.add("// unit: " + this.getName());
      }

      final JCGLSLVersionNumber v = context.getVersion();
      final JCGLApi a = context.getApi();

      for (final Entry<String, JCGPUniform> e : this.uniforms.entrySet()) {
        output.add(e.getValue().toGLSL(v, a));
      }

      for (final Entry<String, JCGPVertexShaderInput> e : this.inputs
        .entrySet()) {
        output.add(e.getValue().toGLSL(v, a));
      }

      for (final Entry<String, JCGPVertexShaderOutput> e : this.outputs
        .entrySet()) {
        output.add(e.getValue().toGLSL(v, a));
      }

      this.source.sourceGet(context, output);
    }

    /**
     * Retrieve the set of explicitly declared vertex shader inputs from the
     * current unit.
     */

    public @Nonnull Map<String, JCGPVertexShaderInput> getDeclaredInputs()
    {
      return Collections.unmodifiableMap(this.inputs);
    }

    /**
     * Retrieve the set of explicitly declared vertex shader outputs from the
     * current unit.
     */

    public @Nonnull Map<String, JCGPVertexShaderOutput> getDeclaredOutputs()
    {
      return Collections.unmodifiableMap(this.outputs);
    }

    /**
     * Retrieve the set of explicitly declared vertex shader uniform inputs
     * from the current unit.
     */

    public @Nonnull Map<String, JCGPUniform> getDeclaredUniformInputs()
    {
      return Collections.unmodifiableMap(this.uniforms);
    }

    @Override public boolean updatedSince(
      final @Nonnull Calendar since)
      throws ConstraintError,
        Exception
    {
      Constraints.constrainNotNull(since, "Since");
      return this.source.sourceChangedSince(since);
    }
  }

  static enum Type
  {
    UNIT_GENERIC("generic"),
    UNIT_VERTEX_SHADER_MAIN("vertex-shader"),
    UNIT_FRAGMENT_SHADER_MAIN("fragment-shader");

    private final @Nonnull String name;

    private Type(
      final @Nonnull String name)
    {
      this.name = name;
    }

    public @Nonnull String getName()
    {
      return this.name;
    }
  }

  @SuppressWarnings("synthetic-access") public static @Nonnull
    JCGPUnitFragmentShader
    makeFragmentShader(
      final @Nonnull String name,
      final @Nonnull JCGPSource source,
      final @Nonnull List<String> imports,
      final @CheckForNull JCGPVersionRange<JCGLApiKindES> versions_es,
      final @CheckForNull JCGPVersionRange<JCGLApiKindFull> versions_full)
      throws ConstraintError
  {
    return new JCGPUnitFragmentShader(
      name,
      source,
      imports,
      versions_es,
      versions_full);
  }

  @SuppressWarnings("synthetic-access") public static @Nonnull
    JCGPUnitGeneric
    makeGeneric(
      final @Nonnull String name,
      final @Nonnull JCGPSource source,
      final @Nonnull List<String> imports,
      final @CheckForNull JCGPVersionRange<JCGLApiKindES> versions_es,
      final @CheckForNull JCGPVersionRange<JCGLApiKindFull> versions_full)
      throws ConstraintError
  {
    return new JCGPUnitGeneric(
      name,
      source,
      imports,
      versions_es,
      versions_full);
  }

  @SuppressWarnings("synthetic-access") public static @Nonnull
    JCGPUnitVertexShader
    makeVertexShader(
      final @Nonnull String name,
      final @Nonnull JCGPSource source,
      final @Nonnull List<String> imports,
      final @CheckForNull JCGPVersionRange<JCGLApiKindES> versions_es,
      final @CheckForNull JCGPVersionRange<JCGLApiKindFull> versions_full)
      throws ConstraintError
  {
    return new JCGPUnitVertexShader(
      name,
      source,
      imports,
      versions_es,
      versions_full);
  }

  protected final @Nonnull String                               name;
  private final @Nonnull List<String>                           imports;
  private final @CheckForNull JCGPVersionRange<JCGLApiKindES>   versions_es;
  private final @CheckForNull JCGPVersionRange<JCGLApiKindFull> versions_full;
  private final @Nonnull Type                                   type;
  protected final @Nonnull JCGPSource                           source;

  private JCGPUnit(
    final @Nonnull Type type,
    final @Nonnull JCGPSource source,
    final @Nonnull String name,
    final @Nonnull List<String> imports,
    final @CheckForNull JCGPVersionRange<JCGLApiKindES> versions_es,
    final @CheckForNull JCGPVersionRange<JCGLApiKindFull> versions_full)
    throws ConstraintError
  {
    this.type = Constraints.constrainNotNull(type, "Type");
    this.source = Constraints.constrainNotNull(source, "Source");
    this.name = Constraints.constrainNotNull(name, "Unit name");
    this.imports = Constraints.constrainNotNull(imports, "Import list");
    this.versions_es = versions_es;
    this.versions_full = versions_full;

    Constraints.constrainArbitrary((versions_es != null)
      || (versions_full != null), "At least one version range is required");
  }

  final void evaluate(
    final @Nonnull JCGPGeneratorContext context,
    final @Nonnull ArrayList<String> output)
    throws ConstraintError,
      Exception
  {
    Constraints.constrainNotNull(context, "Context");
    Constraints.constrainNotNull(output, "Output");
    this.evaluateActual(context, output);
  }

  protected abstract void evaluateActual(
    final @Nonnull JCGPGeneratorContext context,
    final @Nonnull ArrayList<String> output)
    throws Exception,
      ConstraintError;

  public final @Nonnull List<String> getImports()
  {
    return Collections.unmodifiableList(this.imports);
  }

  public final @Nonnull String getName()
  {
    return this.name;
  }

  public final @Nonnull Type getType()
  {
    return this.type;
  }

  public final @CheckForNull
    JCGPVersionRange<JCGLApiKindES>
    getVersionRangeES()
  {
    return this.versions_es;
  }

  public final @CheckForNull
    JCGPVersionRange<JCGLApiKindFull>
    getVersionRangeFull()
  {
    return this.versions_full;
  }

  public abstract boolean updatedSince(
    final @Nonnull Calendar since)
    throws ConstraintError,
      Exception;
}
