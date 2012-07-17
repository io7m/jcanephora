package com.io7m.jcanephora;

import org.junit.After;
import org.junit.Before;
import org.lwjgl.opengl.Pbuffer;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.contracts.PixelUnpackBufferMapContract;
import com.io7m.jlog.Log;

public final class LWJGL30PixelUnpackBufferMapTest extends
  PixelUnpackBufferMapContract
{
  private Pbuffer buffer;

  @Override public GLInterface getGL()
    throws GLException,
      ConstraintError
  {
    return new GLInterfaceLWJGL30(this.getLog());
  }

  @Override public Log getLog()
  {
    return LWJGL30TestLog.getLog();
  }

  @Before public void setUp()
    throws Exception
  {
    this.buffer = LWJGL30.createOffscreenDisplay(640, 480);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyOffscreenDisplay(this.buffer);
  }
}
