package com.example.howareyou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.howareyou.Model.LoadCodeResponseDTO
import com.example.howareyou.Model.SigninDTO
import com.example.howareyou.Model.SigninResponseDTO
import com.example.howareyou.Util.App
import com.example.howareyou.Util.App.Companion.prefs
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

class MainActivity : AppCompatActivity() {

    private var service: ServiceApi? = null
    lateinit var prefs: PreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        // sharedpref 연결
        prefs = PreferenceUtil(applicationContext)

        loadCode()

        /* 버튼 관리 */
        main_textview_freeboard.setOnClickListener {
            App.prefs.myCode = App.prefs.codeFree
            moveBoards()
        }
        main_textview_qabaord.setOnClickListener {
            App.prefs.myCode = App.prefs.codeQA
            moveBoards()
        }
        main_textview_tipsboard.setOnClickListener {
            App.prefs.myCode = App.prefs.codeTips
            moveBoards()
        }
        main_textview_courseboard.setOnClickListener {
            App.prefs.myCode = App.prefs.codeCourse
            moveBoards()
        }
        main_textview_studyboard.setOnClickListener {
            App.prefs.myCode = App.prefs.codeStudy
            moveBoards()
        }
        main_textview_bestboard.setOnClickListener {
            App.prefs.myCode = App.prefs.codeBest
            moveBoards()
        }

        // 내가쓴글, 댓글단글, 스크랩


    }

    private fun loadCode() {
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

    private fun moveBoards(){
        var IT = Intent(applicationContext,PostingActivity::class.java)
        startActivity(IT)
    }


}
