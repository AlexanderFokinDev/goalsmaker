package pt.amn.goalsmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_big_goal_tasks.*
import pt.amn.goalsmaker.DBHelper
import pt.amn.goalsmaker.models.BigGoalModel
import pt.amn.goalsmaker.R
import pt.amn.goalsmaker.adapters.SQLGoalTasksAdapter
import pt.amn.goalsmaker.models.TaskModel

class BigGoalTasksFragment(var isNew : Boolean, val goal : BigGoalModel) : Fragment()
        , View.OnClickListener
        , SQLGoalTasksAdapter.SQLGoalTasksAdapterCallback {

    lateinit var dbHelper : DBHelper
    lateinit var mGoalTasksList : List<TaskModel>
    lateinit var adapter : SQLGoalTasksAdapter
    lateinit var mGoalTasksListForNewGoal : ArrayList<TaskModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_big_goal_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(context!!)
        mGoalTasksList = listOf<TaskModel>()
        adapter = SQLGoalTasksAdapter(mGoalTasksList, this, requireContext())

        initializationView()
        initializationRecyclerView()

    }

    override fun onResume() {
        super.onResume()
        refreshRecyclerView()
    }

    private fun initializationView() {

        btAddTask.setOnClickListener(this)

        if (isNew) {
            mGoalTasksListForNewGoal = ArrayList<TaskModel>()
        }
    }

    private fun initializationRecyclerView() {
        rvTasks.layoutManager = LinearLayoutManager(context
            , LinearLayoutManager.VERTICAL, false)
        rvTasks.adapter = adapter
    }

    private fun refreshRecyclerView() {

        if (!isNew) {
            mGoalTasksList = dbHelper.getGoalTasks(goal)
            adapter.setItems(mGoalTasksList)
        } else {
            adapter.setItems(mGoalTasksListForNewGoal)
        }

        adapter.notifyDataSetChanged()

    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.btAddTask) {

            if(etTask.text.isEmpty()) {
                Toast.makeText(context, getString(R.string.error_message_empty_task)
                    , Toast.LENGTH_SHORT).show()
                return
            }

            val newTask = TaskModel()
            newTask.description = etTask.text.toString()
            newTask.point = Integer.parseInt(etPoint.text.toString())

            if (isNew) {
                newTask.bigGoalId = 0
                mGoalTasksListForNewGoal.add(newTask)
            } else {
                newTask.bigGoalId = goal.id
                dbHelper?.addGoalTask(newTask)
            }

            etTask.text.clear()
            etPoint.text.clear()

            refreshRecyclerView()

        }

    }

    override fun onDeleteClick(pos: Int) {

        if (isNew) {
            val task = mGoalTasksListForNewGoal[pos]
            mGoalTasksListForNewGoal.remove(task)
        } else {
            val task = mGoalTasksList[pos]
            dbHelper.deleteGoalTask(task)
        }

        refreshRecyclerView()

    }

}