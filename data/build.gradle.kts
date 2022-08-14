plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation (project(Modules.domain))
    implementation (Libs.Coroutines.core)
    implementation (Libs.JavaX.inject)
    implementation (Libs.Arrow.core)

    // JUnit y Mockito
    testImplementation (Libs.JUnit.junit)
    testImplementation (Libs.Mockito.kotlin)
    testImplementation (Libs.Mockito.inline)
}