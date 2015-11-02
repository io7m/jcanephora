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

package com.io7m.jcanephora.jogl;

import com.io7m.jcanephora.core.JCGLArrayBufferUsableType;
import com.io7m.jcanephora.core.JCGLArrayObjectBuilderType;
import com.io7m.jcanephora.core.JCGLArrayObjectType;
import com.io7m.jcanephora.core.JCGLArrayObjectUsableType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeFloatingPointType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeIntegralType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeMatcherType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLExceptionObjectNotDeletable;
import com.io7m.jcanephora.core.JCGLIndexBufferUsableType;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLScalarIntegralType;
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveI;
import com.io7m.jranges.Ranges;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Optional;

final class JOGLArrayObjects implements JCGLArrayObjectsType
{
  private static final RangeInclusiveI MAX_ATTRIBS_RANGE;
  private static final Logger          LOG;
  private static final RangeInclusiveI VALID_ELEMENT_COUNT;

  static {
    MAX_ATTRIBS_RANGE = new RangeInclusiveI(16, Integer.MAX_VALUE);
    VALID_ELEMENT_COUNT = new RangeInclusiveI(1, 4);
    LOG = LoggerFactory.getLogger(JOGLArrayObjects.class);
  }

  private final JOGLContext               context;
  private final IntBuffer                 int_cache;
  private final GL3                       gl;
  private final int                       max_attribs;
  private final RangeInclusiveI           valid_attribs;
  private final JOGLArrayBuffers          array_buffers;
  private final JOGLArrayObject           default_buffer;
  private final JOGLIndexBuffers          index_buffers;
  private       JCGLArrayObjectUsableType bind;

  JOGLArrayObjects(
    final JOGLContext c,
    final JOGLArrayBuffers ga,
    final JOGLIndexBuffers gi)
    throws JCGLExceptionNonCompliant
  {
    this.context = NullCheck.notNull(c);
    this.array_buffers = NullCheck.notNull(ga);
    this.index_buffers = NullCheck.notNull(gi);
    this.index_buffers.setArrayObjects(this);

    this.gl = c.getGL3();
    this.int_cache = Buffers.newDirectIntBuffer(1);

    this.gl.glGetIntegerv(GL3.GL_MAX_VERTEX_ATTRIBS, this.int_cache);
    final int max = this.int_cache.get(0);

    JOGLArrayObjects.LOG.debug(
      "reported maximum supported vertex attributes: {}", Integer.valueOf(max));

    if (max < 16) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Non-compliant OpenGL implementation.");
      sb.append("  Required minimum supported vertex attributes: 16");
      sb.append(System.lineSeparator());
      sb.append("  Implementation supports (GL_MAX_VERTEX_ATTRIBS): ");
      sb.append(max);
      throw new JCGLExceptionNonCompliant(sb.toString());
    }

    // Paranoia: Clamp unreasonably large values
    this.max_attribs = Math.min(max, 1024);
    this.valid_attribs = new RangeInclusiveI(0, this.max_attribs - 1);

