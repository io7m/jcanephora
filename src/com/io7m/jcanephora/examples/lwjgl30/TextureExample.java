package com.io7m.jcanephora.examples.lwjgl30;

import java.nio.ByteBuffer;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLInterfaceLWJGL30;
import com.io7m.jcanephora.GLResource;
import com.io7m.jcanephora.LWJGL30;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.Texture2DRGBAStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jlog.Log;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI4F;

public final class TextureExample implements Runnable, GLResource
{
  private static int SCREEN_WIDTH   = 640;
  private static int SCREEN_HEIGHT  = 480;
  private static int TEXTURE_WIDTH  = 256;
  private static int TEXTURE_HEIGHT = 256;

  public static void main(
    final String args[])
    throws ConstraintError,
      GLException
  {
    LWJGL30.createDisplay("TextureExample", 640, 480);

    final Log log = new Log(new Properties(), "com.io7m", "example");
    final GLInterface gl = new GLInterfaceLWJGL30(log);

    try {
      final TextureExample t = new TextureExample(gl);
      t.run();
    } finally {
      Display.destroy();
    }
  }

  private final @Nonnull GLInterface         gl;
  private final @Nonnull MatrixM4x4F         modelview;
  private final @Nonnull MatrixM4x4F         projection;
  private final @Nonnull Texture2DRGBAStatic texture;
  private final @Nonnull VectorI4F           color;
  private long                               current_frame = 0;

  public TextureExample(
    final GLInterface gl)
    throws ConstraintError,
      GLException
  {
    this.gl = gl;
    this.color = new VectorI4F(0.2f, 0.2f, 0.2f, 1.0f);

    this.modelview = new MatrixM4x4F();
    this.projection = new MatrixM4x4F();

    /*
     * Set up an orthographic projection.
     */

    ProjectionMatrix.makeOrthographic(
      this.projection,
      0,
      TextureExample.SCREEN_WIDTH,
      0,
      TextureExample.SCREEN_HEIGHT,
      1,
      100);

    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadMatrix(MatrixM4x4F.floatBuffer(this.projection));
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadMatrix(MatrixM4x4F.floatBuffer(this.modelview));

    /*
     * Allocate an RGBA texture with the given filter and map modes.
     */

    this.texture =
      this.gl.texture2DRGBAStaticAllocate(
        "Texture",
        TextureExample.TEXTURE_WIDTH,
        TextureExample.TEXTURE_HEIGHT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_NEAREST,
        TextureFilter.TEXTURE_FILTER_NEAREST);
    this.updateTexture();
  }

  private void render()
    throws ConstraintError,
      GLException
  {
    ++this.current_frame;

    if ((this.current_frame % 2) == 0) {
      this.updateTexture();
    }

    this.gl.colorBufferClearV4f(this.color);
    this.gl.depthBufferClear(1.0f);

    GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL13.glActiveTexture(GL13.GL_TEXTURE0);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture.getLocation());

    GL11.glBegin(GL11.GL_QUADS);
    {
      GL11.glVertex3d(0.0, TextureExample.SCREEN_HEIGHT, -1.0);
      GL11.glTexCoord2d(0.0, 1.0);
      GL11.glVertex3d(0.0, 0.0, -1.0);
      GL11.glTexCoord2d(0.0, 0.0);
      GL11.glVertex3d(TextureExample.SCREEN_WIDTH, 0.0, -1.0);
      GL11.glTexCoord2d(1.0, 0.0);
      GL11.glVertex3d(
        TextureExample.SCREEN_WIDTH,
        TextureExample.SCREEN_HEIGHT,
        -1.0);
      GL11.glTexCoord2d(1.0, 1.0);
    }
    GL11.glEnd();

    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    GL11.glDisable(GL11.GL_TEXTURE_2D);
  }

  @Override public void resourceDelete(
    final @Nonnull GLInterface g)
    throws ConstraintError,
      GLException
  {
    this.texture.resourceDelete(g);
  }

  @Override public void run()
  {
    try {
      while (Display.isCloseRequested() == false) {
        this.render();
        Display.update();
        Display.sync(60);
      }
    } catch (final ConstraintError e) {
      e.printStackTrace();
      System.exit(1);
    } catch (final GLException e) {
      e.printStackTrace();
      System.exit(1);
    } finally {
      try {
        this.resourceDelete(this.gl);
      } catch (final GLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (final ConstraintError e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  private void updateTexture()
    throws GLException,
      ConstraintError
  {
    final ByteBuffer map =
      this.gl
        .pixelUnpackBufferMapWrite(this.texture.getBuffer())
        .getByteBuffer();

    try {
      map.rewind();
      assert map.remaining() == (TextureExample.TEXTURE_WIDTH
        * TextureExample.TEXTURE_HEIGHT * 4);

      final byte[] colour = new byte[4];
      for (int y = 0; y < TextureExample.TEXTURE_HEIGHT; ++y) {
        for (int x = 0; x < TextureExample.TEXTURE_WIDTH; ++x) {
          final double r =
            (x / (double) TextureExample.TEXTURE_WIDTH) * 255.0;
          final double g = Math.random() * 255.0;
          final double b =
            (y / (double) TextureExample.TEXTURE_HEIGHT) * 255.0;
          final double a = 0xff;

          colour[0] = (byte) ((int) r & 0xff);
          colour[1] = (byte) ((int) g & 0xff);
          colour[2] = (byte) ((int) b & 0xff);
          colour[3] = (byte) ((int) a & 0xff);
          map.put(colour);
        }
      }

      assert map.remaining() == 0;
    } finally {
      this.gl.pixelUnpackBufferUnmap(this.texture.getBuffer());
    }

    this.gl.texture2DRGBAStaticReplace(this.texture);
  }
}
