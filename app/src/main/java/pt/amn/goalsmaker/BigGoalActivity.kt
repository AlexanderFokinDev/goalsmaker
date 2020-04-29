package pt.amn.goalsmaker

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*
import java.lang.Exception
import java.nio.file.Path
import java.util.*

class BigGoalActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var ivBigGoal : ImageView
    lateinit var goal : BigGoalModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_goal)

        initializationView()
    }

    private fun initializationView() {

        goal = intent.extras?.get(EXTRA_PARAM_GOAL) as BigGoalModel

        // Action bar
        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = goal.title

        ivBigGoal = findViewById(R.id.big_goal_iv)
        ivBigGoal.setOnClickListener(this)

        val imageFile = Utils(applicationContext).getImagePathFromInternalStorage(goal.id.toString())
        if (imageFile.exists())
            Glide.with(this).load(imageFile).into(ivBigGoal)
    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.big_goal_iv) {
            selectImageInGallery()
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

                val selectedImage = data.data
                Glide.with(this).load(selectedImage).into(ivBigGoal)

                Utils(applicationContext).saveImageToInternalStorage(selectedImage, goal.id.toString())

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
