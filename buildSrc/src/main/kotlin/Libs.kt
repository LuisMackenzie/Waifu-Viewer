// @file:Suppress("unused")

object Libs {

    object Gradle {
        // const val androidGradlePlugin = "com.android.tools.build:gradle:_"
        const val playServicesLocation = "com.google.android.gms:play-services-location:_"
    }

    object Kotlin {
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin.gradle-plugin:_"
    }

    object GenerativeAI {
        const val generativeai = "com.google.ai.client.generativeai:generativeai:0.2.2"
    }

    object AndroidX {

        const val coreKtx = "androidx.core:core-ktx:_"
        const val appCompat = "androidx.appcompat:appcompat:_"
        const val recyclerView = "androidx.recyclerview:recyclerview:_"
        const val material = "com.google.android.material:material:_"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:_"

        object Activity {
            const val ktx = "androidx.activity:activity-ktx:_"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:_"
        }

        object Lifecycle {
            const val viewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:_"
            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:_"
        }

        object Paging {
            const val runtime = "androidx.paging:paging-runtime:_"
            const val runtimeKtx = "androidx.paging:paging-runtime-ktx:_"
            const val pagingCommon = "androidx.paging:paging-common:_"
            const val pagingCommonKtx = "androidx.paging:paging-common-ktx:_"
        }

        object Navigation {
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:_"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:_"
            const val gradlePlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:_"
        }

        object Room {
            const val runtime = "androidx.room:room-runtime:_"
            const val paging = "androidx.room:room-paging:_"
            const val ktx = "androidx.room:room-ktx:_"
            const val compiler = "androidx.room:room-compiler:_"
        }

        object Test {
            object Ext {
                const val junit = "androidx.test.ext:junit-ktx:_"
            }
            object Espresso{
                const val core = "androidx.test.espresso:espresso-core:_"
                const val contrib = "androidx.test.espresso:espresso-contrib:_"
            }
            object Runner{
                const val runner = "androidx.test:runner:_"
                const val rules = "androidx.test:rules:_"
            }

        }
    }

    object Glide {
        const val glide = "com.github.bumptech.glide:glide:_"
        const val compiler = "com.github.bumptech.glide:compiler:_"
    }

    object OkHttp3 {
        const val okhttp3 = "com.squareup.okhttp3:okhttp:_"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:_"
        const val loginInterceptor = "com.squareup.okhttp3:logging-interceptor:_"
    }

    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:_"
        const val converterGson = "com.squareup.retrofit2:converter-gson:_"
        const val googleGson = "com.google.code.gson:gson:_"
        const val converterMoshi = "com.squareup.retrofit2:converter-moshi:_"
        const val moshi = "com.squareup.moshi:moshi:_"
        const val moshiKtx = "com.squareup.moshi:moshi-kotlin:_"
    }

    object Firebase {
        const val coreBom = "com.google.firebase:firebase-bom:_"
        const val analitycsKtx = "com.google.firebase:firebase-analytics-ktx"
        const val crashlitycsKtx = "com.google.firebase:firebase-crashlytics-ktx"
        const val remoteconfigKtx = "com.google.firebase:firebase-config-ktx"
    }

    object Serialization {
        const val retrofitSerialize = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:_"
        const val ktxSerialize = "org.jetbrains.kotlinx:kotlinx-serialization-json:_"
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:_"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:_"
    }

    object Shimmer {
        const val shimmer = "com.facebook.shimmer:shimmer:_"
    }

    object PhotoView {
        const val core = "com.github.chrisbanes:PhotoView:_"
    }

    object Arrow {
        const val core = "io.arrow-kt:arrow-core:_"
    }

    object Lottie {
        const val core = "com.airbnb.android:lottie:_"
    }

    object Klaxon {
        const val core = "com.beust:klaxon:_"
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:_"
        const val compiler = "com.google.dagger:hilt-android-compiler:_"
        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:_"
        const val test = "com.google.dagger:hilt-android-testing:_"
    }

    object JUnit {
        const val junit = "junit:junit:_"
    }

    object Mockito {
        const val kotlin = "org.mockito.kotlin:mockito-kotlin:_"
        const val inline = "org.mockito:mockito-inline:_"
    }

    object Turbine {
        const val core = "app.cash.turbine:turbine:_"
    }

    object JavaX {
        const val inject = "javax.inject:javax.inject:_"
    }
}