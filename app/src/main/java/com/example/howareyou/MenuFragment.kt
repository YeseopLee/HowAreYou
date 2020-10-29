package com.example.howareyou

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_menu.*

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_menu, container, false )

        val menu_textview_freeboard = view.findViewById(R.id.menu_textview_freeboard) as TextView

        menu_textview_freeboard.setOnClickListener {
            startActivity(Intent(activity,PostingActivity::class.java))
        }

        return view
    }

}

