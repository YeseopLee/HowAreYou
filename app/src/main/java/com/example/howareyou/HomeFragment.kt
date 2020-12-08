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
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.Model.LoadPostDTO
import com.example.howareyou.Model.LoadPostItem
import com.example.howareyou.Model.StatuscodeResponse
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

class HomeFragment : Fragment() {

    private var service: ServiceApi? = null
    val postingDTOlist: ArrayList<LoadPostItem> = arrayListOf()

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)
        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_home, container, false)

        loadPosting()
        setButton(view)

        System.out.println("code test"+App.prefs.myCode)
        System.out.println("code test"+App.prefs.codeFree)
        return view
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        loadPosting()
//    }

    private fun setButton(view : View){

        view.home_button_refresh.setOnClickListener {
            //fragment refresh
            val ft = fragmentManager!!.beginTransaction()
            ft.detach(this).attach(this).commit()
            loadPosting()
        }

        view.home_button_myaccount.setOnClickListener {
            startActivity(Intent(activity,AccountActivity::class.java))
        }
    }

    private fun loadPosting() {
        service?.getAllPost("Bearer "+App.prefs.myJwt)?.enqueue(object : Callback<LoadPostDTO?> {
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
                        }

                        // 어댑터 연결
                        attachAdapter()
                        //mAdapter?.notifyDataSetChanged()
                    } else {
                        //TODO
                    }

                } else {
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<StatuscodeResponse> = gson.getAdapter<StatuscodeResponse>(
                        StatuscodeResponse::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            val result: StatuscodeResponse = adapter.fromJson(
                                response.errorBody()!!.string()
                            )
                            if(result.statusCode == 401) // jwt 토큰 만료
                            {

                            }

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                //home_layout_loading.visibility = View.GONE;

            }

            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
                System.out.println("loading!4")
                Log.e("onFailure", t.message!!)
                showProgress(false)
//                home_layout_loading.visibility = View.GONE;
            }
        })

    }

    private fun attachAdapter(){
        //어댑터 연결
        home_recyclerview.adapter = HomeAdapter(activity!!, postingDTOlist)
        val lm = LinearLayoutManager(activity)
        home_recyclerview.layoutManager = lm
        home_recyclerview.setHasFixedSize(true)

        scrollListener = object : EndlessRecyclerViewScrollListener(lm) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                //requestPagingMovie(query, totalItemsCount + 1)
            }
        }

        home_recyclerview.addOnScrollListener(scrollListener)

        // 역순출력
        //lm.reverseLayout = true
        //lm.stackFromEnd = true

    }

//    private fun requestMovie(query: String) {
//        scrollListener.resetState()
//        myApplication.movieRepository.getSearchMovies(query,
//            success = {
//                if (it.isEmpty()) {
//                    onToastMessage("해당 영화는 존재하지 않습니다.")
//                } else {
//                    movieAdapter.clear()
//                    movieAdapter.setItems(it)
//                    onToastMessage("영화를 불러왔습니다.")
//                }
//            },
//            fail = {
//                Log.d(TAG, it.toString())
//                when (it) {
//                    is HttpException -> onToastMessage("네트워크에 문제가 있습니다.")
//                    else -> onToastMessage(it.message.toString())
//                }
//            })
//    }

    private fun showProgress(show: Boolean){
        home_layout_loading.visibility = (if (show) View.VISIBLE else View.GONE)
    }


}

