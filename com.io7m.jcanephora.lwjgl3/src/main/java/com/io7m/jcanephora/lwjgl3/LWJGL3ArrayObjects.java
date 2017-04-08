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

package com.io7m.jcanephora.lwjgl3;

import com.io7m.jaffirm.core.Preconditions;
import com.io7m.jcanephora.core.JCGLArrayBufferUsableType;
import com.io7m.jcanephora.core.JCGLArrayObjectBuilderType;
import com.io7m.jcanephora.core.JCGLArrayObjectType;
import com.io7m.jcanephora.core.JCGLArrayObjectUsableType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeFloatingPointType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeIntegralType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeMatcherType;
import com.io7m.jcanephora.core.JCGLArrayVertexAttributeType;
import com.io7m.jcanephora.core.JCGLException;
import com.io7m.jcanephora.core.JCGLExceptionAttributeAlreadyAssigned;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionNonCompliant;
import com.io7m.jcanephora.core.JCGLExceptionObjectNotDeletable;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLIndexBufferUsableType;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLScalarIntegralType;
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jfunctional.Unit;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveI;
import com.io7m.jranges.Ranges;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.junsigned.ranges.UnsignedRangeCheck;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

final class LWJGL3ArrayObjects implements JCGLArrayObjectsType
{
  private static final Logger LOG;
  private static final RangeInclusiveI VALID_ELEMENT_COUNT;
  private static final String ATTR_FLOAT_TRACE_FORMAT;
  private static final String ATTR_INTEGRAL_TRACE_FORMAT;
  private static final String ATTR_DISABLED_TRACE_FORMAT;

  static {
    VALID_ELEMENT_COUNT = new RangeInclusiveI(1, 4);
    LOG = LoggerFactory.getLogger(LWJGL3ArrayObjects.class);
    ATTR_FLOAT_TRACE_FORMAT =
      "[{}]: attr {} floating type:{}/{} norm:{} off:{} stride:{} div:{}";
    ATTR_INTEGRAL_TRACE_FORMAT =
      "[{}]: attr {} integral type:{}/{} off:{} stride:{} div:{}";
    ATTR_DISABLED_TRACE_FORMAT =
      "[{}]: attr {} disabled";
  }

  private final LWJGL3Context context;
  private final int max_attribs;
  private final RangeInclusiveI valid_attribs;
  private final LWJGL3ArrayBuffers array_buffers;
  private final LWJGL3ArrayObject default_buffer;
  private final LWJGL3IndexBuffers index_buffers;
  private LWJGL3ArrayObject bind;

  LWJGL3ArrayObjects(
    final LWJGL3Context c,
    final LWJGL3ArrayBuffers ga,
    final LWJGL3IndexBuffers gi)
    throws JCGLExceptionNonCompliant
  {
    this.context = NullCheck.notNull(c, "Context");
    this.array_buffers = NullCheck.notNull(ga, "Array buffers");
    this.index_buffers = NullCheck.notNull(gi, "Index buffers");
    this.index_buffers.setArrayObjects(this);

    final int max = GL11.glGetInteger(GL20.GL_MAX_VERTEX_ATTRIBS);

    if (LOG.isDebugEnabled()) {
      LOG.debug(
        "reported maximum supported vertex attributes: {}",
        Integer.valueOf(max));
    }

    if (max < 16) {
      final StringBuilder sb = new StringBuilder(128);
      sb.append("Non-compliant OpenGL implementation.");
      sb.append(System.lineSeparator());
      sb.append("  Required minimum supported vertex attributes: 16");
      sb.append(System.lineSeparator());
      sb.append("  Implementation supports (GL_MAX_VERTEX_ATTRIBS): ");
      sb.append(max);
      final String message = sb.toString();
      LOG.error(message);
      throw new JCGLExceptionNonCompliant(message);
    }

    // Paranoia: Clamp unreasonably large values
    this.max_attribs = Math.min(max, 1024);
    this.valid_attribs = new RangeInclusiveI(0, this.max_attribs - 1);

    final int vao_id = GL30.glGenVertexArrays();
    this.default_buffer = new LWJGL3ArrayObject(
      this.context, vao_id, new JCGLArrayVertexAttributeType[0]);
    this.bind = this.default_buffer;

    /*
     * Configure baseline defaults.
     */

    GL30.glBindVertexArray(0);
    LWJGL3ErrorChecking.checkErrors();
  }

