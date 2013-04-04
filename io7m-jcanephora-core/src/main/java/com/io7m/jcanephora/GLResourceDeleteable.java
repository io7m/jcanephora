package com.io7m.jcanephora;

abstract class GLResourceDeleteable implements GLResourceUsable
{
  private boolean deleted = false;

  final void resourceSetDeleted()
  {
    this.deleted = true;
  }

  @Override public final boolean resourceIsDeleted()
  {
    return this.deleted;
  }
}
