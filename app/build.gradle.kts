
plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("org.jetbrains.kotlin.kapt")
    id ("kotlin-parcelize")
    id ("androidx.navigation.safeargs.kotlin")
    id ("dagger.hilt.android.plugin")
    id ("com.google.gms.google-services")
    id ("com.google.firebase.crashlytics")
}

android {
    compileSdk = AppConfig.compileSdk

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        testInstrumentationRunner = AppConfig.testInstrumentationRunnerHilt
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

    sourceSets {
        this.getByName("androidTest"){
            //Adds the given source directory to this set.
            this.java.srcDir("$projectDir/src/testShared/androidTest")
        }
        this.getByName("test"){
            this.java.srcDir("$projectDir/src/testShared/test")
        }
    }

    namespace = "com.mackenzie.waifuviewer"
}

dependencies {


    val retrofit_version = "2.9.0"
    val roomVersion = "2.4.3"

    // modules Implementation
    implementation (project(Modules.domain))
    implementation (project(Modules.data))
    implementation (project(Modules.usecases))


    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("androidx.appcompat:appcompat:1.5.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    // Permisions request
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.fragment:fragment-ktx:1.5.4")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // Corrutinas
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    // Room DB
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    // implementation("androidx.room:room-compiler:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    // Glide libraries
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    kapt("com.github.bumptech.glide:compiler:4.14.2")
    // annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")

    // Retrofit Libraries
    implementation ("com.squareup.retrofit2:retrofit:$retrofit_version")
    // Gson converter
    // implementation ("com.squareup.retrofit2:converter-gson:$retrofit_version")
    // implementation ("com.google.code.gson:gson:2.10")

    // Moshi converters
    // implementation ("com.squareup.moshi:moshi:1.12.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.12.0")
    annotationProcessor ("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    implementation ("com.squareup.retrofit2:converter-moshi:$retrofit_version")

    // OKHttp3
    implementation (Libs.OkHttp3.okhttp3)
    // Logging interceptor
    implementation (Libs.OkHttp3.loginInterceptor)
    // library to parse JSON in Kotlin
    // implementation ("com.beust:klaxon:5.6")

    // library to use Either Class in Kotlin
    implementation (Libs.Arrow.core)

    // Paging library
    implementation (Libs.AndroidX.Paging.runtime)
    implementation (Libs.AndroidX.Paging.runtimeKtx)
    implementation (Libs.AndroidX.Room.paging)
    // implementation(Libs.AndroidX.Room.pagingAlpha)

    // Firebase
    implementation (platform("com.google.firebase:firebase-bom:30.5.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")


    // Serialization
    // implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    // implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    // Lottie files
    implementation("com.airbnb.android:lottie:5.2.0")

    // Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // PhotoView imageviewer with zoom
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    // Location Play services
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // JUnit y Mockito
    testImplementation (project(Modules.testShared))
    
    testImplementation (Libs.JUnit.junit)
    testImplementation (Libs.Mockito.kotlin)
    testImplementation (Libs.Mockito.inline)
    testImplementation (Libs.Coroutines.test)
    testImplementation (Libs.Turbine.core)

    androidTestImplementation (Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation (Libs.AndroidX.Test.Espresso.contrib)
    androidTestImplementation (Libs.AndroidX.Test.Runner.runner)
    androidTestImplementation (Libs.AndroidX.Test.Runner.rules)
    androidTestImplementation (Libs.Coroutines.test)
    // For instrumentation tests
    androidTestImplementation (Libs.Hilt.test)
    kaptAndroidTest(Libs.Hilt.compiler)
    // For MockwebServer
    androidTestImplementation (Libs.OkHttp3.mockWebServer)

}