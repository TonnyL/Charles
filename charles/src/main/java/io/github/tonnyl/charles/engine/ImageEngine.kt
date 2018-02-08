package io.github.tonnyl.charles.engine

import android.content.Context
import android.net.Uri
import android.widget.ImageView


/**
 * Created by lizhaotailang on 30/01/2018.
 */
interface ImageEngine {

    /**
     * Load a static image resource.
     *
     * @param context   Context
     * @param imageView ImageView widget
     * @param uri       Uri of the loaded image
     */
    fun loadImage(context: Context, imageView: ImageView, uri: Uri)


}