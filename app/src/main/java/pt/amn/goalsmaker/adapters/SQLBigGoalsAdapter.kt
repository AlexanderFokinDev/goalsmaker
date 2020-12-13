package pt.amn.goalsmaker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.amn.goalsmaker.helpers.DBHelper
import pt.amn.goalsmaker.models.BigGoalModel
import pt.amn.goalsmaker.helpers.Utils
import pt.amn.goalsmaker.databinding.RowBigGoalBinding
import pt.amn.goalsmaker.helpers.loadImageWithoutCache

class SQLBigGoalsAdapter (private val listener : SQLBigGoalsAdapterCallback, val context: Context)
    : RecyclerView.Adapter<SQLBigGoalsAdapter.BigGoalsViewHolder> ()
     {
         private var mAllBigGoals : List<BigGoalModel> = listOf()

        interface SQLBigGoalsAdapterCallback {
            fun onBigGoalClick(bigGoal: BigGoalModel)
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        class BigGoalsViewHolder(private val binding : RowBigGoalBinding
            , private val listener: SQLBigGoalsAdapterCallback, val context: Context)
            : RecyclerView.ViewHolder(binding.root) {

                fun onBind(bigGoal : BigGoalModel) {

                    binding.run {
                        tvTitle.text = bigGoal.title
                        tvDescription.text = bigGoal.description

                        ivBigGoal.setOnClickListener {
                            listener.onBigGoalClick(bigGoal)
                        }

                        val imageFile = Utils(context.applicationContext)
                            .getImagePathFromInternalStorage(bigGoal.imagePath)
                        if (imageFile.exists()) {
                            ivBigGoal.loadImageWithoutCache(binding.root, imageFile.path)
                        }
                    }

                }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : BigGoalsViewHolder {
            return BigGoalsViewHolder(RowBigGoalBinding
                .inflate(LayoutInflater.from(parent.context), parent, false), listener
                , context)
        }

        override fun getItemCount(): Int {
            return mAllBigGoals.size
        }

        override fun onBindViewHolder(holder: BigGoalsViewHolder, position: Int) {
            holder.onBind(mAllBigGoals[position])
        }

         fun setItems() {
             mAllBigGoals = DBHelper(context).getAllBigGoals()
         }

}