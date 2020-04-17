package pt.amn.goalsmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    //private val rvBigGoals : RecyclerView  = findViewById(R.id.rv_big_goals);

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
    }

    private fun initializationRecyclerView() {

        val mBigGoalsList = ArrayList<BigGoalModel>()

        val testString = "Daily plan\t\t\t\t\t73%\nWeekly plan\t\t\t\t20%\nMontly plan\t\t\t\t84%"

        // TODO
        val testBigGoal : BigGoalModel = BigGoalModel(1,
            "Test", testString)

        val testBigGoal1 : BigGoalModel = BigGoalModel(2,
            "Test1", testString)

        mBigGoalsList.add(testBigGoal)
        mBigGoalsList.add(testBigGoal1)

        val rvBigGoals : RecyclerView  = findViewById(R.id.rv_big_goals)

        rvBigGoals.setHasFixedSize(true)
        rvBigGoals.layoutManager = LinearLayoutManager(this
                                , LinearLayoutManager.VERTICAL, false)
        rvBigGoals.adapter = SQLBigGoalsAdapter(mBigGoalsList)


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
            Toast.makeText(this, "Click settings", Toast.LENGTH_LONG)
                .show()
        }

        return super.onOptionsItemSelected(item)
    }


}
