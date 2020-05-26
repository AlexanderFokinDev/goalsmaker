package pt.amn.goalsmaker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class SQLBigGoalsAdapter (private var mAllBigGoals : List<BigGoalModel>
                          , val listener : SQLBigGoalsAdapterCallback,
                          val context: Context)
    : RecyclerView.Adapter<SQLBigGoalsAdapter.BigGoalsViewHolder> ()
     {

    interface SQLBigGoalsAdapterCallback {
        fun onBigGoalClick(pos : Int)
    }

    //private var listener : SQLBigGoalsAdapterCallback
    //private var mAllBigGoals : List<BigGoalModel> = list
    //private val listener  = listener

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class BigGoalsViewHolder(itemView: View, val listener : SQLBigGoalsAdapterCallback)
        : RecyclerView.ViewHolder(itemView) {

        val tvTitle : TextView = itemView.findViewById(R.id.row_big_goal_tv_title)
        val tvDescription : TextView = itemView.findViewById(R.id.row_big_goal_tv_description)
        //cbVisitedCountry = (CheckBox) itemView.findViewById(R.id.country_row_check);
        val ivGoal : ImageView = itemView.findViewById(R.id.row_big_goal_iv)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : SQLBigGoalsAdapter.BigGoalsViewHolder {

        val v : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_big_goal, parent, false)

        return BigGoalsViewHolder(v, listener)

    }

    override fun getItemCount(): Int {
        return mAllBigGoals.count()
    }

    override fun onBindViewHolder(holder: SQLBigGoalsAdapter.BigGoalsViewHolder, position: Int) {

        val bigGoal = mAllBigGoals.get(position)

        holder.tvTitle.text = bigGoal.title
        holder.tvDescription.text = bigGoal.description

        holder.ivGoal.setOnClickListener(View.OnClickListener {
            listener.onBigGoalClick(position)
        })

        val imageFile = Utils(context.applicationContext)
            .getImagePathFromInternalStorage(bigGoal.imagePath)
        if (imageFile.exists())
            Glide.with(context)
                .load(imageFile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.ivGoal)

        /*countriesViewHolder.tvCountry.setText(country.getLocalCountryName());
        countriesViewHolder.cbVisitedCountry.setChecked(country.visited);
        countriesViewHolder.ivFlag.setImageResource(country.flagResId);*/

    }

     fun setItems(list : List<BigGoalModel>) {
         mAllBigGoals = list
     }

}