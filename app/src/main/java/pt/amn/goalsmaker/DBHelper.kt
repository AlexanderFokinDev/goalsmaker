package pt.amn.goalsmaker

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DBHelper (context : Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("CREATE TABLE " + TABLE_BIG_GOALS + "(" + COLUMN_KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
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
                    do {
                        val goal = BigGoalModel()
                        goal.id = cursor.getInt(indexId)
                        goal.title = cursor.getString(indexTitle)
                        goal.description = cursor.getString(indexDescription)
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
            val success = database.insert(TABLE_BIG_GOALS, null, values)
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

    fun updateTask(tasks: Tasks): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, tasks.name)
        values.put(DESC, tasks.desc)
        values.put(COMPLETED, tasks.completed)
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(tasks.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
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
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "goalsDB"
        private const val TABLE_BIG_GOALS = "big_goals"
        private const val TAG = "DBHelper"
        private const val COLUMN_KEY_ID = "_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
    }
}