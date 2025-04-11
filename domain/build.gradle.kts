plugins {
    id(Plugins.javaLibrary)
    id(Plugins.kotlinJVM)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}
