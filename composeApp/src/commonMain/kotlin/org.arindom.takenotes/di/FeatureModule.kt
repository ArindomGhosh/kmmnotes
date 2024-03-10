package org.arindom.takenotes.di

import org.arindom.takenotes.Platform
import org.arindom.takenotes.getPlatform
import org.koin.dsl.module

val platform = module {
  single<Platform> {
    getPlatform()
  }
}