package org.arindom.takenotes.database.dao

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.arindom.takenotes.database.LabelledNotesQueries
import org.arindom.takenotes.database.Notes


interface ILabelledNotesDao {
  suspend fun getAllLabels(): List<String>

  suspend fun getAllNotesForLabel(label: String): List<Notes>
}

internal class LabelledNotesDao(
  private val ioDispatcher: CoroutineDispatcher,
  private val labelledNotesQueries: LabelledNotesQueries
) : ILabelledNotesDao {
  override suspend fun getAllLabels(): List<String> {
    return withContext(ioDispatcher) {
      labelledNotesQueries.getAllLabels().executeAsList()
    }
  }

  override suspend fun getAllNotesForLabel(label: String): List<Notes> {
    return withContext(ioDispatcher) {
      labelledNotesQueries.getAllNotesForLabel(label).executeAsList()
    }
  }
}