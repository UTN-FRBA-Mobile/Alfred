package com.botigocontigo.alfred.foda

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import com.botigocontigo.alfred.storage.db.entities.Dimension


class ContentAdapter(context: Context, dimension: Dimension) : BaseAdapter() {
    private val fodaContext: Context
    private val length: Int
    public var actualDimension: Dimension? = null


    init {
        fodaContext = context
        length = 1
        actualDimension = dimension
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val editText = EditText(this.fodaContext)
        editText.setText(this.actualDimension!!.name)
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank()) {
                    editText.removeTextChangedListener(this)
                    actualDimension!!.name = s.toString()
                    editText.addTextChangedListener(this)
                    editText.setFocusableInTouchMode(true)
                    editText.requestFocus()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        return editText
    }

    override fun getItem(position: Int): Any {
        return "testing"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return this.length
    }

}