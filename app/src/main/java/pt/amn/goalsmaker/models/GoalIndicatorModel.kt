package pt.amn.goalsmaker.models

import java.io.Serializable

class GoalIndicatorModel : Serializable {
    var id : Int = 0
    var description : String = ""
    var done : Byte = 0
    var bigGoalId : Int = 0
}