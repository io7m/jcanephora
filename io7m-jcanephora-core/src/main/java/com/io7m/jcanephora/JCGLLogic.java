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

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified and type-safe interface to OpenGL logic operations.
 */

public interface JCGLLogic
{
  /**
   * Disable logical operations.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void logicOperationsDisable()
    throws JCGLRuntimeException;

  /**
   * Enable logical operations on the framebuffer to be applied between the
   * incoming RGBA colour and the RGBA colour at the corresponding location in
   * the framebuffer.
   * 
   * @param operation
   *          The logical operation to use.
   * @throws ConstraintError
   *           Iff <code>operation == null</code>.
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  void logicOperationsEnable(
    final @Nonnull LogicOperation operation)
    throws ConstraintError,
      JCGLRuntimeException;

  /**
   * Return <code>true</code> iff logical operations are enabled.
   * 
   * @throws JCGLRuntimeException
   *           Iff an OpenGL error occurs.
   */

  boolean logicOperationsEnabled()
    throws JCGLRuntimeException;
}