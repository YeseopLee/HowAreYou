package com.example.howareyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.Model.LoadPostDTO
import com.example.howareyou.Model.LoadPostItem
import com.example.howareyou.Util.App
import com.example.howareyou.Util.EndlessRecyclerViewScrollListener
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SearchFragment : Fragment() {

    private var service: ServiceApi? = null
    val postingDTOlist: ArrayList<LoadPostItem> = arrayListOf()
    //var searchAdapter = SearchAdapter(activity!!,postingDTOlist)

    val loadLimit : Int = 100

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var lastboard_id: String
    private lateinit var target: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_search, container, false )

        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener(view)
        initAdapter()
    }

    private fun initListener(view: View){
        // key listener
        view.search_edittext_search.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                target = view.search_edittext_search.text.toString()
                searchPosting()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun initAdapter() {
        searchAdapter = SearchAdapter(activity!!, postingDTOlist)
        val linearLayoutManager = LinearLayoutManager(activity)
        search_recyclerview.layoutManager = linearLayoutManager
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                searchPostingMore(target,lastboard_id)
            }
        }
        search_recyclerview.addOnScrollListener(scrollListener)
        search_recyclerview.adapter = searchAdapter
    }

    private fun searchPosting() {
        service?.getSearchPost()?.enqueue(object : Callback<LoadPostDTO?> {
            override fun onResponse(
                call: Call<LoadPostDTO?>?,
                response: Response<LoadPostDTO?>

            ) {
                if (response.isSuccessful) {
                    val result: LoadPostDTO = response.body()!!
                    if (result.size != 0) {
                        for (i in 0 until result.size) {
                            if(result[i].title == target || result[i].content == target)
                            {
                                System.out.println(target)
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
                                System.out.println(postingDTOlist)
                            }

                            lastboard_id = result[i].id
                        }

                        // 어댑터 연결
                        initAdapter()

                    } else {
                        //TODO
                    }

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

            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
            }
        })

    }

    private fun searchPostingMore(target: String, board_id: String) {
        service?.getSearchPostMore("Bearer "+ App.prefs.myJwt,board_id,loadLimit)?.enqueue(object : Callback<LoadPostDTO?> {
            override fun onResponse(
                call: Call<LoadPostDTO?>?,
                response: Response<LoadPostDTO?>

            ) {
                if (response.isSuccessful) {
                    val result: LoadPostDTO = response.body()!!
                    if (result.size != 0) {
                        for (i in 0 until result.size) {
                            if(result[i].title == target || result[i].content == target)
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
                                        result[i].is_delected,
                                        result[i].image
                                    )
                                )
                            }
                            lastboard_id = result[i].id
                            searchAdapter.notifyDataSetChanged()
                        }

                        // 어댑터 연결
                        initAdapter()

                    } else {
                        //TODO
                    }

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

            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
            }
        })

    }

//    private fun showProgress(show: Boolean){
//        search_layout_loading.visibility = (if (show) View.VISIBLE else View.GONE)
//    }
}