package org.arindom.takenotes

import com.android.build.gradle.LibraryExtension
import org.arindom.takenotes.utils.configureAndroid
import org.arindom.takenotes.utils.configureKmp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KmpLibraryConvention : Plugin<Project> {
  override fun apply(target: Project) {
    with(target){
      pluginManager.apply {
        apply("org.jetbrains.kotlin.multiplatform")
        apply("com.android.library")
      }
      configureKmp()
      extensions.configure<LibraryExtension> {
        configureAndroid(this)
      }
    }
  }
}