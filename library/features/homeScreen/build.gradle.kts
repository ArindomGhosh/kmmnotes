plugins {
  id("org.arindom.takenotes.compose.library")
  id("org.arindom.takenotes.koin")
  id("org.arindom.takenotes.voyager")
}
kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.koin.compose)
      implementation(libs.kotlinx.collections)
      implementation(libs.kotlinx.datetime)
      implementation(projects.library.ui)
      implementation(projects.library.database)
      implementation(projects.library.navigation)
      implementation(projects.library.data)
    }
    androidMain.dependencies {

    }
  }
}

android {
  namespace = "org.arindom.takenotes.homescreen"
}