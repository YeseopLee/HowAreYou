package com.example.howareyou

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.howareyou.Util.App
import kotlinx.android.synthetic.main.fragment_board.view.*

class BoardFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_board, container, false )

        //button
        view.board_textview_more.setOnClickListener {
            App.prefs.myCode = App.prefs.codeFree

            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_framelayout, PostingFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }

        view.board_textview_qa_more.setOnClickListener {
            App.prefs.myCode = App.prefs.codeQA

            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_framelayout, PostingFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }

        view.board_textview_tips_more.setOnClickListener {
            App.prefs.myCode = App.prefs.codeTips

            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_framelayout, PostingFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }

        view.board_textview_study_more.setOnClickListener {
            App.prefs.myCode = App.prefs.codeStudy

            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_framelayout, PostingFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }

        view.board_textview_best_more.setOnClickListener {
            App.prefs.myCode = App.prefs.codeBest

            val transaction = activity!!.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_framelayout, PostingFragment())
            transaction.disallowAddToBackStack()
            transaction.commit()
        }

        return view
    }

}
