package com.example.howareyou

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.Model.PostingDTO
import kotlinx.android.synthetic.main.fragment_posting.view.*
import java.util.ArrayList

class PostingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_posting, container, false )

        // 임시 데이터
        var postingDTOlist : ArrayList<PostingDTO> = arrayListOf()
        postingDTOlist.add(PostingDTO(0,1,"sample","#HowAreYouHowAreYouHowAre" +
                "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                "AreYouHowAreYouHowAreYou?YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n"))
        postingDTOlist.add(PostingDTO(0,2,"sample","#HowAreYou??"))
        postingDTOlist.add(PostingDTO(0,3,"sample","#HowAreYou???"))
        postingDTOlist.add(PostingDTO(0,4,"sample","#HowAreYou????"))
        postingDTOlist.add(PostingDTO(0,5,"sample","#HowAreYou?????"))

        // 리사이클러뷰 어댑터 연결
        view.posting_recyclerview.adapter = PostingAdapter(activity!!,postingDTOlist)
        val lm = LinearLayoutManager(activity)
        view.posting_recyclerview.layoutManager = lm
        view.posting_recyclerview.setHasFixedSize(true)

        // 리사이클러뷰 역순 출력
        lm.reverseLayout = true
        lm.stackFromEnd = true


        return view
    }

}
