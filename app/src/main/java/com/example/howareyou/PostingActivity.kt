package com.example.howareyou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.Model.PostingDTO
import kotlinx.android.synthetic.main.activity_posting.*
import kotlinx.android.synthetic.main.fragment_posting.view.*
import java.util.ArrayList

class PostingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        // 임시 데이터
        var postingDTOlist : ArrayList<PostingDTO> = arrayListOf()
        postingDTOlist.add(
            PostingDTO(0,"이예섭","sample","#HowAreYouHowAreYouHowAre" +
                    "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                    "AreYouHowAreYouHowAreYou?YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                    "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                    "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                    "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                    "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                    "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                    "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                    "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n" +
                    "YouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\nYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHowAreYouHow\n")
        )
        postingDTOlist.add(PostingDTO(0,"이예섭","sample","#HowAreYou??"))
        postingDTOlist.add(PostingDTO(0,"이예섭","sample","#HowAreYou???"))
        postingDTOlist.add(PostingDTO(0,"이예섭","sample","#HowAreYou????"))
        postingDTOlist.add(PostingDTO(0,"이예섭","sample","#HowAreYou?????"))
        postingDTOlist.add(PostingDTO(0,"이예섭","sample","#HowAreYou?????"))
        postingDTOlist.add(PostingDTO(0,"이예섭","sample","#HowAreYou?????"))
        postingDTOlist.add(PostingDTO(0,"이예섭","sample","#HowAreYou?????"))
        postingDTOlist.add(PostingDTO(0,"이예섭","sample","#HowAreYou?????"))
        postingDTOlist.add(PostingDTO(0,"이예섭","sample","#HowAreYou?????"))
        postingDTOlist.add(PostingDTO(0,"이예섭","sample","#HowAreYou?????"))

        posting_recyclerview.adapter = PostingAdapter(this,postingDTOlist)
        val lm = LinearLayoutManager(this)
        posting_recyclerview.layoutManager = lm
        posting_recyclerview.setHasFixedSize(true)

        // 리사이클러뷰 역순 출력
        lm.reverseLayout = true
        lm.stackFromEnd = true

        posting_button_post.setOnClickListener {
            startActivity(Intent(this,WritingActivity::class.java))
        }

    }


}
