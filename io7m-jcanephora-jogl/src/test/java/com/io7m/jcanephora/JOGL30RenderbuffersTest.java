package com.io7m.jcanephora;

import javax.media.opengl.GLContext;

import org.junit.After;
import org.junit.Before;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.FramebuffersContract;
import com.io7m.jlog.Log;

public final class JOGL30RenderbuffersTest extends FramebuffersContract
{
  private GLContext context;

  @Override public GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    return new GLInterfaceJOGL30(this.context, JOGL30TestLog.getLog());
  }

  @Override public Log getLog()
  {
    return JOGL30TestLog.getLog();
  }

  @Before public void setUp()
    throws Exception
  {
    this.context = JOGL30.createOffscreenDisplay(640, 480);
  }

  @After public void tearDown()
    throws Exception
  {
    JOGL30.destroyDisplay(this.context);
  }
}
