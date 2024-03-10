package org.arindom.takenotes.database.adapters

import app.cash.sqldelight.ColumnAdapter

object BooleanColumnAdapter : ColumnAdapter<Boolean, Long> {
  override fun decode(databaseValue: Long): Boolean {
    return databaseValue == 1L
  }

  override fun encode(value: Boolean): Long {
    return if (value) {
      1
    } else {
      0
    }
  }

}
