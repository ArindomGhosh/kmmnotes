package org.arindom.takenotes.database.di

import org.arindom.takenotes.database.DriverFactory
import org.koin.dsl.module

actual fun sqlDelightDriverModule() = module {
  single<DriverFactory> {
    DriverFactory()
  }
}