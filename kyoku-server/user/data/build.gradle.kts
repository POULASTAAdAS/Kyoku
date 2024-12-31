plugins {
    alias(libs.plugins.kyoku.jvm.library)
}


dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.user.userDomain)

    implementation(libs.bundles.koin)
}