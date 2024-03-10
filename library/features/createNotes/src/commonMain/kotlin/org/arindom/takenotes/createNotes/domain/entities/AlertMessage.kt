package org.arindom.takenotes.createNotes.domain.entities

enum class AlertType {
  INFO,
  ERR
}

data class AlertMessage(
  val message: String,
  val alertType: AlertType
)
