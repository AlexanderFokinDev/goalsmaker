package pt.amn.goalsmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.amn.goalsmaker.databinding.RowDoneTodayBinding
import pt.amn.goalsmaker.models.CompletedTaskModel

class SQLTasksDoneTodayAdapter(private var mAllTasks : List<CompletedTaskModel>
                               , private val listener : SQLTasksDoneTodayAdapterCallback)
    : RecyclerView.Adapter<SQLTasksDoneTodayAdapter.TasksDoneTodayViewHolder> ()
     {

    interface SQLTasksDoneTodayAdapterCallback {
        fun onAddClick(pos : Int)
        fun onSubtractClick(pos : Int)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class TasksDoneTodayViewHolder(private val binding: RowDoneTodayBinding,
                                   private val listener: SQLTasksDoneTodayAdapterCallback)
        : RecyclerView.ViewHolder(binding.root) {

        fun onBind(task: CompletedTaskModel, position: Int) {
            binding.run {
                tvTask.text = task.taskName
                tvQuantity.text = task.quantity.toString()
                tvTotal.text = task.points.toString()
                btSubtract.setOnClickListener() { listener.onSubtractClick(position) }
                btAdd.setOnClickListener() { listener.onAddClick(position) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : TasksDoneTodayViewHolder {
        return TasksDoneTodayViewHolder(
            RowDoneTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener)
    }

    override fun getItemCount(): Int {
        return mAllTasks.size
    }

    override fun onBindViewHolder(holder: TasksDoneTodayViewHolder, position: Int) {
        holder.onBind(mAllTasks[position], position)
    }

     fun setItems(list : List<CompletedTaskModel>) {
         mAllTasks = list
     }

}