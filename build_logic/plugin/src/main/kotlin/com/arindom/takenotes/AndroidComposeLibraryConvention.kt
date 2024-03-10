package com.arindom.takenotes

import com.android.build.gradle.LibraryExtension
import com.arindom.takenotes.utils.configureAndroid
import com.arindom.takenotes.utils.configureKmp
import configureKotlin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
class AndroidLibraryConvention : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply {
        apply("com.android.library")
//        apply("org.jetbrains.kotlin.android")
      }
      configureKmp()
      extensions.configure<LibraryExtension>{
        configureAndroid(this)
        configureKotlin(this)
      }
    }
  }
}