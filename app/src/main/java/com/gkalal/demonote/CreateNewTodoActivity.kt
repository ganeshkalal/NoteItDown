package com.gkalal.demonote

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_create_new_todo.editTextNote
import kotlinx.android.synthetic.main.activity_create_new_todo.editTextTitle
import kotlinx.android.synthetic.main.activity_create_new_todo.fabDone
import kotlinx.android.synthetic.main.activity_create_new_todo.layout
import kotlinx.android.synthetic.main.activity_create_new_todo.number_picker
import kotlinx.android.synthetic.main.activity_create_new_todo.picker


class CreateNewTodoActivity : AppCompatActivity(), View.OnClickListener {

  private lateinit var realm: Realm

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_create_new_todo)

    realm = Realm.getDefaultInstance()

    layout.setBackgroundColor(Color.WHITE)
    changeTheme(Color.WHITE)
    picker.setColors(intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.WHITE))

    picker.setOnColorChangedListener(
        { c ->
          val color: Int = picker.getColor()

          layout.setBackgroundColor(color)

          changeTheme(color)
        })

    fabDone.setOnClickListener(this)

    number_picker.setOnValueChangedListener(
        { picker, oldVal, newVal ->
          //Log.d("length", picker.value.toString())
          editTextNote.setTextSize(picker.value.toFloat())
        })

    if (intent.getBundleExtra("bundle") != null) {
      val bundle: Bundle = intent.getBundleExtra("bundle")

      if (!bundle.isEmpty) {
        val note: String = bundle.getString("note")
        val title: String = bundle.getString("title")
        val color: Int = bundle.getInt("color")
        val id: Long = bundle.getLong("id")
        Log.d("bundle", note + "," + title + "," + color.toString() + "," + id.toString())

        setReceivedDataToView(note, title, color)
      }
    }
  }

  private fun setReceivedDataToView(note: String, title: String, color: Int) {
    editTextNote.setText(note)
    editTextTitle.setText(title)
    layout.setBackgroundColor(color)
    changeTheme(color)
  }

  override fun onDestroy() {
    super.onDestroy()
    realm.close()
  }

  fun changeTheme(color: Int) {
    if (color.equals(Color.BLUE) || color.equals(Color.RED)) {
      editTextNote.setTextColor(Color.WHITE)
      editTextTitle.setTextColor(Color.WHITE)
      number_picker.setTextColor(Color.WHITE)
      number_picker.setDividerColor(Color.WHITE)
      number_picker.setSelectedTextColor(Color.WHITE)
    } else {
      editTextNote.setTextColor(Color.BLACK)
      editTextTitle.setTextColor(Color.BLACK)
      number_picker.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
      number_picker.setSelectedTextColor(ContextCompat.getColor(this, R.color.colorAccent))
      number_picker.setDividerColor(ContextCompat.getColor(this, R.color.colorAccent))
    }
  }

  override fun onClick(p0: View?) {
    when (p0) {
      fabDone -> {
        val title: String = editTextTitle.text.toString()
        val note: String = editTextNote.text.toString()
        val colorDrawable: ColorDrawable = layout.background as ColorDrawable

        val color = colorDrawable.color

        Log.d("BackgroundColor", color.toString())

        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(note)) {
          saveData(title, note, color)
          finish()
        } else {
          Toast.makeText(this, "You forgot to type something!", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  private fun saveData(title: String, note: String,
      color: Int) {
    val id: Int = getNextKey()
    val noteRealm = NoteRealm(id, title, note, color)

    realm.executeTransaction({ realm.copyToRealmOrUpdate(noteRealm) })

  }

  fun getNextKey(): Int {
    try {
      val number = realm.where(NoteRealm::class.java).max("id")
      return if (number != null) {
        number.toInt() + 1
      } else {
        0
      }
    } catch (e: ArrayIndexOutOfBoundsException) {
      return 0
    }

  }
}
