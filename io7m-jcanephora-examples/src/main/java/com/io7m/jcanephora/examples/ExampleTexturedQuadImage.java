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

package com.io7m.jcanephora.examples;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.io7m.jcanephora.AreaInclusive;
import com.io7m.jcanephora.ArrayAttributeDescriptor;
import com.io7m.jcanephora.ArrayAttributeType;
import com.io7m.jcanephora.ArrayBufferType;
import com.io7m.jcanephora.ArrayBufferUpdateUnmapped;
import com.io7m.jcanephora.ArrayBufferUpdateUnmappedType;
import com.io7m.jcanephora.ArrayDescriptor;
import com.io7m.jcanephora.ArrayDescriptorBuilderType;
import com.io7m.jcanephora.BlendFunction;
import com.io7m.jcanephora.CursorWritable2fType;
import com.io7m.jcanephora.CursorWritable4fType;
import com.io7m.jcanephora.CursorWritableIndexType;
import com.io7m.jcanephora.FragmentShaderType;
import com.io7m.jcanephora.IndexBufferType;
import com.io7m.jcanephora.IndexBufferUpdateUnmapped;
import com.io7m.jcanephora.IndexBufferUpdateUnmappedType;
import com.io7m.jcanephora.JCGLException;
import com.io7m.jcanephora.JCGLScalarType;
import com.io7m.jcanephora.Primitives;
import com.io7m.jcanephora.ProgramAttributeType;
import com.io7m.jcanephora.ProgramType;
import com.io7m.jcanephora.ProgramUniformType;
import com.io7m.jcanephora.ProjectionMatrix;
import com.io7m.jcanephora.Texture2DStaticType;
import com.io7m.jcanephora.TextureFilterMagnification;
import com.io7m.jcanephora.TextureFilterMinification;
import com.io7m.jcanephora.TextureFormat;
import com.io7m.jcanephora.TextureFormatMeta;
import com.io7m.jcanephora.TextureLoaderType;
import com.io7m.jcanephora.TextureUnitType;
import com.io7m.jcanephora.TextureWrapS;
import com.io7m.jcanephora.TextureWrapT;
import com.io7m.jcanephora.UsageHint;
import com.io7m.jcanephora.VertexShaderType;
import com.io7m.jcanephora.api.JCGLImplementationType;
import com.io7m.jcanephora.api.JCGLImplementationVisitorType;
import com.io7m.jcanephora.api.JCGLInterfaceCommonType;
import com.io7m.jcanephora.api.JCGLInterfaceGL2Type;
import com.io7m.jcanephora.api.JCGLInterfaceGL3Type;
import com.io7m.jcanephora.api.JCGLInterfaceGLES2Type;
import com.io7m.jcanephora.api.JCGLInterfaceGLES3Type;
import com.io7m.jcanephora.api.JCGLTextures2DStaticGL3ES3Type;
import com.io7m.jcanephora.utilities.ShaderUtilities;
import com.io7m.jranges.RangeInclusiveL;
import com.io7m.jtensors.MatrixM4x4F;
import com.io7m.jtensors.VectorI2F;
import com.io7m.jtensors.VectorReadable2IType;
import com.io7m.junreachable.UnimplementedCodeException;
import com.io7m.junreachable.UnreachableCodeException;
import com.io7m.jvvfs.FilesystemError;
import com.io7m.jvvfs.FilesystemType;
import com.io7m.jvvfs.PathVirtual;

/**
 * Example program that draws a textured quad to the screen, with an
 * orthographic projection. The program cycles between the same texture but
 * reloaded as all of the separate texture types.
 */

