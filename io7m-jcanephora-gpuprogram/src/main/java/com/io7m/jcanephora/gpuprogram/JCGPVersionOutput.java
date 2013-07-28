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

import javax.annotation.Nonnull;

import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLUnsupportedException;

public final class JCGPVersionOutput
{
  public static @Nonnull String toGLSL(
    final @Nonnull JCGLSLVersionNumber version,
    final @Nonnull JCGLApi api)
    throws JCGLUnsupportedException
  {
    switch (api) {
      case JCGL_ES:
      {
        if (version.getVersionMajor() == 3) {
          return "#version 300";
        }
        if (version.getVersionMajor() == 1) {
          return "#version 100";
        }
        throw new JCGLUnsupportedException(
          "Unrecognized shading language version " + version);
      }
      case JCGL_FULL:
      {
        if (version.getVersionMajor() == 1) {
          if (version.getVersionMinor() == 10) {
            return "#version 110";
          }
          if (version.getVersionMinor() == 20) {
            return "#version 120";
          }
          if (version.getVersionMinor() == 30) {
            return "#version 130";
          }
          if (version.getVersionMinor() == 40) {
            return "#version 140";
          }
          if (version.getVersionMinor() == 50) {
            return "#version 150";
          }
        }
        if (version.getVersionMajor() == 3) {
          if (version.getVersionMinor() == 30) {
            return "#version 330";
          }
        }
        if (version.getVersionMajor() == 4) {
          if (version.getVersionMinor() == 0) {
            return "#version 400";
          }
          if (version.getVersionMinor() == 10) {
            return "#version 410";
          }
          if (version.getVersionMinor() == 20) {
            return "#version 420";
          }
          if (version.getVersionMinor() == 30) {
            return "#version 430";
          }
          if (version.getVersionMinor() == 40) {
            return "#version 440";
          }
        }

        throw new JCGLUnsupportedException(
          "Unrecognized shading language version " + version);
      }
    }

    throw new UnreachableCodeException();
  }

}
