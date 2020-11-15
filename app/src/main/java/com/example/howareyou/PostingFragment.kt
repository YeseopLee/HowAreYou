package com.example.howareyou

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class PostingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_posting, container, false )

        // 임시 데이터



        return view
    }

}
