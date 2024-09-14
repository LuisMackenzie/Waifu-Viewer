buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(ClassPath.androidGradlePlugin)
        classpath(ClassPath.navigationSafeArgs)
        classpath(ClassPath.hiltAndroidGradlePlugin)
        classpath(ClassPath.kotlinGradlePlugin)
        classpath(ClassPath.kotlinKspGradlePlugin)
        classpath(ClassPath.secretsGradlePlugin)
        classpath(ClassPath.detektGradlePlugin)
        classpath(ClassPath.playServicesGradlePlugin)
        classpath(ClassPath.crashlitycsGradlePlugin)
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.

// apply(from = "gradle-scripts/detekt.gradle")

// apply(from = "com.github.ben-manes.versions")

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}