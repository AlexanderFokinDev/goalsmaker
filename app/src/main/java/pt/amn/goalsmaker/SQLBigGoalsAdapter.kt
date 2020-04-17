package pt.amn.goalsmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SQLBigGoalsAdapter (list : List<BigGoalModel>)
    : RecyclerView.Adapter<SQLBigGoalsAdapter.BigGoalsViewHolder> ()
     {

    private var mAllBigGoals : List<BigGoalModel> = list

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class BigGoalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle = itemView.findViewById<TextView>(R.id.row_big_goal_tv_title)
        val tvDescription = itemView.findViewById<TextView>(R.id.row_big_goal_tv_description)
        //cbVisitedCountry = (CheckBox) itemView.findViewById(R.id.country_row_check);
        //ivFlag = (ImageView) itemView.findViewById(R.id.country_row_iv_flag);

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : SQLBigGoalsAdapter.BigGoalsViewHolder {

        val v : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_big_goal, parent, false)

        return BigGoalsViewHolder(v)

    }

    override fun getItemCount(): Int {
        return mAllBigGoals.count()
    }

    override fun onBindViewHolder(holder: SQLBigGoalsAdapter.BigGoalsViewHolder, position: Int) {

        val bigGoal = mAllBigGoals.get(position)

        holder.tvTitle.text = bigGoal.title
        holder.tvDescription.text = bigGoal.description

        /*countriesViewHolder.tvCountry.setText(country.getLocalCountryName());
        countriesViewHolder.cbVisitedCountry.setChecked(country.visited);
        countriesViewHolder.ivFlag.setImageResource(country.flagResId);*/

    }

}