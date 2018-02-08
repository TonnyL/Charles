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

package io.github.tonnyl.charles.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ListPopupWindow
import android.view.MenuItem
import io.github.tonnyl.charles.R
import io.github.tonnyl.charles.ui.adapter.CategoriesAdapter
import kotlinx.android.synthetic.main.activity_charles.*
import android.graphics.PorterDuff
import io.github.tonnyl.charles.internal.data.*

/**
 * Created by lizhaotailang on 30/01/2018.
 */
class CharlesActivity : AppCompatActivity(), CharlesFragment.SelectionProvider {

    private lateinit var mAdapter: CategoriesAdapter
    private lateinit var mListPopupWindow: ListPopupWindow
    private val mSelectedCollection = SelectedItemCollection(this)
    private lateinit var mSelectionSpec: SelectionSpec
    private val mCategoryList = listOf(
            Category(R.drawable.ic_root_image, R.string.category_images),
            Category(R.drawable.ic_root_video, R.string.category_videos),
            Category(R.drawable.ic_root_audio, R.string.category_audio),
            Category(R.drawable.ic_root_doc, R.string.category_documents))

    private val mFragments = listOf(
            CharlesFragment.newInstance(MediaFilterType.IMAGE),
            CharlesFragment.newInstance(MediaFilterType.VIDEO),
            CharlesFragment.newInstance(MediaFilterType.AUDIO),
            CharlesFragment.newInstance(MediaFilterType.DOCUMENT))

    companion object {

        const val EXTRA_RESULT_SELECTION = "EXTRA_RESULT_SELECTION"

        const val EXTRA_RESULT_SELECTION_PATH = "EXTRA_RESULT_SELECTION_PATH"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // programmatically set theme before super.onCreate()
        mSelectionSpec = SelectionSpec.INSTANCE
        setTheme(mSelectionSpec.themeId)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_charles)

        if (mSelectionSpec.needOrientationRestriction()) {
            requestedOrientation = mSelectionSpec.orientation.toInt()
        }

        initViews()

        mSelectedCollection.onCreate(savedInstanceState)
        updateBottomToolbar()

        // Display photos fragment default.
        titleTextView.setText(R.string.category_images)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, mFragments[0], CharlesFragment::class.java.simpleName)
                .commitAllowingStateLoss()

        titleTextView.setOnClickListener {
            mListPopupWindow.show()
        }

        applyTextView.setOnClickListener {
            val result = Intent()
            result.putParcelableArrayListExtra(EXTRA_RESULT_SELECTION, ArrayList(mSelectedCollection.asListOfUri()))
            result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, ArrayList(mSelectedCollection.asListOfString()))
            setResult(Activity.RESULT_OK, result)
            finish()
        }

        mListPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val category = mAdapter.getItem(position) as Category
            titleTextView.text = getString(category.nameId)

            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mFragments[position])
                    .commit()

            mListPopupWindow.dismiss()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.let {
            mSelectedCollection.onSaveInstanceState(it)
        }
    }

    override fun provideSelectedItemCollection(): SelectedItemCollection = mSelectedCollection

    private fun initViews() {

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // tint navigation icon
        val navigationIcon = toolbar.navigationIcon
        val ta = theme.obtainStyledAttributes(intArrayOf(R.attr.toolbar_titleColor))
        val color = ta.getColor(0, 0)
        ta.recycle()
        navigationIcon?.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        // tint dropdown arrow icon
        val rightDrawable = titleTextView.compoundDrawables[2]
        rightDrawable?.setColorFilter(color, PorterDuff.Mode.SRC_IN)

        mAdapter = CategoriesAdapter(this@CharlesActivity, mCategoryList)

        mListPopupWindow = ListPopupWindow(this@CharlesActivity)
        mListPopupWindow.isModal = true
        val density = resources.displayMetrics.density
        mListPopupWindow.setContentWidth((160 * density).toInt())
        mListPopupWindow.horizontalOffset = (-8 * density).toInt()
        mListPopupWindow.verticalOffset = (-48 * density).toInt()
        mListPopupWindow.anchorView = titleTextView
        mListPopupWindow.setAdapter(mAdapter)

    }

    fun updateBottomToolbar() {
        progressTextView.text = if (mSelectionSpec.isShowProgressRate) getString(R.string.select_progress_with_max).format(mSelectedCollection.count(), mSelectionSpec.maxSelectable) else getString(R.string.select_progress, mSelectedCollection.count())
        applyTextView.isEnabled = mSelectedCollection.isNotEmpty()
    }

}