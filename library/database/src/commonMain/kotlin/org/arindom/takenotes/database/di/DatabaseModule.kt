package org.arindom.takenotes.database.di

import org.arindom.takenotes.coroutines.di.CoroutineDispatcherType
import org.arindom.takenotes.coroutines.di.CoroutineScopes
import org.arindom.takenotes.database.ActiveNotesQueries
import org.arindom.takenotes.database.DatabaseHelper
import org.arindom.takenotes.database.IDatabaseHelper
import org.arindom.takenotes.database.LabelledNotesQueries
import org.arindom.takenotes.database.NotesQueries
import org.arindom.takenotes.database.dao.ActiveNotesDao
import org.arindom.takenotes.database.dao.IActiveNotesDao
import org.arindom.takenotes.database.dao.ILabelledNotesDao
import org.arindom.takenotes.database.dao.INotesDao
import org.arindom.takenotes.database.dao.LabelledNotesDao
import org.arindom.takenotes.database.dao.NotesDao
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect fun sqlDelightDriverModule(): Module

val databaseModule = module {
  includes(sqlDelightDriverModule())
  single<IDatabaseHelper> { DatabaseHelper(get(), get(named(CoroutineScopes.IoScope))) }

  single<NotesQueries> { get<IDatabaseHelper>().getDb().notesQueries }
  single<ActiveNotesQueries> { get<IDatabaseHelper>().getDb().activeNotesQueries }
  single<LabelledNotesQueries> { get<IDatabaseHelper>().getDb().labelledNotesQueries }

  single<INotesDao> {
    NotesDao(get(named(CoroutineDispatcherType.IO)), get())
  }

  single<ILabelledNotesDao> {
    LabelledNotesDao(get(named(CoroutineDispatcherType.IO)), get())
  }
  single<IActiveNotesDao> {
    ActiveNotesDao(get(named(CoroutineDispatcherType.IO)), get())
  }
}