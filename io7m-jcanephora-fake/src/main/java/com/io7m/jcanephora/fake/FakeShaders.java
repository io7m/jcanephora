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
import com.io7m.jcanephora.core.JCGLFragmentShaderType;
import com.io7m.jcanephora.core.JCGLFragmentShaderUsableType;
import com.io7m.jcanephora.core.JCGLGeometryShaderType;
import com.io7m.jcanephora.core.JCGLGeometryShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramAttributeType;
import com.io7m.jcanephora.core.JCGLProgramShaderType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jnull.NullCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

final class FakeShaders implements JCGLShadersType
{
  private static final Logger  LOG;
  private static final Pattern NON_EMPTY;

  static {
    LOG = LoggerFactory.getLogger(FakeShaders.class);
    NON_EMPTY = Pattern.compile("^\\s*$");
  }

  private final FakeContext                 context;
  private final FakeShaderListenerType      listener;
  private       JCGLProgramShaderUsableType current;

  FakeShaders(final FakeContext c)
  {
    this.context = NullCheck.notNull(c);
    this.listener = NullCheck.notNull(c.getShaderListener());
  }

  private static boolean isEmpty(final List<String> lines)
  {
    NullCheck.notNull(lines, "Lines");

    for (final String line : lines) {
      NullCheck.notNull(line, "Line");
      if (!FakeShaders.NON_EMPTY.matcher(line).matches()) {
        return false;
      }
    }

    return true;
  }

  @Override public void shaderDeleteProgram(final JCGLProgramShaderType p)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(p);

    FakeCompatibilityChecks.checkProgramShader(this.context, p);
    JCGLResources.checkNotDeleted(p);

    FakeShaders.LOG.debug("delete program shader {}", p.getName());
    ((FakeObjectDeletable) p).setDeleted();

    if (p.equals(this.current)) {
      this.current = null;
    }
  }

  @Override public void shaderDeleteVertex(final JCGLVertexShaderType v)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(v);
    FakeCompatibilityChecks.checkVertexShader(this.context, v);
    JCGLResources.checkNotDeleted(v);

    ((FakeObjectDeletable) v).setDeleted();
  }

  @Override public void shaderDeleteFragment(final JCGLFragmentShaderType f)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(f);
    FakeCompatibilityChecks.checkFragmentShader(this.context, f);
    JCGLResources.checkNotDeleted(f);

    ((FakeObjectDeletable) f).setDeleted();
  }

  @Override public void shaderDeleteGeometry(final JCGLGeometryShaderType g)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(g);
    FakeCompatibilityChecks.checkGeometryShader(this.context, g);
    JCGLResources.checkNotDeleted(g);

    ((FakeObjectDeletable) g).setDeleted();
  }

  @Override public JCGLVertexShaderType shaderCompileVertex(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();
    FakeShaders.LOG.debug(
      "compile vertex shader {} ({} lines)", name, Integer.valueOf(size));

    if (FakeShaders.isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    this.listener.onCompileVertexShaderStart(this.context, name, lines);
    return new FakeVertexShader(
      this.context, this.context.getFreshID(), name, lines);
  }

  @Override public JCGLFragmentShaderType shaderCompileFragment(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();
    FakeShaders.LOG.debug(
      "compile fragment shader {} ({} lines)", name, Integer.valueOf(size));

    if (FakeShaders.isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    this.listener.onCompileFragmentShaderStart(this.context, name, lines);
    return new FakeFragmentShader(
      this.context, this.context.getFreshID(), name, lines);
  }

  @Override public JCGLGeometryShaderType shaderCompileGeometry(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    NullCheck.notNull(name, "Name");
    NullCheck.notNullAll(lines, "Lines");

    final int size = lines.size();
    FakeShaders.LOG.debug(
      "compile geometry shader {} ({} lines)", name, Integer.valueOf(size));

    if (FakeShaders.isEmpty(lines)) {
      throw new JCGLExceptionProgramCompileError(name, "Empty program");
    }

    this.listener.onCompileGeometryShaderStart(this.context, name, lines);
    return new FakeGeometryShader(
      this.context, this.context.getFreshID(), name, lines);
  }

  @Override public JCGLProgramShaderType shaderLinkProgram(
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

    FakeShaders.LOG.debug("link program {}", name);
    FakeShaders.LOG.debug("[{}] vertex {}", name, v.getName());
    g.ifPresent(
      gg -> FakeShaders.LOG.debug("[{}] geometry {}", name, gg.getName()));
    FakeShaders.LOG.debug("[{}] fragment {}", name, f.getName());

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
      FakeShaders.LOG.trace(
        "[{}] attribute {} {} {}",
        name,
        a.getName(),
        Integer.valueOf(a.getGLName()),
        a.getType());
    }

    for (final String k : uniforms.keySet()) {
      final JCGLProgramUniformType a = uniforms.get(k);
      FakeShaders.LOG.trace(
        "[{}] uniform {} {} {}",
        name,
        a.getName(),
        Integer.valueOf(a.getGLName()),
        a.getType());
    }

    return p;
  }

  @Override
  public void shaderActivateProgram(final JCGLProgramShaderUsableType p)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(p);

    FakeShaders.LOG.trace("activate {}", p.getName());
    FakeCompatibilityChecks.checkProgramShader(this.context, p);
    JCGLResources.checkNotDeleted(p);
    this.current = p;
  }

  @Override public void shaderDeactivateProgram()
    throws JCGLException
  {
    FakeShaders.LOG.trace("deactivate");
    this.current = null;
  }

  @Override
  public Optional<JCGLProgramShaderUsableType> shaderActivatedProgram()
    throws JCGLException
  {
    return Optional.ofNullable(this.current);
  }
}
