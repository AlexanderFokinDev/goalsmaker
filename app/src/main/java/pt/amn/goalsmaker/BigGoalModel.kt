package pt.amn.goalsmaker

val BIG_GOAL_ID = "big_goal_id"

class BigGoalModel(param_id: Int, param_title: String, param_description: String) {

    var id = param_id;
    var title = param_title
    var description = param_description
}