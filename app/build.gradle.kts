plugins {
    // This module produces an installable APK
    alias(libs.plugins.android.application)

    //Wires up the compose compiler plugin; which turns @Compasable functions into actual UI code at compile time
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.kotlin.serialization)
}

android {
    // the package your generated R class lives under; a compile time/code org thing
    // com.example.movieexplorer.debug can be separate for installing a debug variant
    namespace = "com.example.movieexplorer"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
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
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // platform() imports a Bill of Materials(BOM);
    // a single artifact that pins compatible versions for all compose libraries together;
    // so removes manual version-match and risk mismatches.
    implementation(platform(libs.androidx.compose.bom))

    // implementation-available to this module's compile+runtime but not exposed to modules that depend on this one
    implementation(libs.androidx.activity.compose)
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

    //Only for unit tests
    testImplementation(libs.junit)

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