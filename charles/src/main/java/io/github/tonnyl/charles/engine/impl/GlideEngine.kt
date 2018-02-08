package io.github.tonnyl.charles.engine.impl

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.tonnyl.charles.engine.ImageEngine

class GlideEngine : ImageEngine {

    override fun loadImage(context: Context, imageView: ImageView, uri: Uri) {
        Glide.with(context)
                .load(uri)
                .apply(RequestOptions().centerCrop())
                .into(imageView)
    }

}