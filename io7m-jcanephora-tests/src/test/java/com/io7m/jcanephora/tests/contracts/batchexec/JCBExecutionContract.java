/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.contracts.batchexec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.ArrayDescriptorBuilderType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionAttributeMissing;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionProgramUniformMissing;
import com.io7m.jcanephora.JCGLExceptionProgramValidationError;
import com.io7m.jcanephora.JCGLExceptionTypeError;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.JCGLType;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.api.JCGLInterfaceCommonType;
import com.io7m.jcanephora.api.JCGLShadersCommonType;
import com.io7m.jcanephora.batchexec.JCBExecutor;
import com.io7m.jcanephora.batchexec.JCBExecutorProcedureType;
import com.io7m.jcanephora.batchexec.JCBExecutorType;
import com.io7m.jcanephora.batchexec.JCBProgramProcedureType;
import com.io7m.jcanephora.batchexec.JCBProgramType;
import com.io7m.jcanephora.batchexec.JCGLExceptionExecution;
import com.io7m.jcanephora.tests.TestContext;
import com.io7m.jcanephora.tests.TestUtilities;
import com.io7m.jcanephora.tests.contracts.TestContract;
import com.io7m.jcanephora.tests.types.LoadedShader;
import com.io7m.jfunctional.Pair;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheckException;
import com.io7m.jparasol.xml.FragmentParameter;
import com.io7m.jparasol.xml.VertexInput;
import com.io7m.jparasol.xml.VertexParameter;
import com.io7m.jtensors.MatrixM3x3F;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.MatrixReadable3x3FType;
import com.io7m.jtensors.MatrixReadable4x4FType;
import com.io7m.jtensors.VectorI4F;
import com.io7m.jtensors.VectorI4I;
import com.io7m.jtensors.VectorReadable2FType;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.jtensors.VectorReadable3FType;
import com.io7m.jtensors.VectorReadable3IType;
import com.io7m.jtensors.VectorReadable4FType;
import com.io7m.jtensors.VectorReadable4IType;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.jvvfs.FilesystemType;

