package com.example.howareyou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.Model.PostingDTO
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_posting.*
import java.util.ArrayList

class PostingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

//        @SerializedName("Email") val Email: String,
//        @SerializedName("Board_category") val Board_category: String,
//        @SerializedName("Header") val Header: String,
//        @SerializedName("Title") val Title: String,
//        @SerializedName("Author") val Author: String,
//        @SerializedName("Content") val Content: String,
//        @SerializedName("Liked") val Liked: Int,
//        @SerializedName("Image") val Image: String,
//        @SerializedName("Views") val Views_: Int,
//        @SerializedName("Reported") val Reported: Int,
//        @SerializedName("Is_deleted") val Is_deleted: Int,
//        @SerializedName("Comments_no") val Comments_no: Int

        // 임시 데이터
        var postingDTOlist : ArrayList<PostingDTO> = arrayListOf()
        postingDTOlist.add(
            PostingDTO("xboyss@naver.com","01","01","리액트는 정말 구립니다","Seoplee","리액트 너무 꾸려요옷",5,10,0,false,0,""))

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
