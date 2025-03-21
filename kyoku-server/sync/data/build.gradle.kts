plugins {
    alias(libs.plugins.kyoku.jvm.library)
}


dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.sync.syncDomain)

    implementation(libs.bundles.koin)
}