/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.core.JCGLExceptionProgramNotActive;
import com.io7m.jcanephora.core.JCGLExceptionProgramTypeError;
import com.io7m.jcanephora.core.JCGLFragmentShaderType;
import com.io7m.jcanephora.core.JCGLFragmentShaderUsableType;
import com.io7m.jcanephora.core.JCGLGeometryShaderType;
import com.io7m.jcanephora.core.JCGLGeometryShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramAttributeType;
import com.io7m.jcanephora.core.JCGLProgramShaderType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLType;
import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.core.parameterized.matrices.PMatrix3x3D;
import com.io7m.jtensors.core.parameterized.matrices.PMatrix4x4D;
import com.io7m.jtensors.core.parameterized.vectors.PVector2D;
import com.io7m.jtensors.core.parameterized.vectors.PVector2I;
import com.io7m.jtensors.core.parameterized.vectors.PVector3D;
import com.io7m.jtensors.core.parameterized.vectors.PVector3I;
import com.io7m.jtensors.core.parameterized.vectors.PVector4D;
import com.io7m.jtensors.core.parameterized.vectors.PVector4I;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix3x3D;
import com.io7m.jtensors.core.unparameterized.matrices.Matrix4x4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector2I;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector3I;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vector4I;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

final class FakeShaders implements JCGLShadersType
{
  private static final Logger LOG;
  private static final Pattern NON_EMPTY;

  static {
    LOG = LoggerFactory.getLogger(FakeShaders.class);
    NON_EMPTY = Pattern.compile("^\\s*$");
  }

  private final FakeContext context;
  private final FakeShaderListenerType listener;
  private boolean check_active;
  private boolean check_type;
  private JCGLProgramShaderUsableType current;

  FakeShaders(final FakeContext c)
  {
    this.context = NullCheck.notNull(c, "Context");
    this.listener = NullCheck.notNull(c.getShaderListener(), "Shader listener");
    this.check_active = true;
    this.check_type = true;
  }

  private static boolean isEmpty(final List<String> lines)
  {
    NullCheck.notNull(lines, "Lines");

    for (final String line : lines) {
      NullCheck.notNull(line, "Line");
      if (!NON_EMPTY.matcher(line).matches()) {
        return false;
      }
    }

    return true;
  }

