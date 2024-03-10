package org.arindom.takenotes.createNotes.domain.usecases

import org.arindom.takenotes.createNotes.domain.entities.MutableNote
import org.arindom.takenotes.createNotes.domain.entities.toNotes
import org.arindom.takenotes.data.domain.DomainResponse
import org.arindom.takenotes.data.exceptions.TransactionException
import org.arindom.takenotes.data.utils.map
import org.arindom.takenotes.database.dao.INotesDao
import org.arindom.takenotes.database.dao.TransactionResult

internal class CreateNotes(
  private val notesDao: INotesDao
) {
  suspend operator fun invoke(mutableNote: MutableNote): DomainResponse<out Boolean> {
    return notesDao
      .insertNotes(mutableNote.toNotes())
      .map {
        when (it) {
          TransactionResult.Succeed -> DomainResponse.Success(true)
          TransactionResult.Failed -> DomainResponse.Fail(TransactionException.InsertOperationException)
        }
      }
  }
}