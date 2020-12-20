package com.example.howareyou

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.howareyou.Model.*
import com.example.howareyou.Util.App
import com.example.howareyou.Util.EndlessRecyclerViewScrollListener
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home_all.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.collections.ArrayList

open class HomeBaseFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    var service: ServiceApi? = null

    var postingDTOlist: ArrayList<LoadPostItem> = arrayListOf()

    val loadLimit : Int = 100 // 한번에 불러올 게시물 양
    var getAllPost : Boolean = true // 전체 게시물 탭인지 체크
    lateinit var scrollListener: EndlessRecyclerViewScrollListener
    lateinit var homeAdapter: HomeAdapter
    lateinit var lastboard_id: String // loadMore를 위한 마지막 게시물 id

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)
        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_home_all, container, false)

        return view
    }

    override fun onRefresh() {
        // 데이터 list 초기화
        postingDTOlist.clear()

        // 전체 게시물 tab 체크
        loadSelectedPosting()
        all_swipelayout.isRefreshing = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        all_swipelayout.setOnRefreshListener(this)
    }

    open fun initAdapter() {
        homeAdapter = HomeAdapter(activity!!,postingDTOlist)
        val linearLayoutManager = LinearLayoutManager(activity)
        all_recyclerview.layoutManager = linearLayoutManager

        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadSelectedPostingMore()
            }
        }
        all_recyclerview.addOnScrollListener(scrollListener)
        all_recyclerview.adapter = homeAdapter
    }

    fun loadSelectedPosting() {
        service?.getPost(App.prefs.myCode)?.enqueue(object : Callback<LoadPostDTO?> {
            override fun onResponse(
                call: Call<LoadPostDTO?>?,
                response: Response<LoadPostDTO?>

            ) {
                if (response.isSuccessful) {
                    showProgress(false)
                    val result: LoadPostDTO = response.body()!!
                    val postSize: Int = result.size - 1

                    if (result.size != 0) {
                        for (i in 0..postSize) {

                            if(!result[i].is_deleted)
                            {
                                postingDTOlist?.add(
                                    LoadPostItem(
                                        result[i].id,
                                        result[i].title,
                                        result[i].content,
                                        result[i].author,
                                        result[i].code,
                                        result[i].comments,
                                        result[i].likeds,
                                        result[i].viewed,
                                        result[i].createdAt,
                                        result[i].header,
                                        result[i].user_id,
                                        result[i].is_deleted,
                                        result[i].image
                                    )
                                )
                            }
                            lastboard_id = result[i].id

                        }

                        //
                        homeAdapter.notifyDataSetChanged()

                    } else {
                        //TODO
                    }

                } else {
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<StatuscodeResponse> =
                        gson.getAdapter<StatuscodeResponse>(
                            StatuscodeResponse::class.java
                        )
                    try {
                        if (response.errorBody() != null) {
                            val result: StatuscodeResponse = adapter.fromJson(
                                response.errorBody()!!.string()
                            )
                            if (result.statusCode == 401) // jwt 토큰 만료
                            {

                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
                showProgress(false)
            }
        })

    }

    private fun loadSelectedPostingMore() {
        service?.getPostMore(lastboard_id, loadLimit, App.prefs.myCode)?.enqueue(
            object : Callback<LoadPostDTO?> {
                override fun onResponse(
                    call: Call<LoadPostDTO?>?,
                    response: Response<LoadPostDTO?>

                ) {
                    if (response.isSuccessful) {
                        //showProgress(false)
                        val result: LoadPostDTO = response.body()!!
                        val postSize: Int = result.size - 1
                        if (result.size != 0) {
                            for (i in 0..postSize) {
                                postingDTOlist?.add(
                                    LoadPostItem(
                                        result[i].id,
                                        result[i].title,
                                        result[i].content,
                                        result[i].author,
                                        result[i].code,
                                        result[i].comments,
                                        result[i].likeds,
                                        result[i].viewed,
                                        result[i].createdAt,
                                        result[i].header,
                                        result[i].user_id,
                                        result[i].is_deleted,
                                        result[i].image
                                    )
                                )

                                lastboard_id = result[i].id
                            }
                            // 리사이클러뷰 데이터 갱신
                            homeAdapter.notifyDataSetChanged()
                        } else {
                            //TODO
                        }

                    } else {
                        // 실패시 resopnse.errorbody를 객체화
                        val gson = Gson()
                        val adapter: TypeAdapter<StatuscodeResponse> =
                            gson.getAdapter<StatuscodeResponse>(
                                StatuscodeResponse::class.java
                            )
                        try {
                            if (response.errorBody() != null) {
                                val result: StatuscodeResponse = adapter.fromJson(
                                    response.errorBody()!!.string()
                                )
                                if (result.statusCode == 401) // jwt 토큰 만료
                                {

                                }

                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
                    Log.e("onFailure", t.message!!)
                    //showProgress(false)
                }
            })

    }

    fun showProgress(show: Boolean){
        all_layout_loading.visibility = (if (show) View.VISIBLE else View.GONE)
    }

    fun forceTouch(){
        val downTime: Long = SystemClock.uptimeMillis()
        val eventTime: Long = SystemClock.uptimeMillis()
        val down_event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, 0f, 0f, 0)
        val up_event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, 0f, 0f, 0)

        all_framelayout.dispatchTouchEvent(down_event)
        all_framelayout.dispatchTouchEvent(up_event)

        all_recyclerview.dispatchTouchEvent(down_event)
        all_recyclerview.dispatchTouchEvent(up_event)
    }



}

