package ca.androidyvr.demos.kotlin

import android.app.Application

/**
 * Created by Mahram Z. Foadi on 3/14/2018.
 */
class KotlinDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance:KotlinDemoApplication

        fun instance() : KotlinDemoApplication = instance
    }
}