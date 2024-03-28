
plugins {
    id(Plugins.application)
    id(Plugins.android)
    id(Plugins.kapt)
    id(Plugins.parcelize)
    id(Plugins.safeArgs)
    id(Plugins.hiltAndroid)
    id(Plugins.playServices)
    id(Plugins.crashlitycs)
    id(Plugins.secrets)
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
            // applicationIdSuffix = ".release"
            versionNameSuffix = "-RELEASE"
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
            isDebuggable = true
        }
        create("enhanced") {
            // applicationIdSuffix = ".prime"
            versionNameSuffix = "-PRIME"
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    /*dataBinding {
        enable = true
    }*/

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }

    sourceSets {
        this.getByName("androidTest") {
            this.java.srcDir("$projectDir/src/testShared/androidTest")
        }
        this.getByName("test") {
            this.java.srcDir("$projectDir/src/testShared/test")
        }
    }

    packaging {
        // exclude("META-INF/versions/9/previous-compilation-data.bin"
        resources.excludes.add("META-INF/versions/9/previous-compilation-data.bin")
    }

    namespace = "com.mackenzie.waifuviewer"
}

dependencies {

    // modules Implementation
    implementation(project(Modules.domain))
    implementation(project(Modules.data))
    implementation(project(Modules.usecases))
    implementation(project(Modules.testShared))
    testImplementation(project(Modules.testShared))
    androidTestImplementation(project(Modules.testShared))

    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.constraintLayout)

    // Permisions request
    implementation(Libs.AndroidX.Activity.ktx)
    implementation(Libs.AndroidX.Activity.fragmentKtx)

    // Navigation
    implementation(Libs.AndroidX.Navigation.fragmentKtx)
    implementation(Libs.AndroidX.Navigation.uiKtx)

    // Corrutinas
    // implementation(AndroidX.lifecycle.runtime.ktx)
    implementation(Libs.AndroidX.Lifecycle.runtimeKtx)
    // ViewModel
    implementation(Libs.AndroidX.Lifecycle.viewmodelKtx)
    // Room DB
    implementation(Libs.AndroidX.Room.runtime)
    implementation(Libs.AndroidX.Room.ktx)
    kapt(Libs.AndroidX.Room.compiler)

    // Hilt
    implementation(Libs.Hilt.android)
    kapt(Libs.Hilt.compiler)

    // Glide libraries
    implementation(Libs.Glide.glide)
    kapt(Libs.Glide.compiler)

    // Retrofit Libraries
    implementation(Libs.Retrofit.retrofit)

    // Moshi converters
    implementation(Libs.Retrofit.moshiKtx)
    implementation(Libs.Retrofit.converterMoshi)
    // annotationProcessor (Square.moshi.kotlinCodegen)

    // OKHttp3
    implementation(Libs.OkHttp3.okhttp3)
    // Logging interceptor
    implementation(Libs.OkHttp3.loginInterceptor)

    // library to use Either Class in Kotlin
    implementation(Libs.Arrow.core)

    // Paging library
    // implementation (Libs.AndroidX.Paging.runtime)
    implementation(Libs.AndroidX.Paging.runtimeKtx)
    implementation(Libs.AndroidX.Room.paging)

    // Firebase
    implementation(platform(Libs.Firebase.coreBom))
    implementation(Libs.Firebase.analitycsKtx)
    implementation(Libs.Firebase.remoteconfigKtx)
    implementation(Libs.Firebase.crashlitycsKtx)

    // Serialization
    // implementation(JakeWharton.retrofit2.converter.kotlinxSerialization)
    // implementation(KotlinX.serialization.json)

    // Lottie files
    implementation(Libs.Lottie.core)

    // Shimmer
    implementation(Libs.Shimmer.shimmer)

    // PhotoView imageviewer with zoom
    implementation(Libs.PhotoView.core)

    // Location Play services
    implementation(Libs.Gradle.playServicesLocation)

    // Google AI SDK for Android
    // implementation("com.google.ai.client.generativeai:generativeai:0.2.2")
    implementation(Libs.GenerativeAI.generativeai)

    // JUnit y Mockito
    testImplementation(Libs.JUnit.junit)
    testImplementation(Libs.Mockito.kotlin)
    testImplementation(Libs.Mockito.inline)
    testImplementation(Libs.Coroutines.test)
    testImplementation(Libs.Turbine.core)

    // For instrumentation tests
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.Espresso.contrib)
    androidTestImplementation(Libs.AndroidX.Test.Runner.runner)
    androidTestImplementation(Libs.AndroidX.Test.Runner.rules)
    androidTestImplementation(Libs.Coroutines.test)
    androidTestImplementation(Libs.Hilt.test)
    kaptAndroidTest(Libs.Hilt.compiler)
    // For MockwebServer
    androidTestImplementation(Libs.OkHttp3.mockWebServer)


}

