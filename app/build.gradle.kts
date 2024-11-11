import java.io.FileInputStream
import java.util.Properties

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
        versionCode = (project.findProperty("versionCode") as? String)?.toIntOrNull() ?: 1
        versionName = project.findProperty("versionName") as? String ?: "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val revenueCatKey = if (System.getenv("REVENUECAT_KEY") != null) {
            System.getenv("REVENUECAT_KEY")
        } else {
            val localProperties = Properties().apply {
                val localPropertiesFile = rootProject.file("local.properties")
                if (localPropertiesFile.exists()) {
                    load(FileInputStream(localPropertiesFile))
                } else {
                    println("Warning: local.properties file not found")
                }
            }
            localProperties.getProperty("REVENUECAT_KEY", "")
        }
        buildConfigField("String", "REVENUECAT_KEY", "\"${revenueCatKey}\"")

        // https://developer.android.com/guide/topics/resources/app-languages#gradle-config
        resourceConfigurations += listOf("en", "de")
    }

    signingConfigs {
        create("release") {
            // CI=true is exported by Codemagic
            if (System.getenv("CI") == "true") {
                storeFile = file(System.getenv("CM_KEYSTORE_PATH"))
                storePassword = System.getenv("CM_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("CM_KEY_ALIAS")
                keyPassword = System.getenv("CM_KEY_PASSWORD")
            } else {
                val keystoreProperties = Properties().apply {
                    val keystoreFile = rootProject.file("keystore.properties")
                    if (keystoreFile.exists()) {
                        load(FileInputStream(keystoreFile))
                    } else {
                        println("Warning: keystore.properties file not found")
                    }
                }

                keyAlias = keystoreProperties["keyAlias"] as String?
                keyPassword = keystoreProperties["keyPassword"] as String?
                storeFile = keystoreProperties["storeFile"]?.let { file(it as String) }
                storePassword = keystoreProperties["storePassword"] as String?
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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