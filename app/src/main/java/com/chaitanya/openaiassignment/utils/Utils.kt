package com.chaitanya.openaiassignment.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
    }

    fun ImageView.loadImage(url: String, placeholderResId: Int? = null) {
        val glideRequest = Glide.with(context).load(url)

        placeholderResId?.let {
            glideRequest.placeholder(it)
        }
        glideRequest.into(this)
    }
}