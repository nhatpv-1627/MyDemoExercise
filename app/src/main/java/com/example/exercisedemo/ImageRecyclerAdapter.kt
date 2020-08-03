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
        images[position].localPath?.let {
            holder.ivImage.tag = images[position].id
            scope.launch(Dispatchers.IO + errorHandler) {
                val bitmap = getBitmapImageFromPath(it)
                withContext(Dispatchers.Main) {
                    if (holder.ivImage.tag == images[position].id)
                        holder.ivImage.setImageBitmap(bitmap)
                }
            }
        } ?: loadBitMap(holder.ivImage, images[position])
    }

    private fun loadBitMap(ivImage: ImageView, imageData: ImageData) {
        ivImage.setImageResource(R.drawable.ic_baseline_av_timer_24)
        ivImage.tag = imageData.id
        scope.launch(Dispatchers.IO + errorHandler) {
            imageData.localPath = saveImageToLocal(imageData.url, ivImage.context)
            withContext(Dispatchers.Main) {
                if (ivImage.tag == imageData.id && imageData.localPath != null)
                    ivImage.setImageBitmap(getBitmapImageFromPath(imageData.localPath!!))
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
    }
}
