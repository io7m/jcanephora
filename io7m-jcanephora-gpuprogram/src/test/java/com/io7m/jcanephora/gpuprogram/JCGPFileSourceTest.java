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
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLSLVersionNumber;

public class JCGPFileSourceTest
{
  @SuppressWarnings("static-method") @Test public void testFileEvaluate()
    throws ConstraintError,
      Exception
  {
    final File td = TestData.getTestDataDirectory();
    final JCGPFileSource fs =
      new JCGPFileSource(new File(new File(td, "data"), "example.v"));
    final JCGPGeneratorContext context =
      new JCGPGeneratorContext(
        new JCGLSLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES);
    final ArrayList<String> output = new ArrayList<String>();
    fs.sourceGet(context, output);

    Assert.assertEquals("void", output.get(0));
    Assert.assertEquals("main ()", output.get(1));
    Assert.assertEquals("{", output.get(2));
    Assert.assertEquals("  gl_Position = vec4(1, 2, 3, 1);", output.get(3));
    Assert.assertEquals("}", output.get(4));
    Assert.assertEquals(5, output.size());
  }

  @SuppressWarnings("static-method") @Test public void testFileChanged()
    throws ConstraintError,
      Exception
  {
    final File td = TestData.getTestDataDirectory();
    final File file = new File(new File(td, "data"), "example.v");
    Assert.assertTrue(file.isFile());

    final JCGPFileSource fs = new JCGPFileSource(file);
    Assert.assertTrue(fs.sourceChanged());
    file.setLastModified(100);
    Assert.assertTrue(fs.sourceChanged());

    final JCGPGeneratorContext context =
      new JCGPGeneratorContext(
        new JCGLSLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES);
    final ArrayList<String> output = new ArrayList<String>();

    fs.sourceGet(context, output);
    Assert.assertFalse(fs.sourceChanged());
    file.setLastModified(100000);
    Assert.assertTrue(fs.sourceChanged());
  }

  @SuppressWarnings("static-method") @Test(
    expected = FileNotFoundException.class) public void testFileNonexistent()
    throws ConstraintError,
      Exception
  {
    final File td = TestData.getTestDataDirectory();
    final JCGPFileSource fs = new JCGPFileSource(new File(td, "nonexistent"));
    final JCGPGeneratorContext context =
      new JCGPGeneratorContext(
        new JCGLSLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES);
    final ArrayList<String> output = new ArrayList<String>();
    fs.sourceGet(context, output);
  }
}
