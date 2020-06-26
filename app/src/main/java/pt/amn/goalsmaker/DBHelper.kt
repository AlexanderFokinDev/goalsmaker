package pt.amn.goalsmaker

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pt.amn.goalsmaker.models.BigGoalModel
import pt.amn.goalsmaker.models.CompletedTaskModel
import pt.amn.goalsmaker.models.GoalIndicatorModel
import pt.amn.goalsmaker.models.TaskModel

class DBHelper (context : Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE " + TABLE_BIG_GOALS + "(" + COLUMN_KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_IMAGE_PATH + " TEXT)")

        db?.execSQL("CREATE TABLE " + TABLE_GOAL_INDICATORS + "(" + COLUMN_KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_DONE + " TINYINT,"
                + COLUMN_BIG_GOAL_ID + " INTEGER)")

        db?.execSQL("CREATE TABLE " + TABLE_GOAL_TASKS + "(" + COLUMN_KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_POINT + " INTEGER,"
                + COLUMN_BIG_GOAL_ID + " INTEGER)")

        db?.execSQL("CREATE TABLE " + TABLE_COMPLETED_TASKS + "("
                + COLUMN_DATE + " INTEGER,"
                + COLUMN_QUANTITY + " INTEGER,"
                + COLUMN_TASK_ID + " INTEGER)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        if (oldVersion == 1) {
            db?.execSQL("CREATE TABLE " + TABLE_GOAL_INDICATORS + "(" + COLUMN_KEY_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_DONE + " TINYINT)")

        } else if (oldVersion == 2) {
            db?.execSQL("ALTER TABLE " + TABLE_GOAL_INDICATORS + " ADD "
                    + COLUMN_BIG_GOAL_ID + " INTEGER")

        } else if (oldVersion == 3) {
            db?.execSQL("CREATE TABLE " + TABLE_GOAL_TASKS + "(" + COLUMN_KEY_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_POINT + " INTEGER,"
                    + COLUMN_BIG_GOAL_ID + " INTEGER)")

        } else if (oldVersion == 4) {
            db?.execSQL("CREATE TABLE " + TABLE_COMPLETED_TASKS + "("
                    + COLUMN_DATE + " INTEGER,"
                    + COLUMN_QUANTITY + " INTEGER,"
                    + COLUMN_TASK_ID + " INTEGER)")
        }
    }

    // ********************************************************************************************
    // TABLE_BIG_GOALS
    // ********************************************************************************************

    fun getAllBigGoals() : List<BigGoalModel> {

        var bigGoalsList = ArrayList<BigGoalModel>()

        try {

            val database = readableDatabase
            val cursor = database.query(TABLE_BIG_GOALS,
                null, null, null, null, null, null)

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val indexId = cursor.getColumnIndex(COLUMN_KEY_ID)
                    val indexTitle = cursor.getColumnIndex(COLUMN_TITLE)
                    val indexDescription = cursor.getColumnIndex(COLUMN_DESCRIPTION)
                    val indexImagePath = cursor.getColumnIndex(COLUMN_IMAGE_PATH)
                    do {
                        val goal = BigGoalModel()
                        goal.id = cursor.getInt(indexId)
                        goal.title = cursor.getString(indexTitle)
                        goal.description = cursor.getString(indexDescription)
                        goal.imagePath = cursor.getString(indexImagePath)
                        bigGoalsList.add(goal)

                    } while (cursor.moveToNext())
                }
                cursor.close()
                database.close()
            }

        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return bigGoalsList;

    }

    fun addBigGoal(goal : BigGoalModel) : Boolean {

        try {
            val database = writableDatabase
            val values = ContentValues()
            values.put(COLUMN_TITLE, goal.title)
            values.put(COLUMN_DESCRIPTION, goal.description)
            values.put(COLUMN_IMAGE_PATH, goal.imagePath)
            val success = database.insert(TABLE_BIG_GOALS, null, values)
            database.close()
            if (success.toInt() != -1) {
                goal.id = success.toInt()
                return true
            } else {
                return false
            }

        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun deleteBigGoal(goal : BigGoalModel) : Boolean {

        // Delete all related rows
        deleteGoalIndicatorsByGoalId(goal)

        val tasks = getGoalTasks(goal)
        for (task in tasks) {
            deleteCompletedGoalTasksById(task.id)
            deleteGoalTask(task)
        }


        try {
            val database = writableDatabase
            val success = database.delete(TABLE_BIG_GOALS,
                COLUMN_KEY_ID + "=?",
                arrayOf(goal.id.toString()))

            database.close()
            return (success != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun deleteAllBigGoals() : Boolean {

        try {
            val database = writableDatabase
            val success = database.delete(TABLE_BIG_GOALS, null, null)
            database.close()
            return (success.toInt() != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun updateBigGoal(goal : BigGoalModel): Boolean {

        try {
            val database = writableDatabase
            val values = ContentValues()
            values.put(COLUMN_TITLE, goal.title)
            values.put(COLUMN_DESCRIPTION, goal.description)
            values.put(COLUMN_IMAGE_PATH, goal.imagePath)
            val success = database.update(TABLE_BIG_GOALS, values,
                COLUMN_KEY_ID + "=?", arrayOf(goal.id.toString()))
            database.close()
            return (success != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    /*
    fun getTask(_id: Int): Tasks {
        val tasks = Tasks()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        tasks.name = cursor.getString(cursor.getColumnIndex(NAME))
        tasks.desc = cursor.getString(cursor.getColumnIndex(DESC))
        tasks.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
        cursor.close()
        return tasks
    }
     */

    // ********************************************************************************************


    // ********************************************************************************************
    // TABLE_GOAL_INDICATORS
    // ********************************************************************************************

    fun getGoalIndicators(goal: BigGoalModel) : List<GoalIndicatorModel> {

        var goalIndicatorsList = ArrayList<GoalIndicatorModel>()

        try {

            val database = readableDatabase
            val cursor = database.query(
                TABLE_GOAL_INDICATORS,
                null, COLUMN_BIG_GOAL_ID + "=?", arrayOf(goal.id.toString())
                , null, null, COLUMN_KEY_ID)

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val indexId = cursor.getColumnIndex(COLUMN_KEY_ID)
                    val indexDescription = cursor.getColumnIndex(COLUMN_DESCRIPTION)
                    val indexDone = cursor.getColumnIndex(COLUMN_DONE)
                    val indexGoalId = cursor.getColumnIndex(COLUMN_BIG_GOAL_ID)
                    do {
                        val goalIndicator =
                            GoalIndicatorModel()
                        goalIndicator.id = cursor.getInt(indexId)
                        goalIndicator.description = cursor.getString(indexDescription)
                        goalIndicator.done = cursor.getShort(indexDone).toByte()
                        goalIndicator.bigGoalId = cursor.getInt(indexGoalId)
                        goalIndicatorsList.add(goalIndicator)

                    } while (cursor.moveToNext())
                }
                cursor.close()
                database.close()
            }

        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return goalIndicatorsList;

    }

    fun addGoalIndicator(goalIndicator : GoalIndicatorModel) : Boolean {

        try {
            val database = writableDatabase
            val values = ContentValues()
            values.put(COLUMN_DESCRIPTION, goalIndicator.description)
            values.put(COLUMN_DONE, goalIndicator.done)
            values.put(COLUMN_BIG_GOAL_ID, goalIndicator.bigGoalId)
            val success = database.insert(TABLE_GOAL_INDICATORS, null, values)
            database.close()
            return (success.toInt() != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun deleteGoalIndicator(goalIndicator : GoalIndicatorModel) : Boolean {

        try {
            val database = writableDatabase
            val success = database.delete(
                TABLE_GOAL_INDICATORS,
                COLUMN_KEY_ID + "=?",
                arrayOf(goalIndicator.id.toString()))

            database.close()
            return (success != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun deleteGoalIndicatorsByGoalId(goal: BigGoalModel) : Boolean {

        try {
            val database = writableDatabase
            val success = database.delete(TABLE_GOAL_INDICATORS,
                COLUMN_BIG_GOAL_ID + "=?",
                arrayOf(goal.id.toString()))
            database.close()
            return (success != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun updateGoalIndicator(goalIndicator : GoalIndicatorModel): Boolean {

        try {
            val database = writableDatabase
            val values = ContentValues()
            values.put(COLUMN_DESCRIPTION, goalIndicator.description)
            values.put(COLUMN_DONE, goalIndicator.done)
            values.put(COLUMN_BIG_GOAL_ID, goalIndicator.bigGoalId)
            val success = database.update(
                TABLE_GOAL_INDICATORS, values,
                COLUMN_KEY_ID + "=?", arrayOf(goalIndicator.id.toString()))
            database.close()
            return (success != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    // ********************************************************************************************


    // ********************************************************************************************
    // TABLE_GOAL_TASKS
    // ********************************************************************************************

    fun getGoalTasks(goal: BigGoalModel) : List<TaskModel> {

        var taskList = ArrayList<TaskModel>()

        try {

            val database = readableDatabase
            val cursor = database.query(
                TABLE_GOAL_TASKS,
                null, COLUMN_BIG_GOAL_ID + "=?", arrayOf(goal.id.toString())
                , null, null, COLUMN_KEY_ID)

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val indexId = cursor.getColumnIndex(COLUMN_KEY_ID)
                    val indexDescription = cursor.getColumnIndex(COLUMN_DESCRIPTION)
                    val indexPoint = cursor.getColumnIndex(COLUMN_POINT)
                    val indexGoalId = cursor.getColumnIndex(COLUMN_BIG_GOAL_ID)
                    do {
                        val task = TaskModel()
                        task.id = cursor.getInt(indexId)
                        task.description = cursor.getString(indexDescription)
                        task.point = cursor.getInt(indexPoint)
                        task.bigGoalId = cursor.getInt(indexGoalId)
                        taskList.add(task)

                    } while (cursor.moveToNext())
                }
                cursor.close()
                database.close()
            }

        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return taskList

    }

    fun addGoalTask(task : TaskModel) : Boolean {

        try {
            val database = writableDatabase
            val values = ContentValues()
            values.put(COLUMN_DESCRIPTION, task.description)
            values.put(COLUMN_POINT, task.point)
            values.put(COLUMN_BIG_GOAL_ID, task.bigGoalId)
            val success = database.insert(TABLE_GOAL_TASKS, null, values)
            database.close()
            return (success.toInt() != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun deleteGoalTask(task : TaskModel) : Boolean {

        try {
            val database = writableDatabase
            val success = database.delete(
                TABLE_GOAL_TASKS,
                COLUMN_KEY_ID + "=?",
                arrayOf(task.id.toString()))

            database.close()
            return (success != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun deleteGoalTasksByGoalId(goal: BigGoalModel) : Boolean {

        try {
            val database = writableDatabase
            val success = database.delete(
                TABLE_GOAL_TASKS,
                COLUMN_BIG_GOAL_ID + "=?",
                arrayOf(goal.id.toString()))
            database.close()
            return (success != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun updateGoalTask(task : TaskModel): Boolean {

        try {
            val database = writableDatabase
            val values = ContentValues()
            values.put(COLUMN_DESCRIPTION, task.description)
            values.put(COLUMN_POINT, task.point)
            values.put(COLUMN_BIG_GOAL_ID, task.bigGoalId)
            val success = database.update(
                TABLE_GOAL_TASKS, values,
                COLUMN_KEY_ID + "=?", arrayOf(task.id.toString()))
            database.close()
            return (success != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    // ********************************************************************************************


    // ********************************************************************************************
    // TABLE_COMPLETED_TASKS
    // ********************************************************************************************

    fun getCompletedTasksOfDay(goal: BigGoalModel, date: Long) : List<CompletedTaskModel> {

        var taskList = ArrayList<CompletedTaskModel>()

        try {

            val database = readableDatabase

            val sqlQuery = ("""SELECT
            nestedTable.description,
            nestedTable._id,
            nestedTable.point,
            SUM(nestedTable.quantity) AS quantity
            FROM (
                SELECT
                    goal_tasks.description,
                    goal_tasks._id,
                    goal_tasks.point,
                    0 AS quantity
                FROM goal_tasks
                WHERE goal_tasks.big_goal_id = ?

                UNION ALL

                SELECT
                goal_tasks.description,
                goal_tasks._id,
                goal_tasks.point,
                completed_tasks.quantity

                FROM goal_tasks
                LEFT JOIN completed_tasks AS completed_tasks
                    ON goal_tasks._id = completed_tasks.task_id

                WHERE goal_tasks.big_goal_id = ? AND completed_tasks.date = ?) AS nestedTable

            GROUP BY
                nestedTable.description,
                nestedTable._id,
                nestedTable.point""")


            val cursor = database.rawQuery(sqlQuery, arrayOf(goal.id.toString(),
                goal.id.toString(), date.toString()))

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val indexId = cursor.getColumnIndex(COLUMN_KEY_ID)
                    val indexDescription = cursor.getColumnIndex(COLUMN_DESCRIPTION)
                    val indexQuantity = cursor.getColumnIndex(COLUMN_QUANTITY)
                    val indexPoint = cursor.getColumnIndex(COLUMN_POINT)
                    do {
                        val task = CompletedTaskModel()
                        task.date = date
                        task.taskId = cursor.getInt(indexId)
                        task.taskName = cursor.getString(indexDescription)
                        task.quantity = cursor.getInt(indexQuantity)
                        task.points = task.quantity * cursor.getInt(indexPoint)
                        taskList.add(task)

                    } while (cursor.moveToNext())
                }
                cursor.close()
                database.close()
            }

        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return taskList

    }

    fun getRowCompletedTaskOfDay(task: CompletedTaskModel, date: Long) : Boolean {

        try {

            val database = readableDatabase

            val cursor = database.query(
                TABLE_COMPLETED_TASKS,
                null, COLUMN_TASK_ID + "=? AND " + COLUMN_DATE + "=?"
                , arrayOf(task.taskId.toString(), date.toString())
                , null, null, COLUMN_TASK_ID)

            return cursor != null && cursor.count > 0

        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false

    }

    fun addCompletedTask(task : CompletedTaskModel, date: Long) : Boolean {

        try {
            val database = writableDatabase
            val values = ContentValues()
            values.put(COLUMN_DATE, date)
            values.put(COLUMN_QUANTITY, task.quantity)
            values.put(COLUMN_TASK_ID, task.taskId)
            val success = database.insert(TABLE_COMPLETED_TASKS, null, values)
            database.close()
            return (success.toInt() != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    /*fun deleteGoalTask(task : TaskModel) : Boolean {

        try {
            val database = writableDatabase
            val success = database.delete(
                TABLE_GOAL_TASKS,
                COLUMN_KEY_ID + "=?",
                arrayOf(task.id.toString()))

            database.close()
            return (success != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }*/

    fun deleteCompletedGoalTasksById(id: Int) : Boolean {

        try {
            val database = writableDatabase
            val success = database.delete(
                TABLE_COMPLETED_TASKS,
                COLUMN_TASK_ID + "=?",
                arrayOf(id.toString()))
            database.close()
            return (success != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun updateCompletedTask(task : CompletedTaskModel, date: Long): Boolean {

        // New row
        if (!getRowCompletedTaskOfDay(task, date))
        {
            addCompletedTask(task, date)
            return true
        }

        try {
            val database = writableDatabase
            val values = ContentValues()
            values.put(COLUMN_DATE, date)
            values.put(COLUMN_QUANTITY, task.quantity)
            values.put(COLUMN_TASK_ID, task.taskId)
            val success = database.update(
                TABLE_COMPLETED_TASKS, values,
                COLUMN_TASK_ID + "=? AND " + COLUMN_DATE + "=?"
                , arrayOf(task.taskId.toString(), date.toString()))
            database.close()
            return (success != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    // ********************************************************************************************


    companion object {
        // By changing the version number, DBHelper will understand that you need to update the database structure.
        private const val DATABASE_VERSION = 5
        private const val DATABASE_NAME = "goalsDB"
        private const val TAG = "DBHelper"

        private const val TABLE_BIG_GOALS = "big_goals"
        private const val TABLE_GOAL_INDICATORS = "goal_indicators"
        private const val TABLE_GOAL_TASKS = "goal_tasks"
        private const val TABLE_COMPLETED_TASKS = "completed_tasks"

        private const val COLUMN_KEY_ID = "_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE_PATH = "image_path"
        private const val COLUMN_DONE = "done"
        private const val COLUMN_BIG_GOAL_ID = "big_goal_id"
        private const val COLUMN_POINT = "point"
        private const val COLUMN_TASK_ID = "task_id"
        private const val COLUMN_QUANTITY = "quantity"
        private const val COLUMN_DATE = "date"
    }
}