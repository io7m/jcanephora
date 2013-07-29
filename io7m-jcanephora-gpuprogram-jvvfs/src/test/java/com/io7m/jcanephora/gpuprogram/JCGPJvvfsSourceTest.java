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

import java.util.ArrayList;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jlog.Log;
import com.io7m.jvvfs.Filesystem;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.FilesystemError.Code;
import com.io7m.jvvfs.PathVirtual;

public class JCGPJvvfsSourceTest
{
  @SuppressWarnings("static-method") @Test public void testFileEvaluate()
    throws ConstraintError,
      Exception
  {
    final Filesystem fs =
      Filesystem.makeWithoutArchiveDirectory(JCGPJvvfsSourceTest.makeLog());
    fs.mountClasspathArchive(JCGPJvvfsSourceTest.class, PathVirtual.ROOT);

    final JCGPJvvfsSource s =
      new JCGPJvvfsSource(
        fs,
        PathVirtual
          .ofString("/com/io7m/jcanephora/gpuprogram/jvvfs/example.v"));
    final JCGPCompilationContext context =
      new JCGPCompilationContext(
        new JCGLSLVersionNumber(1, 0, 0),
        JCGLApi.JCGL_ES);
    final ArrayList<String> output = new ArrayList<String>();
    s.sourceGet(context, output);

    Assert.assertEquals("void", output.get(0));
    Assert.assertEquals("main ()", output.get(1));
    Assert.assertEquals("{", output.get(2));
    Assert.assertEquals("  gl_Position = vec4(1, 2, 3, 1);", output.get(3));
    Assert.assertEquals("}", output.get(4));
    Assert.assertEquals(5, output.size());
  }

  private static Log makeLog()
  {
    final Properties props = new Properties();
    return new Log(props, "com.io7m.jcanephora.gpuprogram", "tests");
  }

  @SuppressWarnings("static-method") @Test(expected = FilesystemError.class) public
    void
    testFileNonexistent()
      throws ConstraintError,
        Exception
  {
    try {
      final Filesystem fs =
        Filesystem.makeWithoutArchiveDirectory(JCGPJvvfsSourceTest.makeLog());
      fs.mountClasspathArchive(JCGPJvvfsSourceTest.class, PathVirtual.ROOT);

      final JCGPJvvfsSource s =
        new JCGPJvvfsSource(
          fs,
          PathVirtual.ofString("/com/io7m/jcanephora/gpuprogram/nonexistent"));

      final JCGPCompilationContext context =
        new JCGPCompilationContext(
          new JCGLSLVersionNumber(1, 0, 0),
          JCGLApi.JCGL_ES);
      final ArrayList<String> output = new ArrayList<String>();
      s.sourceGet(context, output);
    } catch (final FilesystemError e) {
      Assert.assertEquals(Code.FS_ERROR_NONEXISTENT, e.getCode());
      throw e;
    }
  }
}
