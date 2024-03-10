package org.arindom.takenotes

import com.android.build.api.dsl.ApplicationExtension
import org.arindom.takenotes.utils.configureAndroid
import org.arindom.takenotes.utils.configureCompose
import org.arindom.takenotes.utils.configureCoroutine
import org.arindom.takenotes.utils.configureKmp
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConvention : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply {
        apply("com.android.application")
      }
      configureKmp()
      val libs = extensions.getByType(VersionCatalogsExtension::class).named("libs")

      extensions.configure<ApplicationExtension> {
        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
        sourceSets["main"].res.srcDirs("src/androidMain/res")
        sourceSets["main"].resources.srcDirs("src/commonMain/resources")
        configureAndroid(this)
        defaultConfig {
          versionCode = libs.findVersion("applicationVersionCode").get().toString().toInt()
          versionName = libs.findVersion("applicationVersionName").get().toString()
        }
        configureCompose(this)
        configureCoroutine()
      }
    }
  }
}