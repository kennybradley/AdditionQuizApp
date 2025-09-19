package additionquiz.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Problem(
    val a: Int,
    val b: Int,
    val answer: Int
) : Parcelable
