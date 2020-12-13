package pt.amn.goalsmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.amn.goalsmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializationView()
    }

    private fun initializationView() {

        binding.run {
            setSupportActionBar(includedToolbar.toolbar)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
