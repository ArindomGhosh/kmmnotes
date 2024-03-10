package org.arindom.takenotes.createNotes.domain.entities.usecases

import org.arindom.takenotes.data.NoteType
import org.arindom.takenotes.database.Notes

internal data class MutableNote(
  val notesId: Long = 0,
  val header: String = "",
  val content: String = "",
  val createdAt: String = "",
  val noteType: NoteType = NoteType.SINGLE,
  val label: String? = null,
  val isActive: Boolean = true
)


internal fun Notes.toCreateNote() = MutableNote(
  notesId = this._id,
  header = this.title,
  content = this.content,
  createdAt = this.createdAt,
  noteType = NoteType.resolveNoteType(this.noteType),
  label = this.label
)

internal fun MutableNote.toNotes() = Notes(
  _id = this.notesId,
  title = this.header,
  content = this.content,
  createdAt = this.createdAt,
  noteType = this.noteType.arg,
  isActive = this.isActive,
  label = this.label
)

