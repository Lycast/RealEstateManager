package anthony.brenon.realestatemanager.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.ActivityFormBinding
import anthony.brenon.realestatemanager.databinding.ActivityHomeBinding
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.ui.adapter.RecyclerAdapter
import anthony.brenon.realestatemanager.utils.Utils

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var adapter: RecyclerAdapter
    private var estates: List<Estate> = listOf(Estate(), Estate(), Estate())

    private lateinit var textViewMain: TextView
    private lateinit var textViewQuantity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        adapter = RecyclerAdapter(estates)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        //Log.e("TAG", "estates : ${estates.get(0).description}")


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