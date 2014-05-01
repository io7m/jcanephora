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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.io7m.jaux.Constraints.ConstraintError;
import com.io7m.jaux.UnreachableCodeException;
import com.io7m.jcanephora.utilities.ShaderUtilities;

final class TextureTypeGeneration
{
  enum Type
  {
    FLOAT,
    SIGNED,
    UNSIGNED,
    UNSIGNED_NORMALIZED_FIXED
  }

  private static void depthType(
    final  StringBuilder s,
    final int component_count,
    final int component_size,
    final  Type t)
  {
    final int bytes_per_pixel = (component_count * component_size) / 8;

    s.setLength(0);
    s.append("TEXTURE_TYPE_DEPTH_");
    if ((component_size == 32) && (t != Type.FLOAT)) {
      s.append(24);
    } else {
      s.append(component_size);
    }

    switch (t) {
      case FLOAT:
        s.append("F");
        break;
      case SIGNED:
      case UNSIGNED:
      case UNSIGNED_NORMALIZED_FIXED:
        break;
    }

    s.append("_");
    s.append(bytes_per_pixel);
    s.append("BPP");

    s.append("(PixelType.");
    switch (component_size) {
      case 8:
        throw new UnreachableCodeException();
      case 16:
        s.append(PixelType.PIXEL_COMPONENT_UNSIGNED_SHORT);
        break;
      case 24:
        s.append(PixelType.PIXEL_COMPONENT_UNSIGNED_INT);
        break;
      case 32:
        switch (t) {
          case FLOAT:
            s.append(PixelType.PIXEL_COMPONENT_FLOAT);
            break;
          case SIGNED:
          case UNSIGNED:
          case UNSIGNED_NORMALIZED_FIXED:
            s.append(PixelType.PIXEL_COMPONENT_UNSIGNED_INT);
            break;
        }
        break;
    }

    s.append(", 1, ");
    s.append(bytes_per_pixel);
    s.append(")");
  }

  private static List<String> depthTypes()
  {
    final ArrayList<String> types = new ArrayList<String>();
    final StringBuilder s = new StringBuilder();

    TextureTypeGeneration.depthType(s, 1, 16, Type.UNSIGNED_NORMALIZED_FIXED);
    types.add(s.toString());

    TextureTypeGeneration.depthType(s, 1, 32, Type.UNSIGNED_NORMALIZED_FIXED);
    types.add(s.toString());

    TextureTypeGeneration.depthType(s, 1, 32, Type.FLOAT);
    types.add(s.toString());

    return types;
  }

  public static void main(
    final String args[])
    throws IOException,
      ConstraintError
  {
    if (args.length != 1) {
      throw new IllegalArgumentException("Expected a filename");
    }

    final File file = new File(args[0]);
    final PrintStream stream = new PrintStream(new FileOutputStream(file));

    try {
      final List<String> license =
        ShaderUtilities.readLines(TextureTypeGeneration.class
          .getResourceAsStream("/com/io7m/jcanephora/LICENSE-java.txt"));

      for (final String line : license) {
        stream.print(line);
      }

      stream.println();
      stream.println("package com.io7m.jcanephora;");
      stream.println();
      stream.println("/* Auto generated, do not edit! */");
      stream.println();
      stream.println("/** Texture types */");
      stream.println();
      stream.println("public final enum TextureType");
      stream.println("{");

      for (final String s : TextureTypeGeneration.unpackedColourTypes()) {
        stream.println("  " + s + ",");
      }

      for (final String s : TextureTypeGeneration.packedColourTypes()) {
        stream.println("  " + s + ",");
      }

      for (final String s : TextureTypeGeneration.depthTypes()) {
        stream.println("  " + s + ",");
      }

      stream.println("  ;");
      stream.println();

      final List<String> rest =
        ShaderUtilities.readLines(TextureTypeGeneration.class
          .getResourceAsStream("/com/io7m/jcanephora/TextureType.txt"));

      for (final String line : rest) {
        stream.print(line);
      }

    } finally {
      stream.flush();
      stream.close();
    }
  }

  private static void packedColourType(
    final  StringBuilder s,
    final  PixelType type,
    final int component_count,
    final int bytes_per_pixel)
  {
    s.setLength(0);
    s.append("TEXTURE_TYPE_");

    switch (component_count) {
      case 1:
        s.append("R_");
        break;
      case 2:
        s.append("RG_");
        break;
      case 3:
        s.append("RGB_");
        break;
      case 4:
        s.append("RGBA_");
        break;
    }

    switch (type) {
      case PIXEL_COMPONENT_BYTE:
      case PIXEL_COMPONENT_FLOAT:
      case PIXEL_COMPONENT_INT:
      case PIXEL_COMPONENT_SHORT:
      case PIXEL_COMPONENT_UNSIGNED_BYTE:
      case PIXEL_COMPONENT_UNSIGNED_INT:
      case PIXEL_COMPONENT_UNSIGNED_SHORT:
      case PIXEL_COMPONENT_HALF_FLOAT:
        throw new UnreachableCodeException();
      case PIXEL_PACKED_UNSIGNED_INT_1010102:
      {
        s.append("1010102");
        break;
      }
      case PIXEL_PACKED_UNSIGNED_SHORT_4444:
      {
        s.append("4444");
        break;
      }
      case PIXEL_PACKED_UNSIGNED_SHORT_5551:
      {
        s.append("5551");
        break;
      }
      case PIXEL_PACKED_UNSIGNED_SHORT_565:
      {
        s.append("565");
        break;
      }
      case PIXEL_PACKED_UNSIGNED_INT_24_8:
      {
        s.append("24_8");
        break;
      }
    }

    s.append("_");
    s.append(bytes_per_pixel);
    s.append("BPP");
    s.append("(PixelType.");
    s.append(type);
    s.append(", ");
    s.append(component_count);
    s.append(", ");
    s.append(bytes_per_pixel);
    s.append(")");
  }

