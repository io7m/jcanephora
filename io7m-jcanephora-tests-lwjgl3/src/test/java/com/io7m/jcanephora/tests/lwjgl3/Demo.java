package com.io7m.jcanephora.tests.lwjgl3;

import com.io7m.jcanephora.core.api.JCGLContextType;
import com.io7m.jcanephora.lwjgl3.JCGLImplementationLWJGL3;
import com.io7m.jcanephora.lwjgl3.JCGLImplementationLWJGL3Type;
import com.io7m.jfunctional.Pair;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import java.util.concurrent.TimeUnit;

public final class Demo
{
  private Demo()
  {

  }

  public static void main(final String[] args)
    throws Exception
  {
    GLFWErrorCallback.createPrint(System.err).set();

    if (!GLFW.glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    GLFW.glfwDefaultWindowHints();
    GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
    GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
    GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
    GLFW.glfwWindowHint(
      GLFW.GLFW_OPENGL_PROFILE,
      GLFW.GLFW_OPENGL_CORE_PROFILE);

    final long master_context =
      GLFW.glfwCreateWindow(
        640,
        480,
        "LWJGL3",
        MemoryUtil.NULL,
        MemoryUtil.NULL);
    GLFW.glfwMakeContextCurrent(master_context);
    GL.createCapabilities();
    GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);

    final long slave_context =
      GLFW.glfwCreateWindow(2, 2, "LWJGL3", MemoryUtil.NULL, master_context);
    GLFW.glfwMakeContextCurrent(slave_context);
    GL.createCapabilities();
    GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);

    GLFW.glfwMakeContextCurrent(master_context);
    GL11.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

    System.out.println("Vendor:   " + GL11.glGetString(GL11.GL_VENDOR));
    System.out.println("Version:  " + GL11.glGetString(GL11.GL_VERSION));
    System.out.println("Renderer: " + GL11.glGetString(GL11.GL_RENDERER));

    GLFW.glfwSwapBuffers(master_context);
    GLFW.glfwShowWindow(master_context);
    GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);

    GLFW.glfwMakeContextCurrent(slave_context);
    GL11.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

    System.out.println("Vendor:   " + GL11.glGetString(GL11.GL_VENDOR));
    System.out.println("Version:  " + GL11.glGetString(GL11.GL_VERSION));
    System.out.println("Renderer: " + GL11.glGetString(GL11.GL_RENDERER));

    GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);

    final JCGLImplementationLWJGL3Type imp =
      JCGLImplementationLWJGL3.getInstance();

    final Pair<JCGLContextType, JCGLContextType> contexts =
      imp.newSharedContextsFrom(
        master_context,
        "Master",
        slave_context,
        "Slave");

    final JCGLContextType ctx_master = contexts.getLeft();
    final JCGLContextType ctx_slave = contexts.getRight();

    System.out.println("Master:         " + ctx_master.contextGetName());
    System.out.println("Master current: " + ctx_master.contextIsCurrent());
    System.out.println("Slave:          " + ctx_slave.contextGetName());
    System.out.println("Slave current:  " + ctx_slave.contextIsCurrent());

    ctx_master.contextDestroy();
    ctx_slave.contextDestroy();

    TimeUnit.SECONDS.sleep(2L);
  }
}
