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
import kotlinx.android.synthetic.main.activity_create_new_todo.*


class CreateNewTodoActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mRealm: Realm
    private var TAG: String = "CreateNewTodoActivity"
    private var isEditMode: Boolean = false
    private var savedId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_create_new_todo)

            mRealm = Realm.getDefaultInstance()

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
                    savedId = bundle.getLong("id")
                    Log.d("bundle", note + "," + title + "," + color.toString() + "," + savedId.toString())

                    isEditMode = true
                    setReceivedDataToView(note, title, color)
                } else {
                    isEditMode = false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setReceivedDataToView(note: String, title: String, color: Int) {
        try {
            editTextNote.setText(note)
            editTextTitle.setText(title)
            layout.setBackgroundColor(color)
            changeTheme(color)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mRealm.close()
    }

    fun changeTheme(color: Int) {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(p0: View?) {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveData(title: String, note: String,
                         color: Int) {
        try {
            var id: Long

            if (!isEditMode) {
                id = getNextKey()
            } else {
                id = savedId
            }
            val noteRealm = NoteRealm(id, title, note, color)

            Log.d(TAG, "Id is : $id")

            mRealm.executeTransaction({ mRealm.copyToRealmOrUpdate(noteRealm) })
        } catch (e: Exception) {
        }
    }

    fun getNextKey(): Long {
        try {
            val number = mRealm.where(NoteRealm::class.java).max("id")
            return if (number != null) {
                number.toLong() + 1
            } else {
                0
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            return 0
        }
    }
}
