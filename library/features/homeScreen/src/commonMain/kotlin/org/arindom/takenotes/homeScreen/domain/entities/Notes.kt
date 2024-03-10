package org.arindom.takenotes.homeScreen.domain.entities

import org.arindom.takenotes.data.NoteType
import org.arindom.takenotes.data.mapToEntityNoteType

internal data class Notes(
  val id: Long,
  val header: String,
  val content: String,
  val noteType: NoteType,
//  Create a labels
  val label: String?,
//  only date component in ISO-8601 format e.g.: 2020-06-01
  val createdAt: String,
  val isActive: Boolean = true,
)

internal fun List<org.arindom.takenotes.database.Notes>.mapToEntityNote(): List<Notes> {
  return this
    .map {
      Notes(
        id = it._id,
        header = it.title,
        content = it.content,
        noteType = it.noteType.mapToEntityNoteType(),
        label = it.label,
        createdAt = it.createdAt,
        isActive = it.isActive
      )
    }
}