package org.arindom.takenotes.homeScreen.entities

enum class NoteType {
  SINGLE,
  LIST
}

data class Notes(
  val id: Int,
  val header: String,
  val content: String,
  val noteType: NoteType,
//  Create a labels
  val label: String
)
