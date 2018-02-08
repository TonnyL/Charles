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
