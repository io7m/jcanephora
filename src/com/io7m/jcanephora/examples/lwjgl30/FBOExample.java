package com.io7m.jcanephora.examples.lwjgl30;

import java.util.Properties;

import javax.annotation.Nonnull;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jcanephora.Framebuffer;
import com.io7m.jcanephora.FramebufferAttachment;
import com.io7m.jcanephora.FramebufferAttachment.ColorAttachment;
import com.io7m.jcanephora.FramebufferAttachment.RenderbufferD24S8Attachment;
import com.io7m.jcanephora.GLException;
import com.io7m.jcanephora.GLInterface;
import com.io7m.jcanephora.GLInterfaceLWJGL30;
import com.io7m.jcanephora.LWJGL30;
import com.io7m.jcanephora.RenderbufferD24S8;
import com.io7m.jcanephora.Texture2DRGBAStatic;
import com.io7m.jcanephora.TextureFilter;
import com.io7m.jcanephora.TextureUnit;
import com.io7m.jcanephora.TextureWrap;
import com.io7m.jlog.Log;
import com.io7m.jtensors.VectorI4F;

public final class FBOExample implements Runnable
{
  private static int SCREEN_WIDTH  = 640;
  private static int SCREEN_HEIGHT = 480;

  public static void main(
    final String args[])
    throws ConstraintError,
      GLException
  {
    LWJGL30.createDisplay(
      "FBOExample",
      FBOExample.SCREEN_WIDTH,
      FBOExample.SCREEN_HEIGHT);

    final Log log = new Log(new Properties(), "com.io7m", "example");
    final GLInterface gl = new GLInterfaceLWJGL30(log);

    try {
      final FBOExample t = new FBOExample(gl);
      t.run();
    } finally {
      Display.destroy();
    }
  }

  private final @Nonnull GLInterface         gl;
  private final @Nonnull Texture2DRGBAStatic texture;
  private final @Nonnull VectorI4F           background;
  private final @Nonnull VectorI4F           texture_background;
  private final @Nonnull TextureUnit[]       units;
  private final @Nonnull Framebuffer         framebuffer;
  private final @Nonnull RenderbufferD24S8   depth_buffer;

  double                                     angle = 0;

  public FBOExample(
    final GLInterface gl)
    throws ConstraintError,
      GLException
  {
    this.gl = gl;
    this.background = new VectorI4F(0.2f, 0.2f, 0.2f, 1.0f);
    this.texture_background = new VectorI4F(0.0f, 0.0f, 1.0f, 1.0f);
    this.units = this.gl.textureGetUnits();

    this.depth_buffer =
      this.gl.renderbufferD24S8Allocate(
        FBOExample.SCREEN_WIDTH,
        FBOExample.SCREEN_HEIGHT);

    this.texture =
      this.gl.texture2DRGBAStaticAllocate(
        "color_buffer",
        FBOExample.SCREEN_WIDTH,
        FBOExample.SCREEN_HEIGHT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureWrap.TEXTURE_WRAP_REPEAT,
        TextureFilter.TEXTURE_FILTER_LINEAR,
        TextureFilter.TEXTURE_FILTER_LINEAR);

    this.framebuffer = this.gl.framebufferAllocate();
    this.gl.framebufferAttachStorage(
      this.framebuffer,
      new FramebufferAttachment[] {
        new ColorAttachment(this.texture, 0),
        new RenderbufferD24S8Attachment(this.depth_buffer) });
  }

  private void render()
    throws GLException,
      ConstraintError
  {
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GL11.glFrustum(-1, 1, -1, 1, 1, 100);

    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity();
    GL11.glTranslated(0, 0, -1);

    /*
     * Render to texture.
     */

    GL11.glDisable(GL11.GL_TEXTURE_2D);

    this.gl.framebufferBind(this.framebuffer);
    {
      this.gl.colorBufferClearV4f(this.texture_background);
      this.gl.depthBufferClear(1.0f);

      this.angle = this.angle + 1;
      this.angle = this.angle % 360.0;

      GL11.glRotated(this.angle, 0, 0, 1);
      GL11.glBegin(GL11.GL_TRIANGLES);
      {
        GL11.glColor3d(1.0, 0.0, 0.0);
        GL11.glVertex3d(0, 1, 0);
        GL11.glVertex3d(0, 0, 0);
        GL11.glVertex3d(1, 0, 0);
      }
      GL11.glEnd();
    }
    this.gl.framebufferUnbind();

    /*
     * Render quad with framebuffer texture.
     */

    GL11.glLoadIdentity();
    GL11.glTranslated(0, 0, -2);
    GL11.glRotated(this.angle, 0, 1, 0);

    this.gl.colorBufferClearV4f(this.background);

    GL11.glEnable(GL11.GL_TEXTURE_2D);
    this.gl.texture2DRGBAStaticBind(this.units[0], this.texture);

    GL11.glBegin(GL11.GL_QUADS);
    {
      GL11.glColor3d(1.0, 1.0, 1.0);
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
