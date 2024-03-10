package org.arindom.takenotes.di

import org.arindom.takenotes.createNotes.di.createNoteModule
import org.arindom.takenotes.homeScreen.di.homeFeatureModule
import org.koin.dsl.module

val featureModule = module {
  includes(homeFeatureModule, createNoteModule)
}