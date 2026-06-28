import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.aboutlibraries.plugin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.plugin)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.secrets.gradle.plugin)
    kotlin("plugin.serialization").version(libs.versions.kotlin)
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "dr.achim.code_scanner"
    compileSdk = 37

    androidResources {
        generateLocaleConfig = true

        // https://developer.android.com/guide/topics/resources/app-languages#gradle-config
        localeFilters.addAll(listOf("en", "de"))
    }

    defaultConfig {
        applicationId = "dr.achim.code_scanner"
        minSdk = 26
        targetSdk = 37
        versionCode = (project.findProperty("versionCode") as? String)?.toIntOrNull() ?: 1
        versionName = project.findProperty("versionName") as? String ?: "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val localPropertiesFile = rootProject.file("local.properties")
        val localProperties = Properties().apply {
            if (localPropertiesFile.exists()) {
                FileInputStream(localPropertiesFile).use { load(it) }
            }
        }

        fun getProperty(name: String): String {
            return System.getenv(name)
                ?: if (localPropertiesFile.exists()) {
                    localProperties.getProperty(name) ?: error("Property '$name' not found in local.properties")
                } else {
                    error("local.properties file not found")
                }
        }

        val revenueCatKey = getProperty("REVENUECAT_KEY")
        buildConfigField("String", "REVENUECAT_KEY", "\"${revenueCatKey}\"")
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
                        error("keystore.properties file not found")
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
            isShrinkResources = true
            optimization { enable = true }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")

            ndk {
                debugSymbolLevel = "FULL"
            }
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
    arg("KOIN_CONFIG_CHECK","true")
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
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.annotations)
    implementation(libs.lifecycle.runtimeCompose)
    implementation(libs.lifecycle.viewModelCompose)
    implementation(libs.browser)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.material)
    implementation(libs.material3)

    implementation(libs.play.services.code.scanner)

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
}