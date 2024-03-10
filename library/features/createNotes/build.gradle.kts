plugins {
  id("org.arindom.takenotes.compose.library")
  id("org.arindom.takenotes.koin")
  id("org.arindom.takenotes.voyager")
  id(libs.plugins.ksp.get().pluginId)
}
kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.koin.compose)
      implementation(libs.kotlinx.collections)
      implementation(libs.kotlinx.datetime)
      implementation(projects.library.ui)
      implementation(projects.library.database)
      implementation(projects.library.data)
    }

    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.mokative)
      implementation(libs.kotlinx.coroutines.test)
    }
    androidMain.dependencies {

    }
  }
}
dependencies {
  configurations
    .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
    .forEach {
      add(it.name, libs.mokative.processor)
    }
}


android {
  namespace = "org.arindom.takenotes.createNotes"
}