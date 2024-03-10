package org.arindom.takenotes.homeScreen.domain.usecases

import org.arindom.takenotes.database.dao.INotesDao
import org.arindom.takenotes.homeScreen.domain.entities.mapToEntityNote
import org.arindom.takenotes.homeScreen.presentation.NotesViewModel

internal class FilterUseCase(
  private val notesDao: INotesDao
) {
  suspend operator fun invoke(pattern: String): List<NotesViewModel> {
    return notesDao.getNoteForTitleLike(pattern)
      .mapToEntityNote()
      .map {
        NotesViewModel(
          notes = it
        )
      }
  }
}