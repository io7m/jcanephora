/*
 * Copyright © 2015 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.jogl;

import com.io7m.jcanephora.core.JCGLExceptionUnsupported;
import com.io7m.jcanephora.jogl.JCGLImplementationJOGL;
import com.io7m.jcanephora.jogl.JCGLImplementationJOGLType;
import com.jogamp.common.util.VersionNumber;
import com.jogamp.opengl.GLContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Field;

public final class JOGLImplementationTest
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  @Test public void testTooOld()
    throws Exception
  {
    /**
     * This is an extremely abusive way to override the context version,
     * but unfortunately JOGL doesn't provide anything else and has arranged
     * the types to make it impossible to do this.
     */

    final GLContext c = JOGLTestContexts.newGL33Drawable("main");
    final Field f = GLContext.class.getDeclaredField("ctxVersion");
    f.setAccessible(true);
    f.set(c, new VersionNumber(1, 0, 0));

    final JCGLImplementationJOGLType i = JCGLImplementationJOGL.getInstance();
    this.expected.expect(JCGLExceptionUnsupported.class);
    i.newContextFrom(c, "main");
  }
}
