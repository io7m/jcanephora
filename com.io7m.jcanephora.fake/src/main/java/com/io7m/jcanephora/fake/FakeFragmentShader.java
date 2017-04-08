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

package com.io7m.jcanephora.fake;

import com.io7m.jcanephora.core.JCGLFragmentShaderType;
import com.io7m.jnull.NullCheck;

import java.util.List;

final class FakeFragmentShader extends FakeReferable
  implements JCGLFragmentShaderType
{
  private final String name;

  FakeFragmentShader(
    final FakeContext in_context,
    final int in_id,
    final String in_name,
    final List<String> in_lines)
  {
    super(in_context, in_id);
    this.name = NullCheck.notNull(in_name, "Name");
    NullCheck.notNull(in_lines, "Lines");
  }

  @Override
  public String getName()
  {
    return this.name;
  }
}
