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

package com.io7m.jcanephora.fake;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

final class FakeArrayObjects implements JCGLArrayObjectsType
{
  private static final RangeInclusiveI MAX_ATTRIBS_RANGE;
  private static final Logger LOG;
  private static final RangeInclusiveI VALID_ELEMENT_COUNT;
  private static final String ATTR_FLOAT_TRACE_FORMAT;
  private static final String ATTR_INTEGRAL_TRACE_FORMAT;
  private static final String ATTR_DISABLED_TRACE_FORMAT;

  static {
    MAX_ATTRIBS_RANGE = new RangeInclusiveI(16, Integer.MAX_VALUE);
    VALID_ELEMENT_COUNT = new RangeInclusiveI(1, 4);
    LOG = LoggerFactory.getLogger(FakeArrayObjects.class);

    ATTR_FLOAT_TRACE_FORMAT =
      "[{}]: attr {} floating type:{}/{} norm:{} off:{} stride:{} div:{}";
    ATTR_INTEGRAL_TRACE_FORMAT =
      "[{}]: attr {} integral type:{}/{} off:{} stride:{} div:{}";
    ATTR_DISABLED_TRACE_FORMAT =
      "[{}]: attr {} disabled";
  }

  private final FakeContext context;
  private final int max_attribs;
  private final RangeInclusiveI valid_attribs;
  private final FakeArrayBuffers array_buffers;
  private final FakeArrayObject default_buffer;
  private final FakeIndexBuffers index_buffers;
  private JCGLArrayObjectUsableType bind;

  FakeArrayObjects(
    final FakeContext c,
    final FakeArrayBuffers ga,
    final FakeIndexBuffers gi)
    throws JCGLExceptionNonCompliant
  {
    this.context = NullCheck.notNull(c, "Context");
    this.array_buffers = NullCheck.notNull(ga, "Array buffers");
    this.index_buffers = NullCheck.notNull(gi, "Index buffers");
    this.index_buffers.setArrayObjects(this);

    this.max_attribs = 16;
    this.valid_attribs = new RangeInclusiveI(0, this.max_attribs - 1);

    this.default_buffer = new FakeArrayObject(
      c, c.getFreshID(), true, null, new JCGLArrayVertexAttributeType[0]);
    this.bind = this.default_buffer;
  }

  @Override
  public JCGLArrayObjectBuilderType arrayObjectNewBuilder()
    throws JCGLException
  {
    return new Builder();
  }

