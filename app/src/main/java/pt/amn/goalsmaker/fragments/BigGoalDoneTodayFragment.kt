package pt.amn.goalsmaker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_big_goal_done_today.*
import pt.amn.goalsmaker.helpers.DBHelper
import pt.amn.goalsmaker.models.BigGoalModel
import pt.amn.goalsmaker.helpers.Utils
import pt.amn.goalsmaker.adapters.SQLTasksDoneTodayAdapter
import pt.amn.goalsmaker.databinding.FragmentBigGoalDoneTodayBinding
import pt.amn.goalsmaker.models.CompletedTaskModel

class BigGoalDoneTodayFragment(var isNew : Boolean, private val goal : BigGoalModel) : Fragment()
    , SQLTasksDoneTodayAdapter.SQLTasksDoneTodayAdapterCallback{

    private var _binding : FragmentBigGoalDoneTodayBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    lateinit var dbHelper : DBHelper
    lateinit var mTasksList : List<CompletedTaskModel>
    lateinit var adapter : SQLTasksDoneTodayAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBigGoalDoneTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(context!!)
        mTasksList = listOf()
        adapter = SQLTasksDoneTodayAdapter(mTasksList, this)
        binding.rvCompletedTasks.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        refreshRecyclerView()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun refreshRecyclerView() {

        if (!isNew) {
            mTasksList = dbHelper.getCompletedTasksOfDay(goal, Utils(context).getCurrentDayAsLong())
            adapter.setItems(mTasksList)
        }

        adapter.notifyDataSetChanged()

        // Total points
        var total = 0
        for (task in mTasksList) {
            total += task.points
        }
        tvTotalPoints.text = total.toString()

    }

    override fun onAddClick(pos: Int) {
        val dayTask = mTasksList[pos]
        dayTask.quantity++
        dbHelper.updateCompletedTask(dayTask, Utils(context).getCurrentDayAsLong())

        refreshRecyclerView()
    }

    override fun onSubtractClick(pos: Int) {
        val dayTask = mTasksList[pos]
        if (dayTask.quantity > 0) dayTask.quantity--
        dbHelper.updateCompletedTask(dayTask, Utils(context).getCurrentDayAsLong())

        refreshRecyclerView()
    }

}