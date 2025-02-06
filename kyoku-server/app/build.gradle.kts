plugins {
    alias(libs.plugins.kyoku.ktor.application)
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(libs.redis.clients.jedis)

    // PROJECTS

    // domain
    implementation(projects.core.coreDomain)
    implementation(projects.auth.authDomain)
    implementation(projects.details.detailsDomain)
    implementation(projects.play.playDomain)
    implementation(projects.search.searchDomain)
    implementation(projects.suggestion.suggestionDomain)
    implementation(projects.user.userDomain)
    implementation(projects.notification.notificationDomain)

    // network
    implementation(projects.auth.authNetwork)
    implementation(projects.core.coreNetwork)
    implementation(projects.user.userNetwork)
    implementation(projects.suggestion.suggestionNetwork)

    // data
    implementation(projects.auth.authData)
    implementation(projects.core.coreData)
    implementation(projects.user.userData)
    implementation(projects.suggestion.suggestionData)

    // notification
    implementation(projects.notification.notificationData)

    // database
    implementation(projects.core.coreDatabase)
}