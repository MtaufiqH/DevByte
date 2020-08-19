package app.taufiq.devbyte

import android.app.Application
import timber.log.Timber

/**
 * Created By Taufiq on 8/18/2020.
 *
 */


/**
 * Override application to setup background work via WorkManager
 */
class DevByteApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}