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

package com.io7m.jcanephora.tests.core;

import com.io7m.jcanephora.core.JCGLCubeMapFaceLH;
import com.io7m.jcanephora.core.JCGLCubeMapFaceRH;
import org.junit.Assert;
import org.junit.Test;

public final class JCGLCubeMapFaceTest
{
  @Test
  public void testConversionsLH_RH()
  {
    for (final JCGLCubeMapFaceLH v : JCGLCubeMapFaceLH.values()) {
      switch (v) {
        case CUBE_MAP_LH_NEGATIVE_X:
          Assert.assertEquals(
            JCGLCubeMapFaceRH.CUBE_MAP_RH_NEGATIVE_X,
            JCGLCubeMapFaceRH.fromLH(v));
          break;
        case CUBE_MAP_LH_NEGATIVE_Y:
          Assert.assertEquals(
            JCGLCubeMapFaceRH.CUBE_MAP_RH_POSITIVE_Y,
            JCGLCubeMapFaceRH.fromLH(v));
          break;
        case CUBE_MAP_LH_NEGATIVE_Z:
          Assert.assertEquals(
            JCGLCubeMapFaceRH.CUBE_MAP_RH_POSITIVE_Z,
            JCGLCubeMapFaceRH.fromLH(v));
          break;
        case CUBE_MAP_LH_POSITIVE_X:
          Assert.assertEquals(
            JCGLCubeMapFaceRH.CUBE_MAP_RH_POSITIVE_X,
            JCGLCubeMapFaceRH.fromLH(v));
          break;
        case CUBE_MAP_LH_POSITIVE_Y:
          Assert.assertEquals(
            JCGLCubeMapFaceRH.CUBE_MAP_RH_NEGATIVE_Y,
            JCGLCubeMapFaceRH.fromLH(v));
          break;
        case CUBE_MAP_LH_POSITIVE_Z:
          Assert.assertEquals(
            JCGLCubeMapFaceRH.CUBE_MAP_RH_NEGATIVE_Z,
            JCGLCubeMapFaceRH.fromLH(v));
          break;
      }
    }
  }

  @Test
  public void testConversionsRH_LH()
  {
    for (final JCGLCubeMapFaceRH v : JCGLCubeMapFaceRH.values()) {
      switch (v) {
        case CUBE_MAP_RH_NEGATIVE_X:
          Assert.assertEquals(
            JCGLCubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_X,
            JCGLCubeMapFaceLH.fromRH(v));
          break;
        case CUBE_MAP_RH_NEGATIVE_Y:
          Assert.assertEquals(
            JCGLCubeMapFaceLH.CUBE_MAP_LH_POSITIVE_Y,
            JCGLCubeMapFaceLH.fromRH(v));
          break;
        case CUBE_MAP_RH_NEGATIVE_Z:
          Assert.assertEquals(
            JCGLCubeMapFaceLH.CUBE_MAP_LH_POSITIVE_Z,
            JCGLCubeMapFaceLH.fromRH(v));
          break;
        case CUBE_MAP_RH_POSITIVE_X:
          Assert.assertEquals(
            JCGLCubeMapFaceLH.CUBE_MAP_LH_POSITIVE_X,
            JCGLCubeMapFaceLH.fromRH(v));
          break;
        case CUBE_MAP_RH_POSITIVE_Y:
          Assert.assertEquals(
            JCGLCubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Y,
            JCGLCubeMapFaceLH.fromRH(v));
          break;
        case CUBE_MAP_RH_POSITIVE_Z:
          Assert.assertEquals(
            JCGLCubeMapFaceLH.CUBE_MAP_LH_NEGATIVE_Z,
            JCGLCubeMapFaceLH.fromRH(v));
          break;
      }
    }
  }
}
