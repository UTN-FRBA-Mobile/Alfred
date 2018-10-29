package com.botigocontigo.alfred.foda

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RadioButton
import android.widget.TextView
import org.w3c.dom.Text

 class ContentAdapter(context: Context, inputArray: Array<String>): BaseAdapter() {
    private val fodaContext: Context
    private val length: Int
    private val array:Array<String>

    init {
        fodaContext = context
        length = inputArray.size
        array= inputArray
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val textView = TextView(this.fodaContext)
        textView.height=120
        textView.setPadding(0,20,0,20)
        textView.setText(this.array[position])
        return textView
    }

    override fun getItem(position: Int): Any {
        return "testing"
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return this.length   }

}