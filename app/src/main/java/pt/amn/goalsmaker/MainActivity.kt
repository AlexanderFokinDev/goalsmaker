package pt.amn.goalsmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import pt.amn.goalsmaker.adapters.SQLBigGoalsAdapter
import pt.amn.goalsmaker.databinding.ActivityMainBinding
import pt.amn.goalsmaker.models.BigGoalModel

class MainActivity : AppCompatActivity(), SQLBigGoalsAdapter.SQLBigGoalsAdapterCallback {

    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter : SQLBigGoalsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializationView()
    }

    override fun onResume() {
        super.onResume()
        updateData()
    }

    private fun initializationView() {

        binding.run {
            setSupportActionBar(includedToolbar.toolbar)

            adapter = SQLBigGoalsAdapter(this@MainActivity, this@MainActivity)
            rvBigGoals.setHasFixedSize(true)
            rvBigGoals.adapter = adapter

            fabBigGoals.setOnClickListener {
                startActivity(Intent(this@MainActivity, BigGoalActivity::class.java))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.mainmenu_action_exit -> finish()
            R.id.mainmenu_action_settings ->
                startActivity(Intent(this, SettingsActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateData() {
        adapter.setItems()
        adapter.notifyDataSetChanged()
    }

    override fun onBigGoalClick(bigGoal: BigGoalModel) {
        val intent = Intent(this, BigGoalActivity::class.java)
        intent.putExtra(BigGoalActivity.EXTRA_PARAM_GOAL, bigGoal)
        startActivity(intent)
    }

}
