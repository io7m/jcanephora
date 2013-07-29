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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLApiKindES;
import com.io7m.jcanephora.JCGLApiKindFull;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLShaderKindFragment;
import com.io7m.jcanephora.JCGLShaderKindVertex;
import com.io7m.jcanephora.JCGLShaderType;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.gpuprogram.JCGPUnit.JCGPUnitFragmentShader;
import com.io7m.jcanephora.gpuprogram.JCGPUnit.JCGPUnitVertexShader;
import com.io7m.jlog.Log;

/**
 * <p>
 * A generator that will produce GLSL source as output.
 * </p>
 * <p>
 * Values of this type cannot be manipulated by multiple threads without
 * explicit synchronization.
 * </p>
 */

@NotThreadSafe public final class JCGPGenerator implements JCGPGeneratorAPI
{
  private static @Nonnull JCGLCompileException checkFailureImportCyclic(
    final @Nonnull JCGPUnit unit,
    final @Nonnull String import_name)
  {
    final StringBuilder message = new StringBuilder();
    message.append("Cyclic import of '");
    message.append(import_name);
    message.append("' detected for unit '");
    message.append(unit.getName());
    message.append("'");
    return new JCGLCompileException(import_name, message.toString());
  }

  private final static @Nonnull
    JCGLCompileException
    checkFailureImportedNonGeneric(
      final @Nonnull JCGPUnit unit,
      final @Nonnull JCGPUnit imported)
  {
    final StringBuilder message = new StringBuilder();
    message.append("Unit '");
    message.append(unit.getName());
    message.append("' imports non-generic unit '");
    message.append(imported.getName());
    message.append("' (");
    message.append(imported.getType().getName());
    message.append(")");
    return new JCGLCompileException(unit.getName(), message.toString());
  }

  private static @Nonnull JCGLCompileException checkFailureImportMissing(
    final @Nonnull JCGPUnit unit,
    final @Nonnull String import_name)
  {
    final StringBuilder message = new StringBuilder();
    message.append("Unit '");
    message.append(unit.getName());
    message.append("' imports nonexistent unit '");
    message.append(import_name);
    message.append("'");
    return new JCGLCompileException(import_name, message.toString());
  }

  private static void generatorGenerateVersion(
    final @Nonnull JCGLSLVersionNumber version,
    final @Nonnull JCGLApi api,
    final @Nonnull ArrayList<String> output)
    throws JCGLUnsupportedException
  {
    output.add(JCGPVersionOutput.toGLSL(version, api));
  }

  /**
   * Construct a new empty generator, that will produce a program called
   * <tt>name</tt>, that will run on the range of OpenGL ES versions given by
   * <tt>v</tt>.
   * 
   * @throws ConstraintError
   *           Iff <code>name == null || v == null</code>.
   */

  public static @Nonnull JCGPGeneratorAPI newProgramES(
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull JCGPVersionRange<JCGLApiKindES> v)
    throws ConstraintError
  {
    return new JCGPGenerator(log, name, null, v);
  }

  /**
   * Construct a new empty generator, that will produce a program called
   * <tt>name</tt>, that will will run on the range of desktop OpenGL versions
   * given by <tt>v</tt>.
   * 
   * @throws ConstraintError
   *           Iff <code>name == null || v == null</code>.
   */

  public static @Nonnull JCGPGeneratorAPI newProgramFull(
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull JCGPVersionRange<JCGLApiKindFull> v)
    throws ConstraintError
  {
    return new JCGPGenerator(log, name, v, null);
  }

  /**
   * Construct a new empty generator, that will produce a program called
   * <tt>name</tt>, that will will run on the range of OpenGL ES versions
   * given by <tt>v_es</tt> and the range of OpenGL desktop versions given by
   * <tt>v_full</tt>.
   * 
   * @throws ConstraintError
   *           Iff <code>name == null || v_es == null || v_full == null</code>
   *           .
   */

  public static @Nonnull JCGPGenerator newProgramFullAndES(
    final @Nonnull Log log,
    final @Nonnull String name,
    final @Nonnull JCGPVersionRange<JCGLApiKindFull> v_full,
    final @Nonnull JCGPVersionRange<JCGLApiKindES> v_es)
    throws ConstraintError
  {
    Constraints.constrainNotNull(v_full, "Full version");
    Constraints.constrainNotNull(v_es, "ES version");
    return new JCGPGenerator(log, name, v_full, v_es);
  }

