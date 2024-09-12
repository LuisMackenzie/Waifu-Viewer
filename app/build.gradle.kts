import java.util.Properties

plugins {
    id(Plugins.application)
    id(Plugins.android)
    id(Plugins.kapt)
    // id(Plugins.ksp)
    id(Plugins.parcelize)
    id(Plugins.safeArgs)
    id(Plugins.hiltAndroid)
    id(Plugins.playServices)
    id(Plugins.crashlitycs)
    id(Plugins.secrets)
}

android {
    compileSdk = AppConfig.compileSdk
    namespace = AppConfig.applicationId

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        testInstrumentationRunner = AppConfig.testInstrumentationRunnerHilt

    }

    signingConfigs {
        val properties = Properties().apply {
            load(File(secrets.propertiesFileName).reader())
        }
        create(SignConstants.variantNameRelease) {
            keyAlias = (properties[SignConstants.keyAliasSign] ?: "") as String
            keyPassword = (properties[SignConstants.keyPassword] ?: "") as String
            storeFile = file((properties[SignConstants.keyStoreFile] ?: "") as String)
            storePassword = (properties[SignConstants.storePassword] ?: "") as String
        }
        create(SignConstants.variantNameEnhanced) {
            keyAlias = (properties[SignConstants.keyAliasSign] ?: "") as String
            keyPassword = (properties[SignConstants.keyPassword] ?: "") as String
            storeFile = file((properties[SignConstants.keyStoreFile] ?: "") as String)
            storePassword = (properties[SignConstants.storePassword] ?: "") as String
        }
    }

    buildTypes {
        getByName(SignConstants.variantNameDebug) {
            applicationIdSuffix = SignConstants.appIdSuffixDebug
            versionNameSuffix = SignConstants.versionNameSuffixDebug
            isDebuggable = true
            resValue(Constants.type, SignConstants.varAppName, SignConstants.valueAppNameDebug)
            resValue(Constants.type, SignConstants.varWaifuViewer, SignConstants.homeWaifuViewerDebug)
        }
        getByName(SignConstants.variantNameRelease) {
            isMinifyEnabled = false
            // applicationIdSuffix = ".release"
            versionNameSuffix = SignConstants.versionNameSuffixRelease
            proguardFiles(getDefaultProguardFile(Constants.proGuardFile), Constants.proGuardRules)
            signingConfig = signingConfigs.getByName(SignConstants.variantNameRelease)
        }
        create(SignConstants.variantNameEnhanced) {
            isMinifyEnabled = false
            applicationIdSuffix = SignConstants.appIdSuffixEnhanced
            versionNameSuffix = SignConstants.versionNameSuffixEnhanced
            resValue(Constants.type, SignConstants.varAppName, SignConstants.valueAppNameEnhanced)
            resValue(Constants.type, SignConstants.varWaifuViewer, SignConstants.homeWaifuViewerEnhanced)
            proguardFiles(getDefaultProguardFile(Constants.proGuardFile), Constants.proGuardRules)
            signingConfig = signingConfigs.getByName(SignConstants.variantNameEnhanced)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }

    sourceSets {
        this.getByName(Constants.androidTestSSName) {
            this.java.srcDir("$projectDir/src/testShared/androidTest")
        }
        this.getByName(Constants.testSSName) {
            this.java.srcDir("$projectDir/src/testShared/test")
        }
    }

    packaging {
        resources.excludes.add(Constants.metaInf)
    }
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
    // ksp(Libs.AndroidX.Room.compiler)

    // Hilt
    implementation(Libs.Hilt.android)
    kapt(Libs.Hilt.compiler)
    // ksp(Libs.Hilt.compiler)

    // Glide libraries
    implementation(Libs.Glide.glide)
    kapt(Libs.Glide.compiler)
    // ksp(Libs.Glide.compiler)

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

    // Kotlin KSP
    // implementation("com.google.devtools.ksp:symbol-processing-api:2.0.20-1.0.24")

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
    // kspAndroidTest(Libs.Hilt.compiler)
    // For MockwebServer
    androidTestImplementation(Libs.OkHttp3.mockWebServer)


}

