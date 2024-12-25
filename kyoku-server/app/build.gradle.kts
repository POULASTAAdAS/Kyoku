plugins {
    alias(libs.plugins.kyoku.ktor.application)
}

dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.auth.authDomain)
    implementation(projects.details.detailsDomain)
    implementation(projects.play.playDomain)
    implementation(projects.search.searchDomain)
    implementation(projects.suggestion.suggestionDomain)
    implementation(projects.user.userDomain)
    implementation(projects.notification.notificationDomain)

    implementation(projects.auth.authNetwork)

    implementation(projects.auth.authData)
    implementation(projects.core.coreData)
    implementation(projects.notification.notificationData)

    implementation(projects.core.coreDatabase)

    implementation(projects.core.coreNetwork)

    implementation(libs.bundles.koin)
    implementation(libs.redis.clients.jedis)
}