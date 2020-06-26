package pt.amn.goalsmaker.models

import java.io.Serializable

class BigGoalModel : Serializable {
    var id : Int = 0
    var title : String = ""
    var description : String = ""
    var imagePath : String = ""
}