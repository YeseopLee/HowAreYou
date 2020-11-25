package com.example.howareyou

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.howareyou.Model.LoadCodeResponseDTO
import com.example.howareyou.Model.LoadPostDTO
import com.example.howareyou.Model.LoadPostItem
import com.example.howareyou.Util.App
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    private var service: ServiceApi? = null
    lateinit var prefs: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        System.out.println("MyJwt${App.prefs.myJwt}")

        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        // sharedpref 연결
        prefs = PreferenceUtil(applicationContext)

        /*Bottom_Navigation*/
        main_bottom_navigation.setOnNavigationItemSelectedListener(this)
        //bottomnavigation 텍스트 제거
        main_bottom_navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
        //시작 탭 결정
        main_bottom_navigation.selectedItemId = R.id.action_home

        // 게시판별 코드 불러오기
        loadCode()


//        /* 버튼 관리 */
//        main_textview_freeboard.setOnClickListener {
//            App.prefs.myCode = App.prefs.codeFree
//            moveBoards()
//        }
//        main_textview_qabaord.setOnClickListener {
//            App.prefs.myCode = App.prefs.codeQA
//            moveBoards()
//        }
//        main_textview_tipsboard.setOnClickListener {
//            App.prefs.myCode = App.prefs.codeTips
//            moveBoards()
//        }
//        main_textview_courseboard.setOnClickListener {
//            App.prefs.myCode = App.prefs.codeCourse
//            moveBoards()
//        }
//        main_textview_studyboard.setOnClickListener {
//            App.prefs.myCode = App.prefs.codeStudy
//            moveBoards()
//        }
//        main_textview_bestboard.setOnClickListener {
//            App.prefs.myCode = App.prefs.codeBest
//            moveBoards()
//        }

        // 내가쓴글, 댓글단글, 스크랩


    }

    override fun onResume() {
        super.onResume()
        if(main_bottom_navigation.selectedItemId != R.id.action_home) main_bottom_navigation.selectedItemId = R.id.action_home
    }


    private fun loadCode() {
        // Board Code initializing
        App.prefs.myCode = App.prefs.codeFree

        service?.getCode()?.enqueue(object : Callback<LoadCodeResponseDTO?> {
            override fun onResponse(
                call: Call<LoadCodeResponseDTO?>?,
                response: Response<LoadCodeResponseDTO?>

            ) {
                if(response.isSuccessful)
                {
                    val result: LoadCodeResponseDTO = response.body()!!
                    for (i in 0 until result.size){
                        when(result[i].code){
                            "01" -> App.prefs.codeFree = result[i].id
                            "02" -> App.prefs.codeQA = result[i].id
                            "03" -> App.prefs.codeTips = result[i].id
                            "04" -> App.prefs.codeCourse = result[i].id
                            "05" -> App.prefs.codeStudy = result[i].id
                            "06" -> App.prefs.codeBest = result[i].id
                        }
                    }
                }else {
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<LoadCodeResponseDTO> = gson.getAdapter<LoadCodeResponseDTO>(
                        LoadCodeResponseDTO::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            val result : LoadCodeResponseDTO = adapter.fromJson(response.errorBody()!!.string())

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<LoadCodeResponseDTO?>?, t: Throwable) {
                Log.e("에러 발생", t.message!!)
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                var FragmentA = HomeFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_framelayout, FragmentA).commit()

                return true
            }

            R.id.action_board -> {
                var FragmentB = BoardFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_framelayout, FragmentB).commit()

                return true
            }

            R.id.action_search -> {
                return true
            }

            R.id.action_notification -> {
                return true
            }

            R.id.action_write -> {

                startActivity(Intent(this,WritingActivity::class.java))
                return true
            }

        }
        return false
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus

        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev!!.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }



}
