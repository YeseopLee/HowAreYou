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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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

class NotiFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var service: ServiceApi? = null

    val notiDTOList: ArrayList<NotiItem> = arrayListOf()

    private lateinit var notiAdapter: NotiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_notification, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton(view)
        initAdapter()
        loadNotification()

    }

    fun setButton(view: View){
        view.notification_button_myaccount.setOnClickListener {
            startActivity(Intent(activity,AccountActivity::class.java))
        }
    }


    override fun onRefresh() {
        // 데이터 list 초기화
        notiDTOList.clear()
        loadNotification()
        notification_swipelayout.isRefreshing = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notification_swipelayout.setOnRefreshListener(this)
    }

    private fun initAdapter(){
        //어댑터 연결
        notiAdapter = NotiAdapter(activity!!,notiDTOList)
        notification_recyclerview.adapter = notiAdapter
        val lm = LinearLayoutManager(activity)
        notification_recyclerview.layoutManager = lm
        notification_recyclerview.setHasFixedSize(true)

        // 역순출력
        lm.reverseLayout = true
        lm.stackFromEnd = true

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
                    notiAdapter.notifyDataSetChanged()

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


//    private fun showProgress(show: Boolean){
//        home_layout_loading.visibility = (if (show) View.VISIBLE else View.GONE)
//    }


}

