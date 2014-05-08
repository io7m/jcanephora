/*
 * Copyright © 2014 <code@io7m.com> http://io7m.com
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

package com.io7m.jcanephora.tests.types;

import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.JCGLExceptionAttributeMissing;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.junreachable.UnimplementedCodeException;

public final class UnimplementedArray implements ArrayBufferType
{
  public UnimplementedArray()
  {

  }

  @Override public ArrayDescriptor arrayGetDescriptor()
  {
    // TODO Auto-generated method stub
    throw new UnimplementedCodeException();
  }

  @Override public ArrayAttributeType arrayGetAttribute(
    final String name)
    throws JCGLExceptionAttributeMissing
  {
    // TODO Auto-generated method stub
    throw new UnimplementedCodeException();
  }

  @Override public UsageHint arrayGetUsageHint()
  {
    // TODO Auto-generated method stub
    throw new UnimplementedCodeException();
  }

  @Override public boolean resourceIsDeleted()
  {
    // TODO Auto-generated method stub
    throw new UnimplementedCodeException();
  }

  @Override public long bufferGetElementSizeBytes()
  {
    // TODO Auto-generated method stub
    throw new UnimplementedCodeException();
  }

  @Override public RangeInclusiveL bufferGetRange()
  {
    // TODO Auto-generated method stub
    throw new UnimplementedCodeException();
  }

  @Override public int getGLName()
  {
    // TODO Auto-generated method stub
    throw new UnimplementedCodeException();
  }

  @Override public long resourceGetSizeBytes()
  {
    // TODO Auto-generated method stub
    throw new UnimplementedCodeException();
  }
}
