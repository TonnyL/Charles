package io.github.tonnyl.charles.engine.impl

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.squareup.picasso.Picasso
import io.github.tonnyl.charles.engine.ImageEngine

class PicassoEngine : ImageEngine {

    override fun loadImage(context: Context, imageView: ImageView, uri: Uri) {
        Picasso.with(context)
                .load(uri)
                .centerCrop()
                .into(imageView)
    }

}