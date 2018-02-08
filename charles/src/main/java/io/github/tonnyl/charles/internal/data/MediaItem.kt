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

package io.github.tonnyl.charles.internal.data

import android.annotation.SuppressLint
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class MediaItem(

        val id: Long,
        val name: String,
        val mimeType: String,
        val size: Long,
        val time: Long,
        val uri: Uri,
        val duration: Long = 0, // only for video and audio.
        val mediaType: MediaFilterType

) : Parcelable {

    companion object {

        @JvmStatic
        fun valueOf(cursor: Cursor, mediaType: MediaFilterType): MediaItem {
            val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
            return when (mediaType) {
                MediaFilterType.IMAGE -> MediaItem(
                        id,
                        cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED)),
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id),
                        0,
                        mediaType
                )
                MediaFilterType.VIDEO -> MediaItem(
                        id,
                        cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED)),
                        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)),
                        mediaType
                )
                MediaFilterType.AUDIO -> MediaItem(
                        id,
                        cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED)),
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)),
                        mediaType
                )
                MediaFilterType.DOCUMENT -> MediaItem(
                        id,
                        cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE)),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)),
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)),
                        ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), id),
                        0,
                        mediaType
                )
            }
        }

    }

}