  private static JCGLExceptionProgramTypeError errorWrongType(
    final JCGLProgramUniformType u,
    final JCGLType t)
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("Uniform type error.");
    sb.append(System.lineSeparator());
    sb.append("Expected: ");
    sb.append(u.type());
    sb.append(System.lineSeparator());
    sb.append("Actual: ");
    sb.append(t);
    return new JCGLExceptionProgramTypeError(sb.toString());
  }

  @Override
  public void shaderDeleteProgram(final JCGLProgramShaderType p)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(p, "Shader");

    FakeCompatibilityChecks.checkProgramShader(this.context, p);
    JCGLResources.checkNotDeleted(p);

    LOG.debug("delete program shader {}", p.name());
    ((FakeObjectDeletable) p).setDeleted();

    if (Objects.equals(p, this.current)) {
      this.current = null;
    }
  }

  @Override
  public void shaderDeleteVertex(final JCGLVertexShaderType v)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(v, "Shader");
    FakeCompatibilityChecks.checkVertexShader(this.context, v);
    JCGLResources.checkNotDeleted(v);

    ((FakeObjectDeletable) v).setDeleted();
  }

  @Override
  public void shaderDeleteFragment(final JCGLFragmentShaderType f)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(f, "Shader");
    FakeCompatibilityChecks.checkFragmentShader(this.context, f);
    JCGLResources.checkNotDeleted(f);

    ((FakeObjectDeletable) f).setDeleted();
  }

  @Override
  public void shaderDeleteGeometry(final JCGLGeometryShaderType g)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(g, "Shader");
    FakeCompatibilityChecks.checkGeometryShader(this.context, g);
    JCGLResources.checkNotDeleted(g);

    ((FakeObjectDeletable) g).setDeleted();
  }

  @Override
  public JCGLVertexShaderType shaderCompileVertex(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();
    LOG.debug(
      "compile vertex shader {} ({} lines)", name, Integer.valueOf(size));

    if (isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    this.listener.onCompileVertexShaderStart(this.context, name, lines);
    return new FakeVertexShader(
      this.context, this.context.getFreshID(), name, lines);
  }

  @Override
  public JCGLFragmentShaderType shaderCompileFragment(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();
    LOG.debug(
      "compile fragment shader {} ({} lines)", name, Integer.valueOf(size));

    if (isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    this.listener.onCompileFragmentShaderStart(this.context, name, lines);
    return new FakeFragmentShader(
      this.context, this.context.getFreshID(), name, lines);
  }

  @Override
  public JCGLGeometryShaderType shaderCompileGeometry(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();
    LOG.debug(
      "compile geometry shader {} ({} lines)", name, Integer.valueOf(size));

    if (isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    this.listener.onCompileGeometryShaderStart(this.context, name, lines);
    return new FakeGeometryShader(
      this.context, this.context.getFreshID(), name, lines);
  }

  @Override
  public JCGLProgramShaderType shaderLinkProgram(
    final String name,
    final JCGLVertexShaderUsableType iv,
    final Optional<JCGLGeometryShaderUsableType> ig,
    final JCGLFragmentShaderUsableType ifs)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNull(iv, "Vertex shader");
    NullCheck.notNull(ig, "Geometry shader");
    NullCheck.notNull(ifs, "Fragment shader");

    final FakeVertexShader v =
      FakeCompatibilityChecks.checkVertexShader(this.context, iv);
    JCGLResources.checkNotDeleted(v);
    final FakeFragmentShader f =
      FakeCompatibilityChecks.checkFragmentShader(this.context, ifs);
    JCGLResources.checkNotDeleted(f);

    final Optional<FakeGeometryShader> g = ig.map(
      gg -> {
        final FakeGeometryShader k =
          FakeCompatibilityChecks.checkGeometryShader(this.context, gg);
        JCGLResources.checkNotDeleted(k);
        return k;
      });

    LOG.debug("link program {}", name);
    LOG.debug("[{}] vertex {}", name, v.name());
    g.ifPresent(
      gg -> LOG.debug("[{}] geometry {}", name, gg.name()));
    LOG.debug("[{}] fragment {}", name, f.name());

    final Map<String, JCGLProgramAttributeType> attributes = new HashMap<>();
    final Map<String, JCGLProgramUniformType> uniforms = new HashMap<>();

    final FakeProgramShader p = new FakeProgramShader(
      this.context,
      this.context.getFreshID(),
      name,
      v,
      g,
      f,
      attributes,
      uniforms);

    this.listener.onLinkProgram(
      this.context, p, name, v, g.map(gg -> gg), f, attributes, uniforms);

    for (final String k : attributes.keySet()) {
      final JCGLProgramAttributeType a = attributes.get(k);
      LOG.trace(
        "[{}] attribute {} {} {}",
        name,
        a.name(),
        Integer.valueOf(a.glName()),
        a.type());
    }

    for (final String k : uniforms.keySet()) {
      final JCGLProgramUniformType a = uniforms.get(k);
      LOG.trace(
        "[{}] uniform {} {} {}",
        name,
        a.name(),
        Integer.valueOf(a.glName()),
        a.type());
    }

    return p;
  }

  @Override
  public void shaderActivateProgram(final JCGLProgramShaderUsableType p)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(p, "Shader");

    LOG.trace("activate {}", p.name());
    FakeCompatibilityChecks.checkProgramShader(this.context, p);
    JCGLResources.checkNotDeleted(p);
    this.current = p;
  }

  @Override
  public void shaderDeactivateProgram()
    throws JCGLException
  {
    LOG.trace("deactivate");
    this.current = null;
  }

  @Override
  public Optional<JCGLProgramShaderUsableType> shaderActivatedProgram()
    throws JCGLException
  {
    return Optional.ofNullable(this.current);
  }

  @Override
  public void shaderUniformSetTypeCheckingEnabled(final boolean enabled)
  {
    this.check_type = enabled;
  }

  @Override
  public void shaderUniformSetActivityCheckingEnabled(final boolean enabled)
  {
    this.check_active = enabled;
  }

  @Override
  public void shaderUniformPutFloat(
    final JCGLProgramUniformType u,
    final float value)
    throws JCGLException
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT);
  }

  @Override
  public void shaderUniformPutInteger(
    final JCGLProgramUniformType u,
    final int value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER);
  }

  @Override
  public void shaderUniformPutUnsignedInteger(
    final JCGLProgramUniformType u,
    final int value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER);
  }

  @Override
  public void shaderUniformPutVectorf(
    final JCGLProgramUniformType u,
    final FloatBuffer value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActive(u);
    this.checkIsFloatingPoint(u);

    final int available = value.capacity() / 4;
    final JCGLType type = u.type();
    final int required = type.getElementCount();
    if (available < required) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Uniform data error.");
      sb.append(System.lineSeparator());
      sb.append("Expected: A buffer containing at least ");
      sb.append(required);
      sb.append(" floating point values");
      sb.append(System.lineSeparator());
      sb.append("Actual: A buffer containing ");
      sb.append(available);
      sb.append(" floating point values");
      throw new JCGLExceptionProgramTypeError(sb.toString());
    }
  }

  @Override
  public void shaderUniformPutVector2f(
    final JCGLProgramUniformType u,
    final Vector2D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_2);
  }

  @Override
  public void shaderUniformPutVector3f(
    final JCGLProgramUniformType u,
    final Vector3D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_3);
  }

  @Override
  public void shaderUniformPutVector4f(
    final JCGLProgramUniformType u,
    final Vector4D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_4);
  }

  @Override
  public void shaderUniformPutVector2i(
    final JCGLProgramUniformType u,
    final Vector2I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_2);
  }

  @Override
  public void shaderUniformPutVector3i(
    final JCGLProgramUniformType u,
    final Vector3I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_3);
  }

  @Override
  public void shaderUniformPutVector4i(
    final JCGLProgramUniformType u,
    final Vector4I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_4);
  }

  @Override
  public void shaderUniformPutVector2ui(
    final JCGLProgramUniformType u,
    final Vector2I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2);
  }

  @Override
  public void shaderUniformPutVector3ui(
    final JCGLProgramUniformType u,
    final Vector3I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3);
  }

  @Override
  public void shaderUniformPutVector4ui(
    final JCGLProgramUniformType u,
    final Vector4I value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4);
  }

  @Override
  public <T> void shaderUniformPutPVector2f(
    final JCGLProgramUniformType u,
    final PVector2D<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_2);
  }

  @Override
  public <T> void shaderUniformPutPVector3f(
    final JCGLProgramUniformType u,
    final PVector3D<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_3);
  }

  @Override
  public <T> void shaderUniformPutPVector4f(
    final JCGLProgramUniformType u,
    final PVector4D<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_VECTOR_4);
  }

  @Override
  public <T> void shaderUniformPutPVector2i(
    final JCGLProgramUniformType u,
    final PVector2I<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_2);
  }

  @Override
  public <T> void shaderUniformPutPVector3i(
    final JCGLProgramUniformType u,
    final PVector3I<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_3);
  }

  @Override
  public <T> void shaderUniformPutPVector4i(
    final JCGLProgramUniformType u,
    final PVector4I<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_INTEGER_VECTOR_4);
  }

  @Override
  public <T> void shaderUniformPutPVector2ui(
    final JCGLProgramUniformType u,
    final PVector2I<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2);
  }

  @Override
  public <T> void shaderUniformPutPVector3ui(
    final JCGLProgramUniformType u,
    final PVector3I<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3);
  }

  @Override
  public <T> void shaderUniformPutPVector4ui(
    final JCGLProgramUniformType u,
    final PVector4I<T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4);
  }

  @Override
  public void shaderUniformPutMatrix3x3f(
    final JCGLProgramUniformType u,
    final Matrix3x3D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_MATRIX_3);
  }

  @Override
  public <S, T> void shaderUniformPutPMatrix3x3f(
    final JCGLProgramUniformType u,
    final PMatrix3x3D<S, T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_MATRIX_3);
  }

  @Override
  public void shaderUniformPutMatrix4x4f(
    final JCGLProgramUniformType u,
    final Matrix4x4D value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_MATRIX_4);
  }

  @Override
  public <S, T> void shaderUniformPutPMatrix4x4f(
    final JCGLProgramUniformType u,
    final PMatrix4x4D<S, T> value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_FLOAT_MATRIX_4);
  }

  @Override
  public void shaderUniformPutTexture2DUnit(
    final JCGLProgramUniformType u,
    final JCGLTextureUnitType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_SAMPLER_2D);
  }

  @Override
  public void shaderUniformPutTextureCubeUnit(
    final JCGLProgramUniformType u,
    final JCGLTextureUnitType value)
    throws
    JCGLException,
    JCGLExceptionProgramNotActive,
    JCGLExceptionProgramTypeError
  {
    this.checkActiveAndType(u, JCGLType.TYPE_SAMPLER_CUBE);
  }

  private void checkIsFloatingPoint(final JCGLProgramUniformType u)
  {
    if (this.check_type) {
      final JCGLType type_uniform = u.type();
      if (!type_uniform.isFloatingPointType()) {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("Uniform type error.");
        sb.append(System.lineSeparator());
        sb.append("Expected: A floating point type");
        sb.append(System.lineSeparator());
        sb.append("Actual: ");
        sb.append(type_uniform);
        throw new JCGLExceptionProgramTypeError(sb.toString());
      }
    }
  }

  private void checkActiveAndType(
    final JCGLProgramUniformType u,
    final JCGLType t)
  {
    this.checkActive(u);
    this.checkType(u, t);
  }

  private void checkType(
    final JCGLProgramUniformType u,
    final JCGLType t)
  {
    if (this.check_type) {
      if (!Objects.equals(u.type(), t)) {
        throw errorWrongType(u, t);
      }
    }
  }

  private void checkActive(final JCGLProgramUniformType u)
  {
    final JCGLProgramShaderUsableType u_program = u.program();
    if (this.check_active) {
      if (!Objects.equals(u_program, this.current)) {
        throw this.errorNotActive(u_program);
      }
    }
  }

  private JCGLExceptionProgramNotActive errorNotActive(
    final JCGLProgramShaderUsableType u_program)
  {
    final StringBuilder sb = new StringBuilder(128);
    sb.append("Program not active.");
    sb.append(System.lineSeparator());
    sb.append("Expected: ");
    sb.append(u_program);
    sb.append(System.lineSeparator());
    sb.append("Actual: ");
    sb.append(this.current);
    return new JCGLExceptionProgramNotActive(sb.toString());
  }
}
