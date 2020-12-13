package pt.amn.goalsmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import pt.amn.goalsmaker.adapters.TabBigGoalPagerFragmentAdapter
import pt.amn.goalsmaker.databinding.ActivityBigGoalBinding
import pt.amn.goalsmaker.helpers.DBHelper
import pt.amn.goalsmaker.models.BigGoalModel

class BigGoalActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBigGoalBinding
    lateinit var goal : BigGoalModel
    private var isNew = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Some boilerplate code for View Binding
        binding = ActivityBigGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check type of goal - new or existing
        val extraParamGoal = intent.extras?.get(EXTRA_PARAM_GOAL)
        if (extraParamGoal == null) {
            goal = BigGoalModel()
            isNew = true
        } else {
            goal = extraParamGoal as BigGoalModel
            isNew = false
        }

        initializationView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.big_goal_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.big_goal_menu_action_delete ->  {
                val dialogQuestion = AlertDialog.Builder(this)
                dialogQuestion.setTitle(goal.title)
                dialogQuestion.setMessage(getString(R.string.dialog_message_delete_goal))
                dialogQuestion.setPositiveButton("Yes") {dialog, which ->
                    DBHelper(this).deleteBigGoal(goal)
                    finish()
                }
                dialogQuestion.setNeutralButton("Cancel"){_,_ ->}

                val dialog : AlertDialog = dialogQuestion.create()
                dialog.show()
            }
            R.id.big_goal_menu_action_edit -> {}
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initializationView() {

        binding.run {
            bigGoalPager.adapter = TabBigGoalPagerFragmentAdapter(
                this@BigGoalActivity,
                supportFragmentManager, isNew, goal)

            bigGoalTab.setupWithViewPager(bigGoalPager)

            setSupportActionBar(includedToolbar.toolbar)
        }

        actionBar?.setDisplayHomeAsUpEnabled(true)

        title = if (!isNew) {
            getString(R.string.title_big_goal)
        } else {
            getString(R.string.title_create_new_goal)
        }

    }

    companion object {
        const val EXTRA_PARAM_GOAL = "extra_param_goal"
    }

}