  private static Builder checkArrayObjectBuilder(
    final LWJGL3Context context,
    final JCGLArrayObjectBuilderType b)
    throws JCGLExceptionWrongContext
  {
    NullCheck.notNull(context, "Context");
    NullCheck.notNull(b, "Builder");
    return (Builder) LWJGL3CompatibilityChecks.checkAny(context, b);
  }

  private static void checkArrayAttribute(
    final LWJGL3Context c,
    final JCGLArrayVertexAttributeType a)
  {
    LWJGL3CompatibilityChecks.checkAny(c, a);
  }

  @Override
  public JCGLArrayObjectBuilderType arrayObjectNewBuilder()
    throws JCGLException
  {
    return new Builder();
  }

  @Override
  public JCGLArrayObjectBuilderType arrayObjectNewBuilderFromObject(
    final JCGLArrayObjectUsableType o)
  {
    final LWJGL3ArrayObject jo = this.checkArrayObject(o);
    final Builder b = new Builder();
    final JCGLArrayVertexAttributeType[] ja = jo.getAttribs();
    for (int index = 0; index < ja.length; ++index) {
      final JCGLArrayVertexAttributeType a = ja[index];
      if (a == null) {
        continue;
      }

      if (a instanceof JCGLArrayVertexAttributeFloatingPointType) {
        final JCGLArrayVertexAttributeFloatingPointType f =
          (JCGLArrayVertexAttributeFloatingPointType) a;
        b.setAttributeFloatingPointWithDivisor(
          index,
          f.getArrayBuffer(),
          f.getElements(),
          f.getType(),
          f.getStride(),
          f.getOffset(),
          f.isNormalized(),
          f.getDivisor());
      } else if (a instanceof JCGLArrayVertexAttributeIntegralType) {
        final JCGLArrayVertexAttributeIntegralType i =
          (JCGLArrayVertexAttributeIntegralType) a;
        b.setAttributeIntegralWithDivisor(
          index,
          i.getArrayBuffer(),
          i.getElements(),
          i.getType(),
          i.getStride(),
          i.getOffset(),
          i.getDivisor());
      } else {
        throw new UnreachableCodeException();
      }
    }

    return b;
  }

  @Override
  public JCGLArrayObjectType arrayObjectAllocate(
    final JCGLArrayObjectBuilderType b)
    throws JCGLException
  {
    checkArrayObjectBuilder(this.context, b);

    Preconditions.checkPrecondition(
      b,
      b instanceof Builder,
      ignored -> "Builder must belong to this implementation");
    final Builder bb = (Builder) b;

    final int max = b.getMaximumVertexAttributes();
    this.checkArrayAttributes(bb, max);

    final LWJGL3IndexBuffer ib;
    if (bb.index_buffer != null) {
      ib = this.checkIndexBuffer(bb.index_buffer);
    } else {
      ib = null;
    }

    final int array_id = GL30.glGenVertexArrays();
    if (LOG.isDebugEnabled()) {
      LOG.debug("allocated {}", Integer.valueOf(array_id));
    }

    final JCGLArrayVertexAttributeType[] write_attribs =
      Arrays.copyOf(bb.attribs, bb.attribs.length);
    final LWJGL3ArrayObject new_ao =
      new LWJGL3ArrayObject(this.context, array_id, write_attribs);

    this.actualBind(new_ao);

    try {
      if (ib != null) {
        this.index_buffers.indexBufferBind(ib);
      }

      for (int index = 0; index < max; ++index) {
        this.arrayObjectAllocateConfigureAttribute(
          bb.attribs[index], array_id, index);
      }

      this.array_buffers.arrayBufferUnbind();
    } catch (final JCGLException e) {
      this.actualUnbind();
      throw e;
    }

    return new_ao;
  }

