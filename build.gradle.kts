// Top-level build file. Plugins are declared here and applied in :app.
// Kotlin support is built into the Android Gradle Plugin (AGP 9+), so there is
// no separate kotlin-android plugin. Only the Compose compiler plugin is added.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
