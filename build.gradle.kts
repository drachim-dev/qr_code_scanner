plugins {
    alias(libs.plugins.aboutlibraries.plugin) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.plugin) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.secrets.gradle.plugin) apply false
    kotlin("plugin.serialization").version(libs.versions.kotlin) apply false
}
