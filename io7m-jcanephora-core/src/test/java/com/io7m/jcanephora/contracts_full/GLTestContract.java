package com.io7m.jcanephora.contracts_full;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Option;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jlog.Log;

public interface GLTestContract
{
  public Log getLog();

  public Option<GLInterface> makeNewGL()
    throws GLException,
      ConstraintError;
}
