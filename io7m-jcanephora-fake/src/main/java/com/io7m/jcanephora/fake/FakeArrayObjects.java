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
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveI;
import com.io7m.jranges.Ranges;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.junsigned.ranges.UnsignedRangeCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.util.Arrays;
import java.util.Optional;

final class FakeArrayObjects implements JCGLArrayObjectsType
{
  private static final RangeInclusiveI MAX_ATTRIBS_RANGE;
  private static final Logger          LOG;
  private static final RangeInclusiveI VALID_ELEMENT_COUNT;
  private static final String          ATTR_FLOAT_TRACE_FORMAT;
  private static final String          ATTR_INTEGRAL_TRACE_FORMAT;
  private static final String          ATTR_DISABLED_TRACE_FORMAT;

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

  private final FakeContext               context;
  private final int                       max_attribs;
  private final RangeInclusiveI           valid_attribs;
  private final FakeArrayBuffers          array_buffers;
  private final FakeArrayObject           default_buffer;
  private final FakeIndexBuffers          index_buffers;
  private       JCGLArrayObjectUsableType bind;

  FakeArrayObjects(
    final FakeContext c,
    final FakeArrayBuffers ga,
    final FakeIndexBuffers gi)
    throws JCGLExceptionNonCompliant
  {
    this.context = NullCheck.notNull(c);
    this.array_buffers = NullCheck.notNull(ga);
    this.index_buffers = NullCheck.notNull(gi);
    this.index_buffers.setArrayObjects(this);

    this.max_attribs = 16;
    this.valid_attribs = new RangeInclusiveI(0, this.max_attribs - 1);

    this.default_buffer = new FakeArrayObject(
      c, c.getFreshID(), new JCGLArrayVertexAttributeType[0]);
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

    Assertive.ensure(b instanceof Builder);
    final Builder bb = (Builder) b;

    final int max = b.getMaximumVertexAttributes();
    for (int index = 0; index < max; ++index) {
      if (bb.attribs[index] != null) {
        FakeCompatibilityChecks.checkArrayAttribute(
          this.context, bb.attribs[index]);
      }
    }

    final FakeIndexBuffer ib;
    if (bb.index_buffer != null) {
      ib = this.checkIndexBuffer(bb.index_buffer);
    } else {
      ib = null;
    }

    final Integer aid = Integer.valueOf(this.context.getFreshID());
    FakeArrayObjects.LOG.debug("allocated {}", aid);

    final JCGLArrayVertexAttributeType[] write_attribs =
      Arrays.copyOf(bb.attribs, bb.attribs.length);
    final FakeArrayObject new_ao = new FakeArrayObject(
      this.context, aid.intValue(), write_attribs);

    this.actualBind(new_ao);

    try {
      if (ib != null) {
        this.index_buffers.indexBufferBind(ib);
      }

      for (int index = 0; index < max; ++index) {
        final Integer box_index = Integer.valueOf(index);
        final JCGLArrayVertexAttributeType a = bb.attribs[index];
        if (a == null) {
          FakeArrayObjects.LOG.trace(
            FakeArrayObjects.ATTR_DISABLED_TRACE_FORMAT,
            aid,
            box_index);
          continue;
        }

        this.array_buffers.arrayBufferBind(a.getArrayBuffer());
        a.matchVertexAttribute(
          new JCGLArrayVertexAttributeMatcherType<Void, JCGLException>()
          {
            @Override
            public Void matchFloatingPoint(
              final JCGLArrayVertexAttributeFloatingPointType af)
              throws JCGLException
            {
              final int e = af.getElements();
              final boolean n = af.isNormalized();
              final long off = af.getOffset();
              final int stride = af.getStride();
              final JCGLScalarType t = af.getType();
              final int divisor = af.getDivisor();

              FakeArrayObjects.LOG.trace(
                FakeArrayObjects.ATTR_FLOAT_TRACE_FORMAT,
                aid,
                box_index,
                t,
                Integer.valueOf(e),
                Boolean.valueOf(n),
                Long.valueOf(off),
                Integer.valueOf(stride),
                Integer.valueOf(divisor));
              return null;
            }

            @Override
            public Void matchIntegral(
              final JCGLArrayVertexAttributeIntegralType ai)
              throws JCGLException
            {
              final JCGLScalarIntegralType t = ai.getType();
              final int e = ai.getElements();
              final long offset = ai.getOffset();
              final int stride = ai.getStride();
              final int divisor = ai.getDivisor();

              FakeArrayObjects.LOG.trace(
                FakeArrayObjects.ATTR_INTEGRAL_TRACE_FORMAT,
                aid,
                box_index,
                t,
                Integer.valueOf(e),
                Long.valueOf(offset),
                Integer.valueOf(stride),
                Integer.valueOf(divisor));
              return null;
            }
          });
      }

      this.array_buffers.arrayBufferUnbind();
    } catch (final JCGLException e) {
      this.actualUnbind();
      throw e;
    }

    return new_ao;
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

  private void actualBind(final FakeArrayObject a)
  {
    FakeArrayObjects.LOG.trace("bind {} -> {}", this.bind, a);
    if (this.bind.getGLName() != a.getGLName()) {
      this.bind = a;
    }
  }

  private void actualUnbind()
  {
    FakeArrayObjects.LOG.trace(
      "unbind {} -> {}", this.bind, this.default_buffer);
    if (this.bind.getGLName() != this.default_buffer.getGLName()) {
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
    NullCheck.notNull(a);
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

    if (this.default_buffer.equals(a)) {
      throw new JCGLExceptionObjectNotDeletable(
        "Cannot delete the default array object");
    }

    FakeArrayObjects.LOG.debug("delete {}", Integer.valueOf(a.getGLName()));

    a.setDeleted();

    final FakeReferenceContainer rc = a.getReferenceContainer();
    for (final JCGLReferableType r : a.getReferences()) {
      rc.referenceRemove((FakeReferable) r);
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
    FakeCompatibilityChecks.checkArrayBuffer(this.context, a);
    JCGLResources.checkNotDeleted(a);
  }

  private FakeIndexBuffer checkIndexBuffer(final JCGLIndexBufferUsableType i)
  {
    NullCheck.notNull(i);
    FakeCompatibilityChecks.checkIndexBuffer(this.context, i);
    JCGLResources.checkNotDeleted(i);
    return (FakeIndexBuffer) i;
  }

  private final class Builder extends FakeObjectPseudoUnshared
    implements JCGLArrayObjectBuilderType
  {
    private final     JCGLArrayVertexAttributeType[] attribs;
    private @Nullable JCGLIndexBufferUsableType      index_buffer;
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
      NullCheck.notNull(type);
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index_buffer",
        FakeArrayObjects.this.valid_attribs,
        "Valid attribute indices");
      RangeCheck.checkIncludedInInteger(
        elements,
        "Element count",
        FakeArrayObjects.VALID_ELEMENT_COUNT,
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
      NullCheck.notNull(type);
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index_buffer",
        FakeArrayObjects.this.valid_attribs,
        "Valid attribute indices");
      RangeCheck.checkIncludedInInteger(
        elements,
        "Element count",
        FakeArrayObjects.VALID_ELEMENT_COUNT,
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
