package com.io7m.jcanephora.contracts;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jlog.Log;

public interface GLTestContract
{
  public GLInterface getGL()
    throws GLException,
      ConstraintError;

  public Log getLog();
}