    final GLContext gc = this.context.getContext();
    this.default_buffer = new JOGLArrayObject(
      gc, gc.getDefaultVAO(), new JCGLArrayVertexAttributeType[0]);
    this.bind = this.default_buffer;
  }

  @Override public JCGLArrayObjectBuilderType arrayObjectNewBuilder()
    throws JCGLException
  {
    return new Builder();
  }

  @Override public JCGLArrayObjectType arrayObjectAllocate(
    final JCGLArrayObjectBuilderType b)
    throws JCGLException
  {
    final GL3 g3 = this.gl;
    JOGLCompatibilityChecks.checkArrayObjectBuilder(g3.getContext(), b);

    Assertive.ensure(b instanceof Builder);
    final Builder bb = (Builder) b;

    final GLContext c = g3.getContext();
    final int max = b.getMaximumVertexAttributes();
    for (int index = 0; index < max; ++index) {
      if (bb.attribs[index] != null) {
        JOGLCompatibilityChecks.checkArrayAttribute(c, bb.attribs[index]);
      }
    }

    final JOGLIndexBuffer ib;
    if (bb.index_buffer != null) {
      ib = this.checkIndexBuffer(bb.index_buffer);
    } else {
      ib = null;
    }

    g3.glGenVertexArrays(1, this.int_cache);
    final Integer aid = Integer.valueOf(this.int_cache.get(0));
    JOGLArrayObjects.LOG.debug("allocated {}", aid);

    final JCGLArrayVertexAttributeType[] write_attribs =
      Arrays.copyOf(bb.attribs, bb.attribs.length);
    final JOGLArrayObject new_ao = new JOGLArrayObject(
      this.context.getContext(), aid.intValue(), write_attribs);

    this.actualBind(new_ao);

    try {
      if (ib != null) {
        this.index_buffers.indexBufferBind(ib);
      }

      for (int index = 0; index < max; ++index) {
        final Integer box_index = Integer.valueOf(index);
        final JCGLArrayVertexAttributeType a = bb.attribs[index];
        if (a == null) {
          JOGLArrayObjects.LOG.trace("[{}]: attr {} disabled", aid, box_index);
          g3.glDisableVertexAttribArray(index);
          continue;
        }

        g3.glEnableVertexAttribArray(index);
        this.array_buffers.arrayBufferBind(a.getArrayBuffer());
        a.matchVertexAttribute(
          new JCGLArrayVertexAttributeMatcherType<Void, JCGLException>()
          {
            @Override public Void matchFloatingPoint(
              final JCGLArrayVertexAttributeFloatingPointType af)
              throws JCGLException
            {
              final int e = af.getElements();
              final boolean n = af.isNormalized();
              final long off = af.getOffset();
              final int stride = af.getStride();
              final JCGLScalarType t = af.getType();

              JOGLArrayObjects.LOG.trace(
                "[{}]: attr {} floating type:{}/{} norm:{} off:{} stride:{}",
                aid,
                box_index,
                t,
                Integer.valueOf(e),
                Boolean.valueOf(n),
                Long.valueOf(off),
                Integer.valueOf(stride));

              g3.glVertexAttribPointer(
                box_index.intValue(),
                e,
                JOGLTypeConversions.scalarTypeToGL(t),
                n,
                stride,
                off);
              return null;
            }

            @Override public Void matchIntegral(
              final JCGLArrayVertexAttributeIntegralType ai)
              throws JCGLException
            {
              final JCGLScalarIntegralType t = ai.getType();
              final int e = ai.getElements();
              final long offset = ai.getOffset();
              final int stride = ai.getStride();

              JOGLArrayObjects.LOG.trace(
                "[{}]: attr {} integral type:{}/{} off:{} stride:{}",
                aid,
                box_index,
                t,
                Integer.valueOf(e),
                Long.valueOf(offset),
                Integer.valueOf(stride));

              g3.glVertexAttribIPointer(
                box_index.intValue(),
                e,
                JOGLTypeConversions.scalarIntegralTypeToGL(t),
                stride,
                offset);
              return null;
            }
          });
      }

      this.array_buffers.arrayBufferUnbind();
    } catch (final GLException | JCGLException e) {
      this.actualUnbind();
      throw e;
    }

    return new_ao;
  }

  private void actualBind(final JOGLArrayObject a)
  {
    JOGLArrayObjects.LOG.trace("bind {} → {}", this.bind, a);
    if (this.bind.getGLName() != a.getGLName()) {
      this.gl.glBindVertexArray(a.getGLName());
      this.bind = a;
    }
  }

  private void actualUnbind()
  {
    JOGLArrayObjects.LOG.trace(
      "unbind {} → {}", this.bind, this.default_buffer);
    if (this.bind.getGLName() != this.default_buffer.getGLName()) {
      this.gl.glBindVertexArray(0);
      this.bind = this.default_buffer;
    }
  }

  @Override public JCGLArrayObjectUsableType arrayObjectGetCurrentlyBound()
    throws JCGLException
  {
    return this.bind;
  }

  @Override public void arrayObjectBind(
    final JCGLArrayObjectUsableType a)
    throws JCGLException, JCGLExceptionDeleted
  {
    this.actualBind(this.checkArrayObject(a));
  }

  private JOGLArrayObject checkArrayObject(final JCGLArrayObjectUsableType a)
  {
    NullCheck.notNull(a);
    JOGLCompatibilityChecks.checkArrayObject(this.context.getContext(), a);
    JCGLResources.checkNotDeleted(a);
    return (JOGLArrayObject) a;
  }

  @Override public void arrayObjectUnbind()
    throws JCGLException
  {
    this.actualUnbind();
  }

  @Override public void arrayObjectDelete(
    final JCGLArrayObjectType ai)
    throws JCGLException, JCGLExceptionDeleted
  {
    final JOGLArrayObject a = this.checkArrayObject(ai);

    if (this.default_buffer.equals(a)) {
      throw new JCGLExceptionObjectNotDeletable(
        "Cannot delete the default array object");
    }

    JOGLArrayObjects.LOG.debug("delete {}", Integer.valueOf(a.getGLName()));

    this.int_cache.rewind();
    this.int_cache.put(0, a.getGLName());
    this.gl.glDeleteVertexArrays(1, this.int_cache);
    a.setDeleted();

    final JOGLReferenceContainer rc = a.getReferenceContainer();
    for (final JCGLReferableType r : a.getReferences()) {
      rc.referenceRemove((JOGLReferable) r);
    }

    if (this.bind.getGLName() == a.getGLName()) {
      this.actualUnbind();
    }
  }

  @Override public JCGLArrayObjectUsableType arrayObjectGetDefault()
  {
    return this.default_buffer;
  }

  private void checkArrayBuffer(final JCGLArrayBufferUsableType a)
  {
    JOGLCompatibilityChecks.checkArray(this.gl.getContext(), a);
    JCGLResources.checkNotDeleted(a);
  }

  private JOGLIndexBuffer checkIndexBuffer(final JCGLIndexBufferUsableType i)
  {
    NullCheck.notNull(i);
    JOGLCompatibilityChecks.checkIndexBuffer(this.gl.getContext(), i);
    JCGLResources.checkNotDeleted(i);
    return (JOGLIndexBuffer) i;
  }

  private final class Builder extends JOGLObjectPseudoUnshared
    implements JCGLArrayObjectBuilderType
  {
    private final     JCGLArrayVertexAttributeType[] attribs;
    private @Nullable JCGLIndexBufferUsableType      index_buffer;

    Builder()
    {
      super(JOGLArrayObjects.this.context.getContext());
      this.attribs =
        new JCGLArrayVertexAttributeType[this.getMaximumVertexAttributes()];
    }

    @Override public Optional<JCGLArrayVertexAttributeType> getAttributeAt(
      final int index)
    {
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index_buffer",
        JOGLArrayObjects.this.valid_attribs,
        "Valid attribute indices");

      return Optional.ofNullable(this.attribs[index]);
    }

    @Override public int getMaximumVertexAttributes()
    {
      return JOGLArrayObjects.this.max_attribs;
    }

    @Override public void setAttributeFloatingPoint(
      final int index,
      final JCGLArrayBufferUsableType a,
      final int elements,
      final JCGLScalarType type,
      final int stride,
      final long offset,
      final boolean normalized)
    {
      JOGLArrayObjects.this.checkArrayBuffer(a);
      NullCheck.notNull(type);
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index_buffer",
        JOGLArrayObjects.this.valid_attribs,
        "Valid attribute indices");
      RangeCheck.checkIncludedInInteger(
        elements,
        "Element count",
        JOGLArrayObjects.VALID_ELEMENT_COUNT,
        "Valid element counts");
      RangeCheck.checkIncludedInInteger(
        stride, "Stride", Ranges.NATURAL_INTEGER, "Valid strides");
      RangeCheck.checkIncludedInLong(
        offset, "Offset", a.getRange(), "Buffer range");

      this.clearRanges(index, index);
      final JOGLArrayVertexAttributeFloating attr =
        new JOGLArrayVertexAttributeFloating(
          JOGLArrayObjects.this.context.getContext(),
          index,
          a,
          type,
          elements,
          stride,
          offset,
          normalized);

      this.attribs[index] = attr;
    }

    @Override public void setAttributeIntegral(
      final int index,
      final JCGLArrayBufferUsableType a,
      final int elements,
      final JCGLScalarIntegralType type,
      final int stride,
      final long offset)
    {
      JOGLArrayObjects.this.checkArrayBuffer(a);
      NullCheck.notNull(type);
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index_buffer",
        JOGLArrayObjects.this.valid_attribs,
        "Valid attribute indices");
      RangeCheck.checkIncludedInInteger(
        elements,
        "Element count",
        JOGLArrayObjects.VALID_ELEMENT_COUNT,
        "Valid element counts");
      RangeCheck.checkIncludedInInteger(
        stride, "Stride", Ranges.NATURAL_INTEGER, "Valid strides");
      RangeCheck.checkIncludedInLong(
        offset, "Offset", a.getRange(), "Buffer range");

      this.clearRanges(index, index);
      final JOGLArrayVertexAttributeIntegral attr =
        new JOGLArrayVertexAttributeIntegral(
          JOGLArrayObjects.this.context.getContext(),
          index,
          a,
          type,
          elements,
          stride,
          offset);

      this.attribs[index] = attr;
    }

    @Override public void setIndexBuffer(final JCGLIndexBufferUsableType i)
      throws JCGLExceptionDeleted
    {
      JOGLArrayObjects.this.checkIndexBuffer(i);
      this.index_buffer = i;
    }

    @Override public void setNoIndexBuffer()
    {
      this.index_buffer = null;
    }

    private void clearRanges(
      final int index_lo,
      final int index_hi)
    {
      final JCGLArrayVertexAttributeType[] aa = this.attribs;
      for (int i = index_lo; i <= index_hi; ++i) {
        final JCGLArrayVertexAttributeType exist_a = aa[i];
        if (exist_a != null) {
          exist_a.matchVertexAttribute(
            new JCGLArrayVertexAttributeMatcherType<Void, RuntimeException>()
            {
              @Override public Void matchFloatingPoint(
                final JCGLArrayVertexAttributeFloatingPointType a)
              {
                aa[a.getIndex()] = null;
                return null;
              }

              @Override public Void matchIntegral(
                final JCGLArrayVertexAttributeIntegralType a)
              {
                aa[a.getIndex()] = null;
                return null;
              }
            });
        }
      }
    }

    @Override public void reset()
    {
      for (int index = 0; index < this.attribs.length; ++index) {
        this.attribs[index] = null;
      }
      this.setNoIndexBuffer();
    }
  }
}
