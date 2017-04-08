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

package com.io7m.jcanephora.tests.fake;

import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLFragmentShaderUsableType;
import com.io7m.jcanephora.core.JCGLGeometryShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramAttributeType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLVertexShaderUsableType;
import com.io7m.jcanephora.fake.FakeContext;
import com.io7m.jcanephora.fake.FakeShaderListenerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

class FakeDefaultShaderListener implements FakeShaderListenerType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(FakeDefaultShaderListener.class);
  }

  FakeDefaultShaderListener()
  {

  }

  @Override
  public void onCompileFragmentShaderStart(
    final FakeContext context,
    final String name,
    final List<String> sources)
    throws JCGLException
  {
    FakeDefaultShaderListener.LOG.debug(
      "onCompileFragmentShaderStart: {}", name);
  }

  @Override
  public void onCompileVertexShaderStart(
    final FakeContext context,
    final String name,
    final List<String> sources)
    throws JCGLException
  {
    FakeDefaultShaderListener.LOG.debug(
      "onCompileVertexShaderStart: {}", name);
  }

  @Override
  public void onCompileGeometryShaderStart(
    final FakeContext context,
    final String name,
    final List<String> sources)
    throws JCGLException
  {
    FakeDefaultShaderListener.LOG.debug(
      "onCompileGeometryShaderStart: {}", name);
  }

  @Override
  public void onLinkProgram(
    final FakeContext context,
    final JCGLProgramShaderUsableType p,
    final String name,
    final JCGLVertexShaderUsableType v,
    final Optional<JCGLGeometryShaderUsableType> g,
    final JCGLFragmentShaderUsableType f,
    final Map<String, JCGLProgramAttributeType> attributes,
    final Map<String, JCGLProgramUniformType> uniforms)
  {
    FakeDefaultShaderListener.LOG.debug(
      "onLinkProgram: {}", name);
  }
}
