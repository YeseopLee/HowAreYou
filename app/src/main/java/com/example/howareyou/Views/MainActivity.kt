package com.example.howareyou.views

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.howareyou.App
import com.example.howareyou.R
import com.example.howareyou.model.LoadCodeResponseDTO
import com.example.howareyou.model.PostdeviceTokenDTO
import com.example.howareyou.model.StatuscodeResponse
import com.example.howareyou.model.UpdateSetResponseDTO
import com.example.howareyou.views.Noti.NotiFragment
import com.example.howareyou.views.Search.SearchFragment
import com.example.howareyou.Util.PreferenceUtil
import com.example.howareyou.databinding.ActivityMainBinding
import com.example.howareyou.databinding.ActivitySigninBinding
import com.example.howareyou.views.Writing.WritingActivity
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.example.howareyou.views.auth.SigninViewModel
import com.example.howareyou.views.home.HomeFragment
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
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main),BottomNavigationView.OnNavigationItemSelectedListener {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        binding.viewModel = mainViewModel

        /*Bottom_Navigation*/
        main_bottom_navigation.setOnNavigationItemSelectedListener(this)
        //bottomnavigation 텍스트 제거
        main_bottom_navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_UNLABELED
        //시작 탭 결정
        main_bottom_navigation.selectedItemId = R.id.action_home

        /////////////// test ////////////////
        Log.e("testcode", App.prefs.myJwt)
        Log.e("testDevicetoken", App.prefs.myDevice)
        Log.e("testMyCode",App.prefs.myCode)
        Log.e("?????","????????")
        Log.e("???????????",App.prefs.myCode+"?????????")

    }

    override fun onResume() {
        super.onResume()
        // 글쓰기에서 메인 진입시 홈으로 이동
        if(main_bottom_navigation.selectedItemId == R.id.action_write) main_bottom_navigation.selectedItemId =
            R.id.action_home
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

}
