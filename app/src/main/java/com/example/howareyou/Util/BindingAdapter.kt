package com.example.howareyou.Util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.model.LoadPostItem
import com.example.howareyou.views.home.HomeAdapter


object BindingAdapter {

//    @BindingAdapter("imgRes")
//    @JvmStatic
//    fun setImageResource(v : ImageView, imageUrl : String?){
//        Glide.with(v.context).load(imageUrl).into(v)
//    }

    // recyclerview 데이터 검색
    @BindingAdapter("loadPostData")
    @JvmStatic
    fun bindRecyclerView(recyclerView: RecyclerView, data: ArrayList<LoadPostItem>?) {
        val adapter = recyclerView.adapter as HomeAdapter
        if (data != null) {
            adapter.setItem(data)
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

}


