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
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
    Assert.assertTrue(v.getGLName() >= 0);
    Assert.assertEquals(f, v.getName());
    Assert.assertEquals(t, v.getType());
    Assert.assertEquals(p, v.getProgram());
  }

  private static void checkUniform(
    final JCGLProgramShaderUsableType p,
    final Map<String, JCGLProgramUniformType> u,
    final String f,
    final JCGLType t)
  {
    Assert.assertTrue(u.containsKey(f));
    final JCGLProgramUniformType v = u.get(f);
    Assert.assertTrue(v.getGLName() >= 0);
    Assert.assertEquals(f, v.getName());
    Assert.assertEquals(t, v.getType());
    Assert.assertEquals(p, v.getProgram());
  }

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

  @Test public final void testVertexShaderCompileEmpty0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileVertex("empty0", new ArrayList<>(0));
  }

  @Test public final void testVertexShaderCompileEmpty1()
  {
    final JCGLShadersType s = this.getShaders("main");

    final List<String> lines = new ArrayList<>(3);
    lines.add(" ");
    lines.add("\t");
    lines.add("\n");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileVertex("empty1", lines);
  }

  @Test public final void testVertexShaderCompileInvalid0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileVertex("invalid0", this.getShaderLines("invalid0.vert"));
  }

  @Test public final void testVertexShaderCompileValidIdentities()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    Assert.assertEquals("valid0", v.getName());
    Assert.assertFalse(v.isDeleted());
    Assert.assertTrue(v.getGLName() > 0);
  }

  @Test public final void testVertexShaderDeleteIdentity()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    s.shaderDeleteVertex(v);
    Assert.assertTrue(v.isDeleted());
  }

  @Test public final void testVertexShaderDeleteDeleted()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    s.shaderDeleteVertex(v);

    this.expected.expect(JCGLExceptionDeleted.class);
    s.shaderDeleteVertex(v);
  }

  @Test public final void testVertexShaderDeleteWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    c0.contextMakeCurrent();
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));

    c1.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderDeleteVertex(v);
  }

  @Test public final void testVertexShaderDeleteSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    c0.contextMakeCurrent();
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));

    c1.contextMakeCurrent();
    s1.shaderDeleteVertex(v);

    Assert.assertTrue(v.isDeleted());
  }

  @Test public final void testFragmentShaderCompileEmpty0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileFragment("empty0", new ArrayList<>(0));
  }

  @Test public final void testFragmentShaderCompileEmpty1()
  {
    final JCGLShadersType s = this.getShaders("main");

    final List<String> lines = new ArrayList<>(3);
    lines.add(" ");
    lines.add("\t");
    lines.add("\n");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileFragment("empty1", lines);
  }

  @Test public final void testFragmentShaderCompileInvalid0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileFragment("invalid0", this.getShaderLines("invalid0.frag"));
  }

  @Test public final void testFragmentShaderCompileValidIdentities()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    Assert.assertEquals("valid0", f.getName());
    Assert.assertFalse(f.isDeleted());
    Assert.assertTrue(f.getGLName() > 0);
  }

  @Test public final void testFragmentShaderDeleteSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    c0.contextMakeCurrent();
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    c1.contextMakeCurrent();
    s1.shaderDeleteFragment(f);

    Assert.assertTrue(f.isDeleted());
  }

  @Test public final void testFragmentShaderDeleteWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    c0.contextMakeCurrent();
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    c1.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderDeleteFragment(f);
  }

  @Test public final void testFragmentShaderDeleteIdentity()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    s.shaderDeleteFragment(f);

    Assert.assertTrue(f.isDeleted());
  }

  @Test public final void testFragmentShaderDeleteDeleted()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    s.shaderDeleteFragment(f);

    this.expected.expect(JCGLExceptionDeleted.class);
    s.shaderDeleteFragment(f);
  }

  @Test public final void testGeometryShaderCompileEmpty0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileGeometry("empty0", new ArrayList<>(0));
  }

  @Test public final void testGeometryShaderCompileEmpty1()
  {
    final JCGLShadersType s = this.getShaders("main");

    final List<String> lines = new ArrayList<>(3);
    lines.add(" ");
    lines.add("\t");
    lines.add("\n");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileGeometry("empty1", lines);
  }

  @Test public final void testGeometryShaderCompileInvalid0()
  {
    final JCGLShadersType s = this.getShaders("main");

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderCompileGeometry("invalid0", this.getShaderLines("invalid0.geom"));
  }

  @Test public final void testGeometryShaderDeleteDeleted()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLGeometryShaderType g =
      s.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    s.shaderDeleteGeometry(g);

    this.expected.expect(JCGLExceptionDeleted.class);
    s.shaderDeleteGeometry(g);
  }

  @Test public final void testGeometryShaderDeleteIdentity()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLGeometryShaderType g =
      s.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    s.shaderDeleteGeometry(g);

    Assert.assertTrue(g.isDeleted());
  }

  @Test public final void testGeometryShaderDeleteWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    c0.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));

    c1.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderDeleteGeometry(g);
  }

  @Test public final void testGeometryShaderDeleteSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    c0.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));

    c1.contextMakeCurrent();
    s1.shaderDeleteGeometry(g);

    Assert.assertTrue(g.isDeleted());
  }

  @Test public final void testGeometryShaderCompileValidIdentities()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLGeometryShaderType g =
      s.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    Assert.assertEquals("valid0", g.getName());
    Assert.assertFalse(g.isDeleted());
    Assert.assertTrue(g.getGLName() > 0);
  }

  @Test public final void testProgramLinkValidIdentities()
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

    Assert.assertEquals("valid0", p.getName());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.getGLName() > 0);

    final Set<JCGLReferableType> p_refs = p.getReferences();
    Assert.assertEquals(3L, (long) p_refs.size());
    Assert.assertTrue(p_refs.contains(f));
    Assert.assertTrue(p_refs.contains(v));
    Assert.assertTrue(p_refs.contains(g));

    final Set<JCGLReferenceContainerType> v_refs = v.getReferringContainers();
    Assert.assertEquals(1L, (long) v_refs.size());
    Assert.assertTrue(v_refs.contains(p));

    final Set<JCGLReferenceContainerType> f_refs = f.getReferringContainers();
    Assert.assertEquals(1L, (long) f_refs.size());
    Assert.assertTrue(f_refs.contains(p));

    final Map<String, JCGLProgramAttributeType> a = p.getAttributes();
    Assert.assertEquals(1L, (long) a.size());

    final Map<String, JCGLProgramUniformType> u = p.getUniforms();
    Assert.assertEquals(0L, (long) u.size());
  }

  @Test public final void testProgramLinkValidAttributes()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes0", this.getShaderLines("attributes0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes0", v, Optional.empty(), f);

    Assert.assertEquals("attributes0", p.getName());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.getGLName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.getAttributes();
    Assert.assertEquals(12L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.getGLName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    JCGLShadersContract.checkAttribute(p, a, "f", JCGLType.TYPE_FLOAT);
    JCGLShadersContract.checkAttribute(
      p, a, "fv2", JCGLType.TYPE_FLOAT_VECTOR_2);
    JCGLShadersContract.checkAttribute(
      p, a, "fv3", JCGLType.TYPE_FLOAT_VECTOR_3);
    JCGLShadersContract.checkAttribute(
      p, a, "fv4", JCGLType.TYPE_FLOAT_VECTOR_4);

    JCGLShadersContract.checkAttribute(p, a, "i", JCGLType.TYPE_INTEGER);
    JCGLShadersContract.checkAttribute(
      p, a, "iv2", JCGLType.TYPE_INTEGER_VECTOR_2);
    JCGLShadersContract.checkAttribute(
      p, a, "iv3", JCGLType.TYPE_INTEGER_VECTOR_3);
    JCGLShadersContract.checkAttribute(
      p, a, "iv4", JCGLType.TYPE_INTEGER_VECTOR_4);

    JCGLShadersContract.checkAttribute(
      p, a, "u", JCGLType.TYPE_UNSIGNED_INTEGER);
    JCGLShadersContract.checkAttribute(
      p, a, "uv2", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2);
    JCGLShadersContract.checkAttribute(
      p, a, "uv3", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3);
    JCGLShadersContract.checkAttribute(
      p, a, "uv4", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4);
  }

  @Test public final void testProgramLinkValidAttributesMatrices0()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes1", this.getShaderLines("attributes1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes1", v, Optional.empty(), f);

    Assert.assertEquals("attributes1", p.getName());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.getGLName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.getAttributes();
    Assert.assertEquals(3L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.getGLName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    JCGLShadersContract.checkAttribute(
      p, a, "m4", JCGLType.TYPE_FLOAT_MATRIX_4);
    JCGLShadersContract.checkAttribute(
      p, a, "m3", JCGLType.TYPE_FLOAT_MATRIX_3);
    JCGLShadersContract.checkAttribute(
      p, a, "m2", JCGLType.TYPE_FLOAT_MATRIX_2);
  }

  @Test public final void testProgramLinkValidAttributesMatrices1()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes2", this.getShaderLines("attributes2.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes2", v, Optional.empty(), f);

    Assert.assertEquals("attributes2", p.getName());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.getGLName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.getAttributes();
    Assert.assertEquals(3L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.getGLName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    JCGLShadersContract.checkAttribute(
      p, a, "m4", JCGLType.TYPE_FLOAT_MATRIX_4);
    JCGLShadersContract.checkAttribute(
      p, a, "m3", JCGLType.TYPE_FLOAT_MATRIX_4x3);
    JCGLShadersContract.checkAttribute(
      p, a, "m2", JCGLType.TYPE_FLOAT_MATRIX_4x2);
  }

  @Test public final void testProgramLinkValidAttributesMatrices2()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes2", this.getShaderLines("attributes2.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes2", v, Optional.empty(), f);

    Assert.assertEquals("attributes2", p.getName());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.getGLName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.getAttributes();
    Assert.assertEquals(3L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.getGLName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    JCGLShadersContract.checkAttribute(
      p, a, "m4", JCGLType.TYPE_FLOAT_MATRIX_4);
    JCGLShadersContract.checkAttribute(
      p, a, "m3", JCGLType.TYPE_FLOAT_MATRIX_4x3);
    JCGLShadersContract.checkAttribute(
      p, a, "m2", JCGLType.TYPE_FLOAT_MATRIX_4x2);
  }

  @Test public final void testProgramLinkValidAttributesMatrices3()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes3", this.getShaderLines("attributes3.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes3", v, Optional.empty(), f);

    Assert.assertEquals("attributes3", p.getName());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.getGLName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.getAttributes();
    Assert.assertEquals(3L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.getGLName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    JCGLShadersContract.checkAttribute(
      p, a, "m4", JCGLType.TYPE_FLOAT_MATRIX_3x4);
    JCGLShadersContract.checkAttribute(
      p, a, "m3", JCGLType.TYPE_FLOAT_MATRIX_3);
    JCGLShadersContract.checkAttribute(
      p, a, "m2", JCGLType.TYPE_FLOAT_MATRIX_3x2);
  }

  @Test public final void testProgramLinkValidAttributesMatrices4()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "attributes4", this.getShaderLines("attributes4.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("attributes4", v, Optional.empty(), f);

    Assert.assertEquals("attributes4", p.getName());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.getGLName() > 0);

    final Map<String, JCGLProgramAttributeType> a = p.getAttributes();
    Assert.assertEquals(3L, (long) a.size());

    final Set<Integer> locations = new HashSet<>(a.values().size());
    for (final JCGLProgramAttributeType attr : a.values()) {
      final Integer id = Integer.valueOf(attr.getGLName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    JCGLShadersContract.checkAttribute(
      p, a, "m4", JCGLType.TYPE_FLOAT_MATRIX_2x4);
    JCGLShadersContract.checkAttribute(
      p, a, "m3", JCGLType.TYPE_FLOAT_MATRIX_2x3);
    JCGLShadersContract.checkAttribute(
      p, a, "m2", JCGLType.TYPE_FLOAT_MATRIX_2);
  }

  @Test public final void testProgramLinkValidUniforms0()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms0", this.getShaderLines("uniforms0.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms0", v, Optional.empty(), f);

    Assert.assertEquals("uniforms0", p.getName());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.getGLName() > 0);

    final Map<String, JCGLProgramUniformType> u = p.getUniforms();
    Assert.assertEquals(16L, (long) u.size());

    final Set<Integer> locations = new HashSet<>(u.values().size());
    for (final JCGLProgramUniformType uni : u.values()) {
      final Integer id = Integer.valueOf(uni.getGLName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    JCGLShadersContract.checkUniform(
      p, u, "f", JCGLType.TYPE_FLOAT);
    JCGLShadersContract.checkUniform(
      p, u, "fv2", JCGLType.TYPE_FLOAT_VECTOR_2);
    JCGLShadersContract.checkUniform(
      p, u, "fv3", JCGLType.TYPE_FLOAT_VECTOR_3);
    JCGLShadersContract.checkUniform(
      p, u, "fv4", JCGLType.TYPE_FLOAT_VECTOR_4);

    JCGLShadersContract.checkUniform(p, u, "i", JCGLType.TYPE_INTEGER);
    JCGLShadersContract.checkUniform(
      p, u, "iv2", JCGLType.TYPE_INTEGER_VECTOR_2);
    JCGLShadersContract.checkUniform(
      p, u, "iv3", JCGLType.TYPE_INTEGER_VECTOR_3);
    JCGLShadersContract.checkUniform(
      p, u, "iv4", JCGLType.TYPE_INTEGER_VECTOR_4);

    JCGLShadersContract.checkUniform(
      p, u, "u", JCGLType.TYPE_UNSIGNED_INTEGER);
    JCGLShadersContract.checkUniform(
      p, u, "uv2", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_2);
    JCGLShadersContract.checkUniform(
      p, u, "uv3", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_3);
    JCGLShadersContract.checkUniform(
      p, u, "uv4", JCGLType.TYPE_UNSIGNED_INTEGER_VECTOR_4);

    JCGLShadersContract.checkUniform(
      p, u, "b", JCGLType.TYPE_BOOLEAN);
    JCGLShadersContract.checkUniform(
      p, u, "bv2", JCGLType.TYPE_BOOLEAN_VECTOR_2);
    JCGLShadersContract.checkUniform(
      p, u, "bv3", JCGLType.TYPE_BOOLEAN_VECTOR_3);
    JCGLShadersContract.checkUniform(
      p, u, "bv4", JCGLType.TYPE_BOOLEAN_VECTOR_4);
  }

  @Test public final void testProgramLinkValidUniforms1()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms1", this.getShaderLines("uniforms1.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms1", v, Optional.empty(), f);

    Assert.assertEquals("uniforms1", p.getName());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.getGLName() > 0);

    final Map<String, JCGLProgramUniformType> u = p.getUniforms();
    Assert.assertEquals(12L, (long) u.size());

    final Set<Integer> locations = new HashSet<>(u.values().size());
    for (final JCGLProgramUniformType uni : u.values()) {
      final Integer id = Integer.valueOf(uni.getGLName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    JCGLShadersContract.checkUniform(
      p, u, "fm4", JCGLType.TYPE_FLOAT_MATRIX_4);
    JCGLShadersContract.checkUniform(
      p, u, "fm4x4", JCGLType.TYPE_FLOAT_MATRIX_4);
    JCGLShadersContract.checkUniform(
      p, u, "fm4x3", JCGLType.TYPE_FLOAT_MATRIX_4x3);
    JCGLShadersContract.checkUniform(
      p, u, "fm4x2", JCGLType.TYPE_FLOAT_MATRIX_4x2);

    JCGLShadersContract.checkUniform(
      p, u, "fm3", JCGLType.TYPE_FLOAT_MATRIX_3);
    JCGLShadersContract.checkUniform(
      p, u, "fm3x4", JCGLType.TYPE_FLOAT_MATRIX_3x4);
    JCGLShadersContract.checkUniform(
      p, u, "fm3x3", JCGLType.TYPE_FLOAT_MATRIX_3);
    JCGLShadersContract.checkUniform(
      p, u, "fm3x2", JCGLType.TYPE_FLOAT_MATRIX_3x2);

    JCGLShadersContract.checkUniform(
      p, u, "fm2", JCGLType.TYPE_FLOAT_MATRIX_2);
    JCGLShadersContract.checkUniform(
      p, u, "fm2x4", JCGLType.TYPE_FLOAT_MATRIX_2x4);
    JCGLShadersContract.checkUniform(
      p, u, "fm2x3", JCGLType.TYPE_FLOAT_MATRIX_2x3);
    JCGLShadersContract.checkUniform(
      p, u, "fm2x2", JCGLType.TYPE_FLOAT_MATRIX_2);
  }

  @Test public final void testProgramLinkValidUniforms2()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v =
      s.shaderCompileVertex("uniforms2", this.getShaderLines("uniforms2.vert"));
    final JCGLFragmentShaderType f =
      s.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s.shaderLinkProgram("uniforms2", v, Optional.empty(), f);

    Assert.assertEquals("uniforms2", p.getName());
    Assert.assertFalse(p.isDeleted());
    Assert.assertTrue(p.getGLName() > 0);

    final Map<String, JCGLProgramUniformType> u = p.getUniforms();
    Assert.assertEquals(3L, (long) u.size());

    final Set<Integer> locations = new HashSet<>(u.values().size());
    for (final JCGLProgramUniformType uni : u.values()) {
      final Integer id = Integer.valueOf(uni.getGLName());
      Assert.assertTrue(id.intValue() >= 0);
      Assert.assertFalse(locations.contains(id));
      locations.add(id);
    }

    JCGLShadersContract.checkUniform(
      p, u, "s2", JCGLType.TYPE_SAMPLER_2D);
    JCGLShadersContract.checkUniform(
      p, u, "s3", JCGLType.TYPE_SAMPLER_3D);
    JCGLShadersContract.checkUniform(
      p, u, "sc", JCGLType.TYPE_SAMPLER_CUBE);
  }

  @Test public final void testProgramLinkDeletedVertex()
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

  @Test public final void testProgramLinkDeletedFragment()
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

  @Test public final void testProgramLinkDeletedGeometry()
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

  @Test public final void testProgramLinkWrongContextVertex()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    c0.contextMakeCurrent();
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));

    c1.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s1.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLFragmentShaderType f =
      s1.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderLinkProgram("valid0", v, Optional.of(g), f);
  }

  @Test public final void testProgramLinkWrongContextFragment()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    c0.contextMakeCurrent();
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    c1.contextMakeCurrent();
    final JCGLVertexShaderType v =
      s1.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLGeometryShaderType g =
      s1.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));

    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderLinkProgram("valid0", v, Optional.of(g), f);
  }

  @Test public final void testProgramLinkWrongContextGeometry()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    c0.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));

    c1.contextMakeCurrent();
    final JCGLVertexShaderType v =
      s1.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s1.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderLinkProgram("valid0", v, Optional.of(g), f);
  }

  @Test public final void testProgramLinkSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    c0.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));

    c1.contextMakeCurrent();
    final JCGLProgramShaderType p =
      s1.shaderLinkProgram("valid0", v, Optional.of(g), f);
    Assert.assertTrue(p.getGLName() > 0);
  }

  @Test public final void testProgramLinkIncompatible()
  {
    final JCGLShadersType s = this.getShaders("main");

    final JCGLVertexShaderType v = s.shaderCompileVertex(
      "incompatible0", this.getShaderLines("incompatible0.vert"));
    final JCGLFragmentShaderType f = s.shaderCompileFragment(
      "incompatible0", this.getShaderLines("incompatible0.frag"));

    this.expected.expect(JCGLExceptionProgramCompileError.class);
    s.shaderLinkProgram("incompatible0", v, Optional.empty(), f);
  }

  @Test public final void testProgramActivateSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    c0.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s0.shaderLinkProgram("valid0", v, Optional.of(g), f);

    c1.contextMakeCurrent();
    s1.shaderActivateProgram(p);

    Assert.assertEquals(Optional.of(p), s1.shaderActivatedProgram());
  }

  @Test public final void testProgramActivateWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    c0.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s0.shaderLinkProgram("valid0", v, Optional.of(g), f);

    c1.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderActivateProgram(p);
  }

  @Test public final void testProgramActivateDeleted()
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

  @Test public final void testProgramDeleteDeleted()
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

  @Test public final void testProgramActivateDeactivateIdentity()
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

  @Test public final void testProgramDeleteActivated()
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

  @Test public final void testProgramDeleteWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLShadersType> cp =
      this.getShadersUnshared("main", "alt");
    final JCGLContextType c0 = cp.getContextA();
    final JCGLShadersType s0 = cp.getValueA();
    final JCGLContextType c1 = cp.getContextB();
    final JCGLShadersType s1 = cp.getValueB();

    c0.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s0.shaderLinkProgram("valid0", v, Optional.of(g), f);

    c1.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    s1.shaderDeleteProgram(p);
  }

  @Test public final void testProgramDeleteSharedContext()
  {
    final JCGLSharedContextPair<JCGLShadersType> cp =
      this.getShadersSharedWith("main", "alt");
    final JCGLContextType c0 = cp.getMasterContext();
    final JCGLShadersType s0 = cp.getMasterValue();
    final JCGLContextType c1 = cp.getSlaveContext();
    final JCGLShadersType s1 = cp.getSlaveValue();

    c0.contextMakeCurrent();
    final JCGLGeometryShaderType g =
      s0.shaderCompileGeometry("valid0", this.getShaderLines("valid0.geom"));
    final JCGLVertexShaderType v =
      s0.shaderCompileVertex("valid0", this.getShaderLines("valid0.vert"));
    final JCGLFragmentShaderType f =
      s0.shaderCompileFragment("valid0", this.getShaderLines("valid0.frag"));
    final JCGLProgramShaderType p =
      s0.shaderLinkProgram("valid0", v, Optional.of(g), f);

    c1.contextMakeCurrent();
    s1.shaderDeleteProgram(p);
    Assert.assertTrue(p.isDeleted());
  }
}
