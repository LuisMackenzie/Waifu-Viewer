// @file:Suppress("unused")

object Libs {

    const val androidGradlePlugin = "com.android.tools.build:gradle:7.2.2" // Updated
    const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:0.42.0"
    const val playServicesLocation = "com.google.android.gms:play-services-location:20.0.0"

    object Kotlin {
        private const val version = "1.7.20-Beta" // Updated
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin.gradle-plugin:$version"
    }

    object AndroidX {

        const val coreKtx = "androidx.core:core-ktx:1.9.0" // updated
        const val appCompat = "androidx.appcompat:appcompat:1.5.0" // updated
        const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
        const val material = "com.google.android.material:material:1.6.1" // updated
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4" // updated

        object Activity {
            private const val version = "1.5.1"  // updated
            const val ktx = "androidx.activity:activity-ktx:$version"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:1.5.2"
        }

        object Lifecycle {
            private const val version = "2.5.1" // updated
            const val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }

        object Paging {
            private const val version = "3.1.1" // updated
            const val runtime = "androidx.paging:paging-runtime:$version"
            const val runtimeKtx = "androidx.paging:paging-runtime-ktx:$version"
            const val pagingCommon = "androidx.paging:paging-common:$version"
            const val pagingCommonKtx = "androidx.paging:paging-common-ktx:$version"
        }

        object Navigation {
            private const val version = "2.5.1" // updated
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val gradlePlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Room {
            private const val version = "2.4.3" // updated
            const val runtime = "androidx.room:room-runtime:$version"
            const val paging = "androidx.room:room-paging:$version"
            const val pagingAlpha = "androidx.room:room-paging:2.5.0-alpha03"
            const val ktx = "androidx.room:room-ktx:$version"
            const val compiler = "androidx.room:room-compiler:$version"
        }

        object Test {
            object Ext {
                private const val version = "1.1.4" // updated
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }
            object Espresso{
                private const val version="3.5.0" // updated
                const val core = "androidx.test.espresso:espresso-core:$version"
                const val contrib = "androidx.test.espresso:espresso-contrib:$version"
            }
            object Runner{
                private const val version="1.5.1" // updated
                const val runner = "androidx.test:runner:$version"
                const val rules = "androidx.test:rules:$version"
            }

        }
    }

    object Glide {
        private const val version = "4.13.2" // updated
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object OkHttp3 {
        private const val version = "4.10.0" // updated
        const val okhttp3 = "com.squareup.okhttp3:okhttp:$version"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
        const val loginInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Retrofit {
        private const val version = "2.9.0" // updated
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val converterGson = "com.squareup.retrofit2:converter-gson:$version"
        const val googleGson = "com.google.code.gson:gson:2.9.1"
    }

    object Serialization {
        const val retrofitSerialize = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
        const val ktxSerialize = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3"
    }

    object Coroutines {
        private const val version = "1.6.4"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Shimmer {
        private const val version = "0.5.0"
        const val shimmer = "com.facebook.shimmer:shimmer:$version"
    }

    object PhotoView {
        private const val version = "2.3.0"
        const val shimmer = "com.github.chrisbanes:PhotoView:$version"
    }

    object Arrow {
        private const val version = "1.1.3"
        const val core = "io.arrow-kt:arrow-core:$version"
    }

    object Klaxon {
        private const val version = "5.6"
        const val core = "com.beust:klaxon:$version"
    }

    object Hilt {
        private const val version = "2.44"
        const val android = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-android-compiler:$version"
        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
        const val test = "com.google.dagger:hilt-android-testing:$version"
    }

    object JUnit {
        private const val version = "4.13.2"
        const val junit = "junit:junit:$version"
    }

    object Mockito {
        const val kotlin = "org.mockito.kotlin:mockito-kotlin:4.0.0"
        const val inline = "org.mockito:mockito-inline:4.8.1"
    }

    object Turbine {
        const val core = "app.cash.turbine:turbine:0.12.0"
    }

    object JavaX {
        const val inject = "javax.inject:javax.inject:1"
    }
}