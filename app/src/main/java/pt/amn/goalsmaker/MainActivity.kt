package pt.amn.goalsmaker

import android.content.Intent
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

class MainActivity : AppCompatActivity(), View.OnClickListener
    , SQLBigGoalsAdapter.SQLBigGoalsAdapterCallback {

    //private val rvBigGoals : RecyclerView  = findViewById(R.id.rv_big_goals);

    private val dbHelper = DBHelper(this)
    private var mBigGoalsList = listOf<BigGoalModel>()
    private var adapter = SQLBigGoalsAdapter(mBigGoalsList, this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializationView()
        initializationRecyclerView()
    }

    override fun onResume() {
        super.onResume()

        refreshRecyclerView()

    }

    private fun initializationView() {

        // Action bar
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        //actionBar?.setDisplayHomeAsUpEnabled(true)

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
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            //finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.fab_big_goals) {
            val intent = Intent(this, BigGoalActivity::class.java)
            startActivity(intent)
        }

    }

    fun refreshRecyclerView() {
        mBigGoalsList = dbHelper.getAllBigGoals()
        adapter.setItems(mBigGoalsList)
        adapter.notifyDataSetChanged()
    }

    override fun onBigGoalClick(pos: Int) {
        var goal = mBigGoalsList.get(pos)

        val intent = Intent(this, BigGoalActivity::class.java)
        intent.putExtra(BigGoalActivity.EXTRA_PARAM_GOAL, goal)
        startActivity(intent)
        //finish()
    }

}
