package com.io7m.jcanephora.contracts_ES2;

import junit.framework.Assert;

import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceES2;

public abstract class MetaContract implements GLES2TestContract
{
  @Test public void testMetaStrings()
    throws GLException,
      ConstraintError
  {
    final GLInterfaceES2 gl = this.makeNewGL();

    final String vn = gl.metaGetVendor();
    final String vr = gl.metaGetVersion();
    final String r = gl.metaGetRenderer();

    Assert.assertNotNull(vn);
    Assert.assertNotNull(vr);
    Assert.assertNotNull(r);

    System.out.println("Vendor   : " + vn);
    System.out.println("Version  : " + vr);
    System.out.println("Renderer : " + r);
  }
}
