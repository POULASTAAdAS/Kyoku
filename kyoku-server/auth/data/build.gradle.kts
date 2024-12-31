plugins {
    alias(libs.plugins.kyoku.jvm.library)
}

dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.auth.authDomain)

    implementation(libs.bundles.ktor.auth)
    implementation(libs.bundles.koin)
    implementation(libs.kotlin.bycrypt)
}