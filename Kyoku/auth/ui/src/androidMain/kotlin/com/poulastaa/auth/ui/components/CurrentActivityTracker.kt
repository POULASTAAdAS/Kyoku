package com.poulastaa.auth.ui.components

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.poulastaa.common.ui.findActivity

/**
 * Credential Manager needs an Activity context; Koin only gives the application context.
 */
internal class CurrentActivityTracker(context: Context) {
    var activity: Activity? = context.findActivity()
        private set

    init {
        (context.applicationContext as? Application)?.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(
                    activity: Activity,
                    savedInstanceState: Bundle?
                ) = Unit

                override fun onActivityStarted(activity: Activity) {
                    this@CurrentActivityTracker.activity = activity
                }

                override fun onActivityResumed(activity: Activity) {
                    this@CurrentActivityTracker.activity = activity
                }

                override fun onActivityPaused(activity: Activity) = Unit

                override fun onActivityStopped(activity: Activity) = Unit

                override fun onActivitySaveInstanceState(
                    activity: Activity,
                    outState: Bundle,
                ) = Unit

                override fun onActivityDestroyed(activity: Activity) {
                    if (this@CurrentActivityTracker.activity === activity) this@CurrentActivityTracker.activity = null
                }
            }
        )
    }
}
