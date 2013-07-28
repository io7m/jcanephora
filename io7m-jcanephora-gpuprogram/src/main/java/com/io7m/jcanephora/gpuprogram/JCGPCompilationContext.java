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

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.JCGLApi;
import com.io7m.jcanephora.JCGLSLVersionNumber;

/**
 * <p>
 * The context of a compilation in progress.
 * </p>
 * <p>
 * Values of this type can be manipulated by multiple threads without explicit
 * synchronization.
 * </p>
 */

@ThreadSafe public final class JCGPCompilationContext
{
  private final @Nonnull JCGLSLVersionNumber version;
  private final @Nonnull JCGLApi             api;
  private final @Nonnull AtomicBoolean       debugging;

  public JCGPCompilationContext(
    final @Nonnull JCGLSLVersionNumber version,
    final @Nonnull JCGLApi api)
    throws ConstraintError
  {
    this.version = Constraints.constrainNotNull(version, "Version");
    this.api = Constraints.constrainNotNull(api, "API");
    this.debugging = new AtomicBoolean();
  }

  /**
   * Retrieve the OpenGL shading language API for which the compilation is
   * occurring.
   */

  public @Nonnull JCGLApi getApi()
  {
    return this.api;
  }

  /**
   * Retrieve the OpenGL shading language version for which the compilation is
   * occurring.
   */

  public @Nonnull JCGLSLVersionNumber getVersion()
  {
    return this.version;
  }

  /**
   * Return <tt>true</tt> if debugging is enabled.
   */

  public boolean isDebugging()
  {
    return this.debugging.get();
  }

  /**
   * Enable or disable debugging.
   */

  public void setDebugging(
    final boolean debugging)
  {
    this.debugging.set(debugging);
  }
}
