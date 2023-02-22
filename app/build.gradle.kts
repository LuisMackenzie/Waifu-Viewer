
plugins {
    id (Plugins.application)
    id (Plugins.android)
    id (Plugins.kapt)
    id (Plugins.parcelize)
    id (Plugins.safeArgs)
    id (Plugins.hiltAndroid)
    id (Plugins.playServices)
    id (Plugins.crashlitycs)
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

    /*buildFeatures {
        dataBinding = true
    }*/

    dataBinding {
        enable = true
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

    // modules Implementation
    implementation (project(Modules.domain))
    implementation (project(Modules.data))
    implementation (project(Modules.usecases))
    implementation (project(Modules.testShared))
    testImplementation (project(Modules.testShared))
    androidTestImplementation (project(Modules.testShared))


    // implementation (AndroidX.core.ktx)
    implementation (Libs.AndroidX.coreKtx)
    // implementation (AndroidX.appCompat)
    implementation (Libs.AndroidX.appCompat)
    // implementation (Google.android.material)
    implementation (Libs.AndroidX.material)
    // implementation (AndroidX.constraintLayout)
    implementation (Libs.AndroidX.constraintLayout)

    // Permisions request
    // implementation(AndroidX.activity.ktx)
    implementation (Libs.AndroidX.Activity.ktx)
    // implementation(AndroidX.fragment.ktx)
    implementation (Libs.AndroidX.Activity.fragmentKtx)

    // Navigation
    // implementation(AndroidX.navigation.fragmentKtx)
    implementation (Libs.AndroidX.Navigation.fragmentKtx)
    // implementation(AndroidX.navigation.uiKtx)
    implementation (Libs.AndroidX.Navigation.uiKtx)

    // Corrutinas
    // implementation(AndroidX.lifecycle.runtime.ktx)
    implementation (Libs.AndroidX.Lifecycle.runtimeKtx)
    // ViewModel
    // implementation(AndroidX.lifecycle.viewModelKtx)
    implementation (Libs.AndroidX.Lifecycle.viewmodelKtx)
    // Room DB
    // implementation(AndroidX.room.runtime)
    implementation (Libs.AndroidX.Room.runtime)
    // implementation(AndroidX.room.ktx)
    implementation (Libs.AndroidX.Room.ktx)
    // kapt(AndroidX.room.compiler)
    kapt(Libs.AndroidX.Room.compiler)

    // Hilt
    // implementation(Google.dagger.hilt.android)
    implementation (Libs.Hilt.android)
    // kapt(Google.dagger.hilt.android.compiler)
    kapt (Libs.Hilt.compiler)

    // Glide libraries
    // implementation (libs.glide)
    implementation (Libs.Glide.glide)
    // kapt(libs.com.github.bumptech.glide.compiler)
    kapt(Libs.Glide.compiler)
    // annotationProcessor (libs.com.github.bumptech.glide.compiler)

    // Retrofit Libraries
    // implementation (Square.retrofit2)
    implementation (Libs.Retrofit.retrofit)
    // Gson converter
    // implementation (Square.retrofit2.converter.gson)
    // implementation ("com.google.code.gson:gson:_")

    // Moshi converters
    // implementation (Square.moshi)
    // implementation (Square.moshi.kotlinReflect)
    implementation (Libs.Retrofit.moshiKtx)
    // implementation (Square.retrofit2.converter.moshi)
    implementation (Libs.Retrofit.converterMoshi)
    // annotationProcessor (Square.moshi.kotlinCodegen)

    // OKHttp3
    implementation (Libs.OkHttp3.okhttp3)
    // implementation (Square.okHttp3)
    // Logging interceptor
    implementation (Libs.OkHttp3.loginInterceptor)
    // implementation (Square.okHttp3.loggingInterceptor)

    // library to use Either Class in Kotlin
    implementation (Libs.Arrow.core)
    // implementation (Arrow.core)

    // Paging library
    // implementation (Libs.AndroidX.Paging.runtime)
    // implementation (AndroidX.paging.runtime)
    implementation (Libs.AndroidX.Paging.runtimeKtx)
    // implementation (AndroidX.paging.runtimeKtx)
    implementation (Libs.AndroidX.Room.paging)
    // implementation (AndroidX.room.paging)
    // implementation(Libs.AndroidX.Room.pagingAlpha)

    // Firebase
    // implementation (platform(Firebase.bom))
    implementation (platform(Libs.Firebase.coreBom))
    // implementation(Firebase.analyticsKtx)
    implementation(Libs.Firebase.analitycsKtx)
    // implementation(Firebase.remoteConfigKtx)
    implementation(Libs.Firebase.remoteconfigKtx)
    // implementation(Firebase.crashlyticsKtx)
    implementation(Libs.Firebase.crashlitycsKtx)


    // Serialization
    // implementation(JakeWharton.retrofit2.converter.kotlinxSerialization)
    // implementation(KotlinX.serialization.json)

    // Lottie files
    // implementation(libs.lottie)
    implementation(Libs.Lottie.core)

    // Shimmer
    // implementation(libs.shimmer)
    implementation(Libs.Shimmer.shimmer)

    // PhotoView imageviewer with zoom
    // implementation(libs.photoview)
    implementation(Libs.PhotoView.core)

    // Location Play services
    // implementation(Google.android.playServices.location)
    implementation(Libs.Gradle.playServicesLocation)

    // JUnit y Mockito

    testImplementation (Libs.JUnit.junit)
    // testImplementation (Testing.junit4)
    testImplementation (Libs.Mockito.kotlin)
    // testImplementation (libs.mockito.kotlin)
    testImplementation (Libs.Mockito.inline)
    // testImplementation (Testing.mockito.inline)
    testImplementation (Libs.Coroutines.test)
    // testImplementation (KotlinX.coroutines.test)
    testImplementation (Libs.Turbine.core)
    // testImplementation (CashApp.turbine)

    androidTestImplementation (Libs.AndroidX.Test.Ext.junit)
    // androidTestImplementation (AndroidX.test.ext.junit.ktx)
    androidTestImplementation (Libs.AndroidX.Test.Espresso.contrib)
    // androidTestImplementation (AndroidX.test.espresso.contrib)
    androidTestImplementation (Libs.AndroidX.Test.Runner.runner)
    // androidTestImplementation (AndroidX.test.runner)
    androidTestImplementation (Libs.AndroidX.Test.Runner.rules)
    // androidTestImplementation (AndroidX.test.rules)
    androidTestImplementation (Libs.Coroutines.test)
    // androidTestImplementation (KotlinX.coroutines.test)
    // For instrumentation tests
    androidTestImplementation (Libs.Hilt.test)
    // androidTestImplementation (Google.dagger.hilt.android.testing)

    kaptAndroidTest(Libs.Hilt.compiler)
    // kaptAndroidTest (Google.dagger.hilt.android.compiler)
    // For MockwebServer
    androidTestImplementation (Libs.OkHttp3.mockWebServer)
    // androidTestImplementation (Square.okHttp3.mockWebServer)

}