package io.github.tonnyl.charles.ui

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.tonnyl.charles.R
import io.github.tonnyl.charles.internal.loader.*
import io.github.tonnyl.charles.ui.adapter.MediaItemsAdapter
import io.github.tonnyl.charles.internal.data.MediaFilterType
import io.github.tonnyl.charles.internal.data.MediaItem
import kotlinx.android.synthetic.main.fragment_charles.*
import io.github.tonnyl.charles.internal.data.SelectedItemCollection

class CharlesFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private var mFilterType: MediaFilterType? = null
    private lateinit var mMediaItemsAdapter: MediaItemsAdapter
    private lateinit var mSelectionProvider: SelectionProvider

    companion object {

        private const val ARG_TYPE = "ARG_TYPE"

        @JvmStatic
        fun newInstance(filterType: MediaFilterType): CharlesFragment {
            val fragment = CharlesFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARG_TYPE, filterType)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SelectionProvider) {
            mSelectionProvider = context
        } else {
            throw IllegalStateException("Context must implement SelectionProvider.")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_charles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFilterType = arguments?.getSerializable(ARG_TYPE) as MediaFilterType?

        initViews()

        mMediaItemsAdapter.registerOnMediaClickListener(object : MediaItemsAdapter.OnMediaItemClickListener {

            override fun onItemClick(item: MediaItem, position: Int) {
                if (activity != null && activity is CharlesActivity) {
                    (activity as CharlesActivity).updateBottomToolbar()
                }
            }

        })

        activity?.supportLoaderManager?.initLoader(0, null, this)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.supportLoaderManager?.destroyLoader(MediaItemLoader.LOADER_ID)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> = MediaItemLoader.newInstance(context!!, mFilterType!!)

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        mMediaItemsAdapter.swapCursor(data)
        emptyTextView.visibility = if (mMediaItemsAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        mMediaItemsAdapter.swapCursor(null)
    }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        mMediaItemsAdapter = MediaItemsAdapter(mFilterType, mSelectionProvider.provideSelectedItemCollection())
        recyclerView.adapter = mMediaItemsAdapter
    }

    interface SelectionProvider {
        fun provideSelectedItemCollection(): SelectedItemCollection
    }

}