
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { setUrl("https://www.jitpack.io") }
        google()
        mavenCentral()
    }
}
/*plugins {
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.0-Beta"
}*/
rootProject.name = "Waifu Viewer"
include (":app")
