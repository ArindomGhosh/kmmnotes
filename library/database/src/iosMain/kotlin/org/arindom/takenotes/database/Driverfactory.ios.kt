package org.arindom.takenotes.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

internal actual class DriverFactory {
  actual fun createDriver(): SqlDriver {
    return NativeSqliteDriver(TakeNotesDatabase.Schema,"take_notes.db")
  }
}