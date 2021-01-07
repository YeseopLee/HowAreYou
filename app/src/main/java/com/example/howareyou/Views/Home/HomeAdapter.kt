package com.example.howareyou.views.home

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.model.LoadPostItem
import com.example.howareyou.databinding.ItemHomePostingBinding
import com.example.howareyou.views.Detail.DetailActivity
import kotlin.collections.ArrayList

class HomeAdapter(val context: Context) : RecyclerView.Adapter<CustomViewHolder>(){

    var postingDTO = ArrayList<LoadPostItem>()

    //클릭 인터페이스 정의
    interface ItemClickListener {
        fun onClick(view: View, position: Int, postArray: ArrayList<LoadPostItem>)
    }

    //클릭리스너 선언
    private lateinit var itemClickListner: ItemClickListener

    //클릭리스너 등록 매소드
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    fun setItem(data: ArrayList<LoadPostItem>){
        this.postingDTO.clear()
        this.postingDTO.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {

        val binding = ItemHomePostingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postingDTO.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        holder.onBind(postingDTO[position])

        Log.e("adapter!!",postingDTO.toString())

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("board_id",postingDTO[position].id)
            context.startActivity(intent)
        }
//
//        var view = holder.itemView
//        var text_content = postingDTO[position].content+"" // Null Exception을 막기위해 공백의 string 추가
//        var pre_text_content : String
//
//        view.homeposting_textview_content.text = text_content
//
//        if(text_content.length >= 100){
//            pre_text_content = text_content.substring(0,100) + "..."
//            view.homeposting_textview_content.text = pre_text_content
//        }
//        else {
//            view.homeposting_textview_content.text = text_content
//        }
//
//        //get Code
//        when(postingDTO[position].code?.id){
//            App.prefs.codeFree -> view.homeposting_textview_boardname.text = "자유게시판"
//            App.prefs.codeQA -> view.homeposting_textview_boardname.text = "Q&A"
//            App.prefs.codeTips -> view.homeposting_textview_boardname.text = "Tips"
//            App.prefs.codeStudy -> view.homeposting_textview_boardname.text = "스터디모집"
//            App.prefs.codeCourse -> view.homeposting_textview_boardname.text = "진로게시판"
//        }
//
//        view.homeposting_textview_title.text = postingDTO[position].title
//        view.homeposting_textview_author.text = postingDTO[position].author
//        view.homeposting_textview_comment.text = postingDTO[position].comments?.size.toString()
//        view.homeposting_textview_favorite.text = postingDTO[position].likeds?.size.toString()
//        view.homeposting_button_favorite.setBackgroundResource(R.drawable.ic_thumbsup_white)

        // 시간 convert
//        val convtime = ConvertTime()
//        view.homeposting_textview_date.text = convtime.showTime(postingDTO[position].createdAt)

        // 좋아요 체크
//        for ( i in  1..postingDTO[position].likeds!!.size)
//        {
//            if( postingDTO[position].likeds!![i-1].user_id == App.prefs.myId) view.homeposting_button_favorite.setBackgroundResource(
//                R.drawable.ic_thumbsup
//            )
//        }


//        view.setOnClickListener{
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("board_id",postingDTO[position].id)
//            context.startActivity(intent)
//        }


    }

}

class CustomViewHolder(val binding : ItemHomePostingBinding) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(data : LoadPostItem?){
        binding.postItem = data
    }
}