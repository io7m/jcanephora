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

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;

import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.FramebufferDrawBufferType;
import com.io7m.jcanephora.JCGLExceptionDeleted;
import com.io7m.jcanephora.JCGLExceptionProgramCompileError;
import com.io7m.jcanephora.JCGLExceptionProgramOutputMappingsError;
import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.JCGLExceptionWrongContext;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.api.JCGLShadersGL3Type;
import com.io7m.jcanephora.api.JCGLShadersGLES3Type;
import com.io7m.jlog.LogLevel;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;
import com.jogamp.common.nio.Buffers;

final class JOGLShadersGL2GL3 extends JOGLShadersAbstract implements
  JCGLShadersGL3Type,
  JCGLShadersGLES3Type
{
  private static void checkOutputs(
    final GLContext ctx,
    final String name,
    final Map<String, FramebufferDrawBufferType> outputs,
    final List<FramebufferDrawBufferType> available_buffers)
    throws JCGLExceptionWrongContext,
      JCGLExceptionProgramOutputMappingsError
  {
    if (outputs.isEmpty()) {
      final String s =
        String.format("Program %s: Draw buffer mappings are empty", name);
      assert s != null;
      throw new JCGLExceptionProgramOutputMappingsError(s);
    }

    if (outputs.size() > available_buffers.size()) {
      @SuppressWarnings("boxing") final String s =
        String
          .format(
            "Program %s: More output mappings (%d) were provided than there are draw buffers available (%d)",
            name,
            outputs.size(),
            available_buffers.size());
      assert s != null;
      throw new JCGLExceptionProgramOutputMappingsError(s);
    }

    for (final String output_name : outputs.keySet()) {
      NullCheck.notNull(output_name, "Output name");
      final FramebufferDrawBufferType dt = outputs.get(output_name);
      JOGLDrawBuffers.checkDrawBuffer(ctx, dt);
    }
  }

  private final JOGLDrawBuffersType buffers;
  private final GL2GL3              g2;

  JOGLShadersGL2GL3(
    final GL2GL3 in_gl,
    final LogUsableType in_log,
    final JOGLIntegerCacheType in_icache,
    final JOGLLogMessageCacheType in_tcache,
    final JCGLArrayBuffersType in_arrays,
    final JOGLDrawBuffersType in_buffers)
  {
    super(in_gl, in_log, in_icache, in_tcache, in_arrays);
    this.g2 = in_gl;
    this.buffers = NullCheck.notNull(in_buffers, "Buffers");
  }

  private void bindOutputs(
    final Map<String, FramebufferDrawBufferType> outputs,
    final LogType logx,
    final StringBuilder text,
    final int id)
    throws JCGLExceptionRuntime
  {
    for (final Entry<String, FramebufferDrawBufferType> e : outputs
      .entrySet()) {
      final String output = e.getKey();
      final FramebufferDrawBufferType buffer = e.getValue();
      this.g2.glBindFragDataLocation(id, buffer.drawBufferGetIndex(), output);

      if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
        text.setLength(0);
        text.append("bound output '");
        text.append(output);
        text.append("' to draw buffer ");
        text.append(buffer);
        final String r = text.toString();
        assert r != null;
        logx.debug(r);
      }
    }
  }

  @Override public ProgramType programCreateWithOutputs(
    final String name,
    final VertexShaderType v,
    final FragmentShaderType f,
    final Map<String, FramebufferDrawBufferType> outputs)
    throws JCGLExceptionRuntime,
      JCGLExceptionProgramCompileError,
      JCGLExceptionWrongContext,
      JCGLExceptionDeleted,
      JCGLExceptionProgramOutputMappingsError
  {
    NullCheck.notNull(name, "Program name");
    NullCheck.notNull(outputs, "Outputs");

    JOGLShadersAbstract.checkVertexShader(this.g2, v);
    JOGLShadersAbstract.checkFragmentShader(this.g2, f);

    final List<FramebufferDrawBufferType> available_buffers =
      this.buffers.getDrawBuffers();

    final GLContext ctx = this.g2.getContext();
    assert ctx != null;
    final StringBuilder text = this.getTcache().getTextCache();
    final LogType logx = this.getLog();

    JOGLShadersGL2GL3.checkOutputs(ctx, name, outputs, available_buffers);

    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("create \"");
      text.append(name);
      text.append("\" with ");
      text.append(v);
      text.append(" ");
      text.append(f);
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    final int id = this.g2.glCreateProgram();
    if (id == 0) {
      throw new JCGLExceptionRuntime(0, "glCreateProgram failed");
    }

    this.g2.glAttachShader(id, v.getGLName());
    this.g2.glAttachShader(id, f.getGLName());
    this.bindOutputs(outputs, logx, text, id);
    this.g2.glLinkProgram(id);

    final JOGLIntegerCacheType icache = this.getIcache();
    final int status =
      icache.getProgramInteger(this.g2, id, GL2ES2.GL_LINK_STATUS);

    if (status == 0) {
      final ByteBuffer buffer = Buffers.newDirectByteBuffer(8192);
      final IntBuffer buffer_length = Buffers.newDirectIntBuffer(1);
      this.g2.glGetProgramInfoLog(id, 8192, buffer_length, buffer);

      final byte[] raw = new byte[buffer.remaining()];
      buffer.get(raw);
      final String tt = new String(raw);
      throw new JCGLExceptionProgramCompileError(name, tt);
    }

    if (logx.wouldLog(LogLevel.LOG_DEBUG)) {
      text.setLength(0);
      text.append("created ");
      text.append(id);
      final String r = text.toString();
      assert r != null;
      logx.debug(r);
    }

    final Map<String, ProgramAttributeType> attributes =
      new HashMap<String, ProgramAttributeType>();
    final Map<String, ProgramUniformType> uniforms =
      new HashMap<String, ProgramUniformType>();

    final JOGLProgram program =
      new JOGLProgram(ctx, id, name, uniforms, attributes);

    this.programGetAttributes(program, attributes);
    this.programGetUniforms(program, uniforms);

    return program;
  }
}
