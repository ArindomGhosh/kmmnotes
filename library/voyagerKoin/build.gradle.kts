plugins {
  id("org.arindom.takenotes.kmp.library")
  id("org.arindom.takenotes.koin")
  id("org.jetbrains.compose")
}

kotlin {
  sourceSets{
    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.runtimeSaveable)
      implementation(libs.voyager.screenModel)
      implementation(libs.voyager.navigator)
    }
  }
}

android {
  namespace = "org.arindom.takenotes.voyager.kion"
}