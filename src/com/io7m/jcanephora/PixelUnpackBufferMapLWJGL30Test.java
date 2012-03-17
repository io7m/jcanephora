package com.io7m.jcanephora;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PixelUnpackBufferMapLWJGL30Test
{
  @Before public void setUp()
    throws Exception
  {
    LWJGL30.createDisplay("PixelUnpackBufferMapTest", 1, 1);
  }

  @After public void tearDown()
    throws Exception
  {
    LWJGL30.destroyDisplay();
  }

  @Test public void test()
  {

  }
}
