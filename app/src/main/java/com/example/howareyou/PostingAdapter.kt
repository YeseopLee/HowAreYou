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

    var friendDTOfilter = ArrayList<PostingDTO>()

    init {
        friendDTOfilter = postingDTO
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
        return friendDTOfilter.size
    }

    override fun getItemViewType(position: Int): Int {
        return friendDTOfilter[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var view = holder.itemView
        view.item_textview_content.text = friendDTOfilter[position].content
        view.item_textview_number.text = "#"+friendDTOfilter[position].number.toString()

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
                    friendDTOfilter = postingDTO
                } else {
                    val resultList = ArrayList<PostingDTO>()
                    for (row in postingDTO)
                        if (row.content.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                    }
                    friendDTOfilter = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = friendDTOfilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                friendDTOfilter = results?.values as ArrayList<PostingDTO>
                notifyDataSetChanged()
            }

        }

    }
}