plugins {
    alias(libs.plugins.kyoku.jvm.library)
}


dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.item.itemDomain)

    implementation(libs.bundles.koin)
}