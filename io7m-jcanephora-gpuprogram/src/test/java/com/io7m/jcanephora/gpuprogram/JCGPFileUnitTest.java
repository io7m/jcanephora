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

package com.io7m.jcanephora.gpuprogram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLApiKindES;
import com.io7m.jcanephora.JCGLApiKindFull;
import com.io7m.jcanephora.JCGLCompileException;
import com.io7m.jcanephora.JCGLUnsupportedException;
import com.io7m.jcanephora.JCGLVersionNumber;

public class JCGPFileUnitTest
{
  /**
   * Evaluating the source of a unit when the unit doesn't support the current
   * ES version fails.
   */

  @SuppressWarnings({ "static-method" }) @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testEvaluateBadESVersion()
      throws ConstraintError,
        FileNotFoundException,
        IOException,
        JCGLUnsupportedException,
        JCGLCompileException
  {
    final File td = TestData.getTestDataDirectory();
    final File ufile = new File(td, "data/example.v");

    final JCGPVersionRange<JCGLApiKindES> v_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLVersionNumber(2, 0, 0),
        new JCGLVersionNumber(3, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> v_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLVersionNumber(2, 1, 0),
        new JCGLVersionNumber(4, 3, 0));
    final String name = "com.io7m.unit";
    final LinkedList<String> imports = new LinkedList<String>();
    final JCGPFileUnit u =
      new JCGPFileUnit(name, imports, v_es, v_full, ufile);

    System.err.println("Evaluating file: " + ufile);
    u.sourceEvaluate(new JCGLVersionNumber(1, 0, 0), JCGLApi.JCGL_ES);
  }

  /**
   * Evaluating the source of a unit when the unit doesn't support any ES
   * version fails.
   */

  @SuppressWarnings({ "static-method" }) @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testEvaluateBadESVersionAny()
      throws ConstraintError,
        FileNotFoundException,
        IOException,
        JCGLUnsupportedException,
        JCGLCompileException
  {
    final File td = TestData.getTestDataDirectory();
    final File ufile = new File(td, "data/example.v");

    final JCGPVersionRange<JCGLApiKindFull> v_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLVersionNumber(2, 1, 0),
        new JCGLVersionNumber(4, 3, 0));
    final String name = "com.io7m.unit";
    final LinkedList<String> imports = new LinkedList<String>();
    final JCGPFileUnit u =
      new JCGPFileUnit(name, imports, null, v_full, ufile);

