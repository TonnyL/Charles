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

package io.github.tonnyl.charles

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import io.github.tonnyl.charles.ui.CharlesActivity
import java.lang.ref.WeakReference


class Charles {

    private var mContext: WeakReference<Activity>? = null
    private var mFragment: WeakReference<Fragment>? = null

    private constructor(fragment: Fragment) : this(fragment.activity, fragment)

    private constructor(activity: Activity) : this(activity, null)

    private constructor(activity: Activity?, fragment: Fragment?) {
        mContext = if (activity != null) WeakReference(activity) else null
        mFragment = if (fragment != null) WeakReference(fragment) else null
    }

    companion object {

        @JvmStatic
        fun from(activity: Activity) = Charles(activity)

        @JvmStatic
        fun from(fragment: Fragment) = Charles(fragment)

        @JvmStatic
        fun obtainResult(data: Intent?) = data?.getParcelableArrayListExtra<Uri>(CharlesActivity.EXTRA_RESULT_SELECTION)

        @JvmStatic
        fun obtainPathResult(data: Intent?) = data?.getStringArrayListExtra(CharlesActivity.EXTRA_RESULT_SELECTION_PATH)

    }

    fun getActivity() = mContext?.get()

    fun getFragment() = mFragment?.get()

    fun choose(): SelectionCreator = SelectionCreator(this)

}