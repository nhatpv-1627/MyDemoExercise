package com.example.exercisedemo

import android.graphics.Bitmap
import android.util.LruCache

class ImageCacheHelper {
    private var instance: ImageCacheHelper? = null
    private var lruCache: LruCache<String, Bitmap>? = null

    init {
        lruCache = LruCache(MAX_CACHE_SIZE)
    }

    fun getLruCache() = lruCache

    fun getDataOrNull(key: String): Bitmap? {
        return try {
            lruCache?.get(key)
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val MAX_CACHE_SIZE = 50 * 1024 * 1024 // 50mb

        fun getInstance() = ImageCacheHelper().apply {
            return instance ?: ImageCacheHelper().apply { instance = this }
        }
    }
}
