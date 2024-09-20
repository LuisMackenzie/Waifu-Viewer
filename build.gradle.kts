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
        classpath(ClassPath.composeGradlePluginCompiler)
        classpath(ClassPath.kotlinGradlePlugin)
        classpath(ClassPath.kotlinKspGradlePlugin)
        classpath(ClassPath.secretsGradlePlugin)
        classpath(ClassPath.detektGradlePlugin)
        classpath(ClassPath.playServicesGradlePlugin)
        classpath(ClassPath.crashlitycsGradlePlugin)
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.

// apply(plugin = Plugins.composePlugin)

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}