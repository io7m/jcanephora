package com.io7m.jcanephora;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Pair;

public class DrawOperationBindingsLWJGL30Test
{
  private static @Nonnull ProgramReference make4AttribProgram(
    final GLInterface gl)
    throws GLException,
      ConstraintError,
      GLCompileException,
      IOException
  {
    final StringBuilder vs = new StringBuilder();
    vs.append("#version 110\n");
    vs.append("attribute vec3 attr0;\n");
    vs.append("attribute vec3 attr1;\n");
    vs.append("attribute vec3 attr2;\n");
    vs.append("attribute vec3 attr3;\n");
    vs.append("varying vec3 ovar0;\n");
    vs.append("varying vec3 ovar1;\n");
    vs.append("varying vec3 ovar2;\n");
    vs.append("varying vec3 ovar3;\n");
    vs.append("void main() {\n");
    vs.append("  ovar0 = attr0;\n");
    vs.append("  ovar1 = attr1;\n");
    vs.append("  ovar2 = attr2;\n");
    vs.append("  ovar3 = attr3;\n");
    vs.append("  gl_Position = vec4(attr0 + attr1 + attr2 + attr3, 1.0);\n");
    vs.append("}\n");

    final StringBuilder fs = new StringBuilder();
    fs.append("#version 110\n");
    fs.append("varying vec3 ovar0;\n");
    fs.append("varying vec3 ovar1;\n");
    fs.append("varying vec3 ovar2;\n");
    fs.append("varying vec3 ovar3;\n");
    fs.append("void main() {\n");
    fs.append("  gl_FragColor = vec4(ovar0 + ovar1 + ovar2 + ovar3, 1.0);\n");
    fs.append("}\n");

    final ProgramReference p = gl.createProgram("4Attrib");
    final VertexShader v =
      gl.compileVertexShader("4AttribV", new ByteArrayInputStream(vs
        .toString()
        .getBytes()));
    final FragmentShader f =
      gl.compileFragmentShader("4AttribF", new ByteArrayInputStream(fs
        .toString()
        .getBytes()));

    gl.attachVertexShader(p, v);
    gl.attachFragmentShader(p, f);
    gl.linkProgram(p);
    return p;
  }

  private static @Nonnull ArrayBuffer makeBuffer(
    final GLInterface gl)
    throws ConstraintError,
      GLException
  {
    final ArrayBufferDescriptor descriptor =
      new ArrayBufferDescriptor(new ArrayBufferAttribute[] {
        new ArrayBufferAttribute("attr0", GLScalarType.TYPE_FLOAT, 3),
        new ArrayBufferAttribute("attr1", GLScalarType.TYPE_FLOAT, 3),
        new ArrayBufferAttribute("attr2", GLScalarType.TYPE_FLOAT, 2) });
    return gl.allocateArrayBuffer(8, descriptor);
  }

  private static @Nonnull ProgramReference makeIdentityProgram(
    final GLInterface gl)
    throws GLException,
      ConstraintError
  {
    return gl.createProgram("example");
  }

  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("DrawOperationBindings", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
  }

  /**
   * GL interface is null.
   * 
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public void testDOBAdd000NullGL()
    throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(null, null, null, null, null);
  }

  /**
   * Program is null.
   * 
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testDOBAdd001NullProgram()
      throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(gl, null, null, null, null);
  }

  /**
   * Program attribute is null.
   * 
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testDOBAdd002NullProgramAttribute()
      throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;
    ProgramReference p = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
      p = DrawOperationBindingsLWJGL30Test.makeIdentityProgram(gl);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(gl, p, null, null, null);
  }

  /**
   * Array buffer is null.
   * 
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testDOBAdd003NullBuffer()
      throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;
    ProgramReference p = null;
    Attribute pa = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
      p = DrawOperationBindingsLWJGL30Test.make4AttribProgram(gl);
      final Map<String, Attribute> out = new HashMap<String, Attribute>();
      gl.getAttributes(p, out);
      pa = out.get("attr0");
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(gl, p, pa, null, null);
  }

  /**
   * Array buffer attribute is null.
   * 
   * @throws ConstraintError
   */

