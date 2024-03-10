package org.arindom.takenotes.coroutines.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class CoroutineDispatcherType {
  IO,
  Main,
  MainImmediate,
  Default
}

enum class CoroutineScopes {
  ApplicationScope,
  IoScope,
}

val coroutineModule = module {
  single(named(CoroutineDispatcherType.IO)) {
    Dispatchers.IO
  }

  single(named(CoroutineDispatcherType.Main)) {
    Dispatchers.Main
  }

  single(named(CoroutineDispatcherType.Default)) {
    Dispatchers.Default
  }

  single(named(CoroutineDispatcherType.MainImmediate)) {
    Dispatchers.Main.immediate
  }

  single(named(CoroutineScopes.ApplicationScope)) {
    CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(CoroutineDispatcherType.Default)))
  }
  single(named(CoroutineScopes.IoScope)) {
    CoroutineScope(SupervisorJob() + get<CoroutineDispatcher>(named(CoroutineDispatcherType.IO)))
  }

}