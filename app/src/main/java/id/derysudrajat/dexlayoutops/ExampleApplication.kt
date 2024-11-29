package id.derysudrajat.dexlayoutops

import android.app.Application
import id.derysudrajat.dexlayoutops.util.ClickTrace

class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ClickTrace.install()
    }
}