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
import com.io7m.jcanephora.core.JCGLProgramShaderType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnimplementedCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

final class FakeShaders implements JCGLShadersType
{
  private static final Logger  LOG;
  private static final Pattern NON_EMPTY;

  static {
    LOG = LoggerFactory.getLogger(FakeShaders.class);
    NON_EMPTY = Pattern.compile("^\\s*$");
  }

  private final FakeContext   context;
  private final AtomicInteger next_id;

  FakeShaders(final FakeContext c)
  {
    this.context = NullCheck.notNull(c);
    this.next_id = new AtomicInteger(1);
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
    // TODO: Generated method stub!
    throw new UnimplementedCodeException();
  }

  @Override public void shaderDeleteVertex(final JCGLVertexShaderType v)
    throws JCGLException, JCGLExceptionDeleted
  {
    // TODO: Generated method stub!
    throw new UnimplementedCodeException();
  }

  @Override public void shaderDeleteFragment(final JCGLFragmentShaderType f)
    throws JCGLException, JCGLExceptionDeleted
  {
    // TODO: Generated method stub!
    throw new UnimplementedCodeException();
  }

  @Override public void shaderDeleteGeometry(final JCGLGeometryShaderType g)
    throws JCGLException, JCGLExceptionDeleted
  {
    // TODO: Generated method stub!
    throw new UnimplementedCodeException();
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

    // TODO: Generated method stub!
    throw new UnimplementedCodeException();
  }

  @Override public JCGLFragmentShaderType shaderCompileFragment(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    // TODO: Generated method stub!
    throw new UnimplementedCodeException();
  }

  @Override public JCGLGeometryShaderType shaderCompileGeometry(
    final String name,
    final List<String> lines)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    // TODO: Generated method stub!
    throw new UnimplementedCodeException();
  }

  @Override public JCGLProgramShaderType shaderLinkProgram(
    final String name,
    final JCGLVertexShaderUsableType v,
    final Optional<JCGLGeometryShaderUsableType> g,
    final JCGLFragmentShaderUsableType f)
    throws JCGLExceptionProgramCompileError, JCGLException
  {
    // TODO: Generated method stub!
    throw new UnimplementedCodeException();
  }

  @Override
  public void shaderActivateProgram(final JCGLProgramShaderUsableType p)
    throws JCGLException, JCGLExceptionDeleted
  {
    // TODO: Generated method stub!
    throw new UnimplementedCodeException();
  }

  @Override public void shaderDeactivateProgram()
    throws JCGLException
  {
    // TODO: Generated method stub!
    throw new UnimplementedCodeException();
  }

  @Override
  public Optional<JCGLProgramShaderUsableType> shaderActivatedProgram()
    throws JCGLException
  {
    // TODO: Generated method stub!
    throw new UnimplementedCodeException();
  }
}
