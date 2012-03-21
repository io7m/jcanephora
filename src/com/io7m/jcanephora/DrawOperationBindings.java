package com.io7m.jcanephora;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints;
import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.functional.Pair;

public final class DrawOperationBindings
{
  private final @Nonnull HashMap<ArrayBuffer, LinkedList<Pair<ArrayBufferAttribute, ProgramAttribute>>> bindings;
  private final int                                                                              max_attributes;
  private int                                                                                    used_attributes;

  public DrawOperationBindings(
    final @Nonnull GLInterface gl)
    throws GLException,
      ConstraintError
  {
    Constraints.constrainNotNull(gl, "GL interface");

    this.bindings =
      new HashMap<ArrayBuffer, LinkedList<Pair<ArrayBufferAttribute, ProgramAttribute>>>();
    this.max_attributes = gl.getMaximumActiveAttributes();
    this.used_attributes = 0;
  }

  /**
   * Add a binding for the given <code>buffer</code>,
   * <code>buffer_attribute</code>, and <code>program_attribute</code>, for
   * the given <code>program</code>.
   * 
   * @param gl
   *          The current OpenGL interface.
   * @param program
   *          The relevant program.
   * @param program_attribute
   *          An attribute for the given program.
   * @param buffer
   *          The relevant array buffer.
   * @param buffer_attribute
   *          An attribute contained in the given array buffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>gl == null</code></li>
   *           <li><code>buffer == null</code></li>
   *           <li><code>buffer_attribute == null</code></li>
   *           <li><code>program == null</code></li>
   *           <li><code>program_attribute == null</code></li>
   *           <li>The number of used attributes would exceed the current
   *           OpenGL implementation's maximum.</li>
   *           <li>The given program attribute does not belong to the given
   *           program.</li>
   *           <li>The given array buffer attribute does not belong to the
   *           given array buffer.</li>
   *           <li>The given program attribute has already been added.</li>
   *           <li>The given array buffer attribute has already been added.</li>
   *           </ul>
   */

  public void addBinding(
    final @Nonnull GLInterface gl,
    final @Nonnull ProgramReference program,
    final @Nonnull ProgramAttribute program_attribute,
    final @Nonnull ArrayBuffer buffer,
    final @Nonnull ArrayBufferAttribute buffer_attribute)
    throws ConstraintError
  {
    Constraints.constrainNotNull(gl, "OpenGL interface");
    Constraints.constrainNotNull(buffer, "Array buffer");
    Constraints.constrainNotNull(buffer_attribute, "Array buffer attribute");
    Constraints.constrainNotNull(program, "Program");
    Constraints.constrainNotNull(program_attribute, "Program attribute");
    Constraints.constrainRange(
      this.used_attributes + 1,
      1,
      this.max_attributes,
      "Number of attributes");

    /*
     * Check that the given program attribute does belong to the given
     * program. Pointer equality is sufficient.
     */

    Constraints.constrainArbitrary(
      program_attribute.getProgram() == program,
      "Attribute " + program_attribute + " belongs to program " + program);
    /*
     * Check that the given array buffer attribute does belong to the given
     * array buffer. Pointer equality is sufficient.
     */

    final ArrayBufferDescriptor descriptor = buffer.getDescriptor();
    final ArrayBufferAttribute buffer_attribute_other =
      descriptor.getAttribute(buffer_attribute.getName());

    Constraints.constrainArbitrary(
      buffer_attribute == buffer_attribute_other,
      "Buffer attribute " + buffer_attribute + " belongs to " + buffer);

    /*
     * XXX: Check program attribute type is equivalent to buffer attribute
     * type.
     */

    /*
     * Insert binding, checking for duplicates.
     */

    LinkedList<Pair<ArrayBufferAttribute, ProgramAttribute>> buffer_binds;

    if (this.bindings.containsKey(buffer)) {
      final LinkedList<Pair<ArrayBufferAttribute, ProgramAttribute>> pairs =
        this.bindings.get(buffer);

      for (final Pair<ArrayBufferAttribute, ProgramAttribute> pair : pairs) {
        Constraints.constrainArbitrary(
          pair.first != buffer_attribute,
          "Array buffer attribute not already added");
        Constraints.constrainArbitrary(
          pair.second != program_attribute,
          "Program attribute not already added");
      }

      buffer_binds = pairs;
    } else {
      buffer_binds = new LinkedList<Pair<ArrayBufferAttribute, ProgramAttribute>>();
      this.bindings.put(buffer, buffer_binds);
    }

    assert (buffer_binds != null);
    buffer_binds.add(new Pair<ArrayBufferAttribute, ProgramAttribute>(
      buffer_attribute,
      program_attribute));

    ++this.used_attributes;
  }

  @SuppressWarnings("boxing") public
    List<Pair<ArrayBufferAttribute, ProgramAttribute>>
    getBindings(
      final @Nonnull ArrayBuffer buffer)
      throws ConstraintError
  {
    Constraints.constrainNotNull(buffer, "Buffer is not null");
    Constraints.constrainNotNull(
      this.bindings.containsKey(buffer),
      "Buffer exists");
    return Collections.unmodifiableList(this.bindings.get(buffer));
  }

  public Set<ArrayBuffer> getBuffers()
  {
    return Collections.unmodifiableSet(this.bindings.keySet());
  }
}
