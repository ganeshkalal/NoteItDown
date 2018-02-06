package com.gkalal.demonote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.fabtransitionactivity.SheetLayout
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.bottom_sheet
import kotlinx.android.synthetic.main.activity_main.floatingActionButton2
import kotlinx.android.synthetic.main.activity_main.staggeredGridView
import kotlinx.android.synthetic.main.item_todo.view.cardView
import kotlinx.android.synthetic.main.item_todo.view.textViewNote
import kotlinx.android.synthetic.main.item_todo.view.textViewTitle


class MainActivity : AppCompatActivity(), SheetLayout.OnFabAnimationEndListener {

  private val REQUEST_CODE = 1
  private var context: Context = this
  private lateinit var realm: Realm
  private var isEditMode: Boolean = false
  private var bundle: Bundle = Bundle()

  override fun onCreate(savedInstanceState: Bundle?) {
    try {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_main)

      realm = Realm.getDefaultInstance()

      bottom_sheet.setFab(floatingActionButton2)
      bottom_sheet.setFabAnimationEndListener(this)

      floatingActionButton2.setOnClickListener({ bottom_sheet.expandFab() })
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  override fun onResume() {
    super.onResume()
    populateList()
  }

  override fun onDestroy() {
    super.onDestroy()
    realm.close()
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
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun editNote(note: String, title: String, color: Int,
      id: Long) {
    isEditMode = true

    bundle.putString("note", note)
    bundle.putString("title", title)
    bundle.putInt("color", color)
    bundle.putLong("id", id)

    bottom_sheet.expandFab()
  }

  override fun onFabAnimationEnd() {
    try {
      if (isEditMode) {
        val intent = Intent(context, CreateNewTodoActivity::class.java)
        if (!bundle.isEmpty) {
          intent.putExtra("bundle", bundle)
        }
        startActivityForResult(intent, REQUEST_CODE)
      } else {
        val intent = Intent(context, CreateNewTodoActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    try {
      if (requestCode == REQUEST_CODE) {
        bottom_sheet.contractFab()
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}
