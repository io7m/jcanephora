package com.io7m.jcanephora.examples.lwjgl30;

import java.util.Properties;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.Display;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLInterfaceLWJGL30;
import com.io7m.jcanephora.LWJGL30;
import com.io7m.jcanephora.PixelUnpackBufferCursorWritable4b;
import com.io7m.jcanephora.PixelUnpackBufferWritableMap;
import com.io7m.jcanephora.Texture2DRGBA;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jlog.Log;

public final class TextureExampleSimple
{
  private static int SCREEN_WIDTH   = 640;
  private static int SCREEN_HEIGHT  = 480;
  private static int TEXTURE_WIDTH  = 4;
  private static int TEXTURE_HEIGHT = 4;

  @SuppressWarnings("unused") public static void main(
    final String args[])
    throws ConstraintError,
      Error,
      GLException
  {
    LWJGL30.createDisplay(
      "TextureExample",
      TextureExampleSimple.SCREEN_WIDTH,
      TextureExampleSimple.SCREEN_HEIGHT);

    final Log log = new Log(new Properties(), "com.io7m", "example");
    final GLInterface gl = new GLInterfaceLWJGL30(log);

    try {
      new TextureExampleSimple(gl);
    } finally {
      Display.destroy();
    }
  }

  private final @Nonnull GLInterface gl;
  private final Texture2DRGBA        texture;

  public TextureExampleSimple(
    final GLInterface gl)
    throws ConstraintError,
      GLException
  {
    this.gl = gl;

    /*
     * Allocate an RGBA texture with the given filter and map modes.
     */

    this.texture =
      this.gl.allocateTextureRGBA(
        "Texture",
        TextureExampleSimple.TEXTURE_WIDTH,
        TextureExampleSimple.TEXTURE_HEIGHT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);

    /*
     * Textures are automatically backed by a pixel unpack buffer. This buffer
     * is mapped into the program's address space in order to be populated
     * with bitmap data. A cursor is obtained in order to safely insert four
     * values of type 'byte' for each element.
     */

    {
      final PixelUnpackBufferWritableMap map =
        this.gl.mapPixelUnpackBufferWrite(this.texture.getBuffer());
      final PixelUnpackBufferCursorWritable4b c = map.getCursor4b();

      for (int y = 0; y < 4; ++y) {
        final double fy = y;
        for (int x = 0; x < 4; ++x) {
          final double fx = x;
          c.put4b(
            (byte) (256.0 * (fx / 4.0)),
            (byte) (256.0 * (fy / 4.0)),
            (byte) 0xff,
            (byte) 0xff);
        }
      }

      assert c.hasNext() == false;
      this.gl.unmapPixelUnpackBuffer(this.texture.getBuffer());
    }

    /*
     * Copy data from the pixel buffer into the texture.
     */

    this.gl.updateTexture2DRGBA(this.texture);
  }
}
