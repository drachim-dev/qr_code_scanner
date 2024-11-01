plugins {
    alias(libs.plugins.aboutlibraries.plugin)
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.plugin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.secrets.gradle.plugin)
    kotlin("plugin.serialization").version(libs.versions.kotlin)
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "dr.achim.code_scanner"
    compileSdk = 35

    androidResources {
        generateLocaleConfig = true
    }

    defaultConfig {
        applicationId = "dr.achim.code_scanner"
        minSdk = 30
        targetSdk = 35
        versionCode = 3
        versionName = "0.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // https://developer.android.com/guide/topics/resources/app-languages#gradle-config
        resourceConfigurations += listOf("en", "de")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

    implementation(platform(libs.compose.bom))
    implementation(libs.activity.compose)
    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose)
    implementation(libs.android.play.review)
    implementation(libs.android.play.review.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.splashscreen)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.runtimeCompose)
    implementation(libs.lifecycle.viewModelCompose)
    implementation(libs.billing.ktx)
    implementation(libs.browser)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.material)
    implementation(libs.material3)

    implementation(libs.play.services.code.scanner)
    implementation(libs.play.services.mlkit.barcode.scanning)

    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)

    // in-app purchases
    implementation(libs.revenuecat.purchases)
    implementation(libs.konfetti.compose)

    // kotlinx serialization
    implementation(libs.kotlinx.serialization.json)

    // room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}