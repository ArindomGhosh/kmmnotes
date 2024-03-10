package org.arindom.takenotes.createNotes.di

import org.arindom.takenotes.coroutines.di.CoroutineDispatcherType
import org.arindom.takenotes.createNotes.domain.usecases.CreateNotes
import org.arindom.takenotes.createNotes.domain.usecases.OpenNoteForId
import org.arindom.takenotes.createNotes.domain.usecases.UpdateNotes
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val domainModule = module {
  factory { CreateNotes(get()) }
  factory { OpenNoteForId(get()) }
  factory { UpdateNotes(get(), get(named(CoroutineDispatcherType.IO))) }
}