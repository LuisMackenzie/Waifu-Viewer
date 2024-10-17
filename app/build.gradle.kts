import java.util.Properties

plugins {
    id(Plugins.application)
    id(Plugins.android)
    id(Plugins.kapt)
    id(Plugins.ksp)
    id(Plugins.parcelize)
    id(Plugins.safeArgs)
    id(Plugins.hiltAndroid)
    id(Plugins.playServices)
    id(Plugins.crashlitycs)
    id(Plugins.secrets)
    id(Plugins.composePlugin)
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
        /*vectorDrawables {
            useSupportLibrary = true
        }*/

    }

    signingConfigs {
        val properties = Properties().apply {
            load(File(secrets.propertiesFileName).reader())
        }
        getByName(SignConstants.variantNameDebug) {
            keyAlias = (properties[SignConstants.keyAliasSign] ?: "") as String
            keyPassword = (properties[SignConstants.keyPassword] ?: "") as String
            storeFile = file((properties[SignConstants.keyStoreFile] ?: "") as String)
            storePassword = (properties[SignConstants.storePassword] ?: "") as String
        }
        create(SignConstants.variantNameRelease) {
            keyAlias = (properties[SignConstants.keyAliasSign] ?: "") as String
            keyPassword = (properties[SignConstants.keyPassword] ?: "") as String
            storeFile = file((properties[SignConstants.keyStoreFile] ?: "") as String)
            storePassword = (properties[SignConstants.storePassword] ?: "") as String
            enableV2Signing = true
        }
        create(SignConstants.variantNameEnhanced) {
            keyAlias = (properties[SignConstants.keyAliasSign] ?: "") as String
            keyPassword = (properties[SignConstants.keyPassword] ?: "") as String
            storeFile = file((properties[SignConstants.keyStoreFile] ?: "") as String)
            storePassword = (properties[SignConstants.storePassword] ?: "") as String
            enableV2Signing = true
        }
    }

    buildTypes {
        getByName(SignConstants.variantNameDebug) {
            applicationIdSuffix = SignConstants.appIdSuffixDebug
            versionNameSuffix = SignConstants.versionNameSuffixDebug
            isDebuggable = true
            resValue(Constants.type, SignConstants.varAppName, SignConstants.valueAppNameDebug)
            resValue(Constants.type, SignConstants.varWaifuViewer, SignConstants.homeWaifuViewerDebug)
            signingConfig = signingConfigs.getByName(SignConstants.variantNameDebug)
        }
        getByName(SignConstants.variantNameRelease) {
            isMinifyEnabled = false
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        compose = true
        // viewBinding = true
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
        resources {
            excludes += Constants.metaLicenses
            excludes += Constants.metaInf
        }
        // resources.excludes.add(Constants.metaInf)
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

    // Compose Libraries
    val composeBom = platform(Libs.AndroidX.Compose.bom)
    implementation(composeBom)
    implementation(Libs.AndroidX.Compose.material3)
    implementation(Libs.AndroidX.Compose.foundation)
    implementation(Libs.AndroidX.Compose.runtime)
    implementation(Libs.AndroidX.Compose.livedata)
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.animation)
    implementation(Libs.AndroidX.Compose.preview)
    implementation(Libs.AndroidX.Compose.navigation)

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
    ksp(Libs.AndroidX.Room.compiler)

    // Hilt
    implementation(Libs.Hilt.android)
    ksp(Libs.Hilt.compiler)

    // Glide libraries
    implementation(Libs.Glide.glide)
    ksp(Libs.Glide.compiler)

    // Coil libraries
    implementation(Libs.Coil.coil)
    implementation(Libs.Coil.coilNetwork)

    // Retrofit Libraries
    implementation(Libs.Retrofit.retrofit)

    // Moshi converters
    implementation(Libs.Retrofit.moshiKtx)
    implementation(Libs.Retrofit.converterMoshi)
    // ksp (Square.moshi.kotlinCodegen)

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
    implementation(Libs.Lottie.compose)

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
    kspAndroidTest(Libs.Hilt.compiler)
    // For MockwebServer
    androidTestImplementation(Libs.OkHttp3.mockWebServer)

    // For UI tests with compose
    androidTestImplementation(composeBom)
    androidTestImplementation(Libs.AndroidX.ComposeTesting.junit4AndroidTest)
    debugImplementation(Libs.AndroidX.ComposeTesting.tooling)
    debugImplementation(Libs.AndroidX.ComposeTesting.manifestDebugTest)


}

