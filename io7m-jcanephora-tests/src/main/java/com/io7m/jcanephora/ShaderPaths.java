package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jvvfs.PathVirtual;

public final class ShaderPaths
{
  static final PathVirtual GLSL_110_SHADER_PATH;
  static final PathVirtual GLSL_130_SHADER_PATH;
  static final PathVirtual GLSL_40_SHADER_PATH;
  static final PathVirtual GLSL_ES_100_SHADER_PATH;

  static {
    try {
      GLSL_40_SHADER_PATH =
        new PathVirtual("/com/io7m/jcanephora/shaders/glsl40");
      GLSL_130_SHADER_PATH =
        new PathVirtual("/com/io7m/jcanephora/shaders/glsl130");
      GLSL_110_SHADER_PATH =
        new PathVirtual("/com/io7m/jcanephora/shaders/glsl110");
      GLSL_ES_100_SHADER_PATH =
        new PathVirtual("/com/io7m/jcanephora/shaders/glsles100");
    } catch (final ConstraintError e) {
      e.printStackTrace();
      throw new UnreachableCodeException();
    }
  }

  static @Nonnull PathVirtual getShaderPath(
    final int major,
    @SuppressWarnings("unused") final int minor,
    final boolean es)
  {
    if (es) {
      return ShaderPaths.GLSL_ES_100_SHADER_PATH;
    }

    if (major == 2) {
      return ShaderPaths.GLSL_110_SHADER_PATH;
    }
    if (major == 3) {
      return ShaderPaths.GLSL_130_SHADER_PATH;
    }
    return ShaderPaths.GLSL_40_SHADER_PATH;
  }
}
