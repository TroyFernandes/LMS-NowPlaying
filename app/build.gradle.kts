@file:Suppress("UnstableApiUsage")

val bundleID = "com.example.lmsnowplaying"

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = bundleID
    compileSdk = 34

    defaultConfig {

        applicationId = bundleID
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders["redirectSchemeName"] =  bundleID
        manifestPlaceholders["redirectHostName"] =  "com.example.callback"

    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            //isDebuggable = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("debug") {
            isDebuggable = true
        }

    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }
}

dependencies {


    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(libs.androidx.splashScreen)

    implementation(libs.bundles.androidx.compose.bom)
    implementation(libs.bundles.androidx.tv.compose)

    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.retrofit)

    implementation(libs.bundles.material3)

    implementation(libs.bundles.coil)

    implementation(libs.runtime.livedata)

    implementation(libs.kotlin.stdlib)

    implementation(libs.jellyfin.sdk)

}
