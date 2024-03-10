package org.arindom.takenotes.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.arindom.takenotes.database.Notes
import org.arindom.takenotes.database.NotesQueries


interface INotesDao {

  suspend fun insertNotes(
    vararg notesList: Notes,
  ): TransactionResult

  suspend fun updateNotes(
    notes: Notes,
  ): TransactionResult

  suspend fun getAllNotes(): List<Notes>

  fun getAllNotesAsFlow(): Flow<List<Notes>>

  suspend fun getNotesForId(id: Long): Notes?

  suspend fun deleteAllNotes(): TransactionResult

  suspend fun deleteNotesForId(id: Long): TransactionResult
  suspend fun deleteNotesForIds(
    ids: List<Long>
  ): TransactionResult

  suspend fun getNoteForTitleLike(pattern: String): List<Notes>
}

internal class NotesDao(
  private val ioDispatcher: CoroutineDispatcher,
  private val notesQueries: NotesQueries
) : INotesDao {
  override suspend fun insertNotes(
    vararg notesList: Notes,
  ): TransactionResult {
    return withContext(ioDispatcher) {
      notesQueries.dataTransaction {
        notesList.forEach { note ->
          notesQueries.insertNotes(note)
        }
      }
    }
  }

  override suspend fun updateNotes(
    notes: Notes,
  ): TransactionResult {
    return withContext(ioDispatcher) {
      notesQueries.dataTransaction {
        notesQueries.updateNotes(
          title = notes.title,
          content = notes.content,
          createdAt = notes.createdAt,
          noteType = notes.noteType,
          label = notes.label,
          isActive = notes.isActive,
          _id = notes._id
        )
      }
    }
  }

  override suspend fun getAllNotes(): List<Notes> {
    return withContext(ioDispatcher) {
      notesQueries.getAllNotes().executeAsList()
    }
  }

  override fun getAllNotesAsFlow(): Flow<List<Notes>> {
    return notesQueries.getAllNotes().asFlow().mapToList(ioDispatcher)
  }

  override suspend fun getNotesForId(id: Long): Notes? {
    return try {
      withContext(ioDispatcher) {
        notesQueries.getNoteForId(id).executeAsOne()
      }
    } catch (e: Exception) {
      return null
    }
  }

  override suspend fun deleteAllNotes(): TransactionResult {
    return withContext(ioDispatcher) {
      notesQueries.dataTransaction {
        notesQueries.deleteAllNotes()
      }
    }
  }

  override suspend fun deleteNotesForId(
    id: Long,
  ): TransactionResult {
    return withContext(ioDispatcher) {
      notesQueries.dataTransaction {
        notesQueries.deleteNoteForId(id)
      }
    }
  }

  override suspend fun deleteNotesForIds(
    ids: List<Long>,
  ): TransactionResult {
    return withContext(ioDispatcher) {
      notesQueries.dataTransaction {
        ids.forEach { id ->
          notesQueries.deleteNoteForId(id)
        }
      }
    }
  }

  override suspend fun getNoteForTitleLike(pattern: String): List<Notes> {
    return withContext(ioDispatcher) {
      notesQueries.getNoteHavingTitle(pattern).executeAsList()
    }
  }
}