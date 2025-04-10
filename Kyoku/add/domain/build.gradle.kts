plugins {
    alias(libs.plugins.kyoku.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.pagingCommon)
}