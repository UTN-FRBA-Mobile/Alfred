package com.botigocontigo.alfred.foda

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RadioButton
import android.widget.TextView
import com.botigocontigo.alfred.R
import org.w3c.dom.Text
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.SpannableString
import android.view.Gravity


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
        val string = SpannableString("Text witBullet point")
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.abc_btn_radio_material, 0,0 , 0)
        textView.height=120

        textView.setCompoundDrawablePadding(16);
        textView.setPadding(20,20,0,20)
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setTextSize(18F);
        textView.setTextColor(Color.BLACK)
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