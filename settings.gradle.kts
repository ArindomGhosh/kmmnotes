rootProject.name = "TakeNote"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    includeBuild("build_logic")
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":composeApp")
include(":library:ui")
include(":library:data")
include(":library:navigation")
include(":library:composeDependencies")
include(":library:features:homeScreen")
include(":library:coroutines")
include(":library:voyagerKoin")
include(":library:database")
include(":library:features:createNotes")