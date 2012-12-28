package com.io7m.jcanephora.contracts_ES2;

import javax.annotation.Nonnull;

import com.io7m.jcanephora.TextureLoader;

public interface TextureLoaderTestContract extends GLES2TestContract
{
  @Nonnull TextureLoader makeTextureLoader();
}
