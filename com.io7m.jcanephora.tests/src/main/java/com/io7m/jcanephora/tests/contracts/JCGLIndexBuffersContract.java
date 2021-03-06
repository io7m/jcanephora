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

package com.io7m.jcanephora.tests.contracts;

import com.io7m.jcanephora.core.JCGLArrayObjectType;
import com.io7m.jcanephora.core.JCGLArrayObjectUsableType;
import com.io7m.jcanephora.core.JCGLBufferUpdateType;
import com.io7m.jcanephora.core.JCGLBufferUpdates;
import com.io7m.jcanephora.core.JCGLExceptionBufferNotBound;
import com.io7m.jcanephora.core.JCGLExceptionDeleted;
import com.io7m.jcanephora.core.JCGLExceptionWrongContext;
import com.io7m.jcanephora.core.JCGLIndexBufferType;
import com.io7m.jcanephora.core.JCGLReferableType;
import com.io7m.jcanephora.core.JCGLReferenceContainerType;
import com.io7m.jcanephora.core.JCGLUnsignedType;
import com.io7m.jcanephora.core.JCGLUsageHint;
import com.io7m.jcanephora.core.api.JCGLArrayBuffersType;
import com.io7m.jcanephora.core.api.JCGLArrayObjectsType;
import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.core.api.JCGLIndexBuffersType;
import com.io7m.jnull.NullCheck;
import com.io7m.jranges.RangeCheckException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.Set;

/**
 * Index buffer contracts.
 */

// CHECKSTYLE_JAVADOC:OFF

