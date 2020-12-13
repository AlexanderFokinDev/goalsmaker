package pt.amn.goalsmaker.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pt.amn.goalsmaker.models.BigGoalModel
import pt.amn.goalsmaker.R
import pt.amn.goalsmaker.fragments.BigGoalDoneTodayFragment
import pt.amn.goalsmaker.fragments.BigGoalMainFragment
import pt.amn.goalsmaker.fragments.BigGoalTasksFragment

class TabBigGoalPagerFragmentAdapter(context : Context, fm : FragmentManager
                                     , private val isNew : Boolean, private val goal : BigGoalModel)
    : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabs =  if(isNew) listOf(context.getString(R.string.tab_item_big_goal_main)
            , context.getString(R.string.tab_item_big_goal_tasks))
            else listOf(context.getString(R.string.tab_item_big_goal_main)
            , context.getString(R.string.tab_item_big_goal_tasks)
            , context.getString(R.string.tab_item_big_goal_done_today))


    override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> BigGoalMainFragment(isNew, goal)
                1 -> BigGoalTasksFragment(isNew, goal)
                2 -> BigGoalDoneTodayFragment(isNew, goal)
                else -> Fragment()
            }

    override fun getCount(): Int {
                return tabs.size
            }

    override fun getPageTitle(position: Int): CharSequence? {
                return tabs[position]
            }

}