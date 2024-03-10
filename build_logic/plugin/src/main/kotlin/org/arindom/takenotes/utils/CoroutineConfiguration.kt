package org.arindom.takenotes.utils

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureCoroutine() {
  pluginManager.apply {
    apply("org.jetbrains.kotlin.multiplatform")
  }
  extensions.configure<KotlinMultiplatformExtension> {
    sourceSets {
      commonMain.dependencies {
        implementation(project(":library:coroutines"))
      }
    }
  }
}