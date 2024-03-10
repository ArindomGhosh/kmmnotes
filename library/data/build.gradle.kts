plugins {
  id("org.arindom.takenotes.kmp.library")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.kotlinx.coroutines.core)
    }
  }
}

android {
  namespace = "org.arindom.takenotes.data"
}