package com.example.howareyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.Model.PostingDTO
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_posting.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var postingAdapter: PostingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 임시 데이터
        var postingDTOlist : ArrayList<PostingDTO> = arrayListOf()
        postingDTOlist.add(PostingDTO(0,1,"sample","#HowAreYou?"))
        postingDTOlist.add(PostingDTO(0,2,"sample","#HowAreYou??"))
        postingDTOlist.add(PostingDTO(0,3,"sample","#HowAreYou???"))
        postingDTOlist.add(PostingDTO(0,4,"sample","#HowAreYou????"))
        postingDTOlist.add(PostingDTO(0,5,"sample","#HowAreYou?????"))
        postingAdapter =PostingAdapter(this,postingDTOlist)

        // 리사이클러뷰 어댑터 연결
        main_recyclerview.adapter = postingAdapter
        val lm = LinearLayoutManager(this)
        main_recyclerview.layoutManager = lm
        main_recyclerview.setHasFixedSize(true)

        // 리사이클러뷰 역순출력
        lm.reverseLayout = true
        lm.stackFromEnd = true

        /* 버튼 관리 */

        //좋아요 버튼
//        item_button_favorite.setOnClickListener {
//            // db에서 좋아요 체크 받아온 후, 카운트 올려주기
//        }
        //댓글 버튼
//        item_button_comment.setOnClickListener {  }
    }
}
