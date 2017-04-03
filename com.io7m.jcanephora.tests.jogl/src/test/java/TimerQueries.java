import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.DebugGL3;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLProfile;

import java.nio.IntBuffer;
import java.nio.LongBuffer;

public final class TimerQueries
{
  private TimerQueries()
  {

  }

  public static void main(final String[] args)
  {
    boolean done = false;

    final GLProfile pro = GLProfile.get(GLProfile.GL3);
    final GLCapabilities caps = new GLCapabilities(pro);
    final GLWindow win = GLWindow.create(caps);
    win.setVisible(true);

    final GLContext ctx = win.getContext();
    ctx.makeCurrent();

    final IntBuffer timers = Buffers.newDirectIntBuffer(2);
    final IntBuffer dbuf = Buffers.newDirectIntBuffer(1);
    final LongBuffer lbuf = Buffers.newDirectLongBuffer(1);

    final GL3 gl = new DebugGL3((GL3) win.getGL());
    gl.glGenQueries(2, timers);

    gl.glQueryCounter(timers.get(0), GL3.GL_TIMESTAMP);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    gl.glQueryCounter(timers.get(1), GL3.GL_TIMESTAMP);

    while (!done) {
      done = true;
      for (int index = 0; index < timers.capacity(); ++index) {
        gl.glGetQueryObjectiv(
          timers.get(0), GL3.GL_QUERY_RESULT_AVAILABLE, dbuf);
        done = done && dbuf.get(0) == 1;
        gl.glGetQueryObjectiv(
          timers.get(1), GL3.GL_QUERY_RESULT_AVAILABLE, dbuf);
        done = done && dbuf.get(0) == 1;
      }
    }

    final long time_start;
    final long time_end;
    final long time;

    gl.glGetQueryObjecti64v(timers.get(0), GL3.GL_QUERY_RESULT, lbuf);
    time_start = lbuf.get(0);
    gl.glGetQueryObjecti64v(timers.get(1), GL3.GL_QUERY_RESULT, lbuf);
    time_end = lbuf.get(0);

    time = time_end - time_start;
    System.out.printf("timer: %fms\n", Double.valueOf(time / 1000000.0));

    ctx.release();
    win.destroy();
  }
}
