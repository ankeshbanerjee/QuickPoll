// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kapt)
    alias(libs.plugins.hilt.gradle.plugin) apply false
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}