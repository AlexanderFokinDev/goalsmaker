package pt.amn.goalsmaker.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_big_goal_main.*
import pt.amn.goalsmaker.*
import pt.amn.goalsmaker.adapters.SQLGoalIndicatorsAdapter
import pt.amn.goalsmaker.models.BigGoalModel
import pt.amn.goalsmaker.models.GoalIndicatorModel
import java.util.*
import kotlin.collections.ArrayList

class BigGoalMainFragment(var isNew : Boolean, val goal : BigGoalModel)
    : Fragment()
    , View.OnClickListener
    , SQLGoalIndicatorsAdapter.SQLGoalIndicatorsAdapterCallback{

    private var selectedImage : Uri? = null
    lateinit var dbHelper : DBHelper
    lateinit var mGoalIndicatorsList : List<GoalIndicatorModel>
    lateinit var adapter : SQLGoalIndicatorsAdapter
    lateinit var mGoalIndicatorsListForNewGoal : ArrayList<GoalIndicatorModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_big_goal_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(context!!)
        mGoalIndicatorsList = listOf<GoalIndicatorModel>()
        adapter = SQLGoalIndicatorsAdapter(mGoalIndicatorsList, this, requireContext())

        initializationView()
        initializationRecyclerView()

    }

    override fun onResume() {
        super.onResume()

        refreshRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM && resultCode == Activity.RESULT_OK
                && data != null) {

                selectedImage = data.data
                Glide.with(this)
                    .load(selectedImage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivGoal)

            }
        } catch (e : Exception) {
            e.printStackTrace()
        }

    }

    private fun initializationView() {

        ivGoal.setOnClickListener(this)
        btSave.setOnClickListener(this)
        btAddIndicator.setOnClickListener(this)

        if (!isNew) {

            etTitle.setText(goal.title)
            etDescription.setText(goal.description)

            // To do disabled some elements
            //etTitle.isEnabled = false
            //etDescription.isEnabled = false
            //ivGoal.isEnabled = false

            //btSave.visibility = View.INVISIBLE
            //etIndicator.visibility = View.INVISIBLE
            //btAddIndicator.visibility = View.INVISIBLE

            val imageFile =
                Utils(context?.applicationContext).getImagePathFromInternalStorage(goal.imagePath)
            if (imageFile.exists())
                Glide.with(this)
                    .load(imageFile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivGoal)

        } else {

            mGoalIndicatorsListForNewGoal = ArrayList<GoalIndicatorModel>()

            //btSave.visibility = View.VISIBLE
            //etIndicator.visibility = View.VISIBLE
            //btAddIndicator.visibility = View.VISIBLE
        }
    }

    private fun initializationRecyclerView() {
        rvIndicators.layoutManager = LinearLayoutManager(context
            , LinearLayoutManager.VERTICAL, false)
        rvIndicators.adapter = adapter
    }

    private fun refreshRecyclerView() {

        if (!isNew) {
            mGoalIndicatorsList = dbHelper.getGoalIndicators(goal)
            adapter.setItems(mGoalIndicatorsList)
        } else {
            adapter.setItems(mGoalIndicatorsListForNewGoal)
        }

        adapter.notifyDataSetChanged()

    }

    override fun onClick(v: View?) {

           if (v?.id == R.id.ivGoal) {
               selectImageInGallery()
           } else if (v?.id == R.id.btSave) {

               goal.title = etTitle.text.toString()
               goal.description = etDescription.text.toString()

               // save new file
               if (selectedImage != null) {
                   goal.imagePath = UUID.randomUUID().toString()
                   Utils(context?.applicationContext).saveImageToInternalStorage(
                       selectedImage,
                       goal.imagePath
                   )
               }

               if (isNew) {
                   dbHelper.addBigGoal(goal)

                   mGoalIndicatorsListForNewGoal.forEach {
                       it.bigGoalId = goal.id
                       dbHelper.addGoalIndicator(it)
                   }

               } else {
                   dbHelper.updateBigGoal(goal)
               }

               isNew = false

               Toast.makeText(context, getString(R.string.info_message_save_goal)
                   , Toast.LENGTH_SHORT).show()

               //finish()

           } else if (v?.id == R.id.btAddIndicator) {

               if(etIndicator.text.isEmpty()) {
                   Toast.makeText(context, getString(R.string.error_message_empty_indicator)
                       , Toast.LENGTH_SHORT).show()
                   return
               }

               val newGoalIndicator =
                   GoalIndicatorModel()
               newGoalIndicator.description = etIndicator.text.toString()
               newGoalIndicator.done = 0

               if (isNew) {
                   newGoalIndicator.bigGoalId = 0
                   mGoalIndicatorsListForNewGoal.add(newGoalIndicator)
               } else {
                   newGoalIndicator.bigGoalId = goal.id
                   dbHelper?.addGoalIndicator(newGoalIndicator)
               }

               etIndicator.text.clear()

               refreshRecyclerView()

           }
    }

    fun selectImageInGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val packageManager = context?.packageManager
        if (intent.resolveActivity(packageManager!!) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }

    // Interfaces

    override fun onCheckClick(pos: Int) {

        // unsaved goal
        if (isNew) {
            val indicator = mGoalIndicatorsListForNewGoal[pos]
            indicator.done = if (indicator.done > 0) 0 else 1
        } else {
            val indicator = mGoalIndicatorsList[pos]
            indicator.done = if (indicator.done > 0) 0 else 1
            dbHelper.updateGoalIndicator(indicator)
        }

    }

    override fun onDeleteClick(pos: Int) {

        if (isNew) {
            val indicator = mGoalIndicatorsListForNewGoal[pos]
            mGoalIndicatorsListForNewGoal.remove(indicator)
        } else {
            val indicator = mGoalIndicatorsList[pos]
            dbHelper.deleteGoalIndicator(indicator)
        }

        refreshRecyclerView()
    }

    companion object {
        const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
}