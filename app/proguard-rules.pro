# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

 -keep class retrofit.** { *; }
 -keepclasseswithmembers class * { @retrofit.http.* <methods>; }
 -keepattributes Signature

 # JSR 305 annotations are for embedding nullability information.
 -dontwarn javax.annotation.**

 -keepclasseswithmembers class * {
     @com.squareup.moshi.* <methods>;
 }

 -keep @interface com.squareup.moshi.JsonQualifier

 # Enum field names are used by the integrated EnumJsonAdapter.
 # values() is synthesized by the Kotlin compiler and is used by EnumJsonAdapter indirectly
 # Annotate enums with @JsonClass(generateAdapter = false) to use them with Moshi.
 -keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
     <fields>;
     **[] values();
 }

 # Keep helper method to avoid R8 optimisation that would keep all Kotlin Metadata when unwanted
 -keepclassmembers class com.squareup.moshi.internal.Util {
     private static java.lang.String getKotlinMetadataClassName();
 }

 # Keep ToJson/FromJson-annotated methods
 -keepclassmembers class * {
   @com.squareup.moshi.FromJson <methods>;
   @com.squareup.moshi.ToJson <methods>;
 }

 # Excluir Firebase Analytics de la ofuscación
 -keep class com.google.firebase.analytics.** { *; }
 -keep class com.google.firebase.** { *; }
 -keep class com.google.android.gms.** { *; }

 # Excluir la actividad NavHostActivity de la ofuscación
 -keep class com.mackenzie.waifuviewer.ui.NavHostActivity { *; }

 # Excluir las clases de Dagger de la ofuscación
 -keep class dagger.hilt.** { *; }
 -keep class javax.inject.** { *; }
 -keep class dagger.** { *; }
 -keep class hilt_aggregated_deps.** { *; }


 # Mantener las clases generadas por Hilt
 -keep class * extends dagger.hilt.internal.GeneratedComponentManager { *; }
 -keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
 -keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$ViewComponentBuilderEntryPoint { *; }
 -keep class * extends dagger.hilt.android.internal.managers.ActivityComponentManager$ActivityComponentBuilderEntryPoint { *; }

 # Mantener las clases de Hilt que implementan interfaces
 -keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
 -keep class * implements dagger.hilt.internal.GeneratedComponentManager { *; }
 -keep class * implements dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
 -keep class * implements dagger.hilt.android.internal.managers.ViewComponentManager$ViewComponentBuilderEntryPoint { *; }
 -keep class * implements dagger.hilt.android.internal.managers.ActivityComponentManager$ActivityComponentBuilderEntryPoint { *; }

 # Mantener las anotaciones de Hilt
 -keepattributes *Annotation*

 # Mantener las clases de Hilt que usan anotaciones específicas

# -keep class ** {
#     @dagger.hilt.InstallIn *;
#     @dagger.hilt.android.HiltAndroidApp *;
#     @dagger.hilt.android.lifecycle.HiltViewModel *;
#     @dagger.hilt.android.scopes.* *;
#     @dagger.hilt.components.* *;
#     @dagger.hilt.EntryPoint *;
#     @dagger.hilt.DefineComponent *;
#     @dagger.hilt.DefineComponent.Builder *;
#     @dagger.hilt.migration.* *;
# }

 #Glide
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public class * extends com.bumptech.glide.GeneratedAppGlideModule
 -keep public class * extends com.bumptech.glide.module.LibraryGlideModule

 # Room
 -keep class androidx.room.** { *; }
 -keepclassmembers class * {
     @androidx.room.* <methods>;
 }

 # Lottie Coil Arrow
 -keep class com.airbnb.lottie.** { *; }
 -keep class coil.** { *; }
 -keep class arrow.** { *; }