  private void arrayObjectAllocateConfigureAttribute(
    final JCGLArrayVertexAttributeType attrib,
    final int array_id,
    final int attrib_index)
  {
    final Integer box_index = Integer.valueOf(attrib_index);
    if (attrib == null) {
      if (LOG.isTraceEnabled()) {
        LOG.trace(
          ATTR_DISABLED_TRACE_FORMAT,
          Integer.valueOf(array_id),
          box_index);
      }
      GL20.glDisableVertexAttribArray(attrib_index);
      return;
    }

    GL20.glEnableVertexAttribArray(attrib_index);

    final JCGLArrayBufferUsableType ab = attrib.getArrayBuffer();
    if (!this.array_buffers.arrayBufferIsBound(ab)) {
      this.array_buffers.arrayBufferBind(ab);
    }

    attrib.matchVertexAttribute(
      new JCGLArrayVertexAttributeMatcherType<Unit, JCGLException>()
      {
        @Override
        public Unit matchFloatingPoint(
          final JCGLArrayVertexAttributeFloatingPointType af)
          throws JCGLException
        {
          final int e = af.getElements();
          final boolean n = af.isNormalized();
          final long off = af.getOffset();
          final int stride = af.getStride();
          final JCGLScalarType t = af.getType();
          final int divisor = af.getDivisor();

          if (LOG.isTraceEnabled()) {
            LOG.trace(
              ATTR_FLOAT_TRACE_FORMAT,
              Integer.valueOf(array_id),
              box_index,
              t,
              Integer.valueOf(e),
              Boolean.valueOf(n),
              Long.valueOf(off),
              Integer.valueOf(stride),
              Integer.valueOf(divisor));
          }

          GL20.glVertexAttribPointer(
            box_index.intValue(),
            e,
            LWJGL3TypeConversions.scalarTypeToGL(t),
            n,
            stride,
            off);
          GL33.glVertexAttribDivisor(box_index.intValue(), divisor);
          return Unit.unit();
        }

        @Override
        public Unit matchIntegral(
          final JCGLArrayVertexAttributeIntegralType ai)
          throws JCGLException
        {
          final JCGLScalarIntegralType t = ai.getType();
          final int e = ai.getElements();
          final long offset = ai.getOffset();
          final int stride = ai.getStride();
          final int divisor = ai.getDivisor();

          if (LOG.isTraceEnabled()) {
            LOG.trace(
              ATTR_INTEGRAL_TRACE_FORMAT,
              Integer.valueOf(array_id),
              box_index,
              t,
              Integer.valueOf(e),
              Long.valueOf(offset),
              Integer.valueOf(stride),
              Integer.valueOf(divisor));
          }

          GL30.glVertexAttribIPointer(
            box_index.intValue(),
            e,
            LWJGL3TypeConversions.scalarIntegralTypeToGL(t),
            stride,
            offset);
          GL33.glVertexAttribDivisor(box_index.intValue(), divisor);
          return Unit.unit();
        }
      });
  }

  private void checkArrayAttributes(
    final Builder bb,
    final int max)
  {
    for (int index = 0; index < max; ++index) {
      if (bb.attribs[index] != null) {
        checkArrayAttribute(this.context, bb.attribs[index]);
      }
    }
  }

  private void actualBind(final LWJGL3ArrayObject a)
  {
    if (LOG.isTraceEnabled()) {
      LOG.trace("bind {} -> {}", this.bind, a);
    }

    if (this.bind.getGLName() != a.getGLName()) {
      GL30.glBindVertexArray(a.getGLName());
      this.bind = a;
    }
  }

