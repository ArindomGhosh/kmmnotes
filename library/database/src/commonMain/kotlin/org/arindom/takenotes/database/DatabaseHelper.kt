package org.arindom.takenotes.database

import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


interface IDatabaseHelper {
  fun getDb(): TakeNotesDatabase
}

suspend fun IDatabaseHelper.executeAsync(action: suspend (TakeNotesDatabase) -> Unit) {
  action(this.getDb())
}

internal class DatabaseHelper(
  private val driverFactory: DriverFactory,
  ioScope: CoroutineScope
) : IDatabaseHelper {
  private val _mutex = Mutex()
  private var _db: TakeNotesDatabase? = null
  private val _completableDeferred = CompletableDeferred<Unit>()

  init {
    ioScope.launch {
      createDb()
      _completableDeferred.complete(Unit)
    }
  }

  private suspend fun createDb() {
    _mutex.withLock {
      _db = TakeNotesDatabase(
        driver = driverFactory.createDriver(),
        notesAdapter = Notes.Adapter(IntColumnAdapter)
      ).also {
//        it.notesQueries.deleteAllNotes()
        val notes = listOf(
          Notes(0, "Meeting", "New Project", "2024-02-01", 1, "daily", true),
          Notes(0, "Meeting", "DSU", "2024-02-01", 1, "daily", true),
          Notes(0, "Break", "Coffee Break", "2024-02-01", 1, null, true),
          Notes(0, "Pickup", "Grocery", "2024-02-01", 1, null, true),
          Notes(0, "Booking", "Movie Tickets", "2024-02-01", 1, null, true),
        )
        it.notesQueries.transaction {
          notes.forEach { note ->
            it.notesQueries.insertNotes(note)
          }
        }
      }
    }
  }

  override fun getDb(): TakeNotesDatabase {
    runBlocking {
      _completableDeferred.await()
    }
    return _db!!
  }
}