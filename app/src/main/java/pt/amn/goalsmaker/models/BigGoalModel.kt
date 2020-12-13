package pt.amn.goalsmaker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BigGoalModel (
    var id : Int = 0,
    var title : String = "",
    var description : String = "",
    var imagePath : String = ""
) : Parcelable