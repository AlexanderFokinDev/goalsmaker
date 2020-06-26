package pt.amn.goalsmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import pt.amn.goalsmaker.models.GoalIndicatorModel
import pt.amn.goalsmaker.R

class SQLGoalIndicatorsAdapter(private var mAllGoalIndicators : List<GoalIndicatorModel>
                               , val listener : SQLGoalIndicatorsAdapterCallback,
                               val context: Context)
    : RecyclerView.Adapter<SQLGoalIndicatorsAdapter.GoalIndicatorsViewHolder> ()
     {

    interface SQLGoalIndicatorsAdapterCallback {
        fun onCheckClick(pos : Int)
        fun onDeleteClick(pos : Int)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class GoalIndicatorsViewHolder(itemView: View, val listener : SQLGoalIndicatorsAdapterCallback)
        : RecyclerView.ViewHolder(itemView) {

        val tvDescription : TextView = itemView.findViewById(R.id.goal_indicators_tv_indicator)
        val cbDone : CheckBox = itemView.findViewById(R.id.goal_indicators_check)
        val btDelete : ImageButton = itemView.findViewById(R.id.goal_indicators_delete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : GoalIndicatorsViewHolder {

        val v : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_goal_indicator, parent, false)

        return GoalIndicatorsViewHolder(
            v,
            listener
        )

    }

    override fun getItemCount(): Int {
        return mAllGoalIndicators.count()
    }

    override fun onBindViewHolder(holder: GoalIndicatorsViewHolder, position: Int) {

        val goalIndicator = mAllGoalIndicators.get(position)

        holder.tvDescription.text = goalIndicator.description

        holder.cbDone.isChecked = if(goalIndicator.done.compareTo(0) == 0) false else true
        holder.cbDone.setOnCheckedChangeListener() {buttonView, isChecked ->
            listener.onCheckClick(position)
        }

        holder.btDelete.setOnClickListener() {v: View? ->  listener.onDeleteClick(position)}

    }

     fun setItems(list : List<GoalIndicatorModel>) {
         mAllGoalIndicators = list
     }

}