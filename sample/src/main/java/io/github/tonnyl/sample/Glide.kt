package io.github.tonnyl.sample

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import io.github.tonnyl.charles.engine.ImageEngine

@GlideModule
class CharlesAppGlideModule : AppGlideModule()

class GlideLoader : ImageEngine {

    override fun loadImage(context: Context, imageView: ImageView, uri: Uri) {
        GlideApp.with(context)
                .load(uri)
                .centerCrop()
                .into(imageView)
    }

}