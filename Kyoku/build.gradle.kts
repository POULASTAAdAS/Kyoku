buildscript {
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}

plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false

    kotlin("plugin.serialization") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
}