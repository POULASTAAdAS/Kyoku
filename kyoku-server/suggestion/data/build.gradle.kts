plugins {
    alias(libs.plugins.kyoku.jvm.library)
}


dependencies {
    implementation(project(":core:domain"))
    implementation(project(":suggestion:domain"))
}