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

package com.io7m.jcanephora.texture.unit_allocator;

import com.io7m.jaffirm.core.Invariants;
import com.io7m.jcanephora.core.JCGLTexture2DType;
import com.io7m.jcanephora.core.JCGLTexture2DUsableType;
import com.io7m.jcanephora.core.JCGLTextureCubeType;
import com.io7m.jcanephora.core.JCGLTextureCubeUsableType;
import com.io7m.jcanephora.core.JCGLTextureFilterMagnification;
import com.io7m.jcanephora.core.JCGLTextureFilterMinification;
import com.io7m.jcanephora.core.JCGLTextureFormat;
import com.io7m.jcanephora.core.JCGLTextureUnitType;
import com.io7m.jcanephora.core.JCGLTextureUsableType;
import com.io7m.jcanephora.core.JCGLTextureWrapR;
import com.io7m.jcanephora.core.JCGLTextureWrapS;
import com.io7m.jcanephora.core.JCGLTextureWrapT;
import com.io7m.jcanephora.core.api.JCGLTexturesType;
import com.io7m.jfunctional.Pair;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;
import com.io7m.junreachable.UnimplementedCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * The default implementation of the {@link JCGLTextureUnitAllocatorType}
 * interface.
 */

public final class JCGLTextureUnitAllocator implements
  JCGLTextureUnitAllocatorType
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(JCGLTextureUnitAllocator.class);
  }

  private final List<JCGLTextureUnitType> units;
  private final Context root;
  private final Deque<Context> contexts_active;
  private final Deque<Context> contexts_free;

  private JCGLTextureUnitAllocator(
    final int stack_depth,
    final List<JCGLTextureUnitType> in_units)
  {
    RangeCheck.checkGreaterInteger(
      stack_depth,
      "Stack depth",
      1,
      "Minimum stack depth");

    this.units = NullCheck.notNullAll(in_units, "Units");
    this.contexts_active = new ArrayDeque<>(stack_depth);
    this.contexts_free = new ArrayDeque<>(stack_depth);
    this.root = new Context();
    this.contexts_active.push(this.root);

    for (int index = 0; index < stack_depth - 1; ++index) {
      this.contexts_free.push(new Context());
    }
  }

  /**
   * Create a new allocator that will allocate a stack of {@code max_depth}
   * contexts. The number of contexts (including the initial root context) will
   * not be allowed to exceed {@code max_depth}).
   *
   * @param max_depth The maximum stack depth
   * @param in_units  The texture units
   *
   * @return A new allocator
   */

  public static JCGLTextureUnitAllocatorType newAllocatorWithStack(
    final int max_depth,
    final List<JCGLTextureUnitType> in_units)
  {
    return new JCGLTextureUnitAllocator(max_depth, in_units);
  }

  @Override
  public JCGLTextureUnitContextParentType rootContext()
  {
    return this.root;
  }

  private Context getCurrent()
  {
    return this.contexts_active.peek();
  }

  private Context contextFresh()
  {
    if (!this.contexts_free.isEmpty()) {
      final Context c = this.contexts_free.pop();
      this.contexts_active.push(c);
      return c;
    }

    throw new JCGLExceptionTextureUnitContextLimitReached(
      "Stack limit reached: " + this.contexts_active.size());
  }

  private void contextPop(
    final JCGLTexturesType g,
    final Context c)
  {
    NullCheck.notNull(g, "Textures");
    NullCheck.notNull(c, "Context");

    final Context c_this = JCGLTextureUnitAllocator.this.contexts_active.pop();

    Invariants.checkInvariant(
      c,
      Objects.equals(c_this, c),
      cc -> "The right context must be popped");

    JCGLTextureUnitAllocator.this.contexts_free.push(c_this);

    final Context c_prev = JCGLTextureUnitAllocator.this.contexts_active.peek();
    c_prev.activate(g);
  }

  private final class Context implements JCGLTextureUnitContextType
  {
    private final JCGLTextureUsableType[] bindings;
    private int next;
    private @Nullable JCGLTexturesType g3;
    private @Nullable JCGLTextureUnitType unit;

    Context()
    {
      this.bindings =
        new JCGLTextureUsableType[JCGLTextureUnitAllocator.this.units.size()];
      this.next = 0;
    }

    private void copyFrom(final Context c)
    {
      this.next = c.next;
      for (int index = 0; index < this.bindings.length; ++index) {
        this.bindings[index] = c.bindings[index];
      }
    }

    @Override
    public JCGLTextureUnitContextType unitContextNew()
    {
      if (!this.isCurrent()) {
        throw new JCGLExceptionTextureUnitContextNotActive(
          "Context not current");
      }

      LOG.trace("new context");
      final Context c = JCGLTextureUnitAllocator.this.contextFresh();
      c.copyFrom(this);
      return c;
    }

    @Override
    public JCGLTextureUnitContextType unitContextNewWithReserved(final int r)
    {
      this.checkTextureUnitsRequired(this.next + r);
      return this.unitContextNew();
    }

    private void checkTextureUnitsRequired(final int required)
    {
      if (required > JCGLTextureUnitAllocator.this.units.size()) {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("Out of texture units.");
        sb.append("Required:  ");
        sb.append(required);
        sb.append("\n");
        sb.append("Available: ");
        sb.append(JCGLTextureUnitAllocator.this.units.size());
        sb.append("\n");
        throw new JCGLExceptionTextureUnitExhausted(sb.toString());
      }
    }

    private boolean isCurrent()
    {
      return Objects.equals(this, JCGLTextureUnitAllocator.this.getCurrent());
    }

    private void activate(final JCGLTexturesType g)
    {
      NullCheck.notNull(g);

      LOG.trace("make current");

      final List<JCGLTextureUnitType> us = JCGLTextureUnitAllocator.this.units;
      this.g3 = g;
      for (int index = 0; index < this.bindings.length; ++index) {
        this.unit = us.get(index);
        final JCGLTextureUsableType t = this.bindings[index];
        if (t == null) {
          if (LOG.isTraceEnabled()) {
            LOG.trace(
              "[{}]: set unbound",
              Integer.valueOf(this.unit.index()));
          }
          g.textureUnitUnbind(this.unit);
        } else {
          if (t.isDeleted()) {
            if (LOG.isTraceEnabled()) {
              LOG.trace(
                "[{}]: set unbound (deleted)",
                Integer.valueOf(this.unit.index()));
            }
            g.textureUnitUnbind(this.unit);
            this.bindings[index] = null;
          } else {
            if (t instanceof JCGLTexture2DUsableType) {
              this.g3.texture2DBind(this.unit, (JCGLTexture2DUsableType) t);
            } else if (t instanceof JCGLTextureCubeUsableType) {
              this.g3.textureCubeBind(this.unit, (JCGLTextureCubeUsableType) t);
            } else {
              throw new UnimplementedCodeException();
            }
          }
        }
        this.unit = null;
      }
      this.g3 = null;
    }

    @Override
    public JCGLTextureUnitType unitContextBindTexture2D(
      final JCGLTexturesType g,
      final JCGLTexture2DUsableType t)
    {
      NullCheck.notNull(g);
      NullCheck.notNull(t);

      if (!this.isCurrent()) {
        throw new JCGLExceptionTextureUnitContextNotActive(
          "Context not current");
      }

      this.checkTextureUnitsRequired(this.next + 1);

      if (LOG.isTraceEnabled()) {
        LOG.trace("bind {}", t);
      }

      final List<JCGLTextureUnitType> us = JCGLTextureUnitAllocator.this.units;
      final JCGLTextureUnitType u = us.get(this.next);
      g.texture2DBind(u, t);
      this.bindings[this.next] = t;
      ++this.next;
      return u;
    }

    @Override
    public JCGLTextureUnitType unitContextBindTextureCube(
      final JCGLTexturesType g,
      final JCGLTextureCubeUsableType t)
    {
      NullCheck.notNull(g, "Textures");
      NullCheck.notNull(t, "Texture");

      if (!this.isCurrent()) {
        throw new JCGLExceptionTextureUnitContextNotActive(
          "Context not current");
      }

      this.checkTextureUnitsRequired(this.next + 1);

      if (LOG.isTraceEnabled()) {
        LOG.trace("bind {}", t);
      }

      final List<JCGLTextureUnitType> us = JCGLTextureUnitAllocator.this.units;
      final JCGLTextureUnitType u = us.get(this.next);
      g.textureCubeBind(u, t);
      this.bindings[this.next] = t;
      ++this.next;
      return u;
    }

    @Override
    public Pair<JCGLTextureUnitType, JCGLTexture2DType>
    unitContextAllocateTexture2D(
      final JCGLTexturesType g,
      final long width,
      final long height,
      final JCGLTextureFormat format,
      final JCGLTextureWrapS wrap_s,
      final JCGLTextureWrapT wrap_t,
      final JCGLTextureFilterMinification min_filter,
      final JCGLTextureFilterMagnification mag_filter)
    {
      NullCheck.notNull(g, "Textures");
      NullCheck.notNull(format, "Format");
      NullCheck.notNull(wrap_s, "Wrapping S mode");
      NullCheck.notNull(wrap_t, "Wrapping T mode");
      NullCheck.notNull(min_filter, "Magnification filter");
      NullCheck.notNull(mag_filter, "Minification filter");

      if (!this.isCurrent()) {
        throw new JCGLExceptionTextureUnitContextNotActive(
          "Context not current");
      }

      this.checkTextureUnitsRequired(this.next + 1);

      if (LOG.isTraceEnabled()) {
        LOG.trace("allocate 2d");
      }

      final List<JCGLTextureUnitType> us = JCGLTextureUnitAllocator.this.units;
      final JCGLTextureUnitType u = us.get(this.next);
      final JCGLTexture2DType t = g.texture2DAllocate(
        u, width, height, format, wrap_s, wrap_t, min_filter, mag_filter);

      g.texture2DBind(u, t);
      this.bindings[this.next] = t;
      ++this.next;
      return Pair.pair(u, t);
    }

    @Override
    public Pair<JCGLTextureUnitType, JCGLTextureCubeType>
    unitContextAllocateTextureCube(
      final JCGLTexturesType g,
      final long size,
      final JCGLTextureFormat format,
      final JCGLTextureWrapR wrap_r,
      final JCGLTextureWrapS wrap_s,
      final JCGLTextureWrapT wrap_t,
      final JCGLTextureFilterMinification min_filter,
      final JCGLTextureFilterMagnification mag_filter)
    {
      NullCheck.notNull(g, "Textures");
      NullCheck.notNull(format, "Format");
      NullCheck.notNull(wrap_r, "Wrapping R mode");
      NullCheck.notNull(wrap_s, "Wrapping S mode");
      NullCheck.notNull(wrap_t, "Wrapping T mode");
      NullCheck.notNull(min_filter, "Magnification filter");
      NullCheck.notNull(mag_filter, "Minification filter");

      if (!this.isCurrent()) {
        throw new JCGLExceptionTextureUnitContextNotActive(
          "Context not current");
      }

      this.checkTextureUnitsRequired(this.next + 1);

      if (LOG.isTraceEnabled()) {
        LOG.trace("allocate cube");
      }

      final List<JCGLTextureUnitType> us = JCGLTextureUnitAllocator.this.units;
      final JCGLTextureUnitType u = us.get(this.next);
      final JCGLTextureCubeType t = g.textureCubeAllocate(
        u, size, format, wrap_r, wrap_s, wrap_t, min_filter, mag_filter);

      g.textureCubeBind(u, t);
      this.bindings[this.next] = t;
      ++this.next;
      return Pair.pair(u, t);
    }


    @Override
    public void unitContextFinish(final JCGLTexturesType g)
    {
      if (!this.isCurrent()) {
        throw new JCGLExceptionTextureUnitContextNotActive(
          "Context not current");
      }

      LOG.trace("finish");
      JCGLTextureUnitAllocator.this.contextPop(g, this);
    }
  }
}
