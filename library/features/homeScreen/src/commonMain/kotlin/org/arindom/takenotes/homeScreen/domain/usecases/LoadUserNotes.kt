package org.arindom.takenotes.homeScreen.domain.usecases

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.arindom.takenotes.database.dao.INotesDao
import org.arindom.takenotes.homeScreen.domain.entities.mapToEntityNote
import org.arindom.takenotes.homeScreen.presentation.NotesViewModel

internal class LoadUserNotes(
  private val notesDao: INotesDao,
  private val defaultCoroutineDispatcher: CoroutineDispatcher,
) {
  operator fun invoke(deleteMode: Boolean = false): Flow<ImmutableList<NotesViewModel>> {
    return notesDao.getAllNotesAsFlow().map {
      it.mapToEntityNote().map { notes ->
        NotesViewModel(
          notes = notes, deleteMode = deleteMode
        )
      }.toImmutableList()
    }.flowOn(defaultCoroutineDispatcher)
  }
}
