--
-- Copyright © 2013 <code@io7m.com> http://io7m.com
-- 
-- Permission to use, copy, modify, and/or distribute this software for any
-- purpose with or without fee is hereby granted, provided that the above
-- copyright notice and this permission notice appear in all copies.
-- 
-- THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
-- WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
-- MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
-- SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
-- WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
-- ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
-- IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
--

package com.io7m.jcanephora;

module Shaders is

  import com.io7m.parasol.Matrix3x3f        as M3;
  import com.io7m.parasol.Matrix4x4f        as M4;
  import com.io7m.parasol.Vector3f          as V3;
  import com.io7m.parasol.Vector4f          as V4;
  import com.io7m.parasol.Vector4i          as V4I;
  import com.io7m.parasol.Sampler2D         as S;
  import com.io7m.parasol.Float             as F;

  shader vertex simple_v is
    in v_position              : vector_3f;
    parameter m_modelview      : matrix_4x4f;
    parameter m_projection     : matrix_4x4f;
    out vertex f_position_clip : vector_4f;
  with
    value clip_position =
      M4.multiply_vector (
        M4.multiply (m_projection, m_modelview),
        new vector_4f (v_position, 1.0)
      );
  as
    out f_position_clip = clip_position;
  end;

  shader fragment simple_f is
    parameter f_ccolour : vector_4f;
    out       out_0     : vector_4f as 0;
  as
    out out_0 = f_ccolour;
  end;

  shader program simple is
    vertex   simple_v;
    fragment simple_f;
  end;

  shader vertex everything_v is
    in a_f            : float;
    in a_vf2          : vector_2f;
    in a_vf3          : vector_3f;
    in a_vf4          : vector_4f;
    parameter u_float : float;
    parameter u_int   : integer;
    parameter u_vf2   : vector_2f;
    parameter u_vf3   : vector_3f;
    parameter u_vf4   : vector_4f;
    parameter u_vi2   : vector_2i;
    parameter u_vi3   : vector_3i;
    parameter u_vi4   : vector_4i;
    parameter u_m3    : matrix_3x3f;
    parameter u_m4    : matrix_4x4f;

    out vertex f_position_clip : vector_4f;
    out        something       : vector_4f;
  with
    value vf0 = new vector_4f (a_vf2 [x], a_vf3 [x], a_vf4 [z], u_float);
    value vf1 = new vector_4f (u_vf2 [x], u_vf3 [x], u_vf4 [x], new float (u_int));
    value vf2 = new vector_4f (a_f, a_f, a_f, a_f);
    value vf3 = new vector_4f (M3.multiply_vector (u_m3, a_vf3), 1.0);
    value vf4 = V4.add (vf0, vf1);
    value vf5 = V4.add (vf2, vf3);
    value vf6 = V4.add (vf4, vf5);

    value vi0 = new vector_4i (u_vi2, u_vi3 [x], u_vi3 [y]);
    value vi1 = new vector_4i (u_vi4 [x y z], u_int);
    value vi2 = V4I.add (vi0, vi1);

    value k = new vector_4f (
      F.add (new float (vi2 [x]), vf6 [x]),
      F.add (new float (vi2 [y]), vf6 [y]),
      F.add (new float (vi2 [z]), vf6 [z]),
      F.add (new float (vi2 [w]), vf6 [w])
    );

    value clip_position =
      M4.multiply_vector (u_m4, k);
  as
    out f_position_clip = clip_position;
    out something       = k;
  end;

  shader fragment everything_f is
    in something        : vector_4f;
    parameter u_texture : sampler_2d;
    out out_0           : vector_4f as 0;
  with
    value rgba = S.texture (u_texture, something [x y]);
  as
    out out_0 = rgba;
  end;

  shader program everything is
    vertex   everything_v;
    fragment everything_f;
  end;

  shader vertex everything_opt_v is
    in a_f            : float;
    in a_vf2          : vector_2f;
    in a_vf3          : vector_3f;
    in a_vf4          : vector_4f;
    parameter u_float : float;
    parameter u_int   : integer;
    parameter u_vf2   : vector_2f;
    parameter u_vf3   : vector_3f;
    parameter u_vf4   : vector_4f;
    parameter u_vi2   : vector_2i;
    parameter u_vi3   : vector_3i;
    parameter u_vi4   : vector_4i;
    parameter u_m3    : matrix_3x3f;
    parameter u_m4    : matrix_4x4f;

    in a_f_opt            : float;
    in a_vf2_opt          : vector_2f;
    in a_vf3_opt          : vector_3f;
    in a_vf4_opt          : vector_4f;
    parameter u_float_opt : float;
    parameter u_int_opt   : integer;
    parameter u_vf2_opt   : vector_2f;
    parameter u_vf3_opt   : vector_3f;
    parameter u_vf4_opt   : vector_4f;
    parameter u_vi2_opt   : vector_2i;
    parameter u_vi3_opt   : vector_3i;
    parameter u_vi4_opt   : vector_4i;
    parameter u_m3_opt    : matrix_3x3f;
    parameter u_m4_opt    : matrix_4x4f;

    out vertex f_position_clip : vector_4f;
    out something              : vector_4f;
  with
    value vf0 = new vector_4f (a_vf2 [x], a_vf3 [x], a_vf4 [z], u_float);
    value vf1 = new vector_4f (u_vf2 [x], u_vf3 [x], u_vf4 [x], new float (u_int));
    value vf2 = new vector_4f (a_f, a_f, a_f, a_f);
    value vf3 = new vector_4f (M3.multiply_vector (u_m3, a_vf3), 1.0);
    value vf4 = V4.add (vf0, vf1);
    value vf5 = V4.add (vf2, vf3);
    value vf6 = V4.add (vf4, vf5);

    value vi0 = new vector_4i (u_vi2, u_vi3 [x], u_vi3 [y]);
    value vi1 = new vector_4i (u_vi4 [x y z], u_int);
    value vi2 = V4I.add (vi0, vi1);

    value k = new vector_4f (
      F.add (new float (vi2 [x]), vf6 [x]),
      F.add (new float (vi2 [y]), vf6 [y]),
      F.add (new float (vi2 [z]), vf6 [z]),
      F.add (new float (vi2 [w]), vf6 [w])
    );

    value clip_position =
      M4.multiply_vector (u_m4, k);
  as
    out f_position_clip = clip_position;
    out something       = k;
  end;

  shader fragment everything_opt_f is
    in something            : vector_4f;
    parameter u_texture     : sampler_2d;
    parameter u_texture_opt : sampler_2d;
    out out_0               : vector_4f as 0;
  with
    value rgba = S.texture (u_texture, something [x y]);
  as
    out out_0 = rgba;
  end;

  shader program everything_opt is
    vertex   everything_opt_v;
    fragment everything_opt_f;
  end;

end;

