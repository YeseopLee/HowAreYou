package com.example.howareyou

import android.accounts.Account
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.Model.LoadPostDTO
import com.example.howareyou.Model.LoadPostItem
import com.example.howareyou.Model.NotiItem
import com.example.howareyou.Model.NotiResponseDTO
import com.example.howareyou.Util.App
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.home_recyclerview
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.home_button_refresh
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_notification.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class NotiFragment : Fragment() {

    private var service: ServiceApi? = null

    val notiDTOList: ArrayList<NotiItem> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_notification, container, false)

        loadNotification()

        view.notification_button_myaccount.setOnClickListener {
            startActivity(Intent(activity,AccountActivity::class.java))
        }

        return view
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        loadPosting()
//    }

    override fun onResume() {
        super.onResume()
    }


    private fun loadNotification() {
        service?.getNoti()?.enqueue(object : Callback<NotiResponseDTO?> {
            override fun onResponse(
                call: Call<NotiResponseDTO?>?,
                response: Response<NotiResponseDTO?>

            ) {
                if (response.isSuccessful) {
                    //showProgress(false)
                    val result: NotiResponseDTO = response.body()!!
                    for ( i in 0 until result.size){
                        if(App.prefs.myId == result[i].user_id) notiDTOList.add(NotiItem(result[i].user_id,result[i].content,result[i].createdAt,result[i].board,result[i]._id,result[i].viewed))
                    }
                    attachAdapter()
                } else {
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<LoadPostDTO> = gson.getAdapter<LoadPostDTO>(
                        LoadPostDTO::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            val result: LoadPostDTO = adapter.fromJson(
                                response.errorBody()!!.string()
                            )

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<NotiResponseDTO?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
                //showProgress(false)
            }
        })

    }

    private fun attachAdapter(){
        //어댑터 연결
        notification_recyclerview.adapter = NotiAdapter(activity!!,notiDTOList)
        val lm = LinearLayoutManager(activity)
        notification_recyclerview.layoutManager = lm
        notification_recyclerview.setHasFixedSize(true)

        // 역순출력
        lm.reverseLayout = true
        lm.stackFromEnd = true


    }

//    private fun showProgress(show: Boolean){
//        home_layout_loading.visibility = (if (show) View.VISIBLE else View.GONE)
//    }


}

