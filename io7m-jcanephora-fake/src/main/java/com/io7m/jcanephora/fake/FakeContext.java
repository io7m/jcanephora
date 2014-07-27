/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

import java.util.HashSet;
import java.util.Set;

import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLSLVersion;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLVersion;
import com.io7m.jcanephora.JCGLVersionNumber;
import com.io7m.jnull.NullCheck;
import com.io7m.junreachable.UnimplementedCodeException;

/**
 * A completely imaginary OpenGL context.
 */

@SuppressWarnings("boxing") public final class FakeContext
{
  private static String makeVersion()
  {
    final String v =
      FakeContext.class.getPackage().getImplementationVersion();
    if (v == null) {
      return "Unknown";
    }
    return v;
  }

  /**
   * Construct a new context.
   *
   * @param in_framebuffer
   *          A description of the default framebuffer.
   * @param in_number
   *          The OpenGL version number.
   * @param in_api
   *          The OpenGL API.
   * @param in_glsl_version
   *          The GLSL version.
   * @return A new context.
   */

  public static FakeContext newContext(
    final FakeDefaultFramebuffer in_framebuffer,
    final JCGLVersionNumber in_number,
    final JCGLApi in_api,
    final JCGLSLVersionNumber in_glsl_version)
  {
    return new FakeContext(in_framebuffer, in_number, in_api, in_glsl_version);
  }

  private final FakeDefaultFramebuffer framebuffer;
  private final String                 renderer;
  private final Set<FakeContext>       shared;
  private final JCGLSLVersion          sl_version;
  private final String                 vendor;
  private final JCGLVersion            version;

  private FakeContext(
    final FakeDefaultFramebuffer in_framebuffer,
    final JCGLVersionNumber in_number,
    final JCGLApi in_api,
    final JCGLSLVersionNumber in_glsl_version)
  {
    NullCheck.notNull(in_framebuffer, "Framebuffer");
    NullCheck.notNull(in_number, "Number");
    NullCheck.notNull(in_api, "API");
    NullCheck.notNull(in_glsl_version, "GLSL version");

    final String v = FakeContext.makeVersion();

    this.framebuffer = in_framebuffer;
    this.shared = new HashSet<FakeContext>();
    this.renderer = "Fake on Canephora " + v;
    this.vendor = "io7m.com";

    String sl_text = null;
    String text = null;
    switch (in_api) {
      case JCGL_ES:
      {
        text =
          String.format(
            "OpenGL ES %d.%d Canephora",
            in_number.getVersionMajor(),
            in_number.getVersionMinor());
        sl_text =
          String.format(
            "OpenGL ES GLSL ES %d.%d",
            in_glsl_version.getVersionMajor(),
            in_glsl_version.getVersionMinor());
        break;
      }
      case JCGL_FULL:
      {
        text =
          String.format(
            "%d.%d Canephora",
            in_number.getVersionMajor(),
            in_number.getVersionMinor());
        sl_text =
          String.format(
            "%d.%d",
            in_glsl_version.getVersionMajor(),
            in_glsl_version.getVersionMinor());
        break;
      }
    }
    assert text != null;
    assert sl_text != null;

    this.version = JCGLVersion.make(in_number, in_api, text);
    this.sl_version = JCGLSLVersion.make(in_glsl_version, in_api, sl_text);
  }

  /**
   * @return The set of contexts with which this context is shared.
   */

  public Set<FakeContext> getCreatedShares()
  {
    return this.shared;
  }

  /**
   * @return A description of the default framebuffer.
   */

  public FakeDefaultFramebuffer getDefaultFramebuffer()
  {
    return this.framebuffer;
  }

  /**
   * @return The current error code for the context.
   */

  public int getError()
  {
    // TODO Auto-generated method stub
    throw new UnimplementedCodeException();
  }

  /**
   * @return The name of the renderer.
   */

  public String getRenderer()
  {
    return this.renderer;
  }

  /**
   * @return The GLSL version number.
   */

  public JCGLSLVersion getSLVersion()
  {
    return this.sl_version;
  }

  /**
   * @return The name of the vendor.
   */

  public String getVendor()
  {
    return this.vendor;
  }

  /**
   * @return The OpenGL version number.
   */

  public JCGLVersion getVersion()
  {
    return this.version;
  }

  /**
   * @return <code>true</code> if this context is shared.
   */

  public boolean isShared()
  {
    return this.shared.isEmpty() == false;
  }

  /**
   * Mark this context as shared with the other given context.
   *
   * @param c
   *          The context.
   */

  public void shareWith(
    final FakeContext c)
  {
    NullCheck.notNull(c, "Context");

    this.shared.add(c);
    c.shared.add(this);
  }
}
