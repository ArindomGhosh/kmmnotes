package org.arindom.takenotes.createNotes.domain.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.arindom.takenotes.createNotes.domain.entities.MutableNote
import org.arindom.takenotes.createNotes.domain.entities.toCreateNote
import org.arindom.takenotes.createNotes.domain.entities.toNotes
import org.arindom.takenotes.data.domain.DomainResponse
import org.arindom.takenotes.data.exceptions.TransactionException
import org.arindom.takenotes.data.utils.map
import org.arindom.takenotes.database.dao.INotesDao
import org.arindom.takenotes.database.dao.TransactionResult

internal class UpdateNotes(
  private val notesDao: INotesDao,
  private val ioDispatcher: CoroutineDispatcher
) {
  suspend operator fun invoke(mutableNote: MutableNote): DomainResponse<out MutableNote> {
    return notesDao.updateNotes(mutableNote.toNotes())
      .map {
        when (it) {
          TransactionResult.Failed -> DomainResponse.Fail(TransactionException.UpdateOperationException)
          TransactionResult.Succeed -> withContext(ioDispatcher) {
            DomainResponse.Success(
              notesDao.getNotesForId(mutableNote.notesId)?.toCreateNote()!!
            )
          }
        }
      }
  }
}