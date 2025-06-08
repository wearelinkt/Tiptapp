package iq.tiptapp

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import iq.tiptapp.di.getAndroidModules
import iq.tiptapp.di.initKoin
import iq.tiptapp.utils.initializeNapier
import org.koin.android.ext.koin.androidContext

class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(
            config = {
                androidContext(this@Application)
            },
            modules = getAndroidModules()
        )

        initializeNapier()
        Firebase.auth.useEmulator("10.0.2.2", 9099)
    }
}