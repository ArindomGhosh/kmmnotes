package org.arindom.takenotes.database.dao

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.arindom.takenotes.database.ActiveNotesQueries
import org.arindom.takenotes.database.Notes

interface IActiveNotesDao {
  suspend fun getAllActiveNotes(): List<Notes>

  suspend fun getAllInActiveNotes(): List<Notes>

  suspend fun deleteInActiveNotes(): TransactionResult

}

internal class ActiveNotesDao(
  private val ioDispatcher: CoroutineDispatcher,
  private val activeNotesQueries: ActiveNotesQueries
) : IActiveNotesDao {
  override suspend fun getAllActiveNotes(): List<Notes> {
    return withContext(ioDispatcher) {
      activeNotesQueries.getAllActiveNotes().executeAsList()
    }
  }

  override suspend fun getAllInActiveNotes(): List<Notes> {
    return withContext(ioDispatcher) {
      activeNotesQueries.getAllInActiveNotes().executeAsList()
    }
  }

  override suspend fun deleteInActiveNotes(): TransactionResult {
    return withContext(ioDispatcher) {
      activeNotesQueries.dataTransaction {
        activeNotesQueries.deleteInActiveNotes()
      }
    }
  }
}