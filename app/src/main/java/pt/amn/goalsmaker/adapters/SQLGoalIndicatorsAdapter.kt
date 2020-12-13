package pt.amn.goalsmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.amn.goalsmaker.models.GoalIndicatorModel
import pt.amn.goalsmaker.databinding.RowGoalIndicatorBinding

class SQLGoalIndicatorsAdapter(private var mAllGoalIndicators : List<GoalIndicatorModel>
                               , private val listener : SQLGoalIndicatorsAdapterCallback,
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
    class GoalIndicatorsViewHolder(private val binding : RowGoalIndicatorBinding,
                                   private val listener : SQLGoalIndicatorsAdapterCallback)
        : RecyclerView.ViewHolder(binding.root) {

            fun onBind(goalIndicator : GoalIndicatorModel, position : Int) {
                binding.run {
                    tvIndicator.text = goalIndicator.description

                    cbCheck.isChecked = goalIndicator.done.compareTo(0) != 0
                    cbCheck.setOnCheckedChangeListener { _, _ ->
                        listener.onCheckClick(position)
                    }

                    ibDelete.setOnClickListener { listener.onDeleteClick(position) }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : GoalIndicatorsViewHolder {
        return GoalIndicatorsViewHolder(
            RowGoalIndicatorBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener
        )
    }

    override fun getItemCount(): Int {
        return mAllGoalIndicators.size
    }

    override fun onBindViewHolder(holder: GoalIndicatorsViewHolder, position: Int) {
        holder.onBind(mAllGoalIndicators[position], position)
    }

     fun setItems(list : List<GoalIndicatorModel>) {
         mAllGoalIndicators = list
     }

}