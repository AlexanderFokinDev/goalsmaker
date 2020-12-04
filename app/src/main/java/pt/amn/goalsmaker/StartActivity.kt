package pt.amn.goalsmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }

    override fun onResume() {

        super.onResume()

        Thread.sleep(1000)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}
