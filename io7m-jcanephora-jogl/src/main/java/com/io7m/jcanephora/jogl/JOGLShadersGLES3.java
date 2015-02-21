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

import java.util.Map;

import javax.media.opengl.GLES3;

import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.api.JCGLShadersGL3Type;
import com.io7m.jcanephora.api.JCGLShadersGLES3Type;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class JOGLShadersGLES3 extends JOGLShadersAbstract implements
  JCGLShadersGLES3Type,
  JCGLShadersGL3Type
{
  JOGLShadersGLES3(
    final GLES3 in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache,
    final JCGLArrayBuffersType in_arrays)
  {
    super(in_gl, in_log, in_icache, in_tcache, in_arrays);
  }

  @Override public ProgramType programCreateWithOutputs(
    final String name,
    final VertexShaderType v,
    final FragmentShaderType f,
    final Map<String, FramebufferDrawBufferType> outputs)
    throws JCGLException,
      JCGLExceptionProgramCompileError
  {
    NullCheck.notNull(outputs);
    return this.programCreateCommon(name, v, f);
  }
}
