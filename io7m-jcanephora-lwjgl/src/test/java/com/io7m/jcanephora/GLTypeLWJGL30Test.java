package com.io7m.jcanephora;

import junit.framework.Assert;

import org.junit.Test;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;

public class GLTypeLWJGL30Test
{
  @SuppressWarnings("static-method") @Test public void testTypeBijection()
  {
    /*
     * toGLType
     */

    Assert.assertEquals(
      GL20.GL_BOOL,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_BOOLEAN));
    Assert.assertEquals(
      GL20.GL_BOOL_VEC2,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_BOOLEAN_VECTOR_2));
    Assert.assertEquals(
      GL20.GL_BOOL_VEC3,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_BOOLEAN_VECTOR_3));
    Assert.assertEquals(
      GL20.GL_BOOL_VEC4,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_BOOLEAN_VECTOR_4));

    Assert.assertEquals(
      GL11.GL_FLOAT,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT));

    Assert.assertEquals(
      GL20.GL_FLOAT_MAT2,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_MATRIX_2));
    Assert.assertEquals(
      GL21.GL_FLOAT_MAT2x3,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_MATRIX_2x3));
    Assert.assertEquals(
      GL21.GL_FLOAT_MAT2x4,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_MATRIX_2x4));

    Assert.assertEquals(
      GL20.GL_FLOAT_MAT3,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_MATRIX_3));
    Assert.assertEquals(
      GL21.GL_FLOAT_MAT3x2,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_MATRIX_3x2));
    Assert.assertEquals(
      GL21.GL_FLOAT_MAT3x4,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_MATRIX_3x4));

    Assert.assertEquals(
      GL20.GL_FLOAT_MAT4,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_MATRIX_4));
    Assert.assertEquals(
      GL21.GL_FLOAT_MAT4x2,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_MATRIX_4x2));
    Assert.assertEquals(
      GL21.GL_FLOAT_MAT4x3,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_MATRIX_4x3));

    Assert.assertEquals(
      GL20.GL_FLOAT_VEC2,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_VECTOR_2));
    Assert.assertEquals(
      GL20.GL_FLOAT_VEC3,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_VECTOR_3));
    Assert.assertEquals(
      GL20.GL_FLOAT_VEC4,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_FLOAT_VECTOR_4));

    Assert.assertEquals(
      GL11.GL_INT,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_INTEGER));

    Assert.assertEquals(
      GL20.GL_INT_VEC2,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_INTEGER_VECTOR_2));
    Assert.assertEquals(
      GL20.GL_INT_VEC3,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_INTEGER_VECTOR_3));
    Assert.assertEquals(
      GL20.GL_INT_VEC4,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_INTEGER_VECTOR_4));

    Assert.assertEquals(
      GL20.GL_SAMPLER_1D,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_SAMPLER_1D));
    Assert.assertEquals(
      GL20.GL_SAMPLER_1D_SHADOW,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_SAMPLER_1D_SHADOW));
    Assert.assertEquals(
      GL20.GL_SAMPLER_2D,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_SAMPLER_2D));
    Assert.assertEquals(
      GL20.GL_SAMPLER_2D_SHADOW,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_SAMPLER_2D_SHADOW));
    Assert.assertEquals(
      GL20.GL_SAMPLER_3D,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_SAMPLER_3D));
    Assert.assertEquals(
      GL20.GL_SAMPLER_CUBE,
      GLInterfaceLWJGL30.typeToGL(GLType.Type.TYPE_SAMPLER_CUBE));

    /*
     * fromGLType
     */

    Assert.assertEquals(
      GLType.Type.TYPE_BOOLEAN,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_BOOL));
    Assert.assertEquals(
      GLType.Type.TYPE_BOOLEAN_VECTOR_2,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_BOOL_VEC2));
    Assert.assertEquals(
      GLType.Type.TYPE_BOOLEAN_VECTOR_3,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_BOOL_VEC3));
    Assert.assertEquals(
      GLType.Type.TYPE_BOOLEAN_VECTOR_4,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_BOOL_VEC4));

    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT,
      GLInterfaceLWJGL30.typeFromGL(GL11.GL_FLOAT));

    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_MATRIX_2,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_FLOAT_MAT2));
    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_MATRIX_2x3,
      GLInterfaceLWJGL30.typeFromGL(GL21.GL_FLOAT_MAT2x3));
    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_MATRIX_2x4,
      GLInterfaceLWJGL30.typeFromGL(GL21.GL_FLOAT_MAT2x4));

    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_MATRIX_3,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_FLOAT_MAT3));
    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_MATRIX_3x2,
      GLInterfaceLWJGL30.typeFromGL(GL21.GL_FLOAT_MAT3x2));
    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_MATRIX_3x4,
      GLInterfaceLWJGL30.typeFromGL(GL21.GL_FLOAT_MAT3x4));

    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_MATRIX_4,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_FLOAT_MAT4));
    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_MATRIX_4x2,
      GLInterfaceLWJGL30.typeFromGL(GL21.GL_FLOAT_MAT4x2));
    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_MATRIX_4x3,
      GLInterfaceLWJGL30.typeFromGL(GL21.GL_FLOAT_MAT4x3));

    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_VECTOR_2,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_FLOAT_VEC2));
    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_VECTOR_3,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_FLOAT_VEC3));
    Assert.assertEquals(
      GLType.Type.TYPE_FLOAT_VECTOR_4,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_FLOAT_VEC4));

    Assert.assertEquals(
      GLType.Type.TYPE_INTEGER,
      GLInterfaceLWJGL30.typeFromGL(GL11.GL_INT));

    Assert.assertEquals(
      GLType.Type.TYPE_INTEGER_VECTOR_2,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_INT_VEC2));
    Assert.assertEquals(
      GLType.Type.TYPE_INTEGER_VECTOR_3,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_INT_VEC3));
    Assert.assertEquals(
      GLType.Type.TYPE_INTEGER_VECTOR_4,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_INT_VEC4));

    Assert.assertEquals(
      GLType.Type.TYPE_SAMPLER_1D,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_SAMPLER_1D));
    Assert.assertEquals(
      GLType.Type.TYPE_SAMPLER_1D_SHADOW,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_SAMPLER_1D_SHADOW));
    Assert.assertEquals(
      GLType.Type.TYPE_SAMPLER_2D,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_SAMPLER_2D));
    Assert.assertEquals(
      GLType.Type.TYPE_SAMPLER_2D_SHADOW,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_SAMPLER_2D_SHADOW));
    Assert.assertEquals(
      GLType.Type.TYPE_SAMPLER_3D,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_SAMPLER_3D));
    Assert.assertEquals(
      GLType.Type.TYPE_SAMPLER_CUBE,
      GLInterfaceLWJGL30.typeFromGL(GL20.GL_SAMPLER_CUBE));
  }

  @SuppressWarnings("static-method") @Test(expected = AssertionError.class) public void testTypeFailure()
  {
    GLInterfaceLWJGL30.typeFromGL(-1);
  }
}
