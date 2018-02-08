package io.github.tonnyl.charles.internal.data

import android.content.Context
import android.net.Uri
import android.os.Bundle
import io.github.tonnyl.charles.utils.PathUtils

class SelectedItemCollection(context: Context) {

    private val mContext = context

    private lateinit var mSelectedItems: LinkedHashSet<MediaItem>

    companion object {

        const val STATE_SELECTION = "STATE_SELECTION"

    }

    fun onCreate(bundle: Bundle?) {
        mSelectedItems = linkedSetOf()
        bundle?.let {
            mSelectedItems.addAll(it.getParcelableArrayList(STATE_SELECTION))
        }
    }

    fun setDefaultSelection(items: List<MediaItem>) {
        mSelectedItems.addAll(items)
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(STATE_SELECTION, ArrayList(mSelectedItems))
    }

    fun getDataWithBundle(): Bundle {
        val bundle = Bundle()
        bundle.putParcelableArrayList(STATE_SELECTION, ArrayList(mSelectedItems))
        return bundle
    }

    fun add(item: MediaItem) {
        mSelectedItems.add(item)
    }

    fun remove(item: MediaItem): Boolean = mSelectedItems.remove(item)

    fun overwrite(items: List<MediaItem>) {
        mSelectedItems.clear()
        mSelectedItems.addAll(items)
    }

    fun asList(): List<MediaItem> = ArrayList(mSelectedItems)

    fun asListOfUri(): List<Uri> = mSelectedItems.map { it.uri }

    fun asListOfString(): List<String> {
        val paths = mutableListOf<String>()
        mSelectedItems.forEach {
            val s = PathUtils.getPath(mContext, it.uri)
            if (s != null) {
                paths.add(s)
            }
        }
        return paths
    }

    fun isEmpty(): Boolean = mSelectedItems.isEmpty()

    fun isNotEmpty(): Boolean = mSelectedItems.isNotEmpty()

    fun isSelected(item: MediaItem): Boolean = mSelectedItems.contains(item)

    fun currentMaxSelectable(): Boolean = SelectionSpec.INSTANCE.maxSelectable == mSelectedItems.size

    fun count(): Int = mSelectedItems.size

}