package org.arindom.takenotes.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
  startKoin {
    appDeclaration()
    modules(applicationModule)
  }

fun KoinApplication.Companion.start(): KoinApplication = initKoin { }

