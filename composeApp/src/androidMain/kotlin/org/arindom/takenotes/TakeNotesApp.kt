package org.arindom.takenotes

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.arindom.takenotes.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class TakeNotesApp : Application() {
  override fun onCreate() {
    super.onCreate()
    Napier.base(DebugAntilog())
    initKoin {
      androidContext(this@TakeNotesApp)
      androidLogger(Level.DEBUG)
    }
  }
}