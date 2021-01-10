package com.example.howareyou.views.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.howareyou.App
import com.example.howareyou.R
import com.example.howareyou.databinding.FragmentHomeViewpagerBinding
import com.example.howareyou.views.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home_viewpager.*

@AndroidEntryPoint
class HomeAllFragment : BaseFragment<FragmentHomeViewpagerBinding>(R.layout.fragment_home_viewpager) {

    private val homePagerViewModel by viewModels<HomePagerViewModel>()
    lateinit var homeAdapter: HomeAdapter

    // view가 전부 생성된 뒤에 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        showProgress(true)
        App.prefs.myCode = App.prefs.codeFree
        binding.viewModel = homePagerViewModel
        initAdapter()
//        loadPosting()
    }

//    override fun onResume() {
//        super.onResume()
//        getAllPost = true
//        forceTouch()
//    }
//
//    override fun onRefresh() {
//        viewpager_swipelayout.isRefreshing = false
//    }

    private fun initAdapter() {

        homeAdapter = HomeAdapter(requireActivity())
        binding.viewpagerRecyclerview.adapter = homeAdapter

//        homeAdapter = HomeAdapter(requireActivity())
//        val linearLayoutManager = LinearLayoutManager(activity)
//        viewpager_recyclerview.layoutManager = linearLayoutManager
//
//        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                loadPostingMore()
//            }
//        }
//        viewpager_recyclerview.addOnScrollListener(scrollListener)
//        viewpager_recyclerview.adapter = homeAdapter
    }


    // 전체게시글 불러오기

//
//    private fun loadPostingMore() {
//        service?.getAllPostMore("Bearer " + App.prefs.myJwt, lastboard_id, loadLimit)?.enqueue(
//            object : Callback<LoadPostDTO?> {
//                override fun onResponse(
//                    call: Call<LoadPostDTO?>?,
//                    response: Response<LoadPostDTO?>
//
//                ) {
//                    if (response.isSuccessful) {
//                        showProgress(false)
//                        val result: LoadPostDTO = response.body()!!
//                        val postSize: Int = result.size - 1
//                        if (result.size != 0) {
//                            for (i in 0..postSize) {
//                                postingDTOlist?.add(
//                                    LoadPostItem(
//                                        result[i].id,
//                                        result[i].title,
//                                        result[i].content,
//                                        result[i].author,
//                                        result[i].code,
//                                        result[i].comments,
//                                        result[i].likeds,
//                                        result[i].viewed,
//                                        result[i].createdAt,
//                                        result[i].header,
//                                        result[i].user_id,
//                                        result[i].is_deleted,
//                                        result[i].image
//                                    )
//                                )
//
//                                lastboard_id = result[i].id
//                            }
//                            // 리사이클러뷰 데이터 갱신
//                            homeAdapter.notifyDataSetChanged()
//                        } else {
//                            //TODO
//                        }
//
//                    } else {
//                        // 실패시 resopnse.errorbody를 객체화
//                        val gson = Gson()
//                        val adapter: TypeAdapter<StatuscodeResponse> =
//                            gson.getAdapter<StatuscodeResponse>(
//                                StatuscodeResponse::class.java
//                            )
//                        try {
//                            if (response.errorBody() != null) {
//                                val result: StatuscodeResponse = adapter.fromJson(
//                                    response.errorBody()!!.string()
//                                )
//                                if (result.statusCode == 401) // jwt 토큰 만료
//                                {
//
//                                }
//
//                            }
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<LoadPostDTO?>?, t: Throwable) {
//                    Log.e("onFailure", t.message!!)
//                    showProgress(false)
//                }
//            })
//
//    }
}