  @Override
  public JCGLArrayObjectType arrayObjectAllocate(
    final JCGLArrayObjectBuilderType b)
    throws JCGLException
  {
    FakeCompatibilityChecks.checkArrayObjectBuilder(this.context, b);

    Preconditions.checkPrecondition(
      b,
      b instanceof Builder,
      ignored -> "Builder must belong to this implementation");

    final Builder bb = (Builder) b;

    final int max = b.getMaximumVertexAttributes();
    this.checkArrayAttributes(bb, max);

    final FakeIndexBuffer ib;
    if (bb.index_buffer != null) {
      ib = this.checkIndexBuffer(bb.index_buffer);
    } else {
      ib = null;
    }

    final Integer array_id = Integer.valueOf(this.context.getFreshID());
    LOG.debug("allocated {}", array_id);

    final JCGLArrayVertexAttributeType[] write_attribs =
      Arrays.copyOf(bb.attribs, bb.attribs.length);
    final FakeArrayObject new_ao = new FakeArrayObject(
      this.context, array_id.intValue(), false, ib, write_attribs);

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
    final Integer array_id,
    final int attrib_index)
  {
    final Integer box_index = Integer.valueOf(attrib_index);
    if (attrib == null) {
      LOG.trace(
        ATTR_DISABLED_TRACE_FORMAT,
        array_id,
        box_index);
      return;
    }

    this.array_buffers.arrayBufferBind(attrib.getArrayBuffer());
    attrib.matchVertexAttribute(
      new JCGLArrayVertexAttributeMatcherType<Unit, JCGLException>()
      {
        @Override
        public Unit matchFloatingPoint(
          final JCGLArrayVertexAttributeFloatingPointType af)
          throws JCGLException
        {
          final int e = af.elementCount();
          final boolean n = af.isNormalized();
          final long off = af.offsetOctets();
          final int stride = af.strideOctets();
          final JCGLScalarType t = af.type();
          final int divisor = af.divisor();

          LOG.trace(
            ATTR_FLOAT_TRACE_FORMAT,
            array_id,
            box_index,
            t,
            Integer.valueOf(e),
            Boolean.valueOf(n),
            Long.valueOf(off),
            Integer.valueOf(stride),
            Integer.valueOf(divisor));
          return Unit.unit();
        }

        @Override
        public Unit matchIntegral(
          final JCGLArrayVertexAttributeIntegralType ai)
          throws JCGLException
        {
          final JCGLScalarIntegralType t = ai.type();
          final int e = ai.elements();
          final long offset = ai.offsetOctets();
          final int stride = ai.strideOctets();
          final int divisor = ai.divisor();

          LOG.trace(
            ATTR_INTEGRAL_TRACE_FORMAT,
            array_id,
            box_index,
            t,
            Integer.valueOf(e),
            Long.valueOf(offset),
            Integer.valueOf(stride),
            Integer.valueOf(divisor));
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
        FakeCompatibilityChecks.checkArrayAttribute(
          this.context, bb.attribs[index]);
      }
    }
  }

  @Override
  public JCGLArrayObjectBuilderType arrayObjectNewBuilderFromObject(
    final JCGLArrayObjectUsableType o)
  {
    final FakeArrayObject jo = this.checkArrayObject(o);
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
          f.elementCount(),
          f.type(),
          f.strideOctets(),
          f.offsetOctets(),
          f.isNormalized(),
          f.divisor());
      } else if (a instanceof JCGLArrayVertexAttributeIntegralType) {
        final JCGLArrayVertexAttributeIntegralType i =
          (JCGLArrayVertexAttributeIntegralType) a;
        b.setAttributeIntegralWithDivisor(
          index,
          i.getArrayBuffer(),
          i.elements(),
          i.type(),
          i.strideOctets(),
          i.offsetOctets(),
          i.divisor());
      } else {
        throw new UnreachableCodeException();
      }
    }

    return b;
  }

  private void actualBind(final FakeArrayObject a)
  {
    LOG.trace("bind {} -> {}", this.bind, a);
    if (this.bind.glName() != a.glName()) {
      this.bind = a;
    }
  }

  private void actualUnbind()
  {
    LOG.trace(
      "unbind {} -> {}", this.bind, this.default_buffer);
    if (this.bind.glName() != this.default_buffer.glName()) {
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

  private FakeArrayObject checkArrayObject(final JCGLArrayObjectUsableType a)
  {
    NullCheck.notNull(a, "Array");
    FakeCompatibilityChecks.checkArrayObject(this.context, a);
    JCGLResources.checkNotDeleted(a);
    return (FakeArrayObject) a;
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
    final FakeArrayObject a = this.checkArrayObject(ai);

    if (Objects.equals(this.default_buffer, a)) {
      throw new JCGLExceptionObjectNotDeletable(
        "Cannot delete the default array object");
    }

    LOG.debug("delete {}", Integer.valueOf(a.glName()));

    a.setDeleted();

    final FakeReferenceContainer rc = a.getReferenceContainer();
    for (final JCGLReferableType r : a.references()) {
      rc.referenceRemove((FakeReferable) r);
    }

    if (this.bind.glName() == a.glName()) {
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
    FakeCompatibilityChecks.checkArrayBuffer(this.context, a);
    JCGLResources.checkNotDeleted(a);
  }

  private FakeIndexBuffer checkIndexBuffer(final JCGLIndexBufferUsableType i)
  {
    NullCheck.notNull(i, "Index buffer");
    FakeCompatibilityChecks.checkIndexBuffer(this.context, i);
    JCGLResources.checkNotDeleted(i);
    return (FakeIndexBuffer) i;
  }

  private final class Builder extends FakeObjectPseudoUnshared
    implements JCGLArrayObjectBuilderType
  {
    private final JCGLArrayVertexAttributeType[] attribs;
    private @Nullable JCGLIndexBufferUsableType index_buffer;
    private boolean strict;

    Builder()
    {
      super(FakeArrayObjects.this.context);
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
        FakeArrayObjects.this.valid_attribs,
        "Valid attribute indices");

      return Optional.ofNullable(this.attribs[index]);
    }

    @Override
    public int getMaximumVertexAttributes()
    {
      return FakeArrayObjects.this.max_attribs;
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
      FakeArrayObjects.this.checkArrayBuffer(a);
      NullCheck.notNull(type, "Type");
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index_buffer",
        FakeArrayObjects.this.valid_attribs,
        "Valid attribute indices");
      RangeCheck.checkIncludedInInteger(
        elements,
        "Element count",
        VALID_ELEMENT_COUNT,
        "Valid element counts");
      RangeCheck.checkIncludedInInteger(
        stride, "Stride", Ranges.NATURAL_INTEGER, "Valid strides");
      UnsignedRangeCheck.checkIncludedInLong(
        offset, "Offset", a.byteRange(), "Buffer range");
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
      final FakeArrayVertexAttributeFloating attr =
        new FakeArrayVertexAttributeFloating(
          FakeArrayObjects.this.context,
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
      FakeArrayObjects.this.checkArrayBuffer(a);
      NullCheck.notNull(type, "Type");
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index_buffer",
        FakeArrayObjects.this.valid_attribs,
        "Valid attribute indices");
      RangeCheck.checkIncludedInInteger(
        elements,
        "Element count",
        VALID_ELEMENT_COUNT,
        "Valid element counts");
      RangeCheck.checkIncludedInInteger(
        stride, "Stride", Ranges.NATURAL_INTEGER, "Valid strides");
      UnsignedRangeCheck.checkIncludedInLong(
        offset, "Offset", a.byteRange(), "Buffer range");
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
      final FakeArrayVertexAttributeIntegral attr =
        new FakeArrayVertexAttributeIntegral(
          FakeArrayObjects.this.context,
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
      FakeArrayObjects.this.checkIndexBuffer(i);
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
                aa[a.index()] = null;
                return null;
              }

              @Override
              public Void matchIntegral(
                final JCGLArrayVertexAttributeIntegralType a)
              {
                aa[a.index()] = null;
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
