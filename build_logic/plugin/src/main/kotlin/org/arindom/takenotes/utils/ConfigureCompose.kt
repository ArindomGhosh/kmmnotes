package org.arindom.takenotes.utils

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.configureCompose(
  commonExtension: CommonExtension<*, *, *, *, *>
) {
  pluginManager.apply {
    apply("org.jetbrains.compose")
    apply("org.jetbrains.kotlin.multiplatform")
  }

  val libs = extensions.getByType(VersionCatalogsExtension::class).named("libs")
  with(commonExtension) {
    buildFeatures {
      compose = true
    }
    composeOptions {
      kotlinCompilerExtensionVersion =
        libs.findVersion("compose.compiler").get().toString()
    }
  }
  extensions.configure<KotlinMultiplatformExtension>{
    sourceSets{
      commonMain.dependencies {
        implementation(project(":library:composeDependencies"))
      }
    }
  }
}