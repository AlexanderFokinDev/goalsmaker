package pt.amn.goalsmaker

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*
import java.lang.Exception
import java.nio.file.Path
import java.util.*

class BigGoalActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var ivBigGoal : ImageView
    lateinit var goal : BigGoalModel
    lateinit var etTitle : EditText
    lateinit var etDescription : EditText
    lateinit var btSave : Button

    private var selectedImage : Uri? = null

    private val dbHelper = DBHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_goal)

        initializationView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.big_goal_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.big_goal_menu_action_edit) {
            etTitle.isEnabled = true
            etDescription.isEnabled = true
            ivBigGoal.isEnabled = true
            btSave.visibility = View.VISIBLE
        } else if (id == R.id.big_goal_menu_action_delete) {

            val dialogQuestion = AlertDialog.Builder(this)
            dialogQuestion.setTitle(goal.title)
            dialogQuestion.setMessage(getString(R.string.dialog_message_delete_goal))
            dialogQuestion.setPositiveButton("Yes") {dialog, which ->
                dbHelper.deleteBigGoal(goal)
                finish()
            }
            dialogQuestion.setNeutralButton("Cancel"){_,_ ->}

            val dialog : AlertDialog = dialogQuestion.create()
            dialog.show()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initializationView() {

        val extraParamGoal = intent.extras?.get(EXTRA_PARAM_GOAL)

        // Action bar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        ivBigGoal = findViewById(R.id.big_goal_iv)
        ivBigGoal.setOnClickListener(this)

        etTitle = findViewById(R.id.big_goal_et_title)
        etDescription = findViewById(R.id.big_goal_et_description)
        btSave = findViewById(R.id.big_goal_bt_save)
        btSave.setOnClickListener(this)

        if (extraParamGoal != null) {

            goal = extraParamGoal as BigGoalModel

            title = goal.title

            etTitle.setText(title)
            etDescription.setText(goal.description)

            btSave.visibility = View.INVISIBLE
            etTitle.isEnabled = false
            etDescription.isEnabled = false
            ivBigGoal.isEnabled = false

            val imageFile =
                Utils(applicationContext).getImagePathFromInternalStorage(goal.imagePath)
            if (imageFile.exists())
                Glide.with(this)
                    .load(imageFile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivBigGoal)

        } else {
            title = getString(R.string.title_create_new_goal)
            btSave.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.big_goal_iv) {
            selectImageInGallery()
        } else if (v?.id == R.id.big_goal_bt_save) {

            val extraParamGoal = intent.extras?.get(EXTRA_PARAM_GOAL)

            if (extraParamGoal == null) {
                //val testString = "Daily plan\t\t\t\t\t73%\nWeekly plan\t\t\t\t20%\nMontly plan\t\t\t\t84%"
                goal = BigGoalModel()
            }

            goal.title = etTitle.text.toString()
            goal.description = etDescription.text.toString()

            // save new file
            if (selectedImage != null) {
                goal.imagePath = UUID.randomUUID().toString()
                Utils(applicationContext).saveImageToInternalStorage(
                    selectedImage,
                    goal.imagePath
                )
            }

            // TODO: delete this row later
            // for test
            //dbHelper.deleteAllBigGoals()

            if (extraParamGoal == null) {
                dbHelper.addBigGoal(goal)
            } else {
                dbHelper.updateBigGoal(goal)
            }

            /*Toast.makeText(this, "Add big goal " + goal.title, Toast.LENGTH_LONG)
                .show()*/

            finish()
        }
    }

    fun selectImageInGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
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
                    .into(ivBigGoal)

                // save new file
                //Utils(applicationContext).saveImageToInternalStorage(selectedImage, goal.id.toString())

            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val EXTRA_PARAM_GOAL = "extra_param_goal"
        const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
}
