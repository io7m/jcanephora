package com.io7m.jcanephora;

import javax.annotation.Nonnull;

import com.io7m.jcanephora.contracts_ES2.TextureLoaderContract;

public abstract class TextureLoaderImageIOTest extends TextureLoaderContract
{
  @Override public final @Nonnull TextureLoader makeTextureLoader()
  {
    return new TextureLoaderImageIO();
  }
}
