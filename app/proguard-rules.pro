# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Kotlin Serialization — keep serializer generation working
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep your DTO/domain classes' serializers
-keep,includedescriptorclasses class com.example.movieexplorer.**$$serializer { *; }
-keepclassmembers class com.example.movieexplorer.** {
    *** Companion;
}
-keepclasseswithmembers class com.example.movieexplorer.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Retrofit / OkHttp — mostly self-contained via consumer rules, minimal extra needed
-dontwarn okhttp3.**
-dontwarn retrofit2.**
