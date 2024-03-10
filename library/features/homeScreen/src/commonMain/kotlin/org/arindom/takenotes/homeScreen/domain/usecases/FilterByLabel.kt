package org.arindom.takenotes.homeScreen.domain.usecases

import org.arindom.takenotes.database.dao.ILabelledNotesDao
import org.arindom.takenotes.homeScreen.domain.entities.mapToEntityNote
import org.arindom.takenotes.homeScreen.presentation.NotesViewModel

internal class FilterByLabel(
  private val labelNotesDao: ILabelledNotesDao
) {
  suspend operator fun invoke(label: String): List<NotesViewModel> {
    return labelNotesDao.getAllNotesForLabel(label)
      .mapToEntityNote()
      .map {
        NotesViewModel(
          notes = it
        )
      }
  }
}