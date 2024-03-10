package org.arindom.takenotes.di

import commonModule
import org.arindom.takenotes.coroutines.di.coroutineModule
import org.koin.dsl.module

val applicationModule = module {
  includes(coroutineModule, featureModule, commonModule)
}