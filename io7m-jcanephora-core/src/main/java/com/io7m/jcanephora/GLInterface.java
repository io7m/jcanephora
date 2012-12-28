/*
 * Copyright © 2012 http://io7m.com
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

/**
 * A simplified and type-safe interface to modern programmable-pipeline GPUs.
 * 
 * The <code>GLInterface</code> interface specifies a kind of
 * minimum-compatibility level: Programs that are written against the
 * <code>GLInterface</code> interface have a reasonable assurance that they
 * will work on implementations that support the common subset of OpenGL 3.0
 * (with none of the deprecated features - the "core" profile) and OpenGL 2.1.
 * 
 * This assurance obviously does not extend to shading language programs.
 */

public interface GLInterface extends
  GLArrayBuffersMapped,
  GLBlending,
  GLInterfaceES2,
  GLIndexBuffersMapped,
  GLLogic,
  GLPolygonModes,
  GLPolygonSmoothing,
  GLProgramPointSizeControl,
  GLTextures2DBuffered
{
  /*
   * All functions defined in the superinterfaces.
   */
}
