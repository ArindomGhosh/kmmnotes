package org.arindom.takenotes.createNotes.di

import org.arindom.takenotes.createNotes.CreateNotesViewModel
import org.koin.dsl.module

internal val viewModelModule = module {
  includes(domainModule)
  factory { CreateNotesViewModel(get(), get(), get()) }
}