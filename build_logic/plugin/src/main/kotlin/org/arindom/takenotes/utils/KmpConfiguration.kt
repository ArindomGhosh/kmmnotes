package org.arindom.takenotes.utils

import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun Project.configureKmp() {
  pluginManager.apply {
    apply("org.jetbrains.kotlin.multiplatform")
  }
  val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
  extensions.configure<KotlinMultiplatformExtension> {
    androidTarget {
      compilations.all {
        kotlinOptions {
          jvmTarget = JavaVersion.VERSION_17.toString()
        }
      }
      sourceSets{
        commonMain.dependencies {
          implementation(libs.findLibrary("napier").get())
        }
        targets.all {
          compilations.all{
            kotlinOptions {
              freeCompilerArgs += "-Xexpect-actual-classes"
            }
          }
        }
      }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
  }
}

internal fun KotlinMultiplatformExtension.`sourceSets`(configure: Action<NamedDomainObjectContainer<KotlinSourceSet>>): Unit =
  (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("sourceSets", configure)