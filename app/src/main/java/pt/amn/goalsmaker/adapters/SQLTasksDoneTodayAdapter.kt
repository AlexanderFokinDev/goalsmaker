package pt.amn.goalsmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_big_goal_main.view.*
import kotlinx.android.synthetic.main.row_done_today.view.*
import kotlinx.android.synthetic.main.row_task.view.*
import pt.amn.goalsmaker.R
import pt.amn.goalsmaker.models.CompletedTaskModel
import pt.amn.goalsmaker.models.TaskModel

class SQLTasksDoneTodayAdapter(private var mAllTasks : List<CompletedTaskModel>
                               , private val listener : SQLTasksDoneTodayAdapterCallback,
                               val context: Context)
    : RecyclerView.Adapter<SQLTasksDoneTodayAdapter.TasksDoneTodayViewHolder> ()
     {

    interface SQLTasksDoneTodayAdapterCallback {
        fun onAddClick(pos : Int)
        fun onSubtractClick(pos : Int)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class TasksDoneTodayViewHolder(itemView: View, val listener : SQLTasksDoneTodayAdapterCallback)
        : RecyclerView.ViewHolder(itemView) {

        val tvTask : TextView = itemView.tvTask
        val tvQuantity : TextView = itemView.tvQuantity
        val tvTotal : TextView = itemView.tvTotal
        val btSubtract : ImageButton = itemView.btSubtract
        val btAdd : ImageButton = itemView.btAdd

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : TasksDoneTodayViewHolder {

        val v : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_done_today, parent, false)

        return TasksDoneTodayViewHolder(
            v,
            listener
        )

    }

    override fun getItemCount(): Int {
        return mAllTasks.count()
    }

    override fun onBindViewHolder(holder: TasksDoneTodayViewHolder, position: Int) {

        val task = mAllTasks.get(position)

        holder.tvTask.text = task.taskName
        holder.tvQuantity.setText(task.quantity.toString())
        holder.tvTotal.setText(task.points.toString())
        holder.btSubtract.setOnClickListener() {_ ->  listener.onSubtractClick(position)}
        holder.btAdd.setOnClickListener() {_ ->  listener.onAddClick(position)}

    }

     fun setItems(list : List<CompletedTaskModel>) {
         mAllTasks = list
     }

}