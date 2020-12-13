package pt.amn.goalsmaker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GoalIndicatorModel (
    var id : Int = 0,
    var description : String = "",
    var done : Byte = 0,
    var bigGoalId : Int = 0
) : Parcelable