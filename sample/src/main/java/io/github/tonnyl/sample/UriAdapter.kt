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

package io.github.tonnyl.sample

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_uri.view.*

class UriAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mUris: List<Uri>? = null
    private var mPaths: List<String>? = null
    private var mContext: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return UriViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_uri, parent, false))
    }

    override fun getItemCount(): Int = mUris?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as UriViewHolder) {
            mUris?.let {
                itemView.uriTextView.text = mContext?.getString(R.string.uri, it[position])
            }
            mPaths?.let {
                itemView.pathTextView.text = mContext?.getString(R.string.path, it[position])
            }
        }
    }

    fun setData(uris: List<Uri>?, paths: List<String>?) {
        mUris = uris
        mPaths = paths
        notifyDataSetChanged()
    }

    inner class UriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}