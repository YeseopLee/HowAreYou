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
            moveBoards("01")
        }
        main_textview_qabaord.setOnClickListener {
            moveBoards("02")
        }
        main_textview_tipsboard.setOnClickListener {
            moveBoards("03")
        }
        main_textview_courseboard.setOnClickListener {
            moveBoards("04")
        }
        main_textview_studyboard.setOnClickListener {
            moveBoards("05")
        }
        main_textview_bestboard.setOnClickListener {
            moveBoards("06")
        }

        // 내가쓴글, 댓글단글, 스크랩


    }

    private fun moveBoards(board_category: String){
        var IT = Intent(applicationContext,PostingActivity::class.java)
        IT.putExtra("board_category",board_category)
        startActivity(IT)
    }


}
