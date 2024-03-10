package org.arindom.takenotes

import org.arindom.takenotes.utils.sourceSets
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class VoyagersConvention : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply {
        apply("org.jetbrains.kotlin.multiplatform")
      }

      val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

      extensions.configure<KotlinMultiplatformExtension> {
        sourceSets {
          commonMain.dependencies {
            implementation(libs.findLibrary("voyagers.navigator"))
            implementation(libs.findLibrary("voyagers.screenModel"))
          }
        }
      }

    }
  }
}