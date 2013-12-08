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

package com.io7m.jcanephora.contracts.batchexec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jaux.functional.Pair;
import com.io7m.jcanephora.ArrayBuffer;
import com.io7m.jcanephora.ArrayBufferAttributeDescriptor;
import com.io7m.jcanephora.ArrayBufferTypeDescriptor;
import com.io7m.jcanephora.JCBExecutionAPI;
import com.io7m.jcanephora.JCBExecutionException;
import com.io7m.jcanephora.JCBExecutor;
import com.io7m.jcanephora.JCBProcedure;
import com.io7m.jcanephora.JCBProgram;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLInterfaceCommon;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.TestContext;
import com.io7m.jcanephora.TestUtilities;
import com.io7m.jcanephora.TestUtilities.LoadedShader;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.contracts.TestContract;
import com.io7m.jtensors.MatrixM3x3F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorI4I;
import com.io7m.jvvfs.FSCapabilityAll;
import com.io7m.parasol.xml.FragmentParameter;
import com.io7m.parasol.xml.VertexInput;
import com.io7m.parasol.xml.VertexParameter;

public abstract class JCBExecutionContract implements TestContract
{
  private static @Nonnull ArrayBuffer makeArrayBuffer(
    final @Nonnull JCGLInterfaceCommon gl)
  {
    try {
      final ArrayList<ArrayBufferAttributeDescriptor> abs =
        new ArrayList<ArrayBufferAttributeDescriptor>();
      abs.add(new ArrayBufferAttributeDescriptor(
        "a_vf2",
        JCGLScalarType.TYPE_FLOAT,
        2));
      abs.add(new ArrayBufferAttributeDescriptor(
        "a_vf3",
        JCGLScalarType.TYPE_FLOAT,
        3));
      abs.add(new ArrayBufferAttributeDescriptor(
        "a_vf4",
        JCGLScalarType.TYPE_FLOAT,
        4));
      abs.add(new ArrayBufferAttributeDescriptor(
        "a_f",
        JCGLScalarType.TYPE_FLOAT,
        1));

      final ArrayBufferTypeDescriptor descriptor =
        new ArrayBufferTypeDescriptor(abs);
      return gl.arrayBufferAllocate(
        3,
        descriptor,
        UsageHint.USAGE_STATIC_DRAW);
    } catch (final ConstraintError e) {
      throw new AssertionError(e);
    } catch (final JCGLException e) {
      throw new AssertionError(e);
    }
  }

  private static @Nonnull Map<String, JCGLType> makeAttributes(
    final @Nonnull LoadedShader ls)
    throws ConstraintError
  {
    final HashMap<String, JCGLType> m = new HashMap<String, JCGLType>();

    for (final VertexInput vi : ls.meta.getDeclaredVertexInputs()) {
      m.put(vi.getName(), JCGLType.fromName(vi.getType()));
    }

    return m;
  }

  private static @Nonnull Map<String, JCGLType> makeUniforms(
    final @Nonnull LoadedShader ls)
    throws ConstraintError
  {
    final HashMap<String, JCGLType> m = new HashMap<String, JCGLType>();

    for (final VertexParameter vp : ls.meta.getDeclaredVertexParameters()) {
      m.put(vp.getName(), JCGLType.fromName(vp.getType()));
    }
    for (final FragmentParameter fp : ls.meta.getDeclaredFragmentParameters()) {
      m.put(fp.getName(), JCGLType.fromName(fp.getType()));
    }

    return m;
  }

  private static @Nonnull Pair<JCBExecutionAPI, LoadedShader> newProgram(
    final @Nonnull TestContext tc,
    final @Nonnull String name)
  {
    LoadedShader ls;
    JCBExecutionAPI e;
    try {
      final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
      final FSCapabilityAll fs = tc.getFilesystem();
      ls = TestUtilities.loadShader(gl, fs, name, tc.getLog());
      e =
        JCBExecutor.newExecutorWithDeclarations(
          gl,
          ls.program,
          JCBExecutionContract.makeUniforms(ls),
          JCBExecutionContract.makeAttributes(ls),
          tc.getLog());
    } catch (final Throwable x) {
      x.printStackTrace(System.err);
      throw new UnreachableCodeException(x);
    }
    return new Pair<JCBExecutionAPI, LoadedShader>(e, ls);
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  @Test(expected = ConstraintError.class) public void testNullAttributes()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final LoadedShader ls =
      TestUtilities.loadShader(gl, fs, "simple", tc.getLog());
    JCBExecutor.newExecutorWithDeclarations(
      gl,
      ls.program,
      null,
      new HashMap<String, JCGLType>(),
      tc.getLog());
  }

