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

package io.github.tonnyl.charles.internal.loader

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.CursorLoader
import android.webkit.MimeTypeMap
import io.github.tonnyl.charles.internal.data.MediaFilterType

class MediaItemLoader private constructor(

        context: Context,
        uri: Uri,
        projection: Array<String>,
        selection: String,
        selectionArgs: Array<String>,
        sortOrder: String

) : CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder) {

    companion object {

        const val LOADER_ID = 0

        // Images
        private const val IMAGES_SELECTION = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? AND ${MediaStore.MediaColumns.SIZE}>0"
        private val IMAGE_SELECTION_ARGS = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
        private const val IMAGES_ORDER_BY = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        private val IMAGE_QUERY_URI = MediaStore.Files.getContentUri("external")
        private val IMAGES_PROJECTION = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.MediaColumns.TITLE,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.SIZE,
                MediaStore.MediaColumns.DATE_ADDED,
                "duration"
        )

        // Videos
        private const val VIDEOS_SELECTION = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? AND ${MediaStore.MediaColumns.SIZE}>0"
        private val VIDEOS_SELECTION_ARGS = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
        private const val VIDEOS_ORDER_BY = "${MediaStore.Video.Media.DATE_TAKEN} DESC"
        private val VIDEOS_QUERY_URI = MediaStore.Files.getContentUri("external")
        private val VIDEOS_PROJECTION = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.MediaColumns.TITLE,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.SIZE,
                MediaStore.MediaColumns.DATE_ADDED,
                "duration"
        )

        // Audio
        private const val AUDIO_SELECTION = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? AND ${MediaStore.MediaColumns.SIZE}>0"
        private val AUDIO_SELECTION_ARGS = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString())
        private const val AUDIO_ORDER_BY = "${MediaStore.Audio.AudioColumns.DATE_ADDED} DESC"
        private val AUDIO_QUERY_URI = MediaStore.Files.getContentUri("external")
        private val AUDIO_PROJECTION = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.MediaColumns.TITLE,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.SIZE,
                MediaStore.Audio.AudioColumns.DATE_ADDED,
                "duration"
        )

        // Documents
        private val DOCUMENTS_SELECTION = """
            |${MediaStore.Files.FileColumns.MIME_TYPE}=?
            |OR ${MediaStore.Files.FileColumns.MIME_TYPE}=?
            |OR ${MediaStore.Files.FileColumns.MIME_TYPE}=?
            |OR ${MediaStore.Files.FileColumns.MIME_TYPE}=?
            |OR ${MediaStore.Files.FileColumns.MIME_TYPE}=?
            |OR ${MediaStore.Files.FileColumns.MIME_TYPE}=?
            |OR ${MediaStore.Files.FileColumns.MIME_TYPE}=?
            |OR ${MediaStore.Files.FileColumns.MIME_TYPE}=?
            |AND ${MediaStore.MediaColumns.SIZE}>0
            """.trimMargin()
        private val DOCUMENTS_SELECTION_ARGS = arrayOf(
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("TXT"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("HTM"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("HTML"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("PDF"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("DOC"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("XLS"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("PPT"),
                MimeTypeMap.getSingleton().getMimeTypeFromExtension("ZIP"))
        private const val DOCUMENTS_ORDER_BY = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
        private val DOCUMENTS_QUERY_URI = MediaStore.Files.getContentUri("external")
        private val DOCUMENTS_PROJECTION = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.MediaColumns.TITLE,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_ADDED
        )

        @JvmStatic
        fun newInstance(context: Context, mediaFilterType: MediaFilterType): MediaItemLoader = when (mediaFilterType) {
            MediaFilterType.IMAGE -> {
                MediaItemLoader(context, IMAGE_QUERY_URI, IMAGES_PROJECTION, IMAGES_SELECTION, IMAGE_SELECTION_ARGS, IMAGES_ORDER_BY)
            }
            MediaFilterType.VIDEO -> {
                MediaItemLoader(context, VIDEOS_QUERY_URI, VIDEOS_PROJECTION, VIDEOS_SELECTION, VIDEOS_SELECTION_ARGS, VIDEOS_ORDER_BY)
            }
            MediaFilterType.AUDIO -> {
                MediaItemLoader(context, AUDIO_QUERY_URI, AUDIO_PROJECTION, AUDIO_SELECTION, AUDIO_SELECTION_ARGS, AUDIO_ORDER_BY)
            }
            MediaFilterType.DOCUMENT -> {
                MediaItemLoader(context, DOCUMENTS_QUERY_URI, DOCUMENTS_PROJECTION, DOCUMENTS_SELECTION, DOCUMENTS_SELECTION_ARGS, DOCUMENTS_ORDER_BY)
            }
        }

    }

}