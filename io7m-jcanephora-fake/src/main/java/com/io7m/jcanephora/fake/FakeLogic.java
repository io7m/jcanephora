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

import com.io7m.jcanephora.JCGLExceptionRuntime;
import com.io7m.jcanephora.LogicOperation;
import com.io7m.jcanephora.api.JCGLLogicType;
import com.io7m.jlog.LogType;
import com.io7m.jlog.LogUsableType;
import com.io7m.jnull.NullCheck;

final class FakeLogic implements JCGLLogicType
{
  private final FakeContext context;
  private final LogType     log;
  private boolean           logic;
  private LogicOperation    op;

  FakeLogic(
    final FakeContext in_gl,
    final LogUsableType in_log)
  {
    this.context = NullCheck.notNull(in_gl, "FakeContext");
    this.log = NullCheck.notNull(in_log, "Log").with("logic");
    this.op = LogicOperation.LOGIC_COPY;
  }

  @Override public void logicOperationsDisable()
    throws JCGLExceptionRuntime
  {
    this.logic = false;
  }

  @Override public void logicOperationsEnable(
    final LogicOperation operation)
    throws JCGLExceptionRuntime
  {
    NullCheck.notNull(operation, "Logic operation");

    this.logic = true;
    this.op = operation;
  }

  @Override public boolean logicOperationsEnabled()
    throws JCGLExceptionRuntime
  {
    return this.logic;
  }
}