  private static void unitShowSupports(
    final @Nonnull JCGPUnit unit,
    final @Nonnull StringBuilder output)
  {
    output.append("Unit '");
    output.append(unit.getName());
    output.append("' provides: ");
    output.append("\n");

    {
      final JCGPVersionRange<JCGLApiKindES> r = unit.getVersionRangeES();
      if (r != null) {
        output.append("  GLSL ES ");
        output.append(r.toRangeNotation());
        output.append("\n");
      }
    }

    {
      final JCGPVersionRange<JCGLApiKindFull> r = unit.getVersionRangeFull();
      if (r != null) {
        output.append("  GLSL ");
        output.append(r.toRangeNotation());
        output.append("\n");
      }
    }
  }

  private final @CheckForNull JCGPVersionRange<JCGLApiKindFull> v_full;
  private final @CheckForNull JCGPVersionRange<JCGLApiKindES>   v_es;
  private final @Nonnull String                                 name;
  private final @Nonnull HashMap<String, JCGPUnit>              units;
  private @CheckForNull JCGPUnitVertexShader                    unit_vertex_main;
  private @CheckForNull JCGPUnitFragmentShader                  unit_fragment_main;
  private final @Nonnull Log                                    log;
  private final @Nonnull HashSet<String>                        cycle_cache;
  private boolean                                               debugging;
  private Calendar                                              generated_last;

  private JCGPGenerator(
    final @Nonnull Log log,
    final @Nonnull String name,
    final @CheckForNull JCGPVersionRange<JCGLApiKindFull> v_full,
    final @CheckForNull JCGPVersionRange<JCGLApiKindES> v_es)
    throws ConstraintError
  {
    this.log = new Log(Constraints.constrainNotNull(log, "Log"), "compiler");

    this.v_full = v_full;
    this.v_es = v_es;
    this.generated_last = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    Constraints.constrainArbitrary(
      (v_full != null) || (v_es != null),
      "At least one version range must be provided");

    this.name = Constraints.constrainNotNull(name, "Program name");
    this.units = new HashMap<String, JCGPUnit>();
    this.cycle_cache = new HashSet<String>();
  }

  /**
   * Check for missing, cyclic, and/or duplicate imports.
   */

  private void generatorCheckImports(
    final @Nonnull JCGPUnit unit)
    throws JCGLCompileException
  {
    this.cycle_cache.clear();
    this.generatorCheckImportsActual(unit);
  }

  private void generatorCheckImportsActual(
    final @Nonnull JCGPUnit unit)
    throws JCGLCompileException
  {
    final List<String> imports = unit.getImports();
    for (final String import_name : imports) {
      if (this.cycle_cache.contains(import_name)) {
        throw JCGPGenerator.checkFailureImportCyclic(unit, import_name);
      }
      if (this.units.containsKey(import_name) == false) {
        throw JCGPGenerator.checkFailureImportMissing(unit, import_name);
      }

      final JCGPUnit imported = this.units.get(import_name);
      switch (imported.getType()) {
        case UNIT_FRAGMENT_SHADER_MAIN:
        {
          throw JCGPGenerator.checkFailureImportedNonGeneric(unit, imported);
        }
        case UNIT_GENERIC:
        {
          break;
        }
        case UNIT_VERTEX_SHADER_MAIN:
        {
          throw JCGPGenerator.checkFailureImportedNonGeneric(unit, imported);
        }
      }

      this.cycle_cache.add(import_name);
      this.generatorCheckImportsActual(imported);
      this.cycle_cache.remove(import_name);
    }
  }

  private void generatorCheckVersion(
    final @Nonnull JCGLSLVersionNumber version,
    final @Nonnull JCGLApi api)
    throws JCGLUnsupportedException
  {
    final StringBuilder message = new StringBuilder();

    switch (api) {
      case JCGL_ES:
      {
        if (this.v_es == null) {
          this.generatorLacksVersionSupport(version, api, message);
        }
        if (this.v_es.includes(version) == false) {
          this.generatorLacksVersionSupport(version, api, message);
        }
        break;
      }
      case JCGL_FULL:
      {
        if (this.v_full == null) {
          this.generatorLacksVersionSupport(version, api, message);
        }
        if (this.v_full.includes(version) == false) {
          this.generatorLacksVersionSupport(version, api, message);
        }
        break;
      }
    }
  }

  @Override public void generatorDebuggingEnable(
    final boolean on)
  {
    this.debugging = on;
  }

  @Override public boolean generatorDebuggingIsEnabled()
  {
    return this.debugging;
  }

