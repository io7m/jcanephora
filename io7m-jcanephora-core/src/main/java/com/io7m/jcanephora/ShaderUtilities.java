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

package com.io7m.jcanephora;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;

public final class ShaderUtilities
{
  /**
   * <p>
   * Read lines of input from <code>stream</code> until there is nothing left
   * to read, and return a list of all the lines returned. Each line will be
   * terminated with (at least) an LF character.
   * </p>
   * 
   * @throws ConstraintError
   *           Iff <code>stream == null</code>.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading.
   */

  public static @Nonnull List<String> readLines(
    final @Nonnull InputStream stream)
    throws ConstraintError,
      IOException
  {
    Constraints.constrainNotNull(stream, "Input stream");

    final BufferedReader reader =
      new BufferedReader(new InputStreamReader(stream));
    final ArrayList<String> lines = new ArrayList<String>();
    for (;;) {
      final String line = reader.readLine();
      if (line == null) {
        break;
      }
      lines.add(line + "\n");
    }
    reader.close();
    return lines;
  }
}
