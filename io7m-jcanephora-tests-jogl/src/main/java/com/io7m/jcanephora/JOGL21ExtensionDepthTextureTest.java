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
import com.io7m.jaux.functional.Option;
import com.io7m.jaux.functional.Option.Some;
import com.io7m.jcanephora.contracts.gl2.ExtensionDepthTextureContract;

public final class JOGL21ExtensionDepthTextureTest extends
  ExtensionDepthTextureContract
{
  @Override public boolean isGLSupported()
  {
    return JOGLTestContext.isOpenGL21WithExtensionsSupported();
  }

  @Override public @Nonnull TestContext newTestContext()
    throws JCGLRuntimeException,
      JCGLUnsupportedException,
      ConstraintError
  {
    return JOGLTestContext.makeContextWithOpenGL2_1();
  }

  @Override public
    Option<JCGLExtensionDepthTexture>
    getExtensionDepthTexture(
      final TestContext tc)
  {
    final Some<JCGLInterfaceGL2> some =
      (Some<JCGLInterfaceGL2>) tc.getGLImplementation().getGL2();
    return some.value.extensionDepthTexture();
  }
}