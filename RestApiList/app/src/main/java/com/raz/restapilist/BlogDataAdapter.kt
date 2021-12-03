package com.raz.restapilist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BlogDataAdapter(private val context: Context,val data:List<BlogData>):RecyclerView.Adapter<BlogDataAdapter.BlogDataViewHolder>() {

    class BlogDataViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
            var id:TextView=itemView.findViewById(R.id.blog_id)
            var title:TextView=itemView.findViewById(R.id.blog_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogDataViewHolder {
        val itemView=LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        return BlogDataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BlogDataViewHolder, position: Int) {
        val aktData:BlogData=data[position]
        holder.id?.text=aktData.id.toString()
        holder.title?.text=aktData.title
    }

    override fun getItemCount(): Int {
        return data.size
    }
}