package pt.amn.goalsmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //private val rvBigGoals : RecyclerView  = findViewById(R.id.rv_big_goals);

    private var mBigGoalsList = listOf<BigGoalModel>()
    private val dbHelper = DBHelper(this)
    private var adapter = SQLBigGoalsAdapter(mBigGoalsList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializationView()
        initializationRecyclerView()
    }

    private fun initializationView() {

        // Action bar
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        //actionBar?.setDisplayHomeAsUpEnabled(true)
        title = "Big goals"

        val mFabButton : FloatingActionButton = findViewById(R.id.fab_big_goals)
        mFabButton.setOnClickListener(this)
    }

    private fun initializationRecyclerView() {

        // TODO
        //val testString = "Daily plan\t\t\t\t\t73%\nWeekly plan\t\t\t\t20%\nMontly plan\t\t\t\t84%"

        /*val testBigGoal = BigGoalModel()
        testBigGoal.title = "Test"
        testBigGoal.description = testString

        val testBigGoal1 = BigGoalModel()
        testBigGoal1.title = "Test1"
        testBigGoal1.description = testString

        mBigGoalsList.add(testBigGoal)
        mBigGoalsList.add(testBigGoal1)*/

        //dbHelper.addBigGoal(testBigGoal)
        //dbHelper.addBigGoal(testBigGoal1)
        //val mBigGoalsList = dbHelper.getAllBigGoals()
        mBigGoalsList = dbHelper.getAllBigGoals()
        adapter = SQLBigGoalsAdapter(mBigGoalsList)

        //////////////////////

        val rvBigGoals : RecyclerView  = findViewById(R.id.rv_big_goals)

        rvBigGoals.setHasFixedSize(true)
        rvBigGoals.layoutManager = LinearLayoutManager(this
                                , LinearLayoutManager.VERTICAL, false)
        rvBigGoals.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.mainmenu_action_exit) {
            finish()
        }
        else if (id == R.id.mainmenu_action_settings) {
            // TODO: Click settings
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.fab_big_goals) {
            val testString = "Daily plan\t\t\t\t\t73%\nWeekly plan\t\t\t\t20%\nMontly plan\t\t\t\t84%"
            val goal = BigGoalModel()
            goal.title = "Test_" + (mBigGoalsList.size + 1).toString()
            goal.description = testString

            //dbHelper.deleteAllBigGoals()

            dbHelper.addBigGoal(goal)
            mBigGoalsList = dbHelper.getAllBigGoals()
            adapter.setItems(mBigGoalsList)
            adapter.notifyDataSetChanged()

            Toast.makeText(this, "Add big goal " + goal.title, Toast.LENGTH_LONG)
                .show()
        }

    }

}
