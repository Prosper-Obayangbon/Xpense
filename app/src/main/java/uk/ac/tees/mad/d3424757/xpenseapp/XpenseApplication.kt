package uk.ac.tees.mad.d3424757.xpenseapp

import android.app.Application
import com.google.firebase.FirebaseApp


class XpenseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
