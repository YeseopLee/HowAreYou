package com.example.howareyou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.Model.*
import com.example.howareyou.Util.App
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import kotlinx.android.synthetic.main.fragment_posting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.collections.ArrayList

class PostingActivity : AppCompatActivity() {

    private var service: ServiceApi? = null
    var postingDTOlist : ArrayList<LoadPostItem> = arrayListOf()
    var mAdapter = PostingAdapter(this,postingDTOlist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_posting)

        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        //어댑터 연결
        posting_recyclerview.adapter = mAdapter
        val lm = LinearLayoutManager(this)
        posting_recyclerview.layoutManager = lm
        posting_recyclerview.setHasFixedSize(true)

        // 리사이클러뷰 역순 출력
        lm.reverseLayout = true
        lm.stackFromEnd = true

        loadPosting()

//        posting_button_back.setOnClickListener {
//            finish()
//        }

    }

    override fun onResume() {
        super.onResume()

        // refresh activity
        mAdapter?.notifyDataSetChanged()

    }

    private fun loadPosting() {
        service?.getPost(App.prefs.myCode)?.enqueue(object : Callback<LoadPostDTO?> {
            override fun onResponse(
                call: Call<LoadPostDTO?>?,
                response: Response<LoadPostDTO?>

            ) {
                if(response.isSuccessful)
                {
                    showProgress(false);
                    val result: LoadPostDTO = response.body()!!
                    val postSize: Int = result.size-1

                    if(result.size != 0)
                    {
                        for (i in 0..postSize){

                            postingDTOlist?.add(LoadPostItem(result[i].id,result[i].title,result[i].content,result[i].author,result[i].code,result[i].comments,result[i].likeds,result[i].viewed,result[i].createdAt
                                ,result[i].header,result[i].user_id,result[i].is_delected))

                        }
                        // 리사이클러뷰 데이터 갱신
                        showProgress(false)
                        mAdapter?.notifyDataSetChanged()
                    }else{
                        //TODO
                    }

                }else {
                    // 실패시 resopnse.errorbody를 객체화
                    val gson = Gson()
                    val adapter: TypeAdapter<LoadPostDTO> = gson.getAdapter<LoadPostDTO>(
                        LoadPostDTO::class.java
                    )
                    try {
                        if (response.errorBody() != null) {
                            showProgress(false)
                            val result : LoadPostDTO = adapter.fromJson(response.errorBody()!!.string())

                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
                showProgress(false);
            }
        })

    }

//    private fun loadQAposting() {
//        service?.getQAPost()?.enqueue(object : Callback<LoadPostDTO?> {
//            override fun onResponse(
//                call: Call<LoadPostDTO?>?,
//                response: Response<LoadPostDTO?>
//
//            ) {
//                if(response.isSuccessful)
//                {
//                    val result: LoadPostDTO = response.body()!!
//                    System.out.println(result.toString())
//                    val postSize : Int = result.size
//                    for (i in 1..postSize){
//                        postingDTOlist.add(PostingDTO(result[i-1].email,result[i-1].board_category,result[i-1].header,result[i-1].title,result[i-1].author,result[i-1].content,
//                            result[i-1].liked,result[i-1].views,result[i-1].reported,result[i-1].is_deleted,result[i-1].comments_no,result[i-1].created_at))
//                    }
//                    // 리사이클러뷰 데이터 갱신
//                    showProgress(false)
//                    mAdapter.notifyDataSetChanged()
//
//                }else {
//                    // 실패시 resopnse.errorbody를 객체화
//                    val gson = Gson()
//                    val adapter: TypeAdapter<LoadPostDTO> = gson.getAdapter<LoadPostDTO>(
//                        LoadPostDTO::class.java
//                    )
//                    try {
//                        if (response.errorBody() != null) {
//                            showProgress(false)
//                            val result : LoadPostDTO = adapter.fromJson(response.errorBody()!!.string())
//
//                        }
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
//                Log.e("onFailure", t.message!!)
//            }
//        })
//
//    }
//
//    private fun loadTipsPosting() {
//        service?.getTipsPost()?.enqueue(object : Callback<LoadPostDTO?> {
//            override fun onResponse(
//                call: Call<LoadPostDTO?>?,
//                response: Response<LoadPostDTO?>
//
//            ) {
//                if(response.isSuccessful)
//                {
//                    val result: LoadPostDTO = response.body()!!
//                    System.out.println(result.toString())
//                    val postSize : Int = result.size
//                    for (i in 1..postSize){
//                        postingDTOlist.add(PostingDTO(result[i-1].email,result[i-1].board_category,result[i-1].header,result[i-1].title,result[i-1].author,result[i-1].content,
//                            result[i-1].liked,result[i-1].views,result[i-1].reported,result[i-1].is_deleted,result[i-1].comments_no,result[i-1].created_at))
//                    }
//                    // 리사이클러뷰 데이터 갱신
//                    showProgress(false)
//                    mAdapter.notifyDataSetChanged()
//
//                }else {
//                    // 실패시 resopnse.errorbody를 객체화
//                    val gson = Gson()
//                    val adapter: TypeAdapter<LoadPostDTO> = gson.getAdapter<LoadPostDTO>(
//                        LoadPostDTO::class.java
//                    )
//                    try {
//                        if (response.errorBody() != null) {
//                            showProgress(false)
//                            val result : LoadPostDTO = adapter.fromJson(response.errorBody()!!.string())
//
//                        }
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
//                Log.e("onFailure", t.message!!)
//            }
//        })
//
//    }
//
//    private fun loadCoursePosting() {
//        service?.getCoursePost()?.enqueue(object : Callback<LoadPostDTO?> {
//            override fun onResponse(
//                call: Call<LoadPostDTO?>?,
//                response: Response<LoadPostDTO?>
//
//            ) {
//                if(response.isSuccessful)
//                {
//                    val result: LoadPostDTO = response.body()!!
//                    System.out.println(result.toString())
//                    val postSize : Int = result.size
//                    for (i in 1..postSize){
//                        postingDTOlist.add(PostingDTO(result[i-1].email,result[i-1].board_category,result[i-1].header,result[i-1].title,result[i-1].author,result[i-1].content,
//                            result[i-1].liked,result[i-1].views,result[i-1].reported,result[i-1].is_deleted,result[i-1].comments_no,result[i-1].created_at))
//                    }
//                    // 리사이클러뷰 데이터 갱신
//                    showProgress(false)
//                    mAdapter.notifyDataSetChanged()
//
//                }else {
//                    // 실패시 resopnse.errorbody를 객체화
//                    val gson = Gson()
//                    val adapter: TypeAdapter<LoadPostDTO> = gson.getAdapter<LoadPostDTO>(
//                        LoadPostDTO::class.java
//                    )
//                    try {
//                        if (response.errorBody() != null) {
//                            showProgress(false)
//                            val result : LoadPostDTO = adapter.fromJson(response.errorBody()!!.string())
//
//                        }
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
//                Log.e("onFailure", t.message!!)
//            }
//        })
//
//    }
//
//    private fun loadStudyPosting() {
//        service?.getStudyPost()?.enqueue(object : Callback<LoadPostDTO?> {
//            override fun onResponse(
//                call: Call<LoadPostDTO?>?,
//                response: Response<LoadPostDTO?>
//
//            ) {
//                if(response.isSuccessful)
//                {
//                    val result: LoadPostDTO = response.body()!!
//                    System.out.println(result.toString())
//                    val postSize : Int = result.size
//                    for (i in 1..postSize){
//                        postingDTOlist.add(PostingDTO(result[i-1].email,result[i-1].board_category,result[i-1].header,result[i-1].title,result[i-1].author,result[i-1].content,
//                            result[i-1].liked,result[i-1].views,result[i-1].reported,result[i-1].is_deleted,result[i-1].comments_no,result[i-1].created_at))
//                    }
//                    // 리사이클러뷰 데이터 갱신
//                    showProgress(false)
//                    mAdapter.notifyDataSetChanged()
//
//                }else {
//                    // 실패시 resopnse.errorbody를 객체화
//                    val gson = Gson()
//                    val adapter: TypeAdapter<LoadPostDTO> = gson.getAdapter<LoadPostDTO>(
//                        LoadPostDTO::class.java
//                    )
//                    try {
//                        if (response.errorBody() != null) {
//                            showProgress(false)
//                            val result : LoadPostDTO = adapter.fromJson(response.errorBody()!!.string())
//
//                        }
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
//                Log.e("onFailure", t.message!!)
//            }
//        })
//
//    }
//
//    private fun loadBestPosting() {
//        service?.getBestPost()?.enqueue(object : Callback<LoadPostDTO?> {
//            override fun onResponse(
//                call: Call<LoadPostDTO?>?,
//                response: Response<LoadPostDTO?>
//
//            ) {
//                if(response.isSuccessful)
//                {
//                    val result: LoadPostDTO = response.body()!!
//                    System.out.println(result.toString())
//                    val postSize : Int = result.size
//                    for (i in 1..postSize){
//                        postingDTOlist.add(PostingDTO(result[i-1].email,result[i-1].board_category,result[i-1].header,result[i-1].title,result[i-1].author,result[i-1].content,
//                            result[i-1].liked,result[i-1].views,result[i-1].reported,result[i-1].is_deleted,result[i-1].comments_no,result[i-1].created_at))
//                    }
//                    // 리사이클러뷰 데이터 갱신
//                    showProgress(false)
//                    mAdapter.notifyDataSetChanged()
//
//                }else {
//                    // 실패시 resopnse.errorbody를 객체화
//                    val gson = Gson()
//                    val adapter: TypeAdapter<LoadPostDTO> = gson.getAdapter<LoadPostDTO>(
//                        LoadPostDTO::class.java
//                    )
//                    try {
//                        if (response.errorBody() != null) {
//                            showProgress(false)
//                            val result : LoadPostDTO = adapter.fromJson(response.errorBody()!!.string())
//
//                        }
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
//                Log.e("onFailure", t.message!!)
//            }
//        })
//
//    }

    private fun showProgress(show: Boolean) {
        posting_progressbar.visibility = (if (show) View.VISIBLE else View.GONE)
    }


}
