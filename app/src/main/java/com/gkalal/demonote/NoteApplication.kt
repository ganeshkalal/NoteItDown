package com.gkalal.demonote

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class NoteApplication : Application() {

    override fun onCreate() {
        try {
            super.onCreate()
            Realm.init(this)
            val realmConfiguration = RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build()
            Realm.setDefaultConfiguration(realmConfiguration)

            CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/ShadowsIntoLightTwo-Regular.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
