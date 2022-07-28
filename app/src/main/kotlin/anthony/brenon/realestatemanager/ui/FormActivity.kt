package anthony.brenon.realestatemanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.ActivityFormBinding
import anthony.brenon.realestatemanager.databinding.ActivityHomeBinding

class FormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}