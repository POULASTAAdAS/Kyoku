plugins {
    alias(libs.plugins.kyoku.jvm.library)

}

dependencies {
    implementation(project(":core:core-domain"))
    implementation(project(":core:core-database"))

    implementation(libs.bundles.koin)
}