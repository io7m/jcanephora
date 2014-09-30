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

package com.io7m.jcanephora.jogl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLContext;

import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLSLVersion;
import com.io7m.jcanephora.JCGLSLVersionNumber;
import com.io7m.jcanephora.JCGLVersion;
import com.io7m.jcanephora.JCGLVersionNumber;
import com.io7m.jcanephora.api.JCGLMetaType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.jogamp.common.util.VersionNumber;

final class JOGLMeta implements JCGLMetaType
{
  private final GL                      gl;
  private final LogType                 log;
  private final JOGLLogMessageCacheType tcache;

  JOGLMeta(
    final GL in_gl,
    final JOGLLogMessageCacheType in_tcache,
    final LogUsableType in_log)
  {
    this.gl = NullCheck.notNull(in_gl, "GL");
    this.log = NullCheck.notNull(in_log, "Log").with("meta");
    this.tcache = NullCheck.notNull(in_tcache, "Text cache");
  }

  @Override public int metaGetError()
    throws JCGLExceptionRuntime
  {
    return this.gl.glGetError();
  }

  @Override public String metaGetRenderer()
    throws JCGLExceptionRuntime
  {
    final String x = this.gl.glGetString(GL.GL_RENDERER);
    return x;
  }

  @Override public JCGLSLVersion metaGetSLVersion()
    throws JCGLExceptionRuntime
  {
    final GLContext context = this.gl.getContext();
    final VersionNumber vn = context.getGLSLVersionNumber();
    final String text =
      this.gl.glGetString(GL2ES2.GL_SHADING_LANGUAGE_VERSION);

    if (context.isGLES()) {
      final String vtext = this.gl.glGetString(GL.GL_VERSION);
      if (vtext.contains("Mesa 9.") || vtext.contains("Mesa 10.")) {
        final StringBuilder tc = this.tcache.getTextCache();
        tc.append("quirk: GL_VERSION is '");
        tc.append(vtext);
        tc.append("' which lies about GLSL ES 3.0 support");
        tc.append(" - downgrading GLSL ES version to 1.0.");
        final String r = tc.toString();
        assert r != null;
        this.log.debug(r);
        return JCGLSLVersion.GLSL_ES_100;
      }
    }

    final JCGLSLVersionNumber gvn =
      new JCGLSLVersionNumber(vn.getMajor(), vn.getMinor());
    final JCGLApi api =
      context.isGLES() ? JCGLApi.JCGL_ES : JCGLApi.JCGL_FULL;

    return JCGLSLVersion.make(gvn, api, text);
  }

  @Override public String metaGetVendor()
    throws JCGLExceptionRuntime
  {
    final String x = this.gl.glGetString(GL.GL_VENDOR);
    return x;
  }

  @Override public JCGLVersion metaGetVersion()
    throws JCGLExceptionRuntime
  {
    final GLContext context = this.gl.getContext();
    final VersionNumber vn = context.getGLVersionNumber();
    final String text = context.getGLVersion();
    return JCGLVersion.make(
      new JCGLVersionNumber(vn.getMajor(), vn.getMinor(), vn.getSub()),
      context.isGLES() ? JCGLApi.JCGL_ES : JCGLApi.JCGL_FULL,
      text);
  }
}
