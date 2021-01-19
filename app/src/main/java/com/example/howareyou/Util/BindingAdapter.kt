package com.example.howareyou.util

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howareyou.model.Comment
import com.example.howareyou.model.ImageDTO
import com.example.howareyou.model.LoadPostItem
import com.example.howareyou.model.NotiItem
import com.example.howareyou.views.detail.DetailCommentAdapter
import com.example.howareyou.views.detail.DetailImageAdapter
import com.example.howareyou.views.home.HomeAdapter
import com.example.howareyou.views.home.HomePagerViewModel
import com.example.howareyou.views.noti.NotiAdapter
import com.example.howareyou.views.search.SearchAdapter
import com.example.howareyou.views.search.SearchViewModel


object BindingAdapter {

    @BindingAdapter("imgRes")
    @JvmStatic
    fun setImageResource(v : ImageView, imageUrl : String?){
        Glide.with(v.context).load(imageUrl).into(v)
    }

    @BindingAdapter("imgUriRes")
    @JvmStatic
    fun setImageUriResource(v : ImageView, imageUrl : Uri?){
        Glide.with(v.context).load(imageUrl).into(v)
    }

    // recyclerview 데이터 검색
    @BindingAdapter("loadPostData")
    @JvmStatic
    fun bindRecyclerView(recyclerView: RecyclerView, data: ArrayList<LoadPostItem>?) {
        val adapter = recyclerView.adapter as HomeAdapter
        if (data != null) {
            adapter.setItem(data)
        }
    }
    // recyclerview search
    @BindingAdapter("loadSearchData")
    @JvmStatic
    fun bindSearchRecyclerView(recyclerView: RecyclerView, data: ArrayList<LoadPostItem>?) {
        val adapter = recyclerView.adapter as SearchAdapter
        if (data != null) {
            adapter.setItem(data)
        }
    }

    // recyclerview noti
    @BindingAdapter("loadNotiData")
    @JvmStatic
    fun bindNotiRecyclerView(recyclerView: RecyclerView, data: ArrayList<NotiItem>?) {
        val adapter = recyclerView.adapter as NotiAdapter
        if (data != null) {
            adapter.setItem(data)
        }
    }

    // endless scroll
    @BindingAdapter("endlessScroll")
    @JvmStatic
    fun setEndlessScroll(recyclerView: RecyclerView, viewModel: HomePagerViewModel) {
        val scrollListener = object : EndlessRecyclerViewScrollListener(recyclerView.layoutManager as LinearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.loadPostMore()
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
        //recyclerView.adapter = recyclerView.adapter as HomeAdapter
    }

    // endless search scroll
    @BindingAdapter("endlessSearchScroll")
    @JvmStatic
    fun setEndlessSearchScroll(recyclerView: RecyclerView, viewModel: SearchViewModel) {
        val scrollListener = object : EndlessRecyclerViewScrollListener(recyclerView.layoutManager as LinearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.searchPostingMore()
            }
        }
        recyclerView.addOnScrollListener(scrollListener)
        //recyclerView.adapter = recyclerView.adapter as HomeAdapter
    }

    // recyclerview 데이터 검색
    @BindingAdapter("loadDetailImageData")
    @JvmStatic
    fun bindDetailImageRecyclerView(recyclerView: RecyclerView, data: ArrayList<ImageDTO>?) {
        val adapter = recyclerView.adapter as DetailImageAdapter?
        if (data != null) {
            adapter?.setItem(data)
        }
    }

    // recyclerview 데이터 검색
    @BindingAdapter("loadDetailCommentData")
    @JvmStatic
    fun bindDetailCommentRecyclerView(recyclerView: RecyclerView, data: ArrayList<Comment>?) {
        val adapter = recyclerView.adapter as DetailCommentAdapter?
        if (data != null) {
            adapter?.setItem(data)
        }
    }

    // recyclerview 아이템 클릭 리스너
//    @BindingAdapter("itemClickListener")
//    @JvmStatic
//    fun clickRecyclerView(recyclerView: RecyclerView, viewModel : SearchViewModel){
//        val adapter = recyclerView.adapter as SearchAdapter
//        adapter.setItemClickListener(object : SearchAdapter.ItemClickListener{
//            override fun onClick(view: View, position: Int, searchArray: ArrayList<RepoSearchResponse.RepoItem>) {
//                //val bundle = bundleOf("owner" to searchArray[position].owner.login, "name" to searchArray[position].name)
//                MyApplication.prefs.selectedOwner = searchArray[position].owner.login
//                MyApplication.prefs.selectedName = searchArray[position].name
//                view.findNavController().navigate(R.id.action_searchFragment_to_detailFragment)
//            }
//        })
//    }

    @BindingAdapter("itemViewClickListener")
    @JvmStatic
    fun clickViewRecyclerView(recyclerView: RecyclerView, viewModel: DetailCommentAdapter) {
        val adapter = recyclerView.adapter as DetailCommentAdapter
        adapter.setItemClickListener(object : DetailCommentAdapter.ItemClickListener {
            override fun onClick(
                view: View,
                position: Int,
                postArray: ArrayList<LoadPostItem>
            ) {
                //val bundle = bundleOf("owner" to searchArray[position].owner.login, "name" to searchArray[position].name)
            }
        })
    }

}


