package com.example.howareyou

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.howareyou.Model.PostingDTO
import kotlinx.android.synthetic.main.item_posting.view.*
import java.util.*
import kotlin.collections.ArrayList

class PostingAdapter(val context: Context, val postingDTO : ArrayList<PostingDTO>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),Filterable{

    var postingDTOfilter = ArrayList<PostingDTO>()
    init {
        postingDTOfilter = postingDTO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType) {
            0 -> {
                var view = LayoutInflater.from(context).inflate(R.layout.item_posting,parent,false)
                CustomViewHolder(view)
            }
            else -> throw RuntimeException("에러")
        }

        //return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int {
        return postingDTOfilter.size
    }

    override fun getItemViewType(position: Int): Int {
        return postingDTOfilter[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var view = holder.itemView
        var text_content = postingDTOfilter[position].content
        var pre_text_content : String

        if(text_content.length >= 100){
            pre_text_content = text_content.substring(0,100) + "..."

            view.posting_textview_content.text = pre_text_content
        }
        else {
            view.posting_textview_content.text = text_content
        }

        view.posting_textview_name.text = "#"+postingDTOfilter[position].name.toString()

        view.setOnClickListener{
            val intent = Intent(context,DetailActivity::class.java)
            intent.putExtra("content",text_content)
            context.startActivity(intent)
        }

//        view.profile_layout.setOnClickListener {
//            val iT = Intent(context,ProfileActivity::class.java)
//            iT.putExtra("name",friendDTOfilter[position].name)
//            iT.putExtra("username",username)
//            context.startActivity(iT)
//        }

    }

    /* 검색기능 */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    postingDTOfilter = postingDTO
                } else {
                    val resultList = ArrayList<PostingDTO>()
                    for (row in postingDTO)
                        if (row.content.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                    }
                    postingDTOfilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = postingDTOfilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                postingDTOfilter = results?.values as ArrayList<PostingDTO>
                notifyDataSetChanged()
            }

        }

    }
}