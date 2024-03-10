plugins {
  id("org.arindom.takenotes.kmp.library")
  id("org.arindom.takenotes.voyager")
}

kotlin{
  sourceSets{
    commonMain.dependencies {

    }
  }
}

android {
  namespace = "org.arindom.takenotes.voyager.kion"
}