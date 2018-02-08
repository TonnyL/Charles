package io.github.tonnyl.charles

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
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