package pt.amn.goalsmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_big_goal_main.view.*
import kotlinx.android.synthetic.main.row_task.view.*
import pt.amn.goalsmaker.R
import pt.amn.goalsmaker.models.TaskModel

class SQLGoalTasksAdapter(private var mAllTasks : List<TaskModel>
                          , private val listener : SQLGoalTasksAdapterCallback,
                          val context: Context)
    : RecyclerView.Adapter<SQLGoalTasksAdapter.TasksViewHolder> ()
     {

    interface SQLGoalTasksAdapterCallback {
        fun onDeleteClick(pos : Int)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class TasksViewHolder(itemView: View, val listener : SQLGoalTasksAdapterCallback)
        : RecyclerView.ViewHolder(itemView) {

        val tvDescription : TextView = itemView.tvRowDescription
        val etPoint : EditText = itemView.etRowPoint
        val btTaskDelete : ImageButton = itemView.btTaskDelete

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : TasksViewHolder {

        val v : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_task, parent, false)

        return TasksViewHolder(
            v,
            listener
        )

    }

    override fun getItemCount(): Int {
        return mAllTasks.count()
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {

        val task = mAllTasks.get(position)

        holder.tvDescription.text = task.description
        holder.etPoint.setText(task.point.toString())
        holder.btTaskDelete.setOnClickListener() {v: View? ->  listener.onDeleteClick(position)}

    }

     fun setItems(list : List<TaskModel>) {
         mAllTasks = list
     }

}