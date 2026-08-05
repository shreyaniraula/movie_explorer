import java.io.FileInputStream
import java.util.Properties

plugins {
    // This module produces an installable APK
    alias(libs.plugins.android.application)

    //Wires up the compose compiler plugin; which turns @Compasable functions into actual UI code at compile time
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(FileInputStream(localPropertiesFile))
    }
}

android {
    // the package your generated R class lives under; a compile time/code org thing
    // com.example.movieexplorer.debug can be separate for installing a debug variant
    namespace = "com.example.movieexplorer"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        // the actual unique ID Google Play uses to identify the app; can differ from namespace
        applicationId = "com.example.movieexplorer"

        //oldest Android version the app will install on
        minSdk = 26

        // the API level you've tested against
        targetSdk = 36

        // compilesdk is the SDK version used to compile the code(gives access to latest APIs/classes)

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String", "OMDB_API_KEY", "\"${localProperties.getProperty("OMDB_API_KEY")}\""
        )
    }

    buildTypes {
        release {
            optimization {
                enable = true
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true // needed to enable BuildConfig generation
    }
}


ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    configurations.all {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }

    // platform() imports a Bill of Materials(BOM);
    // a single artifact that pins compatible versions for all compose libraries together;
    // so removes manual version-match and risk mismatches.
    implementation(platform(libs.androidx.compose.bom))

    // implementation-available to this module's compile+runtime but not exposed to modules that depend on this one
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compiler)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)

    // Kotlin serialization over moshi/gson - compile time safety
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.hilt.android)

    // KSP processes Kotlin symbols directly instead of going through a Java stub layer like Kapt,
    // so builds are faster
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Coil for Compose-first projects since it's coroutine-native
    // and has no View-system baggage; Glide is still common in legacy View-based Android.
    implementation(libs.coil.compose)

    // room-ktx adds Kotlin specific extensions most importantly,
    // Flow return types on DAO queries (without it, you'd be stuck with callback-based or LiveData-only observation)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) {
        exclude(group = "com.google.guava", module = "listenablefuture")
    }
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.compose)

    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    //Only for unit tests
    testImplementation(libs.junit)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)

    // Only for instrumented tests
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    // Only compiled into debug builds; doesn't need to bloat the release APK
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    //api- available to this module and to any module that depends on it.
}