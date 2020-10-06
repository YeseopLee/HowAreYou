package com.example.howareyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.Model.PostingDTO
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var postingAdapter: PostingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var postingDTOlist : ArrayList<PostingDTO> = arrayListOf()
        postingDTOlist.add(PostingDTO(0,1,"sample","#HowAreYou?"))
        postingDTOlist.add(PostingDTO(0,2,"sample","#HowAreYou??"))
        postingDTOlist.add(PostingDTO(0,3,"sample","#HowAreYou???"))
        postingDTOlist.add(PostingDTO(0,4,"sample","#HowAreYou????"))
        postingDTOlist.add(PostingDTO(0,5,"sample","#HowAreYou?????"))

        postingAdapter =PostingAdapter(this,postingDTOlist)

        main_recyclerview.adapter = postingAdapter

        val lm = LinearLayoutManager(this)
        main_recyclerview.layoutManager = lm
        main_recyclerview.setHasFixedSize(true)

        // recyclerview 역순출력
        lm.reverseLayout = true
        lm.stackFromEnd = true
    }
}
