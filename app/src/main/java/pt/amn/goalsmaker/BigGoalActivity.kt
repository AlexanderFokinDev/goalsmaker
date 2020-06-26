package pt.amn.goalsmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pt.amn.goalsmaker.adapters.TabBigGoalPagerFragmentAdapter
import pt.amn.goalsmaker.models.BigGoalModel

class BigGoalActivity : AppCompatActivity()
         {

    lateinit var goal : BigGoalModel
    private val dbHelper = DBHelper(this)
    private var isNew = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_goal)

        val extraParamGoal = intent.extras?.get(EXTRA_PARAM_GOAL)
        if (extraParamGoal == null) {
            goal = BigGoalModel()
            isNew = true
        } else {
            goal = extraParamGoal as BigGoalModel
            isNew = false
        }

        initializationTabs()
        initializationView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.big_goal_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.big_goal_menu_action_edit) {
            /*etTitle.isEnabled = true
            etDescription.isEnabled = true
            ivBigGoal.isEnabled = true
            btSave.visibility = View.VISIBLE
            etIndicator.visibility = View.VISIBLE
            btAdd.visibility = View.VISIBLE*/
        } else if (id == R.id.big_goal_menu_action_delete) {

            val dialogQuestion = AlertDialog.Builder(this)
            dialogQuestion.setTitle(goal.title)
            dialogQuestion.setMessage(getString(R.string.dialog_message_delete_goal))
            dialogQuestion.setPositiveButton("Yes") {dialog, which ->
                dbHelper.deleteBigGoal(goal)
                finish()
            }
            dialogQuestion.setNeutralButton("Cancel"){_,_ ->}

            val dialog : AlertDialog = dialogQuestion.create()
            dialog.show()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initializationView() {
        // Action bar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        if (!isNew) {
            title = getString(R.string.title_big_goal) // goal.title
        } else {
            title = getString(R.string.title_create_new_goal)
        }
    }

    private fun initializationTabs() {

        val pager : ViewPager = findViewById(R.id.big_goal_pager)
        val tabsAdapter = TabBigGoalPagerFragmentAdapter(this, supportFragmentManager
            , isNew, goal)
        pager.adapter = tabsAdapter

        val tabBigGoal : TabLayout = findViewById(R.id.big_goal_tab)
        tabBigGoal.setupWithViewPager(pager)

    }

    // Constants

    companion object {
        const val EXTRA_PARAM_GOAL = "extra_param_goal"
    }

}
