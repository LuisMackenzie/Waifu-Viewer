plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation (project(":data"))
    implementation (project(":domain"))
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation ("javax.inject:javax.inject:1")
    // library to use Either Class in Kotlin
    implementation ("io.arrow-kt:arrow-core:1.1.2")

}