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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.core.JCGLExceptionProgramNotActive;
import com.io7m.jcanephora.core.JCGLExceptionProgramTypeError;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLFragmentShaderType;
import com.io7m.jcanephora.core.JCGLGeometryShaderType;
import com.io7m.jcanephora.core.JCGLProgramAttributeType;
import com.io7m.jcanephora.core.JCGLProgramShaderType;
import com.io7m.jcanephora.core.JCGLProgramShaderUsableType;
import com.io7m.jcanephora.core.JCGLProgramUniformType;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jcanephora.core.JCGLReferenceContainerType;
import com.io7m.jcanephora.core.JCGLType;
import com.io7m.jcanephora.core.JCGLVertexShaderType;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLShadersType;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jnull.NullCheck;
import com.io7m.jtensors.core.parameterized.matrices.PMatrices3x3D;
import com.io7m.jtensors.core.parameterized.matrices.PMatrices4x4D;
import com.io7m.jtensors.core.unparameterized.matrices.Matrices3x3D;
import com.io7m.jtensors.core.unparameterized.matrices.Matrices4x4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors2D;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors2I;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors3D;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors3I;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors4D;
import com.io7m.jtensors.core.unparameterized.vectors.Vectors4I;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Shaders contract.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLShadersContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  private static void checkAttribute(
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramAttributeType> a,
    final String f,
    final JCGLType t)
  {
    Assert.assertTrue(a.containsKey(f));
    final JCGLProgramAttributeType v = a.get(f);
    Assert.assertTrue(v.glName() >= 0);
    Assert.assertEquals(f, v.name());
    Assert.assertEquals(t, v.type());
    Assert.assertEquals(p, v.program());
  }

  private static void checkUniform(
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramUniformType> u,
    final String f,
    final JCGLType t,
    final int size)
  {
    Assert.assertTrue(u.containsKey(f));
    final JCGLProgramUniformType v = u.get(f);
    Assert.assertTrue(v.glName() >= 0);
    Assert.assertEquals(f, v.name());
    Assert.assertEquals(t, v.type());
    Assert.assertEquals(p, v.program());
    Assert.assertEquals(size, v.size());
  }

  protected abstract Interfaces getInterfaces(String name);

  protected abstract JCGLShadersType getShaders(String name);

  protected abstract JCGLUnsharedContextPair<JCGLShadersType>
  getShadersUnshared(
    String main,
    String alt);

  protected abstract JCGLSharedContextPair<JCGLShadersType>
  getShadersSharedWith(
    String name,
    String shared);

  protected abstract List<String> getShaderLines(String name);

  @Test
  public final void testVertexShaderCompileEmpty0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileVertex("empty0", new ArrayList<>(0));
  }

  @Test
  public final void testVertexShaderCompileEmpty1()
  {
    final JCGLShadersType s = this.getShaders("main");

    final List<String> lines = new ArrayList<>(3);
    lines.add(" ");
    lines.add("\t");
    lines.add("\n");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileVertex("empty1", lines);
  }

  @Test
  public final void testVertexShaderCompileInvalid0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileVertex("invalid0", this.getShaderLines("invalid0.vert"));
  }

  @Test
  public final void testVertexShaderCompileValidIdentities()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    Assert.assertEquals("valid0", v.name());
    Assert.assertFalse(v.isDeleted());
    Assert.assertTrue(v.glName() > 0);
  }

  @Test
  public final void testVertexShaderDeleteIdentity()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    s.shaderDeleteVertex(v);
    Assert.assertTrue(v.isDeleted());
  }

  @Test
  public final void testVertexShaderDeleteDeleted()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    s.shaderDeleteVertex(v);

    this.expected.expect(JCGLExceptionDeleted.class);
    s.shaderDeleteVertex(v);
  }

  @Test
  public final void testVertexShaderDeleteWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    Assert.assertFalse(c0.contextIsCurrent());
    Assert.assertTrue(c1.contextIsCurrent());

    c1.contextReleaseCurrent();
    c0.contextMakeCurrent();
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderDeleteVertex(v);
  }

  @Test
  public final void testVertexShaderDeleteSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    Assert.assertTrue(c0.contextIsCurrent());
    Assert.assertFalse(c1.contextIsCurrent());

    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    s1.shaderDeleteVertex(v);

    Assert.assertTrue(v.isDeleted());
  }

  @Test
  public final void testFragmentShaderCompileEmpty0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileFragment("empty0", new ArrayList<>(0));
  }

  @Test
  public final void testFragmentShaderCompileEmpty1()
  {
    final JCGLShadersType s = this.getShaders("main");

    final List<String> lines = new ArrayList<>(3);
    lines.add(" ");
    lines.add("\t");
    lines.add("\n");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileFragment("empty1", lines);
  }

  @Test
  public final void testFragmentShaderCompileInvalid0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileFragment("invalid0", this.getShaderLines("invalid0.frag"));
  }

  @Test
  public final void testFragmentShaderCompileValidIdentities()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    Assert.assertEquals("valid0", f.name());
    Assert.assertFalse(f.isDeleted());
    Assert.assertTrue(f.glName() > 0);
  }

  @Test
  public final void testFragmentShaderDeleteSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    Assert.assertTrue(c0.contextIsCurrent());
    Assert.assertFalse(c1.contextIsCurrent());

    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    s1.shaderDeleteFragment(f);

    Assert.assertTrue(f.isDeleted());
  }

  @Test
  public final void testFragmentShaderDeleteWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    Assert.assertFalse(c0.contextIsCurrent());
    Assert.assertTrue(c1.contextIsCurrent());

    c1.contextReleaseCurrent();
    c0.contextMakeCurrent();
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderDeleteFragment(f);
  }

  @Test
  public final void testFragmentShaderDeleteIdentity()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    s.shaderDeleteFragment(f);

    Assert.assertTrue(f.isDeleted());
  }

  @Test
  public final void testFragmentShaderDeleteDeleted()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    s.shaderDeleteFragment(f);

    this.expected.expect(JCGLExceptionDeleted.class);
    s.shaderDeleteFragment(f);
  }

  @Test
  public final void testGeometryShaderCompileEmpty0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileGeometry("empty0", new ArrayList<>(0));
  }

  @Test
  public final void testGeometryShaderCompileEmpty1()
  {
    final JCGLShadersType s = this.getShaders("main");

    final List<String> lines = new ArrayList<>(3);
    lines.add(" ");
    lines.add("\t");
    lines.add("\n");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileGeometry("empty1", lines);
  }

  @Test
  public final void testGeometryShaderCompileInvalid0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileGeometry("invalid0", this.getShaderLines("invalid0.geom"));
  }

  @Test
  public final void testGeometryShaderDeleteDeleted()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLGeometryShaderType g =
      s.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    s.shaderDeleteGeometry(g);

    this.expected.expect(JCGLExceptionDeleted.class);
    s.shaderDeleteGeometry(g);
  }

  @Test
  public final void testGeometryShaderDeleteIdentity()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLGeometryShaderType g =
      s.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    s.shaderDeleteGeometry(g);

    Assert.assertTrue(g.isDeleted());
  }

  @Test
  public final void testGeometryShaderDeleteWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    Assert.assertFalse(c0.contextIsCurrent());
    Assert.assertTrue(c1.contextIsCurrent());

    c1.contextReleaseCurrent();
    c0.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderDeleteGeometry(g);
  }

  @Test
  public final void testGeometryShaderDeleteSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    Assert.assertTrue(c0.contextIsCurrent());
    Assert.assertFalse(c1.contextIsCurrent());

    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    s1.shaderDeleteGeometry(g);

    Assert.assertTrue(g.isDeleted());
  }

  @Test
  public final void testGeometryShaderCompileValidIdentities()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLGeometryShaderType g =
      s.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    Assert.assertEquals("valid0", g.name());
    Assert.assertFalse(g.isDeleted());
    Assert.assertTrue(g.glName() > 0);
  }

  @Test
  public final void testProgramLinkValidIdentities()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLGeometryShaderType g =
      s.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("valid0", v, Optional.of(g), f);

    Assert.assertEquals("valid0", p.name());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.glName() > 0);

    final Set<JCGLReferableType> p_refs = p.references();
    Assert.assertEquals(3L, (long) p_refs.size());
    Assert.assertTrue(p_refs.contains(f));
    Assert.assertTrue(p_refs.contains(v));
    Assert.assertTrue(p_refs.contains(g));

    final Set<JCGLReferenceContainerType> v_refs = v.referringContainers();
    Assert.assertEquals(1L, (long) v_refs.size());
    Assert.assertTrue(v_refs.contains(p));

    final Set<JCGLReferenceContainerType> f_refs = f.referringContainers();
    Assert.assertEquals(1L, (long) f_refs.size());
    Assert.assertTrue(f_refs.contains(p));

    final Map<String, JCGLProgramAttributeType> a = p.attributes();
    Assert.assertEquals(1L, (long) a.size());

    final Map<String, JCGLProgramUniformType> u = p.uniforms();
    Assert.assertEquals(0L, (long) u.size());
  }

  @Test
  public final void testProgramLinkValidAttributes()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes0", this.getShaderLines("attributes0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes0", v, Optional.empty(), f);

    Assert.assertEquals("attributes0", p.name());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.glName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.attributes();
    Assert.assertEquals(12L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.glName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    checkAttribute(p, a, "f", JCGLType.TYPE_FLOAT);
    checkAttribute(
      p, a, "fv2", JCGLType.TYPE_FLOAT_VECTOR_2);
    checkAttribute(
      p, a, "fv3", JCGLType.TYPE_FLOAT_VECTOR_3);
    checkAttribute(
      p, a, "fv4", JCGLType.TYPE_FLOAT_VECTOR_4);

    checkAttribute(p, a, "i", JCGLType.TYPE_INTEGER);
    checkAttribute(
      p, a, "iv2", JCGLType.TYPE_INTEGER_VECTOR_2);
    checkAttribute(
      p, a, "iv3", JCGLType.TYPE_INTEGER_VECTOR_3);
    checkAttribute(
      p, a, "iv4", JCGLType.TYPE_INTEGER_VECTOR_4);

    checkAttribute(
      p, a, "u", JCGLType.TYPE_UNSIGNED_INTEGER);
    checkAttribute(
      p, a, "uv2", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2);
    checkAttribute(
      p, a, "uv3", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3);
    checkAttribute(
      p, a, "uv4", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4);
  }

  @Test
  public final void testProgramLinkValidAttributesMatrices0()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes1", this.getShaderLines("attributes1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes1", v, Optional.empty(), f);

    Assert.assertEquals("attributes1", p.name());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.glName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.attributes();
    Assert.assertEquals(3L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.glName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    checkAttribute(
      p, a, "m4", JCGLType.TYPE_FLOAT_MATRIX_4);
    checkAttribute(
      p, a, "m3", JCGLType.TYPE_FLOAT_MATRIX_3);
    checkAttribute(
      p, a, "m2", JCGLType.TYPE_FLOAT_MATRIX_2);
  }

  @Test
  public final void testProgramLinkValidAttributesMatrices1()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes2", this.getShaderLines("attributes2.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes2", v, Optional.empty(), f);

    Assert.assertEquals("attributes2", p.name());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.glName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.attributes();
    Assert.assertEquals(3L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.glName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    checkAttribute(
      p, a, "m4", JCGLType.TYPE_FLOAT_MATRIX_4);
    checkAttribute(
      p, a, "m3", JCGLType.TYPE_FLOAT_MATRIX_4x3);
    checkAttribute(
      p, a, "m2", JCGLType.TYPE_FLOAT_MATRIX_4x2);
  }

  @Test
  public final void testProgramLinkValidAttributesMatrices2()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes2", this.getShaderLines("attributes2.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes2", v, Optional.empty(), f);

    Assert.assertEquals("attributes2", p.name());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.glName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.attributes();
    Assert.assertEquals(3L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.glName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    checkAttribute(
      p, a, "m4", JCGLType.TYPE_FLOAT_MATRIX_4);
    checkAttribute(
      p, a, "m3", JCGLType.TYPE_FLOAT_MATRIX_4x3);
    checkAttribute(
      p, a, "m2", JCGLType.TYPE_FLOAT_MATRIX_4x2);
  }

  @Test
  public final void testProgramLinkValidAttributesMatrices3()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes3", this.getShaderLines("attributes3.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes3", v, Optional.empty(), f);

    Assert.assertEquals("attributes3", p.name());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.glName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.attributes();
    Assert.assertEquals(3L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.glName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    checkAttribute(
      p, a, "m4", JCGLType.TYPE_FLOAT_MATRIX_3x4);
    checkAttribute(
      p, a, "m3", JCGLType.TYPE_FLOAT_MATRIX_3);
    checkAttribute(
      p, a, "m2", JCGLType.TYPE_FLOAT_MATRIX_3x2);
  }

  @Test
  public final void testProgramLinkValidAttributesMatrices4()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes4", this.getShaderLines("attributes4.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes4", v, Optional.empty(), f);

    Assert.assertEquals("attributes4", p.name());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.glName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.attributes();
    Assert.assertEquals(3L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.glName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    checkAttribute(
      p, a, "m4", JCGLType.TYPE_FLOAT_MATRIX_2x4);
    checkAttribute(
      p, a, "m3", JCGLType.TYPE_FLOAT_MATRIX_2x3);
    checkAttribute(
      p, a, "m2", JCGLType.TYPE_FLOAT_MATRIX_2);
  }

  @Test
  public final void testProgramLinkValidUniforms0()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    Assert.assertEquals("uniforms0", p.name());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.glName() > 0);

    final Map<String, JCGLProgramUniformType> u = p.uniforms();
    Assert.assertEquals(16L, (long) u.size());

    final Set<Integer> locations = new HashSet<>(u.values().size());
    for (final JCGLProgramUniformType uni : u.values()) {
      final Integer id = Integer.valueOf(uni.glName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    checkUniform(
      p, u, "f", JCGLType.TYPE_FLOAT, 1);
    checkUniform(
      p, u, "fv2", JCGLType.TYPE_FLOAT_VECTOR_2, 1);
    checkUniform(
      p, u, "fv3", JCGLType.TYPE_FLOAT_VECTOR_3, 1);
    checkUniform(
      p, u, "fv4", JCGLType.TYPE_FLOAT_VECTOR_4, 1);

    checkUniform(p, u, "i", JCGLType.TYPE_INTEGER, 1);
    checkUniform(
      p, u, "iv2", JCGLType.TYPE_INTEGER_VECTOR_2, 1);
    checkUniform(
      p, u, "iv3", JCGLType.TYPE_INTEGER_VECTOR_3, 1);
    checkUniform(
      p, u, "iv4", JCGLType.TYPE_INTEGER_VECTOR_4, 1);

    checkUniform(
      p, u, "u", JCGLType.TYPE_UNSIGNED_INTEGER, 1);
    checkUniform(
      p, u, "uv2", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2, 1);
    checkUniform(
      p, u, "uv3", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3, 1);
    checkUniform(
      p, u, "uv4", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4, 1);

    checkUniform(
      p, u, "b", JCGLType.TYPE_BOOLEAN, 1);
    checkUniform(
      p, u, "bv2", JCGLType.TYPE_BOOLEAN_VECTOR_2, 1);
    checkUniform(
      p, u, "bv3", JCGLType.TYPE_BOOLEAN_VECTOR_3, 1);
    checkUniform(
      p, u, "bv4", JCGLType.TYPE_BOOLEAN_VECTOR_4, 1);
  }

  @Test
  public final void testProgramLinkValidUniforms1()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms1", this.getShaderLines("uniforms1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms1", v, Optional.empty(), f);

    Assert.assertEquals("uniforms1", p.name());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.glName() > 0);

    final Map<String, JCGLProgramUniformType> u = p.uniforms();
    Assert.assertEquals(12L, (long) u.size());

    final Set<Integer> locations = new HashSet<>(u.values().size());
    for (final JCGLProgramUniformType uni : u.values()) {
      final Integer id = Integer.valueOf(uni.glName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    checkUniform(
      p, u, "fm4", JCGLType.TYPE_FLOAT_MATRIX_4, 1);
    checkUniform(
      p, u, "fm4x4", JCGLType.TYPE_FLOAT_MATRIX_4, 1);
    checkUniform(
      p, u, "fm4x3", JCGLType.TYPE_FLOAT_MATRIX_4x3, 1);
    checkUniform(
      p, u, "fm4x2", JCGLType.TYPE_FLOAT_MATRIX_4x2, 1);

    checkUniform(
      p, u, "fm3", JCGLType.TYPE_FLOAT_MATRIX_3, 1);
    checkUniform(
      p, u, "fm3x4", JCGLType.TYPE_FLOAT_MATRIX_3x4, 1);
    checkUniform(
      p, u, "fm3x3", JCGLType.TYPE_FLOAT_MATRIX_3, 1);
    checkUniform(
      p, u, "fm3x2", JCGLType.TYPE_FLOAT_MATRIX_3x2, 1);

    checkUniform(
      p, u, "fm2", JCGLType.TYPE_FLOAT_MATRIX_2, 1);
    checkUniform(
      p, u, "fm2x4", JCGLType.TYPE_FLOAT_MATRIX_2x4, 1);
    checkUniform(
      p, u, "fm2x3", JCGLType.TYPE_FLOAT_MATRIX_2x3, 1);
    checkUniform(
      p, u, "fm2x2", JCGLType.TYPE_FLOAT_MATRIX_2, 1);
  }

  @Test
  public final void testProgramLinkValidUniforms2()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms2", this.getShaderLines("uniforms2.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms2", v, Optional.empty(), f);

    Assert.assertEquals("uniforms2", p.name());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.glName() > 0);

    final Map<String, JCGLProgramUniformType> u = p.uniforms();
    Assert.assertEquals(3L, (long) u.size());

    final Set<Integer> locations = new HashSet<>(u.values().size());
    for (final JCGLProgramUniformType uni : u.values()) {
      final Integer id = Integer.valueOf(uni.glName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    checkUniform(
      p, u, "s2", JCGLType.TYPE_SAMPLER_2D, 1);
    checkUniform(
      p, u, "s3", JCGLType.TYPE_SAMPLER_3D, 1);
    checkUniform(
      p, u, "sc", JCGLType.TYPE_SAMPLER_CUBE, 1);
  }

  @Test
  public final void testProgramLinkValidUniforms3()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment(
        "validarray0",
        this.getShaderLines("validarray0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("validarray0", v, Optional.empty(), f);

    Assert.assertEquals("validarray0", p.name());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.glName() > 0);

    final Map<String, JCGLProgramUniformType> u = p.uniforms();
    Assert.assertEquals(1L, (long) u.size());

    final Set<Integer> locations = new HashSet<>(u.values().size());
    for (final JCGLProgramUniformType uni : u.values()) {
      final Integer id = Integer.valueOf(uni.glName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    checkUniform(
      p, u, "big_array[0]", JCGLType.TYPE_FLOAT_VECTOR_4, 16);
  }

  @Test
  public final void testProgramLinkDeletedVertex()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLGeometryShaderType g =
      s.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    s.shaderDeleteVertex(v);

    this.expected.expect(JCGLExceptionDeleted.class);
    s.shaderLinkProgram("valid0", v, Optional.of(g), f);
  }

  @Test
  public final void testProgramLinkDeletedFragment()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLGeometryShaderType g =
      s.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    s.shaderDeleteFragment(f);

    this.expected.expect(JCGLExceptionDeleted.class);
    s.shaderLinkProgram("valid0", v, Optional.of(g), f);
  }

  @Test
  public final void testProgramLinkDeletedGeometry()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLGeometryShaderType g =
      s.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    s.shaderDeleteGeometry(g);

    this.expected.expect(JCGLExceptionDeleted.class);
    s.shaderLinkProgram("valid0", v, Optional.of(g), f);
  }

  @Test
  public final void testProgramLinkWrongContextVertex()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    Assert.assertFalse(c0.contextIsCurrent());
    Assert.assertTrue(c1.contextIsCurrent());

    c1.contextReleaseCurrent();
    c0.contextMakeCurrent();

    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s1.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLFragmentShaderType f =
      s1.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderLinkProgram("valid0", v, Optional.of(g), f);
  }

  @Test
  public final void testProgramLinkWrongContextFragment()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    Assert.assertFalse(c0.contextIsCurrent());
    Assert.assertTrue(c1.contextIsCurrent());

    c1.contextReleaseCurrent();
    c0.contextMakeCurrent();

    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    final JCGLVertexShaderType v =
      s1.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLGeometryShaderType g =
      s1.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));

    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderLinkProgram("valid0", v, Optional.of(g), f);
  }

  @Test
  public final void testProgramLinkWrongContextGeometry()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    Assert.assertFalse(c0.contextIsCurrent());
    Assert.assertTrue(c1.contextIsCurrent());

    c1.contextReleaseCurrent();
    c0.contextMakeCurrent();

    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    c0.contextReleaseCurrent();

    c1.contextMakeCurrent();
    final JCGLVertexShaderType v =
      s1.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s1.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderLinkProgram("valid0", v, Optional.of(g), f);
  }

  @Test
  public final void testProgramLinkSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    Assert.assertTrue(c0.contextIsCurrent());
    Assert.assertFalse(c1.contextIsCurrent());

    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    final JCGLProgramShaderType p =
      s1.shaderLinkProgram("valid0", v, Optional.of(g), f);
    Assert.assertTrue(p.glName() > 0);
  }

  @Test
  public final void testProgramLinkIncompatible()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "incompatible0", this.getShaderLines("incompatible0.vert"));
    final JCGLFragmentShaderType f = s.shaderCompileFragment(
      "incompatible0", this.getShaderLines("incompatible0.frag"));

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderLinkProgram("incompatible0", v, Optional.empty(), f);
  }

  @Test
  public final void testProgramActivateSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    Assert.assertTrue(c0.contextIsCurrent());
    Assert.assertFalse(c1.contextIsCurrent());

    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s0.shaderLinkProgram("valid0", v, Optional.of(g), f);

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    s1.shaderActivateProgram(p);

    Assert.assertEquals(Optional.of(p), s1.shaderActivatedProgram());
  }

  @Test
  public final void testProgramActivateWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    Assert.assertFalse(c0.contextIsCurrent());
    Assert.assertTrue(c1.contextIsCurrent());

    c1.contextReleaseCurrent();
    c0.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s0.shaderLinkProgram("valid0", v, Optional.of(g), f);

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderActivateProgram(p);
  }

  @Test
  public final void testProgramActivateDeleted()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("valid0", v, Optional.empty(), f);

    s.shaderDeleteProgram(p);
    this.expected.expect(JCGLExceptionDeleted.class);
    s.shaderActivateProgram(p);
  }

  @Test
  public final void testProgramDeleteDeleted()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("valid0", v, Optional.empty(), f);

    s.shaderDeleteProgram(p);
    this.expected.expect(JCGLExceptionDeleted.class);
    s.shaderDeleteProgram(p);
  }

  @Test
  public final void testProgramActivateDeactivateIdentity()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("valid0", v, Optional.empty(), f);

    s.shaderActivateProgram(p);
    Assert.assertEquals(Optional.of(p), s.shaderActivatedProgram());
    s.shaderDeactivateProgram();
    Assert.assertEquals(Optional.empty(), s.shaderActivatedProgram());
    s.shaderActivateProgram(p);
    Assert.assertEquals(Optional.of(p), s.shaderActivatedProgram());
    s.shaderDeactivateProgram();
    Assert.assertEquals(Optional.empty(), s.shaderActivatedProgram());
  }

  @Test
  public final void testProgramDeleteActivated()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("valid0", v, Optional.empty(), f);

    s.shaderActivateProgram(p);
    Assert.assertEquals(Optional.of(p), s.shaderActivatedProgram());
    s.shaderDeleteProgram(p);
    Assert.assertEquals(Optional.empty(), s.shaderActivatedProgram());
  }

  @Test
  public final void testProgramDeleteWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    Assert.assertFalse(c0.contextIsCurrent());
    Assert.assertTrue(c1.contextIsCurrent());

    c1.contextReleaseCurrent();
    c0.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s0.shaderLinkProgram("valid0", v, Optional.of(g), f);

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderDeleteProgram(p);
  }

  @Test
  public final void testProgramDeleteSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    Assert.assertTrue(c0.contextIsCurrent());
    Assert.assertFalse(c1.contextIsCurrent());

    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s0.shaderLinkProgram("valid0", v, Optional.of(g), f);

    c0.contextReleaseCurrent();
    c1.contextMakeCurrent();
    s1.shaderDeleteProgram(p);
    Assert.assertTrue(p.isDeleted());
  }

  @Test
  public final void testProgramUniformFloatCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("f"));
    final JCGLProgramUniformType u = us.get("f");
    Assert.assertEquals(JCGLType.TYPE_FLOAT, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutFloat(u, 1.0f);
  }

  @Test
  public final void testProgramUniformFloatWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutFloat(u, 1.0f);
  }

  @Test
  public final void testProgramUniformFloatNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("f"));
    final JCGLProgramUniformType u = us.get("f");
    Assert.assertEquals(JCGLType.TYPE_FLOAT, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutFloat(u, 1.0f);
  }

  @Test
  public final void testProgramUniformFloatWrongTypeNoChecking()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderUniformSetTypeCheckingEnabled(false);
    s.shaderActivateProgram(p);
    s.shaderUniformPutFloat(u, 1.0f);
  }

  @Test
  public final void testProgramUniformIntegerCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("i"));
    final JCGLProgramUniformType u = us.get("i");
    Assert.assertEquals(JCGLType.TYPE_INTEGER, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutInteger(u, 1);
  }

  @Test
  public final void testProgramUniformIntegerWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutInteger(u, 1);
  }

  @Test
  public final void testProgramUniformIntegerNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("i"));
    final JCGLProgramUniformType u = us.get("i");
    Assert.assertEquals(JCGLType.TYPE_INTEGER, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutInteger(u, 1);
  }

  @Test
  public final void testProgramUniformIntegerWrongTypeNoChecking()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderUniformSetTypeCheckingEnabled(false);
    s.shaderActivateProgram(p);
    s.shaderUniformPutInteger(u, 1);
  }

  @Test
  public final void testProgramUniformUnsignedIntegerCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("u"));
    final JCGLProgramUniformType u = us.get("u");
    Assert.assertEquals(JCGLType.TYPE_UNSIGNED_INTEGER, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutUnsignedInteger(u, 1);
  }

  @Test
  public final void testProgramUniformUnsignedIntegerWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutUnsignedInteger(u, 1);
  }

  @Test
  public final void testProgramUniformUnsignedIntegerNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("u"));
    final JCGLProgramUniformType u = us.get("u");
    Assert.assertEquals(JCGLType.TYPE_UNSIGNED_INTEGER, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutUnsignedInteger(u, 1);
  }

  @Test
  public final void testProgramUniformUnsignedIntegerWrongTypeNoChecking()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderUniformSetTypeCheckingEnabled(false);
    s.shaderActivateProgram(p);
    s.shaderUniformPutUnsignedInteger(u, 1);
  }

  @Test
  public final void testProgramUniformVector2fCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fv2"));
    final JCGLProgramUniformType u = us.get("fv2");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_2, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutVector2f(u, Vectors2D.zero());
  }

  @Test
  public final void testProgramUniformVector2fWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutVector2f(u, Vectors2D.zero());
  }

  @Test
  public final void testProgramUniformVector2fNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fv2"));
    final JCGLProgramUniformType u = us.get("fv2");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_2, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutVector2f(u, Vectors2D.zero());
  }

  @Test
  public final void testProgramUniformVector3fCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fv3"));
    final JCGLProgramUniformType u = us.get("fv3");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_3, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutVector3f(u, Vectors3D.zero());
  }

  @Test
  public final void testProgramUniformVector3fWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutVector3f(u, Vectors3D.zero());
  }

  @Test
  public final void testProgramUniformVector3fNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fv3"));
    final JCGLProgramUniformType u = us.get("fv3");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_3, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutVector3f(u, Vectors3D.zero());
  }

  @Test
  public final void testProgramUniformVector4fCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fv4"));
    final JCGLProgramUniformType u = us.get("fv4");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_4, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutVector4f(u, Vectors4D.zero());
  }

  @Test
  public final void testProgramUniformVector4fWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutVector4f(u, Vectors4D.zero());
  }

  @Test
  public final void testProgramUniformVector4fNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fv4"));
    final JCGLProgramUniformType u = us.get("fv4");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_VECTOR_4, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutVector4f(u, Vectors4D.zero());
  }

  @Test
  public final void testProgramUniformVector2iCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("iv2"));
    final JCGLProgramUniformType u = us.get("iv2");
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_2, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutVector2i(u, Vectors2I.zero());
  }

  @Test
  public final void testProgramUniformVector2iWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutVector2i(u, Vectors2I.zero());
  }

  @Test
  public final void testProgramUniformVector2iNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("iv2"));
    final JCGLProgramUniformType u = us.get("iv2");
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_2, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutVector2i(u, Vectors2I.zero());
  }

  @Test
  public final void testProgramUniformVector3iCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("iv3"));
    final JCGLProgramUniformType u = us.get("iv3");
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_3, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutVector3i(u, Vectors3I.zero());
  }

  @Test
  public final void testProgramUniformVector3iWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutVector3i(u, Vectors3I.zero());
  }

  @Test
  public final void testProgramUniformVector3iNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("iv3"));
    final JCGLProgramUniformType u = us.get("iv3");
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_3, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutVector3i(u, Vectors3I.zero());
  }

  @Test
  public final void testProgramUniformVector4iCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("iv4"));
    final JCGLProgramUniformType u = us.get("iv4");
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_4, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutVector4i(u, Vectors4I.zero());
  }

  @Test
  public final void testProgramUniformVector4iWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutVector4i(u, Vectors4I.zero());
  }

  @Test
  public final void testProgramUniformVector4iNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("iv4"));
    final JCGLProgramUniformType u = us.get("iv4");
    Assert.assertEquals(JCGLType.TYPE_INTEGER_VECTOR_4, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutVector4i(u, Vectors4I.zero());
  }

  @Test
  public final void testProgramUniformVector2uCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("uv2"));
    final JCGLProgramUniformType u = us.get("uv2");
    Assert.assertEquals(JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutVector2ui(u, Vectors2I.zero());
  }

  @Test
  public final void testProgramUniformVector2uWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutVector2ui(u, Vectors2I.zero());
  }

  @Test
  public final void testProgramUniformVector2uNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("uv2"));
    final JCGLProgramUniformType u = us.get("uv2");
    Assert.assertEquals(JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutVector2ui(u, Vectors2I.zero());
  }

  @Test
  public final void testProgramUniformVector3uCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("uv3"));
    final JCGLProgramUniformType u = us.get("uv3");
    Assert.assertEquals(JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutVector3ui(u, Vectors3I.zero());
  }

  @Test
  public final void testProgramUniformVector3uWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutVector3ui(u, Vectors3I.zero());
  }

  @Test
  public final void testProgramUniformVector3uNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("uv3"));
    final JCGLProgramUniformType u = us.get("uv3");
    Assert.assertEquals(JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutVector3ui(u, Vectors3I.zero());
  }

  @Test
  public final void testProgramUniformVector4uCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("uv4"));
    final JCGLProgramUniformType u = us.get("uv4");
    Assert.assertEquals(JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutVector4ui(u, Vectors4I.zero());
  }

  @Test
  public final void testProgramUniformVector4uWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutVector4ui(u, Vectors4I.zero());
  }

  @Test
  public final void testProgramUniformVector4uNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("uv4"));
    final JCGLProgramUniformType u = us.get("uv4");
    Assert.assertEquals(JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutVector4ui(u, Vectors4I.zero());
  }

  @Test
  public final void testProgramUniformMatrix4fCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms1", this.getShaderLines("uniforms1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms1", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fm4"));
    final JCGLProgramUniformType u = us.get("fm4");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_4, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutMatrix4x4f(u, Matrices4x4D.identity());
  }

  @Test
  public final void testProgramUniformMatrix4fWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutMatrix4x4f(u, Matrices4x4D.identity());
  }

  @Test
  public final void testProgramUniformMatrix4fNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms1", this.getShaderLines("uniforms1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms1", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fm4"));
    final JCGLProgramUniformType u = us.get("fm4");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_4, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutMatrix4x4f(u, Matrices4x4D.identity());
  }

  @Test
  public final void testProgramUniformMatrix3fCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms1", this.getShaderLines("uniforms1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms1", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fm3"));
    final JCGLProgramUniformType u = us.get("fm3");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_3, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutMatrix3x3f(u, Matrices3x3D.identity());
  }

  @Test
  public final void testProgramUniformMatrix3fWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutMatrix3x3f(u, Matrices3x3D.identity());
  }

  @Test
  public final void testProgramUniformMatrix3fNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms1", this.getShaderLines("uniforms1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms1", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fm3"));
    final JCGLProgramUniformType u = us.get("fm3");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_3, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutMatrix3x3f(u, Matrices3x3D.identity());
  }

  @Test
  public final void testProgramUniformPMatrix4fCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms1", this.getShaderLines("uniforms1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms1", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fm4"));
    final JCGLProgramUniformType u = us.get("fm4");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_4, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutPMatrix4x4f(u, PMatrices4x4D.identity());
  }

  @Test
  public final void testProgramUniformPMatrix4fWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutPMatrix4x4f(u, PMatrices4x4D.identity());
  }

  @Test
  public final void testProgramUniformPMatrix4fNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms1", this.getShaderLines("uniforms1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms1", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fm4"));
    final JCGLProgramUniformType u = us.get("fm4");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_4, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutPMatrix4x4f(u, PMatrices4x4D.identity());
  }

  @Test
  public final void testProgramUniformPMatrix3fCorrect()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms1", this.getShaderLines("uniforms1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms1", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fm3"));
    final JCGLProgramUniformType u = us.get("fm3");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_3, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutPMatrix3x3f(u, PMatrices3x3D.identity());
  }

  @Test
  public final void testProgramUniformPMatrix3fWrong()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("b"));
    final JCGLProgramUniformType u = us.get("b");
    Assert.assertEquals(JCGLType.TYPE_BOOLEAN, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutPMatrix3x3f(u, PMatrices3x3D.identity());
  }

  @Test
  public final void testProgramUniformPMatrix3fNotActive()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms1", this.getShaderLines("uniforms1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms1", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("fm3"));
    final JCGLProgramUniformType u = us.get("fm3");
    Assert.assertEquals(JCGLType.TYPE_FLOAT_MATRIX_3, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutPMatrix3x3f(u, PMatrices3x3D.identity());
  }

  @Test
  public final void testProgramUniformSampler2DCorrect()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLShadersType s = i.getShaders();
    final JCGLTexturesType t = i.getTextures();

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms2", this.getShaderLines("uniforms2.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms2", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("s2"));
    final JCGLProgramUniformType u = us.get("s2");
    Assert.assertEquals(JCGLType.TYPE_SAMPLER_2D, u.type());

    s.shaderActivateProgram(p);
    s.shaderUniformPutTexture2DUnit(u, t.textureGetUnits().get(0));
  }

  @Test
  public final void testProgramUniformSampler2DWrong()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLShadersType s = i.getShaders();
    final JCGLTexturesType t = i.getTextures();

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms2", this.getShaderLines("uniforms2.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms2", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("s3"));
    final JCGLProgramUniformType u = us.get("s3");
    Assert.assertEquals(JCGLType.TYPE_SAMPLER_3D, u.type());

    s.shaderActivateProgram(p);
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutTexture2DUnit(u, t.textureGetUnits().get(0));
  }

  @Test
  public final void testProgramUniformSampler2DNotActive()
  {
    final Interfaces i = this.getInterfaces("main");
    final JCGLShadersType s = i.getShaders();
    final JCGLTexturesType t = i.getTextures();

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms2", this.getShaderLines("uniforms2.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms2", v, Optional.empty(), f);

    final Map<String, JCGLProgramUniformType> us = p.uniforms();
    Assert.assertTrue(us.containsKey("s2"));
    final JCGLProgramUniformType u = us.get("s2");
    Assert.assertEquals(JCGLType.TYPE_SAMPLER_2D, u.type());

    s.shaderDeactivateProgram();
    this.expected.expect(JCGLExceptionProgramNotActive.class);
    s.shaderUniformPutTexture2DUnit(u, t.textureGetUnits().get(0));
  }

  @Test
  public final void testProgramUniformPutVectorfWrongType()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final FloatBuffer buf = ByteBuffer.allocateDirect(4)
      .order(ByteOrder.nativeOrder())
      .asFloatBuffer();

    s.shaderActivateProgram(p);

    final JCGLProgramUniformType u = p.uniforms().get("u");
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutVectorf(u, buf);
  }

  @Test
  public final void testProgramUniformPutVectorfTooFew()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    final FloatBuffer buf = ByteBuffer.allocateDirect(4)
      .order(ByteOrder.nativeOrder())
      .asFloatBuffer();

    s.shaderActivateProgram(p);

    final JCGLProgramUniformType u = p.uniforms().get("fv4");
    this.expected.expect(JCGLExceptionProgramTypeError.class);
    s.shaderUniformPutVectorf(u, buf);
  }

  protected static final class Interfaces
  {
    private final JCGLContextType context;
    private final JCGLShadersType shaders;
    private final JCGLTexturesType textures;

    public Interfaces(
      final JCGLContextType in_context,
      final JCGLShadersType in_shaders,
      final JCGLTexturesType in_textures)
    {
      this.context = NullCheck.notNull(in_context, "Context");
      this.shaders = NullCheck.notNull(in_shaders, "Shaders");
      this.textures = NullCheck.notNull(in_textures, "Textures");
    }

    public JCGLContextType getContext()
    {
      return this.context;
    }

    public JCGLShadersType getShaders()
    {
      return this.shaders;
    }

    public JCGLTexturesType getTextures()
    {
      return this.textures;
    }
  }
}
