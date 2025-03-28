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
    implementation(projects.play.playDomain)
    implementation(projects.user.userDomain)
    implementation(projects.view.viewDomain)
    implementation(projects.search.searchDomain)
    implementation(projects.details.detailsDomain)
    implementation(projects.suggestion.suggestionDomain)
    implementation(projects.notification.notificationDomain)
    implementation(projects.sync.syncDomain)

    // network
    implementation(projects.auth.authNetwork)
    implementation(projects.core.coreNetwork)
    implementation(projects.user.userNetwork)
    implementation(projects.view.viewNetwork)
    implementation(projects.search.searchNetwork)
    implementation(projects.suggestion.suggestionNetwork)
    implementation(projects.sync.syncNetwork)

    // data
    implementation(projects.auth.authData)
    implementation(projects.core.coreData)
    implementation(projects.user.userData)
    implementation(projects.view.viewData)
    implementation(projects.search.searchData)
    implementation(projects.suggestion.suggestionData)
    implementation(projects.sync.syncData)

    // notification
    implementation(projects.notification.notificationData)

    // database
    implementation(projects.core.coreDatabase)
}