package org.arindom.takenotes.database

import app.cash.sqldelight.db.SqlDriver

internal expect class DriverFactory {
  fun createDriver(): SqlDriver
}