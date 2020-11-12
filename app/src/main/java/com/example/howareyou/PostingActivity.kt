package com.example.howareyou

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howareyou.Model.LoadPostDTO
import com.example.howareyou.Model.PostingDTO
import com.example.howareyou.Model.SigninDTO
import com.example.howareyou.Model.SigninResponseDTO
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_posting.*
import kotlinx.android.synthetic.main.activity_signin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.ArrayList

class PostingActivity : AppCompatActivity() {

    private var service: ServiceApi? = null
    lateinit var board_category: String
    var postingDTOlist : ArrayList<PostingDTO> = arrayListOf()
    var mAdapter = PostingAdapter(this,postingDTOlist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posting)

        board_category = intent.getStringExtra("board_category")
        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        //어댑터 연결
        posting_recyclerview.adapter = mAdapter
        val lm = LinearLayoutManager(this)
        posting_recyclerview.layoutManager = lm
        posting_recyclerview.setHasFixedSize(true)

        // 리사이클러뷰 역순 출력
        lm.reverseLayout = true
        lm.stackFromEnd = true

        loadingBranch()

        posting_button_post.setOnClickListener {
            startActivity(Intent(this,WritingActivity::class.java))
            finish()
        }

    }

    private fun loadingBranch(){
        when(board_category){
            "01" -> loadFreePosting()
            "02" -> loadQAposting()
            "03" -> loadTipsPosting()
            "04" -> loadCoursePosting()
            "05" -> loadStudyPosting()
            "06" -> loadBestPosting()
        }
    }

    private fun loadFreePosting() {
        service?.getFreePost()?.enqueue(object : Callback<LoadPostDTO?> {
            override fun onResponse(
                call: Call<LoadPostDTO?>?,
                response: Response<LoadPostDTO?>

            ) {
                if(response.isSuccessful)
                {
                    val result: LoadPostDTO = response.body()!!
                    System.out.println(result.toString())
                    val postSize : Int = result.size
                    for (i in 1..postSize){
                        postingDTOlist.add(PostingDTO(result[i-1].email,result[i-1].board_category,result[i-1].header,result[i-1].title,result[i-1].author,result[i-1].content,
                            result[i-1].liked,result[i-1].views,result[i-1].reported,result[i-1].is_deleted,result[i-1].comments_no,result[i-1].created_at))
                    }
                    // 리사이클러뷰 데이터 갱신
                    showProgress(false)
                    mAdapter.notifyDataSetChanged()

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
            }
        })

    }

    private fun loadQAposting() {
        service?.getQAPost()?.enqueue(object : Callback<LoadPostDTO?> {
            override fun onResponse(
                call: Call<LoadPostDTO?>?,
                response: Response<LoadPostDTO?>

            ) {
                if(response.isSuccessful)
                {
                    val result: LoadPostDTO = response.body()!!
                    System.out.println(result.toString())
                    val postSize : Int = result.size
                    for (i in 1..postSize){
                        postingDTOlist.add(PostingDTO(result[i-1].email,result[i-1].board_category,result[i-1].header,result[i-1].title,result[i-1].author,result[i-1].content,
                            result[i-1].liked,result[i-1].views,result[i-1].reported,result[i-1].is_deleted,result[i-1].comments_no,result[i-1].created_at))
                    }
                    // 리사이클러뷰 데이터 갱신
                    showProgress(false)
                    mAdapter.notifyDataSetChanged()

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
            }
        })

    }

    private fun loadTipsPosting() {
        service?.getTipsPost()?.enqueue(object : Callback<LoadPostDTO?> {
            override fun onResponse(
                call: Call<LoadPostDTO?>?,
                response: Response<LoadPostDTO?>

            ) {
                if(response.isSuccessful)
                {
                    val result: LoadPostDTO = response.body()!!
                    System.out.println(result.toString())
                    val postSize : Int = result.size
                    for (i in 1..postSize){
                        postingDTOlist.add(PostingDTO(result[i-1].email,result[i-1].board_category,result[i-1].header,result[i-1].title,result[i-1].author,result[i-1].content,
                            result[i-1].liked,result[i-1].views,result[i-1].reported,result[i-1].is_deleted,result[i-1].comments_no,result[i-1].created_at))
                    }
                    // 리사이클러뷰 데이터 갱신
                    showProgress(false)
                    mAdapter.notifyDataSetChanged()

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
            }
        })

    }

    private fun loadCoursePosting() {
        service?.getCoursePost()?.enqueue(object : Callback<LoadPostDTO?> {
            override fun onResponse(
                call: Call<LoadPostDTO?>?,
                response: Response<LoadPostDTO?>

            ) {
                if(response.isSuccessful)
                {
                    val result: LoadPostDTO = response.body()!!
                    System.out.println(result.toString())
                    val postSize : Int = result.size
                    for (i in 1..postSize){
                        postingDTOlist.add(PostingDTO(result[i-1].email,result[i-1].board_category,result[i-1].header,result[i-1].title,result[i-1].author,result[i-1].content,
                            result[i-1].liked,result[i-1].views,result[i-1].reported,result[i-1].is_deleted,result[i-1].comments_no,result[i-1].created_at))
                    }
                    // 리사이클러뷰 데이터 갱신
                    showProgress(false)
                    mAdapter.notifyDataSetChanged()

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
            }
        })

    }

    private fun loadStudyPosting() {
        service?.getStudyPost()?.enqueue(object : Callback<LoadPostDTO?> {
            override fun onResponse(
                call: Call<LoadPostDTO?>?,
                response: Response<LoadPostDTO?>

            ) {
                if(response.isSuccessful)
                {
                    val result: LoadPostDTO = response.body()!!
                    System.out.println(result.toString())
                    val postSize : Int = result.size
                    for (i in 1..postSize){
                        postingDTOlist.add(PostingDTO(result[i-1].email,result[i-1].board_category,result[i-1].header,result[i-1].title,result[i-1].author,result[i-1].content,
                            result[i-1].liked,result[i-1].views,result[i-1].reported,result[i-1].is_deleted,result[i-1].comments_no,result[i-1].created_at))
                    }
                    // 리사이클러뷰 데이터 갱신
                    showProgress(false)
                    mAdapter.notifyDataSetChanged()

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
            }
        })

    }

    private fun loadBestPosting() {
        service?.getBestPost()?.enqueue(object : Callback<LoadPostDTO?> {
            override fun onResponse(
                call: Call<LoadPostDTO?>?,
                response: Response<LoadPostDTO?>

            ) {
                if(response.isSuccessful)
                {
                    val result: LoadPostDTO = response.body()!!
                    System.out.println(result.toString())
                    val postSize : Int = result.size
                    for (i in 1..postSize){
                        postingDTOlist.add(PostingDTO(result[i-1].email,result[i-1].board_category,result[i-1].header,result[i-1].title,result[i-1].author,result[i-1].content,
                            result[i-1].liked,result[i-1].views,result[i-1].reported,result[i-1].is_deleted,result[i-1].comments_no,result[i-1].created_at))
                    }
                    // 리사이클러뷰 데이터 갱신
                    showProgress(false)
                    mAdapter.notifyDataSetChanged()

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
            }
        })

    }

    private fun showProgress(show: Boolean) {
        posting_progressbar.visibility = (if (show) View.VISIBLE else View.GONE)
    }


}
