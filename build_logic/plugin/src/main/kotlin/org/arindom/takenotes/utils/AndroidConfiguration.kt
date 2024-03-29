package org.arindom.takenotes.utils

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType


fun Project.configureAndroid(
  commonExtension: CommonExtension<*, *, *, *, *>
) {
  val libs = extensions.getByType(VersionCatalogsExtension::class).named("libs")
  commonExtension.apply {
    compileSdk = libs.findVersion("android.compileSdk").get().toString().toInt()
    defaultConfig {
      minSdk = libs.findVersion("android.minSdk").get().toString().toInt()
    }
    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
    }
  }
}