package com.io7m.jcanephora.examples.lwjgl30;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLInterfaceLWJGL30;
import com.io7m.jcanephora.LWJGL30;
import com.io7m.jcanephora.Texture2DRGBAStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jlog.Log;
import com.io7m.jtensors.VectorI4F;

public final class TextureExampleSimple implements Runnable
{
  private static int SCREEN_WIDTH  = 640;
  private static int SCREEN_HEIGHT = 480;

  public static void main(
    final String args[])
    throws ConstraintError,
      GLException,
      FileNotFoundException,
      IOException
  {
    LWJGL30.createDisplay(
      "TextureExample",
      TextureExampleSimple.SCREEN_WIDTH,
      TextureExampleSimple.SCREEN_HEIGHT);

    final Log log = new Log(new Properties(), "com.io7m", "example");
    final GLInterface gl = new GLInterfaceLWJGL30(log);

    try {
      final TextureExampleSimple t = new TextureExampleSimple(gl);
      t.run();
    } finally {
      Display.destroy();
    }
  }

  private final @Nonnull GLInterface         gl;
  private final @Nonnull Texture2DRGBAStatic texture;
  private final @Nonnull VectorI4F           background;
  private final @Nonnull TextureUnit[]       units;

  public TextureExampleSimple(
    final GLInterface gl)
    throws ConstraintError,
      GLException,
      FileNotFoundException,
      IOException
  {
    this.gl = gl;
    this.background = new VectorI4F(0.2f, 0.2f, 0.2f, 1.0f);

    /*
     * Obtain the available texture units. The number of units is an
     * implementation-defined value but is currently usually 16 or 32.
     */

    this.units = this.gl.textureGetUnits();
    assert this.units.length > 0;

    /*
     * Read an RGBA image directly from a file.
     */

    final FileInputStream stream = new FileInputStream("doc/32.png");
    final BufferedImage image = ImageIO.read(stream);
    this.texture =
      Texture2DRGBAStatic.loadImage(
        "32.tiff",
        image,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        gl);
  }

  private void render()
    throws GLException,
      ConstraintError
  {
    this.gl.colorBufferClear(this.background);

    GL11.glEnable(GL11.GL_TEXTURE_2D);

    /*
     * Bind the texture to the first texture unit.
     */

    this.gl.texture2DRGBAStaticBind(this.units[0], this.texture);

    GL11.glBegin(GL11.GL_QUADS);
    {
      GL11.glTexCoord2d(0.0, 1.0);
      GL11.glVertex3d(-0.5, 0.5, 0.0);
      GL11.glTexCoord2d(0.0, 0.0);
      GL11.glVertex3d(-0.5, -0.5, 0.0);
      GL11.glTexCoord2d(1.0, 0.0);
      GL11.glVertex3d(0.5, -0.5, 0.0);
      GL11.glTexCoord2d(1.0, 1.0);
      GL11.glVertex3d(0.5, 0.5, 0.0);
    }
    GL11.glEnd();
  }

  @Override public void run()
  {
    try {
      while (Display.isCloseRequested() == false) {
        this.render();
        Display.update();
        Display.sync(60);
      }
    } catch (final GLException e) {
      e.printStackTrace();
    } catch (final ConstraintError e) {
      e.printStackTrace();
    } finally {
      Display.destroy();
    }
  }
}
