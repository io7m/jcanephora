package com.io7m.jcanephora.contracts_embedded;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceEmbedded;
import com.io7m.jlog.Log;

public interface GLEmbeddedTestContract
{
  public Log getLog();

  public GLInterfaceEmbedded makeNewGL()
    throws GLException,
      ConstraintError;
}
