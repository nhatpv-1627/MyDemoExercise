package com.example.exercisedemo

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageRecyclerAdapter(private val urls: List<String>) :
    RecyclerView.Adapter<ImageRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(View.inflate(parent.context, R.layout.item_image, null))
    }

    override fun getItemCount(): Int {
        return urls.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        Glide.with(holder.itemView.context).load(urls[position])
//            .placeholder(R.drawable.ic_baseline_av_timer_24).into(holder.ivImage)
        LoadImageManager(holder.ivImage).execute(urls[position])
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
    }
}
