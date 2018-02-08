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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import io.github.tonnyl.charles.R
import io.github.tonnyl.charles.internal.data.Category

class CategoriesAdapter(
        private val mContext: Context,
        private val mCategoryList: List<Category>
) : BaseAdapter() {

    override fun getCount(): Int = mCategoryList.size

    override fun getItem(position: Int): Any = mCategoryList[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var conView = convertView
        val viewHolder: ViewHolder
        if (conView == null) {
            viewHolder = ViewHolder()
            conView = LayoutInflater.from(mContext).inflate(R.layout.item_category, null)
            viewHolder.imageView = conView.findViewById(R.id.categoryImageView)
            viewHolder.textView = conView.findViewById(R.id.categoryTextView)
            conView.tag = viewHolder
        } else {
            viewHolder = conView.tag as ViewHolder
        }

        val (iconId, nameId) = mCategoryList[position]
        viewHolder.textView?.setText(nameId)
        viewHolder.imageView?.setImageResource(iconId)

        return conView!!
    }

    class ViewHolder {

        var imageView: ImageView? = null
        var textView: TextView? = null

    }

}
