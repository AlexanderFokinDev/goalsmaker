package pt.amn.goalsmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        initializationView()
    }

    private fun initializationView() {
        /*val btTest: Button = findViewById(R.id.btTest)
        btTest.setOnClickListener(this)*/
    }

    override fun onResume() {

        super.onResume()

        Thread.sleep(2000)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun onClick(v: View?) {
        /*if (v?.id == R.id.btTest) {
            Toast.makeText(this, "Test", Toast.LENGTH_LONG)
                .show()
        }*/
    }
}
