package com.gkalal.demonote

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class NoteAdapter(private val context: Context,
    private val noteList: ArrayList<NoteData>) : BaseAdapter() {

  override fun getCount(): Int {
    return noteList.size
  }

  override fun getItem(position: Int): Any {
    return noteList[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    var view = convertView

    if (view == null) {
      view = LayoutInflater.from(context).inflate(R.layout.item_todo,
          parent, false)
    }


    val currentItem = getItem(position) as NoteData

    val textViewTitle = view?.findViewById(R.id.textViewTitle) as AppCompatTextView
    val textViewNote = view.findViewById(R.id.textViewNote) as AppCompatTextView
    val cardView = view.findViewById(R.id.cardView) as CardView

    textViewTitle.setText(currentItem.title)
    textViewNote.setText(currentItem.note)
    cardView.setCardBackgroundColor(currentItem.color)

    changeTheme(currentItem.color, textViewTitle, textViewNote)

    return view
  }

  fun changeTheme(color: Int, textViewTitle: AppCompatTextView,
      textViewNote: AppCompatTextView) {
    if (color.equals(Color.BLUE) || color.equals(Color.RED)) {
      textViewTitle.setTextColor(Color.WHITE)
      textViewNote.setTextColor(Color.WHITE)
    } else {
      textViewTitle.setTextColor(Color.DKGRAY)
      textViewNote.setTextColor(Color.DKGRAY)
    }
  }
}