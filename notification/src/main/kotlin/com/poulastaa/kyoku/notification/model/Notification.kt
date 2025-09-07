package com.poulastaa.kyoku.notification.model

sealed class Notification {
    /**
     * make sure queue name are same as in [auth]
     * @see <a
     *      href="https://github.com/POULASTAAdAS/Kyoku/blob/dev/auth/src/main/kotlin/com/poulastaa/kyoku/auth/model/Notification.kt">
     *      auth service notification class
     * </a>
     */
    companion object QUEUE {
        const val EMAIL = "EMAIL"
    }

    enum class Type {
        AUTHENTICATE,
        WELCOME,
        WELCOME_BACK,
    }
}