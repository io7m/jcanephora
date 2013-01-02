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
