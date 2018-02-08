package io.github.tonnyl.charles.internal.data

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes

data class Category(
        @DrawableRes val iconId: Int,
        @StringRes val nameId: Int
)