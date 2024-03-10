package org.arindom.takenotes.homeScreen.domain.usecases

import org.arindom.takenotes.data.domain.DomainResponse
import org.arindom.takenotes.data.exceptions.TransactionException
import org.arindom.takenotes.data.utils.map
import org.arindom.takenotes.database.dao.INotesDao
import org.arindom.takenotes.database.dao.TransactionResult

class DeleteNotesUseCase(
  private val notesDao: INotesDao,
) {
  suspend operator fun invoke(noteIds: List<Long>): DomainResponse<out Boolean> {
    return notesDao.deleteNotesForIds(noteIds)
      .map {
        when(it){
          TransactionResult.Succeed -> DomainResponse.Success(true)
          TransactionResult.Failed -> DomainResponse.Fail(TransactionException.DeleteOperationException)
        }
      }
  }
}