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
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLResources;
import com.io7m.jcanephora.core.JCGLScalarIntegralType;
import com.io7m.jcanephora.core.JCGLScalarType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jnull.NullCheck;
import com.io7m.jnull.Nullable;
import com.io7m.jranges.RangeCheck;
import com.io7m.jranges.RangeInclusiveI;
import com.io7m.jranges.Ranges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.valid4j.Assertive;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

final class FakeArrayObjects implements JCGLArrayObjectsType
{
  private static final RangeInclusiveI MAX_ATTRIBS_RANGE;
  private static final Logger          LOG;
  private static final RangeInclusiveI VALID_ELEMENT_COUNT;

  static {
    MAX_ATTRIBS_RANGE = new RangeInclusiveI(16, Integer.MAX_VALUE);
    VALID_ELEMENT_COUNT = new RangeInclusiveI(1, 4);
    LOG = LoggerFactory.getLogger(FakeArrayObjects.class);
  }

  private final     FakeContext               context;
  private final     int                       max_attribs;
  private final     RangeInclusiveI           valid_attribs;
  private final     FakeArrayBuffers          arrays;
  private final     AtomicInteger             next_array;
  private @Nullable JCGLArrayObjectUsableType bind;

  FakeArrayObjects(
    final FakeContext c,
    final FakeArrayBuffers ga)
  {
    this.context = NullCheck.notNull(c);
    this.arrays = NullCheck.notNull(ga);
    this.next_array = new AtomicInteger(1);
    this.max_attribs = 16;
    this.valid_attribs = new RangeInclusiveI(0, this.max_attribs - 1);
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

    final Integer aid = Integer.valueOf(this.next_array.getAndIncrement());
    FakeArrayObjects.LOG.debug("allocated {}", aid);

    for (int index = 0; index < max; ++index) {
      final Integer box_index = Integer.valueOf(index);
      final JCGLArrayVertexAttributeType a = bb.attribs[index];
      if (a == null) {
        FakeArrayObjects.LOG.trace("[{}]: attr {} disabled", aid, box_index);
        continue;
      }

      this.arrays.arrayBufferBind(a.getArrayBuffer());
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

            FakeArrayObjects.LOG.trace(
              "[{}]: attr {} floating type:{}/{} norm:{} off:{} stride:{}",
              aid,
              box_index,
              t,
              Integer.valueOf(e),
              Boolean.valueOf(n),
              Long.valueOf(off),
              Integer.valueOf(stride));
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

            FakeArrayObjects.LOG.trace(
              "[{}]: attr {} integral type:{}/{} off:{} stride:{}",
              aid,
              box_index,
              t,
              Integer.valueOf(e),
              Long.valueOf(offset),
              Integer.valueOf(stride));
            return null;
          }
        });
    }

    this.arrays.arrayBufferUnbind();

    return new FakeArrayObject(
      this.context,
      aid.intValue(),
      Arrays.copyOf(bb.attribs, bb.attribs.length));
  }

  @Override
  public Optional<JCGLArrayObjectUsableType> arrayObjectGetCurrentlyBound()
    throws JCGLException
  {
    return Optional.ofNullable(this.bind);
  }

  @Override public void arrayObjectBind(
    final JCGLArrayObjectUsableType a)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(a);
    FakeCompatibilityChecks.checkArrayObject(this.context, a);
    JCGLResources.checkNotDeleted(a);

    this.bind = a;
  }

  @Override public void arrayObjectUnbind()
    throws JCGLException
  {
    this.bind = null;
  }

  @Override public void arrayObjectDelete(
    final JCGLArrayObjectType a)
    throws JCGLException, JCGLExceptionDeleted
  {
    NullCheck.notNull(a);
    FakeCompatibilityChecks.checkArrayObject(this.context, a);
    JCGLResources.checkNotDeleted(a);

    ((FakeArrayObject) a).setDeleted();

    if (this.bind != null) {
      if (this.bind.getGLName() == a.getGLName()) {
        this.arrayObjectUnbind();
      }
    }
  }

  private void checkArray(final JCGLArrayBufferUsableType a)
  {
    FakeCompatibilityChecks.checkArray(this.context, a);
    JCGLResources.checkNotDeleted(a);
  }

  private final class Builder extends FakeObjectPseudoUnshared
    implements JCGLArrayObjectBuilderType
  {
    private final JCGLArrayVertexAttributeType[] attribs;

    Builder()
    {
      super(FakeArrayObjects.this.context);
      this.attribs =
        new JCGLArrayVertexAttributeType[this.getMaximumVertexAttributes()];
    }

    @Override public Optional<JCGLArrayVertexAttributeType> getAttributeAt(
      final int index)
    {
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index",
        FakeArrayObjects.this.valid_attribs,
        "Valid attribute indices");

      return Optional.ofNullable(this.attribs[index]);
    }

    @Override public int getMaximumVertexAttributes()
    {
      return FakeArrayObjects.this.max_attribs;
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
      FakeArrayObjects.this.checkArray(a);
      NullCheck.notNull(type);
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index",
        FakeArrayObjects.this.valid_attribs,
        "Valid attribute indices");
      RangeCheck.checkIncludedInInteger(
        elements,
        "Element count",
        FakeArrayObjects.VALID_ELEMENT_COUNT,
        "Valid element counts");
      RangeCheck.checkIncludedInInteger(
        stride, "Stride", Ranges.NATURAL_INTEGER, "Valid strides");
      RangeCheck.checkIncludedInLong(
        offset, "Offset", a.getRange(), "Buffer range");

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
      FakeArrayObjects.this.checkArray(a);
      NullCheck.notNull(type);
      RangeCheck.checkIncludedInInteger(
        index,
        "Attribute index",
        FakeArrayObjects.this.valid_attribs,
        "Valid attribute indices");
      RangeCheck.checkIncludedInInteger(
        elements,
        "Element count",
        FakeArrayObjects.VALID_ELEMENT_COUNT,
        "Valid element counts");
      RangeCheck.checkIncludedInInteger(
        stride, "Stride", Ranges.NATURAL_INTEGER, "Valid strides");
      RangeCheck.checkIncludedInLong(
        offset, "Offset", a.getRange(), "Buffer range");

      this.clearRanges(index, index);
      final FakeArrayVertexAttributeIntegral attr =
        new FakeArrayVertexAttributeIntegral(
          FakeArrayObjects.this.context,
          index,
          a,
          type,
          elements,
          stride,
          offset);

      this.attribs[index] = attr;
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
    }
  }
}