  @Test(expected = ConstraintError.class) public
    void
    testDOBAdd004NullBufferAttribute()
      throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;
    ProgramReference p = null;
    Attribute pa = null;
    ArrayBuffer a = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
      p = DrawOperationBindingsLWJGL30Test.make4AttribProgram(gl);
      final Map<String, Attribute> out = new HashMap<String, Attribute>();
      gl.getAttributes(p, out);
      pa = out.get("attr0");
      a = DrawOperationBindingsLWJGL30Test.makeBuffer(gl);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(gl, p, pa, a, null);
  }

  /**
   * Initialization is OK.
   */

  @Test public void testDOBAdd005OK0()
    throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;
    ProgramReference p = null;
    Attribute pa = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute aa = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
      p = DrawOperationBindingsLWJGL30Test.make4AttribProgram(gl);
      final Map<String, Attribute> out = new HashMap<String, Attribute>();
      gl.getAttributes(p, out);
      pa = out.get("attr0");
      a = DrawOperationBindingsLWJGL30Test.makeBuffer(gl);
      aa = a.getDescriptor().getAttribute("attr0");
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(gl, p, pa, a, aa);
  }

  /**
   * Added program attribute does not belong to the given program.
   */

  @Test(expected = ConstraintError.class) public
    void
    testDOBAdd006WrongProgramAttribute()
      throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;
    ProgramReference p0 = null;
    ProgramReference p1 = null;
    Attribute pa = null;
    ArrayBuffer a = null;
    ArrayBufferAttribute aa = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
      p0 = DrawOperationBindingsLWJGL30Test.make4AttribProgram(gl);
      p1 = DrawOperationBindingsLWJGL30Test.make4AttribProgram(gl);
      final Map<String, Attribute> attrs = new HashMap<String, Attribute>();
      gl.getAttributes(p1, attrs);
      pa = attrs.get("attr0");
      a = DrawOperationBindingsLWJGL30Test.makeBuffer(gl);
      aa = a.getDescriptor().getAttribute("attr0");
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(gl, p0, pa, a, aa);
  }

  /**
   * Array buffer attribute does not belong to the given buffer.
   */

  @Test(expected = ConstraintError.class) public
    void
    testDOBAdd007WrongArrayBufferAttribute()
      throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;
    ProgramReference p = null;
    Attribute pa = null;
    ArrayBuffer a0 = null;
    ArrayBuffer a1 = null;
    ArrayBufferAttribute aa = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
      p = DrawOperationBindingsLWJGL30Test.make4AttribProgram(gl);
      final Map<String, Attribute> out = new HashMap<String, Attribute>();
      gl.getAttributes(p, out);
      pa = out.get("attr0");
      a0 = DrawOperationBindingsLWJGL30Test.makeBuffer(gl);
      a1 = DrawOperationBindingsLWJGL30Test.makeBuffer(gl);
      aa = a1.getDescriptor().getAttribute("attr0");
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(gl, p, pa, a0, aa);
  }

  /**
   * Duplicate array buffer attribute.
   */

  @Test(expected = ConstraintError.class) public
    void
    testDOBAdd008DuplicateArrayBufferAttribute()
      throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;
    ProgramReference p = null;
    Attribute pa0 = null;
    Attribute pa1 = null;
    ArrayBuffer a0 = null;
    ArrayBufferAttribute aa0 = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
      p = DrawOperationBindingsLWJGL30Test.make4AttribProgram(gl);
      final Map<String, Attribute> out = new HashMap<String, Attribute>();
      gl.getAttributes(p, out);
      pa0 = out.get("attr0");
      pa1 = out.get("attr1");
      a0 = DrawOperationBindingsLWJGL30Test.makeBuffer(gl);
      aa0 = a0.getDescriptor().getAttribute("attr0");
      b.addBinding(gl, p, pa0, a0, aa0);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(gl, p, pa1, a0, aa0);
  }

  /**
   * Duplicate program attribute.
   */

  @Test(expected = ConstraintError.class) public
    void
    testDOBAdd009DuplicateProgramAttribute()
      throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;
    ProgramReference p = null;
    Attribute pa0 = null;
    ArrayBuffer a0 = null;
    ArrayBufferAttribute aa0 = null;
    ArrayBufferAttribute aa1 = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
      p = DrawOperationBindingsLWJGL30Test.make4AttribProgram(gl);
      final Map<String, Attribute> out = new HashMap<String, Attribute>();
      gl.getAttributes(p, out);
      pa0 = out.get("attr0");
      a0 = DrawOperationBindingsLWJGL30Test.makeBuffer(gl);
      aa0 = a0.getDescriptor().getAttribute("attr0");
      aa1 = a0.getDescriptor().getAttribute("attr1");
      b.addBinding(gl, p, pa0, a0, aa0);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(gl, p, pa0, a0, aa1);
  }

  /**
   * Adding two attributes works.
   */

  @Test public void testDOBAdd010AddOK()
    throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;
    ProgramReference p = null;
    Attribute pa0 = null;
    Attribute pa1 = null;
    ArrayBuffer a0 = null;
    ArrayBufferAttribute aa0 = null;
    ArrayBufferAttribute aa1 = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
      p = DrawOperationBindingsLWJGL30Test.make4AttribProgram(gl);
      final Map<String, Attribute> out = new HashMap<String, Attribute>();
      gl.getAttributes(p, out);
      pa0 = out.get("attr0");
      pa1 = out.get("attr1");
      a0 = DrawOperationBindingsLWJGL30Test.makeBuffer(gl);
      aa0 = a0.getDescriptor().getAttribute("attr0");
      aa1 = a0.getDescriptor().getAttribute("attr1");
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(gl, p, pa0, a0, aa0);
    b.addBinding(gl, p, pa1, a0, aa1);
  }

  /**
   * Retrieving buffers and attributes works.
   */

  @Test public void testDOBAdd010AddOKGet()
    throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;
    ProgramReference p = null;
    Attribute pa0 = null;
    Attribute pa1 = null;
    ArrayBuffer a0 = null;
    ArrayBuffer a1 = null;
    ArrayBufferAttribute aa0 = null;
    ArrayBufferAttribute aa1 = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
      p = DrawOperationBindingsLWJGL30Test.make4AttribProgram(gl);
      final Map<String, Attribute> out = new HashMap<String, Attribute>();
      gl.getAttributes(p, out);
      pa0 = out.get("attr0");
      pa1 = out.get("attr1");
      a0 = DrawOperationBindingsLWJGL30Test.makeBuffer(gl);
      a1 = DrawOperationBindingsLWJGL30Test.makeBuffer(gl);
      aa0 = a0.getDescriptor().getAttribute("attr0");
      aa1 = a1.getDescriptor().getAttribute("attr1");
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.addBinding(gl, p, pa0, a0, aa0);
    b.addBinding(gl, p, pa1, a1, aa1);

    {
      final Set<ArrayBuffer> xs = b.getBuffers();
      Assert.assertEquals(2, xs.size());
      Assert.assertTrue(xs.contains(a0));
      Assert.assertTrue(xs.contains(a1));

      {
        final List<Pair<ArrayBufferAttribute, Attribute>> ys =
          b.getBindings(a0);
        Assert.assertEquals(1, ys.size());
        Assert.assertSame(aa0, ys.get(0).first);
        Assert.assertSame(pa0, ys.get(0).second);
      }

      {
        final List<Pair<ArrayBufferAttribute, Attribute>> ys =
          b.getBindings(a1);
        Assert.assertEquals(1, ys.size());
        Assert.assertSame(aa1, ys.get(0).first);
        Assert.assertSame(pa1, ys.get(0).second);
      }
    }
  }

  /**
   * Retrieving for a null buffer fails.
   */

  @Test(expected = ConstraintError.class) public
    void
    testDOBAdd011NullBufferGet()
      throws ConstraintError
  {
    GLInterface gl = null;
    DrawOperationBindings b = null;
    ProgramReference p = null;
    Attribute pa0 = null;
    ArrayBuffer a0 = null;
    ArrayBufferAttribute aa0 = null;

    try {
      gl = GLInterfaceLWJGL30Util.getGL();
      b = new DrawOperationBindings(gl);
      p = DrawOperationBindingsLWJGL30Test.make4AttribProgram(gl);
      final Map<String, Attribute> out = new HashMap<String, Attribute>();
      gl.getAttributes(p, out);
      pa0 = out.get("attr0");
      a0 = DrawOperationBindingsLWJGL30Test.makeBuffer(gl);
      aa0 = a0.getDescriptor().getAttribute("attr0");
      b.addBinding(gl, p, pa0, a0, aa0);
    } catch (final Throwable e) {
      Assert.fail(e.getMessage());
    }

    assert b != null;
    b.getBindings(null);
  }

  /**
   * Initialization is OK.
   * 
   * @throws GLException
   * @throws ConstraintError
   * @throws IOException
   */

  @SuppressWarnings("unused") @Test public void testDOBInitial()
    throws GLException,
      ConstraintError,
      IOException
  {
    new DrawOperationBindings(GLInterfaceLWJGL30Util.getGL());
  }

  /**
   * GL interface is null.
   * 
   * @throws GLException
   * @throws ConstraintError
   */

  @SuppressWarnings("unused") @Test(expected = ConstraintError.class) public
    void
    testDOBNullGL()
      throws GLException,
        ConstraintError
  {
    new DrawOperationBindings(null);
  }
}
