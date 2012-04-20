package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Simplified interface to framebuffers.
 */

public interface GLFramebuffers
{
  /**
   * Allocate a framebuffer.
   * 
   * @return A freshly allocated framebuffer.
   * @throws ConstraintError
   *           Iff an internal constraint is violated.
   * @throws GLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull Framebuffer allocateFramebuffer()
    throws ConstraintError,
      GLException;

  /**
   * Allocate a renderbuffer that will be used as storage for a depth buffer.
   * 
   * @return A freshly allocated renderbuffer.
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>1 <= width <= Integer.MAX_VALUE == false</code></li>
   *           <li><code>1 <= height <= Integer.MAX_VALUE == false</code></li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL exception occurs.
   */

  @Nonnull RenderbufferDepth allocateRenderbufferDepth(
    final int width,
    final int height)
    throws ConstraintError,
      GLException;

  /**
   * Attach a set of buffers to the given framebuffer. Each element of
   * <code>attachments</code> specifies the type of buffer (color, depth,
   * etc), and possibly the attachment location (for color buffers).
   * 
   * @throws ConstraintError
   *           Iff any of the following hold:
   *           <ul>
   *           <li><code>buffer == null</code></li>
   *           <li><code>attachments == null</code></li>
   *           <li><code>∃n. attachments[n] == null</code></li>
   *           <li>Multiple depth buffers are specified.</li>
   *           <li>The number of specified color buffers is larger than an
   *           implementation-defined maximum.</li>
   *           </ul>
   * @throws GLException
   *           Iff an OpenGL exception occurs.
   */

  void attachFramebufferStorage(
    final @Nonnull Framebuffer buffer,
    final @Nonnull FramebufferAttachment[] attachments)
    throws ConstraintError,
      GLException;

  /**
   * Bind the given framebuffer as the current framebuffer.
   * 
   * @param buffer
   *          The framebuffer.
   * @throws ConstraintError
   *           Iff <code>buffer == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void bindFramebuffer(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException;

  /**
   * Delete the framebuffer specified by <code>buffer</code>.
   * 
   * @param buffer
   *          The buffer.
   * @throws ConstraintError
   *           Iff <code>buffer == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void deleteFramebuffer(
    final @Nonnull Framebuffer buffer)
    throws ConstraintError,
      GLException;

  /**
   * Delete the depth renderbuffer specified by <code>buffer</code>.
   * 
   * @param buffer
   *          The buffer.
   * @throws ConstraintError
   *           Iff <code>buffer == null</code>.
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void deleteRenderbufferDepth(
    final @Nonnull RenderbufferDepth buffer)
    throws ConstraintError,
      GLException;

  /**
   * Unbind the current framebuffer (if any).
   * 
   * @throws GLException
   *           Iff an OpenGL error occurs.
   */

  void unbindFramebuffer()
    throws GLException;
}
