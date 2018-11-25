package com.botigocontigo.alfred.learn.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.botigocontigo.alfred.R
import kotlinx.android.synthetic.main.fragment_error.view.*

class ErrorFragment : Fragment() {
    var text = "Ops, something went wrong!"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewFragment = inflater.inflate(R.layout.fragment_error, container, false)
        viewFragment.textView.text = text
        return viewFragment
    }

    fun updateText(text: String) {
        this.text = text
        view?.textView?.text = text
    }

}