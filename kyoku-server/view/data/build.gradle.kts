plugins {
    alias(libs.plugins.kyoku.jvm.library)
}


dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.view.viewDomain)

    implementation(libs.bundles.koin)
}