package com.example.howareyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var postingAdapter: PostingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*Bottom_Navigation*/
        main_navigationview.setOnNavigationItemSelectedListener(this)
        //bottomnavigation 텍스트 제거
        main_navigationview.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
        //시작 탭 결정
        main_navigationview.selectedItemId = R.id.action_home

        /* 버튼 관리 */

        //좋아요 버튼
//        posting_button_favorite.setOnClickListener {
//            // db에서 좋아요 체크 받아온 후, 카운트 올려주기
//        }
        //댓글 버튼
//        posting_button_comment.setOnClickListener {  }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                var FragmentA = PostingFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_framelayout, FragmentA).commit()

                return true
            }

            R.id.action_search -> {
                var FragmentB = MenuFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_framelayout, FragmentB).commit()

                return true
            }

            R.id.action_account -> {
                var FragmentC = AccountFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_framelayout, FragmentC).commit()
                return true
            }
        }
        return false
    }

}
