plugins {
    alias(libs.plugins.kyoku.jvm.library)
}


dependencies {
    implementation(project(":core:core-domain"))
    implementation(project(":search:search-domain"))
}