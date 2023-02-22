buildscript {
    dependencies {
        classpath(Google.playServicesGradlePlugin)
        classpath(Firebase.crashlyticsGradlePlugin)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("com.android.application") apply false
    id ("com.android.library") apply false
    id ("org.jetbrains.kotlin.android") version "1.8.20-Beta" apply false
    id ("org.jetbrains.kotlin.jvm") version "1.8.20-Beta" apply false
    id ("androidx.navigation.safeargs.kotlin") apply false
    id ("com.google.dagger.hilt.android") apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}