  private void actualUnbind()
  {
    if (LOG.isTraceEnabled()) {
      LOG.trace(
        "unbind {} -> {}", this.bind, this.default_buffer);
    }

    if (this.bind.getGLName() != this.default_buffer.getGLName()) {
      GL30.glBindVertexArray(0);
      this.bind = this.default_buffer;
    }
  }

  @Override
  public JCGLArrayObjectUsableType arrayObjectGetCurrentlyBound()
    throws JCGLException
  {
    return this.bind;
  }

  @Override
  public void arrayObjectBind(
    final JCGLArrayObjectUsableType a)
    throws JCGLException, JCGLExceptionDeleted
  {
    this.actualBind(this.checkArrayObject(a));
  }

  private LWJGL3ArrayObject checkArrayObject(final JCGLArrayObjectUsableType a)
  {
    NullCheck.notNull(a, "Array object");
    LWJGL3ArrayObject.checkArrayObject(this.context, a);
    JCGLResources.checkNotDeleted(a);
    return (LWJGL3ArrayObject) a;
  }

  @Override
  public void arrayObjectUnbind()
    throws JCGLException
  {
    this.actualUnbind();
  }

  @Override
  public void arrayObjectDelete(
    final JCGLArrayObjectType ai)
    throws JCGLException, JCGLExceptionDeleted
  {
    final LWJGL3ArrayObject a = this.checkArrayObject(ai);

    if (Objects.equals(this.default_buffer, a)) {
      throw new JCGLExceptionObjectNotDeletable(
        "Cannot delete the default array object");
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("delete {}", Integer.valueOf(a.getGLName()));
    }

    GL30.glDeleteVertexArrays(a.getGLName());
    a.setDeleted();

    final LWJGL3ReferenceContainer rc = a.getReferenceContainer();
    for (final JCGLReferableType r : a.getReferences()) {
      rc.referenceRemove((LWJGL3Referable) r);
    }

    if (this.bind.getGLName() == a.getGLName()) {
      this.actualUnbind();
    }
  }

  @Override
  public JCGLArrayObjectUsableType arrayObjectGetDefault()
  {
    return this.default_buffer;
  }

  private void checkArrayBuffer(final JCGLArrayBufferUsableType a)
  {
    LWJGL3ArrayBuffer.checkArray(this.context, a);
    JCGLResources.checkNotDeleted(a);
  }

  private LWJGL3IndexBuffer checkIndexBuffer(final JCGLIndexBufferUsableType i)
  {
    NullCheck.notNull(i, "Index buffer");
    LWJGL3IndexBuffer.checkIndexBuffer(this.context, i);
    JCGLResources.checkNotDeleted(i);
    return (LWJGL3IndexBuffer) i;
  }

  LWJGL3IndexBuffer getCurrentIndexBuffer()
  {
    return this.bind.getIndexBufferUnsafe();
  }

  private final class Builder extends LWJGL3ObjectPseudoUnshared
    implements JCGLArrayObjectBuilderType
  {
    private final JCGLArrayVertexAttributeType[] attribs;
    private @Nullable JCGLIndexBufferUsableType index_buffer;
    private boolean strict;

    Builder()
    {
      super(LWJGL3ArrayObjects.this.context);
      final int count = this.getMaximumVertexAttributes();
      this.attribs = new JCGLArrayVertexAttributeType[count];
    }

    @Override
    public Optional<JCGLArrayVertexAttributeType> getAttributeAt(
      final int index)
    {
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index_buffer",
        LWJGL3ArrayObjects.this.valid_attribs,
        "Valid attribute indices");

      return Optional.ofNullable(this.attribs[index]);
    }

    @Override
    public int getMaximumVertexAttributes()
    {
      return LWJGL3ArrayObjects.this.max_attribs;
    }

