buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(ClassPath.androidGradlePlugin)
        // classpath(ClassPath.gradleVersionsPlugin)
        classpath(ClassPath.navigationSafeArgs)
        classpath(ClassPath.hiltAndroidGradlePlugin)
        classpath(ClassPath.kotlinGradlePlugin)
        classpath(ClassPath.secretsGradlePlugin)
        classpath(ClassPath.detektGradlePlugin)
        classpath(ClassPath.playServicesGradlePlugin)
        classpath(ClassPath.crashlitycsGradlePlugin)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
/*plugins {
    // id ("com.android.application") apply false
    // id ("com.android.library") apply false
    // id ("org.jetbrains.kotlin.android") version "1.8.20-Beta" apply false
    // id ("org.jetbrains.kotlin.jvm") version "1.8.20-Beta" apply false
    // id ("androidx.navigation.safeargs.kotlin") apply false
    // id ("com.google.dagger.hilt.android") apply false
}*/

// apply(from = "gradle-scripts/detekt.gradle")

// apply(from = "com.github.ben-manes.versions")

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}