package pt.amn.goalsmaker.models

import java.io.Serializable

class TaskModel : Serializable {
    var id : Int = 0
    var description : String = ""
    var point : Int = 0
    var bigGoalId : Int = 0
}