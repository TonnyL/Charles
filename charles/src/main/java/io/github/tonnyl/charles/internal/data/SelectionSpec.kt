package io.github.tonnyl.charles.internal.data

import io.github.tonnyl.charles.engine.ImageEngine
import android.content.pm.ActivityInfo
import io.github.tonnyl.charles.R
import io.github.tonnyl.charles.engine.impl.GlideEngine

class SelectionSpec private constructor() {

    var orientation = 0
    var isShowProgressRate = false
    var maxSelectable = 0
    var gridExpectedSize = 0
    var themeId = R.style.Charles
    var imageEngine: ImageEngine = GlideEngine()

    private fun reset() {
        orientation = 0
        isShowProgressRate = false
        maxSelectable = 0
        gridExpectedSize = 0
        imageEngine = GlideEngine()
    }

    fun needOrientationRestriction(): Boolean {
        return orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    private object InstanceHolder {
        internal val INSTANCE = SelectionSpec()
    }

    companion object {

        val INSTANCE: SelectionSpec
            get() = InstanceHolder.INSTANCE

        val cleanInstance: SelectionSpec
            get() {
                val selectionSpec = INSTANCE
                selectionSpec.reset()
                return selectionSpec
            }

    }
}