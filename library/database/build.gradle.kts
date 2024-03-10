plugins {
  id("org.arindom.takenotes.kmp.library")
  id("org.arindom.takenotes.koin")
  alias(libs.plugins.sqlDelight)
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(projects.library.coroutines)
      implementation(libs.sqldelight.primitive.adapters)
      implementation(libs.sqldelight.coroutines.extensions)
//      implementation(libs.sqldelight.async.extensions)
    }
    androidMain.dependencies {
      implementation(libs.koin.android)
      implementation(libs.sqldelight.android.driver)
    }

    iosMain.dependencies {
      implementation(libs.sqldelight.native.driver)
    }
  }
}


android {
  namespace = "org.arindom.takenotes.database"
}

sqldelight {
  databases {
    create("TakeNotesDatabase") {
      packageName.set("org.arindom.takenotes.database")
    }
  }
  linkSqlite.set(true)
}