  private static List<String> packedColourTypes()
  {
    final ArrayList<String> types = new ArrayList<String>();
    final StringBuilder s = new StringBuilder();

    for (final PixelType t : PixelType.values()) {
      switch (t) {
        case PIXEL_COMPONENT_BYTE:
        case PIXEL_COMPONENT_FLOAT:
        case PIXEL_COMPONENT_INT:
        case PIXEL_COMPONENT_SHORT:
        case PIXEL_COMPONENT_UNSIGNED_BYTE:
        case PIXEL_COMPONENT_UNSIGNED_INT:
        case PIXEL_COMPONENT_UNSIGNED_SHORT:
        case PIXEL_COMPONENT_HALF_FLOAT:
        {
          break;
        }
        case PIXEL_PACKED_UNSIGNED_INT_1010102:
        {
          TextureTypeGeneration.packedColourType(s, t, 4, 4);
          types.add(s.toString());
          break;
        }
        case PIXEL_PACKED_UNSIGNED_SHORT_4444:
        {
          TextureTypeGeneration.packedColourType(s, t, 4, 2);
          types.add(s.toString());
          break;
        }
        case PIXEL_PACKED_UNSIGNED_SHORT_5551:
        {
          TextureTypeGeneration.packedColourType(s, t, 4, 2);
          types.add(s.toString());
          break;
        }
        case PIXEL_PACKED_UNSIGNED_SHORT_565:
        {
          TextureTypeGeneration.packedColourType(s, t, 3, 2);
          types.add(s.toString());
          break;
        }
        case PIXEL_PACKED_UNSIGNED_INT_24_8:
        {
          TextureTypeGeneration.packedColourType(s, t, 2, 4);
          types.add(s.toString());
          break;
        }
      }
    }

    return types;
  }

  private static void unpackedColourType(
    final  StringBuilder s,
    final int component_count,
    final int component_size,
    final  Type t)
  {
    final int bytes_per_pixel = (component_count * component_size) / 8;

    s.setLength(0);
    s.append("TEXTURE_TYPE_");

    switch (component_count) {
      case 1:
        s.append("R_");
        break;
      case 2:
        s.append("RG_");
        break;
      case 3:
        s.append("RGB_");
        break;
      case 4:
        s.append("RGBA_");
        break;
    }

    s.append(component_size);

    switch (t) {
      case FLOAT:
        s.append("F");
        break;
      case SIGNED:
        s.append("I");
        break;
      case UNSIGNED:
        s.append("U");
        break;
      case UNSIGNED_NORMALIZED_FIXED:
        break;
    }

    s.append("_");
    s.append(bytes_per_pixel);
    s.append("BPP");
    s.append("(PixelType.");
    switch (component_size) {
      case 8:
        switch (t) {
          case FLOAT:
            throw new UnreachableCodeException();
          case SIGNED:
            s.append(PixelType.PIXEL_COMPONENT_BYTE);
            break;
          case UNSIGNED:
            s.append(PixelType.PIXEL_COMPONENT_UNSIGNED_BYTE);
            break;
          case UNSIGNED_NORMALIZED_FIXED:
            s.append(PixelType.PIXEL_COMPONENT_UNSIGNED_BYTE);
            break;
        }
        break;
      case 16:
        switch (t) {
          case FLOAT:
            s.append(PixelType.PIXEL_COMPONENT_FLOAT);
            break;
          case SIGNED:
            s.append(PixelType.PIXEL_COMPONENT_SHORT);
            break;
          case UNSIGNED:
            s.append(PixelType.PIXEL_COMPONENT_UNSIGNED_SHORT);
            break;
          case UNSIGNED_NORMALIZED_FIXED:
            s.append(PixelType.PIXEL_COMPONENT_UNSIGNED_SHORT);
            break;
        }
        break;
      case 32:
        switch (t) {
          case FLOAT:
            s.append(PixelType.PIXEL_COMPONENT_FLOAT);
            break;
          case SIGNED:
            s.append(PixelType.PIXEL_COMPONENT_INT);
            break;
          case UNSIGNED:
            s.append(PixelType.PIXEL_COMPONENT_UNSIGNED_INT);
            break;
          case UNSIGNED_NORMALIZED_FIXED:
            throw new UnreachableCodeException();
        }
        break;
    }

    s.append(", ");
    s.append(component_count);
    s.append(", ");
    s.append(bytes_per_pixel);
    s.append(")");
  }

  private static List<String> unpackedColourTypes()
  {
    final ArrayList<String> types = new ArrayList<String>();
    final StringBuilder s = new StringBuilder();

    for (int component_count = 1; component_count <= 4; ++component_count) {
      for (int component_size = 8; component_size <= 32; component_size =
        component_size * 2) {
        for (final Type t : Type.values()) {
          switch (t) {
            case FLOAT:
              if (component_size > 8) {
                TextureTypeGeneration.unpackedColourType(
                  s,
                  component_count,
                  component_size,
                  t);
                types.add(s.toString());
                break;
              }
              break;
            case SIGNED:
            case UNSIGNED:
              TextureTypeGeneration.unpackedColourType(
                s,
                component_count,
                component_size,
                t);
              types.add(s.toString());
              break;
            case UNSIGNED_NORMALIZED_FIXED:
              if (component_size < 32) {
                TextureTypeGeneration.unpackedColourType(
                  s,
                  component_count,
                  component_size,
                  t);
                types.add(s.toString());
              }
              break;
          }
        }
      }
    }

    return types;
  }
}