public abstract class JCGLIndexBuffersContract extends JCGLContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  protected abstract Interfaces getIndexBuffers(String name);

  protected abstract JCGLUnsharedContextPair<JCGLIndexBuffersType>
  getIndexBuffersUnshared(
    String main,
    String alt);

  protected abstract JCGLSharedContextPair<JCGLIndexBuffersType>
  getIndexBuffersSharedWith(
    String name,
    String shared);

  @Test
  public final void testIndexBufferBadSize()
  {
    final Interfaces i = this.getIndexBuffers("name");
    final JCGLIndexBuffersType gi = i.getIndexBuffers();

    this.expected.expect(RangeCheckException.class);
    gi.indexBufferAllocate(
      -1L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);
  }

  @Test
  public final void testIndexBufferAllocateIdentity()
  {
    final Interfaces i = this.getIndexBuffers("name");
    final JCGLIndexBuffersType gi = i.getIndexBuffers();

    final JCGLIndexBufferType i0 = gi.indexBufferAllocate(
      1000L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);

    Assert.assertEquals(1000L, i0.indices());
    Assert.assertEquals(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, i0.type());
    Assert.assertEquals(JCGLUsageHint.USAGE_STATIC_DRAW, i0.usageHint());
    Assert.assertFalse(i0.isDeleted());
  }

  @Test
  public final void testIndexBufferReallocateIdentity()
  {
    final Interfaces i = this.getIndexBuffers("name");
    final JCGLIndexBuffersType gi = i.getIndexBuffers();

    final JCGLIndexBufferType i0 = gi.indexBufferAllocate(
      1000L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);

    Assert.assertEquals(1000L, i0.indices());
    Assert.assertEquals(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, i0.type());
    Assert.assertEquals(JCGLUsageHint.USAGE_STATIC_DRAW, i0.usageHint());
    Assert.assertFalse(i0.isDeleted());

    gi.indexBufferReallocate(i0);

    Assert.assertEquals(1000L, i0.indices());
    Assert.assertEquals(JCGLUnsignedType.TYPE_UNSIGNED_BYTE, i0.type());
    Assert.assertEquals(JCGLUsageHint.USAGE_STATIC_DRAW, i0.usageHint());
    Assert.assertFalse(i0.isDeleted());
  }

  @Test
  public final void testIndexBufferBindIdentity()
  {
    final Interfaces i = this.getIndexBuffers("name");
    final JCGLIndexBuffersType gi = i.getIndexBuffers();
    final JCGLArrayObjectsType ga = i.getArrayObjects();

    final JCGLArrayObjectUsableType a0 = ga.arrayObjectGetDefault();
    final Set<JCGLReferableType> a0_refs = a0.references();

    final JCGLIndexBufferType i0 = gi.indexBufferAllocate(
      1000L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);
    final Set<JCGLReferenceContainerType> i0_refs = i0.referringContainers();

    Assert.assertEquals(Optional.of(i0), gi.indexBufferGetCurrentlyBound());
    Assert.assertEquals(Optional.of(i0), a0.indexBufferBound());
    Assert.assertEquals(1L, (long) a0_refs.size());
    Assert.assertTrue(a0_refs.contains(i0));
    Assert.assertEquals(1L, (long) i0_refs.size());
    Assert.assertTrue(i0_refs.contains(a0));

    gi.indexBufferUnbind();

    Assert.assertEquals(a0, ga.arrayObjectGetCurrentlyBound());
    Assert.assertEquals(Optional.empty(), gi.indexBufferGetCurrentlyBound());
    Assert.assertEquals(Optional.empty(), a0.indexBufferBound());
    Assert.assertEquals(0L, (long) a0_refs.size());
    Assert.assertEquals(0L, (long) a0_refs.size());
    Assert.assertEquals(0L, (long) i0_refs.size());
  }

  @Test
  public final void testIndexUpdateDeleted()
  {
    final Interfaces ii = this.getIndexBuffers("main");
    final JCGLIndexBuffersType gi = ii.getIndexBuffers();
    final JCGLIndexBufferType i = gi.indexBufferAllocate(
      100L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLBufferUpdateType<JCGLIndexBufferType> u =
      JCGLBufferUpdates.newUpdateReplacingAll(i);

    gi.indexBufferDelete(i);

    this.expected.expect(JCGLExceptionDeleted.class);
    gi.indexBufferUpdate(u);
  }

  @Test
  public final void testIndexUpdateWrongContext()
  {
    final JCGLUnsharedContextPair<JCGLIndexBuffersType> p =
      this.getIndexBuffersUnshared("main", "alt");
    final JCGLContextType ca = p.getContextA();
    final JCGLContextType cb = p.getContextB();
    final JCGLIndexBuffersType gi = p.getValueA();
    final JCGLIndexBuffersType gb = p.getValueB();

    Assert.assertFalse(ca.contextIsCurrent());
    Assert.assertTrue(cb.contextIsCurrent());

    cb.contextReleaseCurrent();
    ca.contextMakeCurrent();
    final JCGLIndexBufferType i = gi.indexBufferAllocate(
      100L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLBufferUpdateType<JCGLIndexBufferType> u =
      JCGLBufferUpdates.newUpdateReplacingAll(i);

    ca.contextReleaseCurrent();
    cb.contextMakeCurrent();
    this.expected.expect(JCGLExceptionWrongContext.class);
    gb.indexBufferUpdate(u);
  }

  @Test
  public final void testIndexReallocateNotBound()
  {
    final Interfaces ii = this.getIndexBuffers("main");
    final JCGLIndexBuffersType gi = ii.getIndexBuffers();
    final JCGLIndexBufferType i = gi.indexBufferAllocate(
      100L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);

    gi.indexBufferUnbind();
    this.expected.expect(JCGLExceptionBufferNotBound.class);
    gi.indexBufferReallocate(i);
  }

  @Test
  public final void testIndexUpdateNotBound()
  {
    final Interfaces ii = this.getIndexBuffers("main");
    final JCGLIndexBuffersType gi = ii.getIndexBuffers();
    final JCGLIndexBufferType i = gi.indexBufferAllocate(
      100L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLBufferUpdateType<JCGLIndexBufferType> u =
      JCGLBufferUpdates.newUpdateReplacingAll(i);

    gi.indexBufferUnbind();
    this.expected.expect(JCGLExceptionBufferNotBound.class);
    gi.indexBufferUpdate(u);
  }

  @Test
  public final void testIndexUpdateShared()
  {
    final JCGLSharedContextPair<JCGLIndexBuffersType> p =
      this.getIndexBuffersSharedWith("main", "alt");
    final JCGLContextType ca = p.getMasterContext();
    final JCGLContextType cb = p.getSlaveContext();
    final JCGLIndexBuffersType gi = p.getMasterValue();
    final JCGLIndexBuffersType gb = p.getSlaveValue();

    Assert.assertTrue(ca.contextIsCurrent());
    Assert.assertFalse(cb.contextIsCurrent());

    final JCGLIndexBufferType i = gi.indexBufferAllocate(
      100L,
      JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
      JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLBufferUpdateType<JCGLIndexBufferType> u =
      JCGLBufferUpdates.newUpdateReplacingAll(i);

    ca.contextReleaseCurrent();
    cb.contextMakeCurrent();
    gb.indexBufferBind(i);
    gb.indexBufferUpdate(u);
  }

  @Test
  public final void testIndexRead()
  {
    final Interfaces ii = this.getIndexBuffers("main");
    final JCGLIndexBuffersType gi = ii.getIndexBuffers();
    final JCGLIndexBufferType i =
      gi.indexBufferAllocate(
        100L,
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        JCGLUsageHint.USAGE_STATIC_DRAW);

    final JCGLBufferUpdateType<JCGLIndexBufferType> u =
      JCGLBufferUpdates.newUpdateReplacingAll(i);
    final ByteBuffer b = u.data();
    for (int index = 0; index < 100; ++index) {
      b.put(index, (byte) 0x50);
    }

    gi.indexBufferUpdate(u);

    final ByteBuffer e =
      gi.indexBufferRead(i, size -> ByteBuffer.allocateDirect((int) size));

    for (int index = 0; index < 100; ++index) {
      Assert.assertEquals((long) b.get(index), (long) e.get(index));
    }
  }

  @Test
  public final void testIndexReadDeleted()
  {
    final Interfaces ii = this.getIndexBuffers("main");
    final JCGLIndexBuffersType gi = ii.getIndexBuffers();
    final JCGLIndexBufferType i =
      gi.indexBufferAllocate(
        100L,
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        JCGLUsageHint.USAGE_STATIC_DRAW);

    gi.indexBufferDelete(i);

    this.expected.expect(JCGLExceptionDeleted.class);
    gi.indexBufferRead(i, size -> ByteBuffer.allocateDirect((int) size));
  }

  @Test
  public final void testIndexReadNotBound()
  {
    final Interfaces ii = this.getIndexBuffers("main");
    final JCGLIndexBuffersType gi = ii.getIndexBuffers();
    final JCGLIndexBufferType i =
      gi.indexBufferAllocate(
        100L,
        JCGLUnsignedType.TYPE_UNSIGNED_BYTE,
        JCGLUsageHint.USAGE_STATIC_DRAW);

    gi.indexBufferUnbind();

    this.expected.expect(JCGLExceptionBufferNotBound.class);
    gi.indexBufferRead(i, size -> ByteBuffer.allocateDirect((int) size));
  }

  protected static final class Interfaces
  {
    private final JCGLContextType context;
    private final JCGLIndexBuffersType index_buffers;
    private final JCGLArrayBuffersType array_buffers;
    private final JCGLArrayObjectsType array_objects;

    public Interfaces(
      final JCGLContextType in_context,
      final JCGLArrayBuffersType in_array_buffers,
      final JCGLIndexBuffersType in_index_buffers,
      final JCGLArrayObjectsType in_array_objects)
    {
      this.context = NullCheck.notNull(in_context, "Context");
      this.array_buffers = NullCheck.notNull(in_array_buffers, "Array buffers");
      this.array_objects = NullCheck.notNull(in_array_objects, "Array objects");
      this.index_buffers = NullCheck.notNull(in_index_buffers, "Index objects");
    }

    public JCGLContextType getContext()
    {
      return this.context;
    }

    public JCGLIndexBuffersType getIndexBuffers()
    {
      return this.index_buffers;
    }

    public JCGLArrayBuffersType getArrayBuffers()
    {
      return this.array_buffers;
    }

    public JCGLArrayObjectsType getArrayObjects()
    {
      return this.array_objects;
    }
  }
}
