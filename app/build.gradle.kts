
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
    val roomVersion = "2.5.0"

    // modules Implementation
    implementation (project(Modules.domain))
    implementation (project(Modules.data))
    implementation (project(Modules.usecases))
    implementation (project(Modules.testShared))
    testImplementation (project(Modules.testShared))
    androidTestImplementation (project(Modules.testShared))


    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    // Permisions request
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.fragment:fragment-ktx:1.5.5")

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
    implementation("com.google.dagger:hilt-android:2.45")
    kapt("com.google.dagger:hilt-android-compiler:2.45")

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
    implementation ("com.squareup.retrofit2:converter-moshi:$retrofit_version")
    // annotationProcessor ("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")

    // OKHttp3
    // implementation (Libs.OkHttp3.okhttp3)
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    // Logging interceptor
    // implementation (Libs.OkHttp3.loginInterceptor)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // library to use Either Class in Kotlin
    // implementation (Libs.Arrow.core)
    implementation ("io.arrow-kt:arrow-core:1.1.5")

    // Paging library
    // implementation (Libs.AndroidX.Paging.runtime)
    implementation ("androidx.paging:paging-runtime:3.1.1")
    // implementation (Libs.AndroidX.Paging.runtimeKtx)
    implementation ("androidx.paging:paging-runtime-ktx:3.1.1")
    // implementation (Libs.AndroidX.Room.paging)
    implementation ("androidx.room:room-paging:2.5.0")
    // implementation(Libs.AndroidX.Room.pagingAlpha)

    // Firebase
    implementation (platform("com.google.firebase:firebase-bom:31.1.0"))
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

    // testImplementation (Libs.JUnit.junit)
    testImplementation ("junit:junit:4.13.2")
    // testImplementation (Libs.Mockito.kotlin)
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.1.0")
    // testImplementation (Libs.Mockito.inline)
    testImplementation ("org.mockito:mockito-inline:5.1.1")
    // testImplementation (Libs.Coroutines.test)
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    // testImplementation (Libs.Turbine.core)
    testImplementation ("app.cash.turbine:turbine:0.12.1")

    // androidTestImplementation (Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation ("androidx.test.ext:junit-ktx:1.1.5")
    // androidTestImplementation (Libs.AndroidX.Test.Espresso.contrib)
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.5.1")
    // androidTestImplementation (Libs.AndroidX.Test.Runner.runner)
    androidTestImplementation ("androidx.test:runner:1.5.2")
    // androidTestImplementation (Libs.AndroidX.Test.Runner.rules)
    androidTestImplementation ("androidx.test:rules:1.5.0")
    // androidTestImplementation (Libs.Coroutines.test)
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    // For instrumentation tests
    // androidTestImplementation (Libs.Hilt.test)
    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.45")

    // kaptAndroidTest(Libs.Hilt.compiler)
    kaptAndroidTest ("com.google.dagger:hilt-android-compiler:2.44")
    // For MockwebServer
    // androidTestImplementation (Libs.OkHttp3.mockWebServer)
    androidTestImplementation ("com.squareup.okhttp3:mockwebserver:4.10.0")

}