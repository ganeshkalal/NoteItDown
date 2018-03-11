package com.gkalal.demonote.views.activities

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.gkalal.demonote.R
import com.gkalal.demonote.model.NoteRealm
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_create_new_todo.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class CreateNewTodoActivity : AppCompatActivity(), View.OnClickListener {

    private var TAG: String = "CreateNewTodoActivity"
    private lateinit var mRealm: Realm
    private var isEditMode: Boolean = false
    private var savedId: Long = 0
    private var textSize: Long = 15

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_create_new_todo)

            mRealm = Realm.getDefaultInstance()

            getDataFromBundle()
            setListeners()
            initView()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Called after UI is loaded. Used to initialize the entire view
    private fun initView() {
        try {
            setSupportActionBar(toolbar)
            //layout.setBackgroundColor(Color.WHITE)
            //changeTheme(Color.WHITE)
            picker.colors = intArrayOf(ContextCompat.getColor(this, R.color.aquaBlue),
                    ContextCompat.getColor(this, R.color.aquaGreen),
                    ContextCompat.getColor(this, R.color.lightYellow),
                    ContextCompat.getColor(this, R.color.uglyPink),
                    ContextCompat.getColor(this, R.color.lightRed),
                    Color.WHITE)
            if (isEditMode) {
                textSize = mRealm.where(NoteRealm::class.java).findFirst()!!.textSize
                Log.d(TAG, "text size in edit mode : ${textSize.toFloat()}")
                editTextNote.textSize = textSize.toFloat()
                numberPicker.value = textSize.toInt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Sets different types of listeners to UI elements
    private fun setListeners() {
        try {
            fabDone.setOnClickListener(this)

            picker.setOnColorChangedListener(
                    {
                        val color: Int = picker.color

                        layout.setBackgroundColor(color)

                        //changeTheme(color)
                    })

            numberPicker.setOnValueChangedListener(
                    { picker, _, _ ->
                        editTextNote.textSize = picker.value.toFloat()
                        textSize = picker.value.toLong()
                        //Log.d(TAG, "Text Size : " + textSize.toString())
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Get data from bundle in Edit Mode
    private fun getDataFromBundle() {
        try {
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
            // changeTheme(color)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
            mRealm.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*private fun changeTheme(color: Int) {
        try {
            if (color == Color.BLUE || color == Color.RED) {
                editTextNote.setTextColor(Color.WHITE)
                editTextTitle.setTextColor(Color.WHITE)
                numberPicker.textColor = Color.WHITE
                numberPicker.dividerColor = Color.WHITE
                numberPicker.selectedTextColor = Color.WHITE
            } else {
                editTextNote.setTextColor(Color.BLACK)
                editTextTitle.setTextColor(Color.BLACK)
                numberPicker.textColor = ContextCompat.getColor(this, R.color.colorPrimary)
                numberPicker.selectedTextColor = ContextCompat.getColor(this, R.color.colorAccent)
                numberPicker.dividerColor = ContextCompat.getColor(this, R.color.colorAccent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

    override fun onClick(p0: View?) {
        try {
            when (p0) {
                fabDone -> {
                    getData()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        try {
            super.onBackPressed()
            getData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getData() {
        try {
            val title: String = editTextTitle.text.toString()
            val note: String = editTextNote.text.toString()
            val colorDrawable: ColorDrawable = layout.background as ColorDrawable

            val color = colorDrawable.color

            Log.d("BackgroundColor", color.toString())

            if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(note)) {
                saveData(title, note, color)
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveData(title: String, note: String,
                         color: Int) {
        try {
            val id: Long = if (!isEditMode) {
                getNextKey()
            } else {
                savedId
            }

            Log.d(TAG, "textSize : $textSize")

            val noteRealm = NoteRealm(id, title, note, color, textSize)

            Log.d(TAG, "Id is : $id")

            mRealm.executeTransaction({ mRealm.copyToRealmOrUpdate(noteRealm) })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getNextKey(): Long {
        return try {
            val number = mRealm.where(NoteRealm::class.java).max("id")
            if (number != null) {
                number.toLong() + 1
            } else {
                0
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            0
        }
    }
}
