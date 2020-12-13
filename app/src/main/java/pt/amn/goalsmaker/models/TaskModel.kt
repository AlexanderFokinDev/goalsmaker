package pt.amn.goalsmaker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskModel (
    var id : Int = 0,
    var description : String = "",
    var point : Int = 0,
    var bigGoalId : Int = 0
) : Parcelable