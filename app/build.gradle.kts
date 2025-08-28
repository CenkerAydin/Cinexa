import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("gradle.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val apiKey = localProperties.getProperty("API_KEY", "default_key")

android {
    namespace = "com.cenkeraydin.composemovie"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cenkeraydin.composemovie"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Sadece burada tanımla
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    buildTypes {
        debug {
            // BuildConfig field'ı tekrar tanımlamaya gerek yok
            // Zaten defaultConfig'den inherit ediyor
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // BuildConfig field'ı tekrar tanımlamaya gerek yok
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Hilt

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    //Retrofit

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Navigation

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //Coil
    implementation(libs.coil.compose)

    implementation(libs.androidx.material.icons.extended)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //DataStore
    implementation (libs.androidx.datastore.preferences)









}