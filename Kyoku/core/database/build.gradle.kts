plugins {
    alias(libs.plugins.kyoku.android.library)
//    alias(libs.plugins.room)
}

android {
    namespace = "com.poulastaa.core.database"
}


dependencies {
    implementation(libs.room.ktx)
    implementation(libs.room.compiler)
    implementation(libs.room.runtime)

    implementation(projects.core.domain)
}