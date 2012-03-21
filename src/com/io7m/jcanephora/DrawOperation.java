package com.io7m.jcanephora;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Pair;

/**
 * An immutable description of a drawing operation.
 */

@Immutable public final class DrawOperation
{
  private final @Nonnull DrawOperationBindings bindings;

  public DrawOperation(
    final @Nonnull DrawOperationBindings bindings)
    throws ConstraintError
  {
    this.bindings = Constraints.constrainNotNull(bindings, "Bindings");
  }

  public void draw(
    final @Nonnull GLInterface gl,
    final @Nonnull Primitives mode,
    final @Nonnull IndexBuffer indices)
    throws ConstraintError,
      GLException
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(indices, "Index buffer");

    for (final ArrayBuffer buffer : this.bindings.getBuffers()) {
      gl.bindArrayBuffer(buffer);
      for (final Pair<ArrayBufferAttribute, ProgramAttribute> p : this.bindings
        .getBindings(buffer)) {
        gl.bindVertexAttributeArrayForBuffer(buffer, p.first, p.second);
      }
    }

    gl.drawElements(mode, indices);

    for (final ArrayBuffer buffer : this.bindings.getBuffers()) {
      for (final Pair<ArrayBufferAttribute, ProgramAttribute> p : this.bindings
        .getBindings(buffer)) {
        gl.unbindVertexAttributeArrayForBuffer(buffer, p.first, p.second);
      }
    }

    gl.unbindArrayBuffer();
  }
}
