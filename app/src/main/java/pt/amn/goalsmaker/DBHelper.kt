package pt.amn.goalsmaker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.ID
import android.util.Log
import android.widget.Toast

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
                + COLUMN_DONE + " TINYINT)")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        if (oldVersion == 1) {
            db?.execSQL("CREATE TABLE " + TABLE_GOAL_INDICATORS + "(" + COLUMN_KEY_ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_DONE + " TINYINT)")
            /*db?.execSQL("ALTER TABLE " + TABLE_BIG_GOALS + " ADD "
                    + COLUMN_IMAGE_PATH + " TEXT");*/
        }
    }

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
            return (success.toInt() != -1)
        } catch (e : SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun deleteBigGoal(goal : BigGoalModel) : Boolean {

        try {
            val database = writableDatabase
            val success = database.delete(TABLE_BIG_GOALS,
                COLUMN_KEY_ID + "=?",
                arrayOf(goal.id.toString()))

            database.close()
            return (success.toInt() != -1)
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
            return (success.toInt() != -1)
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

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }
     */

    companion object {
        // By changing the version number, DBHelper will understand that you need to update the database structure.
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "goalsDB"
        private const val TAG = "DBHelper"

        private const val TABLE_BIG_GOALS = "big_goals"
        private const val TABLE_GOAL_INDICATORS = "goal_indicators"

        private const val COLUMN_KEY_ID = "_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE_PATH = "image_path"
        private const val COLUMN_DONE = "done"
    }
}