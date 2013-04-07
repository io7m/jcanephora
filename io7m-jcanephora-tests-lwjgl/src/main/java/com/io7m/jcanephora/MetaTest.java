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

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.functional.Pair;

public final class MetaTest
{
  @SuppressWarnings("static-method") @Test public void testVersionParsing()
  {
    {
      final String s = "OpenGL ES 1.0 Mesa 9.0.1";
      final Pair<Integer, Integer> p = GLES2Functions.metaParseVersion(s);
      final boolean es = GLES2Functions.metaVersionIsES(s);
      Assert.assertEquals(1, p.first.intValue());
      Assert.assertEquals(0, p.second.intValue());
      Assert.assertTrue(es);
    }

    {
      final String s = "OpenGL ES 2.0 Mesa 9.0.1";
      final Pair<Integer, Integer> p = GLES2Functions.metaParseVersion(s);
      final boolean es = GLES2Functions.metaVersionIsES(s);
      Assert.assertEquals(2, p.first.intValue());
      Assert.assertEquals(0, p.second.intValue());
      Assert.assertTrue(es);
    }

    {
      final String s = "OpenGL ES 2.1 Mesa 9.0.1";
      final Pair<Integer, Integer> p = GLES2Functions.metaParseVersion(s);
      final boolean es = GLES2Functions.metaVersionIsES(s);
      Assert.assertEquals(2, p.first.intValue());
      Assert.assertEquals(1, p.second.intValue());
      Assert.assertTrue(es);
    }

    {
      final String s = "OpenGL ES 3.0 Mesa 9.0.1";
      final Pair<Integer, Integer> p = GLES2Functions.metaParseVersion(s);
      final boolean es = GLES2Functions.metaVersionIsES(s);
      Assert.assertEquals(3, p.first.intValue());
      Assert.assertEquals(0, p.second.intValue());
      Assert.assertTrue(es);
    }

    {
      final String s = "OpenGL ES 3.1 Mesa 9.0.1";
      final Pair<Integer, Integer> p = GLES2Functions.metaParseVersion(s);
      final boolean es = GLES2Functions.metaVersionIsES(s);
      Assert.assertEquals(3, p.first.intValue());
      Assert.assertEquals(1, p.second.intValue());
      Assert.assertTrue(es);
    }

    {
      final String s = "1.0";
      final Pair<Integer, Integer> p = GLES2Functions.metaParseVersion(s);
      final boolean es = GLES2Functions.metaVersionIsES(s);
      Assert.assertEquals(1, p.first.intValue());
      Assert.assertEquals(0, p.second.intValue());
      Assert.assertFalse(es);
    }

    {
      final String s = "2.0";
      final Pair<Integer, Integer> p = GLES2Functions.metaParseVersion(s);
      final boolean es = GLES2Functions.metaVersionIsES(s);
      Assert.assertEquals(2, p.first.intValue());
      Assert.assertEquals(0, p.second.intValue());
      Assert.assertFalse(es);
    }

    {
      final String s = "2.1";
      final Pair<Integer, Integer> p = GLES2Functions.metaParseVersion(s);
      final boolean es = GLES2Functions.metaVersionIsES(s);
      Assert.assertEquals(2, p.first.intValue());
      Assert.assertEquals(1, p.second.intValue());
      Assert.assertFalse(es);
    }

    {
      final String s = "3.1";
      final Pair<Integer, Integer> p = GLES2Functions.metaParseVersion(s);
      final boolean es = GLES2Functions.metaVersionIsES(s);
      Assert.assertEquals(3, p.first.intValue());
      Assert.assertEquals(1, p.second.intValue());
      Assert.assertFalse(es);
    }
  }
}
