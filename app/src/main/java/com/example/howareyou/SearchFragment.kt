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
import com.example.howareyou.Model.LoadPostDTO
import com.example.howareyou.Model.LoadPostItem
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //fragment view에 담는다
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_search, container, false )

        // retrofit 연결
        service = RetrofitClient.client!!.create(ServiceApi::class.java)

        loadPosting()

        // key listener
        view.search_edittext_search.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                showProgress(false)
                return@OnKeyListener true
            }
            false
        })

        return view
    }

    private fun loadPosting() {
        service?.getSearchPost()?.enqueue(object : Callback<LoadPostDTO?> {
            override fun onResponse(
                call: Call<LoadPostDTO?>?,
                response: Response<LoadPostDTO?>

            ) {
                if (response.isSuccessful) {
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
                                    result[i].is_delected
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
                //home_layout_loading.visibility = View.GONE;

            }

            override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
                Log.e("onFailure", t.message!!)
            }
        })

    }

    private fun attachAdapter(){
        //어댑터 연결
        search_recyclerview.adapter = SearchAdapter(activity!!,postingDTOlist)
        val lm = LinearLayoutManager(activity)
        search_recyclerview.layoutManager = lm
        search_recyclerview.setHasFixedSize(true)

        // 역순출력
        lm.reverseLayout = true
        lm.stackFromEnd = true

        // filter
        view?.search_edittext_search?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                (search_recyclerview.adapter as SearchAdapter).filter.filter(p0)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                (search_recyclerview.adapter as SearchAdapter).filter.filter(p0)
            }
        })

    }

    private fun showProgress(show: Boolean){
        search_layout_loading.visibility = (if (show) View.VISIBLE else View.GONE)
    }
}