    System.err.println("Evaluating file: " + ufile);
    u.sourceEvaluate(new JCGLVersionNumber(1, 0, 0), JCGLApi.JCGL_ES);
  }

  /**
   * Evaluating the source of a unit when the unit doesn't support the current
   * OpenGL version fails.
   */

  @SuppressWarnings({ "static-method" }) @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testEvaluateBadFullVersion()
      throws ConstraintError,
        FileNotFoundException,
        IOException,
        JCGLUnsupportedException,
        JCGLCompileException
  {
    final File td = TestData.getTestDataDirectory();
    final File ufile = new File(td, "data/example.v");

    final JCGPVersionRange<JCGLApiKindES> v_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLVersionNumber(2, 0, 0),
        new JCGLVersionNumber(3, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> v_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLVersionNumber(2, 1, 0),
        new JCGLVersionNumber(4, 3, 0));
    final String name = "com.io7m.unit";
    final LinkedList<String> imports = new LinkedList<String>();
    final JCGPFileUnit u =
      new JCGPFileUnit(name, imports, v_es, v_full, ufile);

    System.err.println("Evaluating File: " + ufile);
    u.sourceEvaluate(new JCGLVersionNumber(1, 0, 0), JCGLApi.JCGL_FULL);
  }

  /**
   * Evaluating the source of a unit when the unit doesn't support any OpenGL
   * version fails.
   */

  @SuppressWarnings({ "static-method" }) @Test(
    expected = JCGLUnsupportedException.class) public
    void
    testEvaluateBadFullVersionAny()
      throws ConstraintError,
        FileNotFoundException,
        IOException,
        JCGLUnsupportedException,
        JCGLCompileException
  {
    final File td = TestData.getTestDataDirectory();
    final File ufile = new File(td, "data/example.v");

    final JCGPVersionRange<JCGLApiKindES> v_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLVersionNumber(2, 0, 0),
        new JCGLVersionNumber(3, 0, 0));
    final String name = "com.io7m.unit";
    final LinkedList<String> imports = new LinkedList<String>();
    final JCGPFileUnit u = new JCGPFileUnit(name, imports, v_es, null, ufile);

    System.err.println("Evaluating file: " + ufile);
    u.sourceEvaluate(new JCGLVersionNumber(1, 0, 0), JCGLApi.JCGL_FULL);
  }

  /**
   * Evaluating the source of a URI works.
   */

  @SuppressWarnings({ "static-method" }) @Test public void testEvaluateOK()
    throws ConstraintError,
      FileNotFoundException,
      IOException,
      JCGLUnsupportedException,
      JCGLCompileException
  {
    final File td = TestData.getTestDataDirectory();
    final File ufile = new File(td, "data/example.v");
    final String contents = TestData.slurpFile(ufile);

    final JCGPVersionRange<JCGLApiKindES> v_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLVersionNumber(2, 0, 0),
        new JCGLVersionNumber(3, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> v_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLVersionNumber(2, 1, 0),
        new JCGLVersionNumber(4, 3, 0));
    final String name = "com.io7m.unit";
    final LinkedList<String> imports = new LinkedList<String>();
    final JCGPFileUnit u =
      new JCGPFileUnit(name, imports, v_es, v_full, ufile);

    System.err.println("Evaluating file: " + ufile);
    final String text =
      u.sourceEvaluate(new JCGLVersionNumber(2, 1, 0), JCGLApi.JCGL_FULL);

    Assert.assertEquals(contents, text);
  }

  /**
   * Evaluating the source (ES) of a URI works.
   */

  @SuppressWarnings({ "static-method" }) @Test public
    void
    testEvaluateOK_ES()
      throws ConstraintError,
        FileNotFoundException,
        IOException,
        JCGLUnsupportedException,
        JCGLCompileException
  {
    final File td = TestData.getTestDataDirectory();
    final File ufile = new File(td, "data/example.v");
    final String contents = TestData.slurpFile(ufile);

    final JCGPVersionRange<JCGLApiKindES> v_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLVersionNumber(2, 0, 0),
        new JCGLVersionNumber(3, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> v_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLVersionNumber(2, 1, 0),
        new JCGLVersionNumber(4, 3, 0));
    final String name = "com.io7m.unit";
    final LinkedList<String> imports = new LinkedList<String>();
    final JCGPFileUnit u =
      new JCGPFileUnit(name, imports, v_es, v_full, ufile);

    System.err.println("Evaluating file: " + ufile);
    final String text =
      u.sourceEvaluate(new JCGLVersionNumber(2, 1, 0), JCGLApi.JCGL_ES);

    Assert.assertEquals(contents, text);
  }

  /**
   * Trying to create a unit with a null list of imports fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public void testNullImports()
    throws ConstraintError
  {
    new JCGPFileUnit(
      "com.io7m.unit",
      null,
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLVersionNumber(2, 0, 0),
        new JCGLVersionNumber(3, 0, 0)),
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLVersionNumber(2, 1, 0),
        new JCGLVersionNumber(4, 3, 0)),
      new File("nonexistent.txt"));
  }

  /**
   * Trying to create a unit without any version ranges fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public void testNullRanges()
    throws ConstraintError
  {
    new JCGPFileUnit(
      "com.io7m.unit",
      new LinkedList<String>(),
      null,
      null,
      new File("nonexistent.txt"));
  }

  /**
   * Trying to create a unit with a null unit name fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public void testNullUnitName()
    throws ConstraintError
  {
    new JCGPFileUnit(
      null,
      new LinkedList<String>(),
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLVersionNumber(2, 0, 0),
        new JCGLVersionNumber(3, 0, 0)),
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLVersionNumber(2, 1, 0),
        new JCGLVersionNumber(4, 3, 0)),
      new File("nonexistent.txt"));
  }

  /**
   * Trying to create a unit with a null URI fails.
   */

  @SuppressWarnings({ "unused", "static-method" }) @Test(
    expected = ConstraintError.class) public void testNullURI()
    throws ConstraintError
  {
    new JCGPFileUnit(
      "com.io7m.unit",
      new LinkedList<String>(),
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLVersionNumber(2, 0, 0),
        new JCGLVersionNumber(3, 0, 0)),
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLVersionNumber(2, 1, 0),
        new JCGLVersionNumber(4, 3, 0)),
      null);
  }

  /**
   * Creating a unit with valid parameters works.
   */

  @SuppressWarnings({ "static-method" }) @Test public void testOK()
    throws ConstraintError
  {
    final JCGPVersionRange<JCGLApiKindES> v_es =
      new JCGPVersionRange<JCGLApiKindES>(
        new JCGLVersionNumber(2, 0, 0),
        new JCGLVersionNumber(3, 0, 0));
    final JCGPVersionRange<JCGLApiKindFull> v_full =
      new JCGPVersionRange<JCGLApiKindFull>(
        new JCGLVersionNumber(2, 1, 0),
        new JCGLVersionNumber(4, 3, 0));
    final String name = "com.io7m.unit";
    final LinkedList<String> imports = new LinkedList<String>();
    final File source = new File("nonexistent.txt");

    final JCGPFileUnit u =
      new JCGPFileUnit(name, imports, v_es, v_full, source);

    Assert.assertEquals(v_es, u.sourceESVersionRange());
    Assert.assertEquals(v_full, u.sourceFullVersionRange());
    Assert.assertEquals(name, u.sourceUnitName());
    Assert.assertEquals(imports, u.sourceGetImports());
  }
}