  @Test(expected = ConstraintError.class) public void testNullGL()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final LoadedShader ls =
      TestUtilities.loadShader(gl, fs, "simple", tc.getLog());
    JCBExecutor.newExecutorWithoutDeclarations(null, ls.program, tc.getLog());
  }

  @Test(expected = ConstraintError.class) public void testNullLog()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final LoadedShader ls =
      TestUtilities.loadShader(gl, fs, "simple", tc.getLog());
    JCBExecutor.newExecutorWithoutDeclarations(gl, ls.program, null);
  }

  @Test(expected = ConstraintError.class) public void testNullProgram()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    JCBExecutor.newExecutorWithoutDeclarations(gl, null, tc.getLog());
  }

  @Test(expected = ConstraintError.class) public void testNullRunnable()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final LoadedShader ls =
      TestUtilities.loadShader(gl, fs, "simple", tc.getLog());

    JCBExecutionAPI e;
    try {
      e =
        JCBExecutor.newExecutorWithoutDeclarations(
          gl,
          ls.program,
          tc.getLog());
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }
    e.execRun(null);
  }

  @Test(expected = ConstraintError.class) public void testNullUniforms()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final LoadedShader ls =
      TestUtilities.loadShader(gl, fs, "simple", tc.getLog());
    JCBExecutor.newExecutorWithDeclarations(
      gl,
      ls.program,
      new HashMap<String, JCGLType>(),
      null,
      tc.getLog());
  }

  @Test public void testProcedureCalled()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        Assert.assertEquals(pair.second.program, program.programGet());
        called.set(true);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAttributePutFloat()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutFloat("a_f", 23.0f);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAttributePutFloatOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutFloat("a_f_opt", 23.0f);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributePutFloatNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributePutFloat("nonexistent", 23.0f);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributePutFloatNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributePutFloat(null, 23.0f);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramAttributePutVector2f()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector2F("a_vf2", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAttributePutVector2fOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector2F("a_vf2_opt", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributePutVector2fNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributePutVector2F("nonexistent", new VectorI4F());
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributePutVector2fNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributePutVector2F(null, new VectorI4F());
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributePutVector2fNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributePutVector2F("a_vf2", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramAttributePutVector3f()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector3F("a_vf3", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAttributePutVector3fOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector3F("a_vf3_opt", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributePutVector3fNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributePutVector3F("nonexistent", new VectorI4F());
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributePutVector3fNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributePutVector3F(null, new VectorI4F());
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributePutVector3fNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributePutVector3F("a_vf3", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramAttributePutVector4f()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector4F("a_vf4", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAttributePutVector4fOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector4F("a_vf4_opt", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributePutVector4fNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributePutVector4F("nonexistent", new VectorI4F());
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributePutVector4fNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributePutVector4F(null, new VectorI4F());
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributePutVector4fNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributePutVector4F("a_vf4", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramContainsIllTypedAttribute()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final Pair<JCBExecutionAPI, LoadedShader> p =
      JCBExecutionContract.newProgram(tc, "everything");

    final Map<String, JCGLType> a =
      JCBExecutionContract.makeAttributes(p.second);
    a.put("a_f", JCGLType.TYPE_BOOLEAN);

    JCBExecutor.newExecutorWithDeclarations(
      gl,
      p.second.program,
      JCBExecutionContract.makeUniforms(p.second),
      a,
      tc.getLog());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramContainsIllTypedUniform()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();

    final Pair<JCBExecutionAPI, LoadedShader> p =
      JCBExecutionContract.newProgram(tc, "everything");

    final Map<String, JCGLType> u =
      JCBExecutionContract.makeUniforms(p.second);
    u.put("u_float", JCGLType.TYPE_INTEGER);

    JCBExecutor.newExecutorWithDeclarations(
      gl,
      p.second.program,
      u,
      JCBExecutionContract.makeAttributes(p.second),
      tc.getLog());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramContainsUndeclaredAttribute()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final LoadedShader ls =
      TestUtilities.loadShader(gl, fs, "everything", tc.getLog());

    final Map<String, JCGLType> a = JCBExecutionContract.makeAttributes(ls);
    a.remove("a_f");

    JCBExecutor.newExecutorWithDeclarations(
      gl,
      ls.program,
      JCBExecutionContract.makeUniforms(ls),
      a,
      tc.getLog());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramContainsUndeclaredUniform()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final LoadedShader ls =
      TestUtilities.loadShader(gl, fs, "everything", tc.getLog());
    final HashMap<String, JCGLType> u = new HashMap<String, JCGLType>();
    u.put("nonexistent", JCGLType.TYPE_BOOLEAN);

    JCBExecutor
      .newExecutorWithDeclarations(gl, ls.program, u, u, tc.getLog());
  }

  @Test(expected = ConstraintError.class) public void testProgramDeleted()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommon gl = tc.getGLImplementation().getGLCommon();
    final FSCapabilityAll fs = tc.getFilesystem();
    final LoadedShader ls =
      TestUtilities.loadShader(gl, fs, "simple", tc.getLog());
    gl.programDelete(ls.program);

    JCBExecutor.newExecutorWithoutDeclarations(gl, ls.program, tc.getLog());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutFloatWrongType()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutFloat("u_vf2", 23.0f);
      }
    });

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutFloat()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutFloat("u_float", 23.0f);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutFloatOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutFloat("u_float_opt", 23.0f);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutFloatNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutFloat("nonexistent", 23.0f);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutFloatNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutFloat(null, 23.0f);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutIntegerWrongType()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutInteger("u_vf2", 23);
      }
    });

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutInteger()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutInteger("u_int", 23);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutIntegerOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutInteger("u_int_opt", 23);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutIntegerNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutInteger("nonexistent", 23);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutIntegerNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutInteger(null, 23);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutMatrix3x3f()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix3x3f("u_m3", new MatrixM3x3F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutMatrix3x3fOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix3x3f("u_m3_opt", new MatrixM3x3F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutMatrix3x3fWrongType()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix3x3f("u_vf2", new MatrixM3x3F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutMatrix4x4fWrongType()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix4x4f("u_vf2", new MatrixM4x4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector2fWrongType()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector2f("u_vf3", new VectorI4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector3fWrongType()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector3f("u_vf2", new VectorI4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector4fWrongType()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector4f("u_vf3", new VectorI4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutMatrix3x3fNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutMatrix3x3f(
            "nonexistent",
            new MatrixM3x3F());
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutMatrix3x3fNullName()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutMatrix3x3f(null, new MatrixM3x3F());
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutMatrix3x3fNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutMatrix3x3f("u_m3", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutMatrix4x4f()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix4x4f("u_m4", new MatrixM4x4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutMatrix4x4fOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix4x4f("u_m4_opt", new MatrixM4x4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutMatrix4x4fNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutMatrix4x4f(
            "nonexistent",
            new MatrixM4x4F());
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutMatrix4x4fNullName()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutMatrix4x4f(null, new MatrixM4x4F());
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutMatrix4x4fNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutMatrix4x4f("u_m4", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutTextureUnit()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    final TextureUnit unit =
      tc.getGLImplementation().getGLCommon().textureGetUnits().get(0);
    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutTextureUnit("u_texture", unit);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutTextureUnitOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    final TextureUnit unit =
      tc.getGLImplementation().getGLCommon().textureGetUnits().get(0);
    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutTextureUnit("u_texture_opt", unit);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutTextureUnitNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      final TextureUnit unit =
        tc.getGLImplementation().getGLCommon().textureGetUnits().get(0);
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutTextureUnit("nonexistent", unit);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutTextureUnitNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      final TextureUnit unit =
        tc.getGLImplementation().getGLCommon().textureGetUnits().get(0);
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutTextureUnit(null, unit);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector2f()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector2f("u_vf2", new VectorI4F(
          0.0f,
          1.0f,
          2.0f,
          3.0f));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutVector2fOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector2f("u_vf2_opt", new VectorI4F(
          0.0f,
          1.0f,
          2.0f,
          3.0f));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector2fNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector2f("nonexistent", new VectorI4F(
            0.0f,
            1.0f,
            2.0f,
            3.0f));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector2fNullName()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector2f(null, new VectorI4F(
            0.0f,
            1.0f,
            2.0f,
            3.0f));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector2fNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector2f("u_vf2", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector2i()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector2i("u_vi2", new VectorI4I(0, 1, 2, 3));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutVector2iOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector2i("u_vi2_opt", new VectorI4I(
          0,
          1,
          2,
          3));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector2iNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector2i("nonexistent", new VectorI4I(
            0,
            1,
            2,
            3));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector2iNullName()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector2i(null, new VectorI4I(0, 1, 2, 3));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector2iNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector2i("u_vi2", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector3f()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector3f("u_vf3", new VectorI4F(
          0.0f,
          1.0f,
          2.0f,
          3.0f));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutVector3fOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector3f("u_vf3_opt", new VectorI4F(
          0.0f,
          1.0f,
          2.0f,
          3.0f));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector3fNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector3f("nonexistent", new VectorI4F(
            0.0f,
            1.0f,
            2.0f,
            3.0f));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector3fNullName()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector3f(null, new VectorI4F(
            0.0f,
            1.0f,
            2.0f,
            3.0f));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector3fNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector3f("u_vf3", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector3i()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector3i("u_vi3", new VectorI4I(0, 1, 2, 3));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutVector3iOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector3i("u_vi3_opt", new VectorI4I(
          0,
          1,
          2,
          3));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector3iNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector3i("nonexistent", new VectorI4I(
            0,
            1,
            2,
            3));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector3iNullName()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector3i(null, new VectorI4I(0, 1, 2, 3));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector3iNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector3i("u_vi3", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector4f()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector4f("u_vf4", new VectorI4F(
          0.0f,
          1.0f,
          2.0f,
          3.0f));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutVector4fOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector4f("u_vf4_opt", new VectorI4F(
          0.0f,
          1.0f,
          2.0f,
          3.0f));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector4fNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector4f("nonexistent", new VectorI4F(
            0.0f,
            1.0f,
            2.0f,
            3.0f));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector4fNullName()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector4f(null, new VectorI4F(
            0.0f,
            1.0f,
            2.0f,
            3.0f));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector4fNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector4f("u_vf4", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector4i()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector4i("u_vi4", new VectorI4I(0, 1, 2, 3));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutVector4iOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector4i("u_vi4_opt", new VectorI4I(
          0,
          1,
          2,
          3));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector4iNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector4i("nonexistent", new VectorI4I(
            0,
            1,
            2,
            3));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector4iNullName()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector4i(null, new VectorI4I(0, 1, 2, 3));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformPutVector4iNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programUniformPutVector4i("u_vi4", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformReuseAssigned()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutFloat("u_float", 23.0f);
        program.programUniformUseExisting("u_float");
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformReuseUnassigned()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformUseExisting("u_float");
      }
    });
  }

  @Test public void testProgramUniformsOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutFloat("u_float_opt", 23.0f);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramUniformUnassigned()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        program.programValidate();
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAttributeBind()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    final JCGLInterfaceCommon gc = tc.getGLImplementation().getGLCommon();
    final ArrayBuffer a = JCBExecutionContract.makeArrayBuffer(gc);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        gc.arrayBufferBind(a);
        program.programAttributeBind("a_f", a.getAttribute("a_f"));
        program.programAttributeBind("a_vf2", a.getAttribute("a_vf2"));
        program.programAttributeBind("a_vf3", a.getAttribute("a_vf3"));
        program.programAttributeBind("a_vf4", a.getAttribute("a_vf4"));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAttributeBindOptimizedOut()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    final JCGLInterfaceCommon gc = tc.getGLImplementation().getGLCommon();
    final ArrayBuffer a = JCBExecutionContract.makeArrayBuffer(gc);

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);
        gc.arrayBufferBind(a);
        program.programAttributeBind("a_f_opt", a.getAttribute("a_f"));
        program.programAttributeBind("a_vf2_opt", a.getAttribute("a_vf2"));
        program.programAttributeBind("a_vf3_opt", a.getAttribute("a_vf3"));
        program.programAttributeBind("a_vf4_opt", a.getAttribute("a_vf4"));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributeBindNonexistent()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      final ArrayBuffer a =
        JCBExecutionContract.makeArrayBuffer(tc
          .getGLImplementation()
          .getGLCommon());

      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributeBind("nonexistent", a.getAttribute("a_f"));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributeBindWrongType()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      final ArrayBuffer a =
        JCBExecutionContract.makeArrayBuffer(tc
          .getGLImplementation()
          .getGLCommon());

      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributeBind("a_vf4", a.getAttribute("a_f"));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributeBindNull()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      final ArrayBuffer a =
        JCBExecutionContract.makeArrayBuffer(tc
          .getGLImplementation()
          .getGLCommon());

      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributeBind(null, a.getAttribute("a_f"));
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = ConstraintError.class) public
    void
    testProgramAttributeBindNullValue()
      throws ConstraintError,
        JCGLException,
        JCGLUnsupportedException,
        JCBExecutionException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          program.programAttributeBind("a_vf4", null);
        }
      });
    } catch (final ConstraintError x) {
      System.err.println(x);
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramAllAssigned()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    final JCGLInterfaceCommon gc = tc.getGLImplementation().getGLCommon();
    final ArrayBuffer a = JCBExecutionContract.makeArrayBuffer(gc);
    final List<TextureUnit> units = gc.textureGetUnits();

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);

        gc.arrayBufferBind(a);

        program.programAttributeBind("a_f", a.getAttribute("a_f"));
        program.programAttributeBind("a_vf2", a.getAttribute("a_vf2"));
        program.programAttributeBind("a_vf3", a.getAttribute("a_vf3"));
        program.programAttributeBind("a_vf4", a.getAttribute("a_vf4"));

        program.programUniformPutFloat("u_float", 23.0f);
        program.programUniformPutVector2f("u_vf2", new VectorI4F());
        program.programUniformPutVector3f("u_vf3", new VectorI4F());
        program.programUniformPutVector4f("u_vf4", new VectorI4F());

        program.programUniformPutInteger("u_int", 23);
        program.programUniformPutVector2i("u_vi2", new VectorI4I());
        program.programUniformPutVector3i("u_vi3", new VectorI4I());
        program.programUniformPutVector4i("u_vi4", new VectorI4I());

        program.programUniformPutMatrix3x3f("u_m3", new MatrixM3x3F());
        program.programUniformPutMatrix4x4f("u_m4", new MatrixM4x4F());
        program.programUniformPutTextureUnit("u_texture", units.get(0));

        program.programValidate();
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramExecException()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");
    final AtomicBoolean caught = new AtomicBoolean(false);

    final JCGLInterfaceCommon gc = tc.getGLImplementation().getGLCommon();
    final ArrayBuffer a = JCBExecutionContract.makeArrayBuffer(gc);

    final IllegalArgumentException z =
      new IllegalArgumentException("Caught!");

    try {
      pair.first.execRun(new JCBProcedure() {
        @Override public void call(
          final @Nonnull JCBProgram program)
          throws ConstraintError,
            JCGLException,
            Exception
        {
          gc.arrayBufferBind(a);
          program.programAttributeBind("v_position", a.getAttribute("a_vf3"));
          program.programUniformPutVector4f("f_ccolour", new VectorI4F());
          program.programUniformPutMatrix4x4f(
            "m_projection",
            new MatrixM4x4F());
          program.programUniformPutMatrix4x4f(
            "m_modelview",
            new MatrixM4x4F());
          program.programExecute(new Runnable() {
            @Override public void run()
            {
              throw z;
            }
          });
        }
      });
    } catch (final JCBExecutionException x) {
      final Throwable k = x.getCause().getCause();
      Assert.assertSame(z, k);
      caught.set(true);
    }

    Assert.assertTrue(caught.get());
  }

  @Test public void testProgramAllAssignedExec()
    throws ConstraintError,
      JCGLException,
      JCGLUnsupportedException,
      JCBExecutionException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutionAPI, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);
    final AtomicBoolean called_inner = new AtomicBoolean(false);

    final JCGLInterfaceCommon gc = tc.getGLImplementation().getGLCommon();
    final ArrayBuffer a = JCBExecutionContract.makeArrayBuffer(gc);
    final List<TextureUnit> units = gc.textureGetUnits();

    pair.first.execRun(new JCBProcedure() {
      @Override public void call(
        final @Nonnull JCBProgram program)
        throws ConstraintError,
          JCGLException,
          Exception
      {
        called.set(true);

        gc.arrayBufferBind(a);

        program.programAttributeBind("a_f", a.getAttribute("a_f"));
        program.programAttributeBind("a_vf2", a.getAttribute("a_vf2"));
        program.programAttributeBind("a_vf3", a.getAttribute("a_vf3"));
        program.programAttributeBind("a_vf4", a.getAttribute("a_vf4"));

        program.programUniformPutFloat("u_float", 23.0f);
        program.programUniformPutVector2f("u_vf2", new VectorI4F());
        program.programUniformPutVector3f("u_vf3", new VectorI4F());
        program.programUniformPutVector4f("u_vf4", new VectorI4F());

        program.programUniformPutInteger("u_int", 23);
        program.programUniformPutVector2i("u_vi2", new VectorI4I());
        program.programUniformPutVector3i("u_vi3", new VectorI4I());
        program.programUniformPutVector4i("u_vi4", new VectorI4I());

        program.programUniformPutMatrix3x3f("u_m3", new MatrixM3x3F());
        program.programUniformPutMatrix4x4f("u_m4", new MatrixM4x4F());
        program.programUniformPutTextureUnit("u_texture", units.get(0));

        program.programExecute(new Runnable() {
          @Override public void run()
          {
            called_inner.set(true);
          }
        });
      }
    });

    Assert.assertTrue(called.get());
    Assert.assertTrue(called_inner.get());
  }
}
