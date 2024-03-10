plugins {
  id("org.arindom.takenotes.application")
  id("org.arindom.takenotes.koin")
}

kotlin {
  listOf(
    iosX64(),
    iosArm64(),
    iosSimulatorArm64()
  ).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "ComposeApp"
      linkerOpts.add("-lsqlite3")
    }
  }

  sourceSets {
    commonMain.dependencies {
      implementation(libs.voyager.navigator)
      implementation(libs.koin.compose)
      implementation(projects.library.ui)
      implementation(projects.library.navigation)
      implementation(projects.library.features.homeScreen)
      implementation(projects.library.features.createNotes)
    }
  }
}

android {
  namespace = "org.arindom.takenotes"

  defaultConfig {
    applicationId = "org.arindom.takenotes"
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }

  dependencies {
    implementation(libs.koin.android)
  }
}

