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

package com.io7m.jcanephora.tests.jogl;

import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLInterfaceGL33Type;
import com.io7m.jcanephora.tests.contracts.JCGLShaderUniformsContract;
import com.io7m.jcanephora.tests.contracts.JCGLShadersContract;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class JOGLShaderUniformsTestGL33 extends JCGLShaderUniformsContract
{
  @Override
  protected JCGLInterfaceGL33Type getGL33()
  {
    final JCGLContextType c = JOGLTestContexts.newGL33Context("main", 24, 8);
    return c.contextGetGL33();
  }

  @Override
  protected List<String> getShaderLines(
    final String name)
  {
    final Class<JCGLShadersContract> c = JCGLShadersContract.class;
    final List<String> lines = new ArrayList<>(32);
    try (final InputStream is = c.getResourceAsStream(name)) {
      try (final BufferedReader reader = new BufferedReader(
        new InputStreamReader(is))) {
        while (true) {
          final String line = reader.readLine();
          if (line == null) {
            return lines;
          }
          lines.add(line + "\n");
        }
      }
    } catch (final IOException e) {
      throw new IOError(e);
    }
  }

  @Override
  public void onTestCompleted()
  {
    JOGLTestContexts.closeAllContexts();
  }
}
