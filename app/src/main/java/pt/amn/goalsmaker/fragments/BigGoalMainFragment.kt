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
import pt.amn.goalsmaker.*
import pt.amn.goalsmaker.adapters.SQLGoalIndicatorsAdapter
import pt.amn.goalsmaker.databinding.FragmentBigGoalMainBinding
import pt.amn.goalsmaker.helpers.DBHelper
import pt.amn.goalsmaker.helpers.loadImageWithCache
import pt.amn.goalsmaker.models.BigGoalModel
import pt.amn.goalsmaker.models.GoalIndicatorModel
import java.util.*
import kotlin.collections.ArrayList

class BigGoalMainFragment(private var isNew : Boolean, private val goal : BigGoalModel)
    : Fragment()
    , SQLGoalIndicatorsAdapter.SQLGoalIndicatorsAdapterCallback{

    private var _binding : FragmentBigGoalMainBinding? = null
    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    private var selectedImage : Uri? = null
    private lateinit var dbHelper : DBHelper
    private lateinit var mGoalIndicatorsList : List<GoalIndicatorModel>
    private lateinit var adapter : SQLGoalIndicatorsAdapter
    private lateinit var mGoalIndicatorsListForNewGoal : ArrayList<GoalIndicatorModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBigGoalMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DBHelper(context!!)
        mGoalIndicatorsList = listOf()
        adapter = SQLGoalIndicatorsAdapter(mGoalIndicatorsList, this, requireContext())

        initializationView()

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
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
                binding.ivGoal.loadImageWithCache(binding.root, selectedImage)
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }

    }

    private fun initializationView() {

        binding.run {

            rvIndicators.adapter = adapter

            ivGoal.setOnClickListener {
                selectImageInGallery()
            }

            btSave.setOnClickListener {
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

                    mGoalIndicatorsListForNewGoal.forEach { goalIndicatorModel ->
                        goalIndicatorModel.bigGoalId = goal.id
                        dbHelper.addGoalIndicator(goalIndicatorModel)
                    }

                } else {
                    dbHelper.updateBigGoal(goal)
                }

                isNew = false

                Toast.makeText(context, getString(R.string.info_message_save_goal)
                    , Toast.LENGTH_SHORT).show()
            }

            btAddIndicator.setOnClickListener {
                if(etIndicator.text.isEmpty()) {
                    Toast.makeText(context, getString(R.string.error_message_empty_indicator)
                        , Toast.LENGTH_SHORT).show()
                } else {
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
                }

                refreshRecyclerView()
            }

            if (!isNew) {

                etTitle.setText(goal.title)
                etDescription.setText(goal.description)

                val imageFile =
                    Utils(context?.applicationContext).getImagePathFromInternalStorage(goal.imagePath)
                if (imageFile.exists()) {
                    ivGoal.loadImageWithCache(root, imageFile.path)
                }

            } else {

                mGoalIndicatorsListForNewGoal = ArrayList<GoalIndicatorModel>()
            }
        }

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

    private fun selectImageInGallery() {
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