    @Override
    public void setAttributeFloatingPointWithDivisor(
      final int index,
      final JCGLArrayBufferUsableType a,
      final int elements,
      final JCGLScalarType type,
      final int stride,
      final long offset,
      final boolean normalized,
      final int divisor)
    {
      LWJGL3ArrayObjects.this.checkArrayBuffer(a);
      NullCheck.notNull(type, "Type");
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index_buffer",
        LWJGL3ArrayObjects.this.valid_attribs,
        "Valid attribute indices");
      RangeCheck.checkIncludedInInteger(
        elements,
        "Element count",
        VALID_ELEMENT_COUNT,
        "Valid element counts");
      RangeCheck.checkIncludedInInteger(
        stride, "Stride", Ranges.NATURAL_INTEGER, "Valid strides");
      UnsignedRangeCheck.checkIncludedInLong(
        offset, "Offset", a.getRange(), "Buffer range");
      RangeCheck.checkIncludedInInteger(
        divisor, "Divisor", Ranges.NATURAL_INTEGER, "Valid divisors");

      if (this.strict) {
        if (this.attribs[index] != null) {
          throw new JCGLExceptionAttributeAlreadyAssigned(
            String.format(
              "Attribute %d has already been assigned",
              Integer.valueOf(index)));
        }
      }

      this.clearRanges(index, index);
      final LWJGL3ArrayVertexAttributeFloating attr =
        new LWJGL3ArrayVertexAttributeFloating(
          LWJGL3ArrayObjects.this.context,
          index,
          a,
          type,
          elements,
          stride,
          offset,
          normalized,
          divisor);

      this.attribs[index] = attr;
    }

    @Override
    public void setAttributeIntegralWithDivisor(
      final int index,
      final JCGLArrayBufferUsableType a,
      final int elements,
      final JCGLScalarIntegralType type,
      final int stride,
      final long offset,
      final int divisor)
    {
      LWJGL3ArrayObjects.this.checkArrayBuffer(a);
      NullCheck.notNull(type, "Type");
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index_buffer",
        LWJGL3ArrayObjects.this.valid_attribs,
        "Valid attribute indices");
      RangeCheck.checkIncludedInInteger(
        elements,
        "Element count",
        VALID_ELEMENT_COUNT,
        "Valid element counts");
      RangeCheck.checkIncludedInInteger(
        stride, "Stride", Ranges.NATURAL_INTEGER, "Valid strides");
      UnsignedRangeCheck.checkIncludedInLong(
        offset, "Offset", a.getRange(), "Buffer range");
      RangeCheck.checkIncludedInInteger(
        divisor, "Divisor", Ranges.NATURAL_INTEGER, "Valid divisors");

      if (this.strict) {
        if (this.attribs[index] != null) {
          throw new JCGLExceptionAttributeAlreadyAssigned(
            String.format(
              "Attribute %d has already been assigned",
              Integer.valueOf(index)));
        }
      }

      this.clearRanges(index, index);
      final LWJGL3ArrayVertexAttributeIntegral attr =
        new LWJGL3ArrayVertexAttributeIntegral(
          LWJGL3ArrayObjects.this.context,
          index,
          a,
          type,
          elements,
          stride,
          offset,
          divisor);

      this.attribs[index] = attr;
    }

    @Override
    public void setStrictChecking(final boolean enabled)
    {
      this.strict = enabled;
    }

    @Override
    public void setIndexBuffer(final JCGLIndexBufferUsableType i)
      throws JCGLExceptionDeleted
    {
      LWJGL3ArrayObjects.this.checkIndexBuffer(i);
      this.index_buffer = i;
    }

    @Override
    public void setNoIndexBuffer()
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
              @Override
              public Void matchFloatingPoint(
                final JCGLArrayVertexAttributeFloatingPointType a)
              {
                aa[a.getIndex()] = null;
                return null;
              }

              @Override
              public Void matchIntegral(
                final JCGLArrayVertexAttributeIntegralType a)
              {
                aa[a.getIndex()] = null;
                return null;
              }
            });
        }
      }
    }

    @Override
    public void reset()
    {
      for (int index = 0; index < this.attribs.length; ++index) {
        this.attribs[index] = null;
      }
      this.setNoIndexBuffer();
    }
  }
}
