buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id ("com.android.application") version "7.3.1" apply false
    id ("com.android.library") version "7.3.1" apply false
    id ("org.jetbrains.kotlin.android") version "1.7.20" apply false
    id ("org.jetbrains.kotlin.jvm") version "1.7.20" apply false
    id ("androidx.navigation.safeargs.kotlin") version "2.4.2" apply false
    id ("com.google.dagger.hilt.android") version "2.43.2" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}