package com.example.exercisedemo

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageRecyclerAdapter(
    private val scope: LifecycleCoroutineScope,
    private val images: List<ImageData>,
    private val errorHandler: CoroutineExceptionHandler
) :
    RecyclerView.Adapter<ImageRecyclerAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(View.inflate(parent.context, R.layout.item_image, null))
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (images[position].bitmap != null) {
            holder.ivImage.setImageBitmap(images[position].bitmap)
        } else {
            holder.ivImage.setImageResource(R.drawable.ic_baseline_av_timer_24)
            holder.ivImage.tag = images[position].id
            scope.launch(Dispatchers.IO + errorHandler) {
                images[position].bitmap = getImageBitmapFromUrl(images[position].url)
                withContext(Dispatchers.Main) {
                    if (holder.ivImage.tag == images[position].id)
                        holder.ivImage.setImageBitmap(images[position].bitmap)
                }
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
    }
}
