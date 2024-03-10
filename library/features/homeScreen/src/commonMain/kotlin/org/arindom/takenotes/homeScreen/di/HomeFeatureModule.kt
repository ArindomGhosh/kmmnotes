package org.arindom.takenotes.homeScreen.di

import org.arindom.takenotes.coroutines.di.CoroutineDispatcherType
import org.arindom.takenotes.database.di.databaseModule
import org.arindom.takenotes.homeScreen.HomeScreenViewModel
import org.arindom.takenotes.homeScreen.domain.usecases.DeleteNotesUseCase
import org.arindom.takenotes.homeScreen.domain.usecases.FilterByLabel
import org.arindom.takenotes.homeScreen.domain.usecases.FilterUseCase
import org.arindom.takenotes.homeScreen.domain.usecases.LoadUserNotes
import org.koin.core.qualifier.named
import org.koin.dsl.module

val homeFeatureModule = module {
  includes(databaseModule)

  factory {
    LoadUserNotes(get(), get(named(CoroutineDispatcherType.Default)))
  }
  factory {
    FilterUseCase(get())
  }

  factory {
    DeleteNotesUseCase(get())
  }

  factory {
    FilterByLabel(get())
  }

  factory {
    HomeScreenViewModel(get(), get(), get(), get())
  }
}