package com.gkalal.demonote

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class NoteApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    Realm.init(this)
    /*val config = RealmConfiguration.Builder().name("demo.realm").build()
    Realm.setDefaultConfiguration(config)*/
  }
}
