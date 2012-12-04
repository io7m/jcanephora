package com.io7m.jcanephora.contracts_embedded;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterfaceEmbedded;
import com.io7m.jcanephora.Texture2DStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureLoader;
import com.io7m.jcanephora.TextureType;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jvvfs.FilesystemAPI;
import com.io7m.jvvfs.FilesystemError;

public abstract class TextureLoaderContract implements
  TextureLoaderTestContract,
  FilesystemTestContract
{
  /**
   * Created textures have the correct types.
   * 
   * @throws ConstraintError
   * @throws GLException
   * @throws FilesystemError
   * @throws IOException
   */

  @Test public final void testSpecificTypes()
    throws GLException,
      ConstraintError,
      FilesystemError,
      IOException
  {
    final GLInterfaceEmbedded gl = this.makeNewGL();
    final FilesystemAPI fs = this.makeNewFS();
    final TextureLoader loader = this.makeTextureLoader();

    for (final TextureType type : TextureType.values()) {
      final InputStream stream = fs.openFile("/com/io7m/jcanephora/32.png");
      final Texture2DStatic texture =
        loader.load2DStaticSpecific(
          gl,
          type,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureWrap.TEXTURE_WRAP_REPEAT,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          TextureFilter.TEXTURE_FILTER_NEAREST,
          stream,
          type.toString());

      Assert.assertFalse(texture.resourceIsDeleted());
      Assert.assertEquals(32, texture.getWidth());
      Assert.assertEquals(32, texture.getHeight());
      Assert.assertEquals(type, texture.getType());
      Assert.assertEquals(type.toString(), texture.getName());
      texture.resourceDelete(gl);
      Assert.assertTrue(texture.resourceIsDeleted());
      stream.close();
    }
  }
}
