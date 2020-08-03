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
    private val lruCache = ImageCacheHelper.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(View.inflate(parent.context, R.layout.item_image, null))
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageBitmap = lruCache.getDataOrNull(images[position].url)
        imageBitmap?.let {
            holder.ivImage.setImageBitmap(it)
        } ?: loadImage(holder.ivImage, images[position])
    }

    private fun loadImage(ivImage: ImageView, imageData: ImageData) {
        ivImage.setImageResource(R.drawable.ic_baseline_av_timer_24)
        ivImage.tag = imageData.id
        scope.launch(Dispatchers.IO + errorHandler) {
            val bitmap = getImageBitmapFromUrl(imageData.url)
            bitmap?.let { lruCache.getLruCache()?.put(imageData.url, it) }
            withContext(Dispatchers.Main) {
                if (ivImage.tag == imageData.id)
                    ivImage.setImageBitmap(bitmap)
            }
        }
    }

    fun deleteCacheData() {
        lruCache.getLruCache()?.evictAll()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
    }
}
