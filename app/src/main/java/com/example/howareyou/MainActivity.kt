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
import com.example.howareyou.views.Home.HomeFragment
import com.example.howareyou.Model.LoadCodeResponseDTO
import com.example.howareyou.Model.PostdeviceTokenDTO
import com.example.howareyou.Model.StatuscodeResponse
import com.example.howareyou.Model.UpdateSetResponseDTO
import com.example.howareyou.views.Noti.NotiFragment
import com.example.howareyou.views.Search.SearchFragment
import com.example.howareyou.Util.PreferenceUtil
import com.example.howareyou.views.Writing.WritingActivity
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),BottomNavigationView.OnNavigationItemSelectedListener {

    private var service: ServiceApi? = null
    lateinit var prefs: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        /*Bottom_Navigation*/
        main_bottom_navigation.setOnNavigationItemSelectedListener(this)
        //bottomnavigation 텍스트 제거
        main_bottom_navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
        //시작 탭 결정
        main_bottom_navigation.selectedItemId = R.id.action_home

        // 게시판별 코드 불러오기
        loadCode()

        // update device token
        // 기기가 변경되었을 수 있으므로 usersetting db의 device 토큰을 update 한다.
        findSettingid(App.prefs.myId)

        /////////////// test ////////////////
        Log.e("testcode", App.prefs.myJwt)
        Log.e("testDevicetoken", App.prefs.myDevice)

    }

    override fun onResume() {
        super.onResume()
        // 글쓰기에서 메인 진입시 홈으로 이동
        if(main_bottom_navigation.selectedItemId == R.id.action_write) main_bottom_navigation.selectedItemId = R.id.action_home
    }

    private fun notimanage(){

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

    // id를 기반으로 usersetting id값 검색
    private fun findSettingid(user_id : String) {
        var tempSettingid : String = ""

        service?.getUsersettings()?.enqueue(object :
            Callback<UpdateSetResponseDTO?> {
            override fun onResponse(
                call: Call<UpdateSetResponseDTO?>?,
                response: Response<UpdateSetResponseDTO?>
            ) {
                if(response.isSuccessful) {
                    val result = response.body()!!
                    for ( i in 0 until result.size){
                        if(result[i].user_id == user_id)
                        {
                            tempSettingid = result[i]._id
                            updateDeviceToken(tempSettingid) // setting id를 기반으로 devicetoken을 update 한다.
                        }
                    }
                }
                else if (response.code() == 400) {
                    val gson = Gson()
                    val adapter: TypeAdapter<StatuscodeResponse> = gson.getAdapter<StatuscodeResponse>(
                        StatuscodeResponse::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            val result : StatuscodeResponse = adapter.fromJson(response.errorBody()!!.string())
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<UpdateSetResponseDTO?>, t: Throwable) {
                //
            }

        })
    }

    //usersetting id값을 기반으로 device token update
    private fun updateDeviceToken(setting_id : String) {

        service?.userUpdatesetting(setting_id,
            PostdeviceTokenDTO(App.prefs.myId, App.prefs.myDevice,true)
        )?.enqueue(object :
            Callback<Void?> {
            override fun onResponse(
                call: Call<Void?>?,
                response: Response<Void?>
            ) {
                if(response.isSuccessful) {

                }
                else if (response.code() == 400) {
                    val gson = Gson()
                    val adapter: TypeAdapter<StatuscodeResponse> = gson.getAdapter<StatuscodeResponse>(
                        StatuscodeResponse::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            val result : StatuscodeResponse = adapter.fromJson(response.errorBody()!!.string())
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                //
            }

        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // 알람 badge
        var badge = main_bottom_navigation.getOrCreateBadge(R.id.action_notification)
        badge.number = App.prefs.notificationCount
        badge.isVisible = badge.number != 0

        when (item.itemId) {
            R.id.action_home -> {
                var FragmentA = HomeFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_framelayout, FragmentA).commit()

                return true
            }

            R.id.action_search -> {
                var FragmentC = SearchFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_framelayout, FragmentC).commit()
                return true
            }

            R.id.action_notification -> {
                val badgeDrawable = main_bottom_navigation.getBadge(R.id.action_notification)
                if (badgeDrawable != null) {
                    App.prefs.notificationCount = 0
                    badgeDrawable.isVisible = false
                    badgeDrawable.clearNumber()
                }

                var FragmentD = NotiFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_framelayout, FragmentD).commit()

                return true
            }

            R.id.action_write -> {

                startActivity(Intent(this, WritingActivity::class.java))
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
