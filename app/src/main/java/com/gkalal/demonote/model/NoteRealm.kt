package com.gkalal.demonote.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class NoteRealm : RealmObject {

    lateinit var title: String
    lateinit var note: String
    @PrimaryKey
    var id: Long = 0
    var color: Int = 0
    var textSize: Long = 0

    constructor()

    constructor(id: Long, title: String, note: String, color: Int, textSize: Long) {
        this.title = title
        this.id = id
        this.note = note
        this.color = color
        this.textSize = textSize
    }
}
