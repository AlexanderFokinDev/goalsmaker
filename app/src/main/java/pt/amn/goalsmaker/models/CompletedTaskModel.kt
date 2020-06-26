package pt.amn.goalsmaker.models

import java.io.Serializable

class CompletedTaskModel : Serializable {
    var date : Long = 0
    var quantity : Int = 0
    var taskId : Int = 0
    var taskName : String = ""
    var points : Int = 0
}