  @Override public @Nonnull
    JCGPGeneratedSource<JCGLShaderKindFragment>
    generatorGenerateFragmentShader(
      final @Nonnull JCGLSLVersionNumber version,
      final @Nonnull JCGLApi api)
      throws JCGLCompileException,
        ConstraintError,
        JCGLUnsupportedException
  {
    Constraints.constrainNotNull(version, "Version");
    Constraints.constrainNotNull(api, "API");

    if (this.unit_fragment_main == null) {
      throw new JCGLCompileException(
        "<none>",
        "No unit provided a main function for the fragment shader.");
    }

    this.generatorCheckVersion(version, api);
    assert this.unit_fragment_main != null;
    this.generatorCheckImports(this.unit_fragment_main);

    final ArrayList<String> output = new ArrayList<String>(32);
    if (this.debugging) {
      output.add("// main: " + this.unit_fragment_main.getName());
    }

    JCGPGenerator.generatorGenerateVersion(version, api, output);
    this.generatorSourceEvaluate(
      this.unit_fragment_main,
      version,
      api,
      output);

    this.generated_last = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    return new JCGPGeneratedSource<JCGLShaderKindFragment>(
      output,
      version,
      api,
      JCGLShaderType.JCGL_FRAGMENT_SHADER);
  }

  @Override public @Nonnull
    JCGPGeneratedSource<JCGLShaderKindVertex>
    generatorGenerateVertexShader(
      final @Nonnull JCGLSLVersionNumber version,
      final @Nonnull JCGLApi api)
      throws JCGLCompileException,
        ConstraintError,
        JCGLUnsupportedException
  {
    Constraints.constrainNotNull(version, "Version");
    Constraints.constrainNotNull(api, "API");

    if (this.unit_vertex_main == null) {
      throw new JCGLCompileException(
        "<none>",
        "No unit provided a main function for the vertex shader.");
    }

    this.generatorCheckVersion(version, api);
    assert this.unit_vertex_main != null;
    this.generatorCheckImports(this.unit_vertex_main);

    final ArrayList<String> output = new ArrayList<String>(32);
    if (this.debugging) {
      output.add("// main: " + this.unit_vertex_main.getName());
    }

    JCGPGenerator.generatorGenerateVersion(version, api, output);
    this.generatorSourceEvaluate(this.unit_vertex_main, version, api, output);

    this.generated_last = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    return new JCGPGeneratedSource<JCGLShaderKindVertex>(
      output,
      version,
      api,
      JCGLShaderType.JCGL_VERTEX_SHADER);
  }

  private void generatorLacksVersionSupport(
    final @Nonnull JCGLSLVersionNumber version,
    final @Nonnull JCGLApi api,
    final @Nonnull StringBuilder message)
    throws JCGLUnsupportedException
  {
    message.append("Compilation '");
    message.append(this.name);
    message.append("' cannot produce GLSL source for ");
    message.append(api.getName());
    message.append(" shading language ");
    message.append(version.getVersionMajor());
    message.append(".");
    message.append(version.getVersionMinor());
    message.append(".");
    message.append(version.getVersionMicro());
    message.append(".\n");
    this.generatorShowRequires(message);
    throw new JCGLUnsupportedException(message.toString());
  }

  private void generatorShowRequires(
    final @Nonnull StringBuilder output)
  {
    output.append("Compilation of program '");
    output.append(this.name);
    output.append("' requires: ");
    output.append("\n");

    if (this.v_es != null) {
      output.append("  GLSL ES ");
      output.append(this.v_es.toRangeNotation());
      output.append("\n");
    }

    if (this.v_full != null) {
      output.append("  GLSL ");
      output.append(this.v_full.toRangeNotation());
      output.append("\n");
    }
  }

  private void generatorSourceEvaluate(
    final @Nonnull JCGPUnit unit,
    final @Nonnull JCGLSLVersionNumber version,
    final @Nonnull JCGLApi api,
    final @Nonnull ArrayList<String> output)
    throws ConstraintError,
      JCGLCompileException
  {
    try {
      final JCGPGeneratorContext ctx = new JCGPGeneratorContext(version, api);
      ctx.setDebugging(this.debugging);

      this.generatorSourceEvaluateActual(unit, ctx, output);
    } catch (final Exception e) {
      throw new JCGLCompileException(e, unit.getName(), e.getMessage());
    }
  }

  private void generatorSourceEvaluateActual(
    final @Nonnull JCGPUnit unit,
    final @Nonnull JCGPGeneratorContext context,
    final @Nonnull ArrayList<String> output)
    throws ConstraintError,
      Exception
  {
    for (final String import_name : unit.getImports()) {
      final JCGPUnit imported = this.units.get(import_name);
      this.generatorSourceEvaluateActual(imported, context, output);
    }

    unit.evaluate(context, output);
  }

