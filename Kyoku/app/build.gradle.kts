import com.android.build.api.variant.BuildConfigField

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    id("com.google.dagger.hilt.android")
    id("kotlinx-serialization")
    kotlin("kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.poulastaa.kyoku"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.poulastaa.kyoku"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

androidComponents {
    onVariants {
        it.buildConfigFields.put(
            "BUILD_TIME", BuildConfigField(
                "String", "\"" + System.currentTimeMillis().toString() + "\"", "build timestamp"
            )
        )
    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // dagger hilt
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.47")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // retrofit , okhttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.4.1")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    // navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    val roomVersion = "2.6.1"

    // room
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    //noinspection KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:$roomVersion")

    // Google Auth
    implementation("com.google.android.gms:play-services-auth:21.1.0")

    // Splash API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    val media3Version = "1.3.1"

    // For media playback using ExoPlayer
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    // For HLS playback support with ExoPlayer
    implementation("androidx.media3:media3-exoplayer-hls:$media3Version")
    implementation("androidx.media3:media3-common:$media3Version")
    implementation("androidx.media3:media3-session:$media3Version")
    implementation("androidx.legacy:legacy-support-v4:1.0.0") // Needed MediaSessionCompat.Token

    // For building media playback UIs
    implementation("androidx.media3:media3-ui:$media3Version")

    // for passkey
    implementation("androidx.credentials:credentials:1.3.0-alpha03")

    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha03")

    // coil
    implementation("io.coil-kt:coil-compose:2.5.0")

    // adaptive layout
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")

    //paging 3
    val pagingVersion = "3.2.1"

    implementation ("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation ("androidx.paging:paging-compose:3.3.0-beta01")

    // palette color extractor
    implementation("androidx.palette:palette-ktx:1.0.0")
}