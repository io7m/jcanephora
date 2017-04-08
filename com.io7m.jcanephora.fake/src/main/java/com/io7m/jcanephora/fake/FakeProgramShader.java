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

import com.io7m.jcanephora.core.JCGLProgramAttributeType;
import com.io7m.jcanephora.core.JCGLProgramShaderType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jnull.NullCheck;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

final class FakeProgramShader extends FakeObjectShared
  implements JCGLProgramShaderType
{
  private final String name;
  private final Map<String, JCGLProgramAttributeType> attributes;
  private final Map<String, JCGLProgramUniformType> uniforms;
  private final FakeReferenceContainer references;

  FakeProgramShader(
    final FakeContext ctx,
    final int id,
    final String in_name,
    final FakeVertexShader vs,
    final Optional<FakeGeometryShader> gs,
    final FakeFragmentShader fs,
    final Map<String, JCGLProgramAttributeType> in_attributes,
    final Map<String, JCGLProgramUniformType> in_uniforms)
  {
    super(ctx, id);
    this.name = NullCheck.notNull(in_name, "Name");
    this.attributes = NullCheck.notNull(in_attributes, "Attributes");
    this.uniforms = NullCheck.notNull(in_uniforms, "Uniforms");
    this.references = new FakeReferenceContainer(this, 3);
    this.references.referenceAdd(vs);
    this.references.referenceAdd(fs);
    gs.ifPresent(this.references::referenceAdd);
  }

  @Override
  public String getName()
  {
    return this.name;
  }

  @Override
  public Map<String, JCGLProgramAttributeType> getAttributes()
  {
    return this.attributes;
  }

  @Override
  public Map<String, JCGLProgramUniformType> getUniforms()
  {
    return this.uniforms;
  }

  @Override
  public Set<JCGLReferableType> getReferences()
  {
    return this.references.getReferences();
  }
}
