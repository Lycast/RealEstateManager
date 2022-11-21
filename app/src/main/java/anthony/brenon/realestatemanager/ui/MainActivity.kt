package anthony.brenon.realestatemanager.ui


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.ActivityMainBinding
import anthony.brenon.realestatemanager.repository.EstateRepository
import anthony.brenon.realestatemanager.utils.Utils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var textViewMain: TextView
    private lateinit var textViewQuantity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val repository = EstateRepository()
        val factory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        // the link was wrong, I modified it
        textViewMain = findViewById(R.id.activity_main_activity_text_view_main)
        textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity)

        this.configureTextViewMain()
        this.configureTextViewQuantity()
    }

    private fun configureTextViewMain() {
        textViewMain.textSize = 15f
        textViewMain.text = "Le premier bien immobilier enregistré vaut "
    }

    private fun configureTextViewQuantity() {
        val quantity = Utils.convertDollarToEuro(100)
        textViewQuantity.textSize = 20f
        // you can't put a int into a text view we need to format that
        textViewQuantity.text = quantity.toString()
    }
}