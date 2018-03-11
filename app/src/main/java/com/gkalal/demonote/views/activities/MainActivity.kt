package com.gkalal.demonote.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.github.fabtransitionactivity.SheetLayout
import com.gkalal.demonote.R
import com.gkalal.demonote.model.NoteData
import com.gkalal.demonote.model.NoteRealm
import com.gkalal.demonote.utils.SharedPrefUtil
import com.gkalal.demonote.views.adapters.NoteAdapter
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_todo.view.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper


class MainActivity : AppCompatActivity(), SheetLayout.OnFabAnimationEndListener {

    private var TAG: String = "MainActivity"
    private val reqCode = 1
    private var context: Context = this
    private lateinit var realm: Realm
    private var isEditMode: Boolean = false
    private var bundle: Bundle = Bundle()

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context))
    }

    override fun onResume() {
        super.onResume()
        try {
            populateList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            realm.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            initView()
            setListeners()
            checkDefaultAppTheme()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkDefaultAppTheme() {
        try {
            val sharedPref = SharedPrefUtil
            val isDarkTheme: Boolean = sharedPref.getPrefBoolean(this, SharedPrefUtil.THEME_MODE)
            Log.d(TAG, "inside method 1")

            if (isDarkTheme) {
                Log.d(TAG, "inside dark 1")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                Log.d(TAG, "inside light 1")
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        try {
            setSupportActionBar(toolbar)
            bottom_sheet.setFab(floatingActionButton2)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setListeners() {
        try {
            realm = Realm.getDefaultInstance()

            bottom_sheet.setFabAnimationEndListener(this)

            floatingActionButton2.setOnClickListener({
                isEditMode = false
                bottom_sheet.expandFab()
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_overflow_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.itemLightTheme -> {
            item.isChecked = true
            changeAppTheme(false)
            true
        }

        R.id.itemDarkTheme -> {
            item.isChecked = true
            changeAppTheme(true)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun changeAppTheme(isDarkTheme: Boolean) {
        try {
            val sharedPref = SharedPrefUtil
            sharedPref.savePrefBoolean(this, SharedPrefUtil.THEME_MODE, isDarkTheme)

            if (isDarkTheme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                this.recreate()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                this.recreate()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun populateList() {
        try {
            val noteSize: RealmResults<NoteRealm>? = realm.where(NoteRealm::class.java).findAll()

            Log.d("Size", noteSize?.size.toString())

            val list = ArrayList<NoteData>()

            for (noteRealm in noteSize!!) {
                val note = noteRealm.note
                val title = noteRealm.title
                val color = noteRealm.color

                Log.d("MainActivity", title + "," + note)

                val noteData = NoteData(note, title, color)
                list.add(noteData)
            }

            Log.d("List Size", list.size.toString())

            val noteAdapter = NoteAdapter(this, list)

            staggeredGridView.adapter = noteAdapter

            val margin = resources.getDimensionPixelSize(R.dimen.margin)

            staggeredGridView.setItemMargin(margin) // set the GridView margin

            staggeredGridView.setPadding(margin, 0, margin, 0)

            staggeredGridView.setOnItemClickListener({ parent, view, position, id ->
                val item = staggeredGridView.adapter.getView(position, view, parent)

                val note: String = item.textViewNote.text.toString()
                val title: String = item.textViewTitle.text.toString()
                val colorDrawable = item.cardView.cardBackgroundColor
                val color: Int = colorDrawable.defaultColor

                Log.d("id", id.toString())

                editNote(note, title, color, id)
            })

            /*staggeredGridView.onItemLongClickListener = StaggeredGridView.OnItemLongClickListener { parent, view, position, id ->
                Log.d(TAG,"long pressed : $position")
                val item = staggeredGridView.adapter.getView(position, view, parent)

                item.isSelected = true
                item.alpha = 0.5f
                true
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun editNote(note: String, title: String, color: Int,
                         id: Long) {
        try {
            isEditMode = true

            bundle.putString("note", note)
            bundle.putString("title", title)
            bundle.putInt("color", color)
            bundle.putLong("id", id)

            bottom_sheet.expandFab()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFabAnimationEnd() {
        try {
            if (isEditMode) {
                val intent = Intent(context, CreateNewTodoActivity::class.java)
                if (!bundle.isEmpty) {
                    intent.putExtra("bundle", bundle)
                }
                startActivityForResult(intent, reqCode)
            } else {
                val intent = Intent(context, CreateNewTodoActivity::class.java)
                startActivityForResult(intent, reqCode)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            if (requestCode == reqCode) {
                bottom_sheet.contractFab()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
