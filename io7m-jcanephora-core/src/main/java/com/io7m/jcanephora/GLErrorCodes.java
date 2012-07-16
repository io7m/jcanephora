package com.io7m.jcanephora;

/**
 * Error code predicates.
 */

public interface GLErrorCodes
{
  /**
   * Return <code>true</code> iff <code>code</code> denotes an invalid
   * operation error code.
   */

  public boolean errorCodeIsInvalidOperation(
    int code);
}
