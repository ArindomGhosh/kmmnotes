package org.arindom.takenotes.createNotes.di

import org.koin.dsl.module

val createNoteModule = module {
  includes(viewModelModule)
}