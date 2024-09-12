plugins {
    id(Plugins.javaLibrary)
    id(Plugins.kotlinJVM)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(Modules.data))
    implementation(project(Modules.domain))
    implementation(Libs.Coroutines.core)
    implementation(Libs.JavaX.inject)
    implementation(Libs.Arrow.core)

    // JUnit y Mockito
    testImplementation(project(Modules.testShared))
    testImplementation(Libs.Mockito.kotlin)
    testImplementation(Libs.Mockito.inline)
    testImplementation(Libs.JUnit.junit)
}