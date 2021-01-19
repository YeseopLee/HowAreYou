package com.example.howareyou.views.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.model.LoadPostItem
import com.example.howareyou.R
import com.example.howareyou.databinding.FragmentNotificationBinding
import com.example.howareyou.databinding.FragmentSearchBinding
import com.example.howareyou.util.EndlessRecyclerViewScrollListener
import com.example.howareyou.network.RetrofitClient
import com.example.howareyou.network.ServiceApi
import com.example.howareyou.views.BaseFragment
import com.example.howareyou.views.home.HomeAdapter
import com.example.howareyou.views.noti.NotiViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search){

    private val searchViewModel by viewModels<SearchViewModel>()
    lateinit var searchAdapter: SearchAdapter

    private lateinit var target: String

    // view가 전부 생성된 뒤에 호출
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = searchViewModel
        initListener(view)
        initAdapter()

    }

    private fun initListener(view: View){
        // key listener
        view.search_edittext_search.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                Log.e("searched00","searched00")
                target = view.search_edittext_search.text.toString()
//                showProgress(true)
                searchViewModel.getValue(target)
                searchViewModel.searchPosting()
                return@OnKeyListener true
            }
            false
        })
    }


    private fun initAdapter() {
        searchAdapter = SearchAdapter(requireActivity())
        binding.searchRecyclerview.adapter = searchAdapter

//        val linearLayoutManager = LinearLayoutManager(activity)
//        search_recyclerview.layoutManager = linearLayoutManager
//        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                searchPostingMore(target,lastboard_id)
//            }
//        }
//        search_recyclerview.addOnScrollListener(scrollListener)
//        search_recyclerview.adapter = searchAdapter
    }



    }
//
