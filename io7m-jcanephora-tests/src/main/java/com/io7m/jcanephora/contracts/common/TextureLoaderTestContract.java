package com.io7m.jcanephora.contracts.common;

import javax.annotation.Nonnull;

import com.io7m.jcanephora.TextureLoader;

public interface TextureLoaderTestContract extends TestContract
{
  @Nonnull TextureLoader makeTextureLoader();
}
