/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jcanephora.core.JCGLProgramAttributeType;
import com.io7m.jcanephora.core.JCGLProgramShaderType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jnull.NullCheck;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

final class LWJGL3ProgramShader extends LWJGL3ObjectShared
  implements JCGLProgramShaderType
{
  private final String name;
  private final LWJGL3ReferenceContainer references;
  private Map<String, JCGLProgramAttributeType> attributes;
  private Map<String, JCGLProgramUniformType> uniforms;

  LWJGL3ProgramShader(
    final LWJGL3Context ctx,
    final int id,
    final String in_name,
    final LWJGL3VertexShader vs,
    final Optional<LWJGL3GeometryShader> gs,
    final LWJGL3FragmentShader fs)
  {
    super(ctx, id);
    this.name = NullCheck.notNull(in_name);
    this.references = new LWJGL3ReferenceContainer(this, 3);
    this.references.referenceAdd(vs);
    gs.ifPresent(this.references::referenceAdd);
    this.references.referenceAdd(fs);
  }

  static LWJGL3ProgramShader checkProgramShader(
    final LWJGL3Context c,
    final JCGLProgramShaderUsableType p)
  {
    return (LWJGL3ProgramShader) LWJGL3CompatibilityChecks.checkAny(c, p);
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("[ProgramShader ");
    sb.append(super.getGLName());
    sb.append(" ");
    sb.append(this.name);
    sb.append(']');
    return sb.toString();
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

  void setAttributes(
    final Map<String, JCGLProgramAttributeType> in_attributes)
  {
    this.attributes =
      Collections.unmodifiableMap(NullCheck.notNull(in_attributes));
  }

  @Override
  public Map<String, JCGLProgramUniformType> getUniforms()
  {
    return this.uniforms;
  }

  void setUniforms(
    final Map<String, JCGLProgramUniformType> in_uniforms)
  {
    this.uniforms = Collections.unmodifiableMap(NullCheck.notNull(in_uniforms));
  }

  @Override
  public Set<JCGLReferableType> getReferences()
  {
    return this.references.getReferences();
  }
}
