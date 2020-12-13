package pt.amn.goalsmaker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CompletedTaskModel (
    var date : Long = 0,
    var quantity : Int = 0,
    var taskId : Int = 0,
    var taskName : String = "",
    var points : Int = 0
    ) : Parcelable