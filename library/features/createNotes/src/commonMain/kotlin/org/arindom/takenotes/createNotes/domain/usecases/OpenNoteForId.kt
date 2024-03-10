package org.arindom.takenotes.createNotes.domain.usecases

import org.arindom.takenotes.createNotes.domain.entities.MutableNote
import org.arindom.takenotes.createNotes.domain.entities.toCreateNote
import org.arindom.takenotes.database.dao.INotesDao

internal class OpenNoteForId(
  private val notesDao: INotesDao
) {
  suspend operator fun invoke(notesId: Long): MutableNote {
    return notesDao.getNotesForId(notesId)?.toCreateNote() ?: MutableNote();
  }
}