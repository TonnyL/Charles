/*
 * Copyright (c) 2018 Li Zhao Tai Lang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.tonnyl.charles.ui.adapter

import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.graphics.PorterDuff
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import io.github.tonnyl.charles.R
import io.github.tonnyl.charles.internal.data.MediaFilterType
import io.github.tonnyl.charles.internal.data.MediaItem
import io.github.tonnyl.charles.internal.data.SelectedItemCollection
import io.github.tonnyl.charles.internal.data.SelectionSpec
import kotlinx.android.synthetic.main.item_media_item.view.*

class MediaItemsAdapter(mediaFilterType: MediaFilterType?, selectedItemCollection: SelectedItemCollection) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mMediaFilterType = mediaFilterType
    private val mSelectedItemCollection = selectedItemCollection
    private var mCursor: Cursor? = null
    private var mContext: Context? = null
    private var mMediaItemClickListener: MediaItemsAdapter.OnMediaItemClickListener? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mCursor?.moveToPosition(position)

        mCursor?.let {
            // get the background color when the item is selected
            val ta = mContext?.obtainStyledAttributes(intArrayOf(R.attr.media_selected_backgroundColor))
            val selectedBackground = ta?.getColor(0, 0)
            ta?.recycle()

            when (mMediaFilterType) {
                MediaFilterType.IMAGE -> {
                    val image = MediaItem.valueOf(it, MediaFilterType.IMAGE)
                    with((holder as MediaItemViewHolder).itemView) {
                        nameTextView.text = image.name
                        var size = image.size / 1024.0
                        val sizeText: String
                        if (size >= 1024) {
                            size /= 1024
                            sizeText = context.getString(R.string.mb).format(size)
                        } else {
                            sizeText = context.getString(R.string.kb).format(size)
                        }
                        descTextView.text = context.getString(R.string.media_item_desc)
                                .format(DateUtils.formatDateTime(context, image.time * 1000, DateUtils.FORMAT_SHOW_DATE),
                                        sizeText,
                                        MimeTypeMap.getSingleton().getExtensionFromMimeType(image.mimeType).toUpperCase())
                        mContext?.let {
                            SelectionSpec.INSTANCE.imageEngine.loadImage(it, imageView, image.uri)
                        }

                        if (mSelectedItemCollection.isSelected(image)) {
                            mediaCheckBox.setChecked(true, false)
                            selectedBackground?.let {
                                mediaItemLayout.setBackgroundColor(it)
                            }
                        } else {
                            mediaCheckBox.setChecked(false, false)
                            mediaItemLayout.setBackgroundResource(0)
                        }

                    }
                }
                MediaFilterType.VIDEO -> {
                    val video = MediaItem.valueOf(it, MediaFilterType.VIDEO)
                    with((holder as MediaItemViewHolder).itemView) {
                        nameTextView.text = video.name
                        // display duration text view
                        durationTextView.visibility = View.VISIBLE
                        durationTextView.text = DateUtils.formatElapsedTime(video.duration / 1000)
                        var size = video.size / 1024.0
                        val sizeText: String
                        if (size >= 1024) {
                            size /= 1024
                            sizeText = context.getString(R.string.mb).format(size)
                        } else {
                            sizeText = context.getString(R.string.kb).format(size)
                        }
                        descTextView.text = context.getString(R.string.media_item_desc)
                                .format(DateUtils.formatDateTime(context, video.time * 1000, DateUtils.FORMAT_SHOW_DATE),
                                        sizeText,
                                        MimeTypeMap.getSingleton().getExtensionFromMimeType(video.mimeType).toUpperCase())
                        mContext?.let {
                            SelectionSpec.INSTANCE.imageEngine.loadImage(it, imageView, video.uri)
                        }

                        if (mSelectedItemCollection.isSelected(video)) {
                            mediaCheckBox.setChecked(true, false)
                            mediaItemLayout.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.charles_item_background_selected, null))
                        } else {
                            mediaCheckBox.setChecked(false, false)
                            mediaItemLayout.setBackgroundResource(0)
                        }

                    }
                }
                MediaFilterType.AUDIO -> {
                    val audio = MediaItem.valueOf(it, MediaFilterType.AUDIO)
                    with((holder as MediaItemViewHolder).itemView) {
                        nameTextView.text = audio.name
                        var size = audio.size / 1024.0
                        val sizeText: String
                        if (size >= 1024) {
                            size /= 1024
                            sizeText = context.getString(R.string.mb).format(size)
                        } else {
                            sizeText = context.getString(R.string.kb).format(size)
                        }
                        descTextView.text = context.getString(R.string.media_item_desc)
                                .format(DateUtils.formatDateTime(context, audio.time * 1000, DateUtils.FORMAT_SHOW_DATE),
                                        sizeText,
                                        MimeTypeMap.getSingleton().getExtensionFromMimeType(audio.mimeType).toUpperCase())

                        imageView.setImageResource(R.drawable.ic_root_audio)
                        imageView.setColorFilter(Color.parseColor("#808080"), PorterDuff.Mode.SRC_IN)

                        if (mSelectedItemCollection.isSelected(audio)) {
                            mediaCheckBox.setChecked(true, false)
                            mediaItemLayout.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.charles_item_background_selected, null))
                        } else {
                            mediaCheckBox.setChecked(false, false)
                            mediaItemLayout.setBackgroundResource(0)
                        }
                    }
                }
                MediaFilterType.DOCUMENT -> {
                    val document = MediaItem.valueOf(it, MediaFilterType.DOCUMENT)
                    with((holder as MediaItemViewHolder).itemView) {
                        nameTextView.text = document.name
                        var size = document.size / 1024.0
                        val sizeText: String
                        if (size >= 1024) {
                            size /= 1024
                            sizeText = context.getString(R.string.mb).format(size)
                        } else {
                            sizeText = context.getString(R.string.kb).format(size)
                        }
                        descTextView.text = context.getString(R.string.media_item_desc)
                                .format(DateUtils.formatDateTime(context, document.time * 1000, DateUtils.FORMAT_SHOW_DATE),
                                        sizeText,
                                        MimeTypeMap.getSingleton().getExtensionFromMimeType(document.mimeType).toUpperCase())
                        imageView.setImageResource(R.drawable.ic_root_doc)
                        imageView.setColorFilter(Color.parseColor("#808080"), PorterDuff.Mode.SRC_IN)

                        if (mSelectedItemCollection.isSelected(document)) {
                            mediaCheckBox.setChecked(true, false)
                            selectedBackground?.let {
                                mediaItemLayout.setBackgroundColor(it)
                            }
                        } else {
                            mediaCheckBox.setChecked(false, false)
                            mediaItemLayout.setBackgroundResource(0)
                        }

                    }
                }
                null -> {

                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return MediaItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_media_item, parent, false))
    }

    override fun getItemCount(): Int = mCursor?.count ?: 0

    fun swapCursor(newCursor: Cursor?) {
        mCursor = newCursor
        notifyDataSetChanged()
    }

    fun registerOnMediaClickListener(listener: OnMediaItemClickListener) {
        mMediaItemClickListener = listener
    }

    fun unregisterOnMediaClickListener() {
        mMediaItemClickListener = null
    }

    inner class MediaItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                if (mMediaFilterType != null && mCursor != null) {
                    mCursor?.moveToPosition(layoutPosition)
                    val item = MediaItem.valueOf(mCursor!!, mMediaFilterType)
                    if (mSelectedItemCollection.isSelected(item)) {
                        mSelectedItemCollection.remove(item)

                        // update ui
                        itemView.setBackgroundResource(0)
                        itemView.mediaCheckBox.setChecked(false, true)

                    } else {
                        if (mSelectedItemCollection.count() < SelectionSpec.INSTANCE.maxSelectable) {
                            mSelectedItemCollection.add(item)

                            mContext?.let {
                                val ta = mContext?.obtainStyledAttributes(intArrayOf(R.attr.media_selected_backgroundColor))
                                val color = ta?.getColor(0, 0)
                                ta?.recycle()
                                color?.let {
                                    itemView.setBackgroundColor(it)
                                }
                            }

                            itemView.mediaCheckBox.setChecked(true, true)
                        } else {
                            mContext?.let {
                                Toast.makeText(it, it.getString(R.string.error_over_count), Toast.LENGTH_SHORT).show()
                            }
                        }

                    }
                    mMediaItemClickListener?.onItemClick(item, layoutPosition)
                }
            }
        }

    }

    interface OnMediaItemClickListener {

        fun onItemClick(item: MediaItem, position: Int)

    }

}