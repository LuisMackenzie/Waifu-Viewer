plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id ("kotlin-kapt")
    id ("androidx.navigation.safeargs.kotlin")
    // id ("org.jetbrains.kotlin.plugin.serialization")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.mackenzie.waifuviewer"
        minSdk = 24
        targetSdk = 32
        versionCode = 1
        versionName = "0.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
    namespace = "com.mackenzie.waifuviewer"
}

dependencies {

    val retrofit_version = "2.9.0"
    val roomVersion = "2.4.2"

    implementation ("androidx.core:core-ktx:1.8.0")
    implementation ("androidx.appcompat:appcompat:1.4.2")
    implementation ("com.google.android.material:material:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    // Permisions request
    implementation("androidx.activity:activity-ktx:1.5.0")
    implementation("androidx.fragment:fragment-ktx:1.5.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.0")

    // Corrutinas
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0")
    // Room DB
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Glide libraries
    implementation ("com.github.bumptech.glide:glide:4.13.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.2")
    // Retrofit Libraries
    implementation ("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation ("com.google.code.gson:gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofit_version")
    // OKHttp3 Logging interceptor
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    // library to parse JSON in Kotlin
    implementation ("com.beust:klaxon:5.5")


    // Serialization
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")


    // Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // PhotoView imageviewer with zoom
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    // Location Play services
    implementation("com.google.android.gms:play-services-location:20.0.0")

    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
}