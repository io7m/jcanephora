package com.io7m.jcanephora.examples;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLCompileException;
import com.io7m.jcanephora.GLException;
import com.io7m.jtensors.VectorReadable2I;

public interface Example
{
  void display()
    throws GLException,
      ConstraintError,
      GLCompileException;

  boolean hasShutDown();

  void reshape(
    final @Nonnull VectorReadable2I position,
    final @Nonnull VectorReadable2I size)
    throws GLException,
      ConstraintError,
      GLCompileException;

  void shutdown()
    throws GLException,
      ConstraintError,
      GLCompileException;
}
