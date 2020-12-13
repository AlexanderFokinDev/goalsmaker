package pt.amn.goalsmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.amn.goalsmaker.databinding.RowTaskBinding
import pt.amn.goalsmaker.models.TaskModel

class SQLGoalTasksAdapter(private var mAllTasks : List<TaskModel>
                          , private val listener : SQLGoalTasksAdapterCallback)
    : RecyclerView.Adapter<SQLGoalTasksAdapter.TasksViewHolder> ()
     {

    interface SQLGoalTasksAdapterCallback {
        fun onDeleteClick(pos : Int)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class TasksViewHolder(private val binding: RowTaskBinding,
                          private val listener : SQLGoalTasksAdapterCallback)
        : RecyclerView.ViewHolder(binding.root) {

            fun onBind(task: TaskModel, position: Int) {
                binding.run {
                    tvRowDescription.text = task.description
                    etRowPoint.setText(task.point.toString())
                    btTaskDelete.setOnClickListener{ listener.onDeleteClick(position) }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : TasksViewHolder {
        return TasksViewHolder(
            RowTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            listener)
    }

    override fun getItemCount(): Int {
        return mAllTasks.size
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.onBind(mAllTasks[position], position)
    }

     fun setItems(list : List<TaskModel>) {
         mAllTasks = list
     }

}