@SuppressWarnings("null") public final class ExampleTexturedQuadImage implements
  Example
{
  private final ArrayBufferType               array;
  private final ArrayBufferUpdateUnmappedType array_data;
  private final ArrayDescriptor               array_type;
  private final ExampleConfig                 config;
  private int                                 frame         = 0;
  private final JCGLInterfaceCommonType       gl;
  private boolean                             has_shut_down;
  private final IndexBufferType               indices;
  private final IndexBufferUpdateUnmappedType indices_data;
  private final MatrixM4x4F                   matrix_modelview;
  private final MatrixM4x4F                   matrix_projection;
  private final ProgramType                   program;
  private int                                 texture_index = 0;
  private final List<TextureUnitType>         texture_units;
  private final Texture2DStaticType           textures[];
  private final JCGLImplementationType        gi;

  public ExampleTexturedQuadImage(
    final ExampleConfig config1)
    throws IOException,
      FilesystemError,
      JCGLException
  {
    this.config = config1;
    this.matrix_modelview = new MatrixM4x4F();
    this.matrix_projection = new MatrixM4x4F();
    this.gi = this.config.getGL();
    this.gl = this.gi.getGLCommon();

    /**
     * Initialize shaders.
     */

    {
      final VertexShaderType v =
        this.gl.vertexShaderCompile(
          "v",
          ShaderUtilities.readLines(config1.getFilesystem().openFile(
            PathVirtual.ofString("/com/io7m/jcanephora/examples/uv.v"))));
      final FragmentShaderType f =
        this.gl.fragmentShaderCompile(
          "f",
          ShaderUtilities.readLines(config1.getFilesystem().openFile(
            PathVirtual.ofString("/com/io7m/jcanephora/examples/uv.f"))));
      this.program = this.gl.programCreateCommon("color", v, f);
    }

    /**
     * Obtain access to the available texture units.
     */

    this.texture_units = this.gl.textureGetUnits();

    /**
     * Load a texture from an image file, one per every supported texture type
     * on the current implementation. Note that real code would usually use
     * one of the convenience functions in the TextureLoader interface that
     * can infer texture types; this is simply an exhaustive example that
     * attempts to load every single type.
     */

    final TextureLoaderType loader = config1.getTextureLoader();
    final FilesystemType filesystem = config1.getFilesystem();

    final TextureWrapS wrap_s = TextureWrapS.TEXTURE_WRAP_CLAMP_TO_EDGE;
    final TextureWrapT wrap_t = TextureWrapT.TEXTURE_WRAP_CLAMP_TO_EDGE;
    final TextureFilterMinification min_filter =
      TextureFilterMinification.TEXTURE_FILTER_LINEAR;
    final TextureFilterMagnification mag_filter =
      TextureFilterMagnification.TEXTURE_FILTER_LINEAR;

    this.textures =
      this.gi
        .implementationAccept(new JCGLImplementationVisitorType<Texture2DStaticType[], IOException>() {

          /**
           * The texture types supported by GL2.
           */

          @Override public Texture2DStaticType[] implementationIsGL2(
            final JCGLInterfaceGL2Type gl2)
            throws JCGLException,
              IOException
          {
            try {
              final EnumSet<TextureFormat> ttypes =
                TextureFormatMeta.getTextures2DRequiredByGL21();
              final Texture2DStaticType[] t =
                new Texture2DStaticType[ttypes.size()];

              int index = 0;
              for (final TextureFormat type : ttypes) {
                System.out.println("Loading texture as " + type);

                final InputStream stream =
                  filesystem.openFile(PathVirtual
                    .ofString("/com/io7m/jcanephora/examples/reference_8888_4.png"));

                switch (type) {
                  case TEXTURE_FORMAT_RGBA_8_4BPP:
                    t[index] =
                      loader.load2DStaticRGBA8(
                        gl2,
                        wrap_s,
                        wrap_t,
                        min_filter,
                        mag_filter,
                        stream,
                        type.toString());
                    break;
                  case TEXTURE_FORMAT_RGB_8_3BPP:
                    t[index] =
                      loader.load2DStaticRGB8(
                        gl2,
                        wrap_s,
                        wrap_t,
                        min_filter,
                        mag_filter,
                        stream,
                        type.toString());
                    break;

                  // $CASES-OMITTED$
                  default:
                    stream.close();
                    throw new UnreachableCodeException(new AssertionError(
                      type.toString()));
                }

                stream.close();
                ++index;
              }

              index = 0;
              for (final TextureFormat type : ttypes) {
                assert (t[index] != null) : type.toString();
                ++index;
              }

              return t;
            } catch (final FilesystemError e) {
              throw new IOException(e);
            }
          }

          /**
           * The texture types supported by GL3.
           */

          @Override public Texture2DStaticType[] implementationIsGL3(
            final JCGLInterfaceGL3Type gl3)
            throws JCGLException,
              IOException
          {
            try {
              return this.texturesGL3ES3(gl3);
            } catch (final FilesystemError e) {
              throw new IOException(e);
            }
          }

          @Override public Texture2DStaticType[] implementationIsGLES2(
            final JCGLInterfaceGLES2Type gles2)
            throws JCGLException,
              IOException
          {
            // TODO Auto-generated method stub
            throw new UnimplementedCodeException();
          }

          @Override public Texture2DStaticType[] implementationIsGLES3(
            final JCGLInterfaceGLES3Type gles3)
            throws JCGLException,
              IOException
          {
            try {
              return this.texturesGL3ES3(gles3);
            } catch (final FilesystemError e) {
              throw new IOException(e);
            }
          }

          private Texture2DStaticType[] texturesGL3ES3(
            final JCGLTextures2DStaticGL3ES3Type gl3es3)
            throws FilesystemError,
              JCGLException,
              IOException
          {
            final Set<TextureFormat> ttypes =
              TextureFormatMeta.getTextures2DRequiredByGL3ES3();
            final Texture2DStaticType[] t =
              new Texture2DStaticType[ttypes.size()];

            int index = 0;
            for (final TextureFormat type : ttypes) {
              System.out.println("Loading texture as " + type);

              final InputStream stream =
                filesystem.openFile(PathVirtual
                  .ofString("/com/io7m/jcanephora/examples/reference_8888_4.png"));

              switch (type) {
                case TEXTURE_FORMAT_RGBA_1010102_4BPP:
                {
                  t[index] =
                    loader.load2DStaticRGBA1010102(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGBA_16F_8BPP:
                {
                  t[index] =
                    loader.load2DStaticRGBA16f(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGBA_16I_8BPP:
                {
                  t[index] =
                    loader.load2DStaticRGBA16I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGBA_16U_8BPP:
                {
                  t[index] =
                    loader.load2DStaticRGBA16U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGBA_16_8BPP:
                {
                  t[index] =
                    loader.load2DStaticRGBA16(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGBA_32I_16BPP:
                {
                  t[index] =
                    loader.load2DStaticRGBA32I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGBA_32U_16BPP:
                {
                  t[index] =
                    loader.load2DStaticRGBA32U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGBA_8I_4BPP:
                {
                  t[index] =
                    loader.load2DStaticRGBA8I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGBA_8U_4BPP:
                {
                  t[index] =
                    loader.load2DStaticRGBA8U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGB_16F_6BPP:
                {
                  t[index] =
                    loader.load2DStaticRGB16f(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGB_16I_6BPP:
                {
                  t[index] =
                    loader.load2DStaticRGB16I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGB_16U_6BPP:
                {
                  t[index] =
                    loader.load2DStaticRGB16U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGB_16_6BPP:
                {
                  t[index] =
                    loader.load2DStaticRGB16(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGB_32F_12BPP:
                {
                  t[index] =
                    loader.load2DStaticRGB32f(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGB_32I_12BPP:
                {
                  t[index] =
                    loader.load2DStaticRGB32I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGB_32U_12BPP:
                {
                  t[index] =
                    loader.load2DStaticRGB32U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGB_8I_3BPP:
                {
                  t[index] =
                    loader.load2DStaticRGB8I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGB_8U_3BPP:
                {
                  t[index] =
                    loader.load2DStaticRGB8U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RG_16F_4BPP:
                {
                  t[index] =
                    loader.load2DStaticRG16f(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RG_16I_4BPP:
                {
                  t[index] =
                    loader.load2DStaticRG16I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RG_16U_4BPP:
                {
                  t[index] =
                    loader.load2DStaticRG16U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RG_16_4BPP:
                {
                  t[index] =
                    loader.load2DStaticRG16(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RG_32F_8BPP:
                {
                  t[index] =
                    loader.load2DStaticRG32f(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RG_32I_8BPP:
                {
                  t[index] =
                    loader.load2DStaticRG32I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RG_32U_8BPP:
                {
                  t[index] =
                    loader.load2DStaticRG32U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RG_8I_2BPP:
                {
                  t[index] =
                    loader.load2DStaticRG8I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RG_8U_2BPP:
                {
                  t[index] =
                    loader.load2DStaticRG8U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_R_32F_4BPP:
                {
                  t[index] =
                    loader.load2DStaticR32f(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_R_32I_4BPP:
                {
                  t[index] =
                    loader.load2DStaticR32I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_R_32U_4BPP:
                {
                  t[index] =
                    loader.load2DStaticR32U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_R_8I_1BPP:
                {
                  t[index] =
                    loader.load2DStaticR8I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_R_8U_1BPP:
                {
                  t[index] =
                    loader.load2DStaticR8U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RG_8_2BPP:
                {
                  t[index] =
                    loader.load2DStaticRG8(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_R_8_1BPP:
                {
                  t[index] =
                    loader.load2DStaticR8(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGBA_32F_16BPP:
                {
                  t[index] =
                    loader.load2DStaticRGBA32f(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGBA_4444_2BPP:
                case TEXTURE_FORMAT_RGBA_5551_2BPP:
                case TEXTURE_FORMAT_RGB_565_2BPP:
                {
                  stream.close();
                  throw new UnreachableCodeException();
                }

                case TEXTURE_FORMAT_DEPTH_32F_4BPP:
                case TEXTURE_FORMAT_DEPTH_24_STENCIL_8_4BPP:
                {
                  System.out.print("Ignoring type " + type);
                  t[index] =
                    loader.load2DStaticR16f(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_R_16_2BPP:
                {
                  t[index] =
                    loader.load2DStaticR16(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_R_16U_2BPP:
                {
                  t[index] =
                    loader.load2DStaticR16U(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_R_16I_2BPP:
                {
                  t[index] =
                    loader.load2DStaticR16I(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_R_16F_2BPP:
                {
                  t[index] =
                    loader.load2DStaticR16f(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_DEPTH_24_4BPP:
                {
                  t[index] =
                    loader.load2DStaticDepth24(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }
                case TEXTURE_FORMAT_DEPTH_16_2BPP:
                {
                  t[index] =
                    loader.load2DStaticDepth16(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGBA_8_4BPP:
                {
                  t[index] =
                    loader.load2DStaticRGBA8(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }

                case TEXTURE_FORMAT_RGB_8_3BPP:
                {
                  t[index] =
                    loader.load2DStaticRGB8(
                      gl3es3,
                      wrap_s,
                      wrap_t,
                      min_filter,
                      mag_filter,
                      stream,
                      type.toString());
                  break;
                }
              }

              assert t[index] != null : type.toString();

              stream.close();
              ++index;
            }

            index = 0;
            for (final TextureFormat type : ttypes) {
              assert (t[index] != null) : type.toString();
              ++index;
            }

            return t;
          }
        });

    /**
     * Allocate an array buffer.
     * 
     * Set up a type descriptor that describes the types of elements within
     * the array. In this case, each element of the array is a series of four
     * floats representing the position of a vertex, followed by a series of
     * two floats representing the texture coordinates of a vertex.
     * 
     * Then, use this descriptor to allocate an array.
     */

    final ArrayDescriptorBuilderType b = ArrayDescriptor.newBuilder();
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "position",
      JCGLScalarType.TYPE_FLOAT,
      4));
    b.addAttribute(ArrayAttributeDescriptor.newAttribute(
      "uv",
      JCGLScalarType.TYPE_FLOAT,
      2));
    this.array_type = b.build();

    this.array =
      this.gl.arrayBufferAllocate(
        4,
        this.array_type,
        UsageHint.USAGE_STATIC_DRAW);

    /**
     * Then, allocate a buffer of data that will be populated and uploaded.
     */

    this.array_data =
      ArrayBufferUpdateUnmapped.newUpdateReplacingAll(this.array);

    {
      /**
       * Obtain typed cursors to the parts of the array to be populated. Note
       * that writes to the two cursors can be interleaved. Each cursor can
       * only point to the parts of the array relevant to their attribute.
       */

      final CursorWritable4fType pos_cursor =
        this.array_data.getCursor4f("position");
      final CursorWritable2fType uv_cursor =
        this.array_data.getCursor2f("uv");

      pos_cursor.put4f(-100.0f, 100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(-100.0f, -100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(100.0f, -100.0f, -1.0f, 1.0f);
      pos_cursor.put4f(100.0f, 100.0f, -1.0f, 1.0f);

      uv_cursor.put2f(0.0f, 1.0f);
      uv_cursor.put2f(0.0f, 0.0f);
      uv_cursor.put2f(1.0f, 0.0f);
      uv_cursor.put2f(1.0f, 1.0f);
    }

    /**
     * Upload the array data.
     */

    this.gl.arrayBufferUpdate(this.array_data);

    /**
     * Allocate and initialize an index buffer sufficient for two triangles.
     */

    this.indices =
      this.gl.indexBufferAllocate(this.array, 6, UsageHint.USAGE_STATIC_DRAW);
    this.indices_data = IndexBufferUpdateUnmapped.newReplacing(this.indices);

    {
      final CursorWritableIndexType ind_cursor =
        this.indices_data.getCursor();
      ind_cursor.putIndex(0);
      ind_cursor.putIndex(1);
      ind_cursor.putIndex(2);

      ind_cursor.putIndex(0);
      ind_cursor.putIndex(2);
      ind_cursor.putIndex(3);
    }

    this.gl.indexBufferUpdate(this.indices_data);
  }

  @Override public void display()
    throws JCGLException
  {
    ++this.frame;

    if ((this.frame % 5) == 0) {
      this.texture_index = (this.texture_index + 1) % this.textures.length;
      this.config.getLog().debug(
        "Selected " + this.textures[this.texture_index]);
    }

    this.gl.colorBufferClear3f(0.0f, 0.2f, 0.3f);

    /**
     * Initialize the projection matrix to an orthographic projection.
     */

    MatrixM4x4F.setIdentity(this.matrix_projection);
    ProjectionMatrix.makeOrthographicProjection(
      this.matrix_projection,
      0,
      640,
      0,
      480,
      1,
      100);

    /**
     * Initialize the modelview matrix, and translate.
     */

    MatrixM4x4F.setIdentity(this.matrix_modelview);
    MatrixM4x4F.translateByVector2FInPlace(
      this.matrix_modelview,
      new VectorI2F(this.config.getWindowSize().getXI() / 2, this.config
        .getWindowSize()
        .getYI() / 2));

    this.gl.blendingEnable(
      BlendFunction.BLEND_SOURCE_ALPHA,
      BlendFunction.BLEND_ONE_MINUS_SOURCE_ALPHA);

    /**
     * Activate shading program, and associate parts of the array buffer with
     * inputs to the shader.
     */

    this.gl.programActivate(this.program);
    {
      /**
       * Get references to the program's uniform variable inputs.
       */

      final ProgramUniformType u_proj =
        this.program.programGetUniforms().get("matrix_projection");
      final ProgramUniformType u_model =
        this.program.programGetUniforms().get("matrix_modelview");
      final ProgramUniformType u_texture =
        this.program.programGetUniforms().get("texture");

      /**
       * Upload the matrices to the uniform variable inputs.
       */

      this.gl.programUniformPutMatrix4x4f(u_proj, this.matrix_projection);
      this.gl.programUniformPutMatrix4x4f(u_model, this.matrix_modelview);

      /**
       * Bind the texture to the first available texture unit, then upload the
       * texture unit reference to the shader.
       */

      this.gl.texture2DStaticBind(
        this.texture_units.get(0),
        this.textures[this.texture_index]);
      this.gl.programUniformPutTextureUnit(
        u_texture,
        this.texture_units.get(0));

      /**
       * Get references to the program's vertex attribute inputs.
       */

      final ProgramAttributeType p_pos =
        this.program.programGetAttributes().get("vertex_position");
      final ProgramAttributeType p_uv =
        this.program.programGetAttributes().get("vertex_uv");

      /**
       * Get references to the array buffer's vertex attributes.
       */

      final ArrayAttributeType b_pos =
        this.array.arrayGetAttribute("position");
      final ArrayAttributeType b_uv = this.array.arrayGetAttribute("uv");

      /**
       * Bind the array buffer, and associate program vertex attribute inputs
       * with array vertex attributes.
       */

      this.gl.arrayBufferBind(this.array);
      this.gl.programAttributeArrayAssociate(p_uv, b_uv);
      this.gl.programAttributeArrayAssociate(p_pos, b_pos);

      /**
       * Draw primitives, using the array buffer and the given index buffer.
       */

      this.gl.drawElements(Primitives.PRIMITIVE_TRIANGLES, this.indices);
      this.gl.programAttributeArrayDisassociate(p_pos);
      this.gl.programAttributeArrayDisassociate(p_uv);
      this.gl.arrayBufferUnbind();
    }
    this.gl.programDeactivate();

    this.gl.blendingDisable();
  }

  @Override public boolean hasShutDown()
  {
    return this.has_shut_down;
  }

  @Override public void reshape(
    final VectorReadable2IType position,
    final VectorReadable2IType size)
    throws JCGLException
  {
    ProjectionMatrix.makeOrthographicProjection(
      this.matrix_projection,
      0,
      size.getXI(),
      0,
      size.getYI(),
      1,
      100);

    final RangeInclusiveL range_x =
      new RangeInclusiveL(position.getXI(), position.getXI()
        + (size.getXI() - 1));
    final RangeInclusiveL range_y =
      new RangeInclusiveL(position.getYI(), position.getYI()
        + (size.getYI() - 1));

    this.gl.viewportSet(new AreaInclusive(range_x, range_y));
  }

  @Override public void shutdown()
    throws JCGLException
  {
    this.has_shut_down = true;
    this.gl.arrayBufferDelete(this.array);
    this.gl.indexBufferDelete(this.indices);

    for (final Texture2DStaticType t : this.textures) {
      this.gl.texture2DStaticDelete(t);
    }

    this.gl.programDelete(this.program);
  }
}
