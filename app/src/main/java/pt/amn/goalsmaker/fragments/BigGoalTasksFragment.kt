package pt.amn.goalsmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import pt.amn.goalsmaker.helpers.DBHelper
import pt.amn.goalsmaker.models.BigGoalModel
import pt.amn.goalsmaker.R
import pt.amn.goalsmaker.adapters.SQLGoalTasksAdapter
import pt.amn.goalsmaker.databinding.FragmentBigGoalTasksBinding
import pt.amn.goalsmaker.models.TaskModel

class BigGoalTasksFragment(var isNew : Boolean, val goal : BigGoalModel) : Fragment()
        , SQLGoalTasksAdapter.SQLGoalTasksAdapterCallback {

    private var _binding : FragmentBigGoalTasksBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    lateinit var dbHelper : DBHelper
    lateinit var mGoalTasksList : List<TaskModel>
    lateinit var adapter : SQLGoalTasksAdapter
    lateinit var mGoalTasksListForNewGoal : ArrayList<TaskModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBigGoalTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(context!!)
        mGoalTasksList = listOf()
        adapter = SQLGoalTasksAdapter(mGoalTasksList, this)

        initializationView()

    }

    override fun onResume() {
        super.onResume()
        refreshRecyclerView()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initializationView() {

        binding.run {

            rvTasks.adapter = adapter

            btAddTask.setOnClickListener {
                if (etTask.text.isEmpty()) {
                    Toast.makeText(
                        context, getString(R.string.error_message_empty_task), Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val newTask = TaskModel()
                    newTask.description = etTask.text.toString()
                    newTask.point = Integer.parseInt(etPoint.text.toString())

                    if (isNew) {
                        newTask.bigGoalId = 0
                        mGoalTasksListForNewGoal.add(newTask)
                    } else {
                        newTask.bigGoalId = goal.id
                        dbHelper.addGoalTask(newTask)
                    }

                    etTask.text.clear()
                    etPoint.text.clear()

                    refreshRecyclerView()
                }
            }
        }

        if (isNew) {
            mGoalTasksListForNewGoal = ArrayList()
        }
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