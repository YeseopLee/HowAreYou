package com.example.howareyou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /* 버튼 관리 */

        main_textview_freeboard.setOnClickListener {
            startActivity(Intent(this,PostingActivity::class.java))
        }

        //좋아요 버튼
//        posting_button_favorite.setOnClickListener {
//            // db에서 좋아요 체크 받아온 후, 카운트 올려주기
//        }
        //댓글 버튼
//        posting_button_comment.setOnClickListener {  }
    }


}
