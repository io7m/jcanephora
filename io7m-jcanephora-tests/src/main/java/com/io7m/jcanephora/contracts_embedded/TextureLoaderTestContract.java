package com.io7m.jcanephora.contracts_embedded;

import javax.annotation.Nonnull;

import com.io7m.jcanephora.TextureLoader;

public interface TextureLoaderTestContract extends GLEmbeddedTestContract
{
  @Nonnull TextureLoader makeTextureLoader();
}
