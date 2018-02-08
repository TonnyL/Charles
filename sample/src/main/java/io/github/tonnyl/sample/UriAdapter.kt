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

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent?.context
        return UriViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_uri, parent, false))
    }

    override fun getItemCount(): Int = mUris?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        holder?.let {
            with(it as UriViewHolder) {
                mUris?.let {
                    itemView.uriTextView.text = mContext?.getString(R.string.uri, it[position])
                }
                mPaths?.let {
                    itemView.pathTextView.text = mContext?.getString(R.string.path, it[position])
                }
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