package com.io7m.jcanephora;

/**
 * Hints to the implementation about how array data will be used.
 */

public enum UsageHint
{
  USAGE_STREAM_DRAW,
  USAGE_STREAM_READ,
  USAGE_STREAM_COPY,
  USAGE_STATIC_DRAW,
  USAGE_STATIC_READ,
  USAGE_STATIC_COPY,
  USAGE_DYNAMIC_DRAW,
  USAGE_DYNAMIC_READ,
  USAGE_DYNAMIC_COPY
}
