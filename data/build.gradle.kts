plugins {
    id("com.android.library")
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.google.devtools.ksp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.gabriel.data"

    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.core.ktx)
    implementation(libs.bundles.retrofit)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(libs.bundles.unitTest)
    androidTestImplementation(libs.bundles.androidTest)
}