plugins {
  id("org.arindom.takenotes.kmp.library")
  id("org.arindom.takenotes.koin")
}
kotlin {
  sourceSets {
    commonMain.dependencies {
      api(libs.kotlinx.coroutines.core)
    }

    androidMain.dependencies {
      api(libs.kotlinx.coroutines.android)
    }

    commonTest.dependencies {
      api(libs.kotlinx.coroutines.test)
    }
  }
}

android {
  namespace = "org.arindom.takenotes.coroutines"
}