  @Override public void generatorUnitAdd(
    final @Nonnull JCGPUnit unit)
    throws ConstraintError,
      JCGLUnsupportedException
  {
    Constraints.constrainNotNull(unit, "Unit");
    Constraints.constrainArbitrary(
      this.units.containsKey(unit.getName()) == false,
      "Unit name unique");

    final JCGPVersionRange<JCGLApiKindES> range_es = unit.getVersionRangeES();
    final JCGPVersionRange<JCGLApiKindFull> range_full =
      unit.getVersionRangeFull();

    this.unitVersionCheck(unit, range_es, range_full);
    this.unitCheckTypes(unit);

    switch (unit.getType()) {
      case UNIT_FRAGMENT_SHADER_MAIN:
      {
        this.unit_fragment_main = (JCGPUnitFragmentShader) unit;
        break;
      }
      case UNIT_GENERIC:
      {
        break;
      }
      case UNIT_VERTEX_SHADER_MAIN:
      {
        this.unit_vertex_main = (JCGPUnitVertexShader) unit;
        break;
      }
    }

    this.units.put(unit.getName(), unit);
  }

  @Override public void generatorUnitRemove(
    final @Nonnull String unit)
    throws ConstraintError
  {
    Constraints.constrainNotNull(unit, "Unit");

    if (this.unit_fragment_main != null) {
      if (this.unit_fragment_main.getName().equals(unit)) {
        this.unit_fragment_main = null;
      }
    }

    if (this.unit_vertex_main != null) {
      if (this.unit_vertex_main.getName().equals(unit)) {
        this.unit_vertex_main = null;
      }
    }

    if (this.units.containsKey(unit)) {
      this.units.remove(unit);
    }
  }

  @Override public boolean generatorUnitsUpdated()
    throws ConstraintError,
      JCGLCompileException
  {
    try {
      for (final JCGPUnit u : this.units.values()) {
        if (u.updatedSince(this.generated_last)) {
          return true;
        }
      }
      return false;
    } catch (final Exception e) {
      throw new JCGLCompileException(e, "<none>", e.getMessage());
    }
  }

  private void unitCheckTypes(
    final JCGPUnit unit)
    throws ConstraintError
  {
    switch (unit.getType()) {
      case UNIT_FRAGMENT_SHADER_MAIN:
      {
        Constraints.constrainArbitrary(
          this.unit_fragment_main == null,
          "Fragment shader main unit not already defined");
        break;
      }
      case UNIT_GENERIC:
      {
        break;
      }
      case UNIT_VERTEX_SHADER_MAIN:
      {
        Constraints.constrainArbitrary(
          this.unit_vertex_main == null,
          "Vertex shader main unit not already defined");
        break;
      }
    }
  }

  private void unitLacksVersionSupport(
    final @Nonnull JCGPUnit unit,
    final @Nonnull StringBuilder message)
    throws JCGLUnsupportedException
  {
    message.append("Unit '");
    message.append(unit.getName());
    message.append("' lacks the required version support.\n");
    this.generatorShowRequires(message);
    JCGPGenerator.unitShowSupports(unit, message);
    throw new JCGLUnsupportedException(message.toString());
  }

  private void unitVersionCheck(
    final @Nonnull JCGPUnit unit,
    final @CheckForNull JCGPVersionRange<JCGLApiKindES> range_es,
    final @CheckForNull JCGPVersionRange<JCGLApiKindFull> range_full)
    throws JCGLUnsupportedException
  {
    final StringBuilder message = new StringBuilder();

    if (range_es != null) {
      if (this.v_es != null) {
        final boolean lower =
          range_es.includes(this.v_es.getLowerBound()) == false;
        final boolean upper =
          range_es.includes(this.v_es.getUpperBound()) == false;
        if (lower || upper) {
          this.unitLacksVersionSupport(unit, message);
        }
      }
      // Unit supports ES, but no support was required.
    } else {
      if (this.v_es != null) {
        this.unitLacksVersionSupport(unit, message);
      }
      // No ES support required.
    }

    if (range_full != null) {
      if (this.v_full != null) {
        final boolean lower =
          range_full.includes(this.v_full.getLowerBound()) == false;
        final boolean upper =
          range_full.includes(this.v_full.getUpperBound()) == false;
        if (lower || upper) {
          this.unitLacksVersionSupport(unit, message);
        }
      }
      // Unit supports full, but no support was required.
    } else {
      if (this.v_full != null) {
        this.unitLacksVersionSupport(unit, message);
      }
      // No full support required.
    }
  }
}
