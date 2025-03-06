plugins {
    alias(libs.plugins.kyoku.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.pagingCommon)

    implementation(projects.core.domain)
}