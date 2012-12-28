package com.io7m.jcanephora.contracts_ES2;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;
import com.io7m.jlog.Log;

public interface GLES2TestContract
{
  public Log getLog();

  public GLInterfaceES2 makeNewGL()
    throws GLException,
      ConstraintError;
}
