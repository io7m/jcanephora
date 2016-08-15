/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.lwjgl3;

import com.io7m.jaffirm.core.Postconditions;
import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLExceptionUnsupported;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.lwjgl3.JCGLImplementationLWJGL3;
import com.io7m.jcanephora.lwjgl3.JCGLImplementationLWJGL3Type;
import com.io7m.jcanephora.tests.contracts.JCGLSharedContextPair;
import com.io7m.jfunctional.Pair;
import com.io7m.junreachable.UnreachableCodeException;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class LWJGL3TestContexts
{
  private static final JCGLImplementationLWJGL3Type IMPLEMENTATION;
  private static final Map<String, Long> CACHED_CONTEXTS;
  private static final Logger LOG;

  static {
    IMPLEMENTATION = JCGLImplementationLWJGL3.getInstance();
    CACHED_CONTEXTS = new HashMap<>(32);
    LOG = LoggerFactory.getLogger(LWJGL3TestContexts.class);

    GLFWErrorCallback.createPrint(System.err).set();

    if (!GLFW.glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }
  }

  private LWJGL3TestContexts()
  {

  }

  public static JCGLContextType newGL33Context(
    final String name,
    final int depth_bits,
    final int stencil_bits)
  {
    try {
      return LWJGL3TestContexts.IMPLEMENTATION.newUnsharedContextFrom(
        LWJGL3TestContexts.newContext(name, depth_bits, stencil_bits), name);
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }

  private static long newContext(
    final String name,
    final int depth_bits,
    final int stencil_bits)
  {
    LWJGL3TestContexts.LOG.debug(
      "creating drawable {} (depth {}) (stencil {})",
      name,
      Integer.valueOf(depth_bits),
      Integer.valueOf(stencil_bits));
    LWJGL3TestContexts.destroyCachedDrawableAndRemove(name);

    GLFW.glfwDefaultWindowHints();
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
    GLFW.glfwWindowHint(
      GLFW.GLFW_OPENGL_PROFILE,
      GLFW.GLFW_OPENGL_CORE_PROFILE);

    GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, 8);
    GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, 8);
    GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, 8);
    GLFW.glfwWindowHint(GLFW.GLFW_ALPHA_BITS, 0);
    GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, depth_bits);
    GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, stencil_bits);

    final long context = GLFW.glfwCreateWindow(
      640, 480, "LWJGL3", MemoryUtil.NULL, MemoryUtil.NULL);
    GLFW.glfwMakeContextCurrent(context);
    GL.createCapabilities();
    GLFW.glfwSwapBuffers(context);

    LWJGL3TestContexts.CACHED_CONTEXTS.put(name, Long.valueOf(context));
    return context;
  }

  public static void closeAllContexts()
  {
    final int size = LWJGL3TestContexts.CACHED_CONTEXTS.size();
    LWJGL3TestContexts.LOG.debug(
      "cleaning up {} contexts", Integer.valueOf(size));

    {
      final Iterator<String> iter =
        LWJGL3TestContexts.CACHED_CONTEXTS.keySet().iterator();

      while (iter.hasNext()) {
        final String name = iter.next();
        LWJGL3TestContexts.LOG.debug("releasing drawable {}", name);

        Postconditions.checkPostcondition(
          LWJGL3TestContexts.CACHED_CONTEXTS,
          LWJGL3TestContexts.CACHED_CONTEXTS.containsKey(name),
          ignored -> "Cached contexts must contain " + name);

        final Long drawable = LWJGL3TestContexts.CACHED_CONTEXTS.get(name);
        LWJGL3TestContexts.releaseDrawable(drawable);
      }
    }

    {
      final Iterator<String> iter =
        LWJGL3TestContexts.CACHED_CONTEXTS.keySet().iterator();

      while (iter.hasNext()) {
        final String name = iter.next();
        LWJGL3TestContexts.LOG.debug("destroying drawable {}", name);

        Postconditions.checkPostcondition(
          LWJGL3TestContexts.CACHED_CONTEXTS,
          LWJGL3TestContexts.CACHED_CONTEXTS.containsKey(name),
          ignored -> "Cached contexts must contain " + name);

        final Long drawable = LWJGL3TestContexts.CACHED_CONTEXTS.get(name);

        LWJGL3TestContexts.destroyDrawable(drawable);
        iter.remove();
      }
    }

    LWJGL3TestContexts.LOG.debug(
      "cleaned up {} contexts",
      Integer.valueOf(size));
  }

  private static void destroyCachedDrawableAndRemove(final String name)
  {
    if (LWJGL3TestContexts.CACHED_CONTEXTS.containsKey(name)) {
      LWJGL3TestContexts.LOG.debug("destroying existing context {}", name);
      final Long cached = LWJGL3TestContexts.CACHED_CONTEXTS.get(name);
      LWJGL3TestContexts.destroyDrawable(cached);
      LWJGL3TestContexts.CACHED_CONTEXTS.remove(name);
    }
  }

  private static void releaseDrawable(final Long context)
  {
    final long context_x = context.longValue();
    if (context_x != 0L && GLFW.glfwGetCurrentContext() == context_x) {
      GLFW.glfwMakeContextCurrent(0L);
    }
  }

  private static void destroyDrawable(final Long context)
  {
    LWJGL3TestContexts.releaseDrawable(context);
    GLFW.glfwDestroyWindow(context.longValue());
  }

  public static JCGLSharedContextPair<JCGLContextType> newGL33ContextSharedWith(
    final String name,
    final String shared)
  {
    try {
      LWJGL3TestContexts.LOG.debug(
        "creating context {} shared with {}", name, shared);

      LWJGL3TestContexts.destroyCachedDrawableAndRemove(name);
      LWJGL3TestContexts.destroyCachedDrawableAndRemove(shared);

      GLFW.glfwDefaultWindowHints();
      GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
      GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
      GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
      GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
      GLFW.glfwWindowHint(
        GLFW.GLFW_OPENGL_PROFILE,
        GLFW.GLFW_OPENGL_CORE_PROFILE);

      GLFW.glfwWindowHint(GLFW.GLFW_RED_BITS, 8);
      GLFW.glfwWindowHint(GLFW.GLFW_GREEN_BITS, 8);
      GLFW.glfwWindowHint(GLFW.GLFW_BLUE_BITS, 8);
      GLFW.glfwWindowHint(GLFW.GLFW_ALPHA_BITS, 0);
      GLFW.glfwWindowHint(GLFW.GLFW_DEPTH_BITS, 24);
      GLFW.glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 8);

      final long master = GLFW.glfwCreateWindow(
        640, 480, "LWJGL3", MemoryUtil.NULL, MemoryUtil.NULL);
      final long slave = GLFW.glfwCreateWindow(
        640, 480, "LWJGL3", MemoryUtil.NULL, master);

      GLFW.glfwMakeContextCurrent(master);
      GL.createCapabilities();
      GLFW.glfwSwapBuffers(master);

      GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);

      GLFW.glfwMakeContextCurrent(slave);
      GL.createCapabilities();
      GLFW.glfwSwapBuffers(slave);

      GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
      GLFW.glfwMakeContextCurrent(master);

      final Pair<JCGLContextType, JCGLContextType> pair =
        LWJGL3TestContexts.IMPLEMENTATION.newSharedContextsFrom(
          master, name, slave, shared);

      final JCGLContextType master_ctx = pair.getLeft();
      final JCGLContextType slave_ctx = pair.getRight();

      Preconditions.checkPrecondition(
        master_ctx.contextGetShares(),
        master_ctx.contextGetShares().contains(slave_ctx),
        ignored -> "Master context must contain slave context");

      Preconditions.checkPrecondition(
        slave_ctx.contextGetShares(),
        slave_ctx.contextGetShares().contains(master_ctx),
        ignored -> "Slave context must contain main context");

      Preconditions.checkPrecondition(
        LWJGL3TestContexts.CACHED_CONTEXTS,
        !LWJGL3TestContexts.CACHED_CONTEXTS.containsKey(name),
        ignored -> "Cached contexts must not contain " + name);

      Preconditions.checkPrecondition(
        LWJGL3TestContexts.CACHED_CONTEXTS,
        !LWJGL3TestContexts.CACHED_CONTEXTS.containsKey(shared),
        ignored -> "Cached contexts must not contain " + shared);

      LWJGL3TestContexts.CACHED_CONTEXTS.put(name, Long.valueOf(master));
      LWJGL3TestContexts.CACHED_CONTEXTS.put(shared, Long.valueOf(slave));

      return new JCGLSharedContextPair<>(
        master_ctx, master_ctx, slave_ctx, slave_ctx);
    } catch (final JCGLExceptionUnsupported | JCGLExceptionNonCompliant x) {
      throw new UnreachableCodeException(x);
    }
  }
}
