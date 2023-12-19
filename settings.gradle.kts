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
plugins {
    id("de.fayard.refreshVersions") version("0.60.3")
}
rootProject.name = "Waifu Viewer"
include (":app")
include (":domain")
include(":data")
include(":usecases")
include(":testShared")