@SuppressWarnings({ "null", "unchecked" }) public abstract class JCBExecutionContract implements
  TestContract
{
  private static ArrayBufferType makeArrayBuffer(
    final JCGLInterfaceCommonType gl)
  {
    try {
      final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "a_vf2",
        JCGLScalarType.TYPE_FLOAT,
        2));
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "a_vf3",
        JCGLScalarType.TYPE_FLOAT,
        3));
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "a_vf4",
        JCGLScalarType.TYPE_FLOAT,
        4));
      b.addAttribute(ArrayAttributeDescriptor.newAttribute(
        "a_f",
        JCGLScalarType.TYPE_FLOAT,
        1));

      final ArrayDescriptor d = b.build();
      return gl.arrayBufferAllocate(3, d, UsageHint.USAGE_STATIC_DRAW);
    } catch (final JCGLException e) {
      throw new AssertionError(e);
    }
  }

  private static Map<String, JCGLType> makeAttributes(
    final LoadedShader ls)
  {
    final HashMap<String, JCGLType> m = new HashMap<String, JCGLType>();

    for (final VertexInput vi : ls.meta.getDeclaredVertexInputs()) {
      m.put(vi.getName(), JCGLType.fromName(vi.getType()));
    }

    return m;
  }

  private static Map<String, JCGLType> makeUniforms(
    final LoadedShader ls)
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

  private static Pair<JCBExecutorType, LoadedShader> newProgram(
    final TestContext tc,
    final String name)
  {
    LoadedShader ls;
    JCBExecutorType e;
    try {
      final JCGLInterfaceCommonType gl =
        tc.getGLImplementation().getGLCommon();
      final FilesystemType fs = tc.getFilesystem();
      ls = LoadedShader.loadFrom(gl, fs, name, tc.getLog());
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
    return Pair.pair(e, ls);
  }

  @Before public final void checkSupport()
  {
    Assume.assumeTrue(this.isGLSupported());
  }

  @Test(expected = NullCheckException.class) public void testNullAttributes()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();
    final LoadedShader ls =
      LoadedShader.loadFrom(gl, fs, "simple", tc.getLog());
    JCBExecutor.newExecutorWithDeclarations(
      gl,
      ls.program,
      (Map<String, JCGLType>) TestUtilities.actuallyNull(),
      new HashMap<String, JCGLType>(),
      tc.getLog());
  }

  @Test(expected = NullCheckException.class) public void testNullGL()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();
    final LoadedShader ls =
      LoadedShader.loadFrom(gl, fs, "simple", tc.getLog());
    JCBExecutor.newExecutorWithoutDeclarations(
      (JCGLShadersCommonType) TestUtilities.actuallyNull(),
      ls.program,
      tc.getLog());
  }

  @Test(expected = NullCheckException.class) public void testNullLog()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();
    final LoadedShader ls =
      LoadedShader.loadFrom(gl, fs, "simple", tc.getLog());
    JCBExecutor.newExecutorWithoutDeclarations(
      gl,
      ls.program,
      (LogUsableType) TestUtilities.actuallyNull());
  }

  @Test(expected = NullCheckException.class) public void testNullProgram()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    JCBExecutor.newExecutorWithoutDeclarations(
      gl,
      (ProgramType) TestUtilities.actuallyNull(),
      tc.getLog());
  }

  @Test(expected = NullCheckException.class) public void testNullRunnable()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();
    final LoadedShader ls =
      LoadedShader.loadFrom(gl, fs, "simple", tc.getLog());

    JCBExecutorType e;
    try {
      e =
        JCBExecutor.newExecutorWithoutDeclarations(
          gl,
          ls.program,
          tc.getLog());
    } catch (final Throwable x) {
      throw new UnreachableCodeException(x);
    }
    e.execRun((JCBExecutorProcedureType<Exception>) TestUtilities
      .actuallyNull());
  }

  @Test(expected = NullCheckException.class) public void testNullUniforms()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();
    final LoadedShader ls =
      LoadedShader.loadFrom(gl, fs, "simple", tc.getLog());
    JCBExecutor.newExecutorWithDeclarations(
      gl,
      ls.program,
      new HashMap<String, JCGLType>(),
      (Map<String, JCGLType>) TestUtilities.actuallyNull(),
      tc.getLog());
  }

  @Test public void testProcedureCalled()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        Assert.assertEquals(pair.getRight().program, program.programGet());
        called.set(true);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAllAssigned()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    final JCGLInterfaceCommonType gc = tc.getGLImplementation().getGLCommon();
    final ArrayBufferType a = JCBExecutionContract.makeArrayBuffer(gc);
    final List<TextureUnitType> units = gc.textureGetUnits();

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);

        gc.arrayBufferBind(a);

        program.programAttributeBind("a_f", a.arrayGetAttribute("a_f"));
        program.programAttributeBind("a_vf2", a.arrayGetAttribute("a_vf2"));
        program.programAttributeBind("a_vf3", a.arrayGetAttribute("a_vf3"));
        program.programAttributeBind("a_vf4", a.arrayGetAttribute("a_vf4"));

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
        program.programUniformPutTextureUnit("u_sampler2d", units.get(0));
        program.programUniformPutTextureUnit("u_sampler_cube", units.get(0));

        program.programValidate();
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAllAssignedExec()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);
    final AtomicBoolean called_inner = new AtomicBoolean(false);

    final JCGLInterfaceCommonType gc = tc.getGLImplementation().getGLCommon();
    final ArrayBufferType a = JCBExecutionContract.makeArrayBuffer(gc);
    final List<TextureUnitType> units = gc.textureGetUnits();

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);

        gc.arrayBufferBind(a);

        program.programAttributeBind("a_f", a.arrayGetAttribute("a_f"));
        program.programAttributeBind("a_vf2", a.arrayGetAttribute("a_vf2"));
        program.programAttributeBind("a_vf3", a.arrayGetAttribute("a_vf3"));
        program.programAttributeBind("a_vf4", a.arrayGetAttribute("a_vf4"));

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
        program.programUniformPutTextureUnit("u_sampler2d", units.get(0));
        program.programUniformPutTextureUnit("u_sampler_cube", units.get(0));

        program.programExecute(new JCBProgramProcedureType<Exception>() {
          @Override public void call()
            throws JCGLException,
              Exception
          {
            called_inner.set(true);
          }
        });
      }
    });

    Assert.assertTrue(called.get());
    Assert.assertTrue(called_inner.get());
  }

  @Test public void testProgramAttributeBind()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    final JCGLInterfaceCommonType gc = tc.getGLImplementation().getGLCommon();
    final ArrayBufferType a = JCBExecutionContract.makeArrayBuffer(gc);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        gc.arrayBufferBind(a);
        program.programAttributeBind("a_f", a.arrayGetAttribute("a_f"));
        program.programAttributeBind("a_vf2", a.arrayGetAttribute("a_vf2"));
        program.programAttributeBind("a_vf3", a.arrayGetAttribute("a_vf3"));
        program.programAttributeBind("a_vf4", a.arrayGetAttribute("a_vf4"));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public
    void
    testProgramAttributeBindNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    final ArrayBufferType a =
      JCBExecutionContract.makeArrayBuffer(tc
        .getGLImplementation()
        .getGLCommon());

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programAttributeBind(
          "nonexistent",
          a.arrayGetAttribute("a_f"));
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramAttributeBindNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    final ArrayBufferType a =
      JCBExecutionContract.makeArrayBuffer(tc
        .getGLImplementation()
        .getGLCommon());

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programAttributeBind(
            (String) TestUtilities.actuallyNull(),
            a.arrayGetAttribute("a_f"));
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramAttributeBindNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programAttributeBind(
            "a_vf4",
            (ArrayAttributeType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramAttributeBindOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    final JCGLInterfaceCommonType gc = tc.getGLImplementation().getGLCommon();
    final ArrayBufferType a = JCBExecutionContract.makeArrayBuffer(gc);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        gc.arrayBufferBind(a);
        program.programAttributeBind("a_f_opt", a.arrayGetAttribute("a_f"));
        program.programAttributeBind(
          "a_vf2_opt",
          a.arrayGetAttribute("a_vf2"));
        program.programAttributeBind(
          "a_vf3_opt",
          a.arrayGetAttribute("a_vf3"));
        program.programAttributeBind(
          "a_vf4_opt",
          a.arrayGetAttribute("a_vf4"));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionTypeError.class) public
    void
    testProgramAttributeBindWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    final ArrayBufferType a =
      JCBExecutionContract.makeArrayBuffer(tc
        .getGLImplementation()
        .getGLCommon());

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programAttributeBind("a_vf4", a.arrayGetAttribute("a_f"));
      }
    });

    throw new UnreachableCodeException();
  }

  @Test public void testProgramAttributePutFloat()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutFloat("a_f", 23.0f);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public
    void
    testProgramAttributePutFloatNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programAttributePutFloat("nonexistent", 23.0f);
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramAttributePutFloatNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programAttributePutFloat(
            (String) TestUtilities.actuallyNull(),
            23.0f);
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramAttributePutFloatOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutFloat("a_f_opt", 23.0f);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAttributePutVector2f()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector2F("a_vf2", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public
    void
    testProgramAttributePutVector2fNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programAttributePutVector2F("nonexistent", new VectorI4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramAttributePutVector2fNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programAttributePutVector2F(
            (String) TestUtilities.actuallyNull(),
            new VectorI4F());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramAttributePutVector2fNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programAttributePutVector2F(
            "a_vf2",
            (VectorReadable2FType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramAttributePutVector2fOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector2F("a_vf2_opt", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAttributePutVector3f()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector3F("a_vf3", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public
    void
    testProgramAttributePutVector3fNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programAttributePutVector3F("nonexistent", new VectorI4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramAttributePutVector3fNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programAttributePutVector3F(
            (String) TestUtilities.actuallyNull(),
            new VectorI4F());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramAttributePutVector3fNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programAttributePutVector3F(
            "a_vf3",
            (VectorReadable3FType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramAttributePutVector3fOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector3F("a_vf3_opt", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramAttributePutVector4f()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector4F("a_vf4", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public
    void
    testProgramAttributePutVector4fNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programAttributePutVector4F("nonexistent", new VectorI4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramAttributePutVector4fNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programAttributePutVector4F(
            (String) TestUtilities.actuallyNull(),
            new VectorI4F());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramAttributePutVector4fNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programAttributePutVector4F(
            "a_vf4",
            (VectorReadable4FType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramAttributePutVector4fOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programAttributePutVector4F("a_vf4_opt", new VectorI4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionTypeError.class) public
    void
    testProgramContainsIllTypedAttribute()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();

    final Pair<JCBExecutorType, LoadedShader> p =
      JCBExecutionContract.newProgram(tc, "everything");

    final Map<String, JCGLType> a =
      JCBExecutionContract.makeAttributes(p.getRight());
    a.put("a_f", JCGLType.TYPE_BOOLEAN);

    JCBExecutor.newExecutorWithDeclarations(
      gl,
      p.getRight().program,
      JCBExecutionContract.makeUniforms(p.getRight()),
      a,
      tc.getLog());
  }

  @Test(expected = JCGLExceptionTypeError.class) public
    void
    testProgramContainsIllTypedUniform()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();

    final Pair<JCBExecutorType, LoadedShader> p =
      JCBExecutionContract.newProgram(tc, "everything");

    final Map<String, JCGLType> u =
      JCBExecutionContract.makeUniforms(p.getRight());
    u.put("u_float", JCGLType.TYPE_INTEGER);

    JCBExecutor.newExecutorWithDeclarations(
      gl,
      p.getRight().program,
      u,
      JCBExecutionContract.makeAttributes(p.getRight()),
      tc.getLog());
  }

  @Test(expected = JCGLExceptionAttributeMissing.class) public
    void
    testProgramContainsUndeclaredAttribute()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();
    final LoadedShader ls =
      LoadedShader.loadFrom(gl, fs, "everything", tc.getLog());

    final Map<String, JCGLType> a = JCBExecutionContract.makeAttributes(ls);
    a.remove("a_f");

    JCBExecutor.newExecutorWithDeclarations(
      gl,
      ls.program,
      JCBExecutionContract.makeUniforms(ls),
      a,
      tc.getLog());
  }

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramContainsUndeclaredUniform()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();
    final LoadedShader ls =
      LoadedShader.loadFrom(gl, fs, "everything", tc.getLog());
    final HashMap<String, JCGLType> u = new HashMap<String, JCGLType>();
    u.put("nonexistent", JCGLType.TYPE_BOOLEAN);

    JCBExecutor
      .newExecutorWithDeclarations(gl, ls.program, u, u, tc.getLog());
  }

  @Test(expected = JCGLExceptionDeleted.class) public
    void
    testProgramDeleted()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final JCGLInterfaceCommonType gl = tc.getGLImplementation().getGLCommon();
    final FilesystemType fs = tc.getFilesystem();
    final LoadedShader ls =
      LoadedShader.loadFrom(gl, fs, "simple", tc.getLog());
    gl.programDelete(ls.program);

    JCBExecutor.newExecutorWithoutDeclarations(gl, ls.program, tc.getLog());
  }

  @Test public void testProgramExecException()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");
    final AtomicBoolean caught = new AtomicBoolean(false);

    final JCGLInterfaceCommonType gc = tc.getGLImplementation().getGLCommon();
    final ArrayBufferType a = JCBExecutionContract.makeArrayBuffer(gc);

    final IllegalArgumentException z =
      new IllegalArgumentException("Caught!");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          gc.arrayBufferBind(a);
          program.programAttributeBind(
            "v_position",
            a.arrayGetAttribute("a_vf3"));
          program.programUniformPutVector4f("f_ccolour", new VectorI4F());
          program.programUniformPutMatrix4x4f(
            "m_projection",
            new MatrixM4x4F());
          program.programUniformPutMatrix4x4f(
            "m_modelview",
            new MatrixM4x4F());
          program.programExecute(new JCBProgramProcedureType<Exception>() {
            @Override public void call()
              throws JCGLException,
                Exception
            {
              throw z;
            }
          });
        }
      });
    } catch (final JCGLExceptionExecution x) {
      final Throwable k = x.getCause();
      Assert.assertSame(z, k);
      caught.set(true);
    }

    Assert.assertTrue(caught.get());
  }

  @Test public void testProgramUniformPutFloat()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutFloat("u_float", 23.0f);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramUniformPutFloatNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programUniformPutFloat("nonexistent", 23.0f);
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutFloatNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutFloat(
            (String) TestUtilities.actuallyNull(),
            23.0f);
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutFloatOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutFloat("u_float_opt", 23.0f);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionTypeError.class) public
    void
    testProgramUniformPutFloatWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutFloat("u_vf2", 23.0f);
      }
    });

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutInteger()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutInteger("u_int", 23);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramUniformPutIntegerNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programUniformPutInteger("nonexistent", 23);
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutIntegerNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutInteger(
            (String) TestUtilities.actuallyNull(),
            23);
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutIntegerOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutInteger("u_int_opt", 23);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionTypeError.class) public
    void
    testProgramUniformPutIntegerWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutInteger("u_vf2", 23);
      }
    });

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutMatrix3x3f()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix3x3f("u_m3", new MatrixM3x3F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramUniformPutMatrix3x3fNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programUniformPutMatrix3x3f("nonexistent", new MatrixM3x3F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutMatrix3x3fNullName()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutMatrix3x3f(
            (String) TestUtilities.actuallyNull(),
            new MatrixM3x3F());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutMatrix3x3fNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutMatrix3x3f(
            "u_m3",
            (MatrixReadable3x3FType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutMatrix3x3fOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix3x3f("u_m3_opt", new MatrixM3x3F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionTypeError.class) public
    void
    testProgramUniformPutMatrix3x3fWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix3x3f("u_vf2", new MatrixM3x3F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutMatrix4x4f()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix4x4f("u_m4", new MatrixM4x4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramUniformPutMatrix4x4fNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programUniformPutMatrix4x4f("nonexistent", new MatrixM4x4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutMatrix4x4fNullName()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutMatrix4x4f(
            (String) TestUtilities.actuallyNull(),
            new MatrixM4x4F());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutMatrix4x4fNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutMatrix4x4f(
            "u_m4",
            (MatrixReadable4x4FType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutMatrix4x4fOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix4x4f("u_m4_opt", new MatrixM4x4F());
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionTypeError.class) public
    void
    testProgramUniformPutMatrix4x4fWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutMatrix4x4f("u_vf2", new MatrixM4x4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutTextureUnit()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    final TextureUnitType unit =
      tc.getGLImplementation().getGLCommon().textureGetUnits().get(0);
    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutTextureUnit("u_sampler2d", unit);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramUniformPutTextureUnitNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    final TextureUnitType unit =
      tc.getGLImplementation().getGLCommon().textureGetUnits().get(0);
    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programUniformPutTextureUnit("nonexistent", unit);
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutTextureUnitNull()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "simple");

    final TextureUnitType unit =
      tc.getGLImplementation().getGLCommon().textureGetUnits().get(0);

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutTextureUnit(
            (String) TestUtilities.actuallyNull(),
            unit);
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutTextureUnitOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    final TextureUnitType unit =
      tc.getGLImplementation().getGLCommon().textureGetUnits().get(0);
    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutTextureUnit("u_sampler2d_opt", unit);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test public void testProgramUniformPutVector2f()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
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

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramUniformPutVector2fNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programUniformPutVector2f("nonexistent", new VectorI4F(
          0.0f,
          1.0f,
          2.0f,
          3.0f));
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector2fNullName()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector2f(
            (String) TestUtilities.actuallyNull(),
            new VectorI4F(0.0f, 1.0f, 2.0f, 3.0f));
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector2fNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector2f(
            "u_vf2",
            (VectorReadable2FType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector2fOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
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

  @Test(expected = JCGLExceptionTypeError.class) public
    void
    testProgramUniformPutVector2fWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector2f("u_vf3", new VectorI4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector2i()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector2i("u_vi2", new VectorI4I(0, 1, 2, 3));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramUniformPutVector2iNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programUniformPutVector2i("nonexistent", new VectorI4I(
          0,
          1,
          2,
          3));
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector2iNullName()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector2i(
            (String) TestUtilities.actuallyNull(),
            new VectorI4I(0, 1, 2, 3));
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector2iNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector2i(
            "u_vi2",
            (VectorReadable2IType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector2iOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
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

  @Test public void testProgramUniformPutVector3f()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
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

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramUniformPutVector3fNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programUniformPutVector3f("nonexistent", new VectorI4F(
          0.0f,
          1.0f,
          2.0f,
          3.0f));
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector3fNullName()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector3f(
            (String) TestUtilities.actuallyNull(),
            new VectorI4F(0.0f, 1.0f, 2.0f, 3.0f));
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector3fNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector3f(
            "u_vf3",
            (VectorReadable3FType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector3fOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
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

  @Test(expected = JCGLExceptionTypeError.class) public
    void
    testProgramUniformPutVector3fWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector3f("u_vf2", new VectorI4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector3i()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector3i("u_vi3", new VectorI4I(0, 1, 2, 3));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramUniformPutVector3iNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programUniformPutVector3i("nonexistent", new VectorI4I(
          0,
          1,
          2,
          3));
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector3iNullName()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector3i(
            (String) TestUtilities.actuallyNull(),
            new VectorI4I(0, 1, 2, 3));
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector3iNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector3i(
            "u_vi3",
            (VectorReadable3IType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector3iOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
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

  @Test public void testProgramUniformPutVector4f()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
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

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramUniformPutVector4fNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programUniformPutVector4f("nonexistent", new VectorI4F(
          0.0f,
          1.0f,
          2.0f,
          3.0f));
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector4fNullName()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector4f(
            (String) TestUtilities.actuallyNull(),
            new VectorI4F(0.0f, 1.0f, 2.0f, 3.0f));
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector4fNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector4f(
            "u_vf4",
            (VectorReadable4FType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector4fOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
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

  @Test(expected = JCGLExceptionTypeError.class) public
    void
    testProgramUniformPutVector4fWrongType()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector4f("u_vf3", new VectorI4F());
      }
    });

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector4i()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutVector4i("u_vi4", new VectorI4I(0, 1, 2, 3));
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionProgramUniformMissing.class) public
    void
    testProgramUniformPutVector4iNonexistent()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        program.programUniformPutVector4i("nonexistent", new VectorI4I(
          0,
          1,
          2,
          3));
      }
    });

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector4iNullName()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector4i(
            (String) TestUtilities.actuallyNull(),
            new VectorI4I(0, 1, 2, 3));
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test(expected = NullCheckException.class) public
    void
    testProgramUniformPutVector4iNullValue()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();
    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");

    try {
      pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
        @Override public void call(
          final JCBProgramType program)
          throws JCGLException,
            Exception
        {
          program.programUniformPutVector4i(
            "u_vi4",
            (VectorReadable4IType) TestUtilities.actuallyNull());
        }
      });
    } catch (final JCGLExceptionExecution e) {
      final NullCheckException x = (NullCheckException) e.getCause();
      throw x;
    }

    throw new UnreachableCodeException();
  }

  @Test public void testProgramUniformPutVector4iOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
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

  @Test public void testProgramUniformReuseAssigned()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutFloat("u_float", 23.0f);
        program.programUniformUseExisting("u_float");
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionProgramValidationError.class) public
    void
    testProgramUniformReuseUnassigned()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformUseExisting("u_float");
      }
    });
  }

  @Test public void testProgramUniformsOptimizedOut()
    throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything_opt");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programUniformPutFloat("u_float_opt", 23.0f);
      }
    });

    Assert.assertTrue(called.get());
  }

  @Test(expected = JCGLExceptionProgramValidationError.class) public
    void
    testProgramUniformUnassigned()
      throws JCGLException
  {
    final TestContext tc = this.newTestContext();

    final Pair<JCBExecutorType, LoadedShader> pair =
      JCBExecutionContract.newProgram(tc, "everything");
    final AtomicBoolean called = new AtomicBoolean(false);

    pair.getLeft().execRun(new JCBExecutorProcedureType<Exception>() {
      @Override public void call(
        final JCBProgramType program)
        throws JCGLException,
          Exception
      {
        called.set(true);
        program.programValidate();
      }
    });

    Assert.assertTrue(called.get());
  }
}
