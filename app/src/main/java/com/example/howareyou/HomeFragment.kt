package com.example.howareyou

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var service: ServiceApi? = null

    var postingDTOlist: ArrayList<LoadPostItem> = arrayListOf()

    private val loadLimit : Int = 100 // 한번에 불러올 게시물 양
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var lastboard_id: String // loadMore를 위한 마지막 게시물 id

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)
        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_home, container, false)

        return view
    }

    // view가 전부 생성된 뒤에 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setButton(view)
        initAdapter()
        loadPosting()
    }

    private fun setButton(view: View){
        view.home_button_refresh.setOnClickListener {
            //fragment refresh
            postingDTOlist.clear()
            loadPosting()
        }

        view.home_button_myaccount.setOnClickListener {
            startActivity(Intent(activity, AccountActivity::class.java))
        }
    }

    private fun initAdapter() {
        homeAdapter = HomeAdapter(activity!!,postingDTOlist)
        val linearLayoutManager = LinearLayoutManager(activity)
        home_recyclerview.layoutManager = linearLayoutManager

        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                loadPostingMore()
            }
        }
        home_recyclerview.addOnScrollListener(scrollListener)
        home_recyclerview.adapter = homeAdapter
    }

    // 게시글 불러오기
    private fun loadPosting() {
        service?.getAllPost("Bearer " + App.prefs.myJwt)?.enqueue(object : Callback<LoadPostDTO?> {
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
                                    result[i].is_delected,
                                    result[i].image
                                )
                            )

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

    private fun loadPostingMore() {
        service?.getAllPostMore("Bearer " + App.prefs.myJwt, lastboard_id, loadLimit)?.enqueue(
            object : Callback<LoadPostDTO?> {
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
                                        result[i].is_delected,
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
                    showProgress(false)
                }
            })

    }

    private fun showProgress(show: Boolean){
        home_layout_loading.visibility = (if (show) View.VISIBLE else View.GONE)
    }

    override fun onRefresh() {
        // 데이터 list 초기화
        postingDTOlist.clear()
        loadPosting()
        home_swipelayout.isRefreshing = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        home_swipelayout.setOnRefreshListener(this)
    }


}

