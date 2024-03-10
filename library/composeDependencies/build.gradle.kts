import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
  id("org.jetbrains.compose")
  id("org.arindom.takenotes.kmp.library")
}

kotlin {
  sourceSets {
    androidMain.dependencies {
      api(libs.compose.ui.tooling.preview)
      api(libs.androidx.activity.compose)
    }
    commonMain.dependencies {
      api(compose.runtime)
      api(compose.foundation)
      api(compose.material3)
      api(compose.ui)
      @OptIn(ExperimentalComposeLibrary::class)
      api(compose.components.resources)
    }
  }
}

android {
  namespace = "org.arindom.takenotes.composedependencies"

  dependencies {
    debugApi(libs.compose.ui.tooling)
  }
}