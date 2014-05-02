/*
 * Copyright © 2013 <code@io7m.com> http://io7m.com
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jcanephora;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;

/**
 * Functions for loading OpenGL textures from image files/streams.
 */

public interface TextureLoader
{
  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_DEPTH_16_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticDepth16(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_DEPTH_24_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticDepth24(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_DEPTH_32F_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticDepth32f(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Attempt to load an OpenGL texture from the stream <code>stream</code>.
   * The texture will be constructed using the given parameters, and named
   * <code>name</code>. The resulting texture will be of a type appropriate to
   * the original image data.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticInferred(
    final  JCGLImplementation gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException,
      JCGLException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_R_16_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticR16(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_R_16F_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticR16f(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_R_16I_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticR16I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_R_16U_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticR16U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_R_32F_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticR32f(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_R_32I_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticR32I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_R_32U_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticR32U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_R_8_1BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticR8(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_R_8I_1BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticR8I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_R_8U_1BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticR8U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RG_16_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRG16(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RG_16F_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRG16f(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RG_16I_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRG16I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RG_16U_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRG16U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RG_32F_8BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRG32f(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RG_32I_8BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRG32I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RG_32U_8BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRG32U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RG_8_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRG8(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RG_8I_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRG8I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RG_8U_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRG8U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_16_6BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGB16(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_16F_6BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGB16f(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_16I_6BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGB16I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_16U_6BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGB16U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_32F_12BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGB32f(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_32I_12BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGB32I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_32U_12BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGB32U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_565_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGB565(
    final  JCGLTextures2DStaticGLES2 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_8_3BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGB8(
    final  JCGLTextures2DStaticGL2ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_8I_3BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGB8I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_8U_3BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGB8U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_1010102_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA1010102(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_16_8BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA16(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_16F_8BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA16f(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_16I_8BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA16I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_16U_8BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA16U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_32F_16BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA32f(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_32I_16BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA32I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_32U_16BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA32U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_4444_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA4444(
    final  JCGLTextures2DStaticGLES2 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_5551_2BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA5551(
    final  JCGLTextures2DStaticGLES2 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_8_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA8(
    final  JCGLTextures2DStaticGL2ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_8I_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA8I(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL texture from the stream <code>stream</code>. The texture
   * will be constructed using the given parameters, and named
   * <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_8U_4BPP}.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param stream
   *          The input stream from which to read image data.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  Texture2DStatic load2DStaticRGBA8U(
    final  JCGLTextures2DStaticGL3ES3 gl,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  InputStream stream,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL cube texture from the six streams <code>positive_z</code>,
   * <code>negative_z</code>, <code>positive_y</code>, <code>negative_x</code>
   * , and <code>positive_x</code>, <code>negative_y</code>. The texture will
   * be constructed using the given parameters, and named <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of a type that most closely matches the
   * original data, depending on what types the current OpenGL implementation
   * supports.
   * </p>
   * <p>
   * The given texture images will be mapped to the faces corresponding to
   * OpenGL's default left-handed cube map coordinate system.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  TextureCubeStatic loadCubeLHStaticInferred(
    final  JCGLImplementation gl,
    final  TextureWrapR wrap_r,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final  CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final  CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final  CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final  CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final  CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final  String name)
    throws ConstraintError,
      IOException,
      JCGLException;

  /**
   * <p>
   * Load an OpenGL cube texture from the six streams <code>positive_z</code>,
   * <code>negative_z</code>, <code>positive_y</code>, <code>negative_x</code>
   * , and <code>positive_x</code>, <code>negative_y</code>. The texture will
   * be constructed using the given parameters, and named <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_8_3BPP}.
   * </p>
   * <p>
   * The given texture images will be mapped to the faces corresponding to
   * OpenGL's default left-handed cube map coordinate system.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  TextureCubeStatic loadCubeLHStaticRGB8(
    final  JCGLTexturesCubeStaticGL2ES3 gl,
    final  TextureWrapR wrap_r,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final  CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final  CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final  CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final  CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final  CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL cube texture from the six streams <code>positive_z</code>,
   * <code>negative_z</code>, <code>positive_y</code>, <code>negative_x</code>
   * , and <code>positive_x</code>, <code>negative_y</code>. The texture will
   * be constructed using the given parameters, and named <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_8_4BPP}.
   * </p>
   * <p>
   * The given texture images will be mapped to the faces corresponding to
   * OpenGL's default left-handed cube map coordinate system.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  TextureCubeStatic loadCubeLHStaticRGBA8(
    final  JCGLTexturesCubeStaticGL2ES3 gl,
    final  TextureWrapR wrap_r,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final  CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final  CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final  CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final  CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final  CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL cube texture from the six streams <code>positive_z</code>,
   * <code>negative_z</code>, <code>positive_y</code>, <code>negative_x</code>
   * , and <code>positive_x</code>, <code>negative_y</code>. The texture will
   * be constructed using the given parameters, and named <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of a type that most closely matches the
   * original data, depending on what types the current OpenGL implementation
   * supports.
   * </p>
   * <p>
   * The given texture images will be mapped to the faces corresponding to
   * OpenGL's right-handed world-space coordinate system.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  TextureCubeStatic loadCubeRHStaticInferred(
    final  JCGLImplementation gl,
    final  TextureWrapR wrap_r,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final  CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final  CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final  CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final  CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final  CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final  String name)
    throws ConstraintError,
      IOException,
      JCGLException;

  /**
   * <p>
   * Load an OpenGL cube texture from the six streams <code>positive_z</code>,
   * <code>negative_z</code>, <code>positive_y</code>, <code>negative_x</code>
   * , and <code>positive_x</code>, <code>negative_y</code>. The texture will
   * be constructed using the given parameters, and named <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGB_8_3BPP}.
   * </p>
   * <p>
   * The given texture images will be mapped to the faces corresponding to
   * OpenGL's right-handed world-space coordinate system.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  TextureCubeStatic loadCubeRHStaticRGB8(
    final  JCGLTexturesCubeStaticGL2ES3 gl,
    final  TextureWrapR wrap_r,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final  CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final  CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final  CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final  CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final  CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;

  /**
   * <p>
   * Load an OpenGL cube texture from the six streams <code>positive_z</code>,
   * <code>negative_z</code>, <code>positive_y</code>, <code>negative_x</code>
   * , and <code>positive_x</code>, <code>negative_y</code>. The texture will
   * be constructed using the given parameters, and named <code>name</code>.
   * </p>
   * <p>
   * The resulting texture will be of type
   * {@link TextureFormat#TEXTURE_TYPE_RGBA_8_4BPP}.
   * </p>
   * <p>
   * The given texture images will be mapped to the faces corresponding to
   * OpenGL's right-handed world-space coordinate system.
   * </p>
   * 
   * @param gl
   *          The OpenGL interface to use.
   * @param wrap_s
   *          The texture wrapping mode on the S axis.
   * @param wrap_t
   *          The texture wrapping mode on the T axis.
   * @param min_filter
   *          The minification filter to use.
   * @param mag_filter
   *          The magnification filter to use.
   * @param name
   *          The name of the resulting texture.
   * 
   * @throws ConstraintError
   *           Iff any of the parameters are <code>null</code>.
   * @throws JCGLRuntimeException
   *           Iff an internal OpenGL error occurs.
   * @throws IOException
   *           Iff an I/O error occurs whilst reading the image, or the image
   *           is of an unreadable format.
   */

  public  TextureCubeStatic loadCubeRHStaticRGBA8(
    final  JCGLTexturesCubeStaticGL2ES3 gl,
    final  TextureWrapR wrap_r,
    final  TextureWrapS wrap_s,
    final  TextureWrapT wrap_t,
    final  TextureFilterMinification min_filter,
    final  TextureFilterMagnification mag_filter,
    final  CubeMapFaceInputStream<CMFPositiveZKind> positive_z,
    final  CubeMapFaceInputStream<CMFNegativeZKind> negative_z,
    final  CubeMapFaceInputStream<CMFPositiveYKind> positive_y,
    final  CubeMapFaceInputStream<CMFNegativeYKind> negative_y,
    final  CubeMapFaceInputStream<CMFPositiveXKind> positive_x,
    final  CubeMapFaceInputStream<CMFNegativeXKind> negative_x,
    final  String name)
    throws ConstraintError,
      JCGLRuntimeException,
      IOException;
}
