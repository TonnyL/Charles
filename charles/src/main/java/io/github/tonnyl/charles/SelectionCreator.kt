package io.github.tonnyl.charles

import io.github.tonnyl.charles.internal.data.SelectionSpec
import io.github.tonnyl.charles.engine.ImageEngine
import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.annotation.StyleRes
import io.github.tonnyl.charles.ui.CharlesActivity

class SelectionCreator(private val mCharles: Charles) {

    private val mSelectionSpec = SelectionSpec.cleanInstance

    init {
        mSelectionSpec.orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    fun maxSelectable(maxSelectable: Int): SelectionCreator {
        if (maxSelectable < 1) {
            throw IllegalArgumentException("maxSelectable must be greater than or equal to one")
        }
        if (mSelectionSpec.maxSelectable > 0) {
            throw  IllegalStateException("already set maxSelectable")
        }
        mSelectionSpec.maxSelectable = maxSelectable
        return this
    }

    fun progressRate(isShow: Boolean): SelectionCreator {
        mSelectionSpec.isShowProgressRate = isShow
        return this
    }

    fun theme(@StyleRes themeId: Int): SelectionCreator {
        mSelectionSpec.themeId = themeId
        return this
    }

    fun imageEngine(imageEngine: ImageEngine): SelectionCreator {
        mSelectionSpec.imageEngine = imageEngine
        return this
    }

    fun restrictOrientation(screenOrientation: Int): SelectionCreator {
        mSelectionSpec.orientation = screenOrientation
        return this
    }

    /**
     * Start to select media and wait for result.
     *
     * @param requestCode Identity of the request Activity or Fragment.
     */
    fun forResult(requestCode: Int) {
        val activity = mCharles.getActivity()

        val intent = Intent(activity, CharlesActivity::class.java)

        val fragment = mCharles.getFragment()
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode)
        } else {
            activity?.startActivityForResult(intent, requestCode